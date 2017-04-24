package com.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

/**
 * Created by zc on 2016/12/8.
 */
public class AlipaySignatureDemo {
    public static void main(String[] s) throws AlipayApiException {
        String content = "{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"mjo***@qq.com\",\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088302479956232\",\"invoice_amount\":\"0.00\",\"open_id\":\"20881080656045423486741332315223\",\"out_trade_no\":\"633581195055813\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2016-12-08 18:57:31\",\"total_amount\":\"0.01\",\"trade_no\":\"2016120821001004235091428908\",\"trade_status\":\"TRADE_CLOSED\"}";
        String sign = "lN6KYtz/qfSnAg3RIPLx98T1cPhRINdxCPOyMbuc61bKFiqt2LyfZEbkff+tC+pl9EWXy3iQLd27Qurci1p2uRo58s2+xazExCobcKfr2x85VHffGNj2KGQqNsPKe8Wsme+DzIhnhElAW2C0y9VPr/8r+kRi3v/a6jWYxtPjFTo=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0IwOiRFhMjuaGVNqPB8TulWbMKlCBVaqskzmxFSHtN6u/d6PVTg8QRCa7jC0aPix8Y6w/rLTgh5R+4jY+iWZbnIbnTjUNlX46nuwy7bvjoi9EYVB1qpOykcdVQ8FLj1liWTUT6+Y3KOcrRdgzVhUZoY0Vnpw4ReDzucDSe7FXTwIDAQAB";
        String charset = "utf-8";


        content = "{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"mjo***@qq.com\",\"buyer_pay_amount\":\"0.01\",\"buyer_user_id\":\"2088302479956232\",\"fund_bill_list\":[{\"amount\":\"0.01\",\"fund_channel\":\"PCREDIT\"}],\"gmt_payment\":\"2016-12-08 19:18:32\",\"invoice_amount\":\"0.01\",\"open_id\":\"20881080656045423486741332315223\",\"out_trade_no\":\"140381196356793\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.01\",\"total_amount\":\"0.01\",\"trade_no\":\"2016120821001004235091489187\"}";
        sign = "TVl7ww3wrZ8dP4X0YXeBwv7aVvAxqMQO1gjgXJPgJjg6hRAjzEctgpIGkkJtRkp84wfs9/sKHi6HpECd7DT1ZmuDbSiB0NDaYH17MFC1jxNlxhn9hz5mxHSaNfGeuCCflNn1wCFtHqZHJeK0aKWhKcS3weLiiZLusPU18fI32FA=";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0IwOiRFhMjuaGVNqPB8TulWbMKlCBVaqskzmxFSHtN6u/d6PVTg8QRCa7jC0aPix8Y6w/rLTgh5R+4jY+iWZbnIbnTjUNlX46nuwy7bvjoi9EYVB1qpOykcdVQ8FLj1liWTUT6+Y3KOcrRdgzVhUZoY0Vnpw4ReDzucDSe7FXTwIDAQAB";
        charset = "utf-8";

//        boolean b = AlipaySignature.rsaCheckContent(content, sign, publicKey, charset);
//        System.out.println(b);
    }
}
