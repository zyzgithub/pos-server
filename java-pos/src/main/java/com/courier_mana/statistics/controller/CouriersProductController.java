package com.courier_mana.statistics.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.CouriersProductService;

/**(OvO)
 * 菜品统计模块Controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/product")
public class CouriersProductController extends BasicController{
	private final static Logger logger = LoggerFactory.getLogger(CouriersProductController.class);
	
	@Autowired
	private CouriersProductService couriersProductService;
	
	/**(OvO)
	 * 
	 * @param vo			搜索条件VO(时间, 地区ID)
	 * @param courierId		快递员ID(必选)
	 * @param page			页数(可选, 默认为1)
	 * @param rowsPerPage	每页显示的记录数(可选, 默认为10)
	 * @return	返回菜品排行信息, 包含
	 * 			rank			排名
	 * 			name			菜名
	 * 			quantity		销售份额
	 * 			totalQuantity	累计销售份额
	 */
	@RequestMapping("/productRank")
	@ResponseBody
	public AjaxJson productRank(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage){
		logger.info("Invoke method: productRank, params: courierId - {}, vo.timeType - {}, vo.orgId - {}, vo.beginTime - {}, vo.endTime - {}, page - {}, rowsPerPage - {}",
															courierId, vo.getTimeType(), vo.getOrgId(), vo.getBeginTime(), vo.getEndTime(), page, rowsPerPage);
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		try{
			return SUCCESS(this.couriersProductService.productRank(vo, courierId, page, rowsPerPage));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取菜品排名失败: " + e.getMessage());
		}
	}
}
