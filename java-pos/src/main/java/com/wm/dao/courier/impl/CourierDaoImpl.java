package com.wm.dao.courier.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.beust.jcommander.internal.Lists;
import com.wm.dao.courier.CourierDao;
import com.wm.dao.org.OrgDao;
import com.wm.entity.courier.CourierEntity;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.StringUtil;

@Repository("courierDao")
public class CourierDaoImpl extends
		GenericBaseCommonDao<CourierEntity, Serializable> implements CourierDao {

	@Autowired
	private CourierServiceI courierService;

	@Autowired
	private OrgDao orgDao;

	@Autowired
	private WUserServiceI userService;

	@Override
	public List<Map<String, Object>> queryCourierReminderListByOrgId(
			Integer orgId, Integer userId, Integer page, Integer rows) {

		List<Map<String, Object>> courierOrderList = Lists.newArrayList();

//		List<CourierOrgVo> queryOrgByUserIdList = courierService
//				.queryOrgByUserId(userId, 1, rows);
		// 是否是见习员工，业务员，物流组长
//		boolean isDownWuliuzuzhang = false;
//		if (queryOrgByUserIdList != null && queryOrgByUserIdList.size() > 0) {
//			CourierOrgVo cov = queryOrgByUserIdList.get(0);
//			if (cov.getPositionName().equalsIgnoreCase("物流组长")
//					|| cov.getPositionName().equalsIgnoreCase("业务员")
//					|| cov.getPositionName().equalsIgnoreCase("见习员工")) {
//				isDownWuliuzuzhang = true;
//			}
//		}
		// 片区经理以上
		StringBuffer sbsql = getReminderSql(orgId, false);
		List<Map<String, Object>> courierOrderMap = null;
		if (page == null || rows == null || page == 0 || rows == 0) {
			courierOrderMap = this.findForJdbc(sbsql.toString(), userId, userId);
		} else {
			courierOrderMap = this.findForJdbcParam(sbsql.toString(), page, rows, userId, userId);
		}

		String sql = "SELECT m.name,o_m.price,o_m.quantity,o.time_remark as timeRemark,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num FROM "
				+ " `order` o LEFT JOIN "
				+ " merchant merchant ON merchant.id=o.merchant_id "
				+ " RIGHT JOIN order_menu o_m ON o_m.order_id=o.id LEFT JOIN menu m "
				+ " ON m.id=o_m.menu_id WHERE o.id=?";

		for (Map<String, Object> m : courierOrderMap) {
			String custType = "新"; // 用户类型
			Integer userIdTmp = Integer.parseInt(m.get("user_id").toString());

			custType = userService.getCustType(userIdTmp);
			m.put("custType", custType);

			List<Map<String, Object>> courierOrderDetail = this.findForJdbc(
					sql, new Object[] { m.get("id") });
			m.put("courierOrderDetail", courierOrderDetail);

			int total_num = 0 ;
			double total_price = 0.0;
			if(courierOrderDetail != null && courierOrderDetail.size() > 0){
				for (Map<String, Object> mapItem : courierOrderDetail) {
					total_num += Integer.parseInt(mapItem.get("quantity").toString());
					total_price += Integer.parseInt(mapItem.get("price").toString());
				}
			}
			m.put("total_num", total_num);
			m.put("total_price", total_price);
			courierOrderList.add(m);
		}

		return courierOrderList;
	}
	
	private StringBuffer getReminderSql(Integer orgId, boolean countSql){
		StringBuffer sbsql = new StringBuffer();
		//查询不包含自己的orgid
		List<String> recursionQueryOrgIdList = orgDao
				.recursionQueryOrgIdList(orgId, false);
		String list2SqlInList = StringUtil.list2SqlIn(recursionQueryOrgIdList);

		if(countSql){
			sbsql.append(" SELECT count(DISTINCT o.id) as count ");
		}else{
			sbsql.append(" SELECT DISTINCT o.courier_id, case when o.courier_id > 0 then 1 else 0 end as scramble,  ");//是否可抢单
			sbsql.append(" case when o.courier_id > 0 then (select username from `user` where id = o.courier_id) else '' end as courier_name,  ");//如果已分了快递员，则返回快递员名字
			sbsql.append(" CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num, ") 
				.append(" o.time_remark as timeRemark,o.pay_id, ")
				.append(" o.id,o.state,o.pay_state,o.order_type,o.mobile,o.realname,o.address,merchant.logo_url,o.user_id, ")
				.append(" o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time, ")
				.append(" merchant.title, o.remark, ")
				.append(" case when o.origin<500 then 1 else 2 end is_priority ");
		}
		// 订单表，快递员区域表，订单催单表，商户表，用户表
		sbsql.append(" FROM 	`order` AS o, 0085_courier_org AS co, ")
			.append(" 0085_order_reminder AS orderRem, merchant as merchant ");
		sbsql.append(" WHERE orderRem.order_id = o.id AND orderRem.resolver IS NULL ")
			.append(" AND merchant.id = o.merchant_id ")
			.append(" AND ( o.courier_id in ( SELECT courier_id FROM 0085_courier_org WHERE org_id IN ")
			.append(list2SqlInList).append("  ) ")
			.append(" OR o.courier_id = ? ") //这里需要传入快递员id
			.append(" OR o.courier_id IN ( SELECT user_id FROM 0085_courier_group_member WHERE group_id in (SELECT group_id FROM 0085_courier_group_member WHERE user_id = ? ) ) )");//这里需要传入快递员id
				
		sbsql.append(" ORDER BY o.time_remark ASC, o.create_time ASC ");
		return sbsql;
	}

	
	@Override
	public Long countCourierReminder(Integer orgId, Integer userId) {
		StringBuffer sql = getReminderSql(orgId, true);
		Map<String, Object> map = this.findOneForJdbc(sql.toString(), userId, userId);
		if(map != null && map.size() > 0){
			Object obj = map.get("count");
			if(!StringUtils.isBlank(String.valueOf(obj))){
				return Long.valueOf(String.valueOf(obj));
			}
		}
		return 0L;
	}

}
