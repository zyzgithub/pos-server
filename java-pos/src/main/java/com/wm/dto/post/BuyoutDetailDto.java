package com.wm.dto.post;

/**
 * Created by mjorcen on 16/8/8.
 */
public class BuyoutDetailDto {
    private String payName;// 一次性购买
    private String money;// 2980
    private String topic;// 限时优惠/推荐
    private Integer itemId;// 1: 硬件,2:软件
    private String unit; // 单位 : 元 , 元/月
    private Integer payType;// 支付类型.

    private String image;// 图片// .

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "BuyoutDetailDto{" +
                "payName='" + payName + '\'' +
                ", money='" + money + '\'' +
                ", topic='" + topic + '\'' +
                ", itemId=" + itemId +
                ", unit='" + unit + '\'' +
                ", payType=" + payType +
                ", image='" + image + '\'' +
                '}';
    }
}
