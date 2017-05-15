package com.dianba.pos.extended.util;


import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.Md5Util;
import com.dianba.pos.extended.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
@SuppressWarnings("all")
public class HfCharge19EApi {
    private static Logger logger= LogManager.getLogger(HfCharge19EApi.class);
    /**
     * 话费充值下单
     *
     * @param chargeUrl
     * @param param
     * @return
     */
    public static ChargeResult hfCharge(String chargeUrl, Charge19E param) {
        String sign = param.sign();
        String MD5 = Md5Util.HEXAndMd5(sign).toUpperCase();
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String tojson = "";
        ChargeResult cr=new ChargeResult();
        try {
            URL realUrl = new URL(chargeUrl);

            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            logger.info("话费充值请求Url:==========="+chargeUrl);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String params = param.params(MD5);
            System.out.println(params);

            logger.info("获取话费充值请求参数:========"+params);
            out.print(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            String urlStr = URLDecoder.decode(result, "UTF-8");

            tojson = HfCharge19EUtil.toJson(urlStr);

            logger.info("话费充值返回结果：======"+tojson);
            System.out.println(tojson);
           cr=(ChargeResult)JSONObject.parseObject(tojson,ChargeResult.class);
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
        return cr;
    }


    /***话费订单查询**/
    public static String hfOrderQuery(String chargeUrl, HfOrderQuery ho) {

        String sign = ho.sign();
        String MD5 = Md5Util.HEXAndMd5(sign).toUpperCase();
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String tojson = "";
        try {
            URL realUrl = new URL(chargeUrl);

            logger.info("话费订单请求Url:==========="+chargeUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String params = ho.params(MD5);
            String urlStr = URLDecoder.decode(params, "UTF-8");
            out.print(params);
            logger.info("获取话费订单信息参数：======"+params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            String sb = URLDecoder.decode(result, "UTF-8");
            tojson = HfCharge19EUtil.toJson(sb);

            logger.info("获取话费订单信息：========"+tojson);
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
