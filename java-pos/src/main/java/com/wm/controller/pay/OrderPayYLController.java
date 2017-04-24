package com.wm.controller.pay;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unionpay.upmp.sdk.conf.UpmpConfig;
import com.unionpay.upmp.sdk.service.UpmpService;
import com.unionpay.upmp.sdk.util.UpmpCore;
import com.wm.service.pay.PayServiceI;

/**
 * 银联支付
 * @author Simon
 */
@Controller
public class OrderPayYLController {
	private static final Logger logger = Logger.getLogger(OrderPayYLController.class);
	
	@Autowired
	private PayServiceI payService;
	
	@Value("${wm_pay_key}")
	private String wmPayKey;
	
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping("/YLPayDone.do")
	public void testpaydone(HttpServletRequest request,HttpServletResponse response) {
		
//		String payKey = p.readProperty("wm_pay_key");
		
		//获取表单参数
		Map<String,String> orderMap = new HashMap<String,String>();
		Enumeration enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName=(String)enu.nextElement();
			orderMap.put(paraName, request.getParameter(paraName));
		}
		//按首字母将传输的参数拼接
		String logs = UpmpCore.createLinkString(orderMap,false,true);
		
		logger.info("银联back logs:" +logs);
		//签名校验
		boolean signFlag= UpmpService.verifySignature(orderMap);
		//获取订单号
		String orderNumber = orderMap.get("orderNumber");
		
		Map<String, Object> map = payService.findOneForJdbc("select a.state,a.user_id,a.id orderid from `order` a where a.pay_id=?",orderNumber);
			
		int userid = Integer.parseInt(map.get("user_id").toString());
		int orderid = Integer.parseInt(map.get("orderid").toString());
		
		if(signFlag){
			//支付完成业务处理
			
			//发送一个订单查询请求到银联中，回应支付异步请求已接受
			Map<String, String> req = new HashMap<String, String>();
			req.put("version", UpmpConfig.VERSION);// 版本号
			req.put("charset", UpmpConfig.CHARSET);// 字符编码
			req.put("transType", "01");// 交易类型
			req.put("merId", UpmpConfig.MER_ID);// 商户代码
			req.put("orderTime", orderMap.get("orderNumber"));// 交易开始日期时间yyyyMMddHHmmss或yyyyMMdd
			req.put("orderNumber", orderNumber);// 订单号
			// 保留域填充方法
	        Map<String, String> merReservedMap = new HashMap<String, String>();
	        merReservedMap.put("test", "test");
	        req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)
			
			Map<String, String> resp = new HashMap<String, String>();
			boolean validResp = UpmpService.query(req, resp);
		}else{
			logger.error("签名校验失败~");
		}	
	}  
}
