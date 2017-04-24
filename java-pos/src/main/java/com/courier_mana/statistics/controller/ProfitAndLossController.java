package com.courier_mana.statistics.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.StringUtil;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.statistics.service.ProfitAndLossService;

/**(OvO)
 * 盈亏统计Controller
 * @author hyj
 */

@Controller
@RequestMapping("/ci/courier/PAL")
public class ProfitAndLossController extends BasicController {
	@Autowired
	private ProfitAndLossService profitAndLossService;
	
	@Autowired
	private CourierOrgServicI courierOrgService; 
	
	@RequestMapping("/PALList")
	@ResponseBody
	public AjaxJson getOrgPALList(Integer userId, SearchVo vo, Integer page, Integer rows){
		/**
		 * 参数检查
		 */
		if(userId == null){
			return BasicController.FAIL("01", "参数: userId 不能为空");
		}
		if("other".equals(vo.getTimeType()) && vo.getBeginTime() >= vo.getEndTime()){
			return BasicController.FAIL("01", "搜索条件中结束时间(" + vo.getEndTime() + ")应该大于开始时间(" + vo.getBeginTime() + ")");
		}
		if(page == null){
			page = 1;
		}
		if(rows == null){
			rows = 5;
		}
		try{
			/**
			 * 获取用户管理的区域ID
			 * 以及用户管理的快递员
			 */
			List<Integer> l6orgIds = this.courierOrgService.getManageL6OrgId(userId);
			String l6orgIdsStr = StringUtil.checkIdsString(StringUtils.join(l6orgIds, ","));

			// 各网点盈亏情况列表
			return BasicController.SUCCESS(this.profitAndLossService.getOrgPALList(l6orgIdsStr, vo, page, rows));
		}catch(Exception e){
			e.printStackTrace();
			return BasicController.FAIL("02", "获取盈亏信息失败");
		}
	}
}
