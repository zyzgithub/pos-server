package com.wm.dao.mongo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.mongodb.WriteResult;
import com.wm.controller.open_api.ValidUtil;

@Repository
public abstract class BaseRepositoryImpl implements BaseRepository {
    private static Logger LOG = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    @Autowired protected MongoTemplate mongoTemplate;
    
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    /** ++++++++++++++++++++ 查询 ++++++++++++++++++++ */
    /**
     * 分页拼接query
     * @param page 当前页数
     * @param pageSize 每页条数
     * @return
     */
    public Query limitQuery(Integer page, Integer pageSize) {
        Query query = new Query();
        if (null == page || null == pageSize) return query;
        int skip = page == 0 ? page * pageSize : (page - 1) * pageSize;
        int limit = page == 0 ? 1 * pageSize : page * pageSize;
        return query.skip(skip).limit(limit);
    }

    /**
     * 自动拼接查询条件
     * @param query 查询语句对象
     * @param t 对象
     * @return
     * @throws Exception
     */
    public <T> Query ConditionQuery(Query query, T t) throws Exception {
        if (null != t) {
            Object idValue = null;
            try {
                idValue = t.getClass().getField("id").get(t);
            } catch (Exception e) { LOG.warn("{}类没有id字段", t.getClass().getName()); }
            if (!ValidUtil.anyEmpty(idValue)) query.addCriteria(Criteria.where("id").is(idValue));
            else {
                for (Field field : t.getClass().getFields()) {
                    if (null == field || Modifier.isStatic(field.getModifiers())) continue;

                    if ("id".equals(field.getName())) continue;
                    else {
                        Object fieldValue = field.get(t);
                        if (null != fieldValue) query.addCriteria(Criteria.where(field.getName()).is(fieldValue));
                    }
                }
            }
        }
        return query;
    }

    /**
     * 自动拼接查询条件
     * @param query 查询语句对象
     * @param t 对象
     * @return
     * @throws Exception
     */
    public <T> Query ConditionSearchQuery(Query query, T t) throws Exception {
        if (null != t) {
            Field idField = t.getClass().getField("id");
            Object idValue = idField.get(t);
            if (!ValidUtil.anyEmpty(idValue)) query.addCriteria(Criteria.where("id").is(idValue));
            else {
                for (Field field : t.getClass().getFields()) {
                    if (null == field || Modifier.isStatic(field.getModifiers())) continue;
                    if ("id".equals(field.getName())) continue;
                    else {
                        Object fieldValue = field.get(t);
                        if (null != fieldValue) {
                            if (ValidUtil.isNumber(fieldValue))
                                query.addCriteria(Criteria.where("$where").is("/" + fieldValue + ".*/.test(this." + field.getName() + ")"));
                            else query.addCriteria(Criteria.where(field.getName()).regex(".*" + fieldValue + ".*"));
                        }
                    }
                }
            }
        }
        return query;
    }

    /**
     * 查找单条数据
     * @param t 对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T findOne(T t) {
        try {
            if (null != t) {
                Query query = ConditionQuery(new Query(), t);
                T result = (T) mongoTemplate.findOne(query, t.getClass());
                if (ValidUtil.anyEmpty(result)) {
                    if (LOG.isInfoEnabled()) LOG.info("【findOne】 find " + t.getClass() + " empty");
                    return null;
                }
                return result;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分页查询
     * @param page 当前页数
     * @param pageSize 每页条数
     * @param t 对象
     * @return
     */
    public <T> List<T> find(Integer page, Integer pageSize, T t) {
        Query query = this.limitQuery(page, pageSize);
        try {
            return find(page, pageSize, t, ConditionQuery(query, t));
        }
        catch (Exception e) {
            if (LOG.isWarnEnabled())
                LOG.warn("【find】 find List failed, param:[page:{},pageSize:{},{}:{}] \n query:[{}] \n exception:{} ", page, pageSize,
                                t.getClass().getName(), null != t ? JSON.toJSONString(t) : " empty ", query.toString(), e.toString());
            return null;
        }
    }

