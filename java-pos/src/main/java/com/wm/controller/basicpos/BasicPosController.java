package com.wm.controller.basicpos;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wm.controller.order.dto.ScanOrderFromPosDTO;
import com.wm.entity.pos.PosOrderModifyMoneyDetail;
import com.wm.entity.pos.PurchaseDetail;
import com.wm.exception.ApplicationRuntimeException;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.pos.BasicPosOrderServiceI;
import com.wm.service.pos.PosOrderModifyMoneyDetailService;
import com.wm.util.HttpProxy;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mjorcen on 16/8/15.
 */
@Controller
@RequestMapping("ci/basicPosController")
public class BasicPosController extends BaseController {

    @Autowired
    private PosOrderModifyMoneyDetailService posOrderModifyMoneyDetailService;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BasicPosOrderServiceI basicPosOrderService;

    @Autowired
    private MerchantServiceI merchantService;

    /**
     * 更改价格
     *
     * @param json
     * @param remark
     * @param request
     * @return
     */
    @RequestMapping(params = "modify")
    @ResponseBody
    public AjaxJson modify(String json, String remark, HttpServletRequest request) {
        AjaxJson j;
        try {
        	logger.info(json);
            PosOrderModifyMoneyDetail detail = JSONObject.parseObject(json, PosOrderModifyMoneyDetail.class);
            Map<String, Object> map = posOrderModifyMoneyDetailService.modify(detail, remark);
            j = new AjaxJson(true,"修改成功", map, "00");
        } catch (ApplicationRuntimeException e) {
            logger.warn("ApplicationRuntimeException", e);
            j = AjaxJson.failJson(e.getMsg());
        } catch (Exception e1) {
            logger.error("Exception", e1);
            j = AjaxJson.failJson("系统繁忙");
        }
        return j;
    }


    /**
     * 查看 库存预警商品
     *
     * @param merchantId
     * @param userId
     * @param request
     * @return
     * @throws HttpProxy.HttpAccessException
     * @throws IOException
     */
    @RequestMapping(params = "warnInventoryList")
    @ResponseBody
    public AjaxJson warnInventoryList(Integer merchantId, Integer userId, HttpServletRequest request) throws HttpProxy.HttpAccessException, IOException {
        AjaxJson j = AjaxJson.successJson("请求成功");
        Map<String, Object> map = this.basicPosOrderService.warnInvenstoryList(merchantId, userId);
        if(map==null || map.isEmpty()){
            j.setType("2");
        } else if(null == map.get("preferentialList") && null == map.get("externalList")){
        	j.setType("3");
        } else if(null != map.get("preferentialList") && null != map.get("externalList")){
        	JSONArray parray = (JSONArray) map.get("preferentialList");
        	JSONArray earray = (JSONArray) map.get("externalList");
        	if(parray.isEmpty() && earray.isEmpty()){
        		j.setType("3");
        	} else {
        		j.setType("1");
        	}
        } else {
            j.setType("1");
        }

        j.setObj(map);
        return j;
    }

    /**
     * 一键采购.
     *
     * @param userId
     * @param itemInfos
     * @param warehouseInfos
     * @return
     * @throws HttpProxy.HttpAccessException
     * @throws IOException
     */
    @RequestMapping(params = "posTryCreateOrder")
    @ResponseBody
    public AjaxJson posTryCreateOrder(Integer userId, String itemInfos, String warehouseInfos, Integer addressId) throws HttpProxy.HttpAccessException, IOException {
        AjaxJson j = AjaxJson.successJson("请求成功");
        try{
            Object o = this.basicPosOrderService.posTryCreateOrder(userId, itemInfos, warehouseInfos, addressId);
            j.setObj(o);

        }catch (ApplicationRuntimeException e){
            j = AjaxJson.failJson(e.getMsg());
        }
        return j;
    }

    @RequestMapping(params = "getPurchaseDetail")
    @ResponseBody
    @Deprecated
    /**
     * 这个是调试接口
     */
    public AjaxJson getPurchaseDetail(Integer merchantId, Integer userId, HttpServletRequest request) throws HttpProxy.HttpAccessException, IOException {
        AjaxJson j = AjaxJson.successJson("请求成功");
        List<PurchaseDetail> list = this.basicPosOrderService.getPurchaseDetail(null, userId, merchantId,null);
        j.setObj(list);
        return j;
    }

    @RequestMapping(params = "savePurchaseDetail")
    @ResponseBody
    @Deprecated()
    /**
     * 这个是调试接口
     */
    public AjaxJson savePurchaseDetail(Integer merchantId, Integer userId, HttpServletRequest request) throws HttpProxy.HttpAccessException, IOException {
        AjaxJson j = AjaxJson.successJson("请求成功");

        PurchaseDetail purchaseDetail = new PurchaseDetail();
        purchaseDetail.setUserId(userId);
        purchaseDetail.setMerchantId(merchantId);
        purchaseDetail.setCreateTime(System.currentTimeMillis() / 1000);
        Map<String, Integer> objectMap = new HashMap<>();
        //                Arrays.asList("6901285991240", "6901285991219");

        objectMap.put("6901285991240", 12);
        objectMap.put("6901285991219", 12);
        purchaseDetail.setBarcodePurchase(objectMap);
        this.basicPosOrderService.savePurchaseDetail(purchaseDetail);
        return j;
    }

    @RequestMapping(params = "scanOrderFromPos")
    @ResponseBody
    public AjaxJson scanOrderFromPos(ScanOrderFromPosDTO scanOrderDto, Errors errors) {
        AjaxJson json = new AjaxJson();
        logger.info("scanOrderDto:{}", JSON.toJSON(scanOrderDto));
        String errormsg = "";
        if (errors.hasErrors()) {
            List<FieldError> list = errors.getFieldErrors();
            Map<String, Object> errorsMap = new HashMap<String, Object>();
            for (int i = 0; i < list.size(); i++) {
                errorsMap.put(list.get(i).getField(), "错误值：" + list.get(i).getRejectedValue());
                errormsg = errormsg + "错误参数： " + list.get(i).getField() + "， 错误值： " + list.get(i).getRejectedValue();
            }
            logger.error("参数错误, 参数: " + JSON.toJSONString(scanOrderDto));
            json.setSuccess(false);
            json.setStateCode("01");
            json.setObj(errorsMap);
            json.setMsg(errormsg);
            logger.error("参数错误  return:" + JSON.toJSONString(json));
            return json;
        }
        try {
            scanOrderDto = basicPosOrderService.creatScanOrderFromPos(scanOrderDto);
            json.setSuccess(true);
            json.setObj(scanOrderDto);
            json.setMsg("操作成功");
            json.setStateCode("00");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(true);
            json.setMsg("操作失败");
            json.setStateCode("01");
        }
        return json;
    }

}
