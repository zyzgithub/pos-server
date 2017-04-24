package com.wm.service.impl.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.base.VO.PageVO;
import com.wm.service.user.CashLoginLogServiceI;

@Service("cashierLoginLogService")
public class CashierLoginLogServiceImpl extends CommonServiceImpl implements
		CashLoginLogServiceI {

	
	@Override
	public PageVO<MerchantCashierLoginLog> getLoginLogs(Integer merchantId, Integer pageNo, Integer pageSize){
		//页码从0页开始，但是前端是从第1页开始
		pageNo = pageNo < 1 ? 0: pageNo - 1;
		//获取总记录的条数
		Long totalRecords = getCountForJdbcParam("select count(*) from 0085_cashier_login_log where merchant_id=?", new Object[]{merchantId});
		
		PageVO<MerchantCashierLoginLog> pageVo = new PageVO<MerchantCashierLoginLog>(totalRecords.intValue(), pageSize);
		
		//分页获取记录
		String sql = "select c_l.id, c.name, c_l.login_type, c_l.create_time from 0085_cashier_login_log c_l,0085_cashier c where c_l.cashier_id=c.id AND c_l.merchant_id=? order by c_l.create_time desc";
		List<Map<String, Object>> loginLogs = findForJdbcParam(sql, pageNo, pageSize, merchantId);
		List<MerchantCashierLoginLog> results = new ArrayList<MerchantCashierLoginLog>();
		//对记录进行重新组合
		if(CollectionUtils.isNotEmpty(loginLogs)){
			
			//解析登录、退出时间
			for(Map<String, Object> loginLog: loginLogs){
				String date = new DateTime(Long.parseLong(loginLog.get("create_time").toString())*1000).toString("yyyy-MM-dd");
				String time = new DateTime(Long.parseLong(loginLog.get("create_time").toString())*1000).toString("HH:mm:ss"); 
				loginLog.put("date", date);
				loginLog.put("time", time);
			}
			
			//上一条记录的日期
			String preDate = null;
			for(Map<String, Object> loginLog: loginLogs){
				String curDate = loginLog.get("date").toString();
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", loginLog.get("id"));
				map.put("name", loginLog.get("name"));
				String loginType = Integer.parseInt(loginLog.get("login_type").toString()) == 1?"登录":"退出";
				map.put("loginType", loginType);
				map.put("time", loginLog.get("time"));
				
				//第一条记录或者当前日期与上一条记录日期不相等
				if(preDate == null || !StringUtils.equals(preDate, curDate)){
					MerchantCashierLoginLog loginLogModel = new MerchantCashierLoginLog();
					loginLogModel.setDate(curDate);
					
					List<Map<String, Object>> loginRecords = new ArrayList<Map<String,Object>>();
					loginRecords.add(map);
					loginLogModel.setLoginRecords(loginRecords);
					
					results.add(loginLogModel);
				}
				else {
					MerchantCashierLoginLog loginLogModel = results.get(results.size()-1);
					loginLogModel.getLoginRecords().add(map);
				}
				
				preDate = curDate;
			}
		}
		pageVo.setResults(results);
		return pageVo;
	}

	public static class MerchantCashierLoginLog{
		private String date;
		private List<Map<String, Object>> loginRecords;
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public List<Map<String, Object>> getLoginRecords() {
			return loginRecords;
		}
		public void setLoginRecords(List<Map<String, Object>> loginRecords) {
			this.loginRecords = loginRecords;
		}
	}
}
