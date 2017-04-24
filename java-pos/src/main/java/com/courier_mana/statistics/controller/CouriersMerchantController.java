package com.courier_mana.statistics.controller;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.CouriersMerchantService;

/**(OvO)
 * 统计-店铺统计模块Controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/merchant")
public class CouriersMerchantController extends BasicController{
	private final static Logger logger = LoggerFactory.getLogger(CouriersMerchantController.class);
	
	@Autowired
	private CouriersMerchantService courierMerchantService;
	
	/**(OvO)
	 * 统计-店铺统计页面
	 * @param vo			搜索条件VO(时间, 地区)
	 * @param courierId		快递员ID(必选)
	 * @param page			page 页数(可选, 默认为1)
	 * @param rowsPerPage	rowsPerPage 每页显示的记录数(可选, 默认为10)
	 * @param merchantType	店铺类型(a: 所有类型; f: 快餐店铺; d: 饮品店铺; c: 社区店铺; o: 其他店铺)
	 * @return	统计-店铺统计页面中所有信息
	 */
	@RequestMapping("/merchantStat")
	@ResponseBody
	public AjaxJson merchantStat(SearchVo vo, Integer courierId, Integer page, Integer rowsPerPage, String merchantType){
		logger.info("Invoke method: merchantStat, params: courierId - {}, vo.timeType - {}, vo.orgId - {}, vo.beginTime - {}, vo.endTime - {}, page - {}, rowsPerPage - {}, merchantType - {}",
															courierId, vo.getTimeType(), vo.getOrgId(), vo.getBeginTime(), vo.getEndTime(), page, rowsPerPage, merchantType);
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		try{
			/**
			 * 构造一个用于返回结果的Map
			 */
			Map<String, Object> result = new HashMap<String, Object>();
			/**
			 * 先获取店铺数量信息
			 */
			result.put("merchantCount", this.courierMerchantService.merchantsCount(vo, courierId));
			/**
			 * 然后获取店铺排行(含最近3天销售记录)信息
			 */
			result.put("rankList", this.courierMerchantService.merchantsRank(vo, courierId, page, rowsPerPage, merchantType));
			
			return SUCCESS(result);
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺统计失败: " + e.getMessage());
		}
	}
}
