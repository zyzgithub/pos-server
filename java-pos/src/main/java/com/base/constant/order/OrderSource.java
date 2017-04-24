package com.base.constant.order;

/**
 * 订单来源
 * @author folo
 */
public enum OrderSource {
    ANDROID(10, "android", "安卓"),
    IOS(20, "ios", "苹果"),
    WECHAT(30, "wechat", "微信");
    
    private int code;
    private String name;
    private String cnName;
    private OrderSource(int code, String name, String cnName) {
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
    
    public static OrderSource get(int code){
        for (OrderSource em : OrderSource.values()) {
            if(em.getCode() == code) return em;
        }
        return null;
    }
    
    public static OrderSource get(String name){
        for (OrderSource em : OrderSource.values()) {
            if(em.getName().equals(name)) return em;
        }
        return null;
    }
    
    public boolean equals(OrderSource em){
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
