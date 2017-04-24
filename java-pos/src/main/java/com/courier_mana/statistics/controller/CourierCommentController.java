package com.courier_mana.statistics.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.service.CourierCommentService;

@Controller
@RequestMapping("/ci/courier/admin/comment")
public class CourierCommentController extends BasicController {

	private final static Logger logger = LoggerFactory.getLogger(CourierCommentController.class);
	
	@Autowired
	private CourierCommentService courierCommentServiceImpl;
	
	
	/**(OvO)
	 * 评价统计-大项统计
	 * @return
	 */
	@RequestMapping("/getCommentStatistic")
	@ResponseBody
	public AjaxJson getCommentStatistic(Integer userId, SearchVo vo){
		AjaxJson ajaxJson = null;
		
		if(userId==null){
			ajaxJson = FAIL("01","用户ID不能为空");
		}else{
			try {
				ajaxJson= SUCCESS(courierCommentServiceImpl.getCommentStatistic(userId, vo));
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	/**(OvO)
	 * 评价统计-评论列表
	 * @return
	 */
	@RequestMapping("/getCommentList")
	@ResponseBody
	public AjaxJson getCommentList(Integer userId, SearchVo vo, Integer commentGrade, Integer page, Integer rows){
		/**
		 * 参数检查
		 */
		if(userId == null){
			return FAIL("01","用户ID不能为空");
		}
		if(page == null){
			page = 1;
		}
		if(rows == null){
			rows = 10;
		}
		if(commentGrade != null){
			if(commentGrade > 4){
				commentGrade = 4;
			}else if(commentGrade < 1){
				commentGrade = 1;
			}
		}
		try {
			return SUCCESS(courierCommentServiceImpl.getCommentList(userId, vo, commentGrade, page, rows));
		} catch (Exception e) {
			e.printStackTrace();
			return FAIL("02",e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * 根据星级不同查询不同的评价
	 * @param courierId 快递员id
	 * @param star星级
	 * @return
	 */
	@RequestMapping("/getCommentByStar")
	@ResponseBody
	public AjaxJson getCommentByStar(Integer courierId,Integer star,@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="rows") Integer rows){
		AjaxJson ajaxJson = null;
		logger.info("invoke class getCommentByStar ：params:{}{}{}{}",courierId,star,page,rows);
		if(page==null&&rows==null){
			page=1;
			rows=10;
		}
		if(courierId==null){
			ajaxJson = FAIL("01","参数错误");
		}else{
			try {
				ajaxJson= SUCCESS(courierCommentServiceImpl.getCommentByStar(courierId, star, page, rows));
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 得到管辖下所有订单评价
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getComment")
	@ResponseBody
	public AjaxJson getComment(Integer courierId,@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="rows") Integer rows){
		AjaxJson ajaxJson = null;
		logger.info("invoke class getComment ：params:{}{}{}{}",courierId,page,rows);
		if(page==null&&rows==null){
			page=1;
			rows=10;
		}
		if(courierId==null){
			ajaxJson = FAIL("01","参数错误");
		}else{
			try {
				ajaxJson= SUCCESS(courierCommentServiceImpl.getComment(courierId, page, rows));
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 获得详细评价
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/getDetailComment")
	@ResponseBody
	public AjaxJson getDetailComment(Integer commentId){
		AjaxJson ajaxJson = null;
		List<Map<String,Object>> list = null;
		Map<String,Object> map = new HashMap<String,Object>();
		logger.info("invoke class getDetailComment ：params:{}",commentId);
	
		if(commentId==null){
			ajaxJson = FAIL("01","参数错误");
		}else{
			try {
				Map<String, Object> temp = null;
				temp = courierCommentServiceImpl.getDetailComment(commentId);
				if(temp != null && temp.size()!=0){
					map = temp;
					list = courierCommentServiceImpl.getReturnVisit(((BigInteger)map.get("id")).intValue());
					if(list.size()==0){
						map.put("returnVisit", 01);
					}else if((Integer)list.get(0).get("courierId")==0){
						map.put("returnVisit", 02);
					}else if((Integer)list.get(0).get("courierId")!=0){
						map.put("returnVisit", 03);
					}
				}
				ajaxJson= SUCCESS(map);
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 根据用户等级不同查询不同的评价(根据消费总金额来决定)
	 * @param max 上限金额
	 * @param min 下限金额
	 * @param page
	 * @param rows
	 * 
	 * @return
	 */
	@RequestMapping("/getCommentByCustType")
	@ResponseBody
	public AjaxJson getCommentByCustType(String userType,@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="rows") Integer rows,Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke class getCommentByCustType ：params:{}{}{}{}{}",courierId,page,rows,userType);
		if(page==null&&rows==null){
			page=1;
			rows=10;
		}
		if(courierId==null || userType==null){
			ajaxJson = FAIL("01","参数错误");
		}else{
			try {
				Double min = .0;
				Double max = null;
				List<Map<String, Object>> typeList = this.courierCommentServiceImpl.getCustTypeList();
				for(int i=typeList.size()-1; i>=0; i--){
					Map<String, Object> listItem = typeList.get(i);
					if(listItem.get("typeName").equals(userType) && i>0){
						min = Double.valueOf(listItem.get("amount").toString());
						max = Double.valueOf(typeList.get(i-1).get("amount").toString());
						break;
					}
					if(listItem.get("typeName").equals(userType) && i==0){
						min = Double.valueOf(listItem.get("amount").toString());
						break;
					}
				}
				ajaxJson= SUCCESS(courierCommentServiceImpl.getCommentByCustType(min/100, max == null?null:max/100, page, rows, courierId));//在report库里面，amount是以分为单位，所以此处作单位换算
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 获得客户等级类型列表
	 * @return
	 */
	@RequestMapping("/getCustTypeList")
	@ResponseBody
	public AjaxJson getCustTypeList(){
		AjaxJson ajaxJson = null;
	

			try {
				ajaxJson= SUCCESS(courierCommentServiceImpl.getCustTypeList());
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	
	/**
	 * 搜索功能
	 * @param courierIds
	 * @param page
	 * @param rows
	 * @param searchVo
	 * @return
	 */
	@RequestMapping("/orderCommentSearch")
	@ResponseBody
	public AjaxJson orderCommentSearch(@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="rows") Integer rows,SearchVo vo){
		AjaxJson ajaxJson = null;
		logger.info("invoke class orderCommentSearch ：params:{}{}{}{}{}{}",vo.getBeginTime(),page,rows,vo.getEndTime(),vo.getTimeType(),vo.getOrgId());
		if(page==null&&rows==null){
			page=1;
			rows=10;
		}
		if(vo.getOrgId()==null){
			ajaxJson = FAIL("01","参数错误");
		}else{
			try {
				List<Integer> orgIds= courierCommentServiceImpl.getOrgIds(vo.getOrgId());
				ajaxJson= SUCCESS(courierCommentServiceImpl.orderCommentSearch(orgIds, page, rows, vo));
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson = FAIL("02",e.getMessage());
			}
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
}
