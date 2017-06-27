package com.dianba.pos.common.util;
/**
 * Created by zhangyong on 2017/6/26.
 *  0、表示未发货 2、表示发货中 3、发货失败 4、表示配送中 8、表示已送达
 */
public enum  OrderDeliverStatusEnum {
    ORDER_DELIVER_STATUS_DEFAULT(0, "未发货"),
    ORDER_DELIVER_STATUS_WAIT(2, "发货中"),
    ORDER_DELIVER_STATUS_ERROR(3, "发货失败"),
    ORDER_DELIVER_STATUS_SHIPPING(4, "配送中"),
    ORDER_DELIVER_STATUS_FAIL(8, "已送达");
    private int key;
    private String value;

    OrderDeliverStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
