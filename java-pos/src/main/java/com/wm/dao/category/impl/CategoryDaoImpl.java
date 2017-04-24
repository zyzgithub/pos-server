package com.wm.dao.category.impl;

import java.util.List;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.category.CategoryDao;
import com.wm.entity.category.CategoryEntity;

@Repository("categoryDao")
@SuppressWarnings("unchecked")
public class CategoryDaoImpl extends GenericBaseCommonDao<CategoryEntity, Integer> implements CategoryDao{

	@Override
	public CategoryEntity findByZoneAndName(String zone, String name) {
		// TODO Auto-generated method stub
		String hql = "from CategoryEntity cate where cate.zone = ? and cate.name = ?";
		Query query = createHqlQuery(hql, zone, name);
		Object obj = query.uniqueResult();
		if(null != obj)
			return (CategoryEntity)obj;
		return null;
	}

	@Override
	public List<CategoryEntity> findByZone(String zone) {
		// TODO Auto-generated method stub
		String sql =  "from CategoryEntity cate where cate.zone = ?";
		Query query = createHqlQuery(sql, zone);
		return query.list();
	}
	
}
