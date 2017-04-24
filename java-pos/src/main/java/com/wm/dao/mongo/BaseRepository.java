package com.wm.dao.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public interface BaseRepository {
    public MongoTemplate getMongoTemplate();
    
    /** ++++++++++++++++++++ 查询 ++++++++++++++++++++ */
    /**
     * 分页拼接query
     * @param page 当前页数
     * @param pageSize 每页条数
     * @return
     */
    public Query limitQuery(Integer page, Integer pageSize);

    /**
     * 自动拼接查询条件
     * @param query 查询语句对象
     * @param t 对象
     * @return
     * @throws Exception
     */
    public <T> Query ConditionQuery(Query query, T t) throws Exception;

    /**
     * 查找单条数据
     * @param t 对象
     * @return
     */
    public <T> T findOne(T t);

    /**
     * 分页查询
     * @param page 当前页数
     * @param pageSize 每页条数
     * @param t 对象
     * @return
     */
    public <T> List<T> find(Integer page, Integer pageSize, T t);
    
    public <T> List<T> find(Integer page, Integer pageSize, T t, boolean showLog);
    
    /**
     * 查询总条数
     * @param t
     * @return
     */
    public <T> int count(T t);

    public <T> List<T> findAll(T t);

    public <T> T searchOne(T t);

    public <T> List<T> search(Integer page, Integer pageSize, T t);

    /** ++++++++++++++++++++ 修改 ++++++++++++++++++++ */
    /**
     * 自动拼接更新更新语句
     * @param update 更新语句对象
     * @param t 对象
     * @return
     * @throws Exception
     */
    public <T> Update ConditionUpdate(Update update, T t) throws Exception;
    
    /**
     * 根据id更新数据
     * @param id 主键
     * @param t 更新的字段（null 不更新）
     * @return 
     * @throws Exception
     */
    public <T> int updateFirstById(String id, T t) throws Exception;
    
    /**
     * 更新对象 多个
     * @param q 查询内容=号
     * @param t 更新后内容=号
     */
    public <T> WriteResult updateMulti(T q, T t) throws Exception;

    /** ++++++++++++++++++++ 删除 ++++++++++++++++++++ */
    /**
     * 通过查询条件删除对象
     * @param t 查询条件对象
     * @return
     */
    public <T> void remove(T t);
    
    /** ++++++++++++++++++++ 添加 ++++++++++++++++++++ */
    /**
     * 保存对象
     * @param t
     */
    public <T> void save(T t);
    
    /**
     * 新增对象
     * @param t
     */
    public <T> void insert(T t);
    
    /**
     * 新增多个对象
     * @param t
     * @return
     */
    public <T> void insertAll(List<T> t);
}
