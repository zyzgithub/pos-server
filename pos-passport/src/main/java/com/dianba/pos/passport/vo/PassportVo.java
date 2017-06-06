package com.dianba.pos.passport.vo;

public class PassportVo {

    /**String 登录用户名(自定义用户名或手机号或其他已绑定的帐号)，必填参数***/
    private String username;

    /** String 登录密码，必填参数**/
    private String password;

    /** int 设备类型，必填参数；参考：DeviceTypeEnum
     * 1 - 设备类型 -- Android
     * 3 - 设备类型 -- H5
     * 4 - 设备类型 -- HTML
     * 2 - 设备类型 -- IOS
     * -1 - 设备类型 -- 未知
     * **/
    private Integer deviceType;

    /**客户端类型，必填参数
     *1、快递员 2、POS 3、消费者 4、商家 5、仓管 6、采购 6、采购 8、后台
     * **/
    private Integer clientType;

    /**int 当前的版本标志，必填参数；(内部版本号，一般递增，初始为1，此部分的信息主要由前端决定)**/
    private Integer versionIndex;

    private Integer fromChannel;

    private Integer accountType;

    private String accountTypeName;

    public Integer getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(Integer fromChannel) {
        this.fromChannel = fromChannel;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(Integer versionIndex) {
        this.versionIndex = versionIndex;
    }


}
