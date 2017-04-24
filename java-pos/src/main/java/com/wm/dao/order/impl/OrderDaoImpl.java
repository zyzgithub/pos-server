package com.wm.dao.order.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Repository;

import com.courier_mana.common.Constants;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.controller.takeout.vo.OrderMenuVo;
import com.wm.controller.takeout.vo.OrderVo;
import com.wm.dao.order.OrderDao;
import com.wm.entity.order.OrderEntity;

@Repository("orderDao")
@SuppressWarnings("unchecked")
public class OrderDaoImpl extends GenericBaseCommonDao<OrderEntity, Integer> implements OrderDao{

	@Override
	public OrderVo queryById(Integer orderId, Integer userId) {
		// TODO Auto-generated method stub
		String sql = "select o.id as orderId, m.id as merchantId, o.courier_id as courierId, o.rstate as restate, "
				+ "m.title as merchantName, o.state as state, o.origin as origin, o.online_money as onlineMoney, "
				+ "o.credit as creditMoney, o.score_money as scoreMoney  ,o.realname as name, "
				+ "o.mobile as mobile, o.address as address, o.invoice as invoice, o.pay_type as payType, "
				+ "o.order_type as orderType, o.sale_type as saleType, o.order_num as orderNum, "
				+ "o.pay_id as payId, o.time_remark as timeRemark, count(c.id) as hasComment, o.delivery_fee as deliveryFee, "
				+ "o.card as couponsMoney "
				+ "from `order` as o "
				+ "left join merchant as m "
				+ "on o.merchant_id = m.id "
				+ "left join 0085_order_comment as c "
				+ "on o.id = c.order_id and c.comment_display = ? "
				+ "where o.id = ? and o.user_id = ? "
				+ "group by o.id";
		SQLQuery query = createSqlQuery(sql, "Y", orderId, userId);
		query.setResultTransformer(Transformers.aliasToBean(OrderVo.class));
		Object obj = query.uniqueResult();
		if (null != obj) {
			return (OrderVo) obj;
		}
		return null;
	}

	@Override
	public List<OrderMenuVo> queryOrderMenusById(Integer orderId) {
		// TODO Auto-generated method stub
		String sql = "select o.quantity as quantity, o.price as price, "
				+ "o.total_price as totalPrice, m.name as name "
				+ "from order_menu as o "
				+ "left join menu as m "
				+ "on o.menu_id = m.id "
				+ "where o.order_id = ?";
		SQLQuery query = createSqlQuery(sql, orderId);
		query.setResultTransformer(Transformers.aliasToBean(OrderMenuVo.class));
		return query.list();
	}
	
/*	@Override
	public PageList findOrderList(OrderEntityVo orderEntity, int page, int rows) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select o from OrderEntity o where 1=1 ");
		if (StringUtil.isNotEmpty(orderEntity.getId())) {
			sql.append(" and id = ");
			sql.append(orderEntity.getId());
		}
		if (StringUtil.isNotEmpty(orderEntity.getPayId())) {
			sql.append(" and payId = ");
			sql.append("'");
			sql.append(orderEntity.getPayId());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getPayType())) {
			sql.append(" and pay_type = ");
			sql.append("'");
			sql.append(orderEntity.getPayType());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getUserId())) {
			sql.append(" and user_id = ");
			sql.append(orderEntity.getUserId());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCourierId())) {
			sql.append(" and courier_id = ");
			sql.append(orderEntity.getCourierId());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCityId())) {
			sql.append(" and city_id = ");
			sql.append(orderEntity.getCityId());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCardId())) {
			sql.append(" and card_id = ");
			sql.append("'");
			sql.append(orderEntity.getCardId());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getStatus())) {
			sql.append(" and status = ");
			sql.append("'");
			sql.append(orderEntity.getStatus());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getState())) {
			sql.append(" and state = ");
			sql.append("'");
			sql.append(orderEntity.getState());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getRstate())) {
			sql.append(" and rstate = ");
			sql.append("'");
			sql.append(orderEntity.getRstate());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getRetime())
				&& !orderEntity.getRetime().equals("0")) {
			sql.append(" and retime = ");
			sql.append(orderEntity.getRetime());
		}
		if (StringUtil.isNotEmpty(orderEntity.getRealname())) {
			sql.append(" and realname = ");
			sql.append("'");
			sql.append(orderEntity.getRealname());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getMobile())) {
			sql.append(" and mobile = ");
			sql.append("'");
			sql.append(orderEntity.getMobile());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getAddress())) {
			sql.append(" and address = ");
			sql.append("'");
			sql.append(orderEntity.getAddress());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getOnlineMoney())) {
			sql.append(" and online_money = ");
			sql.append(orderEntity.getOnlineMoney());
		}
		if (StringUtil.isNotEmpty(orderEntity.getOrigin())) {
			sql.append(" and origin = ");
			sql.append(orderEntity.getOrigin());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCredit())) {
			sql.append(" and credit = ");
			sql.append(orderEntity.getCredit());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCard())) {
			sql.append(" and card = ");
			sql.append(orderEntity.getCard());
		}
		if (StringUtil.isNotEmpty(orderEntity.getCreateTime())
				&& !orderEntity.getCreateTime().equals("0")) {
			sql.append(" and create_time = ");
			sql.append(orderEntity.getCreateTime());
		}
		if (StringUtil.isNotEmpty(orderEntity.getPayTime())
				&& !orderEntity.getPayTime().equals("0")) {
			sql.append(" and pay_time = ");
			sql.append(orderEntity.getPayTime());
		}
		if (StringUtil.isNotEmpty(orderEntity.getMerchantId())) {
			sql.append(" and merchant_id = ");
			sql.append(orderEntity.getMerchantId());
		}
		if (StringUtil.isNotEmpty(orderEntity.getOrderType())) {
			sql.append(" and order_type = ");
			sql.append("'");
			sql.append(orderEntity.getOrderType());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getIfCourier())) {
			sql.append(" and ifCourier = ");
			sql.append("'");
			sql.append(orderEntity.getIfCourier());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getPayState())) {
			sql.append(" and pay_state = ");
			sql.append("'");
			sql.append(orderEntity.getPayState());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getSaleType())) {
			sql.append(" and sale_type = ");
			sql.append("'");
			sql.append(orderEntity.getSaleType());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getOrderNum())) {
			sql.append(" and order_num = ");
			sql.append("'");
			sql.append(orderEntity.getOrderNum());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getOutTraceId())) {
			sql.append(" and out_trace_id = ");
			sql.append("'");
			sql.append(orderEntity.getOutTraceId());
			sql.append("'");
		}
		if (StringUtil.isNotEmpty(orderEntity.getFromType())) {
			sql.append(" and from_type = ");
			sql.append("'");
			sql.append(orderEntity.getFromType());
			sql.append("'");
		}
		
		Query q = createHqlQuery(sql.toString());
		q.setFirstResult((page-1)*rows);
		q.setMaxResults(rows);
		List<OrderEntity> list = q.list();
		List<OrderEntity> list = this.findObjForJdbc(sql.toString(), page,
				rows, OrderEntity.class);
		Dialect dialect = new Dialect();
		String countSql = dialect.getCountString(sql.toString());
		List<Map<String, Object>> countList = this.findForJdbc(countSql);
		Map<String, Object> map = countList.get(0);
		String count = map.get("count").toString();
		PageList pageList = new PageList(rows, page, Integer.parseInt(count),
				list);
		return pageList;
	}*/

