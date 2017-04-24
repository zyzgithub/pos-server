package com.wm.service.category;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.category.CategoryEntity;

public interface CategoryServiceI extends CommonService{

	List<CategoryEntity> findByZone(String zone);
	
	/**
	 * 根据zone的值获取name
	 * @return
	 */
	public List<Map<String, Object>> getCategoryGroup(String zone);

}
