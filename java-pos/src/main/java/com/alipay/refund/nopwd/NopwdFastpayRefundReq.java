package com.alipay.refund.nopwd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.alipay.refund.FastpayRefundBatchSet;
import com.alipay.refund.FastpayRefundDataset;
import com.alipay.refund.FastpayRefundParam;
import com.alipay.refund.FastpayRefundTrade;
import com.alipay.refund.InvalidatedException;
import com.alipay.refund.Signable;
import com.alipay.refund.util.FastpayRefundSignHelper;
import com.base.config.AlipayConfig;
import com.base.config.EnvConfig;

/**
 * 即时到账批量退款无密接口 退款请求，详细参数说明参考支付宝官方文档
 */
public class NopwdFastpayRefundReq implements Signable {

	/**
	 * refund_date（退款请求时间）的日期格式
	 */
	private static final String REFUND_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 退款接口名称
	 */
	private static final String SERVICE = "refund_fastpay_by_platform_nopwd";

	/**
	 * 合作者身份id
	 */
	private static final String PARTNER = EnvConfig.alipay.partner;

	/**
	 * 参数编码字符集
	 */
	private static final String INPUT_CHARSET = AlipayConfig.input_charset;

	/**
	 * 签名类型
	 */
	private static final String SIGN_TYPE = "RSA";

	/**
	 * 服务器异步通知页面路径，此路径不允许携带参数（如&key=value）
	 */
	private static final String NOTIFY_URL = EnvConfig.alipay.nopwdFastpayRefundNotifyUrl;

	/**
	 * 退款批次号。每进行一次即时到账批量退款，都需要提供一个批次号，通过该批次号可以查询这一批次的退款交易记录。对于每一个合作伙伴，传递的每一个批次号都必须保证唯一性。
	 * 格式为：退款日期（8 位当天日期）+流水号（3～24 位，流水号可以接受数字或英文字符，建议使用数字，但不可接受“000”）。
	 * 可用{@link com.alipay.refund.util.FastpayRefundBatchNoHelper}生成
	 */
	private String batchNo;

	/**
	 * 退款请求的当前时间。格式为：yyyy-MM-ddhh:mm:ss。
	 */
	private String refundDate;

	/**
	 * 即参数 detail_data 的值中，“#”字符出现的数量加 1，最大支持 1000 笔（即“#”字符出现的最大数量 999个）。
	 */
	private String batchNum;

	/**
	 * 单笔数据集。单笔数据集格式为：第一笔交易#第二笔交易#第三笔交易...#第 N 笔交易。
	 * 
	 * 其中每一笔交易按退交易、退分润、退子交易的组合来划分，有以下 4 种格式：
	 * –  交易退款数据集|分润退款数据集 1|分润退款数据集 2|...|分润退款数据集 N
	 * –  交易退款数据集|分润退款数据集 1|分润退款数据集 2|...|分润退款数据集 N$$退子交易
	 * –  交易退款数据集–  交易退款数据集$$退子交易
	 * 
	 * 交易退款数据集格式为：原付款支付宝交易号^退款总金额^退款理由。
	 */
	private String detailData;

	/**
	 * 申请结果返回类型
	 */
	private static final String RETURN_TYPE = "xml";

	/**
	 * 构造函数
	 * @param batchNo {@link NopwdFastpayRefundReq#batchNo}
	 * @param fastpayRefundParams 即时到账批量退款-请求退款的参数
	 */
	public NopwdFastpayRefundReq(String batchNo, FastpayRefundParam... fastpayRefundParams) {
		FastpayRefundBatchSet fastpayRefundBatchSet = toFastpayRefundBatchSet(fastpayRefundParams);
		if (!fastpayRefundBatchSet.validate()) {
			throw new InvalidatedException("fastpayRefundBatchSet is invalidated");
		}
		this.batchNo = batchNo;
		refundDate = DateFormatUtils.format(System.currentTimeMillis(), REFUND_DATE_PATTERN);
		batchNum = String.valueOf(fastpayRefundParams.length);
		detailData = fastpayRefundBatchSet.format();
	}

	/**
	 * 创建单笔数据集（可能包含多个交易，每个交易可能包含一组详细子集）
	 * @param fastpayRefundParams 即时到账批量退款 数据对象
	 * @return 单笔数据集
	 */
	private FastpayRefundBatchSet toFastpayRefundBatchSet(FastpayRefundParam... fastpayRefundParams) {
		ArrayList<FastpayRefundDataset> datasetList = new ArrayList<FastpayRefundDataset>(
				fastpayRefundParams.length);
		for (FastpayRefundParam fastpayRefundData : fastpayRefundParams) {
			datasetList.add(new FastpayRefundDataset(fastpayRefundData));
		}
		List<FastpayRefundTrade> tradeList = Arrays.asList(new FastpayRefundTrade(datasetList));
		return new FastpayRefundBatchSet(tradeList);
	}
	
	@Override
	public Map<String, String> sign() {
		return FastpayRefundSignHelper.sign(getUnsignedParam());
	}

	/**
	 * 返回待签名的参数map
	 * @return 待签名的参数map
	 */
	private Map<String, String> getUnsignedParam() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", SERVICE);
		param.put("partner", PARTNER);
		param.put("_input_charset", INPUT_CHARSET);
		param.put("sign_type", SIGN_TYPE);
		param.put("notify_url", NOTIFY_URL);
		param.put("batch_no", batchNo);
		param.put("refund_date", refundDate);
		param.put("batch_num", batchNum);
		param.put("detail_data", detailData);
		param.put("return_type", RETURN_TYPE);
		return param;
	}

}
