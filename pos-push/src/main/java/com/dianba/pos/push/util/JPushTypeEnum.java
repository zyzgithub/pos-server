package com.dianba.pos.push.util;

/**
 * Created by zhangyong on 2017/7/12.
 */
public enum JPushTypeEnum {

    SPEAK(1,"SPEAK","语音播报","您有一笔新的入账,请注意查收!","支付成功,收款")
    ,BLACKLIST(2,"BLACKLIST","拉黑通知","刷单拉黑通知!","您的账号存在刷单行为，返利功能已被限制"
           + "，如需了解详情请联系一号生活客服了解具体情况!")
    ,SETTLEMENT(3,"SETTLEMENT","结算清单","您有一笔新的结算清单,请注意查收!\n结算用户:","您有一笔新的结算清单,请注意查收!\n结算用户:")
    ;

    JPushTypeEnum(int type, String key, String value, String title, String msg) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.title=title;
        this.msg=msg;
    }

    private int type;
    private String key;
    private String value;
    private String title;
    private String msg;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
