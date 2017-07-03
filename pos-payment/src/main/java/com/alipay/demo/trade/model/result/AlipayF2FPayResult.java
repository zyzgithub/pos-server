package com.alipay.demo.trade.model.result;

import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.demo.trade.model.TradeStatus;

public class AlipayF2FPayResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradePayResponse response;

    public AlipayF2FPayResult(AlipayTradePayResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public AlipayTradePayResponse getResponse() {
        return this.response;
    }

    public void setResponse(AlipayTradePayResponse response) {
        this.response = response;
    }

    public boolean isTradeSuccess() {
        return (this.response != null) && (TradeStatus.SUCCESS.equals(this.tradeStatus));
    }
}
