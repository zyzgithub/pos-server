package com.dianba.pos.report.controller;

import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.report.config.ReportURLConstant;
import com.dianba.pos.report.pojo.PosStatistics;
import com.dianba.pos.report.service.PosReportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(ReportURLConstant.POS_REPORT_URL)
public class PosReportController {

    @Autowired
    private PosReportManager posReportManager;

    @ResponseBody
    @RequestMapping("pos_statistics")
    public AjaxJson posStatistics(){
        AjaxJson ajaxJson=new AjaxJson();
        ajaxJson.setSuccess(true);
        PosStatistics posStatistics=posReportManager.posStatistics();
        ajaxJson.setResponse(posStatistics);
        return ajaxJson;
    }
}
