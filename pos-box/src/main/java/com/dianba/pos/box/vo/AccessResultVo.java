package com.dianba.pos.box.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class AccessResultVo implements Serializable {

    //命令代号
    private String cmd;
    //设备序列号
    private String sn;
    //时间戳
    private String curtime;
    //命令数据部分
    private JSONObject data;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCurtime() {
        return curtime;
    }

    public void setCurtime(String curtime) {
        this.curtime = curtime;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
