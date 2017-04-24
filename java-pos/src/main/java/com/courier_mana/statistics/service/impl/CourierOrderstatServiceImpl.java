package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.CourierOrderstatService;
import com.wm.util.ListUtil;
import com.wm.util.SqlUtils;


@Service
public class CourierOrderstatServiceImpl extends CommonServiceImpl implements CourierOrderstatService {
	private static final Logger logger = LoggerFactory.getLogger(CourierOrderstatServiceImpl.class);
	
	@Override
	public List<Map<String, Object>> getOrderstatByOrg(List<Integer> orgIds, Integer page, Integer rows, SearchVo searchVo){
		if(CollectionUtils.isEmpty(orgIds)){
			logger.error("订单统计参数为空：orgIds=null");
			throw new IllegalArgumentException("orgIds=null");
		}
		//按时间类型搜索： 按天：day  按周week  按月：month  自定义：other
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo, "o.create_time");
		
		List<Map<String, Object>> orgList = this.getOrderedOrgOriginList(orgIds, timewhere, page, rows);
		
		int num = (page-1)*rows;	//记录序号
		
		/**
		 * 遍历机构列表, 向列表中的元素添加信息: 客单价， 用户数
		 */
		for(Map<String, Object> item: orgList){
			num++;
			this.putDetailsIntoOrgListItem(item, timewhere, num);
		}
		return orgList;
	}
	
	/**
	 * 通过快递员ID列表 得到 按店铺，并按金额 从大到小排序的列表（字段：大厦名，订单数，份数，金额）
	 * @param courierIds
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getOrderstatByMerchant(List<Integer> orgIds, Integer page, Integer rows,
			SearchVo searchVo){
		if(CollectionUtils.isEmpty(orgIds)){
			logger.error("订单统计参数为空：orgIds=null");
			throw new IllegalArgumentException("orgIds=null");
		}
		//按时间类型搜索： 按天：day  按周week  按月：month  自定义：other
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo, "o.create_time");

		List<Map<String, Object>> merchantList = this.getOrderedMerchantOriginList(orgIds, timewhere, page, rows);
		
		int num = (page-1)*rows;	//记录序号
		
		/**
		 * 遍历商家列表, 向列表中的元素添加信息: 外卖订单份数, 扫码订单份数, 客单价， 用户数
		 */
		for(Map<String, Object> item: merchantList){
			num++;
			this.putDetailsIntoMerchantListItem(item, timewhere, num);
		}
		return merchantList;
	}
	
	@Override
	public List<Map<String, Object>> getOrderstatByMerchant4Agent(Integer userId, Integer page, Integer rows,
			SearchVo searchVo){
		/**
		 * 参数已在Controller检查过
		 */
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo, "o.create_time");
		
		List<Map<String, Object>> merchantList = this.getOrderedMerchantOriginList4Agent(userId, timewhere, page, rows);
		
		int num = (page-1)*rows;	//记录序号
		
		/**
		 * 遍历商家列表, 向列表中的元素添加信息: 外卖订单份数, 扫码订单份数, 客单价， 用户数
		 */
		for(Map<String, Object> item: merchantList){
			num++;
			this.putDetailsIntoMerchantListItem(item, timewhere, num);
			
		}
		
		return merchantList;
	}
	
	
	@Override
	public Map<String, Object> getOrderSumByMerchant(List<Integer> orgIds, SearchVo searchVo){
		//按时间类型搜索： 按天：day  按周week  按月：month  自定义：other
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo, "o.create_time");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" 	select IFNULL(sum(origin),0) origin ");
		sql.append(" 	from `order` o, `merchant` me, 0085_merchant_org mo  ");
		sql.append(" 	where o.merchant_id = me.id and me.id = mo.merchant_id and o.state = 'confirm' and me.is_delete = '0' ");
		sql.append(" 	and mo.org_id in ( " + StringUtils.join(orgIds, ",") + ")");
		sql.append(timewhere);
		return findOneForJdbc(sql.toString());
	}
	
	@Override
	public Integer getOrderNum(Integer id) {
		String sql = " select count(1) orderNum from 0085_merchant_org a INNER JOIN `merchant` b ON a.merchant_id=b.id \n" +
					 "				INNER JOIN `order` c ON c.merchant_id = b.id\n" +
					 "				where a.org_id = ?";
		List<Map<String, Object>> list = findForJdbc(sql,id);
		if(ListUtil.isEmpty(list)){
			Object orderNum = list.get(0).get("orderNum");
			return  new Integer(orderNum.toString());
		}
		return 0;
	}
	
	@Override
	public Integer getOrderNumNew(Integer id) {
		String sql = " select count(1) orderNum from 0085_merchant_org a INNER JOIN `merchant` b ON a.merchant_id=b.id \n" +
					 "				INNER JOIN `order` c ON c.merchant_id = b.id \n" +
					 "				where a.org_id = ? AND b.create_time = UNIX_TIMESTAMP(CURDATE())";
		List<Map<String, Object>> list = findForJdbc(sql,id);
		if(ListUtil.isEmpty(list)){
			Object orderNum = list.get(0).get("orderNum");
			return  new Integer(orderNum.toString());
		}
		return 0;
	}

	@Override
	public Integer getMerchantNewCount(Integer id) {
		
		String sql = "select COUNT(cc.id) merchantCount from 0085_merchant_org bb INNER JOIN `merchant` cc ON bb.merchant_id=cc.id  where bb.org_id=? AND cc.create_time = UNIX_TIMESTAMP(CURDATE())";
		List<Map<String, Object>> list = findForJdbc(sql,id);
		if(ListUtil.isEmpty(list)){
			Object merchantCount = list.get(0).get("merchantCount");
			return new Integer(merchantCount.toString());
		}
		return 0;
	}

	@Override
	public Integer getMerchantOldCount(Integer id) {
		String sql = "select COUNT(cc.id) merchantCount from 0085_merchant_org bb INNER JOIN `merchant` cc ON bb.merchant_id=cc.id  where bb.org_id=? AND cc.create_time < UNIX_TIMESTAMP(CURDATE())";
		List<Map<String, Object>> list = findForJdbc(sql,id);
		if(ListUtil.isEmpty(list)){
			return new Integer(list.get(0).get("merchantCount").toString());
		}
		return 0;
	}

	@Override
	public List<Map<String,Object>>  getMerchantNew(List<Integer> ids, SearchVo vo,Integer page,Integer rows) {
		String period = this.getPeriod(vo);
		/*String sql  = " select ax.*,(select count(1) orderNum from 0085_merchant_org a INNER JOIN `merchant` b ON a.merchant_id=b.id INNER JOIN `order` c ON c.merchant_id = b.id where a.org_id = ax.id AND b.create_time = UNIX_TIMESTAMP(CURDATE())) orderNum" +
					  " FROM(" +
					  " select a.org_name orgName,(select COUNT(cc.id) from 0085_merchant_org bb INNER JOIN `merchant` cc ON bb.merchant_id=cc.id  where bb.org_id=a.id ) merchantCount,a.id" +
					  " from 0085_org a INNER JOIN 0085_merchant_org b ON b.org_id = a.id" +
					  "			INNER JOIN `merchant` c ON b.merchant_id=c.id INNER JOIN `order` d ON d.merchant_id = c.id " +
					  "			where DATE(FROM_UNIXTIME(c.create_time)) = CURDATE() AND  a.id in("+ StringUtils.join(ids,",") +") " +
					  "			GROUP BY a.id) ax ";*/
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT c.org_name orgName,COUNT(b.id) merchantCount,a.org_id ");
		sql.append(" ,( ");
		sql.append(" SELECT COUNT(1) FROM 0085_merchant_org aa ,merchant bb ,`order` cc  ");
		sql.append(" WHERE aa.org_id = a.org_id AND aa.merchant_id = bb.id AND bb.id = cc.merchant_id AND bb.create_time>= ");
		sql.append(period);
		sql.append(" ) orderNum ");
		sql.append(" FROM 0085_merchant_org a,merchant b,0085_org c  ");
		sql.append(" WHERE a.org_id IN("+ StringUtils.join(ids,",") +") ");
		sql.append(" AND b.create_time>= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" AND a.merchant_id = b.id ");
		sql.append(" AND c.id = a.org_id ");
		sql.append(" GROUP BY a.org_id ");
		
		List<Map<String, Object>> list = findForJdbcParam(sql.toString(), page, rows);
		return list;
	}
	
	/**
	 * 商家统计
	 * @param ids
	 * @param state 1:已完成  0:未完成
	 * @return
	 */
	@Override
	public Integer getMerchantCount(List<Integer> ids,Integer state) {
//		String str = "";
//		if(state.equals(1)){
//			str = "pay";
//		}else{
//			str = "unpay";
//		}
		
		String sql = "select COUNT(DISTINCT(b.id)) merchantCount from 0085_merchant_org a INNER JOIN `merchant` b ON a.merchant_id=b.id\n" +
					 "							INNER JOIN `order` c ON c.merchant_id=b.id\n" +
					 "				WHERE a.org_id in("+ StringUtils.join(ids,",") +")";
		List<Map<String, Object>> list = findForJdbc(sql);
		if(ListUtil.isEmpty(list)){
			return new Integer(list.get(0).get("merchantCount").toString());
		}
		return 0;
		
	}
	
	/**
	 * 获取当天订单总数
	 * @param ids
	 * @state  1:总数  0:活跃用户
	 * @return
	 */
	@Override
	public Integer getOrderNumCurrent(List<Integer> ids, Integer state){
		String str = "";
		if(state.equals(1)){
			str = "pay";
		}else{
			str = "unpay";
		}
			
		String sql = "select COUNT(1) orderNum from 0085_merchant_org a INNER JOIN `merchant` b ON a.merchant_id=b.id\n" +
					"							INNER JOIN `order` c ON c.merchant_id=b.id where a.org_id in("+  StringUtils.join(ids, ",") +") AND DATE(FROM_UNIXTIME(c.create_time)) = CURDATE() AND c.state = '"+ str +"'";
		List<Map<String, Object>> list = findForJdbc(sql);
		if(ListUtil.isEmpty(list)){
			return new Integer(list.get(0).get("orderNum").toString());
		}
		return 0;
	}
	
	/**
	 * 获取当前订单总金额
	 * @param ids
	 * @state  1:完成  0:完成
	 * @return
	 */
	@Override
	public Double getMoneyCurrent(List<Integer> orgIds, Integer state){
		String str = "";
		if(state.equals(1)){
			str = "pay";
		}else{
			str = "unpay";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select SUM(c.origin) origin from 0085_merchant_org a ");
		sql.append(" INNER JOIN `merchant` b ON a.merchant_id=b.id ");
		sql.append(" INNER JOIN `order` c ON c.merchant_id=b.id   ");
		sql.append(" WHERE c.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" AND c.state = '" + str + "' AND a.org_id in("+ StringUtils.join(orgIds, ",") +")");
		 
		List<Map<String, Object>> list = findForJdbc(sql.toString());
		if(ListUtil.isEmpty(list)){
			Object object = list.get(0).get("origin");
			if(object==null)return 0.00;
			return new Double(object.toString());
		}
		return 0.00;
	}
	
	/**
	 * 用户数
	 * @param orgIdsStr
	 * @param state 1:完成  0:完成
	 * @return	userCount	用户量
	 * 			oldUser		复购率
	 */
	@Override
	public Map<String, Object> getUserCount(String orgIdsStr, Integer state, SearchVo timeType) {
		String andState = " and o.pay_state = 'unpay' ";
		if(state.equals(1)){
			andState = " and o.state = 'confirm' AND  o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ";
		}
		
		String whereTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		/**
		 * 先获得用户量和新用户量
		 */
		StringBuffer sqlBuff = new StringBuffer(" SELECT IFNULL(COUNT(usc.user_id),0) userCount, IFNULL(COUNT(usc.total_order),0) newUser ");
		sqlBuff.append(" FROM( ");
		sqlBuff.append(" SELECT o.user_id, tus.total_order ");
		sqlBuff.append(" FROM `order` o ");
		sqlBuff.append(" LEFT JOIN 0085_merchant_org morg on o.merchant_id = morg.merchant_id ");
		sqlBuff.append(" LEFT JOIN tum_user_statistics tus ON tus.user_id = o.user_id AND tus.total_order = 1 ");
		sqlBuff.append(" WHERE o.user_id>0 ");
		sqlBuff.append(whereTime);
		sqlBuff.append(andState);
		sqlBuff.append(" AND morg.org_id IN ( ");
		sqlBuff.append(orgIdsStr);
		sqlBuff.append(" ) ");
		sqlBuff.append(" GROUP BY o.user_id ");
		sqlBuff.append(" )usc ");
		Map<String, Object> result = this.findOneForJdbc(sqlBuff.toString());
		return result;
	}
	
	/**(OvO)
	 * 获取按销售额排序的商家列表
	 * @param orgIds	要搜索的区域ID列表
	 * @param period	搜索时间条件
	 * @param page		页码
	 * @param rows		每页显示的记录数
	 * @return	id		商家ID
	 * 			ordnum			订单数
	 * 			takeAwayCount	外卖订单数
	 * 			scanCount		扫码订单数
	 * 			dineInCount		堂食订单
	 * 			origin			销售额
	 */
	private List<Map<String, Object>> getOrderedMerchantOriginList(List<Integer> orgIds, String period, Integer page, Integer rows){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT me.id, IFNULL(SUM(o.origin),0) origin, IFNULL(COUNT(o.id),0) ordnum ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 1 THEN 1 ELSE NULL END) takeAwayCount ");
		sql.append(" 		,COUNT(CASE WHEN order_type = 'scan_order' THEN 1 ELSE NULL END) scanCount ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 2 AND order_type <> 'scan_order' THEN 1 ELSE NULL END) dineInCount ");
		sql.append(" FROM (merchant me, 0085_merchant_org morg) LEFT JOIN `order` o ON o.merchant_id = me.id ");
		sql.append(" 	AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(period);
		sql.append(" WHERE me.id = morg.merchant_id AND me.is_delete = '0' AND morg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		sql.append(" GROUP BY me.id ");
		sql.append(" ORDER BY origin DESC, me.id ");
		return this.findForJdbc(sql.toString(), page, rows);
	}
	
	/**(OvO)
	 * 获取按销售额排序的商家列表(代理商用)
	 * @param userId	代理商用户ID
	 * @param period	搜索时间条件
	 * @param page		页码
	 * @param rows		每页显示的记录数
	 * @return	id				商家ID
	 * 			ordnum			订单数
	 * 			takeAwayCount	外卖订单数
	 * 			scanCount		扫码订单数
	 * 			dineInCount		堂食订单
	 * 			origin			销售额
	 */
	private List<Map<String, Object>> getOrderedMerchantOriginList4Agent(Integer userId, String period, Integer page, Integer rows){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT me.id, IFNULL(SUM(o.origin),0) origin, IFNULL(COUNT(o.id),0) ordnum ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 1 THEN 1 ELSE NULL END) takeAwayCount ");
		sql.append(" 		,COUNT(CASE WHEN order_type = 'scan_order' THEN 1 ELSE NULL END) scanCount ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 2 AND order_type <> 'scan_order' THEN 1 ELSE NULL END) dineInCount ");
		sql.append(" FROM (0085_merchant_info minfo, merchant me) ");
		sql.append(" 	LEFT JOIN `order` o ON o.merchant_id = me.id AND o.pay_state='pay' and o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(period);
		sql.append(" WHERE minfo.creator = ? AND minfo.platform_type = 2 ");
		sql.append(" 	AND me.id = minfo.merchant_id ");
		sql.append(" GROUP BY minfo.merchant_id ");
		sql.append(" ORDER BY origin DESC, me.id ");
		return this.findForJdbcParam(sql.toString(), page, rows, userId);
	}
	
	/**(OvO)
	 * 获取按销售额排序的机构列表
	 * @param orgIds	要搜索的区域ID
	 * @param period	搜索时间条件
	 * @param page		页码
	 * @param rows		每页显示的记录数
	 * @return	orgId			机构ID
	 * 			origin			销售额
	 * 			takeAwayCount	外卖订单数
	 * 			scanCount		自提订单数
	 * 			dineInCount		门店订单
	 * 			ordnum			订单数
	 */
	private List<Map<String, Object>> getOrderedOrgOriginList(List<Integer> orgIds, String period, Integer page, Integer rows){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT org.id orgId, IFNULL(sl.origin,0) origin, IFNULL(sl.ordnum,0) ordnum, IFNULL(sl.takeAwayCount,0) takeAwayCount ");
		sql.append(" 	,IFNULL(sl.scanCount,0) scanCount, IFNULL(sl.dineInCount,0) dineInCount ");
		sql.append(" FROM 0085_org org LEFT JOIN( ");
		sql.append(" 	SELECT morg.org_id, SUM(o.origin) origin, COUNT(o.id) ordnum ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 1 THEN 1 ELSE NULL END) takeAwayCount ");
		sql.append(" 		,COUNT(CASE WHEN order_type = 'scan_order' THEN 1 ELSE NULL END) scanCount ");
		sql.append(" 		,COUNT(CASE WHEN sale_type = 2 AND order_type <> 'scan_order' THEN 1 ELSE NULL END) dineInCount ");
		sql.append(" 	FROM `order` o, 0085_merchant_org morg ");
		sql.append(" 	WHERE o.merchant_id = morg.merchant_id ");
		sql.append(period);
		sql.append(" 		AND o.pay_state='pay' and o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	GROUP BY morg.org_id ");
		sql.append(" )sl ON org.id = sl.org_id ");
		sql.append(" WHERE org.id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		sql.append(" ORDER BY origin DESC, org.id ");
		return this.findForJdbc(sql.toString(), page, rows);
	}
	
	private Map<String, Object> getOrgDetailsById(Integer orgId, String period){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(DISTINCT o.user_id),0)userCount ");
		sql.append(" 	,(SELECT org_name FROM 0085_org WHERE id = morg.org_id) orgName ");
		sql.append(" 	,(SELECT COUNT(1) FROM merchant me, 0085_merchant_org morg WHERE me.id = morg.merchant_id AND morg.org_id = ? AND me.is_delete = 0)merchantCount ");
		sql.append(" 	,IFNULL((SELECT u.username ");
		sql.append(" 		FROM 0085_courier_position cp, 0085_courier_org corg, `user` u ");
		sql.append(" 		WHERE cp.position_id = 3 AND cp.courier_id = corg.courier_id AND u.id = corg.courier_id AND u.is_delete = 0 AND corg.org_id = morg.org_id LIMIT 1),'') admin ");
		sql.append(" FROM 0085_merchant_org morg, `order` o ");
		sql.append(" WHERE morg.org_id = ? ");
		sql.append(" 	AND morg.merchant_id = o.merchant_id ");
		sql.append(period);
		sql.append(" 	AND o.pay_state='pay' and o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		return this.findOneForJdbc(sql.toString(), orgId, orgId);
	}
	
	/**(OvO)
	 * 根据商家ID获取商家外卖订单份数，扫码订单份数，用户数
	 * @param merchantId	商家ID
	 * @param period		搜索时间条件
	 * @return	title		商家名称
	 * 			userCount	用户数
	 */
	private Map<String, Object> getMerchantDetailsById(Integer merchantId, String period){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(DISTINCT o.user_id),0) userCount ");
		sql.append(" 	,(SELECT m.title FROM merchant m WHERE m.id = ?)merchantName ");
		sql.append(" FROM `order` o ");
		sql.append(" WHERE o.merchant_id = ? ");
		sql.append(period);
		sql.append(" 	AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		return this.findOneForJdbc(sql.toString(), merchantId, merchantId);
	}
	
	/**(OvO)
	 * 将商家的外卖订单份数，扫码订单份数，用户数添加到商家信息列表中
	 * @param item		商家信息列表
	 * @param period	查询时间条件
	 * @param num		序号
	 */
	private void putDetailsIntoMerchantListItem(Map<String, Object> item, String period, Integer num){
		item.put("rank", num);
		Integer merchantId = Integer.valueOf(item.get("id").toString());
		BigDecimal origin = BigDecimal.valueOf(Double.parseDouble(item.get("origin").toString()));
		
		/**
		 * 根据商家ID获取商家其他信息
		 */
		Map<String, Object> merchantDetails = this.getMerchantDetailsById(merchantId, period);
		if(merchantDetails != null){
			item.put("merchantName", merchantDetails.get("merchantName"));
			item.put("userCount", merchantDetails.get("userCount"));
		}
		
		/**
		 * 算出客单价
		 */
		BigDecimal userCount = new BigDecimal(item.get("userCount").toString());
		if(userCount.compareTo(BigDecimal.ZERO) == 0){
			item.put("avg", 0);
		}else{
			BigDecimal avg = origin.divide(userCount, 2, BigDecimal.ROUND_HALF_UP);
			item.put("avg", avg);
		}
	}
	
	/**(OvO)
	 * 将机构的外卖订单份数，扫码订单份数，用户数添加到商家信息列表中
	 * @param item		机构信息列表
	 * @param period	查询时间条件
	 * @param num		序号
	 */
	private void putDetailsIntoOrgListItem(Map<String, Object> item, String period, Integer num){
		item.put("rank", num);
		Integer orgId = Integer.valueOf(item.get("orgId").toString());
		BigDecimal origin = BigDecimal.valueOf(Double.parseDouble(item.get("origin").toString()));
		
		/**
		 * 根据商家ID获取商家其他信息
		 */
		Map<String, Object> orgDetails = this.getOrgDetailsById(orgId, period);
		if(orgDetails != null){
			item.put("orgName", orgDetails.get("orgName"));
			item.put("userCount", orgDetails.get("userCount"));
			item.put("merchantCount", orgDetails.get("merchantCount"));
			item.put("admin", orgDetails.get("admin"));
		}
		
		/**
		 * 算出客单价
		 */
		BigDecimal userCount = new BigDecimal(item.get("userCount").toString());
		if(userCount.compareTo(BigDecimal.ZERO) == 0){
			item.put("avg", 0);
		}else{
			BigDecimal avg = origin.divide(userCount, 2, BigDecimal.ROUND_HALF_UP);
			item.put("avg", avg);
		}
	}
	
	/**
	 * 商家统计-网点排名
	 */
	@Override
	public List<Map<String, Object>>  getDotRank(List<Integer> list, SearchVo vo,Integer page,Integer rows) {
		
		StringBuilder newSql = new StringBuilder();
		
		String period = this.getPeriod(vo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT o.org_name , ");
		sql.append(" 	COUNT(morg.merchant_id) merchantCount , ");
		sql.append(" 	SUM(oo.orderNum) orderNum, ");
		sql.append(" 	COUNT(oo.merchant_id) - COUNT(m.merchant_id) as merchantOldCount, ");
		sql.append(" 	COUNT(m.merchant_id) as merchantNewCount ");
		sql.append(" FROM 0085_merchant_org morg INNER JOIN 0085_org o on morg.org_id = o.id ");
		sql.append(" left join ( ");
		sql.append(" 	select COUNT(o.id) as orderNum ,o.merchant_id from ");
		sql.append(" 	`order` o   ");
		sql.append(" 	where ");
		sql.append(" 	o.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" 	AND o.pay_state='pay' and o.state='confirm' and (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	GROUP BY o.merchant_id ");
		sql.append(" ) oo on morg.merchant_id = oo.merchant_id ");
		sql.append(" LEFT join ( ");
		sql.append(" 	select id merchant_id from merchant m where m.create_time = UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" )m on morg.merchant_id = m.merchant_id ");
		sql.append(" WHERE morg.org_id IN( ");
		sql.append(StringUtils.join(list, ","));
		sql.append(" )GROUP BY morg.org_id  ");
		sql.append(" ORDER BY COUNT(morg.merchant_id) DESC ");
		
		
		
		newSql.append(" SELECT ax.*,(ax.merchantCount - ax.merchantNewCount) merchantOldCount FROM ( ");
		newSql.append(" SELECT a.orgId,a.merchantCount, IFNULL(b.merchantNewCount,0) merchantNewCount,a.order_name,IFNULL(c.orderNum,0) orderNum ");
		newSql.append(" FROM ( ");
		newSql.append(" SELECT aa.org_id orgId ,COUNT(aa.merchant_id) merchantCount,bb.org_name order_name FROM 0085_merchant_org aa INNER JOIN 0085_org bb ON aa.org_id = bb.id ");
		newSql.append(" WHERE aa.org_id IN("+ StringUtils.join(list, ",") +") ");
		newSql.append(" GROUP BY aa.org_id) a ");
		newSql.append(" LEFT JOIN ");
		newSql.append(" (SELECT bb.org_id,COUNT(cc.id) merchantNewCount FROM 0085_merchant_org bb INNER JOIN merchant cc ON bb.merchant_id=cc.id  WHERE cc.create_time >= " + period + " GROUP BY bb.org_id) b ");
		newSql.append(" ON a.orgId=b.org_id ");
		newSql.append("	LEFT JOIN ( ");
		newSql.append(" SELECT COUNT(o.id) AS orderNum,e.org_id FROM 0085_merchant_org e,`order` o ");
		newSql.append(" WHERE e.merchant_id=o.merchant_id ");
		newSql.append(" AND	o.create_time >= ");
		newSql.append(period);
		newSql.append(" AND o.pay_state = 'pay' ");
		newSql.append(" AND o.state = 'confirm' ");
		newSql.append(" AND (o.rstate = 'norefund' OR o.rstate = 'normal') ");
		newSql.append(" GROUP BY e.org_id ) c ");
		newSql.append(" ON a.orgId = c.org_id ");
		newSql.append(" ORDER BY a.merchantCount DESC ");
		newSql.append(" ) ax ");
		
		
		return findForJdbcParam(newSql.toString(), page, rows);
		
		
	}

	/**(OvO)
	 * 根据入参构建SQL时间条件(订单表)
	 * @param vo	搜索条件VO
	 * @return	SQL的时间条件
	 */
	private String getPeriod(SearchVo vo){
		String period = vo.getTimeType();
		if("week".equals(period)){
			return " UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL WEEKDAY(curdate()) DAY)) ";
		}else if("month".equals(period)){
			return " UNIX_TIMESTAMP(DATE_SUB(curdate(),INTERVAL DAYOFMONTH(CURDATE())-1 DAY)) ";
		}
		return " UNIX_TIMESTAMP(CURDATE()) ";
	}
	
	@Override
	public Map<String, Object> getDecisionMakingReports(String orgIdsStr, SearchVo timeType) {
		// 检查入参, 防止SQL出现语法错误
		if(orgIdsStr == null || orgIdsStr.isEmpty()){
			orgIdsStr = "-1";
		}
		
		String beforeTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		StringBuffer sql = new StringBuffer(" select  IFNULL(sum(o.origin),0) moneyCurrent ,IFNULL(sum(o.orderNum),0) orderNumCurrent,IFNULL(COUNT(o.merchant_id),0) merchantCount ");
					 sql.append(" from ( ");
					 sql.append(" select SUM(o.origin) origin, COUNT(o.id) orderNum,o.merchant_id FROM `order` o  ");
					 sql.append(" left join 0085_merchant_org a on a.merchant_id=o.merchant_id ");
					 sql.append(" where o.state = 'confirm' AND  o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal')" );
//					 sql.append(" AND o.create_time >= ");
					 sql.append(beforeTime);
					 sql.append(" and a.org_id in(" + orgIdsStr + ") ");
					 sql.append(" GROUP BY o.merchant_id ) o ");
		List<Map<String, Object>> list = findForJdbc(sql.toString());
		if(ListUtil.isEmpty(list)){
			Map<String, Object> result = list.get(0);
			/**
			 * 往结果中添加总商家数
			 */
			result.putAll(this.getTotalMerchantCount(orgIdsStr));
			return result;
		}
		return new HashMap<String, Object>();
	}
	
	@Override
	public Map<String, Object> getOrderCountDetails(String orgIdsStr, Integer state, SearchVo timeType){
		String str = " o.pay_state = 'unpay' ";
		if(state != null && state.equals(1)){
			str = " o.state = 'confirm' AND  o.pay_state = 'pay' AND (o.rstate = 'norefund' OR o.rstate = 'normal') ";
		}
		
		String whereTime = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(CASE WHEN sale_type = 1 THEN 1 ELSE NULL END),0) takeAwayCount ");
		sql.append(" ,IFNULL(COUNT(CASE WHEN order_type = 'scan_order' THEN 1 ELSE NULL END),0) scanCount ");
		sql.append(" ,IFNULL(COUNT(CASE WHEN sale_type = 2 AND order_type <> 'scan_order' THEN 1 ELSE NULL END),0) dineInCount ");
		sql.append(" ,IFNULL(COUNT(DISTINCT o.courier_id),0) courierCount ");
		sql.append(" FROM `order` o, 0085_merchant_org morg WHERE ");
		sql.append(str);
		sql.append(whereTime);
		sql.append(" AND morg.org_id IN ( ");
		sql.append(orgIdsStr);
		sql.append(" ) AND o.merchant_id = morg.merchant_id ");
		Map<String, Object> result = this.findOneForJdbc(sql.toString());
		
		/**
		 * 如果查询未完成数据, 要重新统计快递员数量
		 */
		if(state == null || state.equals(0)){
			result.put("courierCount", this.getIdleCourier(orgIdsStr));
		}
		
		return result;
	}
	
	/**
	 * 决策报表未完成
	 */
	@Override
	public Map<String, Object> getDecisionMakingReportsNo(String orgIdsStr, SearchVo timeType) {
		// 检查入参, 防止SQL出现语法错误
		if(orgIdsStr == null || orgIdsStr.isEmpty()){
			orgIdsStr = "-1";
		}
		String timeWhere = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		StringBuffer sql = new StringBuffer(" select  IFNULL(sum(o.origin),0) moneyCurrent ,IFNULL(sum(o.orderNum),0) orderNumCurrent,IFNULL(COUNT(o.merchant_id),0) merchantCount ");
		 sql.append(" from 0085_merchant_org a ,( ");
		 sql.append(" select SUM(o.origin) origin, COUNT(o.id) orderNum,o.merchant_id FROM `order` o  ");
		 sql.append(" where o.pay_state = 'unpay' " );
		 sql.append(timeWhere);
		 sql.append(" GROUP BY o.merchant_id ) o ");
		 sql.append(" WHERE a.org_id in("+ orgIdsStr +") ");
		 sql.append(" AND a.merchant_id = o.merchant_id ");
		List<Map<String, Object>> list = findForJdbc(sql.toString());
		if(ListUtil.isEmpty(list)){
			Map<String, Object> result = list.get(0);
			/**
			 * 往结果中添加总商家数
			 */
			result.putAll(this.getTotalMerchantCount(orgIdsStr));
			return result;
		}
		return new HashMap<String, Object>();
	}
	
	/**(OvO)
	 * 获取区域内所有商家数量
	 * 使用此方法前要保证入参为非空字符串
	 * @param orgIdsStr	统计区域
	 * @return	totalMerchantCount	区域下所有商家数量
	 */
	private Map<String, Object> getTotalMerchantCount(String orgIdsStr){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT IFNULL(COUNT(m.id),0) totalMerchantCount ");
		sql.append(" FROM 0085_merchant_org morg, merchant m ");
		sql.append(" WHERE morg.org_id IN ( ");
		sql.append(orgIdsStr);
		sql.append(" ) ");
		sql.append(" 	AND m.is_delete = 0 ");
		sql.append(" 	AND morg.merchant_id = m.id ");
		return this.findOneForJdbc(sql.toString());
	}
	
	/**(OvO)
	 * 获取区域内空闲快递员(已上班但没有订单的快递员)数量
	 * @param orgIdsStr	统计区域(需要保证为非空字符串)
	 * @return
	 */
	private Integer getIdleCourier(String orgIdsStr){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(1) courierCount");
		sql.append(" FROM( ");
		sql.append(" 	SELECT * ");
		sql.append(" 	FROM( ");
		sql.append(" 		SELECT catd.user_id, catd.type ");
		sql.append(" 		FROM 0085_courier_attendance catd, 0085_courier_org corg ");
		sql.append(" 		WHERE catd.create_time >= UNIX_TIMESTAMP(CURDATE()) AND catd.user_id = corg.courier_id AND corg.org_id IN ( ");
		sql.append(orgIdsStr);
		sql.append(" 		) ");
		sql.append(" 		ORDER BY catd.create_time DESC ");
		sql.append(" 	)atd ");
		sql.append(" 	WHERE atd.user_id NOT IN ( ");
		sql.append(" 		SELECT o.courier_id ");
		sql.append(" 		FROM `order` o ");
		sql.append(" 		WHERE o.create_time >= UNIX_TIMESTAMP(CURDATE()) ");
		sql.append(" 		GROUP BY o.courier_id ");
		sql.append(" 	) ");
		sql.append(" 	GROUP BY atd.user_id ");
		sql.append(" )idc ");
		
		Map<String, Object> resultMap = this.findOneForJdbc(sql.toString());
		
		return Integer.valueOf(resultMap.get("courierCount").toString());
	}

	/**
	 * 品类报表
	 * @param orgIds    机构ID
	 * @param searchVo  
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCategoryByReports(List<Integer> orgIds,SearchVo searchVo) {
		
		//String timewhere = this.getTimeWhere4SQL(searchVo);
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ax.`name`,SUM(ax.orderNum)orderNum,SUM(ax.quantity)quantity,SUM(ax.syncQuantity) syncQuantity ");
		sql.append(" ,SUM(ax.restaurant)restaurant,SUM(userCount)userCount,SUM(origin)origin,ax.cid ");
		sql.append(" ,IFNULL(CAST((SUM(origin)/SUM(ax.userCount)) AS DECIMAL (10, 2)),0) avg ");
		sql.append(" FROM( ");
		sql.append(" SELECT DISTINCT b.id,a.`name`,a.id cid,COUNT(o.id) orderNum, ");
		sql.append(" IF(o.sale_type = 1,COUNT(o.id),0) quantity, ");
		sql.append(" IF(o.sale_type = 2 AND o.order_type <> 'scan_order' AND o.order_type <> 'supermarket_settlement' AND o.order_type <> 'supermarket',COUNT(o.id),0) syncQuantity, ");
		sql.append(" IF((o.sale_type = 2 AND (o.order_type = 'supermarket_settlement' OR o.order_type = 'supermarket')) OR (o.sale_type = 2 AND o.order_type = 'scan_order'),COUNT(o.id),0) restaurant, ");
		sql.append(" COUNT(DISTINCT o.user_id)userCount,SUM(o.origin)origin ");
		sql.append(" FROM category a,merchant b,0085_merchant_org c  ");
		sql.append(" ,(SELECT o.* FROM `order` o WHERE 1=1 "+ timewhere +" ");
		sql.append(" AND o.pay_state = 'pay' AND o.state = 'confirm' ");
		sql.append(" AND ((o.sale_type = 1 AND o.order_type = 'normal') ");
		sql.append(" OR (o.sale_type = 2 AND (o.order_type = 'scan_order' OR o.order_type = 'normal')) ");
		sql.append(" )) o  ");
		sql.append(" WHERE ");
		sql.append(" c.org_id IN ("+ StringUtils.join(orgIds,",") +")");
		sql.append(" AND c.merchant_id = b.id ");
		sql.append(" AND a.id = b.group_id ");
		sql.append(" AND a.zone = 'group' ");
		sql.append(" AND o.merchant_id = b.id ");
		sql.append(" GROUP BY b.id ");
		sql.append(" ORDER BY orderNum DESC ");
		sql.append(" ) ax GROUP BY ax.`name` ");
		sql.append(" ORDER BY orderNum DESC ");
		
		List<Map<String, Object>> list = findForJdbc(sql.toString());
		return list;
	}
	
	/**
	 * 品类报表详情列表
	 * @param orgIds	     机构ID
	 * @param searchVo	  
	 * @param categoryId  品类ID
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCategoryByReportsInfo(
			List<Integer> orgIds, SearchVo searchVo, Integer categoryId,Integer page,Integer rows) {
		//String timewhere = this.getTimeWhere4SQL(searchVo);
		String timewhere = SqlUtils.getTimeWhere4SQL(searchVo);
		StringBuilder sql = new StringBuilder();


		sql.append(" SELECT DISTINCT d.id,b.title, d.org_name orgName ,COUNT(o.id) orderNum, ");
		sql.append(" IF(o.sale_type = 1,COUNT(o.id),0) quantity, ");
		sql.append(" IF(o.sale_type = 2 AND o.order_type <> 'scan_order' AND o.order_type <> 'supermarket_settlement' AND o.order_type <> 'supermarket',COUNT(o.id),0) syncQuantity, ");
		sql.append(" IF((o.sale_type = 2 AND (o.order_type = 'supermarket_settlement' OR o.order_type = 'supermarket')) OR (o.sale_type = 2 AND o.order_type = 'scan_order'),COUNT(o.id),0) restaurant, ");
		sql.append(" COUNT(DISTINCT o.user_id)userCount,SUM(o.origin)origin, ");
		sql.append(" (SUM(origin)/COUNT(DISTINCT o.user_id)) avg ");
		sql.append(" FROM category a,merchant b,0085_merchant_org c ,0085_org d ");
		sql.append(" ,(SELECT o.* FROM `order` o WHERE 1=1 "+ timewhere +" ");
		sql.append(" AND o.pay_state = 'pay' AND o.state = 'confirm' ");
		sql.append(" AND ((o.sale_type = 1 AND o.order_type = 'normal') ");
		sql.append(" OR (o.sale_type = 2 AND (o.order_type = 'scan_order' OR o.order_type = 'normal')) ");
		sql.append(" )) o  ");
		sql.append(" WHERE ");
		sql.append(" c.org_id IN ("+ StringUtils.join(orgIds, ",") +")");
		sql.append(" AND c.org_id = d.id ");
		sql.append(" AND a.id = "+ categoryId +" ");
		sql.append(" AND c.merchant_id = b.id ");
		sql.append(" AND a.id = b.group_id ");
		sql.append(" AND	a.zone = 'group' ");
		sql.append(" AND o.merchant_id = b.id ");
		sql.append(" GROUP BY b.id ");
		sql.append(" ORDER BY orderNum DESC ");
		List<Map<String, Object>> list = findForJdbcParam(sql.toString(), page, rows);
		return list;
	}

	@Override
	public List<Map<String, Object>> getTransactionFive(List<Integer> orgIds){
		
		
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		
		String timeStr = "";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < 5; i++) {
				StringBuilder sql = new StringBuilder();
				timeStr =  SqlUtils.getDateBefore(i);
				
				sql.append(" SELECT IFNULL(SUM(o.origin),0)"+ numerals[i] +" ");
				sql.append(" FROM `order` o ,0085_merchant_org a WHERE a.org_id IN("+ StringUtils.join(orgIds, ",") +") ");
				if(i==0){
					sql.append(" AND o.create_time >= "+ timeStr +" ");
				}else{
					sql.append(" AND o.create_time >= "+ timeStr +" AND o.create_time < "+ SqlUtils.getDateBefore(i-1) +" ");
				}
				
				sql.append(" AND o.state = 'confirm' AND o.pay_state = 'pay' ");
				sql.append(" AND (o.rstate = 'normal ' OR o.rstate = 'norefund') ");
				Map<String, Object> templ = findOneForJdbc(sql.toString());
				map.putAll(templ);
			}
			list.add(map);
		} catch (Exception e) {}
		return list;
	}
	
	public String[] numerals = {"currentDay","currentDayBeforeOne","currentDayBeforeTwo","currentDayBeforeThree","currentDayBeforeFour"};
	
}
