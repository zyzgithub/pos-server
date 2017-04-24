package com.alipay.refund;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 即时到账批量退款 抽象的可连接数据
 */
public abstract class FastpayRefundConnectedData implements FastpayRefundFormattedData {

	private static final Logger LOGGER = LoggerFactory.getLogger(FastpayRefundConnectedData.class);

	/**
	 * 返回待连接的数据，待连接数据必须也实现了{@link com.alipay.refund.FastpayRefundFormattedData}接口
	 * @return 待连接的数据
	 */
	protected abstract Collection<? extends FastpayRefundFormattedData> getConnectedData();
	
	/**
	 * 返回连接字符串
	 * @return 连接字符串
	 */
	protected abstract String getConnector();

	@Override
	public boolean validate() {
		if (CollectionUtils.isEmpty(getConnectedData())) {
			LOGGER.info("{} data is empty", getClass().getName());
			return false;
		}
		if (getConnectedData().contains(null)) {
			LOGGER.info("{} data contains null", getClass().getName());
			return false;
		}
		for (FastpayRefundFormattedData fastpayRefundDataFormat : getConnectedData()) {
			if (!fastpayRefundDataFormat.validate()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String format() {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<? extends FastpayRefundFormattedData> iterator = getConnectedData().iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().format()).append(getConnector());
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		return stringBuilder.toString();
	}

}
