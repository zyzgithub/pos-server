package com.alipay.refund.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alipay.sign.RSA;
import com.base.config.AlipayConfig;
import com.base.config.EnvConfig;

/**
 * 即时到账批量退款接口 签名工具
 */
public class FastpayRefundSignHelper {

	/**
	 * 签名在参数map中的key，值为{@value}
	 */
	private static final String SIGN_KEY = "sign";

	/**
	 * 签名类型在参数map中的key，值为{@value}
	 */
	private static final String SIGN_TYPE_KEY = "sign_type";

	/**
	 * 需过滤的待签名map参数的key数组，值为{@value}
	 */
	private static final String[] FILTER_KEYS = { SIGN_KEY, SIGN_TYPE_KEY };

	/**
	 * 支持的签名类型，值为{@value}
	 */
	private static final String SUPPORTED_RSA_TYPE = "RSA";

	/**
	 * 对待签名的参数map进行过滤并计算签名，返回签名后的参数map
	 * @param unsignedParam 待签名的参数map
	 * @return 签名后的参数map
	 */
	public static Map<String, String> sign(Map<String, String> unsignedParam) {
		if (!supportSignType(unsignedParam.get(SIGN_TYPE_KEY))) {
			throw new RuntimeException("unsupported sign_type");
		}

		Map<String, String> filteredUnsignedParam = filterParam(unsignedParam);
		String linkString = createLinkString(filteredUnsignedParam);
		String sign = RSA.sign(linkString, EnvConfig.alipay.rsaPrivate, AlipayConfig.input_charset);

		Map<String, String> signedParam = filteredUnsignedParam;
		signedParam.put(SIGN_KEY, sign);
		signedParam.put(SIGN_TYPE_KEY, unsignedParam.get(SIGN_TYPE_KEY));
		return signedParam;
	}
	
	/**
	 * 判断签名类型是否支持
	 * @param signType 签名类型
	 * @return 是否支持
	 */
	private static boolean supportSignType(String signType) {
		return SUPPORTED_RSA_TYPE.equals(signType);
	}

	/**
	 * 过滤参数map，去掉无用的参数
	 * @param unfilteredMap 待过滤的参数map
	 * @return 过滤后的参数map
	 */
	private static Map<String, String> filterParam(Map<String, String> unfilteredMap) {
		Map<String, String> result = new HashMap<String, String>();
		if (unfilteredMap == null || unfilteredMap.size() == 0) {
			return result;
		}
		for (Entry<String, String> entry : unfilteredMap.entrySet()) {
			if (StringUtils.isEmpty(entry.getKey()) || StringUtils.isEmpty(entry.getValue())
					|| equalsFilterKeys(entry.getKey())) {
				continue;
			}
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 判断key是否需要过滤
	 * @param key key
	 * @return 是否需要过滤
	 */
	private static boolean equalsFilterKeys(String key) {
		for (String s : FILTER_KEYS) {
			if (s.equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 生成用于计算签名的字符串
	 * @param param 过滤后的参数map
	 * @return 用于计算签名的字符串
	 */
	private static String createLinkString(Map<String, String> param) {
		List<String> keys = new ArrayList<String>(param.keySet());
		Collections.sort(keys);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			stringBuilder.append(key).append("=").append(param.get(key));
			if (i < keys.size() - 1) {
				stringBuilder.append("&");
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 判断参数map的签名是否合法
	 * @param totalParam 总参数map，过滤后才计算签名值
	 * @param sign 要比较的签名值
	 * @return 签名是否合法
	 */
	public static boolean verify(Map<String, String> totalParam, String sign) {
		if (!supportSignType(totalParam.get(SIGN_TYPE_KEY))) {
			throw new RuntimeException("unsupported sign_type");
		}
		Map<String, String> filteredParam = filterParam(totalParam);
		String linkString = createLinkString(filteredParam);
		return RSA.verify(linkString, sign, AlipayConfig.ali_public_key, AlipayConfig.input_charset);
	}
	
}
