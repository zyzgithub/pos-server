package com.alipay.demo.trade.service;

import com.alipay.demo.trade.model.builder.AlipayTradePayContentBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateContentBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

public interface AlipayTradeService {
    AlipayF2FPayResult tradePay(AlipayTradePayContentBuilder paramAlipayTradePayContentBuilder);

    AlipayF2FQueryResult queryTradeResult(String paramString);

    AlipayF2FRefundResult tradeRefund(AlipayTradeRefundContentBuilder paramAlipayTradeRefundContentBuilder);

    AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateContentBuilder paramAlipayTradePrecreateContentBuilder);
}
