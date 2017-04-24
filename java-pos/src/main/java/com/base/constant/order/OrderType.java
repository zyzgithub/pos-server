package com.base.constant.order;

public enum OrderType {
    //电话订单
    ANDROID(10, "mobile", "电话订单"),
    
    //平台订单
    NORMAL(20, "normal", "平台"),
    DIRECT_PAY(21, "direct_pay", "直接支付"),
    RECHARGE(22, "recharge", "充值"),
    SUPERMARKET(23, "supermarket", "超市"),
    SUPERMARKET_SETTLEMENT(26, "supermarket_settlement", "超市现金结算"),
    EATIN_ORDER(24, "eat_in_order", "堂食系统"),
    AGENT_RECHARGE(25, "agent_recharge", "代理商充值"),
    MERCHANT_RECHARGE(25, "merchant_recharge", "商家充值"),
    
    THIRD_PART(50, "third_part", "第三方"),
    
    SCAN_ORDER(60, "scan_order", "扫码");
    
    private int code;
    private String name;
    private String cnName;
    private OrderType(int code, String name, String cnName) {
       this.code = code;
       this.name = name;
       this.cnName = cnName;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCnName() {
        return cnName;
    }
    
    public static OrderType get(int code){
        for (OrderType em : OrderType.values()) {
            if(em.getCode() == code) return em;
        }
        return null;
    }
    
    public static OrderType get(String name){
        for (OrderType em : OrderType.values()) {
            if(em.getName().equals(name)) return em;
        }
        return null;
    }
    
    public boolean equals(OrderType em){
        if(null == em) return false;
        return em.getCode() == this.getCode();
    }
    
    public boolean equals(Integer code){
        if(null == code) return false;
        return code == this.getCode();
    }
    
    public boolean equals(String name){
        if(null == name) return false;
        return name.equals(this.getName());
    }
}
