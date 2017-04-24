package print;

import com.alibaba.fastjson.JSON;
import com.wm.service.impl.merchant.MerchantSupplyServiceImpl;
import com.wm.service.impl.orderincome.OrderIncomeServiceImpl;
import junit.framework.TestCase;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class MerchantSupplyServiceTest extends TestCase{
	
	@Autowired
	private MerchantSupplyServiceImpl merchantSupplyServiceImpl;
	
	@Autowired
	private OrderIncomeServiceImpl orderIncomeServiceImpl;
	
	/**
	 * 微信支付
	 */
	@Test
	public void weixinConfirmPay() {
		String code = "0";
		merchantSupplyServiceImpl.weixinConfirmPay("201605015555", BigDecimal.valueOf(0.01 * 100).setScale(0).toString(), "http://apptest.0085.com/orderController.do?confirmPayNotify", code);
		System.out.println("支付成功");
	}
	
	/**
	 * H5页面通过手机号登录接
	 */
	@Test
	public void merchantLogin() {
		AjaxJson json = merchantSupplyServiceImpl.merchantLogin("15018770810");
		System.out.println("成功:"+ JSON.toJSONString(json));
	}
	
	@Test
	public void unSupplyOrderIncome() {
		AjaxJson j = new AjaxJson(true, "创建预收入成功", "0");
		try{
			orderIncomeServiceImpl.unSupplyOrderIncome(352031,235329);
		} catch (RuntimeException e) {
			j.setMsg("创建预收入失败，失败原因为："+e.getMessage());
			j.setStateCode("01");
			j.setSuccess(false);
		}  catch (Exception e) {
			j.setMsg("创建预收入失败，失败原因为："+e.getMessage());
			j.setStateCode("01");
			j.setSuccess(false);
		}
		
		System.out.println("成功:"+ JSON.toJSONString(j));
	}

}
