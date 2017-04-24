package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.common.StringUtil;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.ExpenseDetailsService;
import com.courier_mana.statistics.service.IncomeDetailsService;
import com.courier_mana.statistics.service.ProfitAndLossService;
import com.wm.util.SqlUtils;

@Service
public class ProfitAndLossServiceImpl extends CommonServiceImpl implements ProfitAndLossService {
	private static final Logger logger = LoggerFactory.getLogger(ProfitAndLossServiceImpl.class);

	@Autowired
	private ExpenseDetailsService expenseDetailsService;
	
	@Autowired
	private IncomeDetailsService incomeDetailsService;
	
	@Autowired
	private CourierOrgServicI courierOrgService; 
	
	@Override
	public List<Map<String, Object>> getOrgPALList(String orgIdsStr, SearchVo timeType, Integer page, Integer rows) {
		logger.info("调用方法 getOrgPALList(orgIdsStr: {}, courierIdsStr{}, page: {}, rows: {}), 获得各网点盈亏情况列表.", orgIdsStr, page, rows);
		
		/**
		 * 获取按订单量排序的区域列表
		 */
		List<Map<String, Object>> orgList = this.getOrderedOrgList(orgIdsStr, timeType, page, rows);
		
		int num = (page - 1) * rows;	//记录序号
		
		for(Map<String, Object> item: orgList){
			item.put("rank", ++num);
			// 获取当前网点的ID
			String orgIdStr = item.get("orgId").toString();
			List<Integer> courierIds = this.courierOrgService.getCourierIdsByOrgId(orgIdStr);
			String courierIdsStr = StringUtil.checkIdsString(StringUtils.join(courierIds, ","));
			
			BigDecimal totalExpanse = BigDecimal.ZERO;			//支出
			BigDecimal wageAndDeductExpanse = BigDecimal.ZERO;	//员工支出
			BigDecimal marketingExpense = BigDecimal.ZERO;		//营销成本
			BigDecimal totalIncome = BigDecimal.ZERO;			//总收入
			
			/**
			 * 获得支出细节Map, 方便计算总支出
			 */
			Map<String, Object> wageAndDeductExpenses = this.expenseDetailsService.wageAndDeductExpense(orgIdStr, courierIdsStr, timeType);
			Map<String, Object> marketingExpenses = this.expenseDetailsService.marketingExpense(orgIdStr, timeType, true);
			
			/**
			 * 遍历所有支出项, 求和
			 */
			for(Object expenseDetail: wageAndDeductExpenses.values()){
				BigDecimal i = new BigDecimal(expenseDetail.toString());
				wageAndDeductExpanse = wageAndDeductExpanse.add(i);
			}
			for(Object expenseDetail: marketingExpenses.values()){
				BigDecimal i = new BigDecimal(expenseDetail.toString());
				marketingExpense = marketingExpense.add(i);
			}
			
			totalExpanse = wageAndDeductExpanse.add(marketingExpense);
			
			/**
			 * 获得收入细节Map, 方便计算总收入
			 */
			Map<String, Object> incomes = this.incomeDetailsService.getIncomeDetails(orgIdStr, timeType, true);
			
			/**
			 * 遍历所有收入项, 求和
			 */
			for(Object incomeDetail: incomes.values()){
				BigDecimal i = new BigDecimal(incomeDetail.toString());
				totalIncome = totalIncome.add(i);
			}
			
			item.putAll(wageAndDeductExpenses);
			item.putAll(marketingExpenses);
			item.putAll(incomes);
			item.put("wageAndDeductExpanse", wageAndDeductExpanse);
			item.put("marketingExpense", marketingExpense.setScale(2, BigDecimal.ROUND_HALF_UP));
			item.put("totalExpanse", totalExpanse.setScale(2, BigDecimal.ROUND_HALF_UP));
			item.put("totalIncome", totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
			item.put("PAL", totalIncome.subtract(totalExpanse).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return orgList;
	}
	
	/**(OvO)
	 * 获取按订单量排序的区域ID列表
	 * @param orgIdsStr	若干个区域ID(用","分隔)
	 * @param page		页码
	 * @param rows		每页显示记录数
	 * @return
	 */
	private List<Map<String, Object>> getOrderedOrgList(String orgIdsStr, SearchVo timeType, Integer page, Integer rows){
		/**
		 * 根据时间条件获取SQL条件
		 */
		String period = SqlUtils.getTimeWhere4SQL(timeType, "o.create_time");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT morg.org_id orgId, org.org_name orgName, IFNULL(COUNT(o.id),0) orderCount ");
		sql.append(" FROM `order` o, 0085_merchant_org morg, 0085_org org ");
		sql.append(" WHERE org.id = morg.org_id ");
		sql.append(period);
		sql.append(" 	AND o.pay_state='pay' AND o.state='confirm' AND (o.rstate='norefund' OR o.rstate='normal') ");
		sql.append(" 	AND morg.org_id IN ( ");
		sql.append(orgIdsStr);
		sql.append(" 	) ");
		sql.append(" 	AND o.merchant_id = morg.merchant_id ");
		sql.append(" GROUP BY morg.org_id ");
		sql.append(" ORDER BY orderCount DESC, orgId ");
		return this.findForJdbc(sql.toString(), page, rows);
	}
}
