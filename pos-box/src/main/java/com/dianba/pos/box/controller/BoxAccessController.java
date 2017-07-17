package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.vo.AccessResultVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(BoxURLConstant.ACCESS)
public class BoxAccessController {

    private static Logger logger = LogManager.getLogger(BoxAccessController.class);

    @RequestMapping("checkPosition")
    public ModelAndView checkPosition() {
        return new ModelAndView("position");
    }

    @ResponseBody
    @RequestMapping("deviceControl")
    public AccessResultVo test(HttpServletRequest request) {
        AccessResultVo accessResultVo = new AccessResultVo();
        accessResultVo.setCmd("36");
        accessResultVo.setSn("1102890139");
        accessResultVo.setCurtime("141325634232");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("door", "1");
//        jsonObject.put("ctrltype", "1");
//        jsonObject.put("reason","0");
        accessResultVo.setData(jsonObject);
        return accessResultVo;
    }

    @ResponseBody
    @RequestMapping("DeviceControl")
    public AccessResultVo deviceControl(HttpServletRequest request) {
        for (Object key : request.getParameterMap().keySet()) {
            logger.info("key->" + key.toString() + ",val->" + request.getParameter(key.toString()));
        }
        return test(request);
    }

    @ResponseBody
    @RequestMapping("submitrecord")
    public AccessResultVo submitRecord(HttpServletRequest request) {
        logger.info("接收报警记录");
        for (Object key : request.getParameterMap().keySet()) {
            logger.info("key->" + key.toString() + ",val->" + request.getParameter(key.toString()));
        }
        return test(request);
    }
}
