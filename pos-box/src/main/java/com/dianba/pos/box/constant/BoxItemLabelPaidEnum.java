package com.dianba.pos.box.constant;

import java.util.HashMap;
import java.util.Map;

public enum BoxItemLabelPaidEnum {
    NOT_PAID(0, "未支付"),
    PAID(1, "已支付"),
    HALF_PAID(2, "包含未支付商品");

    private Integer key;
    private String value;

    private static final Map<Integer, BoxItemLabelPaidEnum> ITEM_LABEL_PAID_ENUM_MAP = new HashMap<>();

    static {
        for (BoxItemLabelPaidEnum boxItemLabelPaidEnum : values()) {
            ITEM_LABEL_PAID_ENUM_MAP.put(boxItemLabelPaidEnum.getKey(), boxItemLabelPaidEnum);
        }
    }

    public static BoxItemLabelPaidEnum getBoxItemLabelPaidEnum(Integer key) {
        return ITEM_LABEL_PAID_ENUM_MAP.get(key);
    }

    BoxItemLabelPaidEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
