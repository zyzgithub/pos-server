package com.wm.controller.other;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.PrintKLLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.other.OtherEntity;
import com.wm.service.other.OtherServiceI;

/**   
 * @Title: Controller
 * @Description: 交易信息
 * @author wuyong
 * @date 2015-03-16 09:54:58
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/ci/otherController")
public class OtherController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger loggers = Logger.getLogger(OtherController.class);

	@Autowired
	private OtherServiceI otherService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}





	/**
	 * 添加交易信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "otherOrderNotify")
	@ResponseBody
	public AjaxJson otherOrderSubmit( HttpServletRequest request) {
		String app_id = request.getParameter("app_id");	//客来乐appid
		String out_trace_no = request.getParameter("out_trace_no");	//商户方订单号
		String pay_type = request.getParameter("pay_type");	//支付类型：1-微信支付，8-支付宝支付
		String transation_id = request.getParameter("transation_id");//第三方支付交易号
		String pay_user_id = request.getParameter("pay_user_id");	//第三方交易账户：支付宝为buy_user_id,微信为openid
		String trade_state = request.getParameter("trade_state");	//订单状态：4-支付完成
		String device_no = request.getParameter("device_no");		//设备号
		String total_price = request.getParameter("total_price");	//总价
		String shop_code = request.getParameter("shop_code");
		String create_time = request.getParameter("create_time");
		String pay_time = request.getParameter("pay_time");
		String timestamp = request.getParameter("timestamp");
		String sign = request.getParameter("sign");
		
		TreeMap<String, String> kllMap = new TreeMap<String, String>();
		kllMap.put("app_id", app_id);
		kllMap.put("out_trace_no", out_trace_no);
		kllMap.put("pay_type", pay_type);
		kllMap.put("transation_id", transation_id);
		kllMap.put("pay_user_id", pay_user_id);
		kllMap.put("trade_state", trade_state);
		kllMap.put("device_no", device_no);
		kllMap.put("total_price", total_price);
		kllMap.put("shop_code", shop_code);
		kllMap.put("create_time", create_time);
		kllMap.put("pay_time", pay_time);
		kllMap.put("timestamp", timestamp);
		
		//TODO 新增订单号ID
		
		AjaxJson j = new AjaxJson();
		if(PrintKLLUtils.signCheck(kllMap, sign)){
			OtherEntity other=new OtherEntity();
			other.setVid(transation_id);
			other.setOtherOrderNo("");
			other.setOtherMoney(Double.parseDouble(total_price));
			other.setState("unpay");
			systemService.save(other);
			j.setMsg("添加成功");
			j.setStateCode("00");

		}else{
			j.setMsg("验签失败");
			j.setStateCode("01");
		}
		return j;
	}

	
	/**
	 * 查询交易信息并支付订单
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "queryOtherPayOrder")
	@ResponseBody
	@Transactional
	public AjaxJson queryOtherPayOrder(String otherOrderNo,String orderId,String payType) throws Exception{
		AjaxJson j=new AjaxJson();
		Integer id=0;
		if(orderId!=null&&!"".equals(orderId)){
			id=Integer.parseInt(orderId);
		}
		OrderEntity order=otherService.get(OrderEntity.class, id);
		if(order==null){
			j.setMsg("订单不存在");
			j.setStateCode("01");
			return j;
		}
		
		String sql="select ID from other_order o where o.other_order_no=?";
		List<Map<String, Object>> otherList = otherService.findForJdbc(sql, otherOrderNo);// 查询交易表
		if (otherList == null || otherList.size()==0) {
			j.setMsg("交易信息不存在");
			j.setStateCode("01");
			return j;
		}
		if(otherList.size()>1){
			j.setMsg("交易信息超出范围");
			j.setStateCode("01");
		}else{
			int otherId=0;
			Map<String, Object> map = otherList.get(0);
			otherId = Integer.parseInt(map.get("ID").toString());
			OtherEntity other=otherService.get(OtherEntity.class, otherId);
			if(other!=null){
				if(order.getOrigin().equals(other.getOtherMoney())){
					other.setOrderId(order.getId().toString());
					otherService.saveOrUpdate(other);
					otherService.otherAlipayDone(order.getId(), order.getTitle(), otherId, order.getOrigin(), payType);
					
				}else{
					j.setMsg("交易金额不正确");
					j.setStateCode("01");
				}
			}else{
				j.setMsg("交易信息不存在");
				j.setStateCode("01");
			}
		}
		
		return j;
		
	}
}
