package print;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.wm.dto.merchant.MerchantOpenStoreDto;
import com.wm.service.impl.merchant.MerchantOpenStoreServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class MerchantOpenStoreServiceTest extends TestCase{
	
	@Autowired
	private MerchantOpenStoreServiceImpl merchantOpenStoreServiceImpl;
	
	@Test
	public void getOrgList() {
		AjaxJson json = merchantOpenStoreServiceImpl.getOrgList();
		System.out.println("成功"+ JSON.toJSONString(json));
	}
	
	@Test
	public void confirmRegister() {
		MerchantOpenStoreDto merchantOpenStoreDto = new MerchantOpenStoreDto();
		merchantOpenStoreDto.setPhone("15088888888");
		merchantOpenStoreDto.setPassword("564646465164646541646");
		merchantOpenStoreDto.setInviteCode("888888");
		merchantOpenStoreDto.setMerchantName("好好商家");
		AjaxJson json = merchantOpenStoreServiceImpl.confirmRegister(merchantOpenStoreDto);
		System.out.println("成功"+ JSON.toJSONString(json));
	}

	@Test
	public void openWaimai() {
		MerchantOpenStoreDto merchantOpenStoreDto = new MerchantOpenStoreDto();
		merchantOpenStoreDto.setUserId(235577);
		merchantOpenStoreDto.setAddress("我在这里");
		merchantOpenStoreDto.setMobile("15088888888");
		merchantOpenStoreDto.setCityName("广州市");
		merchantOpenStoreDto.setNickname("苏二");
		merchantOpenStoreDto.setMerchantId(4753);
		merchantOpenStoreDto.setLatitude(new BigDecimal("66666"));
		merchantOpenStoreDto.setLongitude(new BigDecimal("66666"));
		AjaxJson json = merchantOpenStoreServiceImpl.openWaimai(merchantOpenStoreDto);
		System.out.println("成功"+ JSON.toJSONString(json));
	}
	
	@Test
	public void getApplyWaimaiStatus() {
		AjaxJson json = merchantOpenStoreServiceImpl.getApplyWaimaiStatus(4753);
		System.out.println("成功"+ JSON.toJSONString(json));
	}
}
