package com.dianba.pos.passport.po;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangyong on 2017/6/5.
 */

@Entity
@Table(name = "life_passport.passport_properties")
public class LifePassportProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "k")
    private String k;


    @Column(name = "v")
    private String v;

    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "type")
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
