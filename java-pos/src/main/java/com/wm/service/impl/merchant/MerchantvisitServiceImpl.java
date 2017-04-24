package com.wm.service.impl.merchant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.merchant.MerchantvisitServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("merchantvisitService")
@Transactional
public class MerchantvisitServiceImpl extends CommonServiceImpl implements MerchantvisitServiceI {
	
}