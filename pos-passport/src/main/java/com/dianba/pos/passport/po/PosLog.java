package com.dianba.pos.passport.po;

import javax.persistence.*;

@Entity
@Table(name="pos_log")
public class PosLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ypl_id")
    private Integer yplId;

    private String title;

    private String content;


    public Integer getYplId() {
        return yplId;
    }

    public void setYplId(Integer yplId) {
        this.yplId = yplId;
    }

    @Column(name="title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
