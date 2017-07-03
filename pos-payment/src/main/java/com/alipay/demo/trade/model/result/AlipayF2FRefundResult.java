package com.alipay.demo.trade.model.result;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.model.TradeStatus;

public class AlipayF2FRefundResult implements Result {

    private TradeStatus tradeStatus;
    private AlipayTradeRefundResponse response;

    public AlipayF2FRefundResult(AlipayTradeRefundResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public AlipayTradeRefundResponse getResponse() {
        return this.response;
    }

    public void setResponse(AlipayTradeRefundResponse response) {
        this.response = response;
    }

    public boolean isTradeSuccess() {
        return (this.response != null) && (TradeStatus.SUCCESS.equals(this.tradeStatus));
    }
}
