package com.alipay.refund.nopwd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alipay.refund.FastpayRefundParam;
import com.alipay.refund.Signable;
import com.alipay.refund.util.FastpayRefundBatchNoHelper;
import com.alipay.refund.util.FastpayRefundSignHelper;
import com.base.config.AlipayConfig;
import com.base.config.EnvConfig;
import com.wm.util.HttpClientUtil;

/**
 * 支付宝即时到账批量退款无密接口实现类（适用于移动支付）<br>
 * 参考官方文档:<br>
 * <a>http://wiki.0085.com:8090/pages/viewpage.action?pageId=2719776</a>
 */
@Component("nopwdFastpayRefundAction")
public class NopwdFastpayRefundActionImpl implements NopwdFastpayRefundAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(NopwdFastpayRefundActionImpl.class);

	/**
	 * 接口网关
	 */
	private static final String GATEWAY = "https://mapi.alipay.com/gateway.do";

	/**
	 * 异步通知校验service
	 */
	private static final String NOTIFY_VERIFY_SERVICE = "notify_verify";

	/**
	 * 校验异步通知notify_id时，应该得到的成功响应，否则可认为notify_id不合法
	 */
	private static final String NOTIFY_ID_VALIDATED_RESP = "true";

	@Override
	public NopwdFastpayRefundApplyResult apply(FastpayRefundParam... fastpayRefundParams) {
		String batchNo = FastpayRefundBatchNoHelper.buildBatchNo();
		try {
			Signable signableReq = new NopwdFastpayRefundReq(batchNo, fastpayRefundParams);
			Map<String, String> param = signableReq.sign();
			return new NopwdFastpayRefundApplyResult(postRefundRequest(param), batchNo);
		} catch (Exception e) {
			LOGGER.info("", e);
		}
		return NopwdFastpayRefundApplyResult.failResult(batchNo);
	}

	/**
	 * 发送退款请求
	 * @param signedParamMap 经过签名的退款请求参数map
	 * @return 是否请求成功
	 */
	private boolean postRefundRequest(Map<String, String> signedParamMap) {
		LOGGER.info("post nopwdFastpayRefund signed request: {}", signedParamMap);
		HttpPost httpPost = new HttpPost(GATEWAY);
		try {
			httpPost.setEntity(refundParamEntity(signedParamMap));
			String entityString = HttpClientUtil.execute(httpPost, AlipayConfig.input_charset);
			NopwdFastpayRefundSynchronousResp synchronousResp = NopwdFastpayRefundSynchronousResp.fromXml(entityString);
			return validateSynchronousResp(synchronousResp);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("unsupported charset [{}]", AlipayConfig.input_charset);
		} catch (IOException e) {
			LOGGER.error("IOException while posting refund request", e);
			httpPost.abort();
		} catch (DocumentException e) {
			LOGGER.error("can't convert response to dom", e);
		}
		return false;
	}

	/**
	 * 用退款请求参数map构建HttpEntity
	 * @param signedParamMap 请求参数map
	 * @return HttpEntity对象
	 * @throws UnsupportedEncodingException 编码不支持
	 */
	private HttpEntity refundParamEntity(Map<String, String> signedParamMap) throws UnsupportedEncodingException {
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : signedParamMap.entrySet()) {
			pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return new UrlEncodedFormEntity(pairList, AlipayConfig.input_charset);
	}

	/**
	 * 根据同步返回参数判断退款请求是否成功
	 * @param synchronousResp
	 * @return 退款请求是否成功
	 */
	private boolean validateSynchronousResp(NopwdFastpayRefundSynchronousResp synchronousResp) {
		if (!NopwdFastpayRefundSynchronousResp.SUCCESS.equals(synchronousResp.getIsSuccess())
				&& !NopwdFastpayRefundSynchronousResp.PROCESS.equals(synchronousResp.getIsSuccess())) {
			LOGGER.info("read error from NopwdFastpayRefundSynchronousResp: {}", synchronousResp.getError());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean verifyAsynchronousNotify(NopwdFastpayRefundAsynchronousNotify notify) {
		boolean notifyIdValidated = verifyNotifyId(notify.getNotify_id());
		LOGGER.info("verify notify_id: {}", notifyIdValidated);
		if (!notifyIdValidated) {
			return false;
		}
		boolean notifySignValidated = verifyNotifySign(notify);
		LOGGER.info("verify sign: {}", notifySignValidated);
		return notifySignValidated;
	}

	/**
	 * 校验异步通知的notify_id
	 * @param notifyId 异步通知的notify_id参数
	 * @return notify_id是否合法
	 */
	private boolean verifyNotifyId(String notifyId) {
		LOGGER.info("post verify request for notifyId: {}", notifyId);
		try {
			HttpGet httpGet = new HttpGet(buildVerifyNotifyUrl(notifyId));
			String response = HttpClientUtil.execute(httpGet, AlipayConfig.input_charset);
			return NOTIFY_ID_VALIDATED_RESP.equals(response);
		} catch (IOException e) {
			LOGGER.error("fail to verify notify_id. ", e);
		}
		return false;
	}

	/**
	 * 构建校验异步通知的URL，详见支付宝官方文档
	 * @param notifyId 需要校验的notify_id
	 * @return 用于校验的URL
	 */
	private String buildVerifyNotifyUrl(String notifyId) {
		StringBuilder stringBuilder = new StringBuilder(GATEWAY);
		stringBuilder.append("?service=").append(NOTIFY_VERIFY_SERVICE);
		stringBuilder.append("&partner=").append(EnvConfig.alipay.partner);
		stringBuilder.append("&notify_id=").append(notifyId);
		return stringBuilder.toString();
	}

	/**
	 * 校验异步通知的签名
	 * @param notify 即时到账批量退款异步通知对象
	 * @return 签名是否合法
	 */
	private boolean verifyNotifySign(NopwdFastpayRefundAsynchronousNotify notify) {
		Map<String, String> paramMap = notify.toParamMap();
		LOGGER.info("paramMap of notify sign be about to verify: {}", paramMap);
		return FastpayRefundSignHelper.verify(paramMap, notify.getSign());
	}

}
