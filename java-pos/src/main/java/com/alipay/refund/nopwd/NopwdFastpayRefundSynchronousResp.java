package com.alipay.refund.nopwd;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 即时到账批量退款无密接口 同步返回参数
 */
class NopwdFastpayRefundSynchronousResp {

	/**
	 * 请求成功标志
	 */
	public static final String SUCCESS = "T";

	/**
	 * 请求失败标志
	 */
	public static final String FAIL = "F";

	/**
	 * 请求处理中标志
	 */
	public static final String PROCESS = "P";

	/**
	 * 是否成功
	 */
	private String isSuccess;

	/**
	 * 错误码
	 */
	private String error;
	
	private NopwdFastpayRefundSynchronousResp(String isSuccess, String error) {
		this.isSuccess = isSuccess;
		this.error = error;
	}

	/**
	 * 从xml字符串中解析 即时到账批量退款无密接口 同步返回参数
	 * @param xml xml字符串
	 * @return 即时到账批量退款无密接口 同步返回参数
	 * @throws DocumentException xml解析异常
	 */
	public static NopwdFastpayRefundSynchronousResp fromXml(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		String isSuccess = elementText(root.element("is_success"));
		String error = elementText(root.element("error"));
		return new NopwdFastpayRefundSynchronousResp(isSuccess, error);
	}

	/**
	 * 读取Element的文本内容，如果Element为空或内容为空则返回null
	 * @param element Element对象
	 * @return 文本内容，可能为null
	 */
	private static String elementText(Element element) {
		if (element == null) {
			return null;
		}
		String text = element.getText();
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		return text;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public String getError() {
		return error;
	}

}
