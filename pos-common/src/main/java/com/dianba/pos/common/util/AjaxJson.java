package com.dianba.pos.common.util;

import java.util.HashMap;
import java.util.Map;

public class AjaxJson {

    /**
     * 状态码：成功
     */
    public static final String STATE_CODE_SUCCESS = "00";

    /**
     * 状态码：失败
     */
    public static final String STATE_CODE_FAIL = "01";

    private boolean success = true; // 是否成功
    private String msg = "操作成功"; // 提示信息
    private Object obj = null; // 结果集
    private Map<String, AjaxJson> arrayAjaxJson; // 多结果集的JSON信息
    private String sql; // 参数补全后的sql
    private String type;
    private Integer affectedRows; // 受影响的行数
    private Map<String, Object> attributes;
    private String sessionkey = null;
    private String state = "success";//sessionkey状态
    private String stateCode = "00";//状态码  00正常 01异常

    public AjaxJson(boolean success, String msg, String stateCode) {
        super();
        this.success = success;
        this.msg = msg;
        this.stateCode = stateCode;
    }

    public AjaxJson(boolean success, String msg, Object obj, String stateCode) {
        super();
        this.success = success;
        this.msg = msg;
        this.obj = obj;
        this.stateCode = stateCode;
    }

    public AjaxJson() {
        super();
        arrayAjaxJson = new HashMap<String, AjaxJson>();
    }

    public static AjaxJson successJson(String msg) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg(msg);
        ajaxJson.setStateCode(STATE_CODE_SUCCESS);
        return ajaxJson;
    }

    public static AjaxJson failJson(String msg) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg(msg);
        ajaxJson.setStateCode(STATE_CODE_FAIL);
        return ajaxJson;
    }

    public static AjaxJson fromMsgResp(MsgResp msgResp) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(msgResp.isSuccess());
        ajaxJson.setMsg(msgResp.getMsg());
        ajaxJson.setStateCode(msgResp.isSuccess() ? STATE_CODE_SUCCESS : STATE_CODE_FAIL);
        return ajaxJson;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Map<String, AjaxJson> getArrayAjaxJson() {
        return arrayAjaxJson;
    }

    public void setArrayAjaxJson(Map<String, AjaxJson> arrayAjaxJson) {
        this.arrayAjaxJson = arrayAjaxJson;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AjaxJson msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(Integer affectedRows) {
        this.affectedRows = affectedRows;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AjaxJson state(String state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "AjaxJson [success=" + success + ", msg=" + msg + ", obj=" + obj
                + ", sessionkey=" + sessionkey
                + ", state=" + state + ", stateCode=" + stateCode + "]";
    }
}
