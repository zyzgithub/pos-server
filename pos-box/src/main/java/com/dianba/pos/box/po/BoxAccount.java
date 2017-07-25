package com.dianba.pos.box.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyong on 2017/7/17.
 */
@Entity
@Table(name = "life_pos.box_account")
public class BoxAccount implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "create_time")
    private Date createTime=new Date();

    @Column(name = "state")
    private Integer state=0;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
