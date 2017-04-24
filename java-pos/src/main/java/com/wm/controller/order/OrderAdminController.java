package com.wm.controller.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntityVo;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.DineInOrderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * 
 * @ClassName: OrderAdminController
 * @Description: 订单后台管理控制器
 * @author 黄聪
 * @date 2015年9月17日 上午10:31:14
 *
 */
@Controller
@RequestMapping("orderAdminController")
public class OrderAdminController extends BaseController {

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private WUserServiceI wUserService;
	
	@Autowired
	private DineInOrderServiceI dineInOrderService;
	
	@Autowired
	private PrintServiceI printService;

	/**
	 * 客服手动接受退单
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "manualAcceptRefund")
	@ResponseBody
	public AjaxJson manualAcceptRefund(int userId, int orderId) throws Exception {
		AjaxJson j = new AjaxJson();
		j.setMsg("退款失败");
		j.setSuccess(false);
		j.setStateCode("01");
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (user == null) {
			j.setMsg("用户为空");
			j.setSuccess(false);
			j.setStateCode("01");
			return j;
		}
		boolean refundStatus = orderService.acceptRefund(orderId, userId);
		if (refundStatus) {
			j.setMsg("退款成功");
			j.setSuccess(refundStatus);
			j.setStateCode("00");
		}
		return j;
	}

	/**
	 * 客服手动拒绝退单
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@RequestMapping(params = "manualUnacceptRefund")
	@ResponseBody
	public AjaxJson manualUnacceptRefund(int userId, int orderId) {
		AjaxJson j = new AjaxJson();
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (user == null) {
			j.setMsg("用户为空");
			j.setSuccess(false);
			j.setStateCode("01");
			return j;
		}
		orderService.unacceptRefund(orderId);
		j.setMsg("拒绝成功");
		j.setSuccess(true);
		j.setStateCode("00");
		return j;
	}

	/**
	 * 客服手动补打小票
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@RequestMapping(params = "manualPrintOrder")
	@ResponseBody
	public AjaxJson manualPrintOrder(int userId, String orderId) {
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setMsg("打印失败，无此订单" + orderId);
		j.setSuccess(false);
		if (!NumberUtils.isDigits(orderId)) {
			return j;
		}
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (user == null) {
			j.setMsg("用户为空");
			j.setSuccess(false);
			j.setStateCode("01");
			return j;
		}
		int oId = NumberUtils.toInt(orderId);
		OrderEntity order = orderService.get(OrderEntity.class, oId);
		printService.print(order, false);
		j.setStateCode("00");
		j.setMsg("打印成功");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 单个门店的前10名客户和月度前10名的客户
	 * 
	 * @param request
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "statisticsOrderRanking")
	@ResponseBody
	public AjaxJson statisticsOrderRanking(HttpServletRequest request,OrderEntityVo order) {
		AjaxJson j = new AjaxJson();
		List<OrderEntityVo> list = orderService.statisticsOrderRanking(order);
//		String title = "";
//		String userName = "";
//		String orderCount = "";
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 堂食订单退单
	 * @param orderId 订单ID
	 * @param opUserId 操作人ID
	 * @return 响应消息
	 */
	@RequestMapping(params = "dineInOrderRefund")
	@ResponseBody
	public AjaxJson dineInOrderRefund(Integer orderId, Integer opUserId) {
		return dineInOrderService.chargeback(orderId, opUserId);
	}
	
}

