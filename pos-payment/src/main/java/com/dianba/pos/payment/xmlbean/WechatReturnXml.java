package com.dianba.pos.payment.xmlbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WechatReturnXml {

    @XmlElement(name = "return_code")
    private String returnCode;
    @XmlElement(name = "return_msg")
    private String returnMsg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public WechatReturnXml(String returnCode, String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public static WechatReturnXml createSuccessReturn() {
        return new WechatReturnXml("SUCCESS", "OK");
    }

    public static WechatReturnXml createFailReturn(String returnCode, String returnMsg) {
        return new WechatReturnXml(returnCode, returnMsg);
    }
}
