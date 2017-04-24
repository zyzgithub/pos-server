package com.alipay.refund.nopwd;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.base.config.EnvConfig;
import org.apache.commons.lang.StringUtils;

/**
 * Created by zc on 2016/12/9.
 */
public class UnificationAlipayRefundUtil {
    /**
     * @param refund_amount 退款金额
     * @param no1_trade_no  一号平台订单号
     * @param ali_trade_no  阿里订单号
     * @param refund_reason 退款原因
     * @param batchNo
     * @return
     */
    public static AlipayTradeRefundResponse apply(String refund_amount, String no1_trade_no, String ali_trade_no, String refund_reason, String batchNo) {
        try {

//            String app_id =  "2016071401615110";
//        String out_trade_no = "975681263674639";
//        String trade_no = "2016120921001004235092130753";
//        String refund_amount = "0.03";
//            String private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALQjA6JEWEyO5oZU2o8HxO6VZswqUIFVqqyTObEVIe03q793o9VODxBEJruMLRo+LHxjrD+stOCHlH7iNj6JZluchudONQ2Vfjqe7DLtu+OiL0RhUHWqk7KRx1VDwUuPWWJZNRPr5jco5ytF2DNWFRmhjRWenDhF4PO5wNJ7sVdPAgMBAAECgYB5Y+8a9nuae+IVPDFcmZu4O63ef9VoktZFHaGPF/KY7R8sE++v2r6D86k2UaxDNwq7eF+nOTda54LGTqLHWchDalzhnYljnMhFMZ5Q7eZgKifLUmJ0ODDUGJodTORZZehgz+k9IcE94tULNTEkGIdO9cgJch8iMuZXd/mJnwr0gQJBAOGR81z4aPWcDkrO31MAb4/Dl3Y58EOE7WMv66oOC7Sf2+iDHJ7vLnwdROJXGA+FIqkSOQSULXMYUZtoX9+SzXECQQDMcAajwQe7wQ05i7+JEXxenG19uybnLdyFtnibkZfDhzHymJ4KCRkdFg4d93El5XSiU090Qe/GvzS2TX6YAxC/AkEAmzDP73nUtI4+Ywd7LF7TYmSd8zAfdkShEimpCKGLOiT1pj12Vn6WS7vTZamoGx1s4EdyRQ0xS8tCchpYQ7h7cQJBAJ6nYb+7kfDXhRffqCkLdOVHqIEmuK44HSd30GmKt6h6RvzqC9vl66Ny9kmzOhAa3kULOmISua4XvSStR01ZM6kCQFMesPTPr9I6gSrZGHe1WAaWAfQQB47XHnbxreyH7eObP3pmGQl4GCUoq4RXIPTExp5dZB2yKfK3geh6EvoFqzY=";
//            String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

            refund_reason = StringUtils.isEmpty(refund_reason) ? "正常退款" : refund_reason;
            String app_id = EnvConfig.alipay.face_to_face_ali_appid;
            String private_key = EnvConfig.alipay.face_to_face_app_private_key;
            String alipay_public_key = EnvConfig.alipay.face_to_face_ali_public_key;
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", app_id, private_key, "json", "GBK", alipay_public_key);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            request.setBizContent("{" +
                    "    \"out_trade_no\":\"" + no1_trade_no + "\"," +
                    "    \"trade_no\":\"" + ali_trade_no + "\"," +
                    "    \"refund_amount\":" + refund_amount + "," +
                    "    \"refund_reason\":\"" + refund_reason + "\"," +
                    "    \"out_request_no\":\"" + batchNo + "\"," +
                    "    \"operator_id\":\"OP001\"," +
                    "    \"store_id\":\"NJ_S_001\"," +
                    "    \"terminal_id\":\"NJ_T_001\"" +
                    "  }");
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            return response;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        AlipayTradeRefundResponse refundResponse = new AlipayTradeRefundResponse();
        return refundResponse;
    }
}
