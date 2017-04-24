package com.wm.service.impl.category;

import com.wm.dao.category.CategoryDao;
import com.wm.entity.category.CategoryEntity;
import com.wm.service.category.CategoryServiceI;
import com.wm.util.AliOcs;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("categoryService")
@Transactional
public class CategoryServiceImpl extends CommonServiceImpl implements CategoryServiceI {

    private final static int EXPIRED_TIME = 60 * 60 * 24;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<CategoryEntity> findByZone(String zone) {
        // TODO Auto-generated method stub
        return categoryDao.findByZone(zone);
    }

    /**
     * 根据zone的值, 获取name
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getCategoryGroup(String zone) {

        String key = "category_search_" + zone;
        Object obj = null;
//        Object obj = AliOcs.getObject(key); 关闭缓存测试

        //如果缓存中已经有了
        if (obj != null) {
            return (List<Map<String, Object>>) obj;
        }
        //缓存中没有
        else {
            StringBuilder query = new StringBuilder();
            query.append(" select `name`, id,czone");
            query.append(" from category");
            query.append(" where zone = ?");
            query.append(" order by id");
            List<Map<String, Object>> list = findForJdbc(query.toString(), zone);
            AliOcs.set(key, list, EXPIRED_TIME);
            return list;
        }
    }

}