	@Override
	public AddressDetailVo queryLastedUserInfo(Integer userId, Integer saleType) {
		// TODO Auto-generated method stub
		String sql =  " SELECT o.realname name, o.mobile mobile "
					+ " FROM `order` o "
					+ " WHERE o.user_id = ? AND o.sale_type = ? AND o.pay_state = ? "
					+ " ORDER BY o.pay_time DESC ";
		SQLQuery query = createSqlQuery(sql, userId, saleType, "pay");
		query.setFirstResult(0).setMaxResults(1);
		query.setResultTransformer(Transformers.aliasToBean(AddressDetailVo.class));
		List<AddressDetailVo> list = query.list();
		if(list.size()>0) {
			AddressDetailVo vo = list.get(0);
			if(StringUtils.isEmpty(vo.getName()) || StringUtils.isEmpty(vo.getMobile()))
				return null;
			return vo;
		}
		return null;
	}
	
	@Override
	public Integer createSupplyChainOrder(Integer cityId, Integer merchantId, Double origin){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String now = dateFormat.format(new Date());
		// String deliyTime = dateFormat.format(new Date(System.currentTimeMillis()  + 1000 * 60 * 30));
		String sql = "INSERT INTO `order`(user_id, time_remark, city_id,merchant_id,state,origin,create_time,order_type,title,sale_type, from_type, pay_state)"
                + " values(0,'" + now + "-" + now + "', ?,?,'accept',?,UNIX_TIMESTAMP(), 'normal', '供应链订单',1, '" + Constants.SUPPLYCHAIN_ORDER + "', 'pay')";
        this.executeSql(sql, cityId, merchantId, origin);
        
        sql = "select last_insert_id() lastInsertId";
        Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
        return Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
	}
	
	
	@Override
	public boolean setPayIdAndOrderNum(Integer orderId, String payId, String orderNum){
        int rows = executeSql("update `order` set pay_id=?,order_num=? where id=?", payId, orderNum, orderId);
        return rows == 1;
	}
	
	@Override
	public void setOrderDeliveryState(Integer orderId){
		String sql = "update `order` o set o.state = ?, o.delivery_time = ? where o.id = ?";
		this.executeSql(sql, "delivery", DateUtils.getSeconds(), orderId);
	}
	
