package com.wm.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.iwash.HttpUtils;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.MerchantScanOrderServiceI;
import com.wp.ConfigUtil;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zc on 2016/12/27.
 */
@Controller
@RequestMapping("ci/pTOrderController")
public class PTOrderController extends BaseController {
    @Autowired
    private MerchantScanOrderServiceI merchantScanOrderService;
    @Autowired
    private MerchantServiceI merchantService;
    
    @RequestMapping(params = "dpay")
    @ResponseBody
    public AjaxJson dpay(Integer merchantId, BigDecimal origin) {
        AjaxJson ajaxJson = new AjaxJson();

        /*if(com.wm.util.StringUtil.isOrder(origin.toString())){
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("不能是顺序数字");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        if(com.wm.util.StringUtil.isSame(origin.toString())){
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("不能是同样数字");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }*/

        // 系统分切,终止动态二维码服务
        ajaxJson.setSuccess(false);
        ajaxJson.setMsg("服务已停止!");

        /*
        String platformType = merchantService.getMerchantPlatformType(merchantId);
        if(!"2".equals(platformType)){
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("功能未开放");
            ajaxJson.setSuccess(false);
            return ajaxJson;
    	}
        String payId = String.valueOf(System.currentTimeMillis() + Thread.currentThread().getId());
        Integer orderId = merchantScanOrderService.createMerchantScanOrder(merchantId, origin, "", "dpt_pay", payId);
        if (orderId == 0) {
            ajaxJson.setStateCode("01");
            ajaxJson.setMsg("支付不成功！");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        // {{pay}}/pay/ptPay/wxDPrepay.action?orderNo=306022000012824979&totalFee=0.01&productName=小王测试1(举起手)
        MerchantEntity merchant = this.merchantService.get(MerchantEntity.class, merchantId);
        JSONObject params = new JSONObject();
        params.put("orderNo", payId);
        params.put("totalFee", origin);
        params.put("productName", merchant.getTitle());
        JSONObject str = HttpUtils.get(ConfigUtil.PAY_REFUND_URL_DPT, params);
        if ("SUCCESS".equalsIgnoreCase(str.getString("result_code"))) {
            Map<String, String> map = new HashMap<>();
            map.put("qrcode", str.getString("htmlParams"));
            map.put("orderId", orderId + "");
            ajaxJson.setObj(map);
        } else {
            ajaxJson.setSuccess(false);
            ajaxJson.setStateCode("02");
            ajaxJson.setMsg(StringUtil.isEmpty(str.getString("msg")) ? "系统繁忙,请稍后再试!" : str.getString("msg"));
        }
*/
        return ajaxJson;
    }


    @RequestMapping(params = "dpayState")
    @ResponseBody
    public AjaxJson dpayState(Integer orderId) {
        AjaxJson ajaxJson = new AjaxJson();

        OrderEntity orderEntity = this.merchantService.getEntity(OrderEntity.class,orderId);
        if(orderEntity==null){
            ajaxJson.setMsg("订单不存在");
            ajaxJson.setSuccess(false);
            ajaxJson.setStateCode("01");
            return ajaxJson;
        }
        ajaxJson.setObj(orderEntity.getPayState());
        return ajaxJson;
    }

}
