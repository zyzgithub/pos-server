package com.dianba.pos.box.controller;

import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.po.BoxDoorInfo;
import com.dianba.pos.box.service.BoxDoorInfoManager;
import com.dianba.pos.box.util.DoorCommandUtil;
import com.dianba.pos.box.util.DoorStatusUtil;
import com.dianba.pos.box.util.NacCryptUtil;
import com.dianba.pos.box.vo.AccessResultVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(BoxURLConstant.ACCESS)
public class BoxAccessController {

    private static Logger logger = LogManager.getLogger(BoxAccessController.class);

    @Autowired
    private BoxDoorInfoManager boxDoorInfoManager;

    @RequestMapping("checkPosition")
    public ModelAndView checkPosition() {
        return new ModelAndView("position");
    }

    @RequestMapping("DeviceControl")
    public void deviceControl(HttpServletRequest request, HttpServletResponse response) {
        AccessResultVo accessResultVoRequest = NacCryptUtil.decode(request);
        BoxDoorInfo boxDoorInfo = boxDoorInfoManager.getDoorInfoByAccessSN(accessResultVoRequest.getSn());
        if (boxDoorInfo != null) {
            Long requestTimeMillis = DoorStatusUtil.getDoorStatus(boxDoorInfo.getPassportId());
            if (null != requestTimeMillis) {
                //有请求进店/离店人员，开门有效期5秒
                Long currTimeMillis = System.currentTimeMillis();
                if (currTimeMillis > requestTimeMillis
                        && currTimeMillis - requestTimeMillis <= 5000) {
                    DoorCommandUtil.openDoor(response, accessResultVoRequest.getSn());
                    return;
                }
            }
        }
        DoorCommandUtil.doNothing(response, accessResultVoRequest.getSn());
    }


}
