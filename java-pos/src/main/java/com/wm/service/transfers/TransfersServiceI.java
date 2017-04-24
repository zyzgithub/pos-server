package com.wm.service.transfers;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.transfers.TransfersEntity;
import com.wm.entity.user.WUserEntity;

public interface TransfersServiceI extends CommonService{

	/**
	 * 查询快递员代付金额记录
	 * @param userId
	 * @return
	 */
	public List<TransfersEntity> findTransLog(Integer userId);

	public void transfersLog(WUserEntity from, WUserEntity to, double money, String orderNum) throws Exception;
	
	public void saveTransfers(WUserEntity from, WUserEntity to, double money, String orderNum);
}
