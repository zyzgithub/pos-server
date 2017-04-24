package codegenerate;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wp.CommonUtil;
import com.wp.WeiXinOauth2Token;


public class Test {
	public static void main(String[] args){
		
		/*
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		String orderNumStr = df.format(22222);	//格式化排号
		
		System.out.println(orderNumStr);
		*/
		
		//System.out.println(URLEncoder.encode("http://192.168.1.100/WM/ci/tvLoginController.do?login&id=8e00e2c8-0f18-491d-befe-d4befe6c89d2"));
		
		/*
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		long start=(calendar.getTime().getTime()/1000);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		long end=(calendar.getTime().getTime()/1000);
		
		System.out.println("start:"+start);
		System.out.println("  end:"+end);
		*/
		

		testUpdate();//模拟更新unionId和openId
		
	}
	
	/**
	 * 更新微信的unionId和openId
	 * @author jusnli
	 * @email  545987886@qq.com
	 */
	public static void testUpdate(){
		//获取accessToken
		String accessToken= Test.getAccessToken("wxf3c01ed21e6e9c6f", "e01c86b04a82ccb1630a0b89305913f7");//点吧外卖
		System.out.println("accessToken："+accessToken);
		//获取用户列表
		if (accessToken!=null) {
			List<String> openIdList= Test.getOpenIdList(accessToken);
			System.out.println("获取到openId数量："+openIdList.size());
			if (openIdList.size()>0) {
				System.out.println("openId、unionId列表：");
				int i = 0;
				for (; i < openIdList.size(); i++) {//获取unionid
					WeiXinOauth2Token wxot= Test.getWeiXinOauth2TokenByOpenId(accessToken, openIdList.get(i));
					System.out.println("openid="+wxot.getOpenId()+",unionid:"+wxot.getUnionId());
					
					//更新用户的unionid
					if (i>10) {//测试不能查找太多用户信息
						break;
					}
				}
				System.out.println("共获取到unionId数量："+i);
			}
		}else{
			System.out.println("获取accessToken失败");
		}
		
		
		
	}
	
	/**
	 * 获取微公众号accessToken
	 * @author jusnli
	 * @email  545987886@qq.com
	 * @param APPID
	 * @param APPSECRET
	 * @return
	 */
	public static String getAccessToken(String APPID,String APPSECRET){
		String accessToken=null;
		//String APPID="wxf3c01ed21e6e9c6f";
		//String APPSECRET="e01c86b04a82ccb1630a0b89305913f7";
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
		JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(requestUrl, "GET", null));
		
		if (null != jsonObject) {
			accessToken = jsonObject.getString("access_token");
		}
		return accessToken;
	}
	
	/**
	 * 获取用户列表
	 * @author jusnli
	 * @email  545987886@qq.com
	 * @param accessToken
	 * @return
	 */
	public static List<String> getOpenIdList(String accessToken){
		List<String> openIds=new ArrayList<String>();
		
		Integer total=0;
		String next_openid="";
		do {
			String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken+"&next_openid="+next_openid;
			JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(requestUrl, "GET", null));
			if (null != jsonObject) {
				total=jsonObject.getInt("total");//总共用户数量
				//Integer count=jsonObject.getInt("count");//当前拿回条数
				next_openid=jsonObject.getString("next_openid");//该页结束的openid
				
				JSONObject data=jsonObject.getJSONObject("data");
				JSONArray openIdArray=data.getJSONArray("openid");
				for (int i = 0; i < openIdArray.size(); i++) {
					openIds.add(openIdArray.getString(i));
				}
			}
		} while (openIds.size()<total);
		System.out.println("获取到openId数量："+openIds.size());
		return openIds;
	}
	
	/**
	 * 获取微信用户授权信息
	 * @author jusnli
	 * @email  545987886@qq.com
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public static WeiXinOauth2Token getWeiXinOauth2TokenByOpenId(String accessToken,String openId){
		WeiXinOauth2Token wat=null;
		String requestUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId;
		JSONObject jsonObject = JSONObject.fromObject(CommonUtil.httpsRequest(requestUrl, "GET", null));
		if (null != jsonObject) {
			if (jsonObject.containsKey("openid")) {
				wat=new WeiXinOauth2Token();
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setUnionId(jsonObject.getString("unionid"));
			}
		}
		return wat;
	}
}
