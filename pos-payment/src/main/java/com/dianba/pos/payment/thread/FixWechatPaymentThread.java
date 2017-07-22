package com.dianba.pos.payment.thread;

import com.dianba.pos.core.context.ApplicationContextHelper;
import com.dianba.pos.core.thread.RunTask;
import com.dianba.pos.payment.pojo.BarcodePayResponse;
import com.dianba.pos.payment.service.PaymentManager;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;

public class FixWechatPaymentThread implements Runnable {

    private static Logger logger = LogManager.getLogger(FixWechatPaymentThread.class);

    private RunTask runTask;

    public FixWechatPaymentThread(RunTask runTask) {
        this.runTask = runTask;
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().substring(0, 6);
        logger.info(id + "微信密码输入等待...");
        boolean isComplete = false;
        int times = 30;
        String sequenceNumber = "";
        String userCode = "";
        do {
            if (times < 0) {
                break;
            }
            Map<String, String> result = (Map<String, String>) runTask.doSomething();
            String resultCode = result.get("result_code");
            String tradeState = result.get("trade_state");
            if ("".equals(sequenceNumber)) {
                sequenceNumber = result.get("out_trade_no");
            }
            if (tradeState.equals(BarcodePayResponse.WX_TRADE_STATE_ERROR)) {
                break;
            } else if (tradeState.equals("NOTPAY")) {
                break;
            } else if (resultCode.equals(BarcodePayResponse.WX_SUCCESS)
                    && tradeState.equals(BarcodePayResponse.WX_SUCCESS)) {
                userCode = result.get("openid");
                isComplete = true;
            }
            if (!isComplete) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
            times--;
        } while (!isComplete);
        if (isComplete) {
            PaymentManager paymentManager = ApplicationContextHelper.getApplicationContext()
                    .getBean(PaymentManager.class);
            paymentManager.processPaidOrder(sequenceNumber, userCode, PaymentTypeEnum.WEIXIN_NATIVE
                    , true, false);
        }
        logger.info(id + "微信密码输入等待结束..是否支付：" + isComplete);
    }
}
