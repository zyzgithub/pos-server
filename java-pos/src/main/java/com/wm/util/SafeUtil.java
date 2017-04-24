package com.wm.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.controller.open_api.OpenResult.State;
import com.wp.ConfigUtil;

public class SafeUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(SafeUtil.class);
	
	/**
	 * 校验签名
	 * @param sign 私钥
	 * @param token 公钥
	 * @param timestamp 时间戳
	 * @return
	 */
    public static State checkSign(String sign, String timestamp) {
        String paramStr = ConfigUtil.TOKEN_DES_KEY + timestamp;
        logger.info("checkSign paramStr:{}", paramStr);
        String md5Param = DigestUtils.md5Hex(paramStr);
        if (!sign.equals(md5Param.toUpperCase())) return State.SignError;
        return null;
    }
}
