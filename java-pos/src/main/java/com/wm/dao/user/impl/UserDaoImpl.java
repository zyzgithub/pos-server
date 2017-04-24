package com.wm.dao.user.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Repository;

import com.wm.dao.user.UserDao;
import com.wm.entity.user.NewAndOldUserVo;
import com.wm.entity.user.WUserEntity;

@Repository("userDao")
@SuppressWarnings("unchecked")
public class UserDaoImpl extends GenericBaseCommonDao<WUserEntity, Integer> implements UserDao {

	@Override
	public WUserEntity queryByOpenId(String openId) {
		String hql = "from WUserEntity wu where wu.openId = ? order by wu.id desc";
		Query query = createHqlQuery(hql, openId);
		List<WUserEntity> list = query.list();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public NewAndOldUserVo statisticsNewAndOldUser(String startTime, String endTime) {
		NewAndOldUserVo newAndOldUserVo = new NewAndOldUserVo();
		StringBuffer newUserSbSql = new StringBuffer();
		StringBuffer oldUserSbSql = new StringBuffer();
		StringBuffer newUserByTimeSbSql = new StringBuffer();
		StringBuffer oldUserByTimeSbSql = new StringBuffer();
		String newUserCount = "";
		String oldUserCount = "";
		newUserSbSql
				.append(" select COUNT(*) as count from (SELECT o.user_id,u.username,COUNT(o.user_id) co from `order` o, "
						+ " user u WHERE o.user_id = u.id GROUP BY o.user_id) t where t.co = 1 ");
		oldUserSbSql
				.append(" select COUNT(*) as count from (SELECT o.user_id,u.username,COUNT(o.user_id) co from `order` o, "
						+ " user u WHERE o.user_id = u.id GROUP BY o.user_id) t where t.co > 1 ");
		newUserByTimeSbSql
				.append("SELECT COUNT(*) as count FROM  `user` u WHERE 1=1");
		oldUserByTimeSbSql
				.append("SELECT COUNT(*) as count FROM  `user` u WHERE 1=1");
		if (StringUtil.isNotEmpty(startTime)) {
			newUserByTimeSbSql
					.append(" AND DATE(FROM_UNIXTIME(u.first_order_time)) >= '"
							+ startTime + "'");
			oldUserByTimeSbSql
					.append(" AND DATE(FROM_UNIXTIME(u.first_order_time)) <= '"
							+ startTime + "'");
		}
		if (StringUtil.isNotEmpty(endTime)) {
			newUserByTimeSbSql
					.append(" AND DATE(FROM_UNIXTIME(u.first_order_time)) <= '"
							+ endTime + "'");
			oldUserByTimeSbSql
					.append(" AND DATE(FROM_UNIXTIME(u.first_order_time)) <= '"
							+ endTime + "'");
		}
		List<Map<String, Object>> newUserList = this.findForJdbc(newUserSbSql.toString());
		Map<String, Object> newUserMap = newUserList.get(0);
		newUserCount = newUserMap.get("count").toString();
		List<Map<String, Object>> oldUserList = this.findForJdbc(oldUserSbSql.toString());
		Map<String, Object> oldUserMap = oldUserList.get(0);
		oldUserCount = oldUserMap.get("count").toString();
		if (StringUtil.isNotEmpty(startTime) || StringUtil.isNotEmpty(endTime)) {
			List<Map<String, Object>> newUserByTimeList = this
					.findForJdbc(newUserByTimeSbSql.toString());
			Map<String, Object> newUserByTimeMap = newUserByTimeList.get(0);
			newUserCount = newUserByTimeMap.get("count").toString();
			List<Map<String, Object>> oldUserByTimeList = this
					.findForJdbc(oldUserByTimeSbSql.toString());
			Map<String, Object> oldUserByTimeMap = oldUserByTimeList.get(0);
			oldUserCount = oldUserByTimeMap.get("count").toString();
		}
		newAndOldUserVo.setNewUserCount(newUserCount);
		newAndOldUserVo.setOldUserCount(oldUserCount);
		return newAndOldUserVo;
	}
	
	@Override
	public BigDecimal getMoneyForUpdate(Integer userId) {
		String sql = "select money from `user` u where u.id=? for update";
		return findOneForJdbc(sql, BigDecimal.class, userId);
	}
	
	@Override
	public int updateMoney(BigDecimal postMoney, Integer userId, BigDecimal preMoney) {
		String sql = "update `user` u set u.money = ? where u.id = ? and money=?";
		return executeSql(sql, postMoney, userId, preMoney);
	}

}
