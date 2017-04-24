package com.wm.service.merchant;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;



public interface MerchantOrderServiceI extends CommonService {

	/**
	 * 根据id查询众包类型信息
	 * @param id 众包订单id
	 * @return
	 */
	public Map<String, Object> getCrowdsourcingType(Integer id);
	
	/**
	 * 查询所有众包类型信息
	 * @param id 商家id
	 * @return
	 */
	public AjaxJson getCrowdsourcingTypeList(Integer id);
	
	/**
	 * 查询商家注册地址
	 * @param id 商家id
	 * @return
	 */
	public AjaxJson getMerchantAddress(Integer id);
	
	/**
	 * 地址模糊查询
	 * @param name 查询关键字
	 * @param page 分页起始,起始为1,不能为0
	 * @param rows 分页行数
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param precision 公里：0.01表示1公里
	 * @return
	 */
	public List<Map<String, Object>> getAddressByName(String name, Double longitude, Double latitude, Double precision, Integer page, Integer rows);
		
	/**
	 * 验证支付密码是否正确
	 * @param id 商家id
	 * @param payPassword 支付密码
	 * @return
	 */
	public AjaxJson validatePayPassword(Integer id, String payPassword);
	
}
