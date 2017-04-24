package service;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.ucf.common.BaseParam;
import com.ucf.common.WithdrawParam;
import com.ucf.sdk.util.UnRepeatCodeGenerator;
import com.ucf.service.WithdrawService;
import com.wp.ConfigUtil;

/**
 * 模拟代付
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc-dev.xml","classpath:spring-mvc-hibernate-dev.xml"})
//@ContextConfiguration(locations = { "classpath:spring-mvc-dev.xml","classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-aop.xml"})
public class TestUcfWithdraw {
	
	private static final Logger logger = LoggerFactory.getLogger(TestUcfWithdraw.class);

	@Autowired
	private WithdrawService ucfWithdrawalService;

	@Test
	public void start() throws Exception {
		
		WithdrawParam param = new WithdrawParam();
		String merId = EnvConfig.ucf.merId;
		logger.info("merId:{}", merId);
//		logger.info("domain:{}", EnvConfig.base.DOMAIN);
		param.setService(BaseParam.Service.WITHDRAW);
		param.setMerchantId(merId);
		String merchantNo = DateTime.now().toString("yyyyMMddhhmmssSSS");//商户订单号
		String reqSn = UnRepeatCodeGenerator.createUnRepeatCode(merId, "REQ_WITHDRAW", merchantNo);
		param.setReqSn(reqSn);
		param.setMerchantNo(merchantNo);
		param.setAmount("1");
		param.setBankNo("ICBC");
		param.setAccountNo("4270205700684830");
		param.setAccountName("汤根生");
		param.setMobileNo("13189011530");
		param.setNoticeUrl(ConfigUtil.UCF_NOTICE_URL);
		param.setMemo("tgs");
		JSONObject result = ucfWithdrawalService.withdraw(param);
		if(null != result){
			if("S".equals(result.get("status"))){
				logger.info("merchantId:{} withdraw amount:{} success ! merchantNo:{}, tradeNo:{}", 
						result.get("merchantId"), result.get("amount"), result.get("merchantNo"), result.get("tradeNo"));
				// TODO
			} else if("I".equals(result.get("status"))){
				logger.info("merchantId:{} withdraw amount:{} resolving ... ! merchantNo:{}, tradeNo:{}", 
						result.get("merchantId"), result.get("amount"), result.get("merchantNo"), result.get("tradeNo"));
			} else {
				logger.warn("withdraw failed. msg:{}", result.get("resMessage"));
			}
		} else {
			logger.error("withdraw error, param:{}", JSONObject.toJSON(param));
		}
		System.in.read();
	}

	
	public static void main(String[] args) {
		
	}

}
