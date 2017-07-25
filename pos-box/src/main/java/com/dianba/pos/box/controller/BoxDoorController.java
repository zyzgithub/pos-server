package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxAccountLogManager;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.util.DoorPlayStatusUtil;
import com.dianba.pos.box.util.DoorStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(BoxURLConstant.DOOR)
public class BoxDoorController {

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private BoxAccountLogManager boxAccountLogManager;

    @RequestMapping(value = "openDoor", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView openDoor(Long passportId, String openId) {
        DoorStatusUtil.writeDoorStatus(passportId);
        boxAccountLogManager.saveOpenLog(openId);
        return new ModelAndView("account/lock");
    }

    @ResponseBody
    @RequestMapping(value = "leaveInspect", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult leaveInspect(Long passportId, String rfids) {
        BasicResult basicResult = BasicResult.createSuccessResult();
        boolean isAllPaid = boxItemLabelManager.isAllPaid(rfids);
        JSONObject jsonObject = new JSONObject();
        Long requestTimeMillis = DoorPlayStatusUtil.getDoorPlayStatus(passportId);
        boolean play = true;
        if (null != requestTimeMillis) {
            //播报延迟3秒
            Long currTimeMillis = System.currentTimeMillis();
            if (currTimeMillis > requestTimeMillis
                    && currTimeMillis - requestTimeMillis <= 3000) {
                play = false;
            }
        }
        if (play) {
            DoorPlayStatusUtil.writeDoorPlayStatus(passportId);
        }
        jsonObject.put("play", play);
        if (isAllPaid) {
            jsonObject.put("content", "门锁已开启！欢迎下次光临！");
            DoorStatusUtil.writeDoorStatus(passportId);
        } else {
            jsonObject.put("content", "您有未支付商品，请返回自助收银台支付！");
        }
        basicResult.setResponse(jsonObject);
        return basicResult;
    }
}
