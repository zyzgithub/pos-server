package com.dianba.pos.extended.util;

import com.alibaba.fastjson.JSON;
import com.dianba.pos.extended.vo.ChargeFlow;
import com.dianba.pos.extended.vo.ChargeFlowResult;
import com.dianba.pos.extended.vo.Product;
import com.dianba.pos.extended.vo.ProductListDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
@SuppressWarnings("all")
public class FlowCharge19EApi {

    /***流量充值**/
    public static ChargeFlowResult flowCharge(String chargeUrl, ChargeFlow flow) {

        Map map = new HashMap<>();
        map.put("signType", flow.getSignType());
        map.put("timestamp", flow.getTimestamp());
        map.put("dataType", flow.getDataType());
        map.put("inputCharset", flow.getInputCharset());
        map.put("version", flow.getVersion());
        map.put("productId", flow.getProductId());
        map.put("mobile", flow.getMobile());
        map.put("merchantId", flow.getMerchantId());
        map.put("merOrderNo", flow.getMerOrderNo());
        // map.put("remake",flow.getRemark());

        String md5 = FlowChargeSign.getSignByMap(map);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String tojson = "";
        ChargeFlowResult cf=new ChargeFlowResult();
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

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            Map mapm = new HashMap<>();
            mapm.put("signType", flow.getSignType());
            mapm.put("timestamp", flow.getTimestamp());
            mapm.put("dataType", flow.getDataType());
            mapm.put("inputCharset", flow.getInputCharset());
            mapm.put("version", flow.getVersion());
            mapm.put("productId", flow.getProductId());
            mapm.put("mobile", flow.getMobile());
            mapm.put("merchantId", flow.getMerchantId());
            mapm.put("merOrderNo", flow.getMerOrderNo());
            //mapm.put("remake",flow.getRemark());
            mapm.put("sign", md5);
            Map<String, String> pdmapar = MapUtil.sortMapByKey(mapm);
            String params = MapUtil.createLinkString(pdmapar);
            // 发送请求参数

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
            System.out.println(result);
            cf= JSON.parseObject(result,ChargeFlowResult.class);

            System.out.println(cf);

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
        return cf;


    }

    /***根据手机号等参数获取产品信息**/
    public static String queryProduct(String chargeUrl, Product pd) {
        Map map = new HashMap<>();
        map.put("signType", pd.getSignType());
        map.put("timestamp", pd.getTimestamp());
        map.put("dataType", pd.getDataType());
        map.put("inputCharset", pd.getInputCharset());
        map.put("version", pd.getVersion());
        map.put("merchantId", pd.getMerchantId());
        map.put("mobile", pd.getMobile());
        Map<String, String> pdmap = MapUtil.sortMapByKey(map);
        String sign = MapUtil.createLinkString(pdmap);
        String Md5 = FlowCharge19EUtil.getKeyedDigest(sign, FlowCharge19EUtil.KEY);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String tojson = "";
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

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            // String params=pd.params(MD5);
            Map mappars = new HashMap<>();
            mappars.put("signType", pd.getSignType());
            mappars.put("timestamp", pd.getTimestamp());
            mappars.put("dataType", pd.getDataType());
            mappars.put("inputCharset", pd.getInputCharset());
            mappars.put("version", pd.getVersion());
            mappars.put("merchantId", pd.getMerchantId());
            mappars.put("mobile", pd.getMobile());
            mappars.put("sign", Md5);
            Map<String, String> mapparsSort = MapUtil.sortMapByKey(mappars);
            String params = MapUtil.createLinkString(mapparsSort);
            String urlStr = URLDecoder.decode(params, "UTF-8");
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
        return result;


    }
}
