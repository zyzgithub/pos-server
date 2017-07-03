package com.alipay.demo.trade.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class AbsAlipayService {
    private Logger logger = LogManager.getLogger(getClass());

    protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
        try {
            AlipayResponse response = client.execute(request);
            if (response != null) {
                logger.info(response.getBody());
            }
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Logger getLogger() {
        return logger;
    }
}
