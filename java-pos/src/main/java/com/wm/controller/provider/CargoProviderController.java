package com.wm.controller.provider;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.provider.ProviderCategoryService;
import com.wm.util.StringUtil;

@Controller
@RequestMapping("/cargoProviderController")
public class CargoProviderController extends BaseController {
	private static final Logger logger = Logger.getLogger(CargoProviderController.class);

	@Autowired
	private ProviderCategoryService categoryService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private PayServiceI payService;

	/**
	 * 获取商铺列表页及页码信息
	 * 
	 * @param groupId
	 *            分类id
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            每页记录数
	 */
	@RequestMapping(value = "/getMerchantPageList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getMerchantPageList(@RequestParam(value = "groupId") final Integer groupId,
			@RequestParam(value = "pageNo", defaultValue = "1") final Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "30") final Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		Object obj = categoryService.pageCategoryProviderName(groupId, pageNo, pageSize);

		return obj;

	}

	/**
	 * 获取商铺列表页及页码信息
	 * 
	 * @param groupId
	 *            分类id
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            每页记录数
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getMenuPage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getMenuPage(String merchantId, String groupId, String groupName, String pageNo, String pageSize,
			HttpServletRequest request) {

		Integer _pageNo = StringUtils.isEmpty(pageNo) ? 1 : Integer.parseInt(pageNo);
		Integer _pageSize = StringUtils.isEmpty(pageSize) ? 30 : Integer.parseInt(pageSize);
		Integer gid = null;
		if (!StringUtils.isEmpty(groupId)) {
			gid = Integer.parseInt(groupId);
		}
		Map<String, Object> obj = (Map<String, Object>) categoryService.pageMenu(Integer.parseInt(merchantId), gid,
				groupName, _pageNo, _pageSize);
		request.setAttribute("category", obj.get("category"));
		Map<String, Object> data = (Map<String, Object>) obj.get("data");
		request.setAttribute("datas", data.get("data"));
		request.setAttribute("notice", obj.get("notice"));
		request.setAttribute("supportSaleType", obj.get("supportSaleType"));
		return "/provider/index";
	}

	/**
	 * 获取商铺列表页及页码信息
	 * 
	 * @param groupId
	 *            分类id
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            每页记录数
	 */
	@RequestMapping(value = "/getOrderPageList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getOrderPageList(Integer userId, String pageNo, String pageSize, HttpServletRequest request) {
		Integer _pageNo = StringUtils.isEmpty(pageNo) ? 1 : Integer.parseInt(pageNo);
		Integer _pageSize = StringUtils.isEmpty(pageSize) ? 30 : Integer.parseInt(pageSize);
		Object obj = this.categoryService.pageUserOrder(userId, _pageNo, _pageSize);
		return obj;
	}

	/**
	 * 获取商铺列表页及页码信息
	 * 
	 * @param groupId
	 *            分类id
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            每页记录数
	 */
	@RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getOrderDetail(Integer orderId, Integer userId) {
		Object obj = this.categoryService.getOrderDetail(orderId, userId);
		return obj;
	}

	/**
	 * 获取商铺列表页及页码信息
	 * 
	 * @param groupId
	 *            分类id
	 * @param pageNo
	 *            页数
	 * @param pageSize
	 *            每页记录数
	 */
	@RequestMapping(value = "/getMenuPageList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object getMenuPageList(String merchantId, String groupId, String groupName, String pageNo, String pageSize,
			HttpServletRequest request) {
		Integer _pageNo = StringUtils.isEmpty(pageNo) ? 1 : Integer.parseInt(pageNo);
		Integer _pageSize = StringUtils.isEmpty(pageSize) ? 30 : Integer.parseInt(pageSize);
		Integer gid = null;
		if (!StringUtils.isEmpty(groupId)) {
			gid = Integer.parseInt(groupId);
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> obj = (Map<String, Object>) categoryService.pageMenu(Integer.parseInt(merchantId), gid,
				groupName, _pageNo, _pageSize);
		obj.remove("category");
		obj.remove("notice");
		return obj;
	}
	//
	// /**
	// *
	// *
	// * 获取商铺列表页及页码信息
	// *
	// * @param groupId
	// * 分类id
	// * @param pageNo
	// * 页数
	// * @param pageSize
	// * 每页记录数
	// */
	// @RequestMapping(value = "/getMenuPageList", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseBody
	// public Object createMobileOrder(Integer userId, String address, String
	// groupName, String pageNo, String pageSize,
	// HttpServletRequest request) {
	// Integer orderId = orderService.createMobileOrder(userId, address,
	// orderDTO);
	//
	// }

	/**
	 * 创建未付款订单-wap版 (区别是不用传用户id，如果登录了就从session取用户id)
	 * 
	 * @author lfq
	 * @param userId
	 *            用户 ID
	 * @param mobile
	 *            手机号
	 * @param realname
	 *            收货人姓名
	 * @param address
	 *            收货人地址
	 * @param title
	 *            订单备注：店铺名+支付方式
	 * @param params
	 *            订单菜品参数:json数据,格式[{menuId:菜品id,num:数量,menuPromotionId:促销id,
	 *            salesPromotion:是否促销(Y|N),dough:价格},...]
	 * @param timeRemark送货时间
	 * @return
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AjaxJson createOrder(HttpServletRequest request, Integer userId, String mobile, String realname,
			String address, String params, String timeRemark, String title, Integer merchantId, Integer saleType) {

		AjaxJson j = new AjaxJson();
		if (userId == null) {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("抱歉！登录凭证失效或未登录,请重新登录");
			return j;
		} else if (saleType == null || (saleType != 1 && saleType != 2)
				|| (saleType == 1 && (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(realname)
						|| StringUtil.isEmpty(address) || StringUtil.isEmpty(params) || StringUtil.isEmpty(timeRemark)
						|| StringUtil.isEmpty(title)))) {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("操作失败,请求数据不完整,请刷新");
			return j;
		}
		// 判断商家是否在开店
		if (!orderService.merchantWhetherDoBusiness(merchantId)) {
			j.setMsg("商家尚未开店,休息中");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
		System.out.println("菜品：============" + params);
		// j = orderService.verifyMenuQuantity(params, userId);// 验证菜品库存是否还有

		// if (j.isSuccess()) {// 判断菜品库存是否还有

		int orderId = orderService.createOrder(userId, merchantId, mobile, realname, address, params, "",
				"provider-normal", saleType, timeRemark);

		if (orderId == 0) {
			j.setMsg("订单金额非法");
			j.setStateCode("01");
			j.setSuccess(false);
		} else {
			orderStateService.createOrderState(orderId);// 订单生成状态信息
			OrderEntity order = systemService.get(OrderEntity.class, orderId);

			if (order.getSaleType() != 1) {// 判断是否是堂食
				orderService.setOrderNum(order);// 设置排号
			}

			Map<String, Object> objs = new HashMap<String, Object>();
			objs.put("orderId", orderId);
			j.setObj(objs);
			j.setSuccess(true);
		}
		// }
		return j;
	}

	/**
	 * wap余额支付
	 * 
	 * @param orderid
	 *            订单id
	 * @param cardid
	 *            代金券id
	 * @return
	 */
	@RequestMapping("balancePay")
	public String balancePay(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,
			Integer orderid) {

		try {

			OrderEntity order = systemService.get(OrderEntity.class, orderid);

			WUserEntity wuser = order.getWuser();

			// 更新用户的信息
			// systemService.updateEntitie(wuser);
			AjaxJson ajaxJson = new AjaxJson();

			// 判断商家是否在营业
			if (orderService.merchantWhetherDoBusiness(order.getMerchant().getId())) {
				ajaxJson = payService.orderPay(order.getId(), wuser.getId(), order.getMobile(), "", 0, 0,
						order.getOrigin(), 0, "balance", order.getTimeRemark(), request, response);
			} else {
				ajaxJson.setMsg("商家尚未开店，休息当中");
				ajaxJson.setStateCode("01");
				ajaxJson.setSuccess(false);
			}

			if (!ajaxJson.getStateCode().equals("01")) {
				// 支付成功绑定手机
				if (StringUtils.isEmpty(wuser.getMobile())) {
					wuser.setMobile(order.getMobile());
				}
			}

			modelMap.put("result", ajaxJson);
			modelMap.put("orderid", orderid);
			modelMap.put("merchantid", order.getMerchant().getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			return "takeout/error";
		}

		return "takeout/payresult";
	}

}
