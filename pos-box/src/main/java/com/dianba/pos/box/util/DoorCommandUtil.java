package com.dianba.pos.box.util;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.box.vo.AccessResultVo;

import javax.servlet.http.HttpServletResponse;

public class DoorCommandUtil {

    /**
     * 无操作返回消息
     *
     * @param response
     * @param sn
     */
    public static void doNothing(HttpServletResponse response, String sn) {
        AccessResultVo accessResultVo = new AccessResultVo();
        accessResultVo.setCmd("1");
        accessResultVo.setSn(sn);
        accessResultVo.setCurtime(System.currentTimeMillis() / 1000 + "");
        accessResultVo.setData(new JSONObject());
        NacCryptUtil.encode(response, accessResultVo);
    }

    /**
     * 开门消息
     *
     * @param response
     * @param sn
     */
    public static void openDoor(HttpServletResponse response, String sn) {
        AccessResultVo accessResultVo = new AccessResultVo();
        accessResultVo.setCmd("36");
        accessResultVo.setSn(sn);
        accessResultVo.setCurtime(System.currentTimeMillis() / 1000 + "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("door", "1");
        accessResultVo.setData(jsonObject);
        NacCryptUtil.encode(response, accessResultVo);
    }

    /**
     * 设置门控方式
     */
    public static void setDoorControlType(HttpServletResponse response, String sn, Integer controlType) {
        AccessResultVo accessResultVo = new AccessResultVo();
        accessResultVo.setCmd("38");
        accessResultVo.setSn(sn);
        accessResultVo.setCurtime(System.currentTimeMillis() / 1000 + "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("door", "1");
        jsonObject.put("ctrltype", controlType + "");
        accessResultVo.setData(jsonObject);
        NacCryptUtil.encode(response, accessResultVo);
    }
}
