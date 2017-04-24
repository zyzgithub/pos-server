package com.wm.dao.category;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.entity.category.CategoryEntity;

public interface CategoryDao extends IGenericBaseCommonDao{

	CategoryEntity findByZoneAndName(String zone, String name);

	List<CategoryEntity> findByZone(String zone);

}
