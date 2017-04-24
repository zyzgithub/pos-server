package com.wm.service.impl.orderthirdbatch;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wm.service.orderthirdbatch.OrderThirdBatchServiceI;

@Service("orderThirdBatchService")
@Transactional
public class OrderThirdBatchServiceImpl extends CommonServiceImpl implements OrderThirdBatchServiceI {
	
}