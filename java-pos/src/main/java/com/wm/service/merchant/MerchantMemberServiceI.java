package com.wm.service.merchant;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

public interface MerchantMemberServiceI extends CommonService {
	
	/**
	 * 获得商家会员信息(总人数和总余额)
	 * @param merchantId 商家id
	 * @return
	 */
	public Map<String, Object> getMemberInfo(Integer merchantId);
	
	/**
	 * 获得商家会员规则
	 * @param merchantId 商家id
	 * @return
	 */
	public Map<String, Object> getMemberRule(Integer merchantId);
	
	/**
	 * 创建或更新商家规则
	 * @param merchantId 商家id
	 * @param userId 操作人员的用户id
	 * @param money 会员首次最低充值金额(单位：元)
	 * @param discount 商家默认会员折扣(单位：%)
	 */
	public void createOrUpdateMemberRule(Integer merchantId, Integer userId, Double money, Integer discount);
	
	/**
	 * 获得商家会员列表
	 * @param merchant_id 商家id
	 * @param page 起始分页
	 * @param rows 分页行数
	 * @return
	 */
	public List<Map<String, Object>> getMemberList(Integer merchantId, Integer page, Integer rows);
	
	/**
	 * 获得商家会员充值记录
	 * @param merchantId 商家id
	 * @param page 起始分页
	 * @param rows 分页行数
	 * @return
	 */
	public List<Map<String, Object>> getMemberChargeMoneyRecords(Integer merchantId, Integer page, Integer rows);
}
