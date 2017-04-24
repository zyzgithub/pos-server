package print;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.wm.service.impl.merchant.MerchantMultiAccoServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class MerchantMultiAccoServiceTest extends TestCase{
	
	@Autowired
	private MerchantMultiAccoServiceImpl merchantMultiAccoServiceImpl;
	
	/**
	 * 获取商家总店和分页列表
	 */
	@Test
	public void getALLStores() {
		List<Map<String, Object>> list = merchantMultiAccoServiceImpl.getALLStoresPageList(3318,1,10);
		System.out.println("成功"+JSON.toJSONString(list));
	}
	
	//总店和分店帐户余额
	@Test
	public void getALLStoresAccount() {
		List<Map<String, Object>> list = merchantMultiAccoServiceImpl.getALLStoresAccountPageList(3318, 1, 2);
		System.out.println("成功"+JSON.toJSONString(list));
	}
	
	//收入记录
	@Test
	public void getMerchantCashflow() {
		List<Map<String, Object>> list = merchantMultiAccoServiceImpl.getMerchantCashflowPageList(3318,1,2);
		System.out.println("成功"+JSON.toJSONString(list));
	}
	

}
