package com.wm.service.order;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.order.dto.OrderFromMerchantDTO;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.dto.ShopcartDTO;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.controller.takeout.vo.OrderMerchantVo;
import com.wm.controller.takeout.vo.OrderVo;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.LogisticsEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntityVo;
import com.wm.entity.orderrefund.OrderRefundEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.util.PageList;

public interface OrderServiceI extends CommonService {

	/**
	 * 创建订单
	 * 
	 * @param userId
	 *            用户id
	 * @param merchantId
	 *            商家id
	 * @param mobile
	 *            电话号码
	 * @param realname
	 *            收货人
	 * @param address
	 *            收货地址
	 * @param params
	 *            菜单字符串，格式：[{menuId:1,num:2},{menuId:2,num:3}]
	 * @param title
	 *            备注
	 * @param orderType
	 *            订单类型：normal-外卖订单,mobile-电话订单
	 * @param saleType
	 *            类型：1-外卖，2-堂食
	 * @param timeRemark
	 *            送达时间备注
	 * @return 订单id
	 */
	public int createOrder(int userId, int merchantId, String mobile,
			String realname, String address, String params, String title,
			String orderType, int saleType, String timeRemark);
	
	/**
	 * 
	 * @param state
	 *            订单状态，如果空字串或者null则查所有状态。订单状态：unpay未支付，pay支付成功，accept制作中，done待评价
	 *            ，confirm 已完成，refund 退款 delivery 配送中，delivery_done配送完成
	 * @param userId
	 *            用户id
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<OrderMerchantVo> queryMineOrderByState(String state,
			int userId, int page, int rows);

	/**
	 * 获取快递员订单列表
	 * 
	 * @param courierId
	 *            快递员id
	 * @param state
	 *            订单状态（'delivery'-递送中,'done'-已完成）
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> getCourierOrderListById(String courierId,
			String state, String start, String num);

	/**
	 * 获得快递员当天订单
	 * 
	 * @param courierId
	 *            快递员编号
	 * @param state
	 *            订单状态
	 * @param queryParam
	 *            查询条件：用户名，电话号码，排号，订单号
	 * @param start
	 *            起始行
	 * @param num
	 *            每页显示行数
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getCourierOrderList(String courierId,
			String state, String queryParam, Integer start, Integer num, String startDate, String endDate);

	/**
	 * 获取店铺订单列表（商家版）
	 * 
	 * @param merchantId
	 * @param state
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> getMerchantOrderListByMerchantId(
			String merchantId, String state, Integer start, Integer num);

	/**
	 * 获取订单详情（通用版）
	 * 
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> selectListDetail(int orderId);
	
	/**
	 * 快递版
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getOrderDetail(int orderId);

	/**
	 * 根据订单状态和订单id获取订单信息
	 * 
	 * @param state
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getOrderDynamicById(int orderId);

	/**
	 * 买单订单生成
	 * 
	 * @param userId
	 * @param merchantId
	 * @param price
	 * @return
	 */
	public int createDirectPayOrder(int userId, int merchantId, double price);
	
	/**
	 * i玩派推送订单生成
	 * @param user
	 * @param order_money
	 * @param create_time
	 * @param cust_mobile
	 * @return
	 */
	public int createOpenTswjOrder(WUserEntity user, String out_order_id, String out_order_title, double order_money, Long create_time, String cust_mobile);

	/**
	 * 创建充值订单
	 * 
	 * @param userId
	 * @param price
	 * @return
	 */
	public int createRechargeOrder(int userId, double price,String title);

	/**
	 * 订单退款
	 * 
	 * @param orderId
	 * @param rereason
	 */
	public void orderRefund(int orderId, String rereason) throws Exception;

	/**
	 * 商家接受订单
	 * 
	 * @param orderId
	 * @return
	 */
	public boolean merchantAcceptOrder(OrderEntity order) throws Exception;

	/**
	 * 商家拒接订单
	 * 
	 * @param orderId
	 * @param merchantid
	 * @param refundReason
	 * @return
	 */
	public boolean merchantNoAcceptOrder(int orderId, int merchantid, String refundReason) throws Exception;

