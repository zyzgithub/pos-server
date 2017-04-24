package com.courier_mana.statistics.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.statistics.service.ReorderStatisticService;

@Controller
@RequestMapping("/ci/courier/orderstat")
public class ReorderStatisticController extends BasicController {
	
	@Autowired
	private ReorderStatisticService reorderStatisticService;

	/**(OvO)
	 * 复购报表
	 * @param userId	当前登录用户ID
	 * @param period	统计时间类型
	 * @param beginTime	自定义开始时间(时间类型为other 时有效)
	 * @param endTime	自定义结束时间(时间类型为other 时有效)
	 * @param page		页码
	 * @param rows		每页显示记录数
	 * @return
	 */
	@RequestMapping("/reorderInfo")
	@ResponseBody
	public AjaxJson reorderInfo(Integer userId, String period, Integer beginTime, Integer endTime, Integer page, Integer rows){
		/**
		 * 参数检查
		 */
		if(userId == null){
			return BasicController.FAIL("01", "参数: userId 不能为空");
		}
		if(!this.isTimeConditionCorrect(period, beginTime, endTime)){
			return BasicController.FAIL("01", "period为 other时, beginTime和 endTime不能为空");
		}
		if(page == null){
			page = 1;
		}
		if(rows == null){
			rows = 5;
		}
		
		try{
			return BasicController.SUCCESS(this.reorderStatisticService.getReorderInfo(userId, period, beginTime, endTime, page, rows));
		}catch(Exception e){
			e.printStackTrace();
			return BasicController.FAIL("02", "获取复购报表数据失败");
		}
	}
	
	/**(OvO)
	 * 检查时间条件
	 * 确保period 为other 时beginTime 和endTime 不为空
	 * @param period	统计时间类型
	 * @param beginTime	自定义开始时间
	 * @param endTime	自定义结束时间
	 * @return
	 */
	private boolean isTimeConditionCorrect(String period, Integer beginTime, Integer endTime){
		if("other".equals(period)){
			if(beginTime == null || endTime == null){
				return false;
			}
		}
		return true;
	}
}
