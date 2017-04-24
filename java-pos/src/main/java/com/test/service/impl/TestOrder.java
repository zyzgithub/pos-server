package com.test.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import jodd.util.MathUtil;

import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.test.service.TestOrderI;
import com.wm.entity.order.OrderEntity;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.util.AliOcs;

@Service("testOrder")
public class TestOrder extends CommonServiceImpl implements TestOrderI{
	
	private static final Logger logger = LoggerFactory.getLogger(TestOrder.class);
	
	private static final String takeoutOrderData = "201607TakeoutOrders.data";
	private static final String withdrawData = "201607Withdraw.data";
	
	private static final String startDate = "2016-07-01";
	
	private static final String copyOrder = "insert into `order`(`pay_id`,  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "`create_time`,  `pay_time`,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  `merchant_id`,  `score_money`,  `score`,  "
			+ "`order_type`,  `access_time`,  `delivery_time`,  `complete_time`,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "`delivery_done_time`,  `pay_state`,  `sale_type`,  `order_num`,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id`) select CONCAT(left(pay_id, 10),?),  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "?,  ?,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  `merchant_id`,  `score_money`,  `score`,  "
			+ "`order_type`,  ?,  ?,  ?,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "?,  `pay_state`,  `sale_type`,  ?,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id` from `order` where id=?";
	
	private static final String copyOrder1 = "insert into `order`(`pay_id`,  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "`create_time`,  `pay_time`,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  `merchant_id`,  `score_money`,  `score`,  "
			+ "`order_type`,  `access_time`,  `delivery_time`,  `complete_time`,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "`delivery_done_time`,  `pay_state`,  `sale_type`,  `order_num`,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id`) select CONCAT(left(pay_id, 10),?),  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "?,  ?,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  ?,  `score_money`,  `score`,  "
			+ "`order_type`,  ?,  ?,  ?,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "?,  `pay_state`,  `sale_type`,  ?,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id` from `order` where id=?";
	
	private static final String copyOrderMenu = "insert into `order_menu`(order_id,menu_id,quantity,price,total_price,state,"
			+ " promotion_money,sales_promotion,menu_promotion_id) select ?,menu_id,quantity,price,total_price,state,"
			+ " promotion_money,sales_promotion,menu_promotion_id from order_menu where order_id=?";
	
	private static final String copyOrderIncome = "insert into `order_income`(order_id,pay_id,create_time,pay_time,state,money,"
			+ " origin,delivery_money,`type`, merchant_id) select ?,(select pay_id from `order` where id=?),?,?,state,money,"
			+ " origin,delivery_money,`type`,0 from order_income where order_id=?";
	
	
	private static final String createWithdraw = "insert into withdrawals (`user_id`,`money`,`submit_time`,`complete_time`,`state`,"
			+ " `bankcard_id`,`user_type`,`before_money`,`after_money`) "
			+ " values (?,?,?,?,'done',(select id from bank_card where user_id=? and `default`='Y'),'merchant',?,?) ";
	
	private static final String randomOrder  = "SELECT id from `order` o where o.order_type='scan_order' and o.state='confirm' "
			+ " and o.online_money BETWEEN ? and ? and o.create_time < ? order by o.create_time asc limit 100";
	
	public static final String selectSourceOrderIdsql = "select o.id from `order` o where o.merchant_id = ? "
			+ " and o.state='confirm' and FROM_UNIXTIME(o.create_time, '%Y%m%d')=?";
	private static final String copyMarketOrder = "insert into `order`(`pay_id`,  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "`create_time`,  `pay_time`,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  `merchant_id`,  `score_money`,  `score`,  "
			+ "`order_type`,  `access_time`,  `delivery_time`,  `complete_time`,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "`delivery_done_time`,  `pay_state`,  `sale_type`,  `order_num`,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id`) select CONCAT(left(pay_id, 4),?),  `pay_type`,  `user_id`,  "
			+ "`courier_id`,  `city_id`,  `card_id`,  `status`,  `state`,  `rstate`,  `rereason`,  `retime`,  "
			+ "`realname`,  `mobile`,  `address`,  `online_money`,  `origin`,  `credit`,  `card`,  `remark`,  "
			+ "`create_time`,  `pay_time`,  `comment_content`,  `comment_display`,  `comment_taste`,  `comment_speed`,  "
			+ "`comment_service`,  `comment_courier`,  `comment_time`,  ?,  `score_money`,  `score`,  "
			+ "`order_type`,  `access_time`,  `delivery_time`,  `complete_time`,  `urgent_time`,  `title`,  `ifCourier`,  "
			+ "`delivery_done_time`,  `pay_state`,  `sale_type`,  `order_num`,  `out_trace_id`,  `time_remark`,  "
			+ "`cook_done_time`,  `cook_done_code`,  `start_time`,  `comment_courier_content`,  `start_send_time`,  "
			+ "`end_send_time`,  `user_address_id`,  `invoice`,  `from_type`,  `delivery_fee`,  `cost_lunch_box`,  "
			+ "`member_discount_money`,  `merchant_member_discount_money`,  `dine_in_discount_money`,  `recharge_src`,  "
			+ "`invite_id`,  `agent_id`,  `is_merchant_delivery`,  `flash_order_id` from `order` where id=?";
	
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	
	
	public void testCopyOrder() {
		logger.info("================= start copy data start =====================");
		this.copyOrders();
		this.createWithdraws();
		logger.info("================= start copy data end   =====================");
	}
	
	
	public boolean testCopyOrder1(Integer merchantId, Integer money) {
		long time = new DateTime(DateTime.now().minusMonths(2).toString("yyyy-MM-dd")).getMillis();
		List<Map<String, Object>> list = orderIncomeService.findForJdbc(randomOrder, money, money + 50, time);
		if(!CollectionUtils.isEmpty(list) && list.size() > 0){
			int size = list.size() - 1;
			int index = MathUtil.randomInt(1, size);
			Map<String, Object> map = list.get(index);
			Integer orderId = Integer.parseInt(map.get("id").toString());
			Long[] ret = this.copyOrder1(orderId, merchantId);
			Long newId = ret[0];
			if(newId != null){
				OrderEntity order = orderIncomeService.getEntity(OrderEntity.class, newId.intValue());
				try {
					orderIncomeService.createOrderIncome(order);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 复制订单流程数据
	 * @param orderId
	 * @return
	 */
	private void copyOrders(){
		int sumFail = 0;
		int sumSuccess = 0;
		Set<Integer> orderIds = loadTakeoutOrderData();
		for(Integer orderId : orderIds){
			long createTime = randomTime();
			Long[] resultArray = this.copyOrder(orderId, createTime);
			if(resultArray[0] != null){
				sumSuccess++;
				boolean flag = this.copyOrderMenu(orderId, resultArray[0].intValue());
				if(!flag){
					logger.error("copyOrderMenu failed !!! orderId:{}, newOrderId:{}", orderId, resultArray[0]);
				}
				flag = this.copyOrderIncome(orderId, resultArray[0].intValue(), resultArray[1]);
				if(!flag){
					logger.error("copyOrderIncome failed !!! orderId:{}, newOrderId:{}", orderId, resultArray[0]);
				}
			} else {
				logger.error("copyOrder failed !!! orderId:{} not exists !!!", orderId);
				sumFail++;
			}
		}
		logger.info("copyOrders size:{}, sumFail:{}, sumSuccess:{}", orderIds.size(), sumFail, sumSuccess);
	}
	
	/**
	 * 批量创建提现记录
	 * @return
	 */
	private void createWithdraws(){
		int sumFail = 0;
		int sumSuccess = 0;
		List<Map<String, String>> withdraws = loadFlowData();
		for(Map<String, String> withdraw : withdraws){
			Integer userId = Integer.parseInt(withdraw.get("userId").toString());
			Double money = Double.parseDouble(withdraw.get("money").toString());
			Long flowId = this.createWithdraw(userId, money);
			if(flowId == null){
				sumFail++;
			} else {
				sumSuccess++;
			}
		}
		logger.info("createWithdraws size:{}, sumFail:{}, sumSuccess:{}", withdraws.size(), sumFail, sumSuccess);
	}
	
	/**
	 * 造单笔提现记录
	 * @param userId
	 * @param money
	 */
	private Long createWithdraw(Integer userId, Double money) {
		Long flowId = null;
		long createTime = randomTime();
		String nextDateTime = new DateTime(createTime).plusDays(1).toString("yyyy-MM-dd");
		int randomHour = MathUtil.randomInt(10, 12);
		int randomMin = MathUtil.randomInt(0, 59);
		long completeTime = new DateTime(nextDateTime).plusHours(randomHour).plusMillis(randomMin).getMillis();
		double postMoney = MathUtil.randomLong(20, 299);
		double preMoney = postMoney + money;
		try {
			flowId = this.insertBySql(createWithdraw, userId, money, createTime/1000, completeTime/1000, userId, preMoney, postMoney);
			if(flowId != null){
				logger.info("createWithdraw flowId:{}, userId:{}, money:{}", flowId, userId, money);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createWithdraw failed !!! userId:{}", userId);
		}
		return flowId;
	}

	/**
	 * 复制订单表
	 * @param 源订单ID
	 * @return 新的订单ID
	 */
	private Long[] copyOrder(Integer orderId, long createTime) {
		String payId = hash(12);
		DateTime createDateTime = new DateTime(createTime);
		int randomValue = MathUtil.randomInt(10, 20);
		long payTime = createDateTime.plusSeconds(randomValue).getMillis();
		long accessTime = createDateTime.plusSeconds(randomValue + 12).getMillis();
		randomValue = MathUtil.randomInt(1, 15);
		long deliveryTime = createDateTime.plusMinutes(randomValue).getMillis();
		int completeMinus = MathUtil.randomInt(20, 40);
		long completeTime = createDateTime.plusMinutes(completeMinus).getMillis();
		long deliveryDoneTime = createDateTime.plusMinutes(completeMinus).getMillis();
		String orderNum = createDateTime.toString("yyyyMMdd") + MathUtil.randomInt(1, 30);
		try {
			Long newOrderId = this.insertBySql(copyOrder, payId, createTime/1000, payTime/1000, accessTime/1000, deliveryTime/1000, completeTime/1000, deliveryDoneTime/1000, orderNum, orderId);
			if(newOrderId != null){
				logger.info("copyOrder originOrderId:{}, newOrderId:{}, orderNum:{}, createDateTime:{}", orderId, newOrderId, orderNum, createDateTime.toString("yyyy-MM-dd HH:mm:ss"));
			}
			return new Long[]{newOrderId, completeTime};
		} catch (ConstraintViolationException e) {
			logger.error("order exist !!!orderId:{}", orderId);
		} catch (Exception e) {
			logger.error("copyOrder failed !!! orderId:{}", orderId);
		}
		return new Long[]{null, completeTime};
	}
	
	private Long[] copyOrder1(Integer orderId, Integer merchantId) {
		String payId = hash(12);
		long createTime = System.currentTimeMillis();
		DateTime createDateTime = new DateTime(createTime);
		int randomValue = MathUtil.randomInt(10, 20);
		long payTime = createDateTime.plusSeconds(randomValue).getMillis();
		long accessTime = createDateTime.plusSeconds(randomValue + 12).getMillis();
		randomValue = MathUtil.randomInt(1, 15);
		long deliveryTime = 0;
		int completeMinus = MathUtil.randomInt(20, 40);
		long completeTime = createDateTime.plusMinutes(completeMinus).getMillis();
		long deliveryDoneTime = createDateTime.plusMinutes(completeMinus).getMillis();
		String orderNum = AliOcs.genOrderNum(merchantId.toString());
		try {
			Long newOrderId = this.insertBySql(copyOrder1, payId, createTime/1000, payTime/1000, merchantId, accessTime/1000, deliveryTime/1000, completeTime/1000, deliveryDoneTime/1000, orderNum, orderId);
			return new Long[]{newOrderId, completeTime};
		} catch (ConstraintViolationException e) {
			logger.error("order exist !!!orderId:{}", orderId);
		} catch (Exception e) {
			logger.error("copyOrder failed !!! orderId:{}", orderId);
		}
		return new Long[]{null, completeTime};
	}
	
	
	
	/**
	 * 复制订单-商品关联表
	 * @param originOrderId 源订单ID
	 * @param newOrderId 新的订单ID
	 * @return
	 */
	private boolean copyOrderMenu(Integer originOrderId, Integer newOrderId) {
		logger.info("copyOrderMenu originOrderId:{}, newOrderId:{}", originOrderId, newOrderId);
		try {
			Integer rows = this.executeSql(copyOrderMenu, newOrderId, originOrderId);
			if(rows > 0){
				logger.info("copyOrderMenu originOrderId:{}, rows:{}", originOrderId, rows);
				return true;
			} else {
				logger.warn("copyOrderMenu originOrderId:{} none data!!!", originOrderId);
				return false;
			}
		} catch (Exception e) {
			logger.error("copyOrderMenu failed !!! orderId:{}", originOrderId);
		}
		return false;
	}
	
	/**
	 * 复制预收入表
	 * @param originOrderId
	 * @param newOrderId
	 * @param createTime
	 * @return
	 */
	private boolean copyOrderIncome(Integer originOrderId, Integer newOrderId, long createTime){
		logger.info("copyOrderIncome originOrderId:{}, newOrderId:{}", originOrderId, newOrderId);
		DateTime createDateTime = new DateTime(createTime);
		int randomMins = MathUtil.randomInt(1, 10);
		long payTime = new DateTime(createDateTime.plusDays(1).toString("yyyy-MM-dd")).plusMillis(randomMins).getMillis();
		try {
			Integer rows = this.executeSql(copyOrderIncome, newOrderId, originOrderId, createTime/1000, payTime/1000, originOrderId);
			logger.info("copyOrderIncome originOrderId:{}, rows:{}", originOrderId, rows);
			if(rows == 1){
				return true;
			} else {
				logger.warn("copyOrderIncome originOrderId:{} none data!!!", originOrderId);
				return false;
			}
		} catch (Exception e) {
			logger.error("copyOrderIncome failed !!!", e);
		}
		return false;
	}
	
	
	/**
	 *  加载原始数据
	 * @return 所有外卖订单ID
	 */
	public static Set<Integer> loadTakeoutOrderData() {
		Set<Integer> originOrderIds = new HashSet<Integer>();
		String filePath = TestOrder.class.getResource("").getPath().replace("file:", "") + takeoutOrderData; // 文件和该类在同个目录下
		logger.info("filePath:{}", filePath);
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			String orderId = null;
			while ((orderId = reader.readLine()) != null) {
				originOrderIds.add(Integer.parseInt(orderId.trim()));
			}
			logger.info("originOrderIds Size:{}", originOrderIds.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return originOrderIds;
	}
	
	public static List<Map<String, String>> loadFlowData() {
		List<Map<String, String>> flows = new ArrayList<Map<String, String>>();
		String filePath = TestOrder.class.getResource("").getPath().replace("file:", "") + withdrawData; // 文件和该类在同个目录下
		logger.info("filePath:{}", filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			String flow = null;
			while ((flow = reader.readLine()) != null) {
				String[] flowItem = flow.split("#");
				String userId = flowItem[0].trim();
				String money =  flowItem[1].trim();
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", userId);
				map.put("money", money);
				flows.add(map);
			}
			logger.info("flows Size:{}", flows.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flows;
	}
	
	
    private String hash(int length) {
        String hashCode = "" + Math.abs(UUID.randomUUID().hashCode());
        String userIdHashCode = "" + System.currentTimeMillis();
        return userIdHashCode.substring(userIdHashCode.length() - (length - hashCode.length()))
                        + hashCode;
    }
    
	private static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}
	
	/**
	 * 返回6月份某天的一个随机时间
	 * @param date
	 * @return
	 */
	private static long randomTime() {
		int randomDay = MathUtil.randomInt(0, 29);
		DateTime curDate = new DateTime(startDate).plusDays(randomDay);
		DateTime start = curDate.plusHours(9);
		DateTime end = curDate.plusHours(22);
		long randomTime = random(start.getMillis(), end.getMillis());  
//		DateTime time = new DateTime(randomTime);
		return randomTime;
	}
	
	public static void main(String[] args) {
//		Set<Integer> originOrderIds = TestOrder.loadData();
//		logger.info("originOrderIds Size:{}", originOrderIds.size());
//		DateTime time = new DateTime(randomTime());
//		System.out.println(time.toString("yyyy-MM-dd HH:mm:ss"));
//		String orderNum = time.toString("yyyyMMdd") + MathUtil.randomInt(1, 30);
//		System.out.println(orderNum);
		System.out.println(MathUtil.randomInt(20, 299));
		System.out.println(DateTime.now().minusMonths(3).toString());
		
	}


	@Override
	public boolean testCopyMarketOrder(String yearMonth) {
		int monthDays = 0;
		if("201609".equals(yearMonth)){
			monthDays = 30;
		} else if("201610".equals(yearMonth)){
			monthDays = 31;
		} if("201611".equals(yearMonth)){
			monthDays = 30;
		} if("201612".equals(yearMonth)){
			monthDays = 31;
		}
		List<Map<String, String>> merchants = loadMarketData(yearMonth);
		for (int i = 1; i < monthDays + 1; i++) {
			String ymd = yearMonth;
			if (i < 10) {
				ymd = ymd + "0" + i;
			} else {
				ymd = ymd + i;
			}
			logger.info(" >>>>>>>>> copyMarketOrder monthDay:{}", ymd);
			for(Map<String, String> map : merchants){
				Integer sourceMchId = Integer.parseInt(map.get("sourceMchId"));
				Integer targetMchId = Integer.parseInt(map.get("targetMchId"));
				Integer orderCount = Integer.parseInt(map.get("orderCount"));
				List<Map<String, Object>> sourceOrderIds = this.findForJdbc(selectSourceOrderIdsql, sourceMchId, ymd);
				if(!CollectionUtils.isEmpty(sourceOrderIds)){
					int sourceOrderIdSize = sourceOrderIds.size();
					logger.info(" sourceOrderIdSize:{}", sourceOrderIdSize);
					for (int j = 0; j < orderCount; j++) {
						int indx = RandomUtils.nextInt(sourceOrderIdSize);
						Map<String, Object> sourceOrderIdMap = sourceOrderIds.get(indx);
						Long sourceOrderId = Long.parseLong(sourceOrderIdMap.get("id").toString());
						String hashCode = Integer.toOctalString((int)System.currentTimeMillis());
						Long newOrderId = this.insertBySql(copyMarketOrder, hashCode, targetMchId, sourceOrderId);
						if(newOrderId != null){
							logger.info("copyOrder sourceOrderId:{}, targetMchId:{}, newOrderId:{}", sourceOrderId, targetMchId, newOrderId);
						}
					}
				} else {
					logger.error("sourceOrderIds is empty. ymd:{}, sourceMchId:{}", ymd, sourceMchId);
				}
			}
		}
		return false;
	}
	
	public static List<Map<String, String>> loadMarketData(String yearMonth) {
		List<Map<String, String>> marketDatas = new ArrayList<Map<String, String>>();
		String filePath = TestOrder.class.getResource("").getPath().replace("file:", "") + yearMonth + ".data"; // 文件和该类在同个目录下
		logger.info("filePath:{}", filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
			String flow = null;
			while ((flow = reader.readLine()) != null) {
				String[] item = flow.split("#");
				String targetMchId = item[0].trim();
				String sourceMchId =  item[1].trim();
				String orderCount =  item[2].trim();
				Map<String, String> map = new HashMap<String, String>();
				map.put("targetMchId", targetMchId);
				map.put("sourceMchId", sourceMchId);
				map.put("orderCount", orderCount);
				marketDatas.add(map);
			}
			logger.info("marketDatas Size:{}", marketDatas.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return marketDatas;
	}


	@Override
	public List<Map<String, Object>> selectSourceOrderIds(Integer sourceMchId, String ymd) {
		return this.findForJdbc(selectSourceOrderIdsql, sourceMchId, ymd);
	}


	@Override
	public void copyMarketOrder(String hashCode, Integer targetMchId, Long sourceOrderId) {
		Long newOrderId = this.insertBySql(copyMarketOrder, hashCode, targetMchId, sourceOrderId);
		if(newOrderId != null){
			logger.info("copyOrder sourceOrderId:{}, targetMchId:{}, newOrderId:{}", sourceOrderId, targetMchId, newOrderId);
		}
	}

}
