package com.wm.service.impl.user;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;
import com.wm.service.user.CustTypeRuleServiceI;
import com.wm.util.AliOcs;

@Service("custTypeRuleService")
@Transactional
public class CustTypeRuleServiceImpl extends CommonServiceImpl implements CustTypeRuleServiceI {
	
	private final static Logger logger = LoggerFactory.getLogger(CustTypeRuleServiceImpl.class);
	
	public static final String MEM_KEY_CUST_TYPE_RULE = "mem_key_cust_type_rule";
	private static final String getRules = "select id,type_name,amount from 0085_custtype_rule where invalid=0 order by amount asc ";
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRules(){
		Object custRule = AliOcs.getObject(MEM_KEY_CUST_TYPE_RULE);
		if(null == custRule){
			List<Map<String, Object>> list = this.findForJdbc(getRules);
			logger.info("getRules from db : {}", JSONUtils.toJSONString(list));
			AliOcs.set(MEM_KEY_CUST_TYPE_RULE, list);
			return list;
		} else {
			logger.info("getRules from memcached : {}", custRule);
			return (List<Map<String, Object>>) custRule;
		}
	}
	
	public String getCustTypeByConsumeAmount(Double consumeAmount){
		if(consumeAmount == null){
			consumeAmount = 0d;
		}
		List<Map<String, Object>> rules = this.getRules();
		for(Map<String, Object> rule : rules){
			Object amount = rule.get("amount");
			if(amount != null){
				if(Double.parseDouble(amount.toString()) >= consumeAmount) {
					return rule.get("type_name").toString();
				}
			} else {
				break;
			}
		}
		return "新";
	}
}