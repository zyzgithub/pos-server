package courier_mana.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MD5;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.controller.courier.dto.UserRegisterDTO;
import com.wm.util.HttpProxy;

public class HttpTest extends TestCase{
	private final static String APP_URL = "http://localhost:8080/ROOT";
	
//	private final static String APP_URL = "http://192.168.8.100:8090/WM/";
//	private final static String APP_URL = "http://apptest.0085.com/";
	private final static String BAIDU_MAP_URL = "http://api.map.baidu.com/direction/v1/routematrix?output=json";
	
	@Test
	public void testGetDistance() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = BAIDU_MAP_URL + "/direction/v1/routematrix";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("output", "json");
			params.put("origins", "40.05781,116.307437");
			params.put("destinations", "36.901104,118.728017");
			params.put("ak", "7FwBKbXqYp7GO9GHRRdROZAs");
			
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
		
	}
	@Test
	public void testUserlogin()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/wUserController.do?userLogin";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			NameValuePair param1 = new BasicNameValuePair("username", "张萌");
			NameValuePair param2 = new BasicNameValuePair("password", MD5.GetMD5Code("123456789" + "@4!@#@W$%@"));
			NameValuePair param3 = new BasicNameValuePair("type", "2");
			NameValuePair param4 = new BasicNameValuePair("channel", "1");
			params.add(param1);
			params.add(param2);
			params.add(param3);
			params.add(param4);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
	}
	
	@Test
	public void testUserRegister()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/courier/register/fill_personal_information.do?";
			UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
			userRegisterDTO.setUsername("134343");
			userRegisterDTO.setIdCard("dfdfdfdfd");
			
			
			Map<String, Object> params = new HashMap<String, Object>();
			BeanUtils.copyProperties(params, userRegisterDTO);
			params.put("sessionkey", "4028881751d7aeba0151d7e40f150000");
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
		
	}
	
	
	@Test
	public void testGetSessionKey()throws Exception{
		String url = APP_URL + "/controller.do?login";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "getAccess");
		params.put("login_name", "fshdskjf");
		params.put("password", "1367jhd");
		HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
		String response = httpProxy.doGet();
		System.out.println(response);
	}
	
	
	@Test
	public void testQueryBindMerchantList() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/courierController.do?queryBindMerchantList";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courierId", "62832");
			params.put("sessionkey", sessionKey);
			params.put("page", "1");
			params.put("rows", "100");
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
		
	}
	
	@Test
	public void testGetCourierOrderList() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/orderController.do?getCourierOrderList";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courierId", "59319");
			params.put("state", "delivery");
			params.put("startDate", "2015-12-31");
			params.put("endDate", "2015-12-31");
			params.put("start", "0");
			params.put("num", "100");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
	}
	
	
	@Test
	public void testCourierAliPayOrder() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/orderController.do?courierAliPayOrder";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courierid", "59319");
			params.put("orderid", "130152");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
	
	}
	
	@Test
	public void testCourierWeixinPayOrder() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/orderController.do?courierWeixinPayOrder";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courierid", "59319");
			params.put("orderid", "129621");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doGet();
			System.out.println(response);
		}
	}
	
	@Test
	public void testSendVerficode()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "/ci/smsController.do?sendSms&phone=15878368360&content=test";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			
			String response = httpProxy.doGet();
			System.out.println(response);
		}
		
	}
	
	@Test
	public void testCourierPosition()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/courierController.do?getUserRenewalLocation";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", "59319");
			//params.put("orderid", "129621");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doPost();
			System.out.println(response);
		}
		
	}
	
	@Test
	public void testSetCourierPosition()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/courierController.do?saveUserRenewalLocation";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", "59319");
			params.put("longitude", "129.621");
			params.put("latitude", "123.621");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doPost();
			System.out.println(response);
		}

	}
	
	
	@Test
	public void testPrintOrder()throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/orderController.do?printOrder";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("orderId", "129797");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doPost();
			System.out.println(response);
		}
	}
	
	@Test
	public void testGetBalance() throws Exception{
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/wUserController.do?getBalanceNew";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", "59319");
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doPost();
			System.out.println(response);
		}
	}
	
	@Test
	public void testGetRand() throws Exception{
		
		String sessionKey = getSessionKey();
		if(sessionKey != null){
			String url = APP_URL + "ci/wUserController.do?getRand";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courierId", "59319");
			params.put("startDate", "2015-12-20");
			params.put("endDate", "2016-01-05");
			params.put("start", 1);
			params.put("num", 3);
			params.put("sessionkey", sessionKey);
			HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
			String response = httpProxy.doPost();
			System.out.println(response);
		}
	}
	
	private String getSessionKey(){
		String url = APP_URL + "/controller.do?login";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "getAccess");
		params.put("login_name", "fshdskjf");
		params.put("password", "1367jhd");
		HttpProxy httpProxy = HttpProxy.createInstance(url, null, params);
		try {
			String response = httpProxy.doGet();
			
			JSONObject jsonObject = (JSONObject)JSON.parseObject(response);
			return jsonObject.getString("sessionkey");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
