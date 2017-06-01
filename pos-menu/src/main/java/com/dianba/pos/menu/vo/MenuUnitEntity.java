package com.dianba.pos.menu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wuyong
 * @version V1.0
 * @Title: Entity
 * @Description: menu_type
 * @date 2015-01-07 09:59:20
 */
@Entity
@Table(name = "menu_unit", schema = "")
@SuppressWarnings("serial")
public class MenuUnitEntity implements java.io.Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 排序序号，从小到大排序
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sort;
    /**
     * 商家ID
     */
    private Integer isDelete;
    /**
     * createTime
     */
    private Date createTime;

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 19, scale = 0)
    public Integer getId() {
        return this.id;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 方法: 取得String
     *
     * @return: String  分类名
     */
    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 设置String
     *
     * @param: String  分类名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  排序序号，从小到大排序
     */
    @Column(name = "SORT", nullable = true, precision = 10, scale = 0)
    public Integer getSort() {
        return this.sort;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  排序序号，从小到大排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  createTime
     */
    @Column(name = "CREATE_TIME", nullable = true, precision = 10, scale = 0)
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  createTime
     */
    @Column(name = "IS_DELETE", nullable = true, precision = 10, scale = 0)
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "MenuunitEntity [id=" + id + ", name=" + name + ", sort=" + sort
                + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }

}