	/**
	 * 用户申请取消订单
	 * 
	 * @param orderId
	 * @param refundReason
	 */
	public void askRefund(int orderId, String refundReason);

	/**
	 * 商家接受退款
	 * 
	 * @param orderId
	 * @param merchantId
	 */
	public boolean acceptRefund(int orderId, int merchantId) throws Exception;
	
	/**
	 * 第三方订单退款
	 * @param orderId
	 * @param merchantId
	 * @return
	 */
	public OrderRefundEntity acceptThirdRefund(int orderId, int merchantId) throws Exception;

	/**
	 * 商家不接受退款
	 * @param orderId
	 */
	public void unacceptRefund(int orderId);

	/**
	 * 开始配送
	 * 
	 * @param orderId
	 * @param courierId
	 * @return
	 */
	public boolean deliveryBegin(Integer orderId, Integer courierId);

	/**
	 * 用户确认订单
	 * @param userId
	 * @param orderId
	 */
	public boolean confirmOrder(Integer orderId, Integer userId) throws Exception;

	/**
	 * 快递员批量完成订单
	 * 
	 * @param orderIdsStr
	 * @param courierId
	 */
	public boolean deliveryDoneOrders(String orderIdsStr, Integer courierId) throws Exception;

	/**
	 * 配送员完成配送
	 * @param courierId
	 * @param orderId
	 */
	public boolean deliveryDone(Integer courierId, Integer orderId) throws Exception;
	
	public boolean supplyOrderFinish(Integer courierId, Integer orderId) throws Exception;

	/**
	 * 
	 * @param orderId
	 */
	public void mobileOrderStateCreate(int orderId);

	/**
	 * 商家自动接单,并打印小票
	 * @param order
	 */
	public void autoPrint(OrderEntity order) throws Exception;

	/**
	 * 设置订单排号
	 * 
	 * @param order
	 */
	public void setOrderNum(OrderEntity order);

	/**
	 * 验证菜单库存数量是否足够
	 * 
	 * @param orderId
	 * @return
	 */
	public boolean verifyMenuRepertoryquantity(int orderId);

	/**
	 * 用户查询当天最新订单详情
	 * 
	 * @return
	 * @throws ParseException
	 */

	public AjaxJson newestOrderDetails(int userId);

	/**
	 * 查询等待快递员接收订单列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> selectWaitDeliveryOrder(int page, int rows);

	/**
	 * 根据商家ID和用户手机号码分布查询订单列表
	 * 
	 * @param merchanId
	 * @param Moblie
	 * @return
	 */
	public List<Map<String, Object>> getOrderByMerchanAndMoblie(
			String merchanId, String Moblie, int page, int rows);

	/**
	 * 商家是否营业
	 * 
	 * @return
	 */
	public boolean merchantWhetherDoBusiness(int merchanId);

	/**
	 * 查询快递排名
	 * 
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCourierRanking(int courierId);

	/**
	 * 创建订单时验证菜单库存是否足够
	 * 
	 * @param params
	 * @return
	 */
	public AjaxJson verifyMenuQuantity(String params, int userId);

	/***
	 * 厨房制作完成
	 * 
	 * @return
	 */
	public void kitchenMakeAccomplish(int orderId);

	/**
	 * 根据商家ID和订单类型,支付状态查询订单
	 * 
	 * @param payState
	 * @param merchanId
	 * @return
	 */
	public List<Map<String, Object>> getOrderBySaleType(int SaleType,
			int merchanId, String payState);

	/**
	 * 根据商家ID和订单状态 分页查询订单
	 * 
	 * @param merchanId
	 * @param state
	 * @param pageNo
	 * @param row
	 * @return
	 */
	public List<Map<String, Object>> getOrderByState(int merchanId,
			String code, int saleType, int codeType, int pageNo, int row);

	/**
	 * 完成堂食订单
	 * 
	 * @param orderId
	 */
	public void completeOrder(int orderId);
	
	/**
	 * 开始制作堂食订单
	 * 
	 * @param orderId
	 */
	public void startExecutionOrder(int orderId);

	public List<Map<String, Object>> getOrderByStateGroup(int merchanId,
			int saleType);

