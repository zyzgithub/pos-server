package com.courier_mana.personal.service;

import java.util.Map;

import com.wm.entity.user.WUserEntity;

/**
 * 
 * 快递员个人中心-我的资料接口
 *
 */
public interface CourierMyInfoService {

	/**
	 * 获取快递员实体
	 * @param courierId
	 * @return
	 */
	WUserEntity getWUserEntity(Integer courierId);
	
	/**
	 * 获取我的资料页面信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getMyInfo(Integer courierId);
	
	/**
	 * 用户修改密码
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public boolean updatePassword(int courierId, String newPassword);
	
	/**(OvO)
	 * 检查指定用户是否为合作商
	 * @param userId	用户ID
	 * @return	返回判断结果(true: 合作商, false: 一号生活平台)
	 */
	public boolean isAgentUser(Integer userId);
}
