package com.unionpay.upmp.sdk.examples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.unionpay.upmp.sdk.conf.UpmpConfig;
import com.unionpay.upmp.sdk.service.UpmpService;

/**
 * 类名：订单推送请求接口实例类文件
 * 功能：订单推送请求接口实例
 * 版本：1.0
 * 日期：2012-10-11
 * 作者：中国银联UPMP团队
 * 版权：中国银联
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */
public class PurchaseExample{
	
	public static void main(String[] args){
        // 请求要素
		Map<String, String> req = new HashMap<String, String>();
		req.put("version", UpmpConfig.VERSION);// 版本号
		req.put("charset", UpmpConfig.CHARSET);// 字符编码
		req.put("transType", "01");// 交易类型
		req.put("merId", UpmpConfig.MER_ID);// 商户代码
		req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);// 通知URL
		req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);// 前台通知URL(可选)
		req.put("orderDescription", "订单描述");// 订单描述(可选)
		req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 交易开始日期时间yyyyMMddHHmmss
//		req.put("orderTimeout", "");// 订单超时时间yyyyMMddHHmmss(可选)
		req.put("orderNumber", generateOrdrNo());//订单号(商户根据自己需要生成订单号)
		req.put("orderAmount", "1");// 订单金额
        req.put("orderCurrency", "156");// 交易币种(可选)
        req.put("reqReserved", "透传信息");// 请求方保留域(可选，用于透传商户信息)
                
        // 保留域填充方法
        Map<String, String> merReservedMap = new HashMap<String, String>();
        merReservedMap.put("test", "test");
        req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)
		
		Map<String, String> resp = new HashMap<String, String>();
		boolean validResp = UpmpService.trade(req, resp);
	       
        // 商户的业务逻辑
        if (validResp){
            // 服务器应答签名验证成功
            System.out.println(resp);
        }else {
            // 服务器应答签名验证失败
            
        }
	}
	
	/**
	 * 订单号生成，该生产订单号仅用于测试，商户根据自己需要生成订单号
	 * @return
	 */
	public static String generateOrdrNo(){
		DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        StringBuilder sb = new StringBuilder(formater.format(new Date()));
        return sb.toString();
	}
	
}
	