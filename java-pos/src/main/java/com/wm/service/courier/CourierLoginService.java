package com.wm.service.courier;

import java.util.Map;

import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WUserEntity;

/**
 * 
 * 快递员个人中心-我的资料接口
 *
 */
public interface CourierLoginService {
	
	/**
	 * 根据手机号和用户类型获取用户信息
	 * @param mobile 用户手机号
	 * @return
	 */
	public WUserEntity getUserByMobile(String mobile);
	
	/**
	 * 用户重置密码
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public boolean resetPassword(int userId, String newPassword);
	
	/**
	 * 用户登录，并返回用户信息
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, Object> courierLogin(String username, String password);
	
	/**
	 * 验证用户是否存在中文名与密码，存在多条记录重复的情况
	 * @param username
	 * @param password
	 * @return
	 */
	public Long getCountByNameAndPwd(String username, String password);
	
	/**
	 * 保存登录记录
	 * @param userlogin
	 */
	public void createUserLogin(UserloginEntity userlogin);
}
