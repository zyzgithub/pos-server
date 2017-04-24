package com.wm.controller.coupons;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.team.wechat.util.JsapiTicket;
import com.wm.controller.coupons.vo.ReceiverVo;
import com.wm.controller.coupons.vo.ShareVo;
import com.wm.entity.coupons.CouponsPublishEntity;
import com.wm.entity.coupons.CouponsUserEntity;
import com.wm.service.coupons.CouponsServiceI;
import com.wp.AccessTokenContext;
import com.wp.AdvancedUtil;
import com.wp.ConfigUtil;

@Controller
@RequestMapping("/coupons")
public class CouponsController extends BaseController{

	@Autowired
	private CouponsServiceI couponsService;
	
	private static final Logger logger = Logger.getLogger(CouponsController.class);
	
	/**
	 * 生成推送优惠券的点击链接
	 * @return
	 */
	@RequestMapping(value="/create/push.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, String> push() {
		Map<String, String> map = new HashMap<String, String>();
		ShareVo vo = new ShareVo();
		
		CouponsPublishEntity publish = new CouponsPublishEntity();
		publish.setUserId(1);
		publish.setCouponsType("push");
		publish.setCouponsSerial(vo.getSerial());
		publish.setCreateTime(DateUtils.getSeconds());
		publish.setEndTime(DateUtils.getSeconds(org.apache.commons.lang3.time.DateUtils.addMonths(new Date(), 2)));
		publish.setCouponsNum(0);
		publish.setStatus("Y");
		
		couponsService.save(publish);
		
		map.put("status", "success");
		map.put("url", vo.getLink());
		
		return map;
	}
	
	/**
	 * 跳转到分享页面
	 * @param score
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/share.do", method = RequestMethod.GET)
	private String share(String score, Model model) {
		JsapiTicket ticket = JsapiTicket.getJsapiTicket(AccessTokenContext.getJsapiTicket(), ConfigUtil.SHARE_COUPONS_URL+score);
		ShareVo vo = new ShareVo();
		model.addAttribute("jsTicket", ticket);
		model.addAttribute("share", vo);
		model.addAttribute("score", score);
		
		return "takeout/couponShare";
	}
	
	/**
	 * 保存分享记录
	 * @param serial
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/share.do", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> saveShare(String serial, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		logger.info("serial-------"+serial);
		
		if(StringUtils.isEmpty(serial))
			return map;
		
		Integer userId = getUserId(request, null);
		CouponsPublishEntity publish = new CouponsPublishEntity();
		publish.setUserId(userId == null ? 0 :userId);
		publish.setCouponsType("share");
		publish.setCouponsSerial(serial);
		publish.setCreateTime(DateUtils.getSeconds());
		publish.setEndTime(DateUtils.getSeconds(org.apache.commons.lang3.time.DateUtils.addMonths(new Date(), 2)));
		publish.setCouponsNum(0);
		publish.setStatus("Y");
		
		couponsService.save(publish);
		return map;
	}
	
	/**
	 * 我的优惠券
	 * @return
	 */
	@RequestMapping(value="/mylist.do", method=RequestMethod.GET)
	private String myList(
			HttpServletRequest request, 
			@RequestParam(required=false, defaultValue="1") Integer pageIndex, 
			@RequestParam(required=false, defaultValue="10") Integer pageSize) {
		if(!isWeixin(request))
			return "main/warn";
		
		//step 1 获取当前用户id
		Integer userId = getUserId(request, null);
		if(null != userId) {
			//根据该用户绑定的手机号码查找有效的优惠券
			List<CouponsUserEntity> myCoupons = couponsService.queryMyCoupons(userId, pageIndex, pageSize);
			request.setAttribute("coupons", myCoupons);
			return "takeout/mineCoupon";
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.MYCOUPONS_URI);
		}
		
	}
	
	/**
	 * 跳转到领取优惠券页面
	 * @return
	 */
	@RequestMapping(value="/receive.do", method=RequestMethod.GET)
	private String toReceive(@RequestParam(required=false, defaultValue="") String serial, Model model) {
		//判断优惠券序列号是否合法
//		if(StringUtils.isEmpty(serial)) {
//			model.addAttribute("valid", false);
//		} else {
//			//判断该序列号的优惠券是否存在
//			boolean exist = couponsService.queryIsExistBySerial(serial);
//			model.addAttribute("valid", exist);
//		}
		model.addAttribute("serial", serial);
		
		return "takeout/receiveCoupon";
	}
	
	/**
	 * 领取优惠券
	 * @return
	 */
	@RequestMapping(value="/receive.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> receive(
			@RequestParam(required=false, defaultValue="") String serial,
			@RequestParam(required=false, defaultValue="") String mobile
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		//校验提交参数
		if(StringUtils.isEmpty(serial) || StringUtils.isEmpty(mobile)) {
			map.put("status", "fail");
			map.put("msg", "领取失败");
			return map;
		} 
		//step 1 查询该优惠券是否存在
		boolean exist = couponsService.queryIsExistBySerial(serial);
		if(exist) {
			//step 2 查询该手机号是否已经领取过该优惠券
			boolean receive = couponsService.queryIsReceive(serial, mobile);
			if(receive) {
				map.put("status", "fail");
				map.put("msg", "不能重复领取,您已经领取过该优惠券了");
			} else {
				//step 3 保存优惠券到该手机号下
				CouponsUserEntity c = couponsService.saveCoupons(serial, mobile);
				if(null != c) {
					map.put("status", "success");
					map.put("coupons", c);
				} else {
					map.put("status", "fail");
					map.put("msg", "您来迟一步，优惠券已经领完了");
				}
			}
		} else {
			map.put("status", "fail");
			map.put("msg", "您来迟一步，优惠券已经领完了");
		}
		
		return map;
	}
	
	/**
	 * 领取优惠券页面，查看领取人列表
	 * @param serial
	 * @return
	 */
	@RequestMapping(value="/getreceiver.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> getReceiver(
			@RequestParam(required=false, defaultValue="") String serial
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(serial)) {
			map.put("status", "fail");
		} else {
			//根据序列号查询领取该优惠券的前五个人
			List<ReceiverVo> vos = couponsService.queryReceiverBySerial(serial, 1, 5);
			if(vos.size() > 0) {
				//解决以前用户表绑定相同手机号问题，只是在页面显示时移除
				Iterator<ReceiverVo> it = vos.iterator();
				String mobile = "";
				while (it.hasNext()) {
					ReceiverVo vo = it.next();
					if(mobile.equals(vo.getMobile()))
						it.remove();
					else
						mobile = vo.getMobile();
				}
				map.put("status", "success");
				map.put("receiver", vos);
			} else {
				map.put("status", "fail");
			}
		}
		return map;
	}
	
	
}
