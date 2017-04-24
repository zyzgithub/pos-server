package com.courier_mana.personal.service.impl;

import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.personal.service.CourierMyInfoService;
import com.wm.entity.user.WUserEntity;


@Service
public class CourierMyInfoServiceImpl extends CommonServiceImpl implements CourierMyInfoService {
	private static final Logger logger = LoggerFactory.getLogger(CourierMyInfoServiceImpl.class);
	
	@Override
	public WUserEntity getWUserEntity(Integer id) {
		if(id == null){
			throw new IllegalArgumentException("id=null");
		}
		WUserEntity user = get(WUserEntity.class,id);
		if(user == null){
			logger.error("无法根据id=" + id + "得到用户信息");
		}
		return user;
	} 
	
	@Override
	public Map<String, Object> getMyInfo(Integer id) {
		if(id == null){
			throw new IllegalArgumentException("id=null");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select u.nickname, u.mobile, u.photoUrl, ci.job_id jobId, p.name positionName, p.sort_order positionGrade from user u ");
		sql.append(" left join 0085_courier_info ci on ci.courier_id = u.id ");
		sql.append(" left join 0085_courier_position cp on cp.courier_id = u.id ");
		sql.append(" left join 0085_position p on p.id = cp.position_id ");
		sql.append(" where u.id = ? ");
		return findOneForJdbc(sql.toString(), id);
	} 
	
	@Override
	public boolean updatePassword(int courierId, String newPassword) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update user set password = '"+ newPassword +"' where id = '"+ courierId +"'; ");
		int i = updateBySqlString(sql.toString());
		return i==1?true:false;
	}
	
	@Override
	public boolean isAgentUser(Integer userId) {
		/**
		 * 检查参数
		 */
		if(userId == null){
			throw new IllegalArgumentException("userId不能为空");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT user_type ");
		sql.append(" FROM `user` ");
		sql.append(" WHERE id = ");
		sql.append(userId);
		
		String userType = this.findOneForJdbc(sql.toString(), String.class);
		
		/**
		 * 判断用户类型
		 */
		if("agent".equals(userType)){
			return true;
		}
		return false;
	}
}
