package com.dianba.pos.passport.vo;

/**
 * Created by zhangyong on 2017/5/28.
 */
public class RegisterVo {

    private  Long accountId;

    /**
     * 登录用户名
     **/
    private String name;

    /****登录密码**/
    private String password;

    /***展示名字，默认name**/
    private String showName;

    /**
     * 身份证名字
     **/
    private String realName;

    /***身份证号码**/
    private String idNumber;

    /***手机号码，可以作为登录账号必填**/
    private String phoneNumber;

    /***1 男 0 女**/
    private Integer sex;


    /**
     * 注册类型 COURIER(1, "courier", "快递员", true),
     * POS(2, "pos", "POS", true),
     * CONSUMER(3, "consumer", "消费者", false),
     * MERCHANT(4, "merchant", "商家", true),
     * WAREHOUSE(5, "warehouse", "仓管", true),
     * PURCHASE(6, "purchase", "采购", true),
     * SUPPLIER(7, "supplier", "供应商", true),
     * BACKSTAGE(8, "backstage", "后台", true);
     **/
    private int type = 8;


    /**
     * 默认1
     **/
    private Long fromChannel = 1L;

    /***
     *
     * DeviceTypeEnum
     * DEVICE_TYPE_UNKNOW(-1, "未知设备"),
     DEVICE_TYPE_ANDROID(1, "Android"),
     DEVICE_TYPE_IOS(2, "IOS"),
     DEVICE_TYPE_H5(3, "H5"),
     DEVICE_TYPE_HTML(4, "HTML");
     */
    private Integer deviceType;


    private String smsCode;
    /**
     * String 设备名字，非必填参数；默认为：Android。
     */
    private String deviceName;

    /**
     * int 当前使用的版本号，必填参数；注意：版本号区分两种情况，1、用于展示的版本号 2、内部使用的版本号；本处指内部使用的版本号
     */
    private Integer versionIndex;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(Integer versionIndex) {
        this.versionIndex = versionIndex;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setType(int type) {
        this.type = type;
    }
}
