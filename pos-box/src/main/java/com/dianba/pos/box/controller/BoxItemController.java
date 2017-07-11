package com.dianba.pos.box.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(BoxURLConstant.ITEM)
public class BoxItemController {

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private PosQRCodeManager posQRCodeManager;

    @ResponseBody
    @RequestMapping("getItemsByRFID")
    public BasicResult getItemsByRFID(HttpServletResponse response, Long passportId, String rfids) {
        if (StringUtils.isEmpty(rfids)) {
            throw new PosIllegalArgumentException("RFIDS不能为空");
        }
        return boxItemLabelManager.showItemsByRFID(passportId, rfids);
    }
}
