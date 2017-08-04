package com.dianba.pos.extended.po;

import org.springframework.stereotype.Controller;

import javax.persistence.*;

/**
 * Created by zhangyong on 2017/8/4.
 */
@Entity
@Table(name = "life_pos.pos_extended_back_log")
public class PosExtendedBackLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    private String content;

    @Column(name = "type")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
