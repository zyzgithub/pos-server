package com.wxap;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;

import com.wxap.util.MD5Util;
import com.wxap.util.TenpayUtil;
import com.wxap.util.XMLUtil;


/**
 * ΢��֧��������ǩ��֧������Ӧ����
 * api˵���� 
 *  getKey()/setKey(),��ȡ/������Կ
 *  getParameter()/setParameter(),��ȡ/���ò���ֵ getAllParameters(),��ȡ���в���
 *  isTenpaySign(),�Ƿ�Ƹ�ͨǩ��,true:�� false:��
 *   getDebugInfo(),��ȡdebug��Ϣ
 */
public class ResponseHandler {

	/** ��Կ */
	private String key;

	/** Ӧ��Ĳ��� */
	private SortedMap<String, String> parameters;

	/** debug��Ϣ */
	private String debugInfo;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String uriEncoding;

	/**
	 * ���캯��
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.key = "";
		this.parameters = new TreeMap<String, String>();
		this.debugInfo = "";

		this.uriEncoding = "";

		Map<String,String[]> m = this.request.getParameterMap();
		Iterator<String> it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = it.next();
			String v = m.get(k)[0];
			this.setParameter(k, v);
		}

	}

	/**
	 *��ȡ��Կ
	 */
	public String getKey() {
		return key;
	}

	/**
	 *������Կ
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param parameter
	 *            �������
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * ���ò���ֵ
	 * 
	 * @param parameter
	 *            �������
	 * @param parameterValue
	 *            ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * �������еĲ���
	 * 
	 * @return SortedMap
	 */
	public SortedMap<String, String> getAllParameters() {
		return this.parameters;
	}
	public void doParse(String xmlContent) throws JDOMException, IOException {
		this.parameters.clear();
		//����xml,�õ�map
		Map<String, String> m = XMLUtil.doXMLParse(xmlContent);
		
		//���ò���
		Iterator<String> it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = it.next();
			String v = m.get(k);
			this.setParameter(k, v);
		}
	}
	/**
	 * �Ƿ�Ƹ�ͨǩ��,������:���������a-z����,������ֵ�Ĳ���μ�ǩ��
	 * 
	 * @return boolean
	 */
	public boolean isTenpaySign() {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = this.parameters.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				// if(!k.equals("PcacheTime")){
				sb.append(k + "=" + v + "&");
				// }
			}
		}

		sb.append("key=" + this.getKey());

		// ���ժҪ
		String enc = TenpayUtil.getCharacterEncoding(this.request,
				this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

		String tenpaySign = this.getParameter("sign").toLowerCase();

		// debug��Ϣ
		this.setDebugInfo(sb.toString() + " => sign:" + sign + " tenpaySign:"
				+ tenpaySign);

		return tenpaySign.equals(sign);
	}

	/**
	 * ���ش������Ƹ�ͨ��������
	 * 
	 * @param msg
	 *            : Success or fail��
	 * @throws IOException
	 */
	public void sendToCFT(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}

	/**
	 * ��ȡuri����
	 * 
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * ����uri����
	 * 
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setUriEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.uriEncoding = uriEncoding;

			// ����ת��
			String enc = TenpayUtil.getCharacterEncoding(request, response);
			Iterator<String> it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}

	/**
	 *��ȡdebug��Ϣ
	 */
	public String getDebugInfo() {
		return debugInfo;
	}
	/**
	 *����debug��Ϣ
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}

	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}

}
