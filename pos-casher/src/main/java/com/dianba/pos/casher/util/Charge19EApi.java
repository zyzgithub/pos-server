package com.dianba.pos.casher.util;

import com.dianba.pos.casher.vo.Charge_19E;
import com.dianba.pos.common.util.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import sun.security.provider.MD5;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
public class Charge19EApi {

    public  static void hfCharge(String chargeUrl, Charge_19E param){

            String sign=   param.sign();

            String MD5=   DigestUtils.md5Hex(sign).toLowerCase();



    }

}