    @Override
    public <T> int count(T t) {
        try {
            return (int) mongoTemplate.count(ConditionQuery(new Query(), t), t.getClass());
        }
        catch (Exception e) {
            if (LOG.isWarnEnabled()) LOG.warn("【find】 find List failed, param:[{}:{}] \n exception:{} ", t.getClass().getName(),
                            null != t ? JSON.toJSONString(t) : " empty ", e.toString());
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> find(Integer page, Integer pageSize, T t, Query query) {
        try {
            if (LOG.isInfoEnabled()) LOG.info("【query】 find List, param:[page:{},pageSize:{},{}:{}] \n query:[{}] ", page, pageSize,
                            null != t ? t.getClass().getName() : " null ", null != t ? JSON.toJSONString(t) : " empty ", query.toString());
            return (List<T>) mongoTemplate.find(query, t.getClass());
        }
        catch (Exception e) {
            if (LOG.isWarnEnabled())
                LOG.warn("【query】 find List failed, param:[page:{},pageSize:{},{}:{}] \n query:[{}] \n exception:{} ", page, pageSize,
                                t.getClass().getName(), null != t ? JSON.toJSONString(t) : " empty ", query.toString(), e.toString());
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> find(Integer page, Integer pageSize, T t, boolean showLog) {
        Query query = this.limitQuery(page, pageSize);
        try {
            if (LOG.isInfoEnabled() && showLog) LOG.info("【query】 find List, param:[page:{},pageSize:{},{}:{}] \n query:[{}] ", page, pageSize,
                            null != t ? t.getClass().getName() : " null ", null != t ? JSON.toJSONString(t) : " empty ", query.toString());
            return (List<T>) mongoTemplate.find(ConditionQuery(query, t), t.getClass());
        }
        catch (Exception e) {
            if (LOG.isWarnEnabled())
                LOG.warn("【query】 find List failed, param:[page:{},pageSize:{},{}:{}] \n query:[{}] \n exception:{} ", page, pageSize,
                                t.getClass().getName(), null != t ? JSON.toJSONString(t) : " empty ", query.toString(), e.toString());
            return null;
        }
    }

    @Override
    public <T> List<T> findAll(T t) {
        return find(null, null, t);
    }

    @SuppressWarnings("unchecked")
    public <T> T searchOne(T t) {
        try {
            if (null != t) {
                Query query = ConditionSearchQuery(new Query(), t);
                T result = (T) mongoTemplate.findOne(query, t.getClass());
                if (ValidUtil.anyEmpty(result)) {
                    if (LOG.isInfoEnabled()) LOG.info("【findOne】 find " + t.getClass() + " empty");
                    return null;
                }
                return result;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> search(Integer page, Integer pageSize, T t) {
        Query query = this.limitQuery(page, pageSize);
        try {
            return find(page, pageSize, t, ConditionSearchQuery(query, t));
        }
        catch (Exception e) {
            if (LOG.isWarnEnabled())
                LOG.warn("【search】 find List failed, param:[page:{},pageSize:{},{}:{}] \n query:[{}] \n exception:{} ", page, pageSize,
                                t.getClass().getName(), null != t ? JSON.toJSONString(t) : " empty ", query.toString(), e.toString());
            return null;
        }
    }

    /** ++++++++++++++++++++ 修改 ++++++++++++++++++++ */
    /**
     * 自动拼接更新更新语句
     * @param update 更新语句对象
     * @param t 对象
     * @return
     * @throws Exception
     */
    public <T> Update ConditionUpdate(Update update, T t) throws Exception {
        if (null != t) {
            for (Field field : t.getClass().getFields()) {
                if (null == field || Modifier.isStatic(field.getModifiers())) continue;

                if ("id".equals(field.getName())) continue;
                else {
                    Object fieldValue = field.get(t);
                    if (null != fieldValue) update.set(field.getName(), fieldValue);
                }
            }
        }
        return update;
    }

    /**
     * 根据id更新数据
     * @param id 主键
     * @param t 更新的字段（null 不更新）
     * @return
     * @throws Exception
     */
    @Override
    public <T> int updateFirstById(String id, T t) throws Exception {
        Update update = ConditionUpdate(new Update(), t);
        WriteResult ret = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), update, t.getClass());
        return ret.getN();
    }

    /**
     * 更新对象 多个
     * @param q 查询内容=号
     * @param t 更新后内容=号
     */
    @Override
    public <T> WriteResult updateMulti(T q, T t) throws Exception {
        Query query = ConditionQuery(new Query(), q);
        Update update = ConditionUpdate(new Update(), t);
        WriteResult ret = mongoTemplate.updateMulti(query, update, t.getClass());
        return ret;
    }

    /** ++++++++++++++++++++ 删除 ++++++++++++++++++++ */
    /**
     * 通过查询条件删除对象
     * @param t 查询条件对象
     * @return
     */
    public <T> void remove(T t) {
        try {
            Query query = ConditionQuery(new Query(), t);
            mongoTemplate.remove(query, t.getClass());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ++++++++++++++++++++ 添加 ++++++++++++++++++++ */
    /**
     * 保存对象
     * @param t
     */
    public <T> void save(T t) {
        try {
            mongoTemplate.save(t);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增对象
     * @param t
     */
    public <T> void insert(T t) {
        try {
            mongoTemplate.insert(t);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insertAll(List<T> t) {
        try {
            mongoTemplate.insertAll(t);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
