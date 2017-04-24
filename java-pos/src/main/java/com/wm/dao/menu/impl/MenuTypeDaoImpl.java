package com.wm.dao.menu.impl;

import java.util.List;

import org.hibernate.Query;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.menu.MenuTypeDao;
import com.wm.entity.menu.MenutypeEntity;

@Repository("menuTypeDao")
@SuppressWarnings("unchecked")
public class MenuTypeDaoImpl extends GenericBaseCommonDao<MenutypeEntity, Integer> implements MenuTypeDao{

	@Override
	public List<MenutypeEntity> queryByMerchantId(Integer merchantId) {
		// TODO Auto-generated method stub
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from MenutypeEntity mt  ");
		hql.append(" where mt.id in (");
		hql.append(" 		select DISTINCT mt.id ");
		hql.append("		from MenutypeEntity mt, MenuEntity me");
		hql.append("		where mt.id = me.menuType and mt.merchantId = ? and me.display='Y'");
		hql.append("	)");
		hql.append(" order by mt.sortNum asc");
		
		Query query = createHqlQuery(hql.toString(), merchantId);
		return query.list();
	}

}
