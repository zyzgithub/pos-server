package com.dianba.pos.payment.util;

import com.dianba.pos.payment.pojo.BarcodePayResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WxBarcodePayReturnHandler {

    private static Logger logger = LogManager.getLogger(WxBarcodePayReturnHandler.class);


    /**
     * 判断支付是否成功
     *
     * @param result
     * @return
     */
    public static boolean isPaySuccess(Map<String, String> result) {
        logger.info("isPaySuccess result:{}", result);
        String resultCode = result.get("result_code");
        String returnCode = result.get("return_code");
        return StringUtils.equals(resultCode, BarcodePayResponse.WX_SUCCESS)
                && StringUtils.equals(returnCode, BarcodePayResponse.WX_SUCCESS);
    }

    /**
     * 对支付结果进行处理
     *
     * @param result
     * @return
     */
    public static BarcodePayResponse handle(Map<String, String> result) {
        if (isPaySuccess(result)) {
            return BarcodePayResponse.SUCCESS;
        } else {
            //其他异常
            logger.info("其他异常 result:{}", result);
            BarcodePayResponse response = BarcodePayResponse.FAILURE;
            response.setMsg(result.get("trade_state_desc") + ", " + result.get("err_code_des"));
            return response;
        }
    }


}
