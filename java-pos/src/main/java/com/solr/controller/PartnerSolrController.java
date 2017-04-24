package com.solr.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solr.service.PartnerSolrServiceI;

@Controller
@RequestMapping("ci/partnerSoloController")
@SuppressWarnings("rawtypes")
public class PartnerSolrController {
//	private static final Logger logger = Logger.getLogger(BaseInterfaceController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private PartnerSolrServiceI partnerSolrService;
	
	@RequestMapping(params = "getMerchantList")
	@ResponseBody
	public AjaxJson getMerchantList(HttpServletRequest request,String group_id,String pt, String sortType,String start,String rows,String searchKeyword) {
		AjaxJson j = new AjaxJson();
		
		List list = partnerSolrService.queryMerchantList(group_id,pt, sortType, start, rows, searchKeyword);
		if(list!=null){
			j.setObj(list);
		}
		j.setMsg("获取商户列表成功！");
		return j;
	}
}
