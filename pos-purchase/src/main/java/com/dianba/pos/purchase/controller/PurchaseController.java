package com.dianba.pos.purchase.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.WarehouseItemPojo;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.purchase.config.PurchaseURLConstant;
import com.dianba.pos.purchase.service.PurchaseManager;
import com.xlibao.common.constant.order.OrderTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("一键采购获取商品列表数据")
@Controller
@RequestMapping(PurchaseURLConstant.PURCHASE_ONE_KEY)
public class PurchaseController {

    @Autowired
    private PurchaseManager purchaseManager;
    @Autowired
    private LifeOrderManager orderManager;
    @Autowired
    private PassportManager passportManager;

    /**
     * 一键采购获取商品
     **/
    @ApiOperation("获取优惠生活建议采购，系统外建议采购列表")
    @ApiImplicitParam(name = "passportId", value = "通行证ID", required = true)
    @ResponseBody
    @RequestMapping(value = "warnInventoryList", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult warnInventoryList(Long passportId) throws Exception {
        return purchaseManager.getWarnRepertoryList(passportId);
    }

    /**
     * 一键采购下单
     *
     * @param passportId  通行证ID
     * @param warehouseId 仓库id
     * @param itemSets    JSONObject类型，{"10000":"2"} key为商品ID:value是商品采购数量。
     */
    @ResponseBody
    @RequestMapping("create_purchase_order")
    public BasicResult createOrder(HttpServletRequest request
            , Long passportId, Long warehouseId, String itemSets) throws Exception {
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.PURCHASE_ORDER_TYPE;
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        if (merchantPassport == null) {
            throw new PosNullPointerException("商家不存在！");
        }
        BasicResult basicResult = orderManager.prepareCreateOrder(merchantPassport.getId(), orderTypeEnum);
        if (basicResult.isSuccess()) {
            List<WarehouseItemPojo> warehouseItemPojos = JsonHelper.toList(itemSets, WarehouseItemPojo.class);
            String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");
            JSONObject jsonObject = new JSONObject();
            for (WarehouseItemPojo itemPojo : warehouseItemPojos) {
                jsonObject.put(itemPojo.getId() + "", itemPojo.getCount());
            }
            basicResult = purchaseManager.generatePurchaseOrder(merchantPassport.getId()
                    , sequenceNumber, warehouseId, jsonObject);
            if (basicResult.isSuccess()) {
                LifeOrder lifeOrder = orderManager.getLifeOrder(sequenceNumber);
                basicResult.setResponse(lifeOrder);
            }
        }
        return basicResult;
    }
}
