package com.base.constant.order;

/**
 * 获取商品方式
 * @author folo
 */
public enum GetGoodsWay {
    TAKEOUT(1, "takeOut", "外卖"),
    EATIN(2, "eatIn", "堂食"),
    MENTION(3, "mention", "自提");
    
    private int code;
    private String name;
    private String cnName;
    private GetGoodsWay(int code, String name, String cnName) {
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
    
    public static GetGoodsWay get(int code){
        for (GetGoodsWay em : GetGoodsWay.values()) {
            if(em.getCode() == code) return em;
        }
        return null;
    }
    
    public static GetGoodsWay get(String name){
        for (GetGoodsWay em : GetGoodsWay.values()) {
            if(em.getName().equals(name)) return em;
        }
        return null;
    }
    
    public boolean equals(GetGoodsWay em){
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
