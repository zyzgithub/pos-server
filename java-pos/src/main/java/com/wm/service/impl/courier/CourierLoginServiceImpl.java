package com.wm.service.impl.courier;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.courier.CourierLoginService;


@Service
@Transactional
public class CourierLoginServiceImpl extends CommonServiceImpl implements CourierLoginService {
	private static final Logger logger = LoggerFactory.getLogger(CourierLoginServiceImpl.class);
	
	/**
	 * 查询是否存在该手机号的管理员
	 */
	@Override
	public WUserEntity getUserByMobile(String mobile) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("mobile", mobile);
		cq.eq("userType", "courier");
		cq.add();
		List<WUserEntity> users = getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}
	
	/**
	 * 重置密码
	 */
	@Override
	public boolean resetPassword(int userId,String newPassword) {
		WUserEntity user = getEntity(WUserEntity.class, userId);
		user.setPassword(newPassword);
		this.saveOrUpdate(user);
		return true;
	}
	
	/**
	 * 验证管理员是否存在中文名与密码，存在多条记录重复的情况
	 * @param username
	 * @param password
	 * @param type
	 * @return
	 */
	@Override
	public Long getCountByNameAndPwd(String username, String password) {
		if(username == null || password == null){
			throw new IllegalArgumentException("username=null || password=null");
		}
		Object[] objs = new Object[2];
		objs[0] = username;
		objs[1] = password;
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) from user u ");
		sql.append(" where u.username = ? and u.password = ? and user_type='courier' ");
		return getCountForJdbcParam(sql.toString(), objs);
	} 
	
	/**
	 * 管理员登录，并返回用户信息
	 */
	@Override
	public Map<String, Object> courierLogin(String username, String password) {
		if(username == null || password == null){
			throw new IllegalArgumentException("username=null || password=null");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select u.id, u.username, u.nickname, u.mobile, u.photoUrl, ci.job_id jobId, p.name positionName, p.sort_order positionGrade from user u ");
		sql.append(" left join 0085_courier_info ci on ci.courier_id = u.id ");
		sql.append(" left join 0085_courier_position cp on cp.courier_id = u.id ");
		sql.append(" left join 0085_position p on p.id = cp.position_id ");
		sql.append(" where (u.username = ? or u.mobile=?) and u.password = ? and u.user_type='courier' and cp.position_id>2 ");
		return findOneForJdbc(sql.toString(), username, username, password);
	} 
	
	/**
	 * 创建管理员登录记录
	 */
	@Override
	public void createUserLogin(UserloginEntity userlogin){
		saveOrUpdate(userlogin);
	}
}
