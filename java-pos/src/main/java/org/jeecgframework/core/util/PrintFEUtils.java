package org.jeecgframework.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 飞蛾云打印机
 * @author Simon
 */
@SuppressWarnings("deprecation")
public class PrintFEUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(PrintFEUtils.class);
	
	// PRINTER_SN：打印机编号9位,查看飞鹅打印机底部贴纸上面的打印机编号
	public static final String PRINTER_SN = "915800477";
	// KEY：去飞鹅打印机官方网站 www.feieyun.com 注册帐号，添加打印机编号，自动生成KEY
	public static final String KEY = "JB3sSq5k";

	public static final String IP = "http://dzp.feieyun.com";
	public static final String HOSTNAME = "/FeieServer";

	/**
	 * @param printCode 小票机编号,多台以逗号隔开，例如：915800477|JB3sSq5k,915800477|JB3sSq5k
	 * @param pTimes 打印联数
	 * @param content 打印内容
	 * @return
	 */
	@SuppressWarnings({ "resource" })
	public static String print(String printCode, String pTimes, String content) {
		logger.info("printCode:{}, printTimes:", printCode, pTimes);
		logger.debug("content:{}", content);
		String retStr = null;
		if(StringUtil.isNotEmpty(printCode)){
			String[] pCodes = printCode.split(",");
			for(String pCode : pCodes){
				String printParams[] = pCode.split("\\|");
				String sn = printParams[0];
				String strkey = printParams[1];
				
				// 标签说明："<BR>"为换行符,"<CB></CB>"为居中放大,"<B></B>"为放大,"<C></C>"为居中,<L></L>字体变高
				// <W></W>字体变宽,"<QR></QR>"为二维码,"<CODE>"为条形码,后面接12个数字
				// 通过POST请求，发送打印信息到服务器
				HttpPost post = new HttpPost(IP + HOSTNAME + "/printOrderAction");
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("sn", sn));
				nvps.add(new BasicNameValuePair("printContent", content));
				nvps.add(new BasicNameValuePair("key", strkey));
				nvps.add(new BasicNameValuePair("times", pTimes));

				InputStream is = null;
				try {
					post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
					HttpResponse response = client.execute(post);
					int statecode = response.getStatusLine().getStatusCode();
					if (statecode == 200) {
						HttpEntity httpentity = response.getEntity();
						String strentity = null;
						if (httpentity != null) {
							is = httpentity.getContent();
							byte[] b = new byte[1024];
							int length = 0;
							StringBuilder sb = new StringBuilder();
							while ((length = is.read(b)) != -1) {
								// sb.append(new String(b,0,length));
								// 如有乱码，请用下面这个句代码
								sb.append(new String(b, 0, length, "utf-8"));
							}
							strentity = sb.toString();
							logger.info("printCode:{} 打印成功，strentity:{}", printCode, strentity);
//							return strentity;
							retStr += strentity;
						} else {
							logger.error("printCode:{} 打印失败，httpentity is null ", printCode);
							return null;
						}
					} else {
						logger.error("printCode:{} 打印失败，statecode:{}", printCode, statecode);
						return null;
					}
				} catch (Exception e) {
					logger.error("打印失败 : " + e.getMessage());
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							logger.error("close 失败 : " + e.getMessage());
						}
					}
					if (post != null) {
						post.abort();
					}
				}
			}
		} else {
			logger.error("printCode:{} connot be null !!! ", printCode);
		}
		return retStr;
	}
	
	public static void main(String arg[]){
		String content;
		content = "<CB>测试打印</CB><BR>";
		content += "名称　　　　　 单价  数量 金额<BR>";
		content += "--------------------------------<BR>";
		content += "饭　　　　　　 1.0    1   1.0<BR>";
		content += "炒饭　　　　　 10.0   10  10.0<BR>";
		content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
		content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
		content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
		content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
		content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
		content += "备注：加辣<BR>";
		content += "--------------------------------<BR>";
		content += "合计：xx.0元<BR>";
		content += "送货地点：广州市南沙区xx路xx号<BR>";
		content += "联系电话：13888888888888<BR>";
		content += "订餐时间：2014-08-08 08:08:08<BR>";
		content += "<QR>http://www.dzist.com</QR>";
		System.out.println(content);
//		PrintFEUtils.print(PRINTER_SN + "|" + KEY, "1", content);
		PrintFEUtils.print("815800445|0RBq9e4S,815800445|0RBq9e4S", "1", content);
	}
}
