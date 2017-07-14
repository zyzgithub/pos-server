package com.dianba.pos.box.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.po.BoxItemLabel;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.service.BoxItemManager;
import com.dianba.pos.box.vo.BoxItemSearchVo;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(BoxURLConstant.ITEM)
public class BoxItemController {

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private BoxItemManager boxItemManager;
    @Autowired
    private PosQRCodeManager posQRCodeManager;

    @ResponseBody
    @RequestMapping(value = "getItemsByRFID", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult getItemsByRFID(HttpServletResponse response, Long passportId, String rfids) {
        if (StringUtils.isEmpty(rfids)) {
            throw new PosIllegalArgumentException("RFIDS不能为空");
        }
        return boxItemLabelManager.showItemsByRFID(passportId, rfids);
    }

    @ResponseBody
    @RequestMapping(value = "getItemsToBindingByRFID", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult getItemsByRFID(HttpServletResponse response, String rfids) {
        if (StringUtils.isEmpty(rfids)) {
            throw new PosIllegalArgumentException("RFIDS不能为空");
        }
        List<BoxItemLabel> boxItemLabels = boxItemLabelManager.getItemsToBindingByRFID(rfids);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(boxItemLabels);
        return basicResult;
    }

    @ResponseBody
    @RequestMapping(value = "getItemsByCodeOrName", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult getItemsByCodeOrName(HttpServletResponse response, Long passportId, String codeOrName) {
        List<BoxItemSearchVo> itemsByCodeOrName = boxItemManager.getItemsByCodeOrName(passportId, codeOrName);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(itemsByCodeOrName);
        return basicResult;
    }

    @ResponseBody
    @RequestMapping(value = "bindItemLabel", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult bindItemLabel(Long passportId, Long itemId, String rfids) {
        if (StringUtils.isEmpty(rfids)) {
            throw new PosIllegalArgumentException("RFIDS不能为空");
        }
        boxItemLabelManager.bindItemLabelToItems(itemId, rfids);
        List<BoxItemLabel> boxItemLabels = boxItemLabelManager.getRFIDItems(rfids);
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponseDatas(boxItemLabels);
        return basicResult;
    }
}
