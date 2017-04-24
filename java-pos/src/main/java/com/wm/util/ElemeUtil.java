package com.wm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用惹饿了么接口工具类
 * 饿了么接口文档：http://openapi.eleme.io/v2/quickstart.html
 * 测试下单地址：http://r.ele.me/openapi-test/
 * 接口根路径：http://v2.openapi.ele.me/
 * 测试consumer_key 		0170804777 商家key
 * 测试consumer_secret 	87217cb263701f90316236c4df00d9352fb1da76 商家密钥
 * 测试restaurant_id 	62028381 餐厅id
 * 
 * 
 * @author lfq
 * @email  545987886@qq.com
 * @2015-5-14
 * 
 * 订单状态
 * 状态	含义
 * -2	STATUS_CODE_PENDING（订单创建中）
 * -1	STATUS_CODE_INVALID（订单已取消）
 * 0	STATUS_CODE_UNPROCESSED（订单未处理）
 * 1	STATUS_CODE_PROCESSING（订单等待餐厅确认）
 * 2	STATUS_CODE_PROCESSED_AND_VALID（订单已处理）
 * 11	STATUS_CODE_USER_CONFIRMED（用户确认收到）
 * 
 * 
 * 订单取消原因类型
 * 状态	含义
 * 0	OTHERS（其它原因）
 * 1	TYPE_FAKE_ORDER（假订单）
 * 2	TYPE_DUPLICATE_ORDER（重复订单）
 * 3	TYPE_FAIL_CONTACT_RESTAURANT（联系不上餐厅）
 * 4	TYPE_FAIL_CONTACT_USER（联系不上用户）
 * 5	TYPE_FOOD_SOLDOUT（食物已售完）
 * 6	TYPE_RESTAURANT_CLOSED（餐厅已打烊）
 * 7	TYPE_TOO_FAR（超出配送范围）
 * 8	TYPE_RST_TOO_BUSY（餐厅太忙）
 * 9	INVALID_DESC_TYPE_FORCE_REJECT_ORDER(用户无理由退单）
 * 10	INVALID_DESC_TYPE_DELIVERY_CHECK_FOOD_UNQUALIFIED（配送方检测餐品不合格）
 * 11	INVALID_DESC_TYPE_DELIVERY_FAULT（由于配送过程问题,用户退单）
 * 12	INVALID_DESC_TYPE_REPLACE_ORDER（订单被替换）
 * 13	TYPE_USR_CANCEL_ORDER（用户取消订单）
 * 
 * 错误码说明
 * 错误码	名称	说明
 * 1000	PERMISSION_DENIED	权限错误
 * 1001	SIGNATURE_ERROR	签名错误
 * 1002	SYSTEM_PARAM_ERROR	系统级参数错误
 * 1003	INVALID_CONSUMER	非法用户
 * 1004	INVALID_REQUEST_PARAM	非法请求参数
 * 1005	INVALID_ONLINE_PAYMENT_ORDER_VALIDATION	在线支付订单验证错误
 * 1006	SYSTEM_ERROR	系统错误
 * 1007	ELEME_SYSTEM_ERROR	饿了么业务系统错误
 * 1008	OPENAPI_SYSTEM_ERROR	开放平台错误
 * 1009	RATE_LIMIT_REACHED	超过请求限制
 * 1010	GPG_KEY_NOT_FOUND	GnuPG公钥未找到
 * 1011	APPLICATION_NOT_FOUND	开放平台应用未找到
 * 1012	ORDER_NOT_FOUND	订单未找到
 * 1013	ORDER_CANCELED	订单已取消
 * 
 */
public class ElemeUtil {
	
	private static final String consumer_key="0170804777";							//商户key
	private static final String consumer_secret="87217cb263701f90316236c4df00d9352fb1da76";	//商户secret
	private static String restaurant_id="62028381";									//餐厅id
	
	
	/**
	 * 参数使用&拼接
	 * @return
	 */
	public static String concatParams(Map<String, String> params2) {
        Object[] key_arr = params2.keySet().toArray();
        Arrays.sort(key_arr);
        String str = "";

        for (Object key : key_arr) {
            String val = params2.get(key);
            str += "&" + key + "=" + val;
        }

        return str.replaceFirst("&", "");
    }

    /**
     * 获取HexString
     * @return
     */
	public static String byte2hex(byte[] b) {
        StringBuffer buf = new StringBuffer();
        int i;

        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }

        return buf.toString();
    }

    /**
     * 获取饿了么签名
     */
	public static String genSig(String pathUrl, Map<String, String> params,
                                String consumerSecret) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String str = concatParams(params);
        str = pathUrl + "?" + str + consumerSecret;
        MessageDigest md = MessageDigest.getInstance("SHA1");
        return byte2hex(md.digest(byte2hex(str.getBytes("UTF-8")).getBytes()));
    }
    
    /**
	 * 返还请求地址的数据，可以跨域
	 * @author lfq
	 * @time 2014-11-22 上午11:08:01
	 * @param strURL 请求地址
	 * @return
	 * @throws Exception
	 */
    public static String getDataFromURL(String strURL) throws Exception {
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		InputStreamReader reder = new InputStreamReader(conn.getInputStream(), "utf-8");

		BufferedReader breader = new BufferedReader(reder);

		String content = "";
		String result = "";
		while ((content = breader.readLine()) != null) {
			result += content;
		}
		
		return result;

	}
    
    /**
	 * 调用饿了么接口得到返回的结果
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param url		饿了么接口地址
	 * @param params	请求参数,无参数时可以传null,默认会加上系统参数consumer_key,timestamp和签名sig
	 * @return
	 * @throws Exception
	 */
    public static String getResult(String url,Map<String, String> params) throws Exception{
		if (params==null) {
			params=new HashMap<String, String>();
		}
		if (!params.containsKey("consumer_key")) {
			params.put("consumer_key", ElemeUtil.consumer_key);
		}
		if (!params.containsKey("timestamp")) {
			params.put("timestamp", ((new Date()).getTime()/1000)+"");//时间戳自1970年1月1日开始的秒数
		}
		
		String sig=ElemeUtil.genSig(url, params, ElemeUtil.consumer_secret);
		params.put("sig", sig);
		StringBuilder requestUrl=new StringBuilder(url);
		requestUrl.append("?");
		requestUrl.append(ElemeUtil.concatParams(params));
		System.out.println("请求地址:"+requestUrl.toString());
		
		String result=ElemeUtil.getDataFromURL(requestUrl.toString());
		System.out.println(result);
		return result;
	}
	
	/**
	 * 获取饿了么新订单列表
	 * @author lfq
	 * @email  545987886@qq.com
	 * @return
	 * {"message":"ok","code":200,"data":{"order_ids":[12769175013782281,12969075916214381,12269875217994981]}}
	 * @throws Exception
	 */
	public static String getNewOrder() throws Exception{
		String url="http://v2.openapi.ele.me/order/new/";//接口地址
		Map<String, String> params=new HashMap<String, String>();
		params.put("restaurant_id", ElemeUtil.restaurant_id);
		String result=ElemeUtil.getResult(url, params);
		return result;
	}
	

	/**
	 * 取消饿了么订单
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param eleme_order_id  饿了么订单id
	 * @param reason		   取消原因,必须
	 * @return
	 * {"message":"ok","code":200,"data":{"status_code":0,"extra":null}}  status_code即为订单状态
	 * @throws Exception
	 */
	public static String cancelOrder(String eleme_order_id,String reason) throws Exception{
		if (reason==null) reason="";
		String url="http://v2.openapi.ele.me/order/"+eleme_order_id+"/status/";//接口地址
		Map<String, String> params=new HashMap<String, String>();
		params.put("status", "-1");
		params.put("reason", reason);
		
		String result=ElemeUtil.getResult(url, params);
		return result;
	}

	/**
	 * 确认饿了么订单
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param eleme_order_id  饿了么订单id
	 * @return
	 * {"message":"ok","code":200,"data":{"status_code":0,"extra":null}}  status_code即为订单状态
	 * @throws Exception
	 */
	public static String confirmOrder(String eleme_order_id) throws Exception{
		String url="http://v2.openapi.ele.me/order/"+eleme_order_id+"/status/";//接口地址
		Map<String, String> params=new HashMap<String, String>();
		params.put("status", "2");
		String result=ElemeUtil.getResult(url, params);
		return result;
	}
	
	/**
	 * 获取饿了么订单状态
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param  eleme_order_id  饿了么订单id
	 * @return {"message":"ok","code":200,"data":{"status_code":0,"extra":null}}  status_code即为订单状态
	 * @throws Exception
	 */
	public static String getOrderStatus(String eleme_order_id) throws Exception{
		String url="http://v2.openapi.ele.me/order/"+eleme_order_id+"/status/";//接口地址
		String result=ElemeUtil.getResult(url, null);
		return result;
	}
	
	/**
	 * 获取饿了么订单信息
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param eleme_order_id  饿了么订单id
	 * @return 
	 * {"message":"ok","code":200,"data":{"deliver_time":null,"total_price":5.51,
	 * "restaurant_name":"饿了么体验店","description":"","order_id":"12369875712636681",
	 * "status_code":0,"created_at":"2015-05-14 19:13:42","phone_list":["15918549510"],
	 * "detail":{"abandoned_extra":[],"group":[[{"category_id":1,"name":"蓝莓果冻布丁 热",
	 * "price":0.01,"garnish":[],"id":16467379,"quantity":1}]],
	 * "extra":[{"description":"","price":0.5,"name":"餐盒费","category_id":102,"id":-70000,"quantity":1},
	 * {"description":"","price":5,"name":"配送费","category_id":2,"id":-10,"quantity":1}]},
	 * "deliver_fee":5.0,"restaurant_id":62028381,"invoice":"","address":"广州市天河区岗顶百脑汇2204",
	 * "user_id":14810978,"user_name":"jusnli","is_online_paid":0,"is_book":0}}
	 * @throws Exception
	 */
	public static String getOrderInfo(String eleme_order_id) throws Exception{
		String url="http://v2.openapi.ele.me/order/"+eleme_order_id+"/";//接口地址
		String result=ElemeUtil.getResult(url, null);
		return result;
	}
	
	/**
	 * 获取城市列表
	 * @author lfq
	 * @email  545987886@qq.com
	 * @return 
	 * @throws Exception
	 */
	public static String getCitys() throws Exception{
		String url="http://v2.openapi.ele.me/cities/";//接口地址
		String result=ElemeUtil.getResult(url, null);
		return result;
	}
	
	
	/**
	 *
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String eleme_order_id="12369875712636681";//饿了么订单id
		//getNewOrder();	//获取新订单
		//confirmOrder(eleme_order_id);//确认订单
		//cancelOrder(eleme_order_id,"太远了送不了");//取消订单
		getOrderInfo(eleme_order_id);//获取订单详情
		//getOrderStatus(eleme_order_id);//获取订单转台
		//getCitys();			//获取城市列表
	}

}
