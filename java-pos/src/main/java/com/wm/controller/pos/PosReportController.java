package com.wm.controller.pos;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.pos.MarketingReportLine;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.util.EMailClient;
import com.wm.util.SimpleExcelWriter;
import jxl.write.WriteException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zc on 2016/11/21.
 */
@Controller
@RequestMapping("/pos/reportController")
public class PosReportController extends BaseController {

    @Autowired
    private MenuServiceI menuService;

    @Autowired
    private MerchantServiceI merchantService;

    @RequestMapping(params = "marketingReport")
    @ResponseBody
    public AjaxJson marketingReport(String email, String merchantId, String typeId, String searchKey, String startTime, String endTime, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer pageSize) {
        if ("0".equals(typeId)) {
            typeId = null;
        }
        List<MarketingReportLine> lineList = menuService.marketingReport(merchantId, typeId, searchKey, startTime, endTime, page, pageSize);
        if (StringUtils.isNotEmpty(email)) {
            try {
                MerchantEntity merchantEntity = merchantService.findUniqueByProperty(MerchantEntity.class, "id", Integer.parseInt(merchantId));
                sendEmail(lineList, email, merchantEntity.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
                AjaxJson j = new AjaxJson();
                j.setObj(lineList);
                j.setSuccess(false);
                j.setMsg("导出失败,请稍后再试!");
                return j;
            }
        }
        AjaxJson j = new AjaxJson();
        j.setObj(lineList);
        if (CollectionUtils.isEmpty(lineList)) {
            j.setStateCode("011");
        }
        j.setSuccess(true);
        return j;
    }

    private void sendEmail(List<MarketingReportLine> lineList, String email, String merchantName) throws IOException, WriteException {
        List<String> title = Arrays.asList("排名", "商品名称", "商品条码", "商品类型名称", "销售数量", "总销售额", "销售占比", "毛利", "毛利率");
        List<List<String>> datas = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(lineList)) {
            for (MarketingReportLine line : lineList) {
                List<String> data = new LinkedList<>();
                data.add(line.getPosition() + "");
                data.add(line.getMenuName() + "");
                data.add(line.getBarcode() + "");
                data.add(line.getMenuTypeName() + "");
                data.add(line.getTotalSales() + "");
                data.add(line.getTotalMoney() + "");
                data.add(line.getGrossMargin() + "");

                if (StringUtils.isNotEmpty(line.getMoneyRatio())) {
                    String profit = line.getMoneyRatio() + "";
                    data.add(new BigDecimal(profit).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + " %");
                } else {
                    data.add("-");
                }


                if (StringUtils.isNotEmpty(line.getGrossProfit())) {
                    String profit = line.getGrossProfit() + "";
                    data.add(new BigDecimal(profit).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + " %");
                } else {
                    data.add("-");
                }
                datas.add(data);
            }
        }
        byte[] bs = SimpleExcelWriter.getWorkbookBytes(title, datas);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str = sdf.format(new Date());
        str = str + merchantName;
        EMailClient mail = new EMailClient(email, str + "POS导出商品销售报表", str + "POS导出商品销售报表", "POS导出销售报表附件已发送，请查看！");
        mail.addAttachfile("销售报表_" + str + ".xls", bs);
        mail.send();
    }
}