	@Override
	public void setOrderConfirmState(Integer orderId){
		String sql = "update `order` o set o.state=?, o.complete_time=? where o.id=?";
		this.executeSql(sql, "confirm", DateUtils.getSeconds(), orderId);
	}
	
	
	@Override
	public List<Map<String, Object>> getMenus(Integer orderId){
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT o_m.price,o_m.quantity, o_m.menu_id, o_m.total_price, o_m.sales_promotion ");
		queryBuilder.append(" from order_menu o_m  ");
        queryBuilder.append(" where o_m.order_id = ?");
        return findForJdbc(queryBuilder.toString(), orderId);
        
	}
	
	@Override
	public List<Map<String, Object>> getSupermarketNoPosPayedOrders(Integer merchantId, String beginTime, String endTime){
		StringBuilder sql = new StringBuilder();
		sql.append(" select o.id , o.pay_id, o.pay_type, o.sale_type, o.order_type, o.from_type, o.create_time, o.complete_time, o.origin ");
		sql.append(" from `order` o");
		sql.append(" where o.merchant_id = ? ");
		sql.append("	and o.pay_state = 'pay'");
		sql.append("	and o.rstate <> 'berefund'");
		sql.append(" 	and o.order_type <> 'supermarket' ");
		sql.append(" 	and o.pay_time >= UNIX_TIMESTAMP(?) and o.pay_time < UNIX_TIMESTAMP(?)");
		return this.findForJdbc(sql.toString(), merchantId, beginTime, endTime);
	}
	
	@Override
	public List<Map<String, Object>> getSupermarketPosPayedOrders(Integer cashierId, String beginTime, String endTime){
		StringBuilder sql = new StringBuilder();
		sql.append(" select o.id , o.pay_id, o.pay_type, o.sale_type, o.order_type, o.from_type, o.create_time, o.complete_time, o.origin ");
		sql.append(" from `order` o, 0085_cashier_order co");
		sql.append(" where co.order_id=o.id ");
		sql.append("	and co.cashier_id = ?");
		sql.append("	and o.pay_state = 'pay'");
		sql.append("	and o.rstate <> 'berefund'");
		sql.append(" 	and o.pay_time >= UNIX_TIMESTAMP(?) and o.pay_time < UNIX_TIMESTAMP(?)");
		return this.findForJdbc(sql.toString(), cashierId, beginTime, endTime);
	}


	@Override
	public Map<String, Object> getSupermarketRefundOrder(Integer cashierId,
			String beginTime, String endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(o.id) num, sum(o.origin) money ");
		sql.append(" from 0085_cashier_order co");
		sql.append(" left join `order` o on o.id = co.order_id" );
		sql.append(" where o.merchant_id = ? ");
		sql.append("	and o.rstate = 'berefund'");
		sql.append(" 	and o.pay_time >= UNIX_TIMESTAMP(?) and o.pay_time < UNIX_TIMESTAMP(?)");
		return findOneForJdbc(sql.toString(), cashierId, beginTime, endTime);
	}

	@Override
	public Map<String, Object> getSupermarketNoCodeOrder(Integer cashierId, String beginTime, String endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(o.id) num, sum(o.origin) money ");
		sql.append(" from `order` o, 0085_cashier_order co");
		sql.append(" where co.cashier_id = ? ");
		sql.append("	and o.id = co.order_id");
		sql.append("	and o.order_type = 'supermarket_codefree'");
		sql.append("	and o.pay_state = 'pay'");
		sql.append("	and o.rstate <> 'berefund'");
		sql.append(" 	and o.pay_time >= UNIX_TIMESTAMP(?) and o.pay_time < UNIX_TIMESTAMP(?)");
		Map<String, Object> resultMap = findOneForJdbc(sql.toString(), cashierId, beginTime, endTime);
		if(resultMap == null || resultMap.get("money") == null){
			resultMap = new HashMap<String, Object>();
			resultMap.put("num", 0);
			resultMap.put("money", 0.00);
		}
		return resultMap;
	}


	@Override
	public List<Map<String, Object>> getBasicPosOrderModifyOrder(Integer merchantId, String beginTime, String endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(po.id) num, sum(po.modify_money) money, ");
		sql.append(" 	case po.modify_type when 1 then '免单'");
		sql.append(" 	when 2 then '整单打折'");
		sql.append(" 	when 3 then '抹零'");
		sql.append(" 	when 4 then '金额修改'");
		sql.append(" 	else '其他' end modifyType ");
		sql.append(" 	from pos_order_modify_money_detail po");
		sql.append(" 	left join `order` o on o.id = po.order_id");
		sql.append(" 	where po.merchant_id = ?");
		sql.append("		and o.pay_state = 'pay'");
		sql.append("		and o.rstate <> 'berefund'");
		sql.append(" 		and o.pay_time >= UNIX_TIMESTAMP(?) and o.pay_time < UNIX_TIMESTAMP(?) ");
		sql.append(" group by po.modify_type");
		return findForJdbc(sql.toString(), merchantId, beginTime, endTime);
	}
}
