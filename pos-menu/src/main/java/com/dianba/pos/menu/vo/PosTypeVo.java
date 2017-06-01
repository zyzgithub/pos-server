package com.dianba.pos.menu.vo;

/**
 * Created by zhangyong on 2017/5/27.
 */
public class PosTypeVo {

    private Long id;

    private String title;

    private Integer typeCount;


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

    public Integer getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(Integer typeCount) {
        this.typeCount = typeCount;
    }
}