	/**
	 * 根据订单ID查询订单菜品信息
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getOrderMenuByOrderId(int orderId);

	/**
	 * 系统自动完成订单（太久没操作或者被遗忘的订单）
	 * 
	 * @param order
	 */
	public void autoCompleteOrder(OrderEntity order) throws Exception;

	/**
	 * 根据商家ID查询未支付的外卖普通订单
	 * 
	 * @param MerchantId
	 */
	public List<Map<String, Object>> getMerchantOrderByOrderPay(
			Integer merchantId, Integer start, Integer num);

	/**
	 * 修改普通订单为电话订单
	 * 
	 * @param order
	 */
	public void updateOrderByPhoneOrder(OrderEntity order) throws Exception;

	/**
	 * 根据类型查询申请取消订单列表或已经取消订单列表
	 * 
	 * @param type
	 *            1为申请取消订单列表 2为已经取消订单列表
	 * @param merchantId
	 *            商家ID
	 * @param start
	 *            开始记录数
	 * @param num
	 *            显示条数
	 * @return
	 */
	public List<Map<String, Object>> askRefundOrderList(int type,
			int merchantId, int start, int num);

	/**
	 * 取消未付款订单
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderId
	 *            订单Id
	 * @param userId
	 *            订单用户Id(即当前用户id)
	 * @return 成功取消返回true，否则返回false
	 */
	Boolean cancelUnpayOrder(Integer orderId, Integer userId);

	/**
	 * 用户订单退款 (与orderRefund不同之处是该业务方法处理只有订单状态state=pay状态即未出单的订单才能订单退款成功)
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderId
	 *            订单Id
	 * @param rereason
	 *            退款原因
	 * @param userId
	 *            订单用户Id(即当前用户id)
	 * @return true订单退款成功,false退款失败
	 */
	Boolean orderRefund(Integer orderId, String rereason, Integer userId) throws Exception;

	/**
	 * 用户向商家申请退单 (与askRefund不同之处是该业务方法处理只有订单状态商家接单、未制作、制作完成、配送中 时才能申请退单)
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderId
	 *            订单Id
	 * @param refundReason
	 *            退款原因
	 * @param userId
	 *            订单用户Id(即当前用户id)
	 * @return
	 */
	Boolean askRefund(Integer orderId, String refundReason, Integer userId);

	Double getTotalMoney(Long merchantId, Long startTime, Long endTime);

	/**
	 * 查询wap个人订单列表(不包括充值订单)
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param start
	 *            开始位置,必须
	 * @param pageSize
	 *            返回记录条数,必须
	 * @param userId
	 *            用户Id,非必须
	 * @param merchantId
	 *            商家Id,非必须
	 * @param state
	 *            订单状态：unpay 未支付， pay 已支付， accept 已接受， unaccept 未接受，done
	 *            已完成，evaluated已评价，confirm确认，refund 退款，delivery 配送
	 * @return
	 */
	List<Map<String, Object>> getOrderList(Integer start, Integer pageSize,
			Integer userId, Integer merchantId, String... state);

	/**
	 * 查询大于10分钟的未支付订单列表
	 * 
	 * @param merchantId
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> getOrderByPayState(int merchantId,
			int start, int num);


	/**
	 * 获得按状态划分的订单数
	 * 
	 * @param courierId
	 *            快递员id
	 * @return
	 */
	public List<Map<String, Object>> countByStatus(Integer courierId, String status);

	/**
	 * 自由抢单
	 * 
	 * @param courierId
	 * @return
	 */
	public AjaxJson scramble(Integer courierId);
	
	/**
	 * 抢特定订单-快递员管理-抢单 
	 * @param courierId 快递员id
	 * @param orderId 订单id
	 * @return
	 */
	public AjaxJson scrambleOrder(Integer courierId, Integer orderId);


