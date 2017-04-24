package com.wm.service.impl.merchantinfo;


import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.merchantinfo.MerchantDeductionEntity;
import com.wm.service.impl.comment.CommentServiceImpl;
import com.wm.service.merchantinfo.MerchantDeductionServiceI;

@Service("merchantDeductionService")
@Transactional
public class MerchantDeductionServiceImpl extends CommentServiceImpl implements MerchantDeductionServiceI {

	@Override
	public MerchantDeductionEntity getMerchantDeduction(Integer merchantId, Integer type) {
		List<MerchantDeductionEntity> list = this.findHql("from MerchantDeductionEntity where merchantId=? and type=?", merchantId, type);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}

}
