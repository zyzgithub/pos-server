package com.wm.service.networkDevelop;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;

import com.wm.controller.networkDevelop.dto.CommunityInfoDTO;
import com.wm.controller.networkDevelop.dto.CompetitionAnalysisDTO;
import com.wm.controller.networkDevelop.dto.MerchantInfoDTO;
import com.wm.controller.networkDevelop.dto.ShopDtlDTO;

public interface NetworkDevelopServiceI {

	public AjaxJson saveCommunityInfo(CommunityInfoDTO dto);
	
	public AjaxJson saveCompetitionAnalysis(CompetitionAnalysisDTO dto);
	
	public AjaxJson saveMerchantInfo(MerchantInfoDTO dto);
	
	public AjaxJson saveShopDtl(ShopDtlDTO dto);
	
	/**
	 * 获取开拓网点记录
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getNetworkRecord(Integer courierId, int page, int rows);
	
	public AjaxJson getNetworkDevDtl(Integer id);
	
	
}
