package com.dianba.pos.casher.util;

import com.dianba.pos.casher.vo.Charge_19E;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.common.util.Md5Util;
import org.apache.commons.codec.digest.DigestUtils;

import sun.security.provider.MD5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
@SuppressWarnings("all")
public class Charge19EApi {

    public  static String hfCharge(String chargeUrl, Charge_19E param){
        String sign=   param.sign();
        String MD5= Md5Util.HEXAndMd5(sign).toUpperCase();
        PrintWriter out = null;
        BufferedReader in = null;
        String result="";
        String tojson="";
        try {
            URL realUrl = new URL(chargeUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
           // conn.setRequestProperty("accept", "*/*");
          //  conn.setRequestProperty("connection", "Keep-Alive");
          //  conn.setRequestProperty("user-agent",
            //        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String aa=param.params(MD5);
            out.print(aa);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
          String  urlStr = URLDecoder.decode(result, "UTF-8");

             tojson=Charge19EUtil.toJson(urlStr);
        } catch (IOException e) {
            e.printStackTrace();
        } // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return tojson;
    }

}
