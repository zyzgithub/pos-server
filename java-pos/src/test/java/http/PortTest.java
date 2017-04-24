package http;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.wm.controller.open_api.tswj.HttpUtils;

public class PortTest {
	private String url = "http://third.iamgenius.com.cn";
	private String key = "d601e083-f8e6-45d9-88d9-271c189e80aa";
	
	/**
	 * 支付成功回调i玩派
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void cpsPayCallback() throws UnsupportedEncodingException{
		url = url + "/order/cpsPayCallback";
		TreeMap<String,Object> params = new TreeMap<String,Object>();
		params.put("tno", "150827000642008");//i玩派的订单编号
		params.put("price", 9999d);//订单金额
		params.put("payTime", "2015-11-16 15:59:59");//支付时间 yyyy-MM-dd HH:mm:ss
		params.put("payMethod", "ORDER_MZ");//固定返回ORDER_MZ
		params.put("transId", "");//微信支付流水号
		params.put("status", "success");//固定返回success
		params.put("appKey",key);//加入签名key
		String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
		params.put("sign", sign.toUpperCase());//放入签名
		params.remove("appKey");//移除签名key
		System.out.println(JSON.toJSONString(params));
		HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
	}
	
	/**
	 * 取消订单成功回调i玩派
	 * @throws UnsupportedEncodingException
	 */
	//{errMsg=null, payMethod=ORDER_MZ, 
	//payTime=1970-01-18 02:13, price=0, 
	//refundId=1447985698651, refundTime=1447985708, 
	//sign=73D9C245AD6015EC87662552AA8B8F41, status=success, 
	//tno=151120003038171, transId=151120003038171_yihaowaimai}
	public static void main(String[] args) {
		for (int i = 0; i < 459; i++) {
			System.out.println(RandomUtils.nextInt(145, 165));
		}
	}
	@Test
	public void cpsRefundCallback() throws UnsupportedEncodingException{
		url = url + "/order/cpsRefundCallback";
		TreeMap<String,Object> params = new TreeMap<String,Object>();
		params.put("tno", "151120003048929");//i玩派的订单编号
		params.put("price", 0.01d);//订单金额
		params.put("status", "success");//固定返回success
		params.put("errMsg", "111");//退款失败原因 没有传入null
		params.put("refundId", "1447997087859");//微信退款单号
		params.put("refundTime", "2015-11-20 13:24:30");//退款时间
		
		params.put("appKey",key);//加入签名key
		String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
		params.put("sign", sign.toUpperCase());//放入签名
		params.remove("appKey");//移除签名key
		
		HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
	}
	
	/**
	 * 支付之前查询订单状态
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void detail() throws UnsupportedEncodingException{
		url = url + "/order/detail";
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("tno", "150827000642008");//i玩派的订单编号
		params.put("appKey",key);//加入签名key
		String sign = DigestUtils.md5Hex(JSON.toJSONString(params).getBytes("utf-8"));
		params.put("sign", sign.toUpperCase());//放入签名
		params.remove("appKey");//移除签名key
		HttpUtils.httpPostRequest(url, JSON.toJSONString(params));
	}
	
}
