package com.wm.controller.merchant;

import java.util.List;
import java.util.Map;

import jodd.util.StringUtil;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.merchant.MerchantOrderServiceI;


/**
 * 众包
 * @author db
 *
 */ 
@Controller
@RequestMapping("ci/merchantOrderController")
public class MerchantOrderController extends BaseController {

	@Autowired
	private MerchantOrderServiceI merchantOrderService;
	
	/**
	 * 根据Id查询众包类型信息
	 * @param id 众包订单id
	 * @return
	 */
	@RequestMapping(params = "getCrowdsourcingType")
	@ResponseBody
	public AjaxJson getCrowdsourcingType(Integer id) {
		AjaxJson j = new AjaxJson();
		if(id!=null){
			Map<String, Object> map = merchantOrderService.getCrowdsourcingType(id);
			j.setObj(map);
			j.setMsg("操作成功");
			j.setStateCode("00");
			j.setSuccess(true);
			return j;
		}else{
			j.setMsg("id不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
	}

	/**
	 * 查询所有众包类型信息
	 * @param id 商家id
	 * @return
	 */
	@RequestMapping(params = "getCrowdsourcingTypeList")
	@ResponseBody
	public AjaxJson getCrowdsourcingTypeList(Integer id){
		return merchantOrderService.getCrowdsourcingTypeList(id);
	}

	/**
	 * 查询商家注册地址
	 * @param id 商家id
	 * @return
	 */
	@RequestMapping(params = "getMerchantAddress")
	@ResponseBody
	public AjaxJson getMerchantAddress(Integer id){
		AjaxJson j = new AjaxJson();
		if(id!=null){
			return merchantOrderService.getMerchantAddress(id);
		}else{
			j.setMsg("商家id不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
	}
	
	/**
	 * 地址模糊查询
	 * @param name 查询关键字
	 * @param page 分页起始
	 * @param rows 分页行数
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param precision 公里：0.01表示1公里
	 * @return
	 */
	@RequestMapping(params = "getAddressByName")
	@ResponseBody
	public AjaxJson getAddressByName(String name, @RequestParam Double longitude, @RequestParam Double latitude, Double precision,
			Integer page, Integer rows) {
		AjaxJson j = new AjaxJson();
		//precision = 0.05;	//设置公里数
		if(page==null || rows==null){
			j.setMsg("请输入分页参数");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}else{
			List<Map<String, Object>> list = merchantOrderService.getAddressByName(name,longitude, latitude, precision,  page, rows);
			j.setObj(list);
			j.setMsg("操作成功");
			j.setStateCode("00");
			j.setSuccess(true);
			return j;
		}		
	}
	
	/**
	 * 验证支付密码是否正确
	 * @param id 商家id
	 * @param payPassword 支付密码
	 * @return
	 */
	@RequestMapping(params = "validatePayPassword")
	@ResponseBody
	public AjaxJson validatePayPassword(Integer id, String payPassword){
		AjaxJson j = new AjaxJson();
		if(id==null || StringUtil.isEmpty(payPassword)){
			j.setMsg("id和支付密码不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}else{
			return merchantOrderService.validatePayPassword(id, payPassword);
		}
	}

}
