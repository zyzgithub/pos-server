package com.dianba.pos.payment.util;

import com.dianba.pos.payment.xmlbean.WechatOrderDto;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;


public class PayService {


    /**
     * 创建扫用户微信条形码支付参数的xml
     *
     * @param authCode
     * @param body
     * @param outTradeNo
     * @param attach
     * @param totalFee
     * @param deviceInfo
     * @param spBillCreateIP
     * @param goodsTag
     * @return
     */
    public static String createBarcodePayXml(String authCode, String body, String outTradeNo
            , String attach, Integer totalFee, String deviceInfo, String spBillCreateIP
            , String goodsTag) {
        WechatOrderDto wechatOrderDto = new WechatOrderDto();
        wechatOrderDto.setAppid(ConfigUtil.APPID_KFZ);
        wechatOrderDto.setMchId(ConfigUtil.MCH_ID_KFZ);
        wechatOrderDto.setAuthCode(authCode);
        wechatOrderDto.setBody(body);
        wechatOrderDto.setOutTradeNo(outTradeNo);
        wechatOrderDto.setAttach(attach);
        wechatOrderDto.setTotalFee(totalFee);
        wechatOrderDto.setNonceStr(PayCommonUtil.createNoncestr());
        if (!StringUtils.isEmpty(deviceInfo)) {
            wechatOrderDto.setDeviceInfo(deviceInfo);
        }
        if (!StringUtils.isEmpty(spBillCreateIP)) {
            wechatOrderDto.setSpbillCreateIp(spBillCreateIP);
        }
        String timeStart = DateTime.now().toString("yyyyMMddHHmmss");
        String timeExpire = DateTime.now().plusMinutes(5).toString("yyyyMMddHHmmss");
        wechatOrderDto.setTimeStart(timeStart);
        wechatOrderDto.setTimeExpire(timeExpire);
        wechatOrderDto.setGoodsTag(goodsTag);
        return wechatOrderDto.buildSign(ConfigUtil.API_KEY);
    }
}
