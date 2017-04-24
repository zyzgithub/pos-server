package com.wm.dao.menu;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.menu.MenutypeEntity;

public interface MenuTypeDao extends IGenericBaseCommonDao{

	List<MenutypeEntity> queryByMerchantId(Integer merchantId);

}
