package com.dianba.pos.box.controller;

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
    @RequestMapping("DeviceControl")
    public AccessResultVo deviceControl(HttpServletRequest request) {
        logger.info(request.getParameterMap());
        for (Object key : request.getParameterMap().keySet()) {
            logger.info("k=" + key.toString() + ",v=" + request.getParameter(key.toString()));
        }
        return new AccessResultVo();
    }
}
