package com.wxap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wxap.client.TenpayHttpClient;
import com.wxap.util.MD5Util;
import com.wxap.util.TenpayUtil;

/*
 '΢��֧��������ǩ��֧������������
 '============================================================================
 'api˵����
 'init(app_id, app_secret, partner_key, app_key);
 '��ʼ������Ĭ�ϸ�һЩ����ֵ����cmdno,date�ȡ�
 'setKey(key_)'�����̻���Կ
 'getLasterrCode(),��ȡ�������
 'GetToken();��ȡToken
 'getTokenReal();Token���ں�ʵʱ��ȡToken
 'createMd5Sign(signParams);���Md5ǩ��
 'genPackage(packageParams);��ȡpackage��
 'createSHA1Sign(signParams);����ǩ��SHA1
 'sendPrepay(packageParams);�ύԤ֧��
 'getDebugInfo(),��ȡdebug��Ϣ
 '============================================================================
 '*/
@SuppressWarnings(value={"unchecked", "unused"})
public class RequestHandler {
	/** Token��ȡ��ص�ַ��ַ */
	private String tokenUrl;
	/** Ԥ֧�����url��ַ */
	private String gateUrl;
	/** ��ѯ֧��֪ͨ���URL */
	private String notifyUrl;
	/** �̻����� */
	private String appid;
	private String appkey;
	private String partnerkey;
	private String appsecret;
	private String key;
	/** ����Ĳ��� */
	private SortedMap<String, String> parameters;
	/** Token */
	private String Token;
	private String charset;
	/** debug��Ϣ */
	private String debugInfo;
	private String last_errcode;

	private HttpServletRequest request;

	private HttpServletResponse response;

	/**
	 * ��ʼ���캯��
	 * 
	 * @return
	 */
	public RequestHandler(HttpServletRequest request,
			HttpServletResponse response) {
		this.last_errcode = "0";
		this.request = request;
		this.response = response;
		this.charset = "GBK";
		this.parameters = new TreeMap<String, String>();
		// ��ȡToken���
		tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
		// �ύԤ֧�������
		gateUrl = "https://api.weixin.qq.com/pay/genprepay";
		// ��֤notify֧���������
		notifyUrl = "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml";
	}

	/**
	 * ��ʼ������
	 */
	public void init(String app_id, String app_secret, String app_key,
			String partner, String key) {
		this.last_errcode = "0";
		this.Token = "token_";
		this.debugInfo = "";
		this.appkey = app_key;
		this.appid = app_id;
		this.partnerkey = partner;
		this.appsecret = app_secret;
		this.key = key;
	}

	public void init() {
	}

	/**
	 * ��ȡ�������
	 */
	public String getLasterrCode() {
		return last_errcode;
	}

	/**
	 *��ȡ��ڵ�ַ,�������ֵ
	 */
	public String getGateUrl() {
		return gateUrl;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param parameter
	 *            �������
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * ������Կ
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡTOKEN��һ������ȡ200�Σ���Ҫ�����û�����ֵ
	 */
	public String GetToken() {
		String requestUrl = tokenUrl + "?grant_type=client_credential&appid="
				+ appid + "&secret=" + appsecret;
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		if (httpClient.call()) {
			String res = httpClient.getResContent();
			Gson gson = new Gson();
			TreeMap<String, String> map = gson.fromJson(res, TreeMap.class);
			// ����Ч����ֱ�ӷ���access_token
			if (map.containsKey("access_token")) {
				String s = map.get("access_token").toString();
			}
			// �������ɹ�
			if (null != map) {
				try {
					if (map.containsKey("access_token")) {
						Token = map.get("access_token").toString();
						return this.Token;
					}
				} catch (Exception e) {
					// ��ȡtokenʧ��
					System.out.println("ʧ��:" + map.get("errmsg"));
				}
			}

		}
		return "";

	}

	/**
	 * ʵʱ��ȡtoken�������µ�application��
	 */
	public String getTokenReal() {
		String requestUrl = tokenUrl + "?grant_type=client_credential&appid="
				+ appid + "&secret=" + appsecret;
		try {
			// �������󣬷���json
			TenpayHttpClient httpClient = new TenpayHttpClient();
			httpClient.setReqContent(requestUrl);
			String resContent = "";
			if (httpClient.callHttpPost(requestUrl, "")) {
				resContent = httpClient.getResContent();
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(resContent,
						new TypeToken<Map<String, String>>() {
						}.getType());
				// �жϷ����Ƿ���access_token
				if (map.containsKey("access_token")) {
					// ����applicationֵ
					Token = map.get("access_token");
				} else {
					System.out.println("get token err ,info ="
							+ map.get("errmsg"));
				}
				System.out.println("res json=" + resContent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Token;
	}

	// �����ַ���
	public String UrlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, this.charset).replace("+", "%20");
	}

	// ��ȡpackage������ǩ���
	public String genPackage(SortedMap<String, String> packageParams)
			throws UnsupportedEncodingException {
		String sign = createSign(packageParams);

		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			sb.append(k + "=" + UrlEncode(v) + "&");
		}

		// ȥ�����һ��&
		String packageValue = sb.append("sign=" + sign).toString();
		System.out.println("packageValue=" + packageValue);
		return packageValue;
	}

	/**
	 * ����md5ժҪ,������:���������a-z����,������ֵ�Ĳ���μ�ǩ��
	 */
	public String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + this.getKey());
		System.out.println("md5 sb:" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(), this.charset)
				.toUpperCase();

		return sign;

	}

	// �ύԤ֧��
	public String sendPrepay(SortedMap<String, String> packageParams) {
		String prepayid = "";
		// ת����json
		Gson gson = new Gson();
		/* String postData =gson.toJson(packageParams); */
		String postData = "{";
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (k != "appkey") {
				if (postData.length() > 1)
					postData += ",";
				postData += "\"" + k + "\":\"" + v + "\"";
			}
		}
		postData += "}";
		// �������Ӳ���
		String requestUrl = this.gateUrl + "?access_token=" + this.Token;
		System.out.println("post url=" + requestUrl);
		System.out.println("post data=" + postData);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		String resContent = "";
		if (httpClient.callHttpPost(requestUrl, postData)) {
			resContent = httpClient.getResContent();
			Map<String, String> map = gson.fromJson(resContent,
					new TypeToken<Map<String, String>>() {
					}.getType());
			if ("0".equals(map.get("errcode"))) {
				prepayid = map.get("prepayid");
			} else {
				System.out.println("get token err ,info =" + map.get("errmsg"));
			}
			// ����debug info
			System.out.println("res json=" + resContent);
		}
		return prepayid;
	}

	/**
	 * ����packageǩ��
	 */
	public boolean createMd5Sign(String signParams) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = this.parameters.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}

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
	 * ����debug��Ϣ
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	public void setPartnerkey(String partnerkey) {
		this.partnerkey = partnerkey;
	}
	public String getDebugInfo() {
		return debugInfo;
	}
	public String getKey() {
		return key;
	}

}
