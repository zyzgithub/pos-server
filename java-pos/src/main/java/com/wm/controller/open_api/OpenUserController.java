
package com.wm.controller.open_api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.controller.open_api.tswj.PortConfig;
import com.wm.entity.user.WUserEntity;
import com.wm.util.StringUtil;
import com.wm.util.security.DesEnc;
import com.wm.util.security.HttpUtils;
import com.wm.util.security.OpenSign;
import com.wp.CommonUtil;
import com.wp.ConfigUtil;

/**
 * <h1>用于第三方接口对接用户部分操作</h1>
 * 
 * @author folo
 * @version 1.5
 *
 */
@Controller
@RequestMapping("/openapi/customer")
public class OpenUserController extends OpenBaseController {
	private static final Logger logger = Logger.getLogger(OpenUserController.class);
	private final String oauthDesKey = "05712DF9-E8C2-4164-A539-187E49D5DF06";
	
	/**
	 * 去微信授权
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/oauth2Auto", method=RequestMethod.GET)
	public void oauth2Auto(HttpServletRequest request, HttpServletResponse response, String appid) throws IOException{
		String url = null;
		try {
			String state = DesEnc.ENCRYPTMethod(appid + "_" + System.currentTimeMillis(), oauthDesKey);
			StringBuilder sb = new StringBuilder();
			sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?")
					.append("appid=").append(ConfigUtil.APPID_KFZ)
					.append("&redirect_uri=").append(CommonUtil.urlEncodeUTF8(getBashPath(request) + "/openapi/customer/oauth2redirect.do"))
					.append("&response_type=code").append("&scope=snsapi_base").append("&state=" + state)
					.append("#wechat_redirect");
			url = sb.toString();
		} catch (Exception e) {
			logger.info(String.format("\n ------【oauth2Auto】 exception:", e.toString()));
			url = getBashPath(request) + "/error.jsp";
		}
		logger.info("\n -----【oauth2Auto】 url:%s" + url);
		response.getWriter().write("<script>window.location.href=\""+url+"\"</script>");
	}
	
	/**
	 * 微信授权回调
	 * @param code
	 * @param state
	 */
	@RequestMapping(value="/oauth2redirect", method=RequestMethod.GET)
	public String oauth2redirect(String code, String state){
		if(logger.isInfoEnabled())
			logger.info(String.format("\n ------【oauth2redirect】 code=%s  state=%s", code, state));
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?")
			.append("appid=").append(ConfigUtil.APPID_KFZ)
			.append("&secret=").append(ConfigUtil.APP_SECRECT_KFZ)
			.append("&code=").append(code)
			.append("&grant_type=").append("authorization_code");
			
			//静默授权 获取授权信息
			JSONObject json = HttpUtils.get(sb.toString());
			if(null != json && !StringUtil.isEmpty(json.getString("openid"))){
				String appid = DesEnc.decrypt(state, oauthDesKey).split("_")[0];
				if(PortConfig.TSWJ_APPID.equals(appid)){//天生玩家
					return "redirect:/openapi/customer/toThirdPage.do?openid=" + json.getString("openid");
				}
			}
			logger.info(String.format("\n ------【oauth2redirect】 ret json:", json));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:takeout/error";
	}
	
	/**
	 * <h1>已登录用户获取第三方签名</h1>
	 * @return
	 */
	@RequestMapping(value="/token", method=RequestMethod.GET)
	@ResponseBody
	public OpenResult token(HttpServletRequest request, Boolean isDebug) {
		//获取openid
		OpenUserInfo info = getWeixinUserInfo(request);
		String openid = info.getOpenid();
		if(null != isDebug && isDebug.booleanValue()) openid = "testopenid";//调试openid
		if(StringUtil.isEmpty(openid)){
			return OpenResult.ERR().msg("微信授权失败！");
		}
		//加密生成token
		String token = OpenSign.EncToken(openid, PortConfig.TSWJ_APPID, PortConfig.TOKEN_KEY);
		String sign = getSign(token, null, new HashMap<String, Object>());
		return State.Success.ret()
				.put("token", token)
				.put("sign", sign);
	}
	
