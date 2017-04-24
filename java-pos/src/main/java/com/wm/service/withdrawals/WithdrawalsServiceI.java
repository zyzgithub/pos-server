package com.wm.service.withdrawals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.flow.FlowEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;

public interface WithdrawalsServiceI extends CommonService{
	
	public List<WithdrawalsEntity> findWithdrLog(Integer userId);
	
	/**
	 * 查找用户最近的提现记录
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> queryLatest(Integer userId,int page, int rows);
	
	public void saveWithdrawals(WUserEntity wuser,WithdrawalsEntity withdrawals,FlowEntity flow);
	
	/**
	 * 提现申请
	 * @param wuser
	 * @param money
	 * @return
	 */
	public AjaxJson oldWithDrawApply(WUserEntity wuser, String money);
	
	/**
	 * 提现 - 走兴业银行代付流程
	 * @param wuser
	 * @param money
	 * @return
	 */
	public AjaxJson newWithDrawApply(WUserEntity wuser, Double money, Integer cardId) throws NumberFormatException, Exception;

	/**
	 * 提现 - 子账户的余额转到总账户余额
	 * @param merchantUserId
	 * @param childMerchUserId
	 * @param money
	 * @return
	 */
	public boolean merchantChildWithdraw(Integer merchantUserId, Integer childMerchUserId, BigDecimal money) throws Exception;
	
	/**
	 * 代理商提现申请
	 * @param agent
	 * @param wuser
	 * @param money
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public AjaxJson agentWithdrawApply(AgentInfoEntity agent, WUserEntity wuser, Double money, Integer cardId) throws Exception;
	
	public AjaxJson takeCashRuleLoader(int userId);
	
	public AjaxJson beforeTakeCash(int userId, int takeMode);
	
	public int remainTakeCashCount(int userId, int takeMode) ;
	
	public double tryTakeCash(int userId, double takeAmount, Integer bindCardId, int takeMode) throws Exception ;
	
	/**
	 * 校验用户当前提现是否有异常
	 * @param userId
	 * @return 异常内容
	 */
	public String checkWithdrawByUser(Integer userId);

	/**
	 * 校验提现是否有异常
	 * @param withdrawId
	 * @return 异常内容
	 */
	public String checkWithdraw(Integer withdrawId);
}
