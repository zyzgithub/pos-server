package com.alipay.refund;

import java.util.Collection;

/**
 * 即时到账批量退款 表示交易的对象
 */
public class FastpayRefundTrade extends FastpayRefundConnectedData {

	/**
	 * 连接字符串
	 */
	private static final String CARET = "|";
	
	/**
	 * 交易数据集集合
	 */
	private Collection<? extends FastpayRefundDataset> fastpayRefundDatasets;
	
	public FastpayRefundTrade(Collection<? extends FastpayRefundDataset> fastpayRefundDatasets) {
		this.fastpayRefundDatasets = fastpayRefundDatasets;
	}

	/**
	 * 返回待连接的数据，本类返回的就是 交易退款数据 集合。子类可覆盖本方法返回其他实现{@link com.alipay.refund.FastpayRefundDataset}接口的集合
	 * @see com.alipay.refund.FastpayRefundConnectedData#getConnectedData()
	 */
	@Override
	protected Collection<? extends FastpayRefundDataset> getConnectedData() {
		return fastpayRefundDatasets;
	}

	@Override
	protected String getConnector() {
		return CARET;
	}
	
}
