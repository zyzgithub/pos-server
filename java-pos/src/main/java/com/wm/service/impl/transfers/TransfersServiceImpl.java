package com.wm.service.impl.transfers;

import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.transfers.TransfersEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.transfers.TransfersServiceI;

@Service("transfersService")
@Transactional
public class TransfersServiceImpl extends CommonServiceImpl implements TransfersServiceI {
	
	@Autowired
	private FlowServiceI flowService;
	
	@Override
	public void transfersLog(WUserEntity from, WUserEntity to, double money, String orderNum) throws Exception {
		TransfersEntity transfers = new TransfersEntity();
		
		transfers.setMoney(money);
		transfers.setPayUser(from);
		transfers.setTransferUser(to);
		transfers.setOrderNum(orderNum);
		this.save(transfers);
		
		flowService.courierPayOrderFlowCreate(transfers);
	}
	
	@Override
	public void saveTransfers(WUserEntity from, WUserEntity to, double money, String orderNum){
		TransfersEntity transfers = new TransfersEntity();
		transfers.setMoney(money);
		transfers.setPayUser(from);
		transfers.setTransferUser(to);
		transfers.setOrderNum(orderNum);
		this.save(transfers);
	}

	@Override
	public List<TransfersEntity> findTransLog(Integer userId) {
		return this.findHql("select t from TransfersEntity t left join t.payUser u where u.id=? ", new Object[]{userId});
	}
	
}