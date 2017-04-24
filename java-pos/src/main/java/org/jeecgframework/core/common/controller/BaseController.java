package org.jeecgframework.core.common.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.common.SessionInfo;
import org.jeecgframework.core.common.model.common.UserInfo;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.interceptors.DateConvertEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.OpenUserInfo;
import com.wm.controller.user.AccountGenerator;
import com.wm.entity.location.Location;
import com.wm.entity.user.WUserEntity;
import com.wm.service.user.WUserServiceI;
import com.wm.util.IPUtil;
import com.wp.AccessTokenContext;
import com.wp.AdvancedUtil;
import com.wp.CommonUtil;
import com.wp.ConfigUtil;
import com.wp.WeiXinOauth2Token;

import jeecg.system.pojo.base.TSUser;


/**
 * 基础控制器，其他控制器需集成此控制器获得initBinder自动转换的功能
 * 
 * 
 */
@Controller
@RequestMapping("/baseController")
public class BaseController {
	
	@Autowired
	private WUserServiceI wUserService;
	
	private static final Logger logger = Logger.getLogger(BaseController.class);
	// wap版登录后用户id在的session对应key名
	private static final String LOGIN_USER_SESSION_KEY = "LOGIN_USER_SESSION_KEY";
	protected static final String WX_USER_OPEN_ID = "WX_USER_OPEN_ID";
	private static final String LOCATION_LNG = "location_lng";
	private static final String LOCATION_LAT = "location_lat";
	private static final String LOCATION_ADD = "location_add";
	private static final String LOCATION_CITY = "location_city";
	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
	}
	
	public TSUser getSessionUser(HttpServletRequest request){
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(Globals.USER_SESSION);
		TSUser userVO = sessionInfo.getUser();
		return userVO;
	}
	
	/**
	 * 判断是否为微信打开
	 * 
	 * @author lfq
	 * @param request
	 * @return true/false,为true是表示为微信打开
	 */
	public Boolean isWeixin(HttpServletRequest request) {
		// 测试模拟登录
		if (ConfigUtil.isTest) 
			return true;
			
		String userAgent = request.getHeader("user-agent");
		if(StringUtils.isEmpty(userAgent))
			return true;
		return StringUtils.contains(userAgent.toLowerCase(), "micromessenger");
	}
	
	public Location getLocation(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object lng = session.getAttribute(LOCATION_LNG);
		Object lat = session.getAttribute(LOCATION_LAT);
		Object address = session.getAttribute(LOCATION_ADD);
		Object city = session.getAttribute(LOCATION_CITY);
		if(null != lng && null != lat && null != address && null != city)
			return new Location((Double)lng, (Double)lat, (String)address, (String)city);
		return null;
	}

	public void setLocation(HttpServletRequest request, double lng, double lat, String address, String city) {
		HttpSession session = request.getSession();
		session.setAttribute(LOCATION_ADD, address);
		session.setAttribute(LOCATION_LAT, lat);
		session.setAttribute(LOCATION_LNG, lng);
		session.setAttribute(LOCATION_CITY, city);
	}
	
	public UserInfo getUserInfo(HttpServletRequest request) {
		UserInfo u = new UserInfo();
		// 测试模拟登录
		if (ConfigUtil.isTest) {
			u.setUserId(ConfigUtil.testUserId);
			return u;
		}

		// step 1 判断session之前是否已经保存userId
		Integer userId = getUserId(request);
		logger.info("getuserId-----" + userId);

		if (null != userId) {
			u.setUserId(userId);
			return u;
		}

		// step 2 根据openid获取用户
		Object wxOpenId = request.getSession(true).getAttribute(WX_USER_OPEN_ID);
		if (null != wxOpenId) {
			u.setRedirectUrl(true);
			u.setUserId(getUserId(wxOpenId.toString(), request, u));
			return u;
		}

		// step 3 当来到这一步，证明用户没有登录，则通过微信code获取当前用户在微信的唯一id
		if (isWeixin(request)) {

			// 根据weixinCode 获取当前用户的微信权限
			String weixinCode = request.getParameter("code");
			logger.info("weixinCode 获取当前用户的微信权限" + weixinCode);
			if (null == weixinCode) {
				return u;
			}

			WeiXinOauth2Token oauth = AdvancedUtil.getOauth2AccessToken(ConfigUtil.APPID, ConfigUtil.APP_SECRECT, weixinCode);
			logger.info("openid-----" + oauth.getOpenId());
			if (StringUtils.isEmpty(oauth.getOpenId())) {
				return u;
			} else {
				request.getSession(true).setAttribute(WX_USER_OPEN_ID, oauth.getOpenId());
			}

			u.setRedirectUrl(true);
			u.setUserId(getUserId(oauth.getOpenId(), request, u));
			return u;

		}

		return u;
	}
	
	
	public OpenUserInfo getWeixinUserInfo(HttpServletRequest request){
		OpenUserInfo u = new OpenUserInfo();
		// step 2 根据openid获取用户
		Object wxOpenId = request.getSession(true).getAttribute(WX_USER_OPEN_ID);
		if (null != wxOpenId) {
			u.setRedirectUrl(true);
			u.setUserId(getUserId(wxOpenId.toString(), request, u));
			u.setOpenid(wxOpenId.toString());
			return u;
		}

		// step 3 当来到这一步，证明用户没有登录，则通过微信code获取当前用户在微信的唯一id
		if (isWeixin(request)) {

			// 根据weixinCode 获取当前用户的微信权限
			String weixinCode = request.getParameter("code");
			logger.info("weixinCode 获取当前用户的微信权限" + weixinCode);
			if (null == weixinCode) {
				return u;
			}

			WeiXinOauth2Token oauth = AdvancedUtil.getOauth2AccessToken(ConfigUtil.APPID, ConfigUtil.APP_SECRECT, weixinCode);
			logger.info("openid-----" + oauth.getOpenId());
			if (StringUtils.isEmpty(oauth.getOpenId())) {
				return u;
			} else {
				request.getSession(true).setAttribute(WX_USER_OPEN_ID, oauth.getOpenId());
			}

			u.setRedirectUrl(true);
			u.setUserId(getUserId(oauth.getOpenId(), request, u));
			u.setOpenid(oauth.getOpenId());
			return u;

		}
		
		return u;
	}
	
	/**
	 * 微信用户自动注册
	 * @param request
	 * @param openId 用户的微信openId
	 * @return
	 */
	public Integer autoRegister(HttpServletRequest request, String openId){
		if(StringUtils.isEmpty(openId)) return null;
		return getUserId(openId, request, new UserInfo());
	}
	
	/**
	 * 统一获取登录帐号的用户id，在微信授权界面调用可以实现微信登录 微信时自动登录，非微信并且未登录时返回null
	 * 
	 * @author lfq
	 * @param request
	 * @param unionid
	 *            用户的unionid，默认不需要传，默认是取微信传回来的code来登录的，
	 *            如果指定unionid则会使用指定的unionid来登录
	 * @return 返回登录用户的id
	 * @throws Exception
	 */
	public Integer getUserId(HttpServletRequest request, String unionid) {
		// 测试模拟登录
		return getUserInfo(request).getUserId();
	}

	public void setUserId(HttpServletRequest request, Integer userId) {
		request.getSession(true).setAttribute(LOGIN_USER_SESSION_KEY, userId);
	}
	
	private Integer getUserId(HttpServletRequest request) {
		Object obj = request.getSession(true).getAttribute(LOGIN_USER_SESSION_KEY);
		if(null != obj)
			return Integer.valueOf(obj.toString());
		return null;
	}
	
	/**
	 * 通过openId获取客户资料，如果不存在，则新增一个
	 * @param openId
	 * @param request
	 * @param u
	 * @return
	 */
	protected Integer getUserId(String openId, HttpServletRequest request, UserInfo u) {
		WUserEntity wxUser = wUserService.findByOpenId(openId);
					
		logger.info("数据库用户"+wxUser);
		if(null == wxUser) {
			// 拼获取用户信息的URL
			String getWXUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + AccessTokenContext.getAccessToken() + "&openid=" + openId;
			JSONObject json = JSONObject.parseObject(CommonUtil.httpsRequest(getWXUrl, "GET", null));
			wxUser = new WUserEntity();
			
			wxUser.setOpenId(openId);
			wxUser.setUnionId(StringUtils.defaultString(json.getString("unionid")));
			wxUser.setUsername(AccountGenerator.getAccount());
			wxUser.setPassword(StringUtils.EMPTY);
			wxUser.setPayPassword(StringUtils.EMPTY);
			wxUser.setMobile(StringUtils.EMPTY);
			wxUser.setSns(StringUtils.EMPTY);
			wxUser.setNickname(StringUtils.defaultString(json.getString("nickname")));
			wxUser.setPhotoUrl(StringUtils.defaultString(json.getString("headimgurl")));
			wxUser.setIp(IPUtil.getRemoteIp(request));
			wxUser.setGender(json.getIntValue("sex") == 1 ? "M" : "F");
			wxUser.setFirstOrderTime(0);
			wxUser.setIsDelete(WUserEntity.SERVING_STATE);
			
			wUserService.save(wxUser);
			u.setNewUser(true);
			logger.info("-----新增用户：id-----"+wxUser.getId());
		}	
		// 把用户id保存到session中
		setUserId(request, wxUser.getId());
		return wxUser.getId();
	}
}
