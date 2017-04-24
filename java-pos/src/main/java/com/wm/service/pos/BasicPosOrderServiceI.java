package com.wm.service.pos;

import com.wm.controller.supplychain.SupplyChainMenuVo;
import com.wm.entity.pos.PurchaseDetail;
import com.wm.util.HttpProxy;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.order.dto.ScanOrderFromPosDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhanxinming
 * @version 1.0
 * @date 创建时间：2016年8月16日 上午9:50:36
 * @return
 */
public interface BasicPosOrderServiceI extends CommonService {

    public ScanOrderFromPosDTO creatScanOrderFromPos(ScanOrderFromPosDTO scanOrderDto);

    Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId) throws HttpProxy.HttpAccessException, IOException;

    List<String> getPurchaseBarcodes(Integer orderId, Integer userId, Integer merchantId);

    List<PurchaseDetail> getPurchaseDetail(Integer id, Integer userId, Integer merchantId, Long createTime);

    void enablePurchaseDetail(Integer orderId);

    void savePurchaseDetail(PurchaseDetail purchaseDetail);

    Object posTryCreateOrder(Integer userId, String itemInfos, String warehouseInfos, Integer addressId) throws IOException;

    void doComfortOrder(Integer merchantId, Integer mainOrderId, List<SupplyChainMenuVo> menus);
    
    PurchaseDetail getUnUsePurchaseDetail(Integer orderId);
}
