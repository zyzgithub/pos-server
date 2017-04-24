package com.base.constant.order;

/**
 * 配送方式
 * @author folo
 */
public enum DeliveryType {
    MERCHANT(1, "merchant", "商家"),
    COURIER(2, "courier", "快递员");
    
    private int code;
    private String name;
    private String cnName;
    private DeliveryType(int code, String name, String cnName) {
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
    
    public static DeliveryType get(int code){
        for (DeliveryType em : DeliveryType.values()) {
            if(em.getCode() == code) return em;
        }
        return null;
    }
    
    public static DeliveryType get(String name){
        for (DeliveryType em : DeliveryType.values()) {
            if(em.getName().equals(name)) return em;
        }
        return null;
    }
    
    public boolean equals(DeliveryType em){
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
