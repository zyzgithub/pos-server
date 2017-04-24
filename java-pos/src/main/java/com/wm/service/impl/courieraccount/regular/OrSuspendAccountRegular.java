package com.wm.service.impl.courieraccount.regular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * 两个封号规则（规则1、规则2），只需要满足其中一个条件即可以封号
 *
 */
public class OrSuspendAccountRegular extends AbstractSuspendAccountRegular {

	private final AbstractSuspendAccountRegular regular1;
	private final AbstractSuspendAccountRegular regular2;
	
	public OrSuspendAccountRegular(AbstractSuspendAccountRegular regular1, AbstractSuspendAccountRegular regular2){
		this.regular1 = regular1;
		this.regular2 = regular2;
	}
	
	@Override
	public List<SuspendAccount> findSuspendAccountsByRegular() {
		List<SuspendAccount> suspendAccountsByRegular1 = regular1.findSuspendAccountsByRegular();
		List<SuspendAccount> suspendAccountsByRegular2 = regular2.findSuspendAccountsByRegular();
		
		//根据规则1得到的封号列表为空
		if(CollectionUtils.isEmpty(suspendAccountsByRegular1)){
			return suspendAccountsByRegular2;
		}
		
		//根据规则2得到的封号列表为空
		if(CollectionUtils.isEmpty(suspendAccountsByRegular2)){
			return suspendAccountsByRegular1;
		}
		
		return union(suspendAccountsByRegular1, suspendAccountsByRegular2);
	}
	
	/**
	 * 求两个集合的并集
	 * @param suspendAccountsByRegular1
	 * @param suspendAccountsByRegular2
	 * @return
	 */
	public List<SuspendAccount> union(List<SuspendAccount> suspendAccountsByRegular1, List<SuspendAccount> suspendAccountsByRegular2){
		
		Set<SuspendAccount> suspendAccounts = new HashSet<SuspendAccount>();
		for(SuspendAccount suspendAccountByReglar1: suspendAccountsByRegular1){
			for(SuspendAccount suspendAccountByReglar2: suspendAccountsByRegular2){
				if(suspendAccountByReglar2.getCourierId().equals(suspendAccountByReglar1.getCourierId())){
					String reason = suspendAccountByReglar1.getSuspendReason() + "," + suspendAccountByReglar2.getSuspendReason();
					suspendAccountByReglar1.setSuspendReason(reason);
					suspendAccountByReglar2.setSuspendReason(reason);
				}
			}
		}
		suspendAccounts.addAll(suspendAccountsByRegular1);
		suspendAccounts.addAll(suspendAccountsByRegular2);
		
		List<SuspendAccount> list = new ArrayList<SuspendAccount>();
		list.addAll(suspendAccounts);
		return list;
	}

}
