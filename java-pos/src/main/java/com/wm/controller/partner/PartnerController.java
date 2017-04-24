package com.wm.controller.partner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.order.dto.OrderFromMerchantDTO;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.partner.PartnerVo;
import com.wm.entity.user.WUserEntity;
import com.wm.service.address.AddressServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.partner.PartnerServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.IPUtil;

/**   
 * @Title: Controller
 * @Description: 合作方
 * @author wuyong
 * @date 2015-09-30 10:41:30
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/partnerController")
public class PartnerController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PartnerController.class);

	@Autowired
	private PartnerServiceI partnerService;
	
	@Autowired
	private AddressServiceI addressService;
	
	@Autowired
	private WUserServiceI wUserService;
	
	@Autowired
	private OrderServiceI orderService;
	
	@Autowired
	private OrderStateServiceI orderStateService;
	
	@Autowired
	private PrintServiceI printService;
	
	@Autowired
	private MenuServiceI menuService;
	
	
	/**
	 * 根据合作方标识ID获取商家和菜品信息
	 * @param openid
	 * @return
	 */
	@RequestMapping(params = "findMerchantMenuList")
	@ResponseBody
	public AjaxJson findMerchantMenuList(String openid,@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("查询失败");
		j.setSuccess(false);
		j.setStateCode("01");
		if(StringUtil.isEmpty(openid)){
			return j;
		}
		PartnerVo partnerVo = partnerService.findMerchantMenuList(openid,page,rows);
		if (partnerVo!=null) {
			j.setObj(partnerVo);
			j.setMsg("查询成功");
			j.setSuccess(true);
			j.setStateCode("00");
		}
		return j;
	}
	
	/**
	 * 创建第三方电话订单
	 * @param orderDTO
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "createMobileOrder", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson createMobileOrder(@Valid OrderFromMerchantDTO orderDTO, BindingResult result, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();

		if(!result.hasErrors()) {
			return createOrderFromMobile(orderDTO, request);
		} 
		
		j.setMsg("提交参数有误");
		j.setStateCode("01");
		j.setSuccess(false);
		return j;
	}
	
	public AjaxJson createOrderFromMobile(OrderFromMerchantDTO orderDTO, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//step 1 根据手机号查询，该手机号是否存在
		logger.info("--------电话订单，来源"+orderDTO.getFromType()+", 电话来源:"+orderDTO.getMobile());
		WUserEntity user = wUserService.getUserByUserNameOrMobile(orderDTO.getMobile(), "user");
		//如果该手机号不存在，插入一条新数据
		if(null == user) {
			logger.info("---------电话订单，电话用户未存在数据库中，准备写入数据库");
			user = new WUserEntity();
			user.setMobile(orderDTO.getMobile());
			user.setUsername(orderDTO.getMobile());
			user.setNickname(orderDTO.getRealname());
			user.setCreateTime(DateUtils.getSeconds());
			user.setIp(IPUtil.getRemoteIp(request));
			wUserService.save(user);
			logger.info("------电话订单,保存新用户成功----");
		}
		
		AddressEntity address = new AddressEntity();
		address.setUserId(user.getId());
		address.setName(orderDTO.getRealname());
		address.setMobile(orderDTO.getMobile());
		address.setCreateTime(new Date());
		address.setAddressDetail(orderDTO.getAddress());
		address.setIsDefault("N");
		address.setBuildingFloor(orderDTO.getFloor());
		address.setBuildingId(orderDTO.getBuildingId());
		address.setBuildingName(orderDTO.getBuildingName());
		addressService.save(address);
		
		//step 3 验证菜品库存是否还有
		logger.info("电话订单的菜单详情："+orderDTO.getParams());
		j = orderService.verifyMenuQuantity(orderDTO.getParams(), 0);
		if (j.isSuccess()) {
			Integer orderId = orderService.createMobileOrder(user, address, orderDTO);
			if(null != orderId) {
				logger.info("-----电话订单生成成功，id--"+orderId);
				//生成订单。电话订单不需经过网上付款步骤
				//生成排号及基本信息并更新
				OrderEntity order = orderService.get(OrderEntity.class, orderId);
				String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
				order.setOrderNum(orderNum);
				order.setState("accept");
				order.setPayState(orderDTO.getPayState());
				order.setAccessTime(DateUtils.getSeconds());
				order.setPayTime(orderDTO.getPayTime());
				orderService.updateEntitie(order);
				//打印订单小票
				printService.print(order, false);
				// 销量统计
				menuService.buyCount(orderId);
				//状态日志
				orderStateService.createPhoneOrderState(orderId);
				//推送快递员
				orderService.pushOrder(orderId);
				
				Map<String, Object> objs = new HashMap<String, Object>();
				objs.put("orderId", orderId);
				objs.put("orderMoney", order.getOrigin());
				objs.put("orderNum", order.getOrderNum());
				objs.put("orderPayId", order.getPayId());
				objs.put("userId", user.getId());
				objs.put("username", user.getUsername());
				objs.put("password", "");

				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				list.add(objs);
				j.setObj(list);
				j.setSuccess(true);
				j.setMsg("下单成功");
				return j;
				
			} else {
				j.setMsg("生成订单失败");
				j.setStateCode("01");
				j.setSuccess(false);
				return j;
			}
			
		} 
		return j;
	}
}
