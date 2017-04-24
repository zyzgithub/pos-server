package courier_mana.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.jeecgframework.core.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wm.service.rebate.RebateServiceI;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class RebateServiceTest extends TestCase{

	@Autowired
	private RebateServiceI rebateService;
	
	@Test
	public void statMerchantRebateByEveryday() {
		//统计商家每一天
		rebateService.statMerchantRebateByEveryday();
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void statMerchantRebateByBefore(){
		//统计一段时间内商家每日返点
		rebateService.statMerchantRebateByBefore(DateUtils.parseDate("2016-03-28"), DateUtils.parseDate("2016-03-30"), 3318);
		//rebateService.statMerchantRebateByBefore(null, DateUtils.parseDate("2016-03-02"), 3318);
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void payMerchantRebate1(){
		//统计固定时间、固定商家的返点明细
		rebateService.payMerchantRebate(DateUtils.parseDate("2016-04-01"), 3318);
	}
	
	@Test
	public void statCourierRebateByEveryday() {
		rebateService.statCourierRebateByEveryday();
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void payMerchantRebate() {
		rebateService.payMerchantRebate();
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void payCourierRebate() throws Exception {
		rebateService.payCourierRebate();
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void statCourierRebateByBefore(){
		rebateService.statCourierRebateByBefore(DateUtils.parseDate("2015-12-01"), DateUtils.parseDate("2016-02-02"));
		System.out.println(">>>>>>>>>>");
	}
	
	@Test
	public void manualGrant(){
		//手动发放
		rebateService.manualGrant(32);
		System.out.println(">>>>>>>>>>");
	}
}
