package com.dianba.pos.payment.util;

import com.dianba.pos.payment.pojo.BarcodePayResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WxBarcodePayReturnHandler {

    private static Logger logger = LogManager.getLogger(WxBarcodePayReturnHandler.class);

    private static final int RETRY_TIMES = 12;

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
     * 判断是否需要用户支付密码
     *
     * @param result
     * @return
     */
    public static boolean isUserPaying(Map<String, String> result) {
        logger.info("isUserPaying result:{}", result);
        String errCode = result.get("err_code");
        String errCodeDes = result.get("err_code_des");
        return StringUtils.equals(errCode, BarcodePayResponse.WX_EC_USERPAYING)
                || StringUtils.equals(errCodeDes, "交易已提交，请检查是否已扣款后再试，不要重复支付。");
    }

    /**
     * 查询订单是否支付
     *
     * @param result
     * @return
     */
    public static boolean isTradeStateSuccess(Map<String, String> result) {
        logger.info("isTradeStateSuccess result:{}", result);
        String resultCode = result.get("result_code");
        String tradeState = result.get("trade_state");
        logger.info("resultCode:{}, tradeState:{}", resultCode, tradeState);
        return StringUtils.equals(resultCode, BarcodePayResponse.WX_SUCCESS)
                && StringUtils.equals(tradeState, BarcodePayResponse.WX_SUCCESS);
    }

    /**
     * 对支付结果进行处理
     *
     * @param result
     * @return
     */
    public static BarcodePayResponse handle(Map<String, String> result) {
        if (isPaySuccess(result)) {
            return onPaySuccess(result);
        } else if (isUserPaying(result)) {
            //等待用户输入密码
            return onWaitingEntryPassword(result);
        } else {
            //其他异常
            logger.info("其他异常 result:{}", result);
            BarcodePayResponse response = BarcodePayResponse.FAILURE;
            response.setMsg(result.get("trade_state_desc") + ", " + result.get("err_code_des"));
            return response;
        }
    }

    public static BarcodePayResponse onPaySuccess(Map<String, String> result) {
        return BarcodePayResponse.SUCCESS;
    }


    /**
     * 等待用户输入密码时，循环查看订单是否被支付
     *
     * @param result
     * @return
     */
    public static BarcodePayResponse onWaitingEntryPassword(Map<String, String> result) {
        String outTradeNo = result.get("out_trade_no");
        Map<String, String> query = null;
        for (int i = 0; i < RETRY_TIMES; i++) {

            try {
                Thread.sleep(5000);

                query = WxBarcodePayApi.orderPayQuery(outTradeNo);
                if (query != null) {
                    if (StringUtils.equals(query.get("trade_state"), BarcodePayResponse.WX_TRADE_STATE_ERROR)) {
                        return BarcodePayResponse.FAILURE;
                    }
                    if (isTradeStateSuccess(query)) {
                        logger.info("第{}次查询，支付成功...", i + 1);
                        return BarcodePayResponse.SUCCESS;
                    } else {
                        logger.info("第{}次查询，支付失败...", i + 1);
                    }
                }
            } catch (Exception e) {
                return BarcodePayResponse.FAILURE;
            }
        }
        BarcodePayResponse response = BarcodePayResponse.FAILURE;
        if (query != null && query.get("trade_state_desc") != null) {
            response.setMsg(result.get("trade_state_desc"));
            //撤销订单
            WxBarcodePayApi.reverseOrder(outTradeNo);
        }
        return response;
    }
}
