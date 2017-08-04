package com.dianba.pos.extended.support;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.common.util.MapUtil;
import com.dianba.pos.common.util.Md5Util;
import com.dianba.pos.extended.config.ExtendedConfig;
import com.dianba.pos.extended.util.FlowCharge19EUtil;
import com.dianba.pos.extended.util.FlowChargeSign;
import com.dianba.pos.extended.util.HfCharge19EUtil;
import com.dianba.pos.extended.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyong on 2017/6/22.
 */
public class ExtendedRemoteService {
    private static Logger logger = LogManager.getLogger(ExtendedRemoteService.class);
    @Autowired
    private ExtendedConfig extendedConfig;

    /**
     * 话费充值请求
     *
     * @param
     * @param param
     * @return
     */
    protected ChargeResult hfCharge(Charge19E param) {
        //签名
        String sign = param.sign();
        //签名加密
        String md5 = Md5Util.HEXAndMd5(sign).toUpperCase();
        // 发送请求参数
        String params = param.params(md5);
        //发送话费充值请求
        String result = HttpUtil.postParams(extendedConfig.getExtendedHfChargeUrl(), params);
        logger.info("话费充值返回:"+result);
        String json = HfCharge19EUtil.toJson(result);
        ChargeResult chargeResult =JSONObject.parseObject(json, ChargeResult.class);
        return chargeResult;
    }

    /**
     * 流量充值
     *
     * @param
     * @param flow
     * @return
     */
    protected ChargeFlowResult flowCharge(ChargeFlow flow) {

        Map map = new HashMap<>();
        map.put("signType", flow.getSignType());
        map.put("timestamp", flow.getTimestamp());
        map.put("dataType", flow.getDataType());
        map.put("inputCharset", flow.getInputCharset());
        map.put("version", flow.getVersion());
        map.put("productId", flow.getProductId());
        map.put("mobile", flow.getMobile());
        map.put("merchantId", flow.getMerchantId());
        map.put("merOrderNo", flow.getMerOrderNo());
        String md5 = FlowChargeSign.getSignByMap(map);
        logger.info("流量充值md5:"+md5);
        map.put("sign", md5);
        Map<String, Object> sortMapByKey = MapUtil.sortMapByKey(map);
        String params = MapUtil.createLinkString(sortMapByKey);
        logger.info("流量充值参数:" + params);
        String result = HttpUtil.postParams(extendedConfig.getExtendedFlowChargeUrl(), params);
        logger.info("流量充值返回结果:" + result);
        ChargeFlowResult chargeFlowResult =JSONObject.parseObject(result, ChargeFlowResult.class);
        return chargeFlowResult;
    }

    /***根据手机号等参数获取产品信息**/
    protected String queryProduct(String chargeUrl, Product pd) {

        Map map = new HashMap<>();
        map.put("signType", pd.getSignType());
        map.put("timestamp", pd.getTimestamp());
        map.put("dataType", pd.getDataType());
        map.put("inputCharset", pd.getInputCharset());
        map.put("version", pd.getVersion());
        map.put("merchantId", pd.getMerchantId());
        map.put("mobile", pd.getMobile());
        Map<String, Object> pdmap = MapUtil.sortMapByKey(map);
        String sign = MapUtil.createLinkString(pdmap);
        String md5 = FlowCharge19EUtil.getKeyedDigest(sign, extendedConfig.getExtendedFlowKey());
        map.put("sign", md5);
        Map<String, Object> sortMapByKey = MapUtil.sortMapByKey(map);
        String params = MapUtil.createLinkString(sortMapByKey);
        String result = HttpUtil.postParams(extendedConfig.getExtendedFlowProductUrl(), params);
        return result;

    }
}
