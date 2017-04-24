package com.wm.service.merchant;

import java.math.BigDecimal;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

public interface MerchantSupplyServiceI extends CommonService  {
	
	/**
	 * 验证商家支付密码是否正确
	 * @param userId
	 * @param payPassword
	 * @return
	 */
	public AjaxJson validatePayPassword(Integer userId, String payPassword);

	/**
	 * 余额支付
	 * @param payId
	 * @param userId
	 * @param origin
	 * @param type
	 * @param returnCallUrl
	 * @return
	 */
	public AjaxJson balanceConfirmPay(Long orderId, Integer userId, BigDecimal origin, String type, String payType, String detail);
	
	/**
	 * 微信支付
	 * @param userId
	 * @param payId
	 * @param origin 单位为分
	 * @param returnCallUrl
	 * @param code
	 * @return
	 */
	public AjaxJson weixinConfirmPay(String payId, String origin, String returnCallUrl, String code);
	
	/**
	 * 获取商家用户余额
	 * @param userId
	 * @return
	 */
	public Double getMerchantUserMoney(Integer userId);

	/**
	 * 获取商家账户余额
	 * @param userId
	 * @return
	 */
	public Double getMerchantAccountMoney(Integer userId);
	
	/**
	 * 商品采购商通过手机号登录
	 * @param phone
	 * @return
	 */
	public AjaxJson merchantLogin(String phone);
	
	/**
	 * 筛选出入参中已关店或者已删除的商家用户ID
	 * @author hyj
	 * @param merchantUserIds 若干个商家用户ID(多个ID使用","分隔)
	 * @return
	 */
	public abstract AjaxJson userIdsWhitchMerchantNotAvailable(String merchantUserIds);
	
}
