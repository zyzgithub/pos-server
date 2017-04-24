package com.alipay.refund;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 * 即时到账批量退款 单笔数据集
 */
public class FastpayRefundBatchSet extends FastpayRefundConnectedData {

	/**
	 * 连接符，值为{@value}
	 */
	private static final String CARET = "#";
	
	/**
	 * 一笔数据集最多为{@value}笔交易
	 */
	private static final int MAX_BATCH = 1000;

	/**
	 * 单笔数据集包含的多笔交易
	 */
	private Collection<? extends FastpayRefundTrade> fastpayRefundTrades;

	public FastpayRefundBatchSet(Collection<? extends FastpayRefundTrade> fastpayRefundTrades) {
		this.fastpayRefundTrades = fastpayRefundTrades;
	}

	/**
	 * 返回待连接的数据，本类返回的就是 交易 集合。子类可覆盖本方法返回其他实现{@link com.alipay.refund.FastpayRefundTrade}接口的集合
	 * @see com.alipay.refund.FastpayRefundConnectedData#getConnectedData()
	 */
	@Override
	protected Collection<? extends FastpayRefundTrade> getConnectedData() {
		return fastpayRefundTrades;
	}

	@Override
	protected String getConnector() {
		return CARET;
	}
	
	/**
	 * 添加额外的判断，交易笔数是否超过限制 
	 * @see com.alipay.refund.FastpayRefundConnectedData#validate()
	 */
	@Override
	public boolean validate() {
		if (CollectionUtils.isEmpty(getConnectedData()) || getConnectedData().size() > MAX_BATCH) {
			return false;
		}
		return super.validate();
	}

}
