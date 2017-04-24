package com.wm.service.impl.bank;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.bank.BankEntity;
import com.wm.service.bank.BankServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("bankService")
@Transactional
public class BankServiceImpl extends CommonServiceImpl implements BankServiceI {

	@Override
	public List<BankEntity> getBank() {
		return getList(BankEntity.class);
	}
	
}