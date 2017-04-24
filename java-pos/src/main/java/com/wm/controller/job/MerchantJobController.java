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

import com.life.bank.industrial.LifeBankService;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.entity.statistics.MerchatStatisticsDalyEntity;
import com.wm.service.comment.CommentServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.MerchantSettlementServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.rebate.RebateServiceI;
import com.wm.service.statistics.MerchatStatisticsDalyServiceI;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/merchant")
public class MerchantJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(MerchantJobController.class);

	@Autowired
	OrderServiceI orderService;
	@Autowired
	RebateServiceI rebateService;
	@Autowired
	CommentServiceI commentService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	MerchantSettlementServiceI merchantSettlementService;
	@Autowired
	private MerchatStatisticsDalyServiceI merchatStatisticsDalyService;
	@Autowired
	private LifeBankService lifeBankService;

	/**
	 * 商家日统计
	 */
	@RequestMapping("/statiDayly")
	@ResponseBody
	public AjaxJson statiDayly(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		
		DateTime statisticsDate = DateTime.now().minusDays(1);
		String strStatiDate = statisticsDate.toString("yyyy-MM-dd");
		List<Map<String, Object>> merchantList = merchantService.findByDelState(0);
		int size = merchantList.size();
		logger.info("statiDayly total size:" + size);
		for (Map<String, Object> merchant : merchantList) {
			logger.info("statiDayly current index:" + size--);
			Integer merchantId = Integer.parseInt(merchant.get("id").toString());
			try {
				MerchatStatisticsDalyEntity msde = new MerchatStatisticsDalyEntity();
				msde.setMerchantId(merchantId);
				
				// 统计订单
				List<Map<String, Object>> list = merchatStatisticsDalyService.findOrderStati(merchantId, strStatiDate);
				if(list != null && list.size() > 0){
					Map<String, Object> map = list.get(0);
					Integer totalOrder = Integer.parseInt(map.get("c").toString());
					msde.setDaylyTotalOrder(totalOrder);
					Object saledMoney = map.get("s");
					if(saledMoney != null){
						Double totalSaledMoney = Double.parseDouble(saledMoney.toString());
						msde.setDaylyTotalSaledMoney(totalSaledMoney);
					} else {
						msde.setDaylyTotalSaledMoney(0.0);
					}
				}
				
				// 统计总份数
				Long yestodayMenus = orderService.getMerchantMenus(merchantId, strStatiDate, strStatiDate);
				msde.setDaylyTotalSaledQuantity(yestodayMenus.intValue());
				
				// 统计评论
				list = commentService.findCommentStati(merchantId, strStatiDate);
				if(list != null && list.size() > 0){
					Map<String, Object> map = list.get(0);
					Integer totalComm = Integer.parseInt(map.get("c").toString());
					msde.setDaylyTotalComment(totalComm);
					Object grade = map.get("s");
					if(grade != null){
						Integer totalScore = Integer.parseInt(grade.toString());
						msde.setDaylyTotalCommentScore(totalScore);
					} else {
						msde.setDaylyTotalCommentScore(0);
					}
				}
				
				msde.setUpdateDate((int)(statisticsDate.getMillis()/1000));
				merchatStatisticsDalyService.saveDayly(msde);
				
				// 日结算统计
				merchantSettlementService.settleDayly(merchantId);
			
			} catch (Exception e) {
				logger.error("商家【" + merchantId + "】日统计失败！", e);
			}
		}
		
		j.setSuccess(true);
		j.setMsg("商家日统计!!");
		return j;
	}
	
	/**
	 * 返点计算
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/rebate")
	@ResponseBody
	public AjaxJson rebate(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		try {
			logger.info("定时任务-商家每天返点奖励补贴计算");
			rebateService.statMerchantRebateByEveryday();
		} catch (Exception e) {
			logger.error("商家每天返点奖励补贴计算失败！", e);
		}
		
		try {
			logger.info("定时任务-物流人员每天奖励计算");
			rebateService.statCourierRebateByEveryday();
		} catch (Exception e) {
			logger.error("物流人员每天奖励计算！", e);
		}
	
		j.setSuccess(true);
		j.setMsg("返点计算!!");
		return j;
	}
	
	/**
	 * 返点计算奖励补贴发放
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	@RequestMapping("/rebateGrant")
	@ResponseBody
	public AjaxJson rebateGrant(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			logger.info("定时任务-商家返点奖励补贴发放定时器");
			rebateService.payMerchantRebate();
		} catch (Exception e) {
			logger.error("商家每天返点奖励补贴计算失败！", e);
		}
		j.setSuccess(true);
		j.setMsg("返点计算奖励补贴发放成功!!");
		return j;
	}
	
	/**
	 * 修复支付状态
	 * 
	 * @return
	 */
	@RequestMapping("/paymentRecoverStatus")
	@ResponseBody
	public AjaxJson paymentRecoverStatus() {
		// 执行修复任务
		lifeBankService.executorRecoverTask();
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setStateCode("00");
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}	
}