package com.wxpay.refund;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.util.JacksonUtil;

public class WxRefundXmlBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(WxRefundXmlBuilder.class);

	private String refund_fee;

	private String total_fee;

	private String transaction_id;

	public WxRefundXmlBuilder(String refund_fee, String total_fee, String transaction_id) {
		this.refund_fee = refund_fee;
		this.total_fee = total_fee;
		this.transaction_id = transaction_id;
	}

	@SuppressWarnings("unchecked")
	public String getRefundXml() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("xml");
		WxRefundRequest request = signedRequest();
		Map<String, String> map = new HashMap<String, String>();
		try {
			// BeanUtils.copyProperties(map, request);
			map = JacksonUtil.readValue(JacksonUtil.writeValueAsString(request), map.getClass());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		for (String key : list) {
			Element element = root.addElement(key);
			element.setText(map.get(key));
		}

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);
		try {
			xw.write(doc);
			xw.flush();
			LOGGER.info("pretty format:\n{}", sw.toString());
		} catch (IOException e) {
			LOGGER.error("", e);
		} finally {
			try {
				xw.close();
			} catch (IOException e) {
			}
		}

		return doc.getRootElement().asXML();
	}

	private WxRefundRequest unsignedRequest() {
		WxRefundRequest request = new WxRefundRequest();
		request.setAppid(WxRefundConfig.APPID);
		request.setMch_id(WxRefundConfig.MCH_ID);
		request.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		request.setOp_user_id(WxRefundConfig.MCH_ID);
		request.setOut_refund_no(UUID.randomUUID().toString().replace("-", ""));
		request.setRefund_fee(refund_fee);
		request.setTotal_fee(total_fee);
		request.setTransaction_id(transaction_id);
		return request;
	}

	@SuppressWarnings("unchecked")
	private WxRefundRequest signedRequest() {
		try {
			WxRefundRequest request = unsignedRequest();
			Map<String, String> map = new HashMap<String, String>();
			// BeanUtils.copyProperties(map, unsignedRequest());
			map = JacksonUtil.readValue(JacksonUtil.writeValueAsString(request), map.getClass());
			request.setSign(WxRefundCore.getSign(map));
			return request;
		} catch (Exception e) {
			LOGGER.error("", e);
			return null;
		}
	}

}
