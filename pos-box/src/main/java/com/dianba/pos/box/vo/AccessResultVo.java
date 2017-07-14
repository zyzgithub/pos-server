package com.dianba.pos.box.vo;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

public class AccessResultVo implements Serializable {

    //命令代号
    private Integer cmd;
    //设备序列号
    private String sn;
    //时间戳
    private Long curTime;
    //命令数据部分
    private JSONArray data;

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Long getCurTime() {
        return curTime;
    }

    public void setCurTime(Long curTime) {
        this.curTime = curTime;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
