/**
 * 
 */
package com.wm.service.partnerDevelop;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.partnerDevelop.dto.PartnerDevelopDTO;

public interface PartnerDevelopServiceI extends CommonService{

	public List<Map<String,Object>> getPartnerDevelopHis(Integer userId, int page, int rows) ;
	
	public AjaxJson savePartnerDevelop(PartnerDevelopDTO partnerDevelop);

	PartnerDevelopDTO getPartnerDevelopDtl(Integer id);

	List<String> getSignAmount();
}
