package com.dianba.pos.passport.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Entity
@Table(name = "life_passport.passport")
public class Passport implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**登录默认账号**/
    @Column(name = "default_name")
    private String defaultName;

    /**登录默认密码**/
    @Column(name = "password")
    private String password;

    /**登录展示名字**/
    @Column(name = "show_name")
    private String showName;

    /**用户真实姓名**/
    @Column(name = "real_name")
    private String realName;

    /**用户身份证号码**/
    @Column(name = "id_number")
    private String idNumber;

    /**用户手机号码**/
    @Column(name = "phone_number")
    private String phoneNumber;

    /**用户性别(0:女性 1:男性；默认值:0)**/
    @Column(name = "sex")
    private Integer sex;

    /**用户类型(普通用户，VIP用户等，由逻辑设定**/
    @Column(name = "type")
    private Integer type;

    /**当前的状态(如：正常、禁言、禁登、黑名单等)**/
    @Column(name = "status")
    private Integer status;

    /**注册时间**/
    @Column(name = "create_time")
    private Date createTime;

    /**册的渠道(如：官网、第三方渠道等；用于分析推广方式)**/
    @Column(name="from_channel")
    private Long fromChannel;

    /**设备类型(分析用户群，可针对不同用户开展不同的活动)**/
    @Column(name = "device_type")
    private Integer deviceType;

    /**设备名称**/
    @Column(name = "device_name")
    private String deviceName;

    /**最后的访问令牌(每次登录进行重置)**/
    @Column(name = "access_token")
    private String accessToken;

    private String cashierPhoto;

    private String accountTypeName;

    private Integer accountType;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(Long fromChannel) {
        this.fromChannel = fromChannel;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCashierPhoto() {
        return cashierPhoto;
    }

    public void setCashierPhoto(String cashierPhoto) {
        this.cashierPhoto = cashierPhoto;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
