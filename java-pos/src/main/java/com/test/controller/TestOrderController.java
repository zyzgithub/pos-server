package com.test.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.service.TestOrderI;
import com.test.service.impl.TestOrder;

/**
 * @Description: 模拟复制订单
 * @author Simon
 */
@Controller
@RequestMapping("ci/testOrder")
public class TestOrderController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestOrderController.class);

	@Autowired
	private TestOrderI testOrder;
	
	@ResponseBody
	@RequestMapping(params = "copydata")
	public AjaxJson copydata(String mobile, String name, String desc) {
		AjaxJson j = new AjaxJson();
		if("13189011530".equals(mobile)){
			logger.info("mobile:{}, name:{}, desc:{}", mobile, name, desc);
			testOrder.testCopyOrder();
			j.setSuccess(true);
			j.setMsg("exe done, please check the logs ");
		} else {
			logger.warn("not allowed mobile:{}, name:{}, desc:{}", mobile, name, desc);
			j.setSuccess(false);
			j.setMsg("sorry not allowed !!! ");
		}
		return j;
	}
	
	@ResponseBody
	@RequestMapping(params = "copyOrder")
	public AjaxJson copyOrder(Integer merchantId, Integer money) {
		AjaxJson j = new AjaxJson();
		boolean ret = testOrder.testCopyOrder1(merchantId, money);
		j.setSuccess(ret);
		return j;
	}
	
	@ResponseBody
	@RequestMapping(params = "copyMarketOrder")
	public AjaxJson copyOrder(String yearMonth) {
		AjaxJson j = new AjaxJson();
//		boolean ret = testOrder.testCopyMarketOrder(yearMonth);
		boolean ret = true;
		int monthDays = 0;
		if("201609".equals(yearMonth)){
			monthDays = 30;
		} else if("201610".equals(yearMonth)){
			monthDays = 31;
		} if("201611".equals(yearMonth)){
			monthDays = 30;
		} if("201612".equals(yearMonth)){
			monthDays = 31;
		}
		List<Map<String, String>> merchants = TestOrder.loadMarketData(yearMonth);
		for (int i = 1; i < monthDays + 1; i++) {
			String ymd = yearMonth;
			if (i < 10) {
				ymd = ymd + "0" + i;
			} else {
				ymd = ymd + i;
			}
			logger.info(" >>>>>>>>> copyMarketOrder monthDay:{}", ymd);
			for(Map<String, String> map : merchants){
				Integer sourceMchId = Integer.parseInt(map.get("sourceMchId"));
				Integer targetMchId = Integer.parseInt(map.get("targetMchId"));
				Integer orderCount = Integer.parseInt(map.get("orderCount"));
				List<Map<String, Object>> sourceOrderIds = testOrder.selectSourceOrderIds(sourceMchId, ymd);
				if(!CollectionUtils.isEmpty(sourceOrderIds)){
					int sourceOrderIdSize = sourceOrderIds.size();
					logger.info(" sourceOrderIdSize:{}", sourceOrderIdSize);
					for (int k = 0; k < orderCount; k++) {
						int indx = RandomUtils.nextInt(sourceOrderIdSize);
						Map<String, Object> sourceOrderIdMap = sourceOrderIds.get(indx);
						Long sourceOrderId = Long.parseLong(sourceOrderIdMap.get("id").toString());
						String hashCode = Integer.toOctalString((int)System.currentTimeMillis());
						testOrder.copyMarketOrder(hashCode, targetMchId, sourceOrderId);
					}
				} else {
					logger.error("sourceOrderIds is empty. ymd:{}, sourceMchId:{}", ymd, sourceMchId);
				}
			}
		}
//		return false;
		j.setSuccess(ret);
		return j;
	}
	
}

