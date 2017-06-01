package com.dianba.pos.item.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhangyong on 2017/5/24.
 *
 */
@Entity
@Table(name = "life_item.item_type")
public class LifeItemType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "sort")
    private Integer sort;
    @Column(name = "top")
    private Integer top;
    @Column(name = "icon")
    private String icon;
    @Column(name = "image")
    private String image;
    @Column(name = "ascription_type")
    private Integer ascriptionType;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAscriptionType() {
        return ascriptionType;
    }

    public void setAscriptionType(Integer ascriptionType) {
        this.ascriptionType = ascriptionType;
    }
}

