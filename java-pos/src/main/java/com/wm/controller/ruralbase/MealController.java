package com.wm.controller.ruralbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.enums.AppTypeConstants;
import com.wm.entity.order.OrderEntity;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.ruralbase.MealServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.StringUtil;

/**
 * @Title: Controller
 * @Description: pay
 * @author wuyong
 * @date 2015-01-07 10:01:54
 * @version V1.0
 *
 */
@Controller
@RequestMapping("ci/mealController")
public class MealController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(MealController.class);

	@Autowired
	private MealServiceI mealServiceI;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private PrintServiceI printService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
    private WmessageServiceI messageService;
	@Autowired
	private JpushServiceI jpushService;
	@Autowired
	private OrderStateServiceI orderStateService;
	
	/**
	 * 出餐列表
	 * @param request
	 * @param response
	 * @param merchantId
	 * @param type
	 * @param start
	 * @param num
	 * @param lineNo
	 * @return
	 */
	@RequestMapping(params = "meallist")
	@ResponseBody
	public AjaxJson meallist(HttpServletRequest request,
			HttpServletResponse response, Integer merchantId,@RequestParam(defaultValue = "A")String type, @RequestParam(defaultValue = "1") Integer start,
			@RequestParam(defaultValue = "10") Integer num,@RequestParam(defaultValue = "2")  Integer lineNo) {

		AjaxJson j = new AjaxJson();
		start = start - 1 < 0 ? 0 : start - 1;
		List<Map<String, Object>> mealList = mealServiceI.getMealList(
				merchantId, type,start, num, lineNo);
		j.setObj(mealList);

		return j;
	}
	
	/**
	 * 准备领餐列表
	 * @param request
	 * @param response
	 * @param merchantId
	 * @param type
	 * @param start
	 * @param num
	 * @param lineNo
	 * @return
	 */
	@RequestMapping(params = "mealPrelist")
	@ResponseBody
	public AjaxJson mealPrelist(HttpServletRequest request,
			HttpServletResponse response, Integer merchantId,@RequestParam(defaultValue = "A")String type, @RequestParam(defaultValue = "1") Integer start,
			@RequestParam(defaultValue = "10") Integer num,@RequestParam(defaultValue = "2")  Integer lineNo) {

		AjaxJson j = new AjaxJson();
		start = start - 1 < 0 ? 0 : start - 1;
		List<Map<String, Object>> mealList = mealServiceI.getMealPreList(
				merchantId, type,start, num);
		j.setObj(mealList);

		return j;
	}

	/**
	 * 置顶
	 * 
	 * @param request
	 * @param response
	 * @param fullName
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params = "setTop")
	@ResponseBody
	public AjaxJson setTop(HttpServletRequest request,
			HttpServletResponse response, String orderNum, int merchantId) {

		AjaxJson j = new AjaxJson();
		try {
			if (StringUtil.isEmpty(orderNum)) {
				j.setMsg("置顶参数有误");
				j.setStateCode("01");
				j.setSuccess(false);
			}
			boolean result = mealServiceI.setTop(orderNum, merchantId);
			if (result) {
				j.setMsg("置顶成功");
			}else {
				j.setMsg("置顶失败");
				j.setStateCode("01");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			logger.info("****乡村基置顶异常*****");
			j.setMsg("置顶异常");
			j.setStateCode("01");
			j.setSuccess(false);
		}

		return j;
	}


	/**
	 * 电视排号
	 * 
	 * @param request
	 * @param response
	 * @param merchantId
	 * @param id
	 * @param orderType
	 * @param start
	 * @param num
	 * @return
	 */
	 
	@RequestMapping(params = "orderNumlist")
	@ResponseBody
	public AjaxJson orderNumlist(HttpServletRequest request,
			HttpServletResponse response, int merchantId, Integer id,
			String type,  Integer start, Integer num) {
		AjaxJson j = new AjaxJson();

		start = start - 1;

		if ((Integer) merchantId == null) {
			j.setStateCode("01");
			j.setMsg("商户ID不能为空!");
			j.setSuccess(false);
			return j;
		}

		if (type == "" || type == null) {
			j.setStateCode("01");
			j.setMsg("堂食订单类型不能为空!");
			j.setSuccess(false);
			return j;
		}

		if (start == null || num == null) {
			j.setStateCode("01");
			j.setMsg("分页起止数，终止数不能为空!");
			j.setSuccess(false);
			return j;
		}

		String orderType = type.toUpperCase();
		Map<String, Object> orderNum = mealServiceI.getMealOrderNum(
				merchantId, id, orderType, start, num);
		j.setObj(orderNum);

		return j;
	}
	
	/**
	 * 修改失效状态
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	@RequestMapping(params = "updateDisplayStatus")    
	@ResponseBody
	public AjaxJson updateDisplayStatus(Integer merchantId,Integer orderId,String type,String fullNum,String version) {
		AjaxJson j = new AjaxJson();
		if (merchantId==null) {
			j.setStateCode("01");
			j.setMsg("商家ID不能为空!");
			j.setSuccess(false);
			return j;
		}
		if (orderId==null && StringUtil.isEmpty(type)) {
			j.setStateCode("01");
			j.setMsg("订单ID或者商品类型不能为空!");
			j.setSuccess(false);
			return j;
		}
		try {
			Integer num = mealServiceI.updateDisplayStatus(orderId, type);
			//修改成功极光推送
			if (num != null && num > 0) {
				if (StringUtil.isEmpty(version)) {
					Map<String, String> pushMap = new HashMap<String, String>();
					pushMap.put("appType", AppTypeConstants.APP_TYPE_RURALBASE);
					pushMap.put("pushType", "Message");
					pushMap.put("fullNum", fullNum);
					pushMap.put("status", "1");
					String title = "您有一条新的消餐信息";
					pushMap.put("title", title);
					pushMap.put("content", title);
					pushMap.put("voiceFile", "");
					jpushService.push(Integer.parseInt(type.trim() + merchantId), pushMap);
				}
			}else {
				j.setStateCode("01");
				j.setMsg("消餐失败!");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			logger.info("****乡村基消餐异常*****");
			j.setStateCode("01");
			j.setMsg("消餐失败!");
			j.setSuccess(false);
		}
		
		return j;
	}

	/**
	 * PAD出餐
	 * @param request
	 * @param response
	 * @param orderId
	 * @return
	 */
	@RequestMapping(params = "orderlist")    
	@ResponseBody
	public AjaxJson orderlist(HttpServletRequest request,
			HttpServletResponse response,Integer merchantId,Integer orderId,String type) {
		AjaxJson j = new AjaxJson();
		
		if( merchantId == null){
			j.setStateCode("01");
			j.setMsg("商家ID不能为空!");
			j.setSuccess(false);
			return j;
		}
		
		if(orderId == null){
			j.setStateCode("01");
			j.setMsg("订单ID不能为空!");
			j.setSuccess(false);
			return j;
		}
		
		if (type == "" || type == null) {
			j.setStateCode("01");
			j.setMsg("堂食订单类型不能为空!");
			j.setSuccess(false);
			return j;
		}
		
		type =  type.toUpperCase();
		try {
			Integer num = mealServiceI.updatePadList(merchantId,orderId,type);
			
			if (num!=null && num>0) {
				try {
					//用户端微信推送
					OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
					Long orderType = getOrderType(type);
					String status="2";//状态2代表出餐
					messageService.sendRuralbaseMessage(order,status,type,orderType);
				} catch (Exception e) {
					// 微信推送异常不做任何操作
				}
				
			}else {
				j.setStateCode("01");
				j.setMsg("出餐失败!");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			logger.info("****乡村基(旧)出餐异常*****");
			j.setStateCode("01");
			j.setMsg("出餐异常!");
			j.setSuccess(false);
		}
		
		return j;
	}
	
	private Long getOrderType(String type) {
		Long orderType=1L;
		switch (type.trim()) {
		case "A":
			orderType=1L;
			break;
		case "B":
			orderType=2L;
			break;
		case "C":
			orderType=3L;
			break;
		default:
			orderType=1L;
			break;
		}
		return orderType;
	}
	
	/**
	 * PAD出餐
	 * @param request
	 * @param response
	 * @param orderId
	 * @return
	*/
	@RequestMapping(params = "outMeal")    
	@ResponseBody
	public AjaxJson outMeal(Integer merchantId,Integer orderId,String type,String fullNum,String version) {
		AjaxJson j = new AjaxJson();
		
		if( merchantId == null){
			j.setStateCode("01");
			j.setMsg("商家ID不能为空!");
			j.setSuccess(false);
			return j;
		}
		
		if(orderId == null){
			j.setStateCode("01");
			j.setMsg("订单ID不能为空!");
			j.setSuccess(false);
			return j;
		}
		
		if (type == "" || type == null) {
			j.setStateCode("01");
			j.setMsg("堂食订单类型不能为空!");
			j.setSuccess(false);
			return j;
		}
		try {
			type =  type.toUpperCase();
			//获得状态。判断 状态<3出餐， 状态>=3消餐
			Map<String, Object> mealStatus = mealServiceI.getMealStatus(merchantId, orderId, type);
			if (Integer.parseInt(mealStatus.get("status").toString())<2 ) {
				j = mealServiceI.outMeal(merchantId,orderId,type,fullNum,version);
				try {
					if ("00".equals(j.getStateCode())) {
						//微信不推送不做任何处理
						OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
						Long orderType =getOrderType(type);
						String status="2";//状态2代表出餐
						messageService.sendRuralbaseMessage(order,status,type,orderType);
					}
				} catch (Exception e) {
					// 微信推送异常不做任何操作
				}
			}else {
				j = updateDisplayStatus(merchantId, orderId, type, fullNum,version) ;
				return j;
			}
			
		} catch (Exception e) {
			logger.info("****乡村基出餐异常*****");
			j.setStateCode("01");
			j.setMsg("网络异常,请检查网络!");
			j.setSuccess(false);
		}
		
		return j;
	} 
	
	/**
	 * 电视排号列表
	 * @param request
	 * @param response
	 * @param merchantId
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(params = "TVOrderNumList")
	@ResponseBody
	public AjaxJson TVOrderNumList(HttpServletRequest request,
			HttpServletResponse response, int merchantId, 
			String type) {
		AjaxJson j = new AjaxJson();
		
		if ((Integer) merchantId == null) {
			j.setStateCode("01");
			j.setMsg("商户ID不能为空!");
			j.setSuccess(false);
			return j;
		}

		if (type == "" || type == null) {
			j.setStateCode("01");
			j.setMsg("堂食订单类型不能为空!");
			j.setSuccess(false);
			return j;
		}

		try {
			String orderType = type.toUpperCase();
			List<Map<String, Object>> tvOrderNumList = mealServiceI.TVOrderNumList(
					merchantId, orderType);
			j.setObj(tvOrderNumList);
		} catch (Exception e) {
			j.setStateCode("01");
			j.setMsg("系统异常!");
			j.setSuccess(false);
			return j;
		}
	
		return j;
	}
}