	/**
	 * 通过快递员ID获取转单记录
	 * 
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Map<String, Object>> getTransOrderByCourierId(Integer courierId,
			String startDate, String endDate);

	/**
	 * 新增订单
	 * 
	 * @param userId
	 * @param merchantId
	 * @param payType
	 * @param saleType
	 * @param carts
	 * @return
	 */
	public Integer createOrderFromWX(Integer userId, ShopcartDTO shopcart, List<Shopcart> carts);

	
	/**
	 * 获取快递员可拿提成的送餐份数
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Long getCourierMenus(Integer courierId, String startDate, String endDate);
	
	/**
	 * 获取快递员可拿提成的订单数
	 * @param courierId
	 * @param deductDate
	 * @return
	 */
	public Long getCourierOrders(Integer courierId, String deductDate);
	
	/**
	 * 获取快递员普通订单数,排除众包订单
	 * @param courierId 快递员ID
	 * @param deductDate 统计提成的日期 格式 yyyy-MM-dd
	 * @return 订单数量
	 */
	public Long getCourierNormalOrders(Integer courierId, String deductDate);
	
	/**
	 * 获取快递员众包提成
	 * @param courierId 快递员ID
	 * @param deductDate 统计提成的日期 格式 yyyy-MM-dd
	 * @return 提成金额（单位：分）
	 */
	public Long getCrowdsourcingDeduct(Integer courierId, String deductDate);
	
	/**
	 * 获取快递员众包提成份数
	 * @param courierId
	 * @param deductDate
	 * @return
	 */
	public Integer getCrowdsourcingQuantity(Integer courierId, String deductDate);
	
	/**
	 * 获取商家的餐份数
	 * @param merchantId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Long getMerchantMenus(Integer merchantId, String startDate, String endDate);
	
	/**
	 * 自动分单,在商家确认订单的时候调用
	 * 
	 * @param orderId
	 * @return 返回接单快递员ID，失败返回0
	 */
	public Integer autoScramble(Integer orderId);

//	public void confirmOrder(int orderid);
	
	/**
	 * 获取可配送该订单的快递员
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getRelaCourier(Integer orderId);

	public boolean validOrderByOrderId(Integer orderId);

	public OrderVo queryOrderById(Integer orderId, Integer userId);

	/**
	 * 获取快递员订单数：我的订单、配送中、已完成、可抢订单数
	 * @param courierId
	 * @return
	 */
	public Map<String, Object> getCourierOrderNumbers(Integer courierId);

	/**
	 * 取消订单或退单成功时，订单使用的余额和积分返还到用户的账户中
	 * @param orderId
	 */
	public void returnCreditAndScore(Integer orderId);
	
	/**
	 * 快递员余额代付
	 * @param order 订单
	 * @param courier 快递员
	 * @param user 用户
	 * @return
	 */
	public boolean payOrderByCourier(OrderEntity order, WUserEntity courier, WUserEntity user) throws Exception;
	
	/**
	 * 快递员支付宝代付
	 * @param order 订单
	 * @return
	 */
	public String aliPayOrderByCourier(OrderEntity order)
			throws UnsupportedEncodingException;
	
	/**
	 * 快递员微信代付
	 * @param order 订单
	 * @return
	 */
	public Map<String, String> weixinPayOrderByCourier(OrderEntity order)throws Exception;
	
	

	public Integer createMobileOrder(WUserEntity user, AddressEntity address,
			OrderFromMerchantDTO orderDTO);

	/**
	 * 根据快递员ID，订单状态，支付开始时间和结束时间获取订单数
	 * 
	 * @param courierId
	 * @param state
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getOrderNumbersByCourierId(Integer courierId,
			String state, String beginTime, String endTime);
	
	/**
	 * 推送订单信息给相应的快递员
	 * @param orderId
	 */
	public void pushOrder(Integer orderId);
	
	/**
	 * 给快递员推送可抢订单信息
	 * @param courierId
	 * @param orderId
	 * @param canScramble
	 */
	public void pushCanScramble(Integer courierId, Integer orderId, Integer canScramble);
	
	/**
	 * 根据参数查询订单分页列表
	 * @param OrderEntityVo
	 * @param pageNo
	 * @param row
	 * @return
	 */
	public PageList<OrderEntity> findOrderList(OrderEntityVo order,int page, int rows);
	

	/**
	 * 根据参数查询订单列表
	 * @param OrderEntityVo
	 * @param pageNo
	 * @param row
	 * @return
	 */
	public List<OrderEntityVo> findOrderList(OrderEntityVo order);


