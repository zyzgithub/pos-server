package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.util.NacCryptUtil;
import com.dianba.pos.box.vo.AccessResultVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(BoxURLConstant.ACCESS)
public class BoxAccessController {

    private static Logger logger = LogManager.getLogger(BoxAccessController.class);

    @RequestMapping("checkPosition")
    public ModelAndView checkPosition() {
        return new ModelAndView("position");
    }

    @RequestMapping("DeviceControl")
    public void deviceControl(HttpServletRequest request, HttpServletResponse response) {
        AccessResultVo accessResultVoRequest = NacCryptUtil.decode(request);
        AccessResultVo accessResultVo = new AccessResultVo();
        accessResultVo.setCmd("36");
        accessResultVo.setSn("1102890139");
        accessResultVo.setCurtime(System.currentTimeMillis() / 1000 + "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("door", "1");
        accessResultVo.setData(jsonObject);
        NacCryptUtil.encode(response, accessResultVo);
    }
}
