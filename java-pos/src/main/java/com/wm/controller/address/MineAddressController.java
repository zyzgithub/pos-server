package com.wm.controller.address;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Lists;
import com.team.wechat.util.MapUtil;
import com.wm.controller.address.dto.AddressDTO;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.building.BuildingEntity;
import com.wm.entity.location.Location;
import com.wm.service.address.AddressServiceI;

@Controller
@RequestMapping("/address")
public class MineAddressController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(MineAddressController.class);
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private AddressServiceI addressService;
	
	@Value("${serviceScope}")
	private String serviceScope;

	@RequestMapping(value="/near.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> nearByLocation(HttpServletRequest request, Double lng, Double lat) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Location location = getLocation(request);
		List<BuildingEntity> buildingList = Lists.newArrayList();
		Double precision = Double.parseDouble(serviceScope);
		
		logger.info("---------lng"+lng+"-------------lat"+lat);
		// 查询用户附近的大厦列表
		if(location != null){
			buildingList = systemService.findHql(
					"from BuildingEntity "
					+"where latitude between ? and ? and longitude between ? and ? ",
					location.getLat() - precision, location.getLat() + precision , location.getLng() - precision, location.getLng() + precision);
			if(buildingList != null && buildingList.size() > 0){
				for (BuildingEntity b : buildingList) {
					b.setDistance(MapUtil.GetShortDistance(location.getLng(), location.getLat(), b.getLng(), b.getLat()));
				}
			}
		} else if (lng != null && lat != null) {
			buildingList = systemService.findHql(
					"from BuildingEntity "
							+"where latitude between ? and ? and longitude between ? and ? ",
							lat - precision, lat + precision , lng - precision, lng + precision);
			
			if(buildingList != null && buildingList.size() > 0){
				for (BuildingEntity b : buildingList) {
					b.setDistance(MapUtil.GetShortDistance(lng, lat, b.getLng(), b.getLat()));
				}
			}
		}
		Collections.sort(buildingList, new Comparator<BuildingEntity>() {
			@Override
			public int compare(BuildingEntity o1, BuildingEntity o2) {
				Double plus = o1.getDistance() - o2.getDistance();
				return plus.intValue();
			}
		});
		
//		if(buildingList != null && buildingList.size() > 0){
//			for (BuildingEntity b : buildingList) {
//				System.out.println(b.getDistance());
//			}
//		}
		
		//加入其他地址选择
		BuildingEntity b = new BuildingEntity();
		b.setId(0);b.setName("其他地址");b.setAddress("配送时间60分钟以内");
		b.setFirstFloor(0);b.setLastFloor(0);
		buildingList.add(b);
		
		map.put("state", "success");
		map.put("building", buildingList);
		return map;
	}
	
	@RequestMapping(value="/add.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addAddress(@Valid AddressDTO addr, BindingResult result, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!result.hasErrors()) {
			Integer userId = getUserId(request, null);
			if(userId != null) {
				//取消已有默认地址状态
				addressService.cancelAddrDefault(userId);
				//新建地址
				AddressEntity entity = addr.getEntity();
				entity.setUserId(userId);
				addressService.save(entity);
				map.put("state", "success");
				return map;
			} 
		} 
		map.put("state", "fail");
		return map;
	}
	
	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> list(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = getUserId(request, null);
		if(null != userId) {
			List<AddressEntity> list = addressService.findByProperty(AddressEntity.class, "userId", userId);
			map.put("list", list);
			map.put("state", "success");
		} else {
			map.put("state", "fail");
		}
		return map;
	}
	
	@RequestMapping(value="/change.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> change(@RequestParam Integer addressId, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = getUserId(request, null);
		if(null != userId) {
			addressService.updateAddrDefault(userId, addressId, "Y");
			AddressEntity e = addressService.queryLasted(userId);
			map.put("state", "success");
			map.put("addr", e);
		} else {
			map.put("state", "fail");
		}
		return map;
	}
}
