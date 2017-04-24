package com.courier_mana.statistics.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.statistics.service.CourierStatisticService;

/**
 * 统计-快递员统计模块Controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/courierStat")
public class CouriersStatisticController extends BasicController{
	private final static Logger logger = LoggerFactory.getLogger(CouriersStatisticController.class);
	
	@Autowired
	private CourierStatisticService courierStatisticService;
	
	/**
	 * 快递员实时统计
	 * @param courierId		快递员ID(必选)
	 * @param orgId			orgId：机构ID(可选)(用于搜索)
	 * @param page			page：页数(可选, 默认为1)
	 * @param rowsPerPage	rowsPerPage：每页显示的记录数(可选, 默认为10)
	 * @param interval		区间    day日,week周,month月
	 * @return
	 */
	@RequestMapping("/realTimeStat")
	@ResponseBody
	public AjaxJson realTimeStatistic(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage,String interval){
		logger.info("Invoke method: realTimeStatistic， params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId, page, rowsPerPage);
		/** 
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		try{
			if(interval == null || interval.equals(""))interval = "day";
			return SUCCESS(this.courierStatisticService.realTimeStatistic(courierId, orgId, page, rowsPerPage, interval));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取快递员实时统计失败: " + e.getMessage());
		}
	}

    /**
     * 快递员订单信息
     * @param courierId     快递员ID(必选)
     * @param page          page：页数(可选, 默认为1)
     * @param rowsPerPage   rowsPerPage：每页显示的记录数(可选, 默认为10)
     * @return
     */
    @RequestMapping("/courierOrderInfos")
    @ResponseBody
    public AjaxJson getCourierOrderInfos(Integer courierId, Integer page, Integer rowsPerPage){
        logger.info("Invoke method: realTimeStatistic， params: courierId - {}, page - {}, rowsPerPage - {}", courierId, page, rowsPerPage);
        /** 
         * 检查必要的参数
         */
        if(courierId == null){
            return FAIL("01","参数: 快递员ID,不能为空");
        }
        try{
            return SUCCESS(this.courierStatisticService.getCourierOrderInfos(courierId, page, rowsPerPage));
        }catch(Exception e){
            e.printStackTrace();
            return FAIL("02", "获取快递员实时统计失败: " + e.getMessage());
        }
    }
	
	
	/**
	 * 快递员实时排行
	 * @param courierId		快递员ID(必选)
	 * @param orgId			orgId：机构ID(可选)(用于搜索)
	 * @param page			page：页数(可选, 默认为1)
	 * @param rowsPerPage	rowsPerPage：每页显示的记录数(可选, 默认为10)
	 * @return
	 */
	@RequestMapping("/realTimeRank")
	@ResponseBody
	public AjaxJson realTimeRank(Integer courierId, Integer orgId, Integer page, Integer rowsPerPage){
		logger.info("Invoke method: realTimeRank， params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId, page, rowsPerPage);
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		try{
			return SUCCESS(this.courierStatisticService.realTimeRank(courierId, orgId, page, rowsPerPage));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取快递员实时排行失败: " + e.getMessage());
		}
	}
	
	/**
	 * 快递员实时排行前三
	 * @param courierId     快递员ID(必选)
	 * @param orgId			机构ID
	 * @param interval		区间    day日,week周,month月
	 * @return
	 */
	@RequestMapping("/firstThreeRank")
	@ResponseBody
	public AjaxJson firstThreeRank(Integer courierId, Integer orgId,String interval){
		logger.info("Invoke method: realTimeRank， params: courierId - {}, orgId - {}, page - {}, rowsPerPage - {}", courierId, orgId);
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		if(interval == null){
			return FAIL("01","参数: 搜索区间,不能为空");
		}
		try {
			return SUCCESS(this.courierStatisticService.firstThreeRank(courierId, orgId, interval));
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL("02", "获取快递员实时排行失败: " + e.getMessage());
		}
		
	}
}
