package com.wm.controller.order;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.order.OrderReminderServiceI;
import com.wm.service.order.OrderServiceI;

/**
 * @Title: Controller
 * @Description: 订单催单控制器
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("ci/orderReminderController")
public class OrderReminderController extends BaseController {

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private OrderReminderServiceI reminderService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 新建催单，如果用户的催单没有处理，则返回提示消息，如果都处理了，则新建催单
	 * 
	 * @param orderId
	 *            , 订单id
	 * @param remindDesc
	 *            ,msg
	 * @return
	 */
	@RequestMapping(params = "newReminder")
	@ResponseBody
	public AjaxJson newReminder(@RequestParam int orderId,
			@RequestParam String remindDesc) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> map = reminderService.newReminder(orderId,
				remindDesc);
		if (map.get("success").equals("true")) {
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
		}
		j.setMsg(map.get("msg").toString());
		return j;
	}

	/**
	 * 处理催单，商家或快递员处理催单
	 * 
	 * @param orderId
	 *            , 订单id
	 * @param resolver
	 *            处理人， merchant , courier
	 * @param resolverDesc
	 *            , msg
	 * @return
	 */
	@RequestMapping(params = "resolverReminder")
	@ResponseBody
	public AjaxJson resolverReminder(@RequestParam int orderId,
			@RequestParam String resolver, @RequestParam String resolverDesc) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> map = reminderService.resolverReminder(orderId,
				resolver, resolverDesc);
		if (map.get("success").equals("true")) {
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
		}
		j.setMsg(map.get("msg").toString());
		return j;
	}

	/**
	 * 查询催单列表(通过用户id，用户类型，分钟查询) 查询当前时间到指定分钟内的催单列表，如查询30分钟内的催单，或当天的催单，或一段时间内的催单
	 * 
	 * @param userId
	 *            用户id，如果是快递员则传快递员id，是商家则传商家id
	 * @param userType
	 *            用户类型，3为快递员，2为商家
	 * @param minuteBefore
	 *            , 如30分钟内的写30，24小时内的写24*60， 一段时间内的写（小时差）*60, 如果填空，则默认30分钟内的催单
	 * @param page
	 *            , 页数，从1开始
	 * @param rows
	 *            ，每页记录数
	 * @return 订单催单列表
	 */
	@RequestMapping(params = "queryByUserIdUserTypeAndMinute")
	@ResponseBody
	public AjaxJson queryByUserIdUserTypeAndMinute(
			@RequestParam Integer userId, @RequestParam Integer userType,
			@RequestParam Integer minuteBefore, @RequestParam int page,
			@RequestParam int rows) {
		AjaxJson j = new AjaxJson();
		List<Map<String, Object>> list = reminderService
				.queryByUserIdUserTypeAndMinute(userId, userType, minuteBefore,
						page, rows);
		if (list != null && list.size() > 0) {
			j.setSuccess(true);
			j.setObj(list);
			j.setMsg("查询催单列表成功");
		} else {
			j.setSuccess(false);
			j.setMsg("查询催单列表失败");
		}
		return j;
	}

	/**
	 * 查询催单列表(通过用户id，用户类型，日期时间段查询) 查询时间段内的催单列表
	 * 
	 * @param userId
	 *            用户id，如果是快递员则传快递员id，是商家则传商家id
	 * @param userType
	 *            用户类型，3为快递员，2为商家
	 * @param dtStart
	 *            日期时间开始，格式yyyy-MM-dd HH:mm:ss， 非空, 如2015-02-03 12:22:33
	 * @param dtEnd
	 *            日期时间结束，格式yyyy-MM-dd HH:mm:ss， 非空, 如2015-03-03 12:22:33
	 * @param page
	 *            , 页数，从1开始
	 * @param rows
	 *            ，每页记录数
	 * @return 订单催单列表
	 */
	@RequestMapping(params = "queryByUserIdUserTypeAndDateTime")
	@ResponseBody
	public AjaxJson queryByUserIdUserTypeAndDateTime(
			@RequestParam Integer userId, @RequestParam Integer userType,
			@RequestParam String dtStart, @RequestParam String dtEnd,
			@RequestParam int page, @RequestParam int rows) {

		AjaxJson j = new AjaxJson();
		List<Map<String, Object>> list = reminderService
				.queryByUserIdUserTypeAndDateTime(userId, userType, dtStart,
						dtEnd, page, rows);

		if (list != null && list.size() > 0) {
			j.setSuccess(true);
			j.setObj(list);
			j.setMsg("查询催单列表成功");
		} else {
			j.setSuccess(false);
			j.setMsg("查询催单列表失败");
		}
		return j;
	}
}
