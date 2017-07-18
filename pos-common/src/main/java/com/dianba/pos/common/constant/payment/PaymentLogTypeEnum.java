package com.dianba.pos.common.constant.payment;

public enum PaymentLogTypeEnum {

    PAYMENT_FAIL(0, "未支付，失败原因："),
    COMPLETE_ORDER_SUCCESS(1, "已支付,订单更新完毕。"),
    COMPLETE_ORDER_FAIL(2, "已支付，订单更新异常：");

    private Integer type;
    private String title;

    PaymentLogTypeEnum(Integer type, String title) {
        this.type = type;
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
