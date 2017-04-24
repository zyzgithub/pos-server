package com.wm.controller.withdrawals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;
import com.wm.service.bank.BankcardServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;

/**
 * @Title: Controller
 * @Description: withdrawals
 * @author wuyong
 * @date 2015-01-07 10:06:50
 * @version V1.0
 */
@Controller
@RequestMapping("/withdrawalsController")
public class WithdrawalsController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(WithdrawalsController.class);

	@Autowired
	private WithdrawalsServiceI withdrawalsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private BankcardServiceI bankcardService;
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private WUserServiceI userService;
	@Autowired
	private MerchantServiceI merchantService;
	
	
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * withdrawals列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "withdrawals")
	public ModelAndView withdrawals(HttpServletRequest request) {
		return new ModelAndView("com/wm/withdrawals/withdrawalsList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WithdrawalsEntity withdrawals,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WithdrawalsEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, withdrawals);
		this.withdrawalsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除withdrawals
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(WithdrawalsEntity withdrawals,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		withdrawals = systemService.getEntity(WithdrawalsEntity.class,
				withdrawals.getId());
		message = "删除成功";
		withdrawalsService.delete(withdrawals);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加withdrawals
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WithdrawalsEntity withdrawals,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(withdrawals.getId())) {
			message = "更新成功";
			WithdrawalsEntity t = withdrawalsService.get(
					WithdrawalsEntity.class, withdrawals.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(withdrawals, t);
				withdrawalsService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			withdrawalsService.save(withdrawals);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * withdrawals列表页面跳转
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(WithdrawalsEntity withdrawals, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(withdrawals.getId())) {
			withdrawals = withdrawalsService.getEntity(WithdrawalsEntity.class, withdrawals.getId());
			req.setAttribute("withdrawalsPage", withdrawals);
		}
		return new ModelAndView("com/wm/withdrawals/withdrawals");
	}

	/**
	 * 快递员提现
	 * @param userId
	 * @param password
	 * @param money
	 * @return
	 */
	@RequestMapping(params = "saveWithdrawals")
	@ResponseBody
	public AjaxJson saveWithdrawals(@RequestParam int userId, @RequestParam String password, @RequestParam String money) {
		AjaxJson j = new AjaxJson();
		if (userId != 0) {
			WUserEntity wuser = userService.getEntity(WUserEntity.class, userId);
			if (wuser != null) {
				if (wuser.getMoney() < Double.parseDouble(money)) {
					j.setStateCode("01");
					j.setMsg("提现金额不能大于余额");
					j.setSuccess(false);
					return j;
				}
				if (wuser.getPassword().equals(password)) {
					try {
						return withdrawalsService.newWithDrawApply(wuser, Double.parseDouble(money), null);
					} catch (Exception e) {
						e.printStackTrace();
						j.setSuccess(false);
						j.setMsg("提现申请失败!");
						return j;
					}
				} else {
					j.setSuccess(false);
					j.setMsg("抱歉，输入的密码不正确，无法提现");
					j.setStateCode("01");
					return j;
				}
			} else {
				j.setSuccess(false);
				j.setMsg("找不到该用户信息");
				j.setStateCode("01");
			}
		} else {
			j.setSuccess(false);
			j.setMsg("申请提现失敗");
			j.setStateCode("01");
		}
		return j;
	}
	
	/**
	 * 后台提现申请
	 * @param merchantUserId 商家总账号
	 * @param childMerchUserId 商家子账户,如果为空则是普通提现，否则为总账号提子账户的钱
	 * @param money 提现金额
	 * @param cardId 提现卡号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="merchantWithdraw")
	@ResponseBody
	public AjaxJson merchantWithdraw(Integer merchantUserId, Integer childMerchUserId, Double money, Integer cardId) throws Exception {
		logger.info("merchantWithdraw merchantUserId:{}, childMerchUserId:{}, money:{}, cardId:{}", merchantUserId, childMerchUserId, money, cardId);
		AjaxJson j = new AjaxJson();
		if (merchantUserId == null) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("商家不能为空");
			return j;
		}
		if (cardId == null) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("请先绑定银行卡");
			return j;
		}
		if (money < 0) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("提现金额不合法");
			return j;
		}
		WUserEntity merchantUser = userService.getEntity(WUserEntity.class, merchantUserId);
		if (!merchantUserId.equals(childMerchUserId)) {
			// 子账户的余额转到总账户余额，然后再对总账户提现
			boolean flag = withdrawalsService.merchantChildWithdraw(merchantUserId, childMerchUserId, new BigDecimal(money));
			if(!flag){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("不存在子账户和总账户的关系");
				return j;
			}
		}
		try {
			j = withdrawalsService.newWithDrawApply(merchantUser, money, cardId);
		} catch (Exception ex) {
			j.setStateCode("01");
			j.setMsg(ex.getMessage());
			j.setSuccess(false);
			return j;
		}
		return j;
	}

	/**
	 * 查询最新的一笔提现记录
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "queryLatest")
	@ResponseBody
	public AjaxJson queryLatest(int userId) {
		AjaxJson j = new AjaxJson();
		if (userId != 0) {
			List<Map<String, Object>> withdrawalsList = withdrawalsService.queryLatest(userId, 1, 1);
			if (CollectionUtils.isNotEmpty(withdrawalsList)) {
				Map<String, Object> withdrawals = withdrawalsList.get(0);
				if (withdrawals != null) {
					if ("apply".equals(withdrawals.get("state").toString())) {
						j.setSuccess(false);
						j.setMsg("有一笔提现正在处理，暂时无法提现");
						j.setStateCode("01");
					} else {
						j.setSuccess(true);
						j.setMsg("可以申请提现操作");
						j.setStateCode("00");
					}
					return j;
				}
			}
		}
		j.setSuccess(false);
		j.setMsg("查询失敗");
		j.setStateCode("01");
		return j;
	}
	
	/**
	 * 代理商提现申请
	 * @param userId 代理商用户Id
	 * @param money 提现金额
	 * @param cardId 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "agentWithdrawApply")
	@ResponseBody
	public AjaxJson agentWithdrawApply(Integer userId, Double money, Integer cardId) throws Exception {
		logger.info("agentWithdrawApply userId:{}, money:{}, cardId:{}", userId, money, cardId);
		AjaxJson j = new AjaxJson();

		// 系统分切,终止代理商提现服务
		j.setSuccess(false);
		j.setMsg("服务已停止!");
		return j;

		/*if(userId==null){
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("请检查用户是否为空");
			return j;
		}
		
		WUserEntity user = withdrawalsService.get(WUserEntity.class, userId);
		
		//代理商提现
		AgentInfoEntity agent = withdrawalsService.findUniqueByProperty(AgentInfoEntity.class, "userId", userId);
		if(agent==null){
			logger.error("未找到相关代理商信息userId:{},无法提现", userId);
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("代理商不存在，无法提现");
			return j;
		}
		return withdrawalsService.agentWithdrawApply(agent, user, money, cardId);*/
	}
	
	@ResponseBody
	@RequestMapping(params = "checkWithdrawByUser")
	public AjaxJson checkWithdrawByUser(Integer userId) {
		AjaxJson j = new AjaxJson();
		String msg = withdrawalsService.checkWithdrawByUser(userId);
		if(StringUtils.isNotEmpty(msg)){
			j.setSuccess(false);
			j.setMsg(msg);
		} else {
			j.setSuccess(true);
			j.setMsg("OK");
		}
		return j;
	}
	
	@ResponseBody
	@RequestMapping(params = "checkWithdraw")
	public AjaxJson checkWithdraw(String withdrawIds) {
		AjaxJson j = new AjaxJson();
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isNotEmpty(withdrawIds)){
			String[] withdraws = withdrawIds.split(",");
			for(String withdrawId : withdraws){
				String retMsg = withdrawalsService.checkWithdraw(Integer.parseInt(withdrawId));
				map.put(withdrawId, retMsg);
			}
		}
		j.setSuccess(true);
		j.setMsg("ok");
		j.setObj(map);
		logger.info("checkWithdraw result : {}", JSONObject.fromObject(j).toString());
		return j;
	}
}
