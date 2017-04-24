package com.wm.controller.job;

import com.wm.entity.pay.PostBuyoutEntityLog;
import com.wm.service.pay.PostBuyoutLogService;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mjorcen on 16/8/24.
 */

@Controller
@RequestMapping("/jobController/POSJobBuyout")
public class POSJobBuyoutController {

    private final static Logger logger = LoggerFactory.getLogger(OrderJobController.class);
    @Autowired
    private PostBuyoutLogService buyoutLogService;

    /**
     * @param request
     * @param logId
     * @return
     */
    @RequestMapping("payBuyoutLog")
    @ResponseBody
    public AjaxJson payBuyoutLog(HttpServletRequest request, @RequestParam Long logId) {
        PostBuyoutEntityLog log = buyoutLogService.getEntity(PostBuyoutEntityLog.class, logId);
        buyoutLogService.payBuyoutLog(log);
        return AjaxJson.successJson("success");
    }

    /**
     * 支付
     *
     * @param request
     * @return
     */
    @RequestMapping("payBuyoutLogs")
    @ResponseBody
    public AjaxJson payBuyoutLogs(HttpServletRequest request) throws Exception {
        buyoutLogService.payBuyoutLogs();
        return AjaxJson.successJson("success");
    }


}
