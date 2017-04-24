package com.wm.controller.job;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.Constants;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.order.scamble.ScheduledCleanServiceI;
import com.wm.util.AliOcs;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/cache")
public class CacheJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(CacheJobController.class);

	@Autowired
	private MerchantInfoServiceI merchantInfoService;
	@Autowired
	private ScheduledCleanServiceI scheduledCleanService;

	/**
	 * 清除快递员位置
	 */
	@RequestMapping("/cleanCourierId")
	@ResponseBody
	public AjaxJson cleanCourierId(String sign, String timestamp) {
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
			logger.info("begin to clean redis Cache ...");
			scheduledCleanService.cleanExpiredCourierIds();
			logger.info("Clean redis Cache end!");
			j.setSuccess(true);
			j.setMsg("清除快递员位置成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			logger.error("清除快递员位置失败！", e);
		}
		return j;
	}
	
	/**
	 * 刷新商家来源
	 */
	@RequestMapping("/refreshMerchantSource")
	@ResponseBody
	public AjaxJson refreshMerchantSource(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		logger.info("定时任务-缓存商家ID与商家来源");
        List<Map<String, Object>> sourceMapList = merchantInfoService.getMerchantList();
        if(CollectionUtils.isNotEmpty(sourceMapList)){
	        int size = sourceMapList.size();
			logger.info("refreshMerchantSource total size:" + size);
	        for(Map<String,Object> map : sourceMapList){
	        	try {
		        	logger.debug("refreshMerchantSource current index:" + size--);
		            AliOcs.set(Constants.MERCHANT_SOURCE_KEY + map.get("merchant_id"),  map.get("merchant_source"));
	        	} catch (Exception e) {
	    			logger.error("刷新商家来源失败！", e);
	    		}
	        }
        }
		j.setSuccess(true);
		j.setMsg("刷新商家来源完成");
		return j;
	}
	

}
