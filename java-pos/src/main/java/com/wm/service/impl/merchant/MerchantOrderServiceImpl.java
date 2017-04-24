package com.wm.service.impl.merchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.baidu.BaiduMapServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantOrderServiceI;
import com.wm.service.order.OrderServiceI;

@Service("merchantOrderService")
@Transactional
public class MerchantOrderServiceImpl extends CommonServiceImpl implements MerchantOrderServiceI {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private FlowServiceI flowService;
	
	@Autowired
	private BaiduMapServiceI baiduMapService;
	
	@Value("${search_radius}")
	private String radiusFromConf;
	

	@Override
	public Map<String, Object> getCrowdsourcingType(Integer id) {
		String sql = "select id id,name name,convert(delivery_fee/100,decimal(11,2)) deliveryFee,"
				+ "convert(courier_deduct/100,decimal(11,2)) courierDeduct from tom_crowdsourcing_type where id=?";
		Map<String, Object> map = this.findOneForJdbc(sql, id);
		return map;
	}

	@Override
	public AjaxJson getMerchantAddress(Integer id){
		AjaxJson j = new AjaxJson();
		Map<String, Object> maps = new HashMap<String, Object>();
		
		MerchantEntity merchant = this.get(MerchantEntity.class, id);
		if(merchant!=null){
			Double latitude = merchant.getLat();
			Double longitude = merchant.getLng();
			
			maps.put("latitude", latitude);	  //纬度	
			maps.put("longitude", longitude);	//经度
			
			j.setObj(maps);
			j.setMsg("商家经纬度");
			j.setStateCode("00");
			j.setSuccess(true);
		}else{
			j.setMsg("商家不存在");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}

	@Override
	public List<Map<String, Object>> getAddressByName(String name, Double longitude, Double latitude, Double precision, Integer page, Integer rows) {
		logger.info("getAddressByName获取地址范围precision："+precision);
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		long radius = BaiduMapServiceI.DEFALUT_RADIUS;
		if(radiusFromConf != null){
			radius = NumberUtils.createInteger(radiusFromConf);
		}
		if(precision != null && precision != 0){
			radius = (int) (precision * 100000);
		}
		logger.info("getAddressByName获取地址范围radius："+radius);
		results = baiduMapService.searchAddressByRadis(name, longitude, latitude, radius);
		return results;
	}
	
	@Override
	public AjaxJson validatePayPassword(Integer id, String payPassword) {
		AjaxJson aj = new AjaxJson();
		
		MerchantEntity merchant = this.get(MerchantEntity.class, id);
		if(merchant!=null){
			WUserEntity user = merchant.getWuser();
			String pw = user.getPayPassword();
			
			if(pw.equals(payPassword)){
				aj.setMsg("支付密码正确");
				aj.setStateCode("00");
				aj.setSuccess(true);
			}else{
				aj.setMsg("支付密码错误");
				aj.setStateCode("01");
				aj.setSuccess(false);
			}
		}else{
			aj.setMsg("商家不存在");
			aj.setStateCode("01");
			aj.setSuccess(false);
		}
		return aj;
	}
	
	@Override
	public AjaxJson getCrowdsourcingTypeList(Integer id) {
		AjaxJson aj = new AjaxJson();
		String sql ="select id id,name name,convert(delivery_fee/100,decimal(11,2)) deliveryFee,"
				+ "convert(courier_deduct/100,decimal(11,2)) courierDeduct from tom_crowdsourcing_type "
				+ "where is_delete=0 and is_show=1 order by cro_sort asc";
		List<Map<String, Object>> orderType = this.findForJdbc(sql);
		
		if(orderType!=null && orderType.size()>0){
			MerchantEntity merchant = this.get(MerchantEntity.class, id);
			if(merchant!=null){
				WUserEntity user = merchant.getWuser();
				Double money = user.getMoney();
				String payPassword = user.getPayPassword();  //获取商家支付密码
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("orderType", orderType);	//众包类型列表
				map.put("money", money);	//商家余额
				
				if(payPassword==null){
					map.put("state", 0);	//0代表商家没有设置支付密码
				}else{
					map.put("state", 1);	//1代表商家有设置支付密码
				}
				aj.setObj(map);
				aj.setMsg("操作成功");
				aj.setStateCode("00");
				aj.setSuccess(true);
		
			}else{
				aj.setMsg("商家id不能为空");
				aj.setStateCode("01");
				aj.setSuccess(false);
			}
		}
		return aj;
	}
}