	/**
	 * 统计订单排名
	 * @param order
	 * @return
	 */
	public List<OrderEntityVo> statisticsOrderRanking(OrderEntityVo order);

	public AddressDetailVo queryLastedRestaurantUserInfo(Integer userId);

	/**
	 * 创建扫码支付订单
	 * @param payId
	 * @param user
	 * @param merchantId
	 * @param checkCost
	 * @param saleType
	 * @param payType
	 * @return
	 */
	public Integer createQcCodeOrder(String payId,WUserEntity user,String merchantId,Double checkCost,String saleType,String payType);
	
	/**
	 * 删除没有支付的扫码订单
	 * @param orderId
	 */
	public void deleteOrder(Integer orderId);
	
	/**
	 * 判断此订单是否商家自己接单配送
	 * @param order
	 * @return true表示商家自己接单
	 */
	public boolean isMerchantDelivery (MerchantEntity merchant);
	
	/**
	 * 商家自己配送：更改状态(delivery)
	 */
	public boolean merchantUpdateOrderState(String state,Integer orderId);
	
	/**
	 * 私厨商家接收订单
	 * @param merchantid
	 * @param orderid
	 * @return
	 */
	public boolean kitMerchantAcceptOrder(int merchantid, int orderid) throws Exception;
	
	/**
	 * 商家拒接订单
	 * 
	 * @param orderId
	 * @param merchantid
	 * @param refundReason
	 * @return
	 */
	public boolean kitMerchantUnAcceptOrder(int orderId, int merchantid,String refundReason) throws Exception;
	
	/**
	 * 私厨15分钟内定时器退单
	 * @return
	 */
	public List<Map<String, Object>> getOrderTimer();
	
	/**
	 * 删除私厨15分钟内定时器已完成退单
	 */
	public void deleteOrderTimer(int id);
	
	/**
	 * 商家添加众包订单
	 * @param id 商家id
	 * @param password 支付密码
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param realname 客户名称
	 * @param mobile 客户手机号码
	 * @param address 客户地址
	 * @param remark 备注
	 * @param crowdId 众包类型id(tom_crowdsourcing_type表)
	 * @return
	 */
	public AjaxJson createCrowdsourcingOrder(Integer id, String password, Double longitude,
			Double latitude, String realname, String mobile,String address, String remark, Integer crowdId) throws Exception;

	/**
	 * 获取众包订单列表
	 * @param  merchantId 商家id
	 * @param  page 起始分页
	 * @param  rows 分页行数
	 * @return
	 */
	public List<Map<String, Object>> getCrowdsourcingOrders(Integer merchantId, Integer page, Integer rows);

	/**
	 * 取消众包订单
	 * @param id 订单id
	 * @return
	 */
	public AjaxJson cancelCrowdsourcingOrder(Integer orderId, Integer merchantId) throws Exception;

	/**
	 * 自动扫描取消众包订单
	 * @param orderId 订单id
	 * @param merchantId 商家 id
	 * @return
	 */
	public Boolean autoCancelCrowdsourcingOrder(Integer orderId, Integer merchantId) throws Exception;
	
	/**
	 * 获取快递员未完成订单的数量
	 * @param courierId
	 * @return
	 */
	public  Integer getOrderNumber(Integer courierId, String time);
	
	/**
	 * 判断订单是否是代付电话订单
	 * @param order
	 * @return
	 */
	public boolean isTransferMobileOrder(OrderEntity order);
	
	/**
	 * 商家账单统计
	 * @param merchantId 商家id
	 * @param num  0代表全部订单 ， 1代表近1个月内的订单 ， 15代表近15天内的订单
	 * @param page  分页起始
	 * @param rows  分页行数
	 * @return
	 */
	public Map<String, Object> getMerchantOrderCount(Integer merchantId, Integer num, Integer page, Integer rows);
	
