package com.dianba.pos.item.po;

//用户信息表
public class User {
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String gender;

    private String mobile;

    private String photourl;

    private Double money;

    private Integer score;

    private String userType;

    private String sns;

    private String ip;

    private Integer loginTime;

    private Integer createTime;

    private String payPassword;

    private String openid;

    private String unionid;

    private String unionidRemark;

    private String mobileRemark;

    private String openidRemark;

    private Boolean isDelete;

    private Integer firstOrderTime;

    private Byte userState;

    private Integer age;

    private Integer buyCount;

    private Long updateTime;

    private Byte memStatus;

    private Integer creator;

    private String aliacno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl == null ? null : photourl.trim();
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getSns() {
        return sns;
    }

    public void setSns(String sns) {
        this.sns = sns == null ? null : sns.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Integer loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    public String getUnionidRemark() {
        return unionidRemark;
    }

    public void setUnionidRemark(String unionidRemark) {
        this.unionidRemark = unionidRemark == null ? null : unionidRemark.trim();
    }

    public String getMobileRemark() {
        return mobileRemark;
    }

    public void setMobileRemark(String mobileRemark) {
        this.mobileRemark = mobileRemark == null ? null : mobileRemark.trim();
    }

    public String getOpenidRemark() {
        return openidRemark;
    }

    public void setOpenidRemark(String openidRemark) {
        this.openidRemark = openidRemark == null ? null : openidRemark.trim();
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getFirstOrderTime() {
        return firstOrderTime;
    }

    public void setFirstOrderTime(Integer firstOrderTime) {
        this.firstOrderTime = firstOrderTime;
    }

    public Byte getUserState() {
        return userState;
    }

    public void setUserState(Byte userState) {
        this.userState = userState;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getMemStatus() {
        return memStatus;
    }

    public void setMemStatus(Byte memStatus) {
        this.memStatus = memStatus;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getAliacno() {
        return aliacno;
    }

    public void setAliacno(String aliacno) {
        this.aliacno = aliacno == null ? null : aliacno.trim();
    }
}
