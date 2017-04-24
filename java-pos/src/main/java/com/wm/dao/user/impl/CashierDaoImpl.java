package com.wm.dao.user.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.user.CashierDao;
import com.wm.entity.user.WUserEntity;

@Repository("cashierDao")
public class CashierDaoImpl extends GenericBaseCommonDao<WUserEntity, Integer> 
	implements CashierDao{

}
