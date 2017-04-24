package com.wxpay.refund;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.uno.RuntimeException;

public class WxRefundResponseParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WxRefundResponseParser.class);

	@SuppressWarnings("unchecked")
	public WxRefundResponse parse(String refundString) {
		try {
			Document doc = DocumentHelper.parseText(refundString);
			Element root = doc.getRootElement();
			if (!"xml".equalsIgnoreCase(root.getName())) {
				throw new RuntimeException("refund string is without xml element");
			}
			List<Element> elements = root.elements();
			Map<String,String> map = new HashMap<String, String>();
			for (Element element : elements) {
				map.put(element.getName(), element.getText());
			}
			WxRefundResponse refundResponse = new WxRefundResponse();
			BeanUtils.populate(refundResponse, map);
			return refundResponse;
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return null;
	}
	
}
