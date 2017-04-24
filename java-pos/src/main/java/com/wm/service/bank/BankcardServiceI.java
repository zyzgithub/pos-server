package com.wm.service.bank;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.takeout.vo.BankCardVo;

public interface BankcardServiceI extends CommonService{
	
	List<BankCardVo> queryByDefault(int userId,String defaultStr,int page,int rows);
	
	/**
	 * 获取用户默认的银行卡
	 * @param userId
	 * @return
	 */
	BankCardVo getDefaultBankCard(int userId);
	
}
