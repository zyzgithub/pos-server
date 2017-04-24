package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.OrgUserCountService;
import com.wm.util.SqlUtils;

@Service
public class OrgUserCountServiceImpl extends CommonServiceImpl implements OrgUserCountService {
	private static final Logger logger = LoggerFactory.getLogger(OrgUserCountServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> getOrgUserInfo(Integer userId, SearchVo vo, Integer page, Integer rows) {
		logger.info("调用方法: getOrgUserInfo ,获取网点人数信息, 参数: userId: {}, vo: {}, page: {}, rows: {}", userId, page, rows);
		
		List<Integer> orgIds = this.getManageL6OrgId(userId);
		
		List<Map<String, Object>> orgUserList = this.getOrderedOrgUserList(orgIds, vo, page, rows);
		
		int num = (page-1)*rows;	//记录序号
		
		/**
		 * 遍历网点-用户列表, 往列表元素中添加字段
		 */
		for(Map<String, Object> item: orgUserList){
			num++;
			this.putDetailsIntoOrgUserListItem(item, vo, num);
		}
		
		logger.info("获取网点人数信息成功");
		
		return orgUserList;
	}
	
	/**(OvO)
	 * 获取按总人数排序的机构列表
	 * @param orgIds	要查询的机构ID
	 * @param timeType	查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @param page		页数
	 * @param rows		每页显示记录数
	 * @return	id			机构ID
	 * 			orgName		机构名称
	 * 			userCount	总用户数
	 */
	private List<Map<String, Object>> getOrderedOrgUserList(List<Integer> orgIds, SearchVo timeType, Integer page, Integer rows){
		StringBuilder sql = new StringBuilder();
		String period = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		sql.append(" SELECT org.id, org.org_name orgName, IFNULL(COUNT(DISTINCT po.user_id),0) userCount, IFNULL(COUNT(1),0) orderCount ");
		sql.append(" FROM 0085_org org, 0085_merchant_org morg ");
		sql.append(" 	LEFT JOIN ( ");
		sql.append(" 		SELECT o.merchant_id, o.user_id ");
		sql.append(" 		FROM `order` o ");
		sql.append(" 		WHERE o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		sql.append(period);
		sql.append(" 	)po ON po.merchant_id = morg.merchant_id ");
		sql.append(" WHERE org.id = morg.org_id ");
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) GROUP BY org.id ");
		sql.append(" ORDER BY userCount DESC, org.id ");
		return this.findForJdbc(sql.toString(), page, rows);
	}

	/**(OvO)
	 * 往机构-人数信息列表元素中插入其他信息: 新用户数、老用户数、复购率(百分数)
	 * @param item		机构-人数信息
	 * @param timeType	查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @param num		记录序号
	 */
	private void putDetailsIntoOrgUserListItem(Map<String, Object> item, SearchVo timeType, Integer num){
		item.put("rank", num);
		
		Integer orgId = Integer.valueOf(item.get("id").toString());
		
		/**
		 * 新用户数
		 */
		Integer newUser = this.getNewUserCountByOrgId(orgId, timeType);
		
		/**
		 * 总用户数
		 */
		Integer userCount = Integer.valueOf(item.get("userCount").toString());
		
		/**
		 * 老用户数
		 */
		Integer oldUser = userCount - newUser;
		
		/**
		 * 订单数
		 */
		Integer orderCount = Integer.parseInt(item.get("orderCount").toString());
		item.put("newUser", newUser);
		item.put("oldUser", oldUser);
		
		BigDecimal oldUserBD = BigDecimal.valueOf(oldUser);
		
		if(Integer.valueOf(0).equals(orderCount)){
			item.put("reorderRate", 0 + "%");
		}else{
			BigDecimal reorderRate = oldUserBD.divide(BigDecimal.valueOf(orderCount), 4, BigDecimal.ROUND_HALF_UP);
			reorderRate = reorderRate.movePointRight(2);
			item.put("reorderRate", reorderRate + "%");
		}
	}
	
	/**(OvO)
	 * 根据机构ID获取新用户数
	 * @param orgId 	机构ID
	 * @param timeType	查询时间类型(day: 日; week: 周; month: 月; other: 指定时间段, 需要额外提供beginTime, endTime)
	 * @return	新用户数
	 */
	private Integer getNewUserCountByOrgId(Integer orgId, SearchVo timeType){
		StringBuilder sql = new StringBuilder();
		String firstOrderTime = SqlUtils.getTimeWhere4SQL(timeType, "u.first_order_time");
		String createTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		sql.append(" SELECT IFNULL(COUNT(DISTINCT u.id),0) newUser ");
		sql.append(" FROM `user` u, `order` o, 0085_merchant_org morg ");
		sql.append(" WHERE o.pay_time = u.first_order_time AND o.user_id = u.id ");
		sql.append(" AND o.merchant_id = morg.merchant_id AND morg.org_id = ? ");
		sql.append(firstOrderTime);
		sql.append(createTime);
		sql.append(" 	AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		
		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString(), orgId);
		return Integer.valueOf(resultMap.get("newUser").toString());
	}
	
	/**
	 * 根据机构ID获取订单数
	 * @param orgId	机构ID
	 * @return	订单数
	 */
//	private Integer getOrderCountByOrgId(Integer orgId, SearchVo timeType){
//		StringBuilder sql = new StringBuilder();
//		String createTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
//		
//		sql.append(" SELECT IFNULL(COUNT(1),0) orderCount ");
//		sql.append(" FROM `order` o, 0085_merchant_org morg ");
//		sql.append(" WHERE morg.org_id = ? AND o.merchant_id = morg.merchant_id AND o.state = 'confirm' AND o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
//		sql.append(createTime);
//		
//		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString(), orgId);
//		return Integer.valueOf(resultMap.get("orderCount").toString());
//	}
	
	/**(OvO)
	 * 根据快递员ID获取管辖范围内所有level6的机构ID
	 * @param courierId	快递员ID
	 * @return	level6 的机构ID列表
	 */
	private List<Integer> getManageL6OrgId(Integer courierId){
		/**
		 * 获取快递员所绑定的机构信息
		 */
		Map<String, Object> orgInfo = this.courierOrgService.getParentOrg(courierId);
		
		if(orgInfo == null){
			throw new IllegalArgumentException("权限不足: 无法根据快递员ID获取机构信息");
		}
		
		/**
		 * level 字段在数据库中非空可以放心使用
		 */
		Integer level = Integer.valueOf(orgInfo.get("level").toString());
		/**
		 * 如果获取的机构本身就是level6的直接返回此机构ID
		 */
		if(level.equals(6)){
			List<Integer> orgIds = new ArrayList<Integer>();
			orgIds.add(Integer.valueOf(orgInfo.get("id").toString()));
			return orgIds;
		}
		
		/**
		 * 如果机构不是level6，就查询下属的level6机构ID
		 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT org.id ");
		sql.append(" FROM 0085_org org ");
		sql.append(" WHERE org.`status` = 1 AND org.`level` = 6 AND org.p_path REGEXP '^");
		sql.append(orgInfo.get("p_path"));
		sql.append(orgInfo.get("id"));
		sql.append("_' ");
		return this.findListbySql(sql.toString());
	}
}
