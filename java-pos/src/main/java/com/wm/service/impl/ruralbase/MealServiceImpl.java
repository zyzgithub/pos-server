package com.wm.service.impl.ruralbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.AppTypeConstants;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.ruralbase.MealServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;
import com.wm.util.StringUtil;

@Service("mealService")
@Transactional
public class MealServiceImpl extends CommonServiceImpl implements MealServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(MealServiceImpl.class);

	@Autowired
	private OrderServiceI orderService;
	@Autowired
    private JpushServiceI jpushService;
	
	/**
	 * 备餐列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMealPreList(Integer merchantId,
			String type, Integer start, Integer num) {
		//先查缓存
		String key = CacheKeyUtil.getMealPreList(merchantId);
		Object object = AliOcs.getObject(key);
		if(object != null){
			logger.debug("已从memcache缓存得到备餐列表,merchantId={}", merchantId);
			return (List<Map<String, Object>>) object;
		}
		else {
			String sql = "SELECT id,order_num, full_num,status"
					+ " from 0085_dinein_order"
					+ " where  merchant_id=? "
					+ " AND order_type=? AND status=1 and DATE(NOW())=DATE(create_time)"
					+ "  GROUP BY order_id  ORDER BY sort_order desc, sort_time desc , create_time ASC  "
					+ " LIMIT 0,9";
			List<Map<String, Object>> mealPreList = this.findForJdbc(sql, merchantId, type);
			AliOcs.set(key, mealPreList, 60*60*24);
			return mealPreList;
		}
	}

	/**
	 * 出餐列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMealList(Integer merchantId, String type, Integer start, Integer num, int no) {
		//先查缓存
		String key = CacheKeyUtil.getMealList(merchantId);
		Object object = AliOcs.getObject(key);
		if(object != null){
			logger.debug("已从memcache缓存得到出餐列表,merchantId={}", merchantId);
			return (List<Map<String, Object>>) object;
		}
		else {
			String sql = "SELECT merchant_id,order_id,order_num, full_num, order_type,sort_order,`status`"
					+ " from 0085_dinein_order"
					+ " where  merchant_id=? "
					+ " AND order_type=? AND (status=1 or status=2) and DATE(NOW())=DATE(create_time)"
					+ "  GROUP BY order_id  ORDER BY sort_order desc, sort_time desc , create_time ASC  "
					+ " LIMIT ?,?";
			String sql1 = "UPDATE 0085_dinein_order SET sort_order=2 where order_num=? AND order_type=? ";
			String sql2 = "SELECT menu_name,quantity FROM 0085_dinein_order WHERE order_id=? AND order_type=? ";
	
			List<Map<String, Object>> merchantOrderMap = this.findForJdbc(sql, merchantId, type, start, num);
			List<Map<String, Object>> merchantOrderList = new ArrayList<Map<String, Object>>();
	
			if (merchantOrderMap != null) {
				for (int i = 0; i < merchantOrderMap.size(); i++) {
					Map<String, Object> m = merchantOrderMap.get(i);
					if (i < no) {
						if (!m.get("sort_order").equals(2)) {
							this.executeSql(sql1, m.get("order_num"), m.get("order_type"));
						}
						m.put("merchantOrderDetail", this.findForJdbc(sql2, m.get("order_id").toString(), m.get("order_type")));
					}
					merchantOrderList.add(m);
				}
			}
			
			AliOcs.set(key, merchantOrderList, 60*60*24);
			return merchantOrderList;
		}
	}

	/**
	 * 置顶
	 */
	@Override
	public boolean setTop(String orderNum, int merchantId) {
		orderNum=orderNum.toUpperCase();
		String regex="[A-Z]\\d+";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(orderNum);
		boolean rs =matcher.matches();
		if (rs && orderNum.length()>1) {
			orderNum =orderNum.substring(1);
		}
		String sqlNum="SELECT id from 0085_dinein_order "
				+ " where merchant_id=? and order_num=? and DATE(NOW())=DATE(create_time) and sort_order<2 and `status`<3";
		List<Map<String, Object>> list = this.findForJdbc(sqlNum, merchantId, orderNum);
		if (list !=null && list.size()>0) {
			String sql = "UPDATE 0085_dinein_order SET sort_order=1,sort_time=NOW() "
					+ " where merchant_id=? and order_num=? and DATE(NOW())=DATE(create_time)";

			//0085_dinein_order表变动清缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealPreList(merchantId)); //清除备餐列表缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealList(merchantId)); //清除出餐列表缓存
			
			this.executeSql(sql, merchantId, orderNum);
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 获得打印次数
	 * 
	 * @param orderId
	 * @return
	 */
	@Override
	public Long getPrintCount(Integer orderId) {
		String sql = "SELECT COUNT(1) from 0085_print_log where print_flag=1 and order_id=?";
		Long num = this.getCountForJdbcParam(sql, new Object[] { orderId });
		return num;

	}

	/**
	 * 获取电视排号
	*/
	@Override
	public Map<String, Object> getMealOrderNum(int merchantId, Integer id,
			String orderType, Integer start, Integer num) {
		if (id != null) {
			String sql = "update 0085_dinein_order set `status` = 3 , update_time=now() where id =? ";
			this.executeSql(sql, id);
		}
		String sql = "select DISTINCT order_id,full_num from 0085_dinein_order where merchant_id= ?"
				+ " and `status`=2 and order_type = ?"
				+ " ORDER BY update_time asc" + " LIMIT " + start + "," + num;

		Map<String, Object> findForJdbc = this.findOneForJdbc(sql, merchantId, orderType);

		if (findForJdbc != null) {
			id = (Integer) findForJdbc.get("order_id");
			String sql1 = "update 0085_dinein_order set `status` = 3 where order_id =? and order_type = ?";
			this.executeSql(sql1, id,orderType);
			//0085_dinein_order表变动清缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealPreList(merchantId)); //清除备餐列表缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealList(merchantId)); //清除出餐列表缓存
		}

		return findForJdbc;
	} 

	@Override
	public Integer updateDisplayStatus(int orderId,String orderType) {
		String sql1 = "update 0085_dinein_order set `status` = 3 where order_id =? and order_type = ?";
		Integer num = this.executeSql(sql1, orderId,orderType);
		
		String sql2 = "select merchant_id from `order` where id = ?";
		Map<String, Object> order = this.findOneForJdbc(sql2, orderId);
		Integer merchantId = Integer.parseInt(String.valueOf(order.get("merchant_id")));
		
		//0085_dinein_order表变动清缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealPreList(merchantId)); //清除备餐列表缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealList(merchantId)); //清除出餐列表缓存
		return num;
	}

	
	

	/**
	 * PAD出餐
	 */
	@Override
	public Integer updatePadList(Integer merchantId, Integer orderId, String type) {
		String sql = "update 0085_dinein_order set `status` =2 ,update_time=now() where order_id =? and order_type = ? and merchant_id = ?";
		Integer num = this.executeSql(sql, orderId, type, merchantId);
		
		//0085_dinein_order表变动清缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealPreList(merchantId)); //清除备餐列表缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealList(merchantId)); //清除出餐列表缓存
		return num;
	}

	@Override
	public void getPrintLog(OrderEntity orderEntity, AjaxJson j) {
		Integer orderId = orderEntity.getId(); // 获取订单号
		Integer merchantId = orderEntity.getMerchant().getWuser().getId(); // 商家ID
		String printCode = orderEntity.getMerchant().getPrintCode();
		String printStatus = j.getStateCode(); // 打印状态
		if (printStatus.equals("00")) {
			printStatus = "1";
		} else {
			printStatus = "0";
		}
		String sql = "insert into 0085_print_log(order_id, merchant_id,print_code,print_time,print_flag,operator)"
				+ " values(?,?,?,now(),?,'2')";
		this.executeSql(sql, orderId, merchantId, printCode, printStatus);
	}

	/**
	 * 乡村基扫码
	 */
	@Override
	public List<Map<String, Object>> qrCodeGetDineinOrder(Integer userId, Integer merchantId) {
		String sql = "SELECT dio.*"
				+ " FROM `order` AS o LEFT JOIN 0085_dinein_order AS dio ON o.id = dio.order_id"
				+ " WHERE o.user_id =?  AND o.merchant_id =?"
				+ " AND o.state = 'pay'  AND o.pay_state = 'pay'"
				+ " AND TO_DAYS(FROM_UNIXTIME(o.pay_time)) = TO_DAYS(NOW())"
				+ " AND dio.`status` = 0";
		return this.findForJdbc(sql, userId, merchantId);
	}

	/**
	 * 乡村基扫码更改状态
	 */
	@Override
	public void qrCodeUpdateDineinOrder(Integer orderId) {
		String sql = "update 0085_dinein_order set `status` =2 ,update_time=now() where order_id =?";
		this.executeSql(sql, orderId);
		OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
		//0085_dinein_order表变动清缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealPreList(order.getMerchant().getId())); //清除备餐列表缓存
		AliOcs.syncRemove(CacheKeyUtil.getMealList(order.getMerchant().getId())); //清除出餐列表缓存
	}
	
	/**
	 * PAD出餐
	 * @throws Exception 
	 */
	@Override
	public AjaxJson outMeal(Integer merchantId, Integer orderId, String type,String fullNum,String version) throws Exception {
		AjaxJson j = new AjaxJson();
		String sql = "update 0085_dinein_order set `status` =2 ,update_time=now() where order_id =? and order_type = ? and merchant_id = ?";
		Integer num = this.executeSql(sql, orderId, type, merchantId);
		
		if (num!=null && num>0) {
			//0085_dinein_order表变动清缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealPreList(merchantId)); //清除备餐列表缓存
			AliOcs.syncRemove(CacheKeyUtil.getMealList(merchantId)); //清除出餐列表缓存
			
			if (StringUtil.isEmpty(version)) {
				//极光推送
				Map<String, String> pushMap = new HashMap<String, String>();
				pushMap.put("appType", AppTypeConstants.APP_TYPE_RURALBASE);
				pushMap.put("pushType", "Message");
				pushMap.put("fullNum", fullNum);
				pushMap.put("status", "0");
				String title = "您有一条新的出餐信息";
				pushMap.put("title", title);
				pushMap.put("content", title);
				pushMap.put("voiceFile", "");
				jpushService.push(Integer.parseInt(type.trim() + merchantId), pushMap);
			}
			return j;
		}else {
			j.setStateCode("01");
			j.setMsg("出餐失败!");
			j.setSuccess(false);
		}
		return j;
		
	}
	
	/**
	 * PAD出餐
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> getMealStatus(Integer merchantId, Integer orderId, String type) {
		Map<String, Object> map=new HashMap<String, Object>();
		String sql = "SELECT status FROM 0085_dinein_order where order_id =? and order_type = ? and merchant_id = ? limit 0,1";
		map = this.findOneForJdbc(sql, orderId, type, merchantId);
		return map;
	}
	
	/**
	 * 电视排号
	 * @param merchantId
	 * @param id
	 * @param orderType
	 * @return
	 */
	public List<Map<String, Object>> TVOrderNumList(int merchantId,String orderType){
		String sql = "SELECT DISTINCT full_num from 0085_dinein_order where `status`=2 and merchant_id=? "
				+ "and order_type=? AND DATE(create_time)=DATE(NOW()) ORDER BY update_time ";
		List<Map<String, Object>> list = this.findForJdbc(sql, merchantId,orderType);
		return list;
	}
	
	/**
	 * 乡村基需求--堂食系统
	 * @param orderId
	 * @param order
	 */
	public void rarulbase(OrderEntity order) {
		String num ="";
		String orderNum = order.getOrderNum();
		if(!com.wm.util.StringUtil.isEmpty(orderNum)){
			if(orderNum.length() > 8){
				num = orderNum.substring(8);
			}
		}
		String menuSql = "SELECT m.print_type,o_m.quantity,o_m.total_price,m.name"
				+ " FROM order_menu o_m,menu m"
				+ " WHERE o_m.menu_id=m.id"
				+ " AND o_m.order_id=?";
		List<Map<String, Object>> menu = orderService.findForJdbc(menuSql, order.getId());
		logger.info("*****menu:******"+menu);
		if (null != menu && menu.size() > 0) {
			for (int i = 0; i < menu.size(); i++) {
				Map<String, Object> menuMap = menu.get(i);
				logger.info("menuMap:" + menuMap);
				String type = "A";
				// 生成堂食订单购物车每个商品插入0085_dinein_order
				if (null != menuMap.get("print_type")) {
					// 乡村基操作
					switch (menuMap.get("print_type").toString()) {
					case "1":
						type = "A";
						break;
					case "2":
						type = "B";
						break;
					case "3":
						type = "C";
						break;
					default:
						type = "A";
						break;
					}
					// 插入0085_dinein_order
					String fullName = type + "" + num;
					String ordersql = "INSERT INTO 0085_dinein_order (merchant_id,order_id,order_type,order_num,full_num,sort_order,"
							+ "create_time,status,update_time,remark,order_time,menu_name,quantity)"
							+ "VALUES(?,?,?,?,?,0,now(),1,now(),?,now(),?,?)";
					orderService.executeSql(ordersql, order.getMerchant().getId(), order.getId(), type, num,
							fullName, "", menuMap.get("name").toString(), menuMap.get("quantity").toString());
					
					//0085_dinein_order表变动清缓存
					AliOcs.syncRemove(CacheKeyUtil.getMealPreList(order.getMerchant().getId())); //清除备餐列表缓存
					AliOcs.syncRemove(CacheKeyUtil.getMealList(order.getMerchant().getId())); //清除出餐列表缓存
					
					logger.info("menuName**********:" + menuMap.get("name").toString() + "--" + menuMap.get("quantity").toString());
				}
			}
		}
	}
}
