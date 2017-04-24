package com.wm.controller.job;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.open_api.OpenResult.State;
import com.wm.dao.agent.AgentIncomeTimerDao;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/agent")
public class AgentJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(AgentJobController.class);

	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private WUserServiceI userService;
	@Autowired
	private OrderIncomeServiceI orderIncomeService;
	@Autowired
	private AgentIncomeTimerDao agentIncomeTimerDao;

	/**
	 * 定时任务 - 对代理商扣点返点及商家预收入结算
	 */
	@RequestMapping("/orderIncome")
	@ResponseBody
	public AjaxJson orderIncome(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		logger.info("代理商定时器结算开始");
		List<Map<String, Object>> list = agentIncomeTimerDao.findAgentTimerList();
		orderIncomeService.unAgentIncome(list);//代理商结算
		j.setSuccess(true);
		j.setMsg("对代理商扣点返点及商家预收入结算完成！");
		return j;
	}
	

	/**
	 * 定时任务 - 代理商直营返点收入、分销返点收入每日统计
	 */
	@RequestMapping("/orderIncomeDayly")
	@ResponseBody
	public AjaxJson orderIncomeDayly(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		logger.info("定时器-----代理商直营返点收入、分销返点收入每日统计！");
		String date = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
		agentIncomeTimerDao.orderIncomeDayly(date);
		
		j.setSuccess(true);
		j.setMsg("统计代理商返点扣点日收入完成！");
		return j;
	}
}
