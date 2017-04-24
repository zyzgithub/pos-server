package com.wm.controller.order.simulateordercomplete;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.orderincome.OrderIncomeServiceI;

@Controller
@RequestMapping("ci/simulateJobController")
public class SimulateJobController {
	private static final Logger logger = LoggerFactory.getLogger(SimulateJobController.class);
	
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	/**
	 * 批量结算订单
	 */
	@RequestMapping(params = "settlement")
	@ResponseBody
	public AjaxJson settlement(String runTime, String merchantIds) {
		AjaxJson j = new AjaxJson();
		logger.info("runTime:{}, merchantIds:{}", runTime, merchantIds);
		 String findSettleList = "select oi.id from order_income oi left join `order` o on o.id=oi.order_id "
					+ " where oi.type=0 and oi.state='unpay' and oi.pay_time<unix_timestamp(?) and o.state='confirm' "
					+ " and o.rstate in ('normal','norefund') and o.order_type<>'flashsales'";
					if(!StringUtils.isBlank(merchantIds)){
						findSettleList += " and oi.merchant_id in ("+merchantIds+") ";
					}
					findSettleList += "order by oi.id desc";
		logger.info("SQL:{}", findSettleList);
		List<Map<String, Object>> settleList = orderIncomeService.findForJdbc(findSettleList, runTime);
		int size = settleList.size();
		logger.info("settlement total size:" + size);
		for(Map<String, Object> settleMap : settleList){
			logger.info("settlement current index:" + size--);
			Integer orderIncomeId = Integer.valueOf(settleMap.get("id").toString());
			try {
				orderIncomeService.settleMent(orderIncomeId);
			} catch (Exception e) {
				logger.error("预收入{}结算失败!", orderIncomeId);
				e.printStackTrace();
			}
		}
		j.setSuccess(true);
		j.setMsg("订单结算完成!!");
		return j;
	}

}
