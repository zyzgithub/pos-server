package courier_mana.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.common.vo.SearchVo4UserRank;
import com.courier_mana.statistics.service.CourierCommentService;
import com.courier_mana.statistics.service.CourierUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml","classpath:spring-mvc-context.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-mvc-aop.xml"})
public class CourierUserServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(CourierUserServiceTest.class);
	
	@Autowired
	private CourierUserService courierUserService;
	
	@Autowired
	private CourierCommentService courierCommentService;
	
	private void showMap(Map<String, Object> map){
		StringBuilder str = new StringBuilder();
		str.append("\n");	
		for(Map.Entry<String, Object> entry:map.entrySet()){
			str.append(entry.getKey());
			str.append(":");
			str.append(entry.getValue());
		}
		logger.info(str.toString());
	}
	
	private void showList(List<Map<String, Object>> list){
		for(Map<String, Object> m:list){
			showMap(m);
		}
	}
	
	private void showAsJSON(Object obj){
		AjaxJson ajaxJson = BasicController.SUCCESS(obj);
		logger.info(JSON.toJSONString(ajaxJson));
	}
	
	/**
	 * 获取指定用户在指定时间三天内的消费详情
	 * done
	 */
//	@Test
	
	/**
	 * 获取划分用户类型规则测试
	 * done
	 */
//	@Test
	public void getUserTypeRuleTest(){
		showAsJSON(this.courierUserService.getUserTypeRule());
	}
	
	/**
	 * 获取消费金额在指定范围内的用户数量
	 * done
	 */
//	@Test
	public void getUserCountTest(){
//		this.showMap(this.courierUserService.getUserCount(null, null));//done
//		this.showMap(this.courierUserService.getUserCount(0, 11));//done
//		this.showMap(this.courierUserService.getUserCount(null, 11));//done
//		this.showMap(this.courierUserService.getUserCount(0, null));//done
	}
	
//	@Test
//	public void getUserTotalSpentTest(){
//		List<Map<String, Object>> list = this.courierUserService.getUserTotalSpent(9);
//		this.showList(list);
//		Double a = (Double)list.get(1).get("totalSpent");
//		System.out.println(a+1);
//	}
	
//	@Test
	public void getUserCount1Test(){
		Long startTime = System.currentTimeMillis();
		List<Map<String, Object>> typeRuleList = this.courierUserService.getUserTypeRule();
		int amountOfType = typeRuleList.size();
		int[] userCount = new int[amountOfType]; 
		SearchVo vo = new SearchVo();
		vo.setTimeType("day");
		for(Map<String, Object>m: this.courierUserService.getUserTotalSpent(vo, 4)){
			Integer totalSpent = (Integer)m.get("totalSpent");
			for(int i = 0; i < amountOfType; i++){
				Integer typeAmount = (Integer)typeRuleList.get(i).get("amount");
				if(totalSpent/100 > typeAmount){
					userCount[i] += 1;
					break;
				}
			}
		}
		for(int k = 0; k < amountOfType; k++){
			typeRuleList.get(k).put("userCount", userCount[k]);
		}
		System.out.println("Elapse: "+(System.currentTimeMillis()-startTime));
		showAsJSON(typeRuleList);
	}
	
	@Test
	public void getUserRankTest(){
		Long startTime = System.currentTimeMillis();
		SearchVo4UserRank param = new SearchVo4UserRank();
		param.setTimeType("month");
//		showAsJSON(this.courierUserService.getUserRank(param, 1, 220));
		logger.info("Elapsed: " + (System.currentTimeMillis() - startTime));
	}
	
//	@Test
	public void tete(){
		String userType = "VIP5";
		Double min = .0;
		Double max = null;
		List<Map<String, Object>> typeList = this.courierCommentService.getCustTypeList();
		for(int i=typeList.size()-1; i>=0; i--){
			Map<String, Object> listItem = typeList.get(i);
			if(listItem.get("typeName").equals(userType) && i>0){
				min = Double.valueOf(listItem.get("amount").toString());
				max = Double.valueOf(typeList.get(i-1).get("amount").toString());
				break;
			}
			if(listItem.get("typeName").equals(userType) && i==0){
				min = Double.valueOf(listItem.get("amount").toString());
				break;
			}
		}
		System.out.println(min+"\n"+max);
	}
}
