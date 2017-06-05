package com.dianba.pos.passport.po;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangyong on 2017/6/5.
 */
@Entity
@Table(name = "life_passport.passport_alias")
public class LifePassportAlias {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "alias_name")
    private String aliasName;


    @Column(name = "create_time")
    private Date crateTime;

    @Column(name = "passport_id")
    private String passportId;


    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
