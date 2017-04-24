package com.wm.service.impl.couriergroupmember;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.couriergroupmember.CourierGroupMemberServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("courierGroupMemberService")
@Transactional
public class CourierGroupMemberServiceImpl extends CommonServiceImpl implements CourierGroupMemberServiceI {
	
}