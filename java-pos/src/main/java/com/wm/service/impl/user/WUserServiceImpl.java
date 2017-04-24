package com.wm.service.impl.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.core.util.StringUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.VO.UploadFileVO;
import com.base.util.FileUtil;
import com.sun.star.uno.RuntimeException;
import com.wm.dao.user.UserDao;
import com.wm.entity.user.NewAndOldUserVo;
import com.wm.entity.user.WUserEntity;
import com.wm.service.card.CardServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.service.user.CustTypeRuleServiceI;
import com.wm.service.user.WUserServiceI;

@Service("wUserService")
@Transactional
public class WUserServiceImpl extends CommonServiceImpl implements WUserServiceI {
	
	private final static Logger logger = LoggerFactory.getLogger(WUserServiceImpl.class);

	private static final String PHOTO_PRE_PATH = "image/user";
	private static final String getUserOrders = "select count(0) from `order` where user_id=?";
	private static final String reconciliationSql = "select id,pre_money,post_money from flow "
			+ " where user_id=? and date(FROM_UNIXTIME(create_time))>=? "
			+ " and date(FROM_UNIXTIME(create_time))<=? order by create_time desc";

	@Autowired
	private CardServiceI cardService;
	
	@Autowired
	private CustTypeRuleServiceI custTypeRuleService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrgServiceI orgServiceI;

	
	
	
	@Override
	public String uploadUserPhoto(int userId, UploadFileVO file, String rootPath) {
		// 文件上传路径
		String path = rootPath + File.separator + PHOTO_PRE_PATH
				+ File.separator + userId;
		// 随机文件名
		String tempFileName = System.currentTimeMillis() + "."
				+ FileUtils.getExtend(file.getName());
		// 全路径
		String fullPathFileName = File.separator + PHOTO_PRE_PATH
				+ File.separator + userId + File.separator + tempFileName;
		// 保存文件
		FileUtil.write(path, file.decodePicBase64(), tempFileName, false);

		WUserEntity wuser = this.get(WUserEntity.class, userId);
		if (StringUtil.isNotEmpty(wuser.getPhotoUrl())) {
			// 删除原头像文件
			FileUtil.delFile(rootPath + wuser.getPhotoUrl());
		}
		// 保存新头像URL
		wuser.setPhotoUrl(fullPathFileName.replaceAll("\\\\", "/"));
		this.saveOrUpdate(wuser);
		return fullPathFileName.replaceAll("\\\\", "/");
	}
	
	private enum UserType {

		USER(0, "user"), 
		
		MERCHANT(1, "merchant"),
		
		COURIER(2, "courier"),
		
		WAREHOUSE(3, "warehouse"),
		SUPPLIER(5, "supplier"),
		AGENT(6, "agent");

		private int code;

		private String type;

		private UserType(int code, String type) {
			this.code = code;
			this.type = type;
		}

		public int getCode() {
			return code;
		}

		public String getType() {
			return type;
		}

	}

	@Override
	public WUserEntity userLogin(String username, String password, String type) {
		String typeString = null;
		for (UserType userType : UserType.values()) {
			if (String.valueOf(userType.getCode()).equals(type)) {
				typeString = userType.getType();
				break;
			}
		}
		if (typeString == null) {
			throw new RuntimeException("找不到对应的用户类型数据" + type);
		}
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.or(Restrictions.in("username", new Object[] { username }), Restrictions.in("mobile", new Object[] { username }));
		cq.eq("password", password);
		cq.eq("userType", typeString);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	/**
	 * 验证管理员是否存在中文名与密码，存在多条记录重复的情况
	 * @param username
	 * @param password
	 * @param type
	 * @author SUYUQIANG
	 * @return
	 */
	@Override
	public Long getCountByNameAndPwd(String username, String password) {
		Object[] objs = new Object[2];
		objs[0] = username;
		objs[1] = password;
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) from user u ");
		sql.append(" where u.username = ? and u.password = ? and user_type='courier' ");
		return getCountForJdbcParam(sql.toString(), objs);
	} 
	
	/**
	 * 查询快递员的职位
	 * @author SUYUQIANG
	 */
	@Override
	public Long getCourierPosition(Integer userId) {
		Object[] objs = new Object[1];
		objs[0] = userId;
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) from 0085_courier_position ");
		sql.append(" where courier_id = ? and position_id > 2 ");
		return getCountForJdbcParam(sql.toString(), objs);
	} 

	@Override
	public WUserEntity authLogin(String sns) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("sns", sns);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public boolean updateUsername(int userId, String username) {
		List<WUserEntity> list = this.findByProperty(WUserEntity.class, "username", username);
		if (list != null && list.size() > 0) {
			return false;
		}
		WUserEntity user = this.getEntity(WUserEntity.class, userId);
		user.setUsername(username);
		this.saveOrUpdate(user);

		return true;
	}

	public boolean createUser(WUserEntity user) {

		this.saveOrUpdate(user);
		return false;
	}

	@Override
	public AjaxJson AuthRegister(HttpServletRequest request, String username,
			String sns, String gender, String password) {

		AjaxJson j = new AjaxJson();
		WUserEntity wuser = new WUserEntity();
		wuser.setUsername(username);
		wuser.setNickname(username);
		wuser.setGender(gender);
		wuser.setSns(sns);
		wuser.setPassword(password);
		wuser.setUserType("user");
		wuser.setCreateTime(DateUtils.getSeconds());
		wuser.setLoginTime(DateUtils.getSeconds());
		wuser.setIp(request.getRemoteAddr());
		wuser.setMoney(0.0);
		wuser.setScore(0);
		this.save(wuser);

		j.setObj(wuser);
		if (wuser.getId() == null || wuser.getId() == 0) {
			j.setSuccess(false);
			j.setMsg("获取帐号失败");
			j.setStateCode("01");
		} else {
			cardService.registerCard(wuser.getId());// 注册送代金券
			j.setSuccess(true);
			j.setMsg("获取账号成功");
			j.setStateCode("00");
		}

		return j;
	}

	@Override
	public AjaxJson userRegister(HttpServletRequest request, String username,
			String mobile, String password) {
		AjaxJson j = new AjaxJson();
		WUserEntity wuser = new WUserEntity();
		wuser.setUsername(username);
		wuser.setNickname(username);
		wuser.setPassword(password);
		wuser.setUserType("user");
		wuser.setCreateTime(DateUtils.getSeconds());
		wuser.setLoginTime(DateUtils.getSeconds());
		wuser.setIp(request.getRemoteAddr());
		wuser.setMoney(0.0);
		wuser.setScore(0);
		wuser.setMobile(mobile);
		this.save(wuser);

		j.setObj(wuser);
		if (wuser.getId() == null || wuser.getId() == 0) {
			j.setSuccess(false);
			j.setMsg("注册帐号失败");
			j.setStateCode("01");
		} else {
			cardService.registerCard(wuser.getId());// 注册送代金券
			j.setSuccess(true);
			j.setMsg("注册账号成功");
			j.setStateCode("00");
		}

		return j;
	}

	@Override
	public WUserEntity merchantLogin(String username) {
		String usertype = "merchant";
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.or(Restrictions.in("username", new Object[] { username }),
				Restrictions.in("mobile", new Object[] { username }));
		cq.eq("userType", usertype);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public WUserEntity getUserByUserNameOrMobile(String userNameOrMobile,
			String userType) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.or(Restrictions.in("username", new Object[] { userNameOrMobile }),
				Restrictions.in("mobile", new Object[] { userNameOrMobile }));
		cq.eq("userType", userType);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public WUserEntity getUserByMobile(String mobile, String userType) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("mobile", mobile);
		cq.eq("userType", userType);
		cq.eq("isDelete", 0);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}
	@Override
	public WUserEntity getUserByAliAcNo(String aliAcNo, String userType) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("aliAcNo", aliAcNo);
		cq.eq("userType", userType);
		cq.eq("isDelete", 0);
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);
		
		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}
	
	@Override
	public Integer updateUnionIdByOpenId(String openId, String unionId,
			String userType) {
		String sql = "update `user` set  unionid = ? where openid = ? and USER_TYPE = ? ";
		return this.commonDao.executeSql(sql, unionId, openId, userType);
	}

	@Override
	public Integer updateOpenIdByUnionId(String unionId, String openId,
			String userType) {
		String sql = "update `user` set  openid = ? where unionid = ? and USER_TYPE = ? ";
		return this.commonDao.executeSql(sql, openId, unionId, userType);
	}

	@Override
	public Boolean checkedUnion(String openId) {
		String sql = "select count(1) from `user` where openid = ? and (unionid is null or unionid = '' ) ";
		List<Object> params = new ArrayList<Object>();
		params.add(openId);
		Long count = this.commonDao.getCountForJdbcParam(sql, params.toArray());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> findNullUniondWhenNotNullOpenId() {
		String sql = "select openId from `user` where (openid is not null and openid !='' ) and openid like 'ow1%' and (unionid is null or unionid = '' ) ";
		List<Map<String, Object>> list = this.commonDao.findForJdbc(sql);
		if (list != null && list.size() > 0) {
			List<String> tempList = new ArrayList<String>();
			for (Map<String, Object> map : list) {
				tempList.add(map.get("openId").toString());
			}
			return tempList;
		} else {
			return new ArrayList<String>();
		}
	}

	@Override
	public Map<String, Object> getUserOrg(Integer userId) {
		String userOrgSql = "select o.id, o.org_name from 0085_org o left join "
				+ "0085_user_org uo on o.id=uo.org_id where o.status=1 and uo.user_id=?";
		return this.findOneForJdbc(userOrgSql, new Object[] { userId });
	}
	
	public Map<String, Object> getUserPosition(Integer userId) {
		String userOrgSql = "select p.* from 0085_position p ";
		userOrgSql += " left join 0085_courier_position cp on cp.position_id=p.id ";
		userOrgSql += " where cp.courier_id=? ";
		return this.findOneForJdbc(userOrgSql, new Object[]{userId});
	}
	
	public Map<String, Object> getCourierInfo(Integer courierId) {
		String userOrgSql = "SELECT * FROM 0085_courier_info where courier_id=?";
		return this.findOneForJdbc(userOrgSql, new Object[]{courierId});
	}

	@Override
	public List<WUserEntity> findUserByType(String userType) {
		return this.findByProperty(WUserEntity.class, "userType", userType);
	}

	@Override
	public Double getBalance(Integer userId) {
		WUserEntity user = this.get(WUserEntity.class, userId);
		if (user != null) {
			return user.getMoney();
		}
		return 0.0;
	}

	/**
	 * 快递员成绩排名（按时间、区域、接单数排名）
	 * 
	 * @param courierId
	 *            快递员ID，必填
	 * @param startDate
	 *            统计起始时间，可以为空
	 * @param endDate
	 *            统计截止时间，可以为空
	 * @param isRankByArea
	 *            是否按区域排名，true：按区域排名，返回快递员所属分区的所有快递员订单数排名；false：返回当前快递员总排名
	 * @return
	 */
	/*@Override
	public List<Map<String, Object>> getCourierRank(Integer courierId,
			String startDate, String endDate, Boolean isRankByArea,
			String start, String num) {
		String sql = "";
		if (!isRankByArea) {
			sql += "select * from (";
		}
		sql += " select b.courier_id, u.username, b.total, (select count(1) + 1 from ";
		sql += " (select o.courier_id, count(0) total from `order` o ";
		if(isRankByArea){
			sql += " left join 0085_courier_org co on o.courier_id = co.courier_id ";
		}
		sql += " where o.courier_id<>0 and o.state='confirm' ";
		if (StringUtils.isNotEmpty(startDate)) {
			sql += " and DATE(FROM_UNIXTIME(complete_time)) >= '" + startDate
					+ "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql += " and DATE(FROM_UNIXTIME(complete_time)) <= '" + endDate + "'";
		}
		if (isRankByArea) {
			sql += " and co.org_id = (select org_id from 0085_courier_org where courier_id = "
					+ courierId + ")";
		}
		sql += " group by courier_id) a where a.total > b.total) as rank from ";
		sql += " (select o.courier_id, count(0) total from `order` o ";
		if(isRankByArea){
			sql += " left join 0085_courier_org co on o.courier_id = co.courier_id ";
		}
		sql += " where o.courier_id<>0 and o.state='confirm' ";
		if (StringUtils.isNotEmpty(startDate)) {
			sql += " and DATE(FROM_UNIXTIME(complete_time)) >= '" + startDate
					+ "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql += " and DATE(FROM_UNIXTIME(complete_time)) <= '" + endDate + "'";
		}
		if (isRankByArea) {
			sql += " and co.org_id = (select org_id from 0085_courier_org where courier_id = "
					+ courierId + ")";
		}
		sql += " group by courier_id) b left join user u on u.id=b.courier_id order by rank ";
		if (!isRankByArea) {
			sql += " ) c where c.courier_id = " + courierId;
		}
		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(num)) {
			sql += " limit " + start + " , " + num;
		}
		return systemService.findForJdbc(sql);
	}*/
	
	/**
	 * 快递员成绩排名（按时间、区域、接单数排名）
	 */
	@Override
	public List<Map<String, Object>> getCourierRank(Integer courierId,
			String startDate, String endDate, Boolean isRankByArea,
			String start, String num) {
		String sql = "";
		if(!isRankByArea){
			sql += "select * from (";
		}
		sql += "select b.courier_id,u.username,case when b.total is null then 0 else b.total end total, case when b.total_income is null then 0 else b.total_income end total_income,";
		sql += "case when @prevrank = b.total then @currank when @prevrank := b.total then @currank := @currank + 1 end as rank, u.photoUrl from (";
		sql += "select o.courier_id,count(0) as total, sum(d.total_deduct) total_income from `order` o ";
		if(isRankByArea){
			sql += " left join 0085_courier_org co on o.courier_id = co.courier_id ";
		}
		String yestoday = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
		sql += " left join 0085_courier_deduct_log d on o.courier_id=d.courier_id ";
		sql += " where o.courier_id<>0 and o.state='confirm' ";
		sql += " and  date(FROM_UNIXTIME(d.account_date)) = '" + yestoday + "'";
		
		if (StringUtils.isNotEmpty(startDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
			sql += " and o.complete_time >= UNIX_TIMESTAMP('" + startDate + "')";
		}
		if (StringUtils.isNotEmpty(endDate)) {
//			sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
			sql += " and o.complete_time <= UNIX_TIMESTAMP('" + endDate + "')";
		}
		if (isRankByArea) {
			sql += " and co.org_id in (select org_id from 0085_courier_org where courier_id = " + courierId + ")";
		}
		sql += " group by o.courier_id order by total desc) b left join user u force index(i_u_id_un_ut) on u.id=b.courier_id, ";
		sql += " (select @currank :=0, @prevrank := null) r  ";
		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(num)) {
			sql += " limit " + start + " , " + num;
		}
		if(!isRankByArea){
			sql += ") c where courier_id=" + courierId;
		}
		return this.findForJdbc(sql);
	}	
	/**
	 * 获取快递员排名列表
	 */
	@Override
	public List<Map<String, Object>> getCourierRankNew(Integer courierId,
			String startDate, String endDate, Integer level,
			String start, String num) {
		//根据与指定快递员在同一级别组织架构下的快递员列表，比如：获取同一个片区、同一个区、同一个市、同一个省下的快递员列表
		List<Integer> courierIds = orgServiceI.getCourierIdByOrgLevel(courierId, level);
		if(CollectionUtils.isEmpty(courierIds)){
			return new ArrayList<Map<String,Object>>();
		}
		
		List<Object> params = new ArrayList<Object>();
		String sql = "";		
		sql += "select oo.courier_id, ifnull(oo.total, 0) total, u.username, ifnull(total_income, 0) total_income, u.photoUrl ";		
		sql += " from ";		
		sql += " 	(	SELECT o.courier_id, count(0) total FROM  `order` o where o.state='confirm'  ";
		if (StringUtils.isNotEmpty(startDate)) {
			sql += " 	and o.complete_time >= UNIX_TIMESTAMP(?)";
			params.add(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql += " 	and o.complete_time <= UNIX_TIMESTAMP(?)";
			params.add(endDate);
		}
		sql += " 		and o.courier_id in (" + StringUtils.join(courierIds, ",") + ")";
		sql += " ";
		sql += " 		group by o.courier_id ";
		sql += " 	) oo ";
		sql += " LEFT JOIN ";
		sql += " 		( SELECT cdl.courier_id, SUM(cdl.total_deduct) total_income FROM 0085_courier_deduct_log cdl  where 1=1 ";
		if (StringUtils.isNotEmpty(startDate)) {
			sql += " 	and cdl.account_date >= UNIX_TIMESTAMP(?)";
			params.add(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql += " 	and cdl.account_date <= UNIX_TIMESTAMP(?)";
			params.add(endDate);
		}
		sql += " 		and cdl.courier_id in (" + StringUtils.join(courierIds, ",")+ " ) ";
		sql += "   		group by cdl.courier_id";
		sql	+= " 		) d ";
		sql += " ON d.courier_id = oo.courier_id ";
		sql += " LEFT JOIN `user` u ";
		sql += " ON u.id = oo.courier_id ";	
		sql += " ORDER BY oo.total DESC ";	
		int startRank = 0;
		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(num)) {
			sql += " limit ? , ? " ;
			params.add(Integer.valueOf(start));
			params.add(Integer.valueOf(num));
			startRank = NumberUtils.toInt(start);
		}		
		List<Map<String, Object>> list = this.findForJdbc(sql, params.toArray());
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> map: list){
				map.put("rank", String.valueOf(++startRank));
			}
		}
		return list;
	}
	
	/**
	 * 获取快递员排名
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Integer getRank(Integer courierId, String startDate, String endDate, Integer level){
		List<Integer> courierIds = orgServiceI.getCourierIdByOrgLevel(courierId, level);
		if(CollectionUtils.isEmpty(courierIds)){
			return 0;
		}
		
		Integer totalOrder = getMyTotalOrders(courierId, startDate, endDate);
		List<Object> params = new ArrayList<Object>();		
		String sql = "";
		sql += " SELECT COUNT(0) rank FROM ";
		sql += " (	SELECT o.courier_id, COUNT(0) total from `order` o ";
		sql += " 	WHERE 1 = 1 ";		
		if(level != 1){
			sql += " AND o.courier_id in (" + StringUtils.join(courierIds, ",")+ ")";
		}
		sql += " AND  o.state='confirm'";
		if (StringUtils.isNotEmpty(startDate)) {
//			sql += " AND DATE(FROM_UNIXTIME(o.complete_time)) >= ? ";
			sql += " AND o.complete_time >= UNIX_TIMESTAMP(?)";
			params.add(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
//			sql += " AND DATE(FROM_UNIXTIME(o.complete_time)) <= ? " ;
			sql += " AND o.complete_time <= UNIX_TIMESTAMP(?)";
			params.add(endDate);
		}
		sql += " GROUP BY o.courier_id ";
		sql += " ) a where total > ?";
		params.add(totalOrder);		
		return this.findOneForJdbc(sql, Integer.class, params.toArray());		
	}
	
	/**
	 * 获取快递员订单数量
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Integer getMyTotalOrders(Integer courierId, String startDate, String endDate) {
		String sql = "";
		sql += " SELECT COUNT(0) total FROM `order` oo  ";		
		sql += "  WHERE oo.courier_id = '" + courierId + "'";
		sql += "  AND  oo.state='confirm' ";
		if (StringUtils.isNotEmpty(startDate)) {
//			sql += " AND DATE(FROM_UNIXTIME(oo.complete_time)) >=  '" + startDate + "'" ;
			sql += " AND oo.complete_time >=  UNIX_TIMESTAMP('" + startDate + "') " ;
		}
		if (StringUtils.isNotEmpty(endDate)) {
//			sql += " AND DATE(FROM_UNIXTIME(oo.complete_time)) <= '" + endDate + "'" ;	
			sql += " AND oo.complete_time  <=  UNIX_TIMESTAMP('" + endDate + "') " ;
		}
		return findOneForJdbc(sql, Integer.class);
	}

	@Override
	public List<Map<String, Object>> getCourierDeduct(Integer courierId,
			String startDate, String endDate) {
		String sql = "select * from 0085_courier_deduct_log where courier_id = "
				+ courierId;
		if (StringUtils.isNotEmpty(startDate)) {
			sql += " and DATE(FROM_UNIXTIME(account_date)) >= '" + startDate
					+ "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql += " and DATE(FROM_UNIXTIME(account_date)) <= '" + endDate
					+ "'";
		}
		return this.findForJdbc(sql);
	}
	
	@Override
	public boolean updateUserInfo(int userId,String nickname, String photoUrl,String mobile) {
		WUserEntity user = this.getEntity(WUserEntity.class, userId);
		if (user == null) {
			return false;
		}
		if(StringUtil.isNotEmpty(nickname)){
			user.setNickname(nickname);
		}
		if(StringUtil.isNotEmpty(photoUrl)){
			user.setPhotoUrl(photoUrl);
		}
		if(StringUtil.isNotEmpty(mobile)){
			user.setMobile(mobile);
		}
		this.saveOrUpdate(user);
		return true;
	}

	@Override
	public boolean updatePhotoUrl(int userId, String photoUrl) {
		WUserEntity user = this.getEntity(WUserEntity.class, userId);
		if (user == null) {
			return false;
		}
		user.setPhotoUrl(photoUrl);
		this.saveOrUpdate(user);
		return true;
	}

	@Override
	public boolean updatePassword(int userId, String password, String newPassword) {
		WUserEntity user = this.getEntity(WUserEntity.class, userId);
		if (user == null || !user.getPassword().equals(password)) {
			return false;
		}
		user.setPassword(newPassword);
		this.saveOrUpdate(user);
		return true;
	}
	
	@Override
	public boolean resetPassword(int userId,String newPassword) {
		WUserEntity user = this.getEntity(WUserEntity.class, userId);
		user.setPassword(newPassword);
		this.saveOrUpdate(user);
		return true;
	}
	
	/**
	 * 获取客户类型
	 * @return
	 */
	public String getCustType(Integer userId){
		String custType = "新";
		if(userId != 0){
			String sql = "select count(*) from `order` where user_id=? ";
			Long hisOrders = this.getCountForJdbcParam(sql, new Object[]{userId});
			logger.debug("user:{} hisOrders:{}", userId, hisOrders);
			if(hisOrders > 0){
				sql = "select sum(origin) total from `order` where user_id=? and state='confirm' ";
				Double total = super.findOneForJdbc(sql, Double.class, userId);
				logger.debug("user:{} totalConsumeMoney:{}", userId, total);
				custType = custTypeRuleService.getCustTypeByConsumeAmount(total);
			}
		}
		return custType;
	}

	@Override
	public boolean checkIdCard(String idcard) {
		String sql = "select id from 0085_courier_info where id_card=? ";
		List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{idcard});
		boolean isValid = true;
		if(list != null && list.size() > 0){
			isValid = false;
		}
		return isValid;
	}
	
	@Override
	public WUserEntity findByOpenId(String openId) {
		return userDao.queryByOpenId(openId);
	}
	
	@Override
	public NewAndOldUserVo statisticsNewAndOldUser(String startTime, String endTime){
		return userDao.statisticsNewAndOldUser(startTime,endTime);
	}
	
	/**
	 * 对账
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public AjaxJson reconciliation(Integer userId, String startDate, String endDate){
		AjaxJson retMap = new AjaxJson();
		List<Map<String, Object>> list = this.findForJdbc(reconciliationSql, new Object[]{userId, startDate, endDate});
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			if(size == 1){
				retMap.setSuccess(false);
				retMap.setStateCode("2");
				retMap.setMsg("该段时间内只有一条流水，请重新选择时间！");
			} else {
				for(int i = 0; i < size - 1; i++){
					Map<String, Object> map1 = list.get(i);
					String preMoney = map1.get("pre_money").toString();
					Map<String, Object> map2 = list.get(i+1);
					String postMoney = map2.get("post_money").toString();
					if(!preMoney.equals(postMoney)){
						retMap.setSuccess(false);
						retMap.setStateCode("3");
						retMap.setMsg("对账失败！流水号：" + map1.get("id")+ "," + map2.get("id"));
						return retMap;
					}
				}
				retMap.setSuccess(true);
				retMap.setStateCode("0");
				retMap.setMsg("对账成功");
			}
		} else {
			retMap.setSuccess(false);
			retMap.setStateCode("1");
			retMap.setMsg("该段时间内没有产生流水！");
		}
		return retMap;
	}
	
	@Override
	public List<WUserEntity> getAllServingCouriers() {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("isDelete", WUserEntity.SERVING_STATE);
		cq.eq("userType", "courier");
		cq.add();
		return getListByCriteriaQuery(cq, false);
	}

	@Override
	public WUserEntity getUserByOpenId(String openId) {
		CriteriaQuery cq = new CriteriaQuery(WUserEntity.class);
		cq.eq("openId", openId);
		cq.eq("userType", "user");
		cq.add();
		List<WUserEntity> users = this.getListByCriteriaQuery(cq, false);

		WUserEntity user = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public Double getSumDeduct(Integer courierId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  case when SUM(total_deduct) is null then 0.0 else SUM(total_deduct) end sumDeduct ");
		query.append(" FROM 0085_courier_deduct_log ");
		query.append(" WHERE ");
		query.append("courier_id = ? ");		
		return findOneForJdbc(query.toString(), Double.class, courierId);
	}

	@Override
	public Double getSumWithdrawals(Integer courierId) {
		if(courierId == null){
			throw new IllegalArgumentException("courierId=null");
		}
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT case when SUM(money) is null then 0.0 else SUM(money) end sumMoney ");
		query.append(" FROM withdrawals ");
		query.append(" WHERE ");
		query.append(" user_id = ? ");
		query.append(" AND state = 'done' ");
		return findOneForJdbc(query.toString(), Double.class, courierId);	
	}

	@Override
	public Map<String, Object> countNotice(Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(tnu.read_status) countUnread ");
		query.append(" FROM tsm_notice_user tnu");
		query.append(" left join tsm_notice tn on tnu.notice_id = tn.id");
		query.append(" WHERE ");
		query.append(" user_id = ? ");
		query.append(" AND tnu.read_status = 0");
		query.append(" AND tn.send_status = 1");
		return findOneForJdbc(query.toString(), userId);
	}

	@Override
	public List<Map<String, Object>> getUnreadNotice(Integer userId, int page, int rows) {
		int Start = (page-1)*rows;
		int end = page*rows;
		StringBuilder query = new StringBuilder();
		query.append(" SELECT tn.id, tn.title, tn.content, tn.type,  tnu.read_status, ifnull(update_time, create_time) time ");
		query.append(" FROM tsm_notice tn");
		query.append(" LEFT JOIN  tsm_notice_user tnu ON tn.id = tnu.notice_id ");
		query.append(" WHERE ");
		query.append(" user_id = ? ");
		query.append(" AND read_status = 0");
		query.append(" AND notice_Object = 3 ");
		query.append(" AND send_status = 1 ");
		query.append(" order by time desc ");
		query.append(" limit ?, ? ");
		return findForJdbc(query.toString(),  userId, Start, end);
	}
	
	@Override
	public List<Map<String, Object>> getNotice(Integer userId, int page, int rows) {
		int Start = (page-1)*rows;
		int end = page*rows;
		StringBuilder query = new StringBuilder();
		query.append(" SELECT tn.id, title, content, type, read_status, ifnull(update_time, create_time) time  ");
		query.append(" FROM tsm_notice tn");
		query.append(" LEFT JOIN  tsm_notice_user tnu ON tn.id = tnu.notice_id ");
		query.append(" WHERE ");
		query.append(" user_id = ? ");
		query.append(" AND notice_Object = 3 ");
		query.append(" AND send_status = 1 ");
		query.append(" order by read_status, time desc ");
		query.append(" limit ?, ? ");
		return findForJdbc(query.toString(),  userId, Start, end);
	}

	@Override
	public Integer readNotice(Integer userId, String noticeIds, Date readTime) {
		List<Long> list = new ArrayList<Long>();
		String [] ids = noticeIds.split(",");
		for(String str : ids){
			list.add(Long.valueOf(str));
		}
		if(list.size() > 0){
			for(int i=0; i<list.size(); i++){
				Long noticeId = list.get(i);
				StringBuilder query = new StringBuilder();
				query.append(" UPDATE tsm_notice_user ");
				query.append(" SET ");
				query.append(" read_status = 1, ");
				query.append(" read_time = ? ");
				query.append(" WHERE ");
				query.append(" user_id = ? ");
				query.append(" AND notice_id = ? ");
				executeSql(query.toString(), readTime, userId, noticeId);
			}
		}
		return 0;
		
	}

	@Override
	public Integer getUserState(Integer userId) {
		StringBuilder query = new StringBuilder();
		query.append("select CASE when user_state is null then 1 ELSE user_state END userState ");
		query.append(" from `user` ");
		query.append(" where ");
		query.append(" id = ? ");
 		return findOneForJdbc(query.toString(), Integer.class, userId);
	}
	
	@Override
	public List<Map<String, Object>> getRegulationName(){
		logger.info("获取快递员管理条例");
		StringBuilder query = new StringBuilder(); 
		query.append(" select id, title ");
		query.append(" from mana_regulation where type in(0,1) ");
		return findForJdbc(query.toString());
	}

	@Override
	public Map<String, Object> getRegulationContent(Integer id){
		logger.info("获取管理条例id为: " + id + "的详细内容");
		StringBuilder query = new StringBuilder();
		query.append(" select id, title, ifnull(content, '') content, type, ifnull(href, '') href, creator, ifnull(update_time, create_time) updateTime, ifnull(logo, '') logo " );
		query.append(" from mana_regulation  " );
		query.append(" where id = ?  " );
		return findOneForJdbc(query.toString(), id);
	}

	@Override
	public boolean isFirstOrder(Integer userId) {
		Integer orderCount = this.findOneForJdbc(getUserOrders, Integer.class, userId);
		logger.info("user[{}] total orders:{}", userId, orderCount);
		return orderCount.equals(1) ? true : false;
	}

	@Override
	public Map<String, Object> getUserInfo(Integer userId) {
		logger.info("获取用户" + userId + "的用户信息");
//		SELECT * 
//		FROM `user` u
//		WHERE u.id = ?
		StringBuilder query = new StringBuilder(" SELECT * ");
		query.append(" FROM `user` u ");
		query.append(" WHERE u.id = ? AND is_delete = 0 ");
		return findOneForJdbc(query.toString(), userId);
	}
}