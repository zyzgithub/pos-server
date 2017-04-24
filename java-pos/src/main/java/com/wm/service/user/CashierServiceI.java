package com.wm.service.user;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.PageVO;
import com.base.exception.BusinessException;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.supermarket.CashierEntity;

public interface CashierServiceI extends CommonService{

	/**
	 * 根据商家ID获取所有的收银员
	 * @param merchantId 商家Id
	 * @return
	 */
	List<CashierEntity> getCashiers(Integer merchantId);
	
	/**
	 * 分页获取商家对应的收银员列表
	 * @param merchantId 商家Id
	 * @param pageNo	页码
	 * @param pageSize	页码大小
	 * @return
	 */
	PageVO<Map<String, Object>> getCashiersByPage(Integer merchantId, int pageNo, int pageSize);
	
	/**
	 * 保存或更新收银员
	 * @param vo
	 * @throws 
	 */
	void saveOrUpdate(CashierVo vo) throws BusinessException;
	
	/**
	 * 根据ID查找对应的收银员
	 * @param id
	 * @return
	 */
	CashierVo get(Integer id);
	
	/**
	 * 删除营业员
	 * @param id
	 * @return
	 */
	boolean delete(Integer id);
	
	/**
	 * 根据手机号码、密码登录
	 * @param mobile	手机号码
	 * @param password 	密码
	 * @return
	 */
	Map<String, Object> login(String mobile, String password) throws BusinessException; 
	
	/**
	 * 根据验证码登录
	 * @param merhantId	商家ID
	 * @param mobile	手机号码
	 * @param verifyCode	验证码
	 * @return
	 */
	Map<String, Object> loginByVerifyCode(Integer merchantId, String mobile, String verifyCode);
	
	
	/**
	 * 收银员退出登录
	 * @param cashierId
	 * @return
	 */
	Map<String, Object> exitLogin(Integer cashierId);
	
	
	/**
	 * 获取收银员最近一次登录时间
	 * @param cashierId 收银员
	 * @return 格式 yyyy-MM-dd HH:mm:ss
	 */
	String getLatestLoginTime(Integer cashierId);
	
	/**
	 * 保存收银员登录、退出日志
	 * @param cashier
	 * @param loginType
	 */
	void saveCashierLoginLog(CashierEntity cashier, int loginType, String deviceCode);
	
	/**
	 * 根据手机号码、密码登录
	 * @param mobile	手机号码
	 * @param password 	密码
	 * @return
	 */
	Map<String, Object> newLogin(String mobile, String password, String deviceCode, int posEdition) throws BusinessException;
	
	/**
	 * 根据收银员确定商家绑定的pos版本
	 * @param cashierId
	 * @return
	 */
	Integer getPosEdition (Integer cashierId);
	
}