	@RequestMapping(value="/toThirdPage", method=RequestMethod.GET)
	public void toThirdPage(HttpServletRequest request, HttpServletResponse response, String openid, Boolean isDebug) throws IOException{
		if(StringUtil.isEmpty(openid)){
			//获取openid
			OpenUserInfo info = getWeixinUserInfo(request);
			openid = info.getOpenid();
		}else request.getSession(true).setAttribute(WX_USER_OPEN_ID, openid);
		if(logger.isInfoEnabled())
			logger.info("\n ------【toThirdPage】 openid:" + openid);
		if(StringUtil.isEmpty(openid)){//最终未得到openid 去微信授权
			String url = getBashPath(request) + "/openapi/customer/oauth2Auto.do?appid=" + PortConfig.TSWJ_APPID;
			response.getWriter().write("<script>window.location.href=\"" + url + "\"</script>");
			return;
		}
		if(null != isDebug && isDebug.booleanValue()) openid = "testopenid";//调试openid
		String url = null;
		if(StringUtil.isEmpty(openid)){//微信授权失败
			String basePath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
			url = basePath + "/error.jsp";
		}else{
			//加密生成token
			String token = OpenSign.EncToken(openid, PortConfig.TSWJ_APPID, PortConfig.TOKEN_KEY);
			Long timestamp = System.currentTimeMillis();
			String sign = getSign(token, timestamp, new HashMap<String, Object>());
			
			StringBuilder sb = new StringBuilder();
			sb.append(PortConfig.TSWJ_ACTIVITY_URL)
			.append("?token=").append(token)
			.append("&sign=").append(sign)
			.append("&timestamp=").append(timestamp);
			url = sb.toString();
		}
		
		if(logger.isInfoEnabled())
			logger.info("----- 【toThirdPage】 url:" + url);
		response.getWriter().write("<script>window.location.href=\""+url+"\"</script>");
	}
	
	/**
	 * <h1>第三方请求获取用户信息</h1>
	 * @param token 用户加密后的token
	 * @param sign 商家签名
	 * @return
	 */
	@RequestMapping(value="/get", method=RequestMethod.POST)
	@ResponseBody
	public OpenResult getUser(String token, Long timestamp, String sign, HttpServletRequest request){
		OpenResult ret = doGetUser(token, timestamp, sign);
		if(logger.isInfoEnabled())
			logger.info(String.format("\n ---------------【getUser】 params:%s  \n return:%s", 
				JSON.toJSONString(request.getParameterMap()), ret.toString()));
		return ret;
	}
	
	private OpenResult doGetUser(String token, Long timestamp, String sign){
		if(StringUtil.isEmpty(token, timestamp, sign)) return State.ParamError.ret();
		
		//获取解密后的用户数据
		Object obj = getDesToken(token, timestamp, sign, null);
		if(obj instanceof OpenResult) return (OpenResult) obj;
		WUserEntity wuser = (WUserEntity) obj;
		
		//返回
		return OpenResult.OK()
				.put("cust_mobile", wuser.getMobile())
				.put("cust_name", wuser.getNickname());
	}
	
	/**
	 * <h1>第三方同步用户资料到本系统</h1>
	 * @param token 用户加密后的token
	 * @param sign 商家签名
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public OpenResult updateUser(String token, Long timestamp, String sign, 
			String cust_mobile, String cust_name, HttpServletRequest request){
		OpenResult ret = doUpdateUser(token, timestamp, sign, cust_mobile, cust_name);
		if(logger.isInfoEnabled())
			logger.debug(String.format("\n  ---------------【updateUser】params:%s  \n return:%s",
					JSON.toJSONString(request.getParameterMap()), ret.toString()));
		return ret;
	}
	
	private OpenResult doUpdateUser(String token, Long timestamp, String sign, 
			String cust_mobile, String cust_name){
		//校验请求参数
		if(StringUtil.isEmpty(token,timestamp,sign,cust_mobile, cust_name)) 
			return State.ParamError.ret();
		
		//获取请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cust_mobile", cust_mobile);
		params.put("cust_name", cust_name);
		
		//获取解密后的用户信息
		Object obj = getDesToken(token, timestamp, sign, params);
		if(obj instanceof OpenResult) return (OpenResult) obj;
		WUserEntity wuser = (WUserEntity) obj;
		
		//只有1号外卖不存在该用户号码时才允许
		if(!StringUtil.isEmpty(wuser.getMobile())) return State.AlreadyUser.ret();
		//更新数据
		wuser.setMobile(cust_mobile);
		wuser.setNickname(cust_name);
		boolean isUpdate = wUserService.updateUserInfo(wuser.getId(), wuser.getNickname(), wuser.getPhotoUrl(), wuser.getMobile());
		if(!isUpdate) return State.Error.ret();
		
		return State.Success.ret();
	}
}