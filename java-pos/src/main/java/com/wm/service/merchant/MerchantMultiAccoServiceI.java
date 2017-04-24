package com.wm.service.merchant;

import java.util.List;
import java.util.Map;

import com.wm.dto.merchant.MerchantCashflowDto;

public interface MerchantMultiAccoServiceI {
	/**
	 * 获取商家总店和分店列表
	 * @param merchantId
	 * @return
	 */
	public List<Map<String, Object>> getALLStoresPageList(Integer merchantId, Integer page, Integer rows);
	
	/**
	 * 获取商家分店列表(分页)
	 * @param merchantId
	 * @return
	 */
	public List<Map<String, Object>> getBranchStoresPageList(Integer merchantId, Integer page, Integer rows);
	
	/**
	 * 得到分店总数，包括余额为0的(不分页)
	 * @param merchantId
	 * @return
	 */
	public Long getBranchStoresCount(Integer merchantId);
	
	/**
	 * 获取商家总账记录,包括余额为0的
	 * @param merchantId
	 * @return
	 */                       
	List<Map<String, Object>> getALLStoresAccountPageList(Integer merchantId, Integer page, Integer rows);
	
	/**
	 * 极光推送文本消息给商家
	 * @param userId
	 * @param title
	 * @param content
	 * @return
	 */
	public void jpushMsgToMerchant(Integer userId, String title, String content);

	/**
	 * 获取商家账户转入记录
	 * @param merchantId
	 * @return
	 */
	List<Map<String, Object>> getMerchantCashflowPageList(Integer merchantId, Integer page, Integer rows);

	/**
	 * 分店余额转入到主店余额
	 * @param dto
	 * @throws Exception 
	 */
	public void addMerchantCashflow(MerchantCashflowDto dto);


}
