package com.wm.service.bank;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.bank.BankEntity;

public interface BankServiceI extends CommonService{
	
	/**
	 * 获取银行信息
	 * @return
	 */
	public List<BankEntity> getBank();

}
