package com.wxpay.refund;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.wp.WftAdvertisementUtils;

public class WftTest {

	@Test
	public void testAdv() {
		JSONObject json = new JSONObject();
//		json.put("sex", 1);
//		json.put("province", "广东省");
//		json.put("city", "深圳");
		json.put("request_type", 0);
//		json.put("appid", "wx8a3854d58201a090");
//		json.put("openid", "o3gAGuHT2eX5sGt407gRvYdbp8xY");
//		json.put("advert_type", 1);
//		json.put("out_trade_no", "48485152432536");
//		json.put("mch_name", "员村木桶饭");
//		json.put("body", "农家小炒肉"); 
//		json.put("pay_type", 0);
//		json.put("amount", 14);
//		json.put("trade_time", DateUtils.getDataString(DateUtils.yyyymmddhhmmss));
		
		System.out.println(json.toString());
		String response = WftAdvertisementUtils.setAdBody();
		System.out.println(response);
	}
	
	@Test
	public void testCustMsg(){
		JSONObject json = new JSONObject();
		json.put("touser", "o3gAGuHT2eX5sGt407gRvYdbp8xY");
		json.put("msgtype", "news");
		
//		json.put("msgtype", "text");
//		JSONObject textJson = new JSONObject();
//		textJson.put("content","iphone中奖通知");
//		json.put("text", textJson);
		
		String description = "付款金额：14元\n商品名称：雪里红\n商户名称：木桶饭\n交易订单：152415461";
		
		JSONObject newsJson = new JSONObject();
		JSONArray articlesJson = new JSONArray();
		JSONObject articleJson = new JSONObject();
		articleJson.put("title", "支付确认");
		articleJson.put("description", description);
		articleJson.put("url", "http://advert.swiftpass.cn/advert/click/redirectPage/10187894");
		articleJson.put("picurl", "http://mmbiz.qpic.cn/mmbiz_jpg/GyCF8Qw3Ug0524o4lY8MGBQ7spB89Rr8XsUXov8LMKRZekFrICNZ9CC3EDxXcLAic442Jj2tIQXOKJ5qvnYCh3Q/0?wx_fmt=jpeg");
		articlesJson.add(articleJson);
		newsJson.put("articles", articlesJson);
		json.put("news", newsJson);
		
		System.out.println(json.toString());
//		TemplateMessageUtils.sendCustomMessage(json.toJSONString());
	}
	
}

