package com.dianba.pos.menu.vo;

/**
 * Created by zhangyong on 2017/5/27.
 */
public class PosTypeVo {

    private Long id;

    private String title;

    private Integer type_count;


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

    public Integer getType_count() {
        return type_count;
    }

    public void setType_count(Integer type_count) {
        this.type_count = type_count;
    }
}