	/**
	 * 获取商家每日订单详情
	 * @param merchantId 商家id
	 * @param time 订单年－月－日
	 * @param page 分页起始
	 * @param rows 分页行数
	 * @return
	 */
	public List<Map<String, Object>> getMerchantDayOrders(Integer merchantId, String time, Integer page, Integer rows);

	
	/**
	 * 商家端根据排号或订单号模糊查询(当天)
	 * @param merchantId 商家id
	 * @param value	排号(orderNum)/订单号(payId)
	 * @param page 分页起始
	 * @param rows 分页行数
	 * @return
	 */
	public List<Map<String, Object>> selectMerchantOrdersByValue(Integer merchantId, String value, Integer page, Integer rows);

	
	
//	public boolean courierHandlerefund(Integer courierId, Integer orderId, String state);
	/**
	 * 根据商家id查看该商家的订单是否有快递员接单
	 * @param mercahntId
	 * @return
	 */
	
	public boolean exsistsCanReceiveCouriers(Integer mercahntId);
	
	/**
	 * 获取可以接单的众包快递员
	 */
	public List<Integer> findCanReceiveCrowdsourcingCouriers(OrderEntity order);
	
	/**
	 * 获取众包快递员配送的普通订单
	 * @param courierId
	 * @param date
	 * @return
	 */
	public List<Map<String, Object>> getMerchantTypeAndOrderQuantityBycourierId(Integer courierId, String date);
	
	/**
	 * 获取优惠券
	 * @param sn
	 */
	public Map<String, Object> getCouponUser(String sn,Integer userId);

	/**
	 * 闪购订单：商家确认发货
	 * @param orderId 订单id
	 * @param logisticsName  物流名称
	 * @param logisticsNumber	物流单号
	 * @param logisticsSnapshort 物流快照图片
	 * @return
	 */
	public int createLogisticsByMerchant(Integer orderId, String logisticsName, String logisticsNumber, String logisticsSnapshot);
	
	/**
	 * 闪购订单：获得退款详情
	 * @param order
	 * @param state  详情状态： 1 退款       2 退货
	 * @return
	 */
	public Map<String, Object> getRefundDetail(OrderEntity order, String state);
	
	/**
	 * 闪购订单：获得物流信息
	 * @param orderId 
	 * @param type 物流信息类型 0:商家发货,1:买家退货
	 * @return
	 */
	public LogisticsEntity getLogisticsByOrderId(Integer orderId,String type);
	
	/**
	 * 闪购订单：同意退款申请
	 * @param orderId 订单id
	 * @return
	 */
	public String flashOrderAcceptRefundApply(Integer orderId);
	
	/**
	 * 闪购订单：拒绝退款申请
	 * @param orderId 订单id
	 * @return
	 */
	public void flashOrderUnAcceptRefundApply(Integer orderId);
	
	/**
	 * 闪购订单：确认退款
	 * @param orderId 订单id
	 * @param merchantId 商家id
	 * @return
	 */
	public boolean flashOrderAcceptRefund(Integer orderId, Integer merchantId);
	
	/**
	 * 获取物流名称列表
	 * @return
	 */
	public List<Map<String, Object>> getExpressList();
	
	
	/**
	 * 重置订单排号
	 */
	public void resetOrderNum();

	/**
	 * 需要自动结算的订单
	 * @return
	 */
	public List<Map<String, Object>> findNeedCompleteList();
	
	/**
	 * 退款通知快递员端
	 * @param orderId 订单ID
	 * @return
	 */
	public boolean refundNoticeToCourier(Integer orderId);
	
	/**
	 * 获取堂食订单详情
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getDineSeatDetail(Integer orderId);

	/**
	 * 获取订单实际金额
	 * @param order
	 * @return
	 */
	public String getOrderRealMoney(OrderEntity order);
	
	public boolean isSupplyChainOrder(Integer orderId);
	
	public int matchSupplyOrderId(Integer orderId);
	
	/**
	 * 
	 * @param courierId
	 * @param orderId
	 * @param isSupplyOrder
	 * @return
	 */
	public AjaxJson supplyOrderDistribution(int courierId, int orderId, boolean isSupplyOrder); 
	
	/**
	 * 将订单设置为分批送订单
	 * @param orderNum	订单号(pay_id)
	 * @param orderState 订单状态
	 * @return TODO
	 */
	public abstract int setOrderState(String orderNum, String orderState);
}