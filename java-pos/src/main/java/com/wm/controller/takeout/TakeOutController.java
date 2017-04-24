
package com.wm.controller.takeout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.common.UserInfo;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.VO.GoodsVO;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.beust.jcommander.internal.Lists;
import com.courier_mana.common.Constants;
import com.jpush.SoundFile;
import com.life.commons.CommonUtils;
import com.solr.service.PartnerSolrServiceI;
import com.team.wechat.util.JsapiTicket;
import com.wm.controller.open_api.OpenResult;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.controller.open_api.OpenUserInfo;
import com.wm.controller.open_api.retail.RetailPortCall;
import com.wm.controller.open_api.tswj.PortCall;
import com.wm.controller.open_api.tswj.PortConfig;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.dto.ShopcartDTO;
import com.wm.controller.takeout.dto.WXHomeDTO;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;
import com.wm.controller.takeout.vo.MenuVo;
import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.controller.takeout.vo.OrderMerchantVo;
import com.wm.controller.takeout.vo.OrderSimpleVo;
import com.wm.controller.takeout.vo.OrderVo;
import com.wm.dao.agent.AgentIncomeTimerDao;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.building.BuildingEntity;
import com.wm.entity.card.CardEntity;
import com.wm.entity.category.CategoryEntity;
import com.wm.entity.coupons.CouponsUserEntity;
import com.wm.entity.credit.CreditEntity;
import com.wm.entity.game.GameEntity;
import com.wm.entity.location.Location;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.address.AddressServiceI;
import com.wm.service.category.CategoryServiceI;
import com.wm.service.comment.CommentServiceI;
import com.wm.service.coupons.CouponsServiceI;
import com.wm.service.courier.TlmStatisticsServiceI;
import com.wm.service.credit.CreditServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.game.GameServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.TpmStatisticsRealtimeServiceI;
import com.wm.service.order.OrderCommentServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.OrdermenuServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.ruralbase.MealServiceI;
import com.wm.service.user.TumUserStatisticsServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;
import com.wm.util.IPUtil;
import com.wm.util.JSONUtil;
import com.wm.util.MapUtil;
import com.wm.util.StringUtil;
import com.wm.util.security.OpenSign;
import com.wp.AccessTokenContext;
import com.wp.AdvancedUtil;
import com.wp.CommonUtil;
import com.wp.ConfigUtil;
import com.wp.PayCommonUtil;
import com.wp.PayService;
import com.wp.WftAdvertisementUtils;
import com.wp.XMLUtil;
import com.wxpay.util.SDKRuntimeException;

import jeecg.system.service.SystemService;

/**
 * @Title: Controller
 * @Description: 微信外卖wap版
 * @author zhenjunzhuo
 * @date 2015-02-09 23:46:15
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/takeOutController")
public class TakeOutController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TakeOutController.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private PartnerSolrServiceI partnerSolrService;
	@Autowired
	private PayServiceI payService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private MenutypeServiceI menutypeService;
	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
	private GameServiceI gameService;
	@Autowired
	private CreditServiceI creditService;
	@Autowired
	private WUserServiceI wUserService;
	@Autowired
	private CategoryServiceI categoryService;
	@Autowired
	private CommentServiceI commentService;
	@Autowired
	private AddressServiceI addressService;
	@Autowired
	private OrdermenuServiceI ordermenuService;
	@Autowired
	private OrderCommentServiceI orderCommentService;
	@Autowired
    private JpushServiceI jpushService;
	@Autowired
	private CouponsServiceI couponsService;
	@Autowired
	private FlowServiceI flowService;
	@Autowired
	private MealServiceI mealService;
	@Autowired
	private TlmStatisticsServiceI tlmStatisticsService;
	@Autowired
	private TpmStatisticsRealtimeServiceI tpmStatisticsRealtimeService;
	@Autowired
	private TumUserStatisticsServiceI tumUserStatisticsService;
	@Autowired
	private PrintServiceI printService;
	
	@Value("${isMixVersion}")
	private String isMixVersion;
	
	@Value("${serviceScope}")
	private String serviceScope;
	
	private static final Map<String, Object> sortKey = new HashMap<>();
	
	static {
		sortKey.put("supplychain", CommonUtils.STATIC_FINAl);
	}
	
	
	// 外卖wap首页微信授权跳转
	@RequestMapping(params = "merchantList")
	public String merchantList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if(!isWeixin(request)){
			return "main/warn";
		}
		// 升级至1.8版本
		if("false".equals(isMixVersion)){
			return "redirect:" + ConfigUtil.WEIXIN_HOME;
		}

		UserInfo u = getUserInfo(request);
		if (u.getUserId() != null) {
			if(u.isRedirectUrl()) {
				return "redirect:" + ConfigUtil.INDEX_URI;
			}
			List<CategoryEntity> list = categoryService.findByZone("group");
			request.setAttribute("groups", list);
			
			//判断是否是新用户
			request.setAttribute("newUser", u.isNewUser());
			
			return "takeout/merchantlist";
			
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
		}
		
	}

	// 外卖wap首页
	@RequestMapping(params = "merchantListAfterAuth")
	public String merchantListAfterAuth(HttpServletRequest request) {
		
		List<CategoryEntity> list = categoryService.findByZone("group");
		request.setAttribute("groups", list);
		
		//微信jsapi
		JsapiTicket ticket = JsapiTicket.getJsapiTicket(AccessTokenContext.getJsapiTicket(), ConfigUtil.INDEX_URI);
		request.setAttribute("jsTicket", ticket);
		
		return "takeout/merchantlist";
	}
	
	@RequestMapping(value="/wxhome.do")
	@ResponseBody
	private Map<String, Object> wxHome(WXHomeDTO wx, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//step 1 根据经纬度找出附近快餐店铺
		List<MerchantSimpleVo> merchants = merchantService.findByLocation(wx);
		
		//step 2 把用户最新的经纬度保存在session
		setLocation(request, wx.getLng(), wx.getLat(), wx.getAddress(), wx.getCity());
		
		map.put("state", "success");
		map.put("obj", merchants);
		return map;
	}
	
	@RequestMapping(value="/setlocation.do", method=RequestMethod.POST)
	@ResponseBody
	private void setLocation(HttpServletRequest request,  WXHomeDTO wx) {
		setLocation(request, wx.getLng(), wx.getLat(), wx.getAddress(), wx.getCity());
	}
	
	@RequestMapping(value="/merchant/lastedorder.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> lastedOrder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer userId = getUserId(request, null);
		if(null != userId) {
			OrderSimpleVo vo = merchantService.lastedOrder(userId);
			if(null != vo) {
				map.put("obj", vo);
				map.put("state", "success");
				return map;
			}
		}
		
		map.put("state", "fail");
		
		return map;
	}
	
	@RequestMapping(value="/menu.do", method=RequestMethod.GET)
	private String toMenuAfterAuth(@RequestParam Integer merchantId, Integer menuTypeId, Model model, HttpServletRequest request) {
		if("false".equals(isMixVersion)){
			return "redirect:" + ConfigUtil.MERCHANT_HOME.replace("MERCHANT_ID", merchantId + "");
		}
		long start = System.currentTimeMillis();
		Integer userId = getUserId(request, null);
		logger.info("进入店铺页面--------merchantId："+merchantId+"-----------userId："+userId + "------------------request session id" + request.getSession().getId());
		if(null != userId) {
			//step 1 查找店铺信息
			MerchantEntity mer = merchantService.findUniqueByProperty(MerchantEntity.class, "id", merchantId);
			model.addAttribute("mer", mer);
			
			//购买次数
			int buyCount = menuService.findBuyCount(merchantId);
			model.addAttribute("buycount", buyCount);
			
			//评论
			CommentScoreVo scoreVo = commentService.queryCommentScore(1, merchantId);
			model.addAttribute("score", scoreVo);
			//其他信息
			model.addAttribute("merchantId", merchantId);
			model.addAttribute("location", getLocation(request));
			model.addAttribute("userId", userId);
			//step 2 查找店铺菜品分类
			List<MenutypeEntity> types = menutypeService.queryByMerchantId(merchantId);
			
			model.addAttribute("types", types);
			//查找店铺的菜单总条数
			String sql = "select ifnull(count(0), 0) count from menu where merchant_id=?";
			Long menuCount = menuService.getCountForJdbcParam(sql, new Object[]{merchantId});
			//对于店铺的菜单数量超过300条进行特殊处理
			if(null != menuCount && menuCount > 300){
				doLoadLargeMerchantMenu(request, merchantId, menuTypeId, userId, model, types);
				model.addAttribute("typeid", menuTypeId);
				logger.info(String.format("\n ----- 【menu list】times:%s", System.currentTimeMillis() - start));
				return "takeout/menu_large";
			}
			
			doLoadLessMerchantMenu(request, merchantId, userId, model, mer, types);
			logger.info(String.format("\n ----- 【menu list】times:%s", System.currentTimeMillis() - start));
			return "takeout/menu";
		}else if (isWeixin(request)) {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(String.format(ConfigUtil.MENULIST_URI_AUTH, merchantId));
		} else {
			return "main/warn";
		}
		
	}
	
	@RequestMapping(value="/menuByPage.do", method=RequestMethod.GET)
	@ResponseBody
	private Object menuByPage(HttpServletRequest request, Integer merchantId, Integer menuTypeId, Model model, Integer page, Integer pageSize){
		AjaxJson json = new AjaxJson();
		json.setState("faild");
		json.setStateCode("01");
		try {
			page = null == page ? 1 : page;
			pageSize = null == pageSize ? 20 : pageSize;
			Integer userId = getUserId(request, null);
			List<MenuVo> menus = menuService.findFirstPageMenuByMerchantId(merchantId, menuTypeId, userId, page, pageSize);
			if(null == menus || menus.isEmpty()){
				json.setStateCode("02");
			}else{
				json.setState("success");
				json.setObj(menus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	private void doLoadLargeMerchantMenu(HttpServletRequest request, Integer merchantId, Integer menuTypeId, Integer userId, Model model, List<MenutypeEntity> types){
		Integer page = Integer.getInteger(request.getParameter("page"), 1);
		model.addAttribute("page", page);
		Integer pageSize = Integer.getInteger(request.getParameter("pageSize"), 20);
		if(null == menuTypeId && null != types && !types.isEmpty()) menuTypeId = types.get(0).getId();
		List<MenuVo> menus = menuService.findFirstPageMenuByMerchantId(merchantId, menuTypeId, userId, page, pageSize);
		//step 4 分拣菜单所属分类
		List<List<MenuVo>> list = new ArrayList<List<MenuVo>>();
		
		Iterator<MenutypeEntity> it = types.iterator();
		while (it.hasNext()) {
			MenutypeEntity type = it.next();
			int typeId = type.getId();
			List<MenuVo> vos = new ArrayList<MenuVo>();
			for (MenuVo vo : menus) {
				if(typeId == vo.getTypeId())
					vos.add(vo);
			}
			//判断该分类是否有菜品
			if(vos.size() > 0) 
				list.add(vos);
			else
				list.add(new ArrayList<MenuVo>());
				//it.remove();
		}
		model.addAttribute("menus", list);
		model.addAttribute("typeid", menuTypeId);
	}
	
	/**
	 * 
	 * @param request
	 * @param merchantId
	 * @param userId
	 * @param model
	 * @param mer
	 * @param buyCount
	 */
	private void doLoadLessMerchantMenu(HttpServletRequest request, Integer merchantId, Integer userId, Model model, MerchantEntity mer, List<MenutypeEntity> types){
		//step 3 查找店铺所有菜单
		List<MenuVo> menus = menuService.findByMerchantId(merchantId, userId);
		
		//step 4 分拣菜单所属分类
		List<List<MenuVo>> list = new ArrayList<List<MenuVo>>();
		
		Iterator<MenutypeEntity> it = types.iterator();
		while (it.hasNext()) {
			MenutypeEntity type = it.next();
			int typeId = type.getId();
			List<MenuVo> vos = new ArrayList<MenuVo>();
			for (MenuVo vo : menus) {
				if(typeId == vo.getTypeId())
					vos.add(vo);
			}
			//判断该分类是否有菜品
			if(vos.size() > 0) 
				list.add(vos);
			else
				it.remove();
		}
		model.addAttribute("menus", list);
	}
	
	/**
	 * 店铺菜单
	 * @param merchantId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menu/{merchantId}.do", method=RequestMethod.GET)
	private String toMenu(@PathVariable Integer merchantId, Model model, HttpServletRequest request) {
		
		if("false".equals(isMixVersion)){
			return "redirect:" + ConfigUtil.MERCHANT_HOME.replace("MERCHANT_ID", merchantId + "");
		}
		
		Integer userId = getUserId(request, null);
		logger.info("进入店铺页面--------merchantId："+merchantId+"-----------userId："+userId+"------------------request session id" + request.getSession().getId());
		if(null != userId) {
			return toMenuAfterAuth(merchantId, null, model, request);
		}else if (isWeixin(request)) {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(String.format(ConfigUtil.MENULIST_URI_AUTH, merchantId));
		} else {
			return "main/warn";
		}
		
	}

	/**
	 * 店铺评论
	 * @param merchantId
	 * @param start
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/comments/{merchantId}.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> commentList(@PathVariable Integer merchantId, int start, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<CommentVo> comments = commentService.queryByMerChantId(1, merchantId, start, rows);
		
		map.put("state", "success");
		map.put("comments", comments);
		
		return map;
	}
	
	@RequestMapping(value="/checkShopcart.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> checkShopcart(@Valid ShopcartDTO shopcart, BindingResult result) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!result.hasErrors()) {
			List<Shopcart> carts = shopcart.getShopcarts();
			//step 1 判断加入购物车的数量和价格是否符合当前数据库
			if(menuService.judgePriceAndCount(carts)) {
				map.put("state", "success");
			} else {
				map.put("reason", "价格发生变动或库存不足，请重新刷新");
				map.put("state", "fail");
			}
		} else {
			map.put("reason", "购物车不能为空");
			map.put("state", "fail");
		}
		
		return map;
	}
	
	
	@RequestMapping(value="/addShopcart.do", method=RequestMethod.POST)
	private String addShopcart(
			@Valid ShopcartDTO shopcart, BindingResult result, 
			Model model, HttpServletRequest request) {
		if(!result.hasErrors()) {
			Integer userId = getUserId(request, null);
			if(null == userId) {
				logger.info("when session userid is null-----addshopcart----userid:"+shopcart.getUserId());
				setUserId(request, shopcart.getUserId());
			}
			model.addAttribute("shopcart", shopcart);
			return "takeout/selectType";
			
		} 
		
		return "";
	}
	
	@RequestMapping(value="/writeOrder.do", method=RequestMethod.POST)
	private String writeOrder(HttpServletRequest request, @Valid ShopcartDTO shopcart, BindingResult result, Model model) {
		if(!result.hasErrors()) {
			Integer userId = getUserId(request, null);
			logger.info("进入写订单页面-------------------userId："+userId+"------------------request session id" + request.getSession().getId());
			if (userId == null) {
				logger.info("when session userid is null-----writeOrder----userid:"+shopcart.getUserId());
				userId = shopcart.getUserId();
				setUserId(request, userId);
			}
			
			//step 1 判断加入购物车的数量和价格是否符合当	前数据库
			List<Shopcart> carts = shopcart.getShopcarts();
			if(!menuService.judgePriceAndCount(carts)) {
				//重定向错误页面
				return "redirect:/takeOutController/errorOrder/"+shopcart.getMerchantId()+".do";
			}
			//统计订单金额、数量
			int count = 0;
			//单位为分
			double totalPrice = 0.0;
			for (Shopcart c : carts) {
				count += c.getCount();
				totalPrice += Math.rint(c.getPrice()*100)*c.getCount();
			}
			
			//查询该订单是否需要配送费，单位为分
			double deliveryFee = 0.0;
			Map<String, Object> fee = merchantService.findOneForJdbc("SELECT cost_delivery FROM merchant WHERE id = ?", shopcart.getMerchantId());
			//外卖订单配送费
			if(null != fee && shopcart.getSaleType() == 1) {
				deliveryFee = Math.rint(Double.valueOf(fee.get("cost_delivery").toString())*100);
				if(deliveryFee > 0) {
					model.addAttribute("isDelevery", true);
					model.addAttribute("deliveryFee", deliveryFee/100); 
				} else {
					model.addAttribute("isDelevery", false);
				}
					
			} else {
				model.addAttribute("isDelevery", false);
			}
			//应付金额，单位为分
			double oughtPay = totalPrice + deliveryFee;
			
			//获取用户的积分
			WUserEntity user = wUserService.get(WUserEntity.class, userId);
			if(user != null){
				int credit = user.getScore();
				//判断当前订单金额和积分是否符合使用规则（订单金额大于等于10元，积分大于等于100）
				if(oughtPay >= 10*100 && credit >= 100) {
					model.addAttribute("isCredit", true);
					model.addAttribute("credit", credit);
					model.addAttribute("creditMoney", 5);
				} else {
					model.addAttribute("isCredit", false);
				}
				
				//判断用户优惠券,获取最大可优惠金额
				CouponsUserEntity coupons = couponsService.queryAvailableForOrder(userId, (int)oughtPay);
				if(null != coupons) {
					model.addAttribute("isCoupons", true);
					model.addAttribute("coupons", coupons);
				} else {
					model.addAttribute("isCoupons", false);
				}
				
				//获取用户余额
				double myMoney = user.getMoney();
				//判断当前用户余额是否满足订单金额
				if(myMoney*100 >= oughtPay ) {
					model.addAttribute("myMoney", myMoney);
					model.addAttribute("isMoney", true);
				} else {
					model.addAttribute("isMoney", false);
				}
					
				
				model.addAttribute("count", count);
				model.addAttribute("totalPrice", totalPrice/100);
				model.addAttribute("oughtPay", oughtPay/100);
				model.addAttribute("carts", carts);
				
				
				//作为数据传递给下一步，生成订单数据
				model.addAttribute("shopcart", shopcart);
				//判断订单类型（外卖、堂食）
				//step 2 是否定位地址
				model.addAttribute("location", getLocation(request));
				if(shopcart.getSaleType() == 1) {
					//外卖
					//step 1 查询当前用户默认使用的地址
					AddressEntity addr = addressService.queryLasted(userId);
					
					model.addAttribute("addr", addr);
					
					return "takeout/takeout";
					
				} else if (shopcart.getSaleType() == 2) {
					//堂食
					//step 1 查询上一次堂食订单填写的联系人信息
					AddressDetailVo vo = orderService.queryLastedRestaurantUserInfo(userId);
					model.addAttribute("addr", vo);
					
					return "takeout/restaurant";
				}
			} else {
				return "";
			}
		}
		
		return "";
	}
	
	@RequestMapping(value="/createOrder.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, String> createOrder(@Valid ShopcartDTO shopcart, BindingResult result,  HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(!result.hasErrors()) {
			Integer userId = getUserId(request, null);
			if (userId == null) {
				map.put("reason", "您的验证已过期,请重新进入首页");
				map.put("state", "fail");
				return map;
			}
			//step 1 判断订单的数量和价格是否符合当前数据库
			List<Shopcart> carts = shopcart.getShopcarts();
			if(!menuService.judgePriceAndCount(carts)) {
				map.put("reason", "选购菜品有误,请重新选购");
				map.put("state", "fail");
				return map;
			}
			
			//step 2 生成订单
			if(carts.size() > 0) {
				Integer orderId = orderService.createOrderFromWX(userId, shopcart, carts);
				
				if(null != orderId) {
					//生成支付订单,根据不同的支付方式调到不同的pay
					if(shopcart.getPayType() == 1) 
						map = orderWXPay(orderId, true, request);
					else if(shopcart.getPayType() == 2)
						map = orderBanlancePay(orderId, request);
				} else {
					map.put("reason", "生成订单失败");
					map.put("state", "fail");
				}
			}
			
			
		}
		
		return map;
	}
	
	@RequestMapping(value="/wxpay/{orderId}.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, String> orderWXPay(
			@PathVariable Integer orderId,
			@RequestParam(required=false, defaultValue="false")Boolean valid,
			HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		Integer userId = getUserId(request, null);
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		
		if(!valid) {
			boolean flag = orderService.validOrderByOrderId(orderId);
			if(!flag) {
				map.put("state", "fail");
				map.put("reason", "订单已无效，必须在20分钟内完成支付");
				orderService.returnCreditAndScore(orderId);
				return map;
			}
			//判断订单是否使用积分
			if (order.getScore() == 100 && order.getScoreMoney() == 5.0) {
				if(order.getOrigin() < 10) {
					order.setScore(0);
					order.setScoreMoney(0.0);
					orderService.updateEntitie(order);
				}
			}
		}
		
		//查询订单
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		MerchantEntity mer = merchantService.get(MerchantEntity.class, order.getMerchant().getId());
		
		//生成微信预付订单
		int money = ((int)Math.rint(order.getOrigin()*100) + (int)Math.rint(order.getDeliveryFee()*100)
					 - (int)Math.rint(order.getScoreMoney()*100) - (int)Math.rint(order.getCard()*100));
		String xml = PayService.createJSPackage(mer.getTitle(), money+"", order.getPayId(), user.getOpenId(), IPUtil.getRemoteIp(request));
		logger.info("预付订单提交数据"+xml);
		String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", xml);
		logger.info("请求预付订单返回数据"+result);
		try {
			Map<String, String> resultMap = XMLUtil.doXMLParse(result);
			
			//判断prepay_id是否生成
			if(resultMap.get("return_code").equals("FAIL")) {
				map.put("state", "fail");
				map.put("reason", resultMap.get("return_msg"));
				return map;
			} else {
				if(resultMap.get("result_code").equals("FAIL")) {
					String errCode = resultMap.get("err_code");
					String errMsg = resultMap.get("err_code_des");
					logger.info("微信支付失败，错误码："+errCode+"------错误信息："+errMsg);
					map.put("state", "fail");
					map.put("reason", errMsg);
					return map;
				}
			}
			
			logger.info("微信预付订单prepay_id"+resultMap.get("prepay_id"));
			//封装用户支付订单
			SortedMap<String, String> params = new TreeMap<String, String>();
			params.put("appId", ConfigUtil.APPID_KFZ);
			params.put("timeStamp", System.currentTimeMillis()+"");
			params.put("nonceStr", PayCommonUtil.CreateNoncestr());
			params.put("package", "prepay_id=" + resultMap.get("prepay_id"));
			params.put("signType", ConfigUtil.SIGN_TYPE);
			
			// paySign的生成规则和Sign的生成规则一致
			String paySign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.API_KEY);
			params.put("paySign", paySign); 
			// 这里用packageValue是预防package是关键字在js获取值出错
			params.put("packageValue", "prepay_id=" + resultMap.get("prepay_id")); 
			
			logger.info("生成微信预付订单" + order.getPayId() + "|" + user.getNickname());
			params.put("orderId", orderId.toString());
			params.put("state", "success");
			params.put("payType", "wx");
			params.put("saleType", order.getSaleType().toString());
			params.put("preScore", String.valueOf(order.getOrigin().intValue()));
			return params;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("state", "fail");
			map.put("reason", "订单支付失败");
		}
		
		return map;
	}
	
	// 获取支付信息
		@RequestMapping(params = "getThirdPayInfo")
		@ResponseBody
		public AjaxJson getThirdPayInfo(HttpServletRequest request,
				HttpServletResponse response, Integer orderid, String userid) {
			AjaxJson j = new AjaxJson();
			j.setStateCode("01");
			j.setSuccess(false);
			
			if (orderid == null || orderid < 1) return j.msg("订单数据有误");

			try {
				// 订单
				String sql = "select id,title,pay_id payId,origin,origin from `order` where id=?";
				Map<String, Object> orderMap = orderService.findOneForJdbc(sql, orderid);
				OrderEntity order = MapUtil.convertMapToBean(orderMap, OrderEntity.class);
				System.out.println("获取支付信息 orderid" + orderid);
				String[] payIdArray = order.getPayId().split("_");
				if(StringUtil.isEmpty(order.getPayId()) || order.getPayId().split("_").length != 2) return j.msg("订单数据有误");
				
				//需要去天生外卖获取订单状态 然后发起支付
				JSONObject orderInfo = PortCall.detail(payIdArray[0]);
				if(null == orderInfo) return j;
				Integer msgCode = orderInfo.getInteger("msgCode");
				JSONObject resultJson = orderInfo.getJSONObject("result");
				
				if(State.Success.equal(msgCode)){//获取订单信息成功
					Integer payStatus = resultJson.getInteger("payStatus");
					Integer status = resultJson.getInteger("status");
					Double price = resultJson.getDouble("price");

					if(status != 1 || payStatus != 0){//检查订单状态
						if(status == 3){//已取消的订单 需要更新订单状态
							sql = "insert into order_state(order_id,deal_time,state,detail) values(?,?,?,?)";
							orderStateService.executeSql(sql, order.getId(), (int)System.currentTimeMillis()/100, "已取消", "第三方订单取消");
							
							//变更订单状态
							sql = "update `order` set state='cancel' where id=?";
							orderStateService.executeSql(sql, order.getId());
						}
						return j.msg("只能支付未支付的订单");
					}
					
					if(order.getOrigin().doubleValue() != price.doubleValue()){
						return j.msg("订单金额不一致");
					}
					
				} else{//获取订单信息失败 
					return j.msg("获取订单信息失败");
				}

				// 实际支付金额
				sql = "update `order` set online_money=?,pay_type=? where id=?";
				orderService.executeSql(sql, order.getOrigin(), "weixinpay", orderid);
				
				sql = "select openid from user where id=?";
				Map<String, Object> userMap = wUserService.findOneForJdbc(sql, userid);

				// 金额
				int money = (int) (Math.rint(order.getOrigin() * 100));
				String requestXML = PayService.createJSPackage(order.getTitle() + "-微信公众号支付", 
						money + "", 
						order.getPayId(),
						String.valueOf(userMap.get("openid")), request);
				System.out.println("生成的支付信息结果为：" + requestXML);

				String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
				if(logger.isInfoEnabled()) logger.info(String.format("----【getThirdPayInfo】 result:%s", result));
				Map<String, String> resultMap = XMLUtil.doXMLParse(result);
					//判断prepay_id是否生成
				if(resultMap.get("return_code").equals("FAIL")) {
					return j.state("fail").msg(resultMap.get("return_msg"));
				} else {
					if(resultMap.get("result_code").equals("FAIL")) {
						String errCode = resultMap.get("err_code");
						String errMsg = resultMap.get("err_code_des");
						logger.info("微信支付失败，错误码："+errCode+"------错误信息："+errMsg);
						return j.state("fail").msg(errMsg);
					}
				}
					
				logger.info("微信预付订单prepay_id"+resultMap.get("prepay_id"));
				//封装用户支付订单
				SortedMap<String, String> params = new TreeMap<String, String>();
				params.put("appId", ConfigUtil.APPID_KFZ);
				params.put("timeStamp", System.currentTimeMillis()/100+"");//微信参数要求秒
				params.put("nonceStr", PayCommonUtil.CreateNoncestr());
				params.put("package", "prepay_id=" + resultMap.get("prepay_id"));
				params.put("signType", ConfigUtil.SIGN_TYPE);
				
				// paySign的生成规则和Sign的生成规则一致
				String paySign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.API_KEY);
				params.put("paySign", paySign); 
				// 这里用packageValue是预防package是关键字在js获取值出错
				params.put("packageValue", "prepay_id=" + resultMap.get("prepay_id")); 
				
				logger.info("生成微信预付订单" + order.getPayId() + "|" + userid);
				params.put("orderId", order.getId() + "");
				params.put("state", "success");
				params.put("payType", "wx");
				params.put("saleType", order.getSaleType().toString());
				params.put("preScore", String.valueOf(order.getOrigin().intValue()));
				
				j.setObj(params);
				j.setStateCode("00");
				j.setState("successs");
				return j;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e.fillInStackTrace());
			}
			return j;
		}
		
	@RequestMapping(value="/myPay")
	private String toMyPay(HttpServletRequest request, String orderid, Model model){
		if(StringUtil.isEmpty(orderid)) return "takeout/error";
		UserInfo user = getUserInfo(request);
		if(null == user) return "takeout/error";
		String sql = "select pay_id from `order` where id=?";
		Map<String, Object> orderMap = orderService.findOneForJdbc(sql, orderid);
		if(null == orderMap || null == orderMap.get("pay_id")) return "takeout/error";
		
		String payId = String.valueOf(orderMap.get("pay_id"));
		if(payId.split("_").length !=2) return "takeout/error";
		
		JSONObject params = new JSONObject();
		params.put("out_order_id", payId.split("_")[0]);
		params = OpenSign.getSignJson(params);
		model.addAllAttributes(params);
		return "redirect:/takeOutController/pay.do";
	}
	
	

	/**
	 * <h1>发起支付请求,转到自己的支付界面</h1>
	 */
	@RequestMapping(value="/pay")
	private String pay(Long timestamp, String sign,
			String out_order_id, HttpServletRequest request, Boolean isDebug){
		OpenResult ret = doPay(timestamp, sign, out_order_id, request, isDebug);
		if(logger.isInfoEnabled())
			logger.info(String.format("--------【order pay】params:%s  \n\r return:%s",
					JSON.toJSONString(request.getParameterMap()), ret.toString()));
		request.setAttribute("OpenResult", ret.toString());
		
		//校验订单是否正确，并返回订单信息
		Object orderCheck = checkAndGetOrderId(null, out_order_id);
		OrderEntity order = new OrderEntity();
		order.setId(-1);
		if(orderCheck instanceof OrderEntity){
			order = (OrderEntity) orderCheck;
		}
		return "redirect:" + ConfigUtil.THIRD_TSWJ + order.getId();
	}
	
	private OpenResult doPay(Long timestamp, String sign,
			String out_order_id, HttpServletRequest request, Boolean isDebug){
		//校验订单是否正确，并返回订单信息
		Object orderCheck = checkAndGetOrderId(null, out_order_id);
		if(orderCheck instanceof OpenResult) return (OpenResult) orderCheck;
		OrderEntity order = (OrderEntity) orderCheck;
		
		if(!OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getState())){
			return State.Error.ret().msg("支付失败，只能支付未付款的订单");
		}
		return State.Success.ret()
				.put("out_order_title", order.getTitle())
				.put("price", order.getOrigin())
				.put("orderid", order.getId())
				.put("userid", order.getWuser().getId());
	}
	
	/**
	 * 校验订单是否正确，并返回订单信息（包含：wuser.userId,id,title,state,rstate,origin）
	 * @param wuser 为空则校验订单与请求用户是否一致
	 * @param out_order_id 第三方商家订单号 放在本系统pay_id字段格式：(out_order_id + "_" + TSWJ_PARTNERID)
	 * @return
	 */
	private Object checkAndGetOrderId(WUserEntity wuser, String out_order_id){
		//校验订单是否类型正确、是否已支付
		String sql = "select id,pay_id payId,title,state,rstate,origin,user_id from `order` where pay_id=? and from_type=? and order_type=?";
		Map<String, Object> orderMap = orderService.findOneForJdbc(sql, out_order_id + "_" + PortConfig.TSWJ_APPID, "tswj", "third_part");
		Integer orderid = null;
		if(null != orderMap) orderid = Integer.valueOf(String.valueOf(orderMap.get("id")));
		if(StringUtil.isEmpty(orderid)) return State.NotFound.ret().msg("订单不存在");
		int req_user_id = Integer.valueOf(String.valueOf(orderMap.get("user_id"))).intValue();
		if(null != wuser && wuser.getId().intValue() != req_user_id)// 
			return State.Error.ret().msg("订单与用户不匹配");
		
		//组装order
		OrderEntity order = new OrderEntity();
		order.setId(orderid);
		if(null == wuser){
			wuser = new WUserEntity();
			wuser.setId(req_user_id);
		}
		order.setWuser(wuser);
		order.setTitle(String.valueOf(orderMap.get("title")));
		order.setOrigin((Double) orderMap.get("origin"));
		return order;
	}
	
	private Map<String, String> orderBanlancePay(Integer orderId, HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Integer userId = getUserId(request, null);
		logger.info("-----微信端进入余额，用户id--" + userId);
		// 获取用户余额
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (null == user) {
			map.put("state", "fail");
			map.put("reason", "操作无效");
			logger.info("-----微信端进入余额，用户不存在，id--" + userId);
			return map;
		}
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		if (null == order) {
			map.put("state", "fail");
			map.put("reason", "订单无效");
			logger.info("-----微信端进入余额，订单不存在，id--" + userId);
			return map;
		}

		// 判断订单是否使用积分
		if (order.getScore() == 100 && order.getScoreMoney() == 5.0) {
			if (order.getOrigin() < 10) {
				order.setScore(0);
				order.setScoreMoney(0.0);
				orderService.updateEntitie(order);
			}
		}

		// 判断用户余额是否满足支付订单金额(单位为分)
		double oughtPay = Math.rint(order.getOrigin() * 100)
				+ Math.rint(order.getDeliveryFee() * 100)
				- Math.rint(order.getScoreMoney() * 100)
				- Math.rint(order.getCard() * 100);
		if (Math.rint(user.getMoney() * 100) >= oughtPay) {
			// 排号
			String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
			String num = orderNum;
			if(!StringUtils.isEmpty(orderNum)){
				if(orderNum.length() > 8){
					num = orderNum.substring(8);
				}
			}
			/**
			 * 乡村基业务
			 */
			// ************start***************
			String sql = "select a.id,a.merchant_id,a.remark,b.merchant_source from `order` a"
					+ " LEFT JOIN 0085_merchant_info b on a.merchant_id=b.merchant_id where a.id=?";
			Map<String, Object> orderMap = orderService.findOneForJdbc(sql, orderId);
			// 订单存在 并且是未支付状态 修改订单状态
					
			if (null != orderMap && "2".equals(order.getSaleType()) && null != orderMap.get("merchant_source")
					&& orderMap.get("merchant_source").equals(1)) {
				// 获得商家排号
				
				String menuSql = "SELECT m.print_type,o_m.quantity,o_m.total_price,m.name"
						+ " FROM order_menu o_m,menu m"
						+ " WHERE o_m.menu_id=m.id" + " AND o_m.order_id=?";
				List<Map<String, Object>> menu = orderService.findForJdbc(
						menuSql, orderId);
				if (null != menu && menu.size() > 0) {
					for (int i = 0; i < menu.size(); i++) {
						Map<String, Object> menuMap = menu.get(i);
						String type = "A";
						// 生成堂食订单购物车每个商品插入0085_dinein_order
						if (null != menuMap.get("print_type")) {
							// 乡村基操作
							switch (menuMap.get("print_type").toString()) {
							case "1":
								type = "A";
								break;
							case "2":
								type = "B";
								break;
							case "3":
								type = "C";
								break;
							default:
								type = "A";
								break;
							}

							// 插入0085_dinein_order
							String fullName = type + "" + num;
							String ordersql = "INSERT INTO 0085_dinein_order (merchant_id,order_id,order_type,order_num,full_num,sort_order,"
									+ " create_time,status,update_time,remark,order_time,menu_name,quantity)"
									+ " VALUES(?,?,?,?,?,0,now(),1,now(),?,now(),?,?)";
							
							orderService.executeSql(ordersql, orderMap
									.get("merchant_id"), orderId, type,
									num, fullName, orderMap.get("remark"),
									menuMap.get("name").toString(), menuMap
											.get("quantity").toString());
							
							//0085_dinein_order表变动清缓存
							AliOcs.syncRemove(CacheKeyUtil.getMealPreList(order.getMerchant().getId())); //清除备餐列表缓存
							AliOcs.syncRemove(CacheKeyUtil.getMealList(order.getMerchant().getId())); //清除出餐列表缓存
						}
					}
				}
			}
			// ************end***************
			logger.info("-----微信端进入余额，用户余额--" + user.getMoney() + ",----订单金额--"
					+ order.getOrigin() + "----积分金额--" + order.getScoreMoney());
			// 修改订单和用户信息
			order.setPayType(PayEnum.balance.getEn());
			order.setCredit(oughtPay / 100);
			order.setPayTime(DateUtils.getSeconds());
			orderService.updateEntitie(order);
			payService.orderPayCallback(order);
			map.put("state", "success");
			map.put("payType", "balance");
			map.put("saleType", order.getSaleType().toString());
			map.put("orderId", orderId.toString());
			map.put("preScore", String.valueOf(order.getOrigin().intValue()));
		} else {

			map.put("state", "fail");
			map.put("reason", "余额不足");
		}

		return map;
	}
	
	@RequestMapping(value="/errorOrder/{merchantId}.do", method=RequestMethod.GET)
	private String errorOrder(@PathVariable Integer merchantId, Model model) {
		model.addAttribute("merchantId", merchantId);
		return "takeout/errorOrder";
	}
	
	@RequestMapping(value="/orderlist.do", method=RequestMethod.GET)
	private String myOrderList(HttpServletRequest request,
			@RequestParam(required=false, defaultValue="1") int start, 
			@RequestParam(required=false, defaultValue="20")int rows, 
			Model model) {
		Integer userId = getUserId(request, null);
		if(null != userId) {
			List<OrderMerchantVo> vos = orderService.queryMineOrderByState(null, userId, start, rows);
			model.addAttribute("orders", vos);
			return "takeout/orderlist";
		}else if (isWeixin(request)) {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.ORDER_LIST_URL);
		} else {
			return "main/warn";
		}
		
	}
	
	@RequestMapping(value="/{orderId}orderdetail.do", method=RequestMethod.GET)
	private String myOrderDetail(HttpServletRequest request, @PathVariable Integer orderId, String userid, String token, Boolean isThirdOrder, Model model) {
		if(null != isThirdOrder && isThirdOrder){//查看第三方订单详细
			return toThirdOrderDetail(orderId, userid, token, request);
		}
		if("false".equals(isMixVersion)){
			return "redirect:" + ConfigUtil.ORDER_DETAIL.replace("ORDER_ID", orderId + "");
		}
		Integer userId = getUserId(request, null);
		if(null != userId) {
			OrderVo vo = orderService.queryOrderById(orderId, userId);
			if(vo != null){
				String orderNum = vo.getOrderNum();
				if(!StringUtils.isEmpty(orderNum)){
					if(orderNum.length() > 8){
						orderNum = orderNum.substring(8);
					}
				}
				vo.setOrderNum(orderNum);
			}
			model.addAttribute("order", vo);
			return "takeout/orderDetail";
		} else if (isWeixin(request)) {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(String.format(ConfigUtil.ORDER_DETAIL_URL, orderId));
		} else {
			return "main/warn";
		}
	}
	
	private String toThirdOrderDetail(Integer orderId, String userid, String token, HttpServletRequest request){
		logger.info(String.format("\n -----【toThirdOrderDetail】 params: orderId=%s userid=%s token=%s", orderId, userid, token));
		if(StringUtil.isEmpty(token)){
			//获取openid
			OpenUserInfo info = getWeixinUserInfo(request);
			String openid = info.getOpenid();
			if(StringUtil.isEmpty(openid)){
				if(!StringUtils.isEmpty(userid)){
					String sql = "select openid from `user` where id=?";
					Map<String, Object> orderMap = orderService.findOneForJdbc(sql, userid);
					if(null == orderMap || StringUtil.isEmpty(orderMap.get("openid"))) return "takeout/error";
					openid = (String) orderMap.get("openid");
				}
				else return "takeout/error";//微信授权失败
			}
			//加密生成token
			token = OpenSign.EncToken(openid, PortConfig.TSWJ_APPID, PortConfig.TOKEN_KEY);
		}
		Long timestamp = System.currentTimeMillis();
		String sql = "select pay_id from `order` where id=?";
		Map<String, Object> orderMap = orderService.findOneForJdbc(sql, orderId);
		if(null == orderMap || StringUtil.isEmpty(orderMap.get("pay_id"))) return "takeout/error";
		
		String[] pay_id = String.valueOf(orderMap.get("pay_id")).split("_");
		if(pay_id.length != 2) return "takeout/error";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("timestamp", timestamp);
		params.put("tno", pay_id[0]);
		params.put("appId", PortConfig.TSWJ_APPID);
		String sign = OpenSign.getSign(params);
		
		StringBuilder sb = new StringBuilder();
		sb.append("redirect:").append(PortConfig.TSWJ_ORDER_DETAIL_URL)
		.append("?token=").append(token)
		.append("&sign=").append(sign)
		.append("&tno=").append(pay_id[0])
		.append("&timestamp=").append(timestamp);
		if(logger.isInfoEnabled())
			logger.info("----- 【toThirdOrderDetail】 url:" + sb.toString());
        return sb.toString();
	}
	
	@RequestMapping(value="/{orderId}/confirmorder.do", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> confirmOrder(HttpServletRequest request,@PathVariable Integer orderId, Integer courierId, Integer merchantId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int userId = this.getUserId(request, null);
		
		boolean flag = orderService.confirmOrder(orderId, userId);
		if (flag) {
			systemService.addLog("确认收货成功", Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			//添加系统评价为此订单
			orderCommentService.createSystemOrderComment(orderId, userId, 0, courierId);
			orderCommentService.createSystemOrderComment(orderId, userId, 1, merchantId);
			
            // ===============================第三方Retail超市需求=======================================//
            String source = merchantService.getMerchantSource(merchantId);
            if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
                logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行确认完成订单操作【用户点确认:confirmOrder()】,orderId="+orderId);
                RetailPortCall.updateOrder(orderId);
            }
            // ===============================第三方Retail超市需求=======================================//
			map.put("state", "success");
		} else {
			map.put("state", "fail");
		}
		
		return map;
	}
	
	/**
	 * 店铺首页微信授权跳转
	 * @author lfq
	 * @param merchantid 店铺id
	 */
	@RequestMapping(params = "waimai")
	public String waimai(HttpServletRequest request,
			HttpServletResponse response, Integer merchantid)
			throws IOException {
		if (merchantid == null || merchantid < 1) {// 商家id不传时跳转到外卖wap首页
			if (ConfigUtil.isTest || !this.isWeixin(request)
					|| this.getUserId(request, null) != null) {
				return this.merchantListAfterAuth(request);
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
			}
		}

		if (ConfigUtil.isTest || !this.isWeixin(request) || this.getUserId(request, null) != null) {
			return this.waimaiAfterAuth(request, merchantid);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(String.format(ConfigUtil.MENULIST_URI, merchantid));
		}
	}

	/**
	 * 进入店铺首页
	 * 
	 * @param merchantid
	 *            店铺id
	 * @return
	 */
	@RequestMapping(params = "waimaiAfterAuth")
	public String waimaiAfterAuth(HttpServletRequest request, Integer merchantid) {
		Integer userId = this.getUserId(request, null);// 微信授权后自动注册登录

		MerchantEntity merchantEntity = null;
		if (merchantid != null && merchantid > 0) {
			merchantEntity = this.systemService.get(MerchantEntity.class,
					merchantid);
		}
		if (merchantEntity == null) {// 店铺不存在时跳回wap首页
			if (ConfigUtil.isTest || !this.isWeixin(request) || userId != null) {
				return this.merchantListAfterAuth(request);
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
			}
		}
		request.setAttribute("merchant", merchantEntity);
		return "takeout/waimai";
	}

	// 微信--跳转到购物车
	@RequestMapping(params = "shoppingcar")
	public String shoppingcar(HttpServletRequest request, ModelMap modelMap,
			String params, Integer merchantId) {
		Integer userId = this.getUserId(request, null);
		MerchantEntity merchantEntity = null;
		if (merchantId != null && merchantId > 0) {
			merchantEntity = this.systemService.get(MerchantEntity.class,
					merchantId);
		}
		if (merchantEntity == null) {// 店铺不存在时跳回wap首页
			if (ConfigUtil.isTest || !this.isWeixin(request) || userId != null) {
				return this.merchantListAfterAuth(request);
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
			}
		}

		String waimaiUrl = ConfigUtil.MENULIST_URI.replace("MERCHANTID",
				merchantId + "");
		WUserEntity user = null;
		if (userId != null) {
			user = systemService.get(WUserEntity.class, userId);
		}
		if (userId == null || user == null) {// 用户未登录或不存在时跳转到店铺首页进行登录，非微信时跳转到登录页
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil.getWeiXinRedirectUrl(waimaiUrl);
			}
		}

		// 默认收货地址
		List<AddressEntity> addressList = systemService.findHql(
				"from AddressEntity where userId = ? and isDefault = 'Y'",
				userId);
		AddressEntity ad = new AddressEntity();
		if (addressList != null && addressList.size() > 0) {
			ad = addressList.get(0);
		}
		Date currentDay = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		modelMap.put("currentDay", dateFormat.format(currentDay));
		modelMap.put("timeList",
				TakeOutController.getTimeList(merchantEntity.getDeliveryTime()));// 送餐时间段
		modelMap.put("ad", ad); // 默认收获地址
		modelMap.put("params", params); // 选购的菜品信息
		modelMap.put("merchant", merchantEntity);// 商家信息
		return "takeout/shopingcar";
	}

	/**
	 * wap版--进入订单支付界面
	 * 
	 * @author lfq
	 * @param orderid
	 *            订单id
	 * @return
	 * @throws SDKRuntimeException
	 */
	@RequestMapping(params = "payOrder")
	public String payOrder(HttpServletRequest request, Integer orderid) {
		Integer userId = this.getUserId(request, null);// 获取登录的用户id
		OrderEntity order = null; // 订单
		MerchantEntity merchant = null; // 订单店铺
		WUserEntity user = null; // 订单用户
		try {
			if (orderid != null && orderid > 0) {
				order = systemService.get(OrderEntity.class, orderid);
			}

			if (userId == null || order == null) {// 用户未登录或订单不存在时跳回wap首页
				if (ConfigUtil.isTest || !this.isWeixin(request)) {
					return this.merchantListAfterAuth(request);
				} else {
					return "redirect:"
							+ AdvancedUtil
									.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
				}
			}

			merchant = order.getMerchant();
			user = order.getWuser();

			if (userId.intValue() != user.getId()) {// 当前登录用户和订单用户不一致时跳转进入订单的店铺
				if (ConfigUtil.isTest || !this.isWeixin(request)) {
					return this.waimaiAfterAuth(request, merchant.getId());
				} else {
					return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(String.format(ConfigUtil.MENULIST_URI, merchant.getId()));
				}
			}

			// 订单关联的菜品选购数据列表
			List<GoodsVO> list = new ArrayList<GoodsVO>();
			// 所有商品的总价
			double totalprice = 0;
			// 总数量
			int totalnum = 0;

			// 订单下的菜选购情况
			List<Map<String, Object>> tempList = systemService.findForJdbc(
					"select * from order_menu where ORDER_ID = ?",
					order.getId());
			if (tempList != null && tempList.size() > 0) {
				for (int i = 0; i < tempList.size(); i++) {
					Map<String, Object> map = tempList.get(i);

					MenuEntity m = systemService.get(MenuEntity.class,
							((Long) map.get("menu_id")).intValue());

					GoodsVO vo = new GoodsVO();
					vo.setName(m.getName());
					vo.setTypename(m.getMenuType().getName());
					vo.setPrice((Double) map.get("total_price"));
					vo.setNum((Integer) map.get("quantity"));
					vo.setId(m.getId());
					list.add(vo);

					totalprice += (Double) map.get("total_price");
					totalnum += (Integer) map.get("quantity");
				}
			}
			// order.setOrigin(totalprice);
			// 代金卷
			List<CardEntity> cardList = systemService
					.findHql(
							"from CardEntity where (merchant.id = ? or merchant.id = 0) and consume = 'N' and endTime > ? and userId = ? and 5 * credit <= ? ",
							merchant.getId(), DateUtils.getSeconds(), userId,
							(int) totalprice);

			// 更新用户的常用地址
			if (order.getSaleType() == 1) {// 是外卖时如果数据库没保存过该地址则新增用户的送货地址
				List<AddressEntity> addList = systemService
						.findHql(
								"from AddressEntity where name = ? and mobile = ? and userId = ? and addressDetail = ?",
								order.getRealname(), order.getMobile(), userId,
								order.getAddress());
				if (addList == null || addList.size() < 1) {
					String sql = "update address set is_default = 'N' where user_id = ? ";
					systemService.executeSql(sql, userId);
					AddressEntity ad = new AddressEntity();
					ad.setMobile(order.getMobile());
					ad.setName(order.getRealname());
					ad.setUserId(userId);
					ad.setCreateTime(new Date());
					ad.setIsDefault("Y");
					ad.setAddressDetail(order.getAddress());
					systemService.save(ad);
				}
			}
			request.setAttribute("order", order); // 订单
			request.setAttribute("chooseList", list); // 选择的菜品信息
			request.setAttribute("total", totalprice); // 选购总价格
			request.setAttribute("allnum", totalnum); // 选购总数量
			request.setAttribute("cardList", cardList); // 代金卷列表
			request.setAttribute("user", user); // 用户信息
			request.setAttribute("d_score",
					systemService.getSystemConfigValue("score"));// 积分规则兑换分数
			request.setAttribute("d_score_money",
					systemService.getSystemConfigValue("score_money"));// 积分规则兑换金额
			request.setAttribute("d_score_total_prices",
					systemService.getSystemConfigValue("score_total_prices"));// 积分规则多少金额起可以兑换
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			// 异常时如果店铺存在则调转到该店铺，否则调转到wap首页
			return "takeout/error";
		}

		return "takeout/payorder";
	}
	
	// 授权跳转
	@RequestMapping(params = "storeList")
	public String storeList(HttpServletRequest request,
			HttpServletResponse response) {
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.merchantListAfterAuth(request);
		} else {
			return "redirect:"
					+ AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
		}
	}

	// 获取支付信息
	@RequestMapping(params = "getPayInfo")
	@ResponseBody
	public AjaxJson getPayInfo(HttpServletRequest request,
			HttpServletResponse response, Integer orderid) {
		AjaxJson j = new AjaxJson();
		if (orderid == null || orderid < 1) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("订单数据有误");
			return j;
		}
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("登录失效，请重新登录");
			return j;
		}
		WUserEntity user = systemService.get(WUserEntity.class, userId);
		if (user == null) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("登录失效，用户不存在");
			return j;
		}

		if (!orderService.verifyMenuRepertoryquantity(orderid)) {// 验证菜单库存是否充足
			j.setMsg("菜单库存不足");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}

		j = menuService.verificationPayMenuPromotion(orderid);// 验证促销菜品信息
		if (!j.isSuccess()) {// 判断是否验证成功
			return j;
		}

		try {
			// 订单
			OrderEntity order = systemService.get(OrderEntity.class, orderid);
			System.out.println("获取支付信息 orderid" + orderid);

			boolean flag = orderService.verifyMenuRepertoryquantity(orderid);
			if (!flag) {
				j.setStateCode("01");
				j.setMsg("库存不足");
				return j;
			}
			// 代金券ID
			String cardid = request.getParameter("cardid");
			// 代金券金额
			String cardMoney = request.getParameter("cardMoney");
			// 积分金额
			String scoreMoney = request.getParameter("scoreMoney");
			// 实际支付金额
			String payMoney = request.getParameter("payMoney");

			// 代金券
			if (!StringUtil.isEmpty(cardid)) {
				order.setCardId(cardid);
				order.setCard(Double.valueOf(cardMoney));
			} else {
				order.setCardId(null);
				order.setCard(0.0);
			}

			// 实际支付金额
			order.setOnlineMoney(Double.valueOf(payMoney));

			// 积分
			if (Double.valueOf(scoreMoney) != 0) {
				order.setScoreMoney(Double.valueOf(scoreMoney));
				order.setScore(100);
			} else {
				order.setScoreMoney(0.0);
				order.setScore(0);
			}
			order.setPayType("weixinpay");
			systemService.saveOrUpdate(order);

			// 商店
			MerchantEntity m = systemService.get(MerchantEntity.class, order
					.getMerchant().getId());

			// 金额
			int money = (int) (Math.rint(Double.valueOf(payMoney) * 100));
			String requestXML = PayService.createJSPackage(m.getTitle()
					+ "-微信公众号支付", money + "", order.getPayId(),
					user.getOpenId(), request);
			System.out.println("生成的支付信息结果为：" + requestXML);

			String result = CommonUtil.httpsRequest(
					ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);

			Map<String, String> map = XMLUtil.doXMLParse(result);

			SortedMap<String, String> params = new TreeMap<String, String>();
			params.put("appId", ConfigUtil.APPID_KFZ);
			params.put("timeStamp", Long.toString(new Date().getTime()));
			params.put("nonceStr", PayCommonUtil.CreateNoncestr());
			params.put("package", "prepay_id=" + map.get("prepay_id"));
			params.put("signType", ConfigUtil.SIGN_TYPE);
			String paySign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.API_KEY);
			params.put("packageValue", "prepay_id=" + map.get("prepay_id")); // 这里用packageValue是预防package是关键字在js获取值出错
			params.put("paySign", paySign); // paySign的生成规则和Sign的生成规则一致
			String userAgent = request.getHeader("user-agent");
			char agent = userAgent
					.charAt(userAgent.indexOf("MicroMessenger") + 15);
			params.put("agent", new String(new char[] { agent }));// 微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。

			// String orderNo = order.getPayId();
			// PayEntity pay = systemService.get(PayEntity.class, orderNo);

			// if (pay == null) {
			// pay = new PayEntity();
			// pay.setId(orderNo);
			// pay.setOrderId(order.getId());
			// pay.setBank("微信支付");
			// pay.setMoney(order.getOrigin());
			// pay.setService("wxgzpay");
			// pay.setState("pay");
			// systemService.save(pay);
			// } else {
			// pay.setBank("微信支付");
			// pay.setMoney(order.getOrigin());
			// pay.setService("wxgzpay");
			// pay.setState("pay");
			// systemService.saveOrUpdate(pay);
			// }
			j.setObj(params);
			return j;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		return null;
	}
	
	/**
	 * wap余额支付
	 * @param orderid 订单id
	 * @param cardid 代金券id
	 * @return
	 */
	@RequestMapping(params = "balancePay")
	public String balancePay(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Integer orderid,
			Integer cardid) {
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
			}
		}

		try {
			// 代金券金额
			String cardMoney = request.getParameter("cardMoney");
			// 积分金额
			String scoreMoney = request.getParameter("scoreMoney");
			// 余额金额
			String payMoney = request.getParameter("payMoney");

			OrderEntity order = systemService.get(OrderEntity.class, orderid);
			if (userId.intValue() != order.getWuser().getId()) {
				return "takeout/login";
			}
			WUserEntity wuser = order.getWuser();
			AjaxJson ajaxJson = new AjaxJson();
			Integer score = 0;
			if (StringUtils.isNotEmpty(scoreMoney) && Double.valueOf(scoreMoney) != 0) {
				if (Double.valueOf(scoreMoney) > Double
						.parseDouble(systemService
								.getSystemConfigValue("score_money"))) {
					throw new RuntimeException("积分抵消金额不对");
				}
				score = Integer.parseInt(systemService.getSystemConfigValue("score"));
			}

			// 判断商家是否在营业
			if (orderService.merchantWhetherDoBusiness(order.getMerchant().getId())) {
				// 验证菜单库存是否充足
				if (!orderService.verifyMenuRepertoryquantity(order.getId())) {
					ajaxJson.setMsg("菜单库存不足");
					ajaxJson.setStateCode("01");
					ajaxJson.setSuccess(false);

				} else {
					ajaxJson = menuService.verificationPayMenuPromotion(order.getId());// 验证促销菜品信息
					if (ajaxJson.isSuccess()) {// 判断是否验证成功
						ajaxJson = payService.orderPay(order.getId(),
								wuser.getId(), order.getMobile(), cardid + "",
								Integer.parseInt(cardMoney), score,
								Double.parseDouble(payMoney), 0, "balance",
								order.getTimeRemark(), request, response);
					}
				}

			} else {
				ajaxJson.setMsg("商家尚未开店，休息当中");
				ajaxJson.setStateCode("01");
				ajaxJson.setSuccess(false);
			}

			if (!ajaxJson.getStateCode().equals("01")) {
				// 支付成功绑定手机
				if (StringUtils.isEmpty(wuser.getMobile())) {
					wuser.setMobile(order.getMobile());
				}
			}

			modelMap.put("result", ajaxJson);
			modelMap.put("orderid", orderid);
			modelMap.put("merchantid", order.getMerchant().getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			return "takeout/error";
		}

		return "takeout/payresult";
	}

	// 微信--微信支付页面
	@RequestMapping(params = "payPage")
	public ModelAndView payPage(HttpServletRequest request, Integer orderid) {
		OrderEntity order = systemService.get(OrderEntity.class, orderid);
		request.setAttribute("order", order);
		return new ModelAndView("/takeout/paypage");
	}

	// 微信--支付结果页面
	@RequestMapping(params = "payresult")
	public String payresult(HttpServletRequest request, ModelMap map,
			Integer orderid, Integer merchantid) {
		map.put("orderid", orderid);
		map.put("merchantid", merchantid);
		return "takeout/payresult";
	}

	/**
	 * 登录界面
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param 登录后跳转的地址
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(HttpServletRequest request, ModelMap modelMap,
			String backUrl) {
		if (StringUtil.isEmpty(backUrl)) {
			backUrl = ConfigUtil.INDEX_URI;
		}
		modelMap.put("backUrl", backUrl);
		return "takeout/login";
	}

	/**
	 * 用户登录
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param username
	 *            登录帐号/手机号
	 * @param password
	 *            登录密码
	 * @return
	 */
	@RequestMapping(params = "doLogin")
	public void doLogin(HttpServletRequest request, ModelMap modelMap,
			String username, String password) {

	}

	// 我的订单-微信跳转
	@RequestMapping(params = "orderList")
	public String orderList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String requestUrl = ConfigUtil.ORDER_LIST_URI;
		if (this.getUserId(request, null) == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				requestUrl = "takeout/login";
			} else {
				requestUrl = "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.INDEX_URI);
			}
		} else {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				requestUrl = "redirect:" + requestUrl;
			} else {
				requestUrl = "redirect:"
						+ AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
			}
		}
		return requestUrl;
	}

	// 我的订单
	@RequestMapping(params = "orderListAfterAuth")
	public String orderListAfterAuth(HttpServletRequest request) {
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.ORDER_LIST_URI);
			}
		}
		List<OrderEntity> list1 = systemService
				.findByQueryString("from OrderEntity where wuser.id = "
						+ userId
						+ " and (orderType = 'normal' or orderType = 'mobile' or orderType = 'supermarket' "
						+ " or orderType = 'scan_order' or orderType = 'third_part') order by createTime desc");

		// 获取所有商品
		List<MenuEntity> allMenus = systemService.findByQueryString("from MenuEntity");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<OrderEntity> data = new ArrayList<OrderEntity>();
		if (list1 != null && list1.size() > 0) {
			for (OrderEntity order : list1) {
				List<GoodsVO> menuList = new ArrayList<GoodsVO>();

				// 获取所有的商品
				List<Map<String, Object>> tempList = systemService
						.findForJdbc(
								"select menu_id,total_price,quantity  from order_menu where ORDER_ID = ?",
								order.getId());

				if (tempList != null && tempList.size() > 0) {

					for (int i = 0; i < tempList.size(); i++) {
						Map<String, Object> map = tempList.get(i);

						if (allMenus != null && allMenus.size() > 0) {
							for (MenuEntity m : allMenus) {
								if (m.getId() == ((Long) map.get("menu_id"))
										.intValue()) {
									GoodsVO vo = new GoodsVO();
									vo.setName(m.getName());
									vo.setTypename(m.getMenuType().getName());
									vo.setPrice((Double) map.get("total_price"));
									vo.setNum((Integer) map.get("quantity"));
									vo.setId(m.getId());
									vo.setImage(systemService
											.getSystemConfigValue("logo_url")
											+ m.getImage());
									menuList.add(vo);
									break;
								}
							}
						}
					}
				}
				order.setMenuList(menuList);
				order.setCreateDate(sdf.format(new Date((long) order
						.getCreateTime() * 1000)));
				order.setPayDate(sdf.format(new Date(
						(long) order.getPayTime() * 1000)));

				data.add(order);
			}
		}
		request.setAttribute("orderList", data);
		return "takeout/orderlist";
	}

	// 绑定手机
	@RequestMapping(params = "bindMobile")
	@ResponseBody
	public AjaxJson bindMobile(HttpServletRequest request, String code,
			String mobile) {
		AjaxJson j = new AjaxJson();

		try {
			Integer userId = this.getUserId(request, null);
			if (userId == null) {
				j.setSuccess(false);
				j.setMsg("登录凭证失效,请重新登录");
			} else {
				WUserEntity tempUser = wUserService.getUserByMobile(mobile,
						"user");
				if (tempUser != null && tempUser.getId() != userId.intValue()) {
					j.setSuccess(false);
					j.setMsg("抱歉！该手机号已经被其他帐号绑定了");
				} else if (tempUser != null
						&& tempUser.getId() == userId.intValue()) {
					j.setSuccess(true);
					j.setMsg("亲，你已经绑定过该手机号了");
				} else {
					WUserEntity wuser = systemService.get(WUserEntity.class,
							userId);
					wuser.setMobile(mobile);
					systemService.updateEntitie(wuser);
					j.setSuccess(true);
					j.setMsg("成功绑定手机号");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			j.setSuccess(false);
			j.setMsg("绑定失败,请重试");
		}
		return j;
	}

	// 检查号码是否已被使用
	@RequestMapping(params = "checkMobile")
	@ResponseBody
	public AjaxJson checkMobile(HttpServletRequest request, String mobile) {
		AjaxJson j = new AjaxJson();
		int userId = this.getUserId(request, null);
		WUserEntity user = this.wUserService.getUserByMobile(mobile, "user");

		if (user != null) {
			if (user.getId().intValue() == userId) {
				j.setSuccess(false);
				j.setMsg("亲，你目前已绑定该手机号了");
			} else {
				j.setSuccess(false);
				j.setMsg("该手机号码已被其他帐号绑定,请使用其他手机号码");
			}
		} else {
			j.setSuccess(true);
			j.setMsg("手机号码可以使用");
		}
		return j;
	}

	/**
	 * 我的信息微信授权跳转
	 */
	@RequestMapping(params = "selfInfo")
	public String selfInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.SELF_INFO_URI;
		Integer userId = this.getUserId(request, null);
		if (ConfigUtil.isTest || !this.isWeixin(request) || userId != null) {
			return this.selfInfoAfterAuth(request);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的信息
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @return
	 */
	@RequestMapping(params = "selfInfoAfterAuth")
	public String selfInfoAfterAuth(HttpServletRequest request) {
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.SELF_INFO_URI);
			}
		}
		WUserEntity userEntity = systemService.get(WUserEntity.class, userId);
		request.setAttribute("user", userEntity);

		SystemconfigEntity sf = systemService.findUniqueByProperty(
				SystemconfigEntity.class, "code", "user_url");
		request.setAttribute("rootPath", sf.getValue());

		return "takeout/selfinfo";
	}

	/**
	 * 我的代金券微信授权跳转
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 */
	@RequestMapping(params = "myCard")
	public String myCard(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.MYCARD_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.myCardAfterAuth(request);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的代金券
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @return
	 */
	@RequestMapping(params = "myCardAfterAuth")
	public String myCardAfterAuth(HttpServletRequest request) {
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MYCARD_URI);
			}
		}
		// 我的代金券
		List<CardEntity> cardList = systemService.findHql(
				"from CardEntity where userId = ? order by consume desc",
				userId);
		request.setAttribute("cardList", cardList);
		return "takeout/mycard";
	}

	/**
	 * 获取店铺的开关状态
	 * 
	 * @author lfq
	 * @param merchantId
	 *            店铺id
	 * @return
	 */
	@RequestMapping(params = "checkMerchantState")
	@ResponseBody
	public AjaxJson checkMerchantState(HttpServletRequest request,
			Integer merchantId) {
		AjaxJson j = new AjaxJson();
		if (merchantId == null || merchantId < 1) {
			j.setSuccess(false);
			j.setMsg("错误请求");
			return j;
		}
		MerchantEntity merchant = systemService.get(MerchantEntity.class,
				merchantId);
		if (merchant == null) {
			j.setSuccess(false);
			j.setMsg("获取店铺信息失败，不存在该店铺信息");
			return j;
		}
		// 判断是否开店
		if ("N".equals(merchant.getDisplay())) {
			j.setObj("close");
		} else {
			Calendar calendar = Calendar.getInstance();
			int hours = calendar.get(Calendar.HOUR_OF_DAY); // 时
			int minutes = calendar.get(Calendar.MINUTE); // 分
			int seconds = calendar.get(Calendar.SECOND); // 秒
			int nowTime = hours * 3600 + minutes * 60 + seconds;
			if (nowTime >= merchant.getStartTime()
					&& nowTime <= merchant.getEndTime()) {
				j.setObj("open");
			} else {
				j.setObj("close");
			}
		}
		return j;
	}

	/**
	 * 获取菜单列表-wap版用 (区别是不用传用户id，如果登录了就从session取用户id)
	 * 
	 * @author lfq
	 * @param request
	 * @param response
	 * @param merchantId
	 *            店铺id,必须传
	 * @return
	 */
	@RequestMapping(params = "getMenuList")
	@ResponseBody
	public AjaxJson getMenuList(HttpServletRequest request,
			HttpServletResponse response, Integer merchantId) {
		AjaxJson result = new AjaxJson();
		if (merchantId == null || merchantId < 1) {
			result.setSuccess(false);
			result.setMsg("错误请求");
		}
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			userId = 0;
		}
		result.setObj(menuService.getMenuList(merchantId, userId));
		return result;
	}

	/**
	 * 创建未付款订单-wap版 (区别是不用传用户id，如果登录了就从session取用户id)
	 * 
	 * @author lfq
	 * @param mobile
	 *            手机号
	 * @param realname
	 *            收货人姓名
	 * @param address
	 *            收货人地址
	 * @param title
	 *            订单备注：店铺名+支付方式
	 * @param params
	 *            订单菜品参数:json数据,格式[{menuId:菜品id,num:数量,menuPromotionId:促销id,
	 *            salesPromotion:是否促销(Y|N),dough:价格},...]
	 * @param timeRemark送货时间
	 * @return
	 */
	@RequestMapping(params = "createOrder")
	@ResponseBody
	public AjaxJson createOrder(HttpServletRequest request, String mobile,
			String realname, String address, String params, String timeRemark,
			String title, Integer merchantId, Integer saleType) {

		Integer userId = this.getUserId(request, null);
		AjaxJson j = new AjaxJson();
		if (userId == null) {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("抱歉！登录凭证失效或未登录,请重新登录");
			return j;
		} else if (saleType == null
				|| (saleType != 1 && saleType != 2)
				|| (saleType == 1 && (StringUtil.isEmpty(mobile)
						|| StringUtil.isEmpty(realname)
						|| StringUtil.isEmpty(address)
						|| StringUtil.isEmpty(params)
						|| StringUtil.isEmpty(timeRemark) || StringUtil
							.isEmpty(title)))) {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("操作失败,请求数据不完整,请刷新");
			return j;
		}
		// 判断商家是否在开店
		if (!orderService.merchantWhetherDoBusiness(merchantId)) {
			j.setMsg("商家尚未开店,休息中");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
		System.out.println("菜品：============" + params);
		j = orderService.verifyMenuQuantity(params, userId);// 验证菜品库存是否还有

		if (j.isSuccess()) {// 判断菜品库存是否还有

			int orderId = orderService.createOrder(userId, merchantId, mobile,
					realname, address, params, "", "normal", saleType,
					timeRemark);

			if (orderId == 0) {
				j.setMsg("订单金额非法");
				j.setStateCode("01");
				j.setSuccess(false);
			} else {
				orderStateService.createOrderState(orderId);// 订单生成状态信息
				OrderEntity order = systemService.get(OrderEntity.class,
						orderId);

				if (order.getSaleType() != 1) {// 判断是否是堂食
					orderService.setOrderNum(order);// 设置排号
				}

				Map<String, Object> objs = new HashMap<String, Object>();
				objs.put("orderId", orderId);
				j.setObj(objs);
				j.setSuccess(true);
			}
		}
		return j;
	}

	/**
	 * 进入商家详情界面
	 * 
	 * @author lfq
	 * @param merchantId
	 *            商家id
	 * @return
	 */
	@RequestMapping(params = "merchantDetail")
	public String merchantDetail(ModelMap modelMap, Integer merchantId) {

		String sql = "SELECT id,m.bidding_money, title, substr( SEC_TO_TIME(start_time), 1, 5 ) start_time,"
				+ " substr(SEC_TO_TIME(end_time), 1, 5) end_time, delivery_begin, notice, address, mobile,"
				+ "CONCAT( t.`value`,m.logo_url) logo_url,location FROM merchant m,"
				+ " ( select `value` FROM system_config WHERE `CODE` = 'img_url' ) t WHERE m.id = "
				+ merchantId;
		Map<String, Object> merchantDetail = systemService.findOneForJdbc(sql);
		if (merchantDetail != null) {
			modelMap.put("merchantDetail", merchantDetail);
			String location = merchantDetail.get("location") + "";
			if (!StringUtil.isEmpty(location)) {// 取出经纬度
				String[] pt = location.split(",");
				modelMap.put("lat", pt[0]);
				modelMap.put("lng", pt[1]);
			}
		}
		return "takeout/merchantDetail";
	}

	/**
	 * 进入百度地图界面
	 * 
	 * @author lfq
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @param name
	 *            标注名字
	 * @param address
	 *            标注地址
	 * @return
	 */
	@RequestMapping(params = "toMap")
	public String toMap(ModelMap modelMap, String lat, String lng, String name,
			String address) {
		modelMap.put("lat", lat);
		modelMap.put("lng", lng);
		modelMap.put("name", name);
		modelMap.put("address", address);
		return "takeout/map";
	}

	/**
	 * 菜品详情页微信授权跳转
	 * 
	 * @author lfq
	 * @param menuId
	 *            菜品id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "menu")
	public String menu(HttpServletRequest request,HttpServletResponse response, ModelMap modelMap, Integer menuId) throws IOException {
		
		// 升级至1.8版本
		if("false".equals(isMixVersion)){
			MenuEntity menu = menuService.get(MenuEntity.class, menuId);
			return "redirect:" + ConfigUtil.MENU_HOME.replace("MERCHANT_ID", menu.getMerchantId() + "").replace("MENU_ID", menuId + "");
		}
		
		if(getUserId(request, null) != null) {
			return menuAfterAuth(request, response, modelMap, menuId);
		} else if (isWeixin(request)) {
			String requestUrl = ConfigUtil.MENU_URI.replace("MENU_ID", menuId+"");
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		} else {
			return "main/warn";
		}
		
	}

	/**
	 * 菜品详情页
	 * 
	 * @author lfq
	 * @param menuId
	 *            菜品id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(params = "menuAfterAuth")
	public String menuAfterAuth(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, Integer menuId)
			throws IOException {
		
		if (menuId == null || menuId <= 0) 
			return "redirect:" + ConfigUtil.INDEX_URI;
		
		Integer userId = this.getUserId(request, null);
		
		MenuVo vo = menuService.getMenuById(menuId, userId);
		MerchantEntity mer = merchantService.queryByMenuId(menuId);
		//Map<String, Object> menuDetail = menuService.getMenu(menuId,userId);
		modelMap.put("menu", vo);
		modelMap.put("mer", mer);
		modelMap.put("userId", userId);
		modelMap.put("location", getLocation(request));
		return "takeout/menuDetail";
	}

	/**
	 * 信步游戏微信授权跳转
	 * 
	 * @author lfq
	 * @param gameSecret
	 *            游戏凭证加密串
	 * @param gameOpenId
	 *            游戏openId
	 * @return
	 */
	@RequestMapping(params = "game")
	public String game(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, String gameSecret,
			String gameOpenId) {
		String requestUrl = ConfigUtil.GAME_URI.replace("GAME_SECRET",
				gameSecret + "").replace("GAME_OPENID", gameOpenId + "");
		if (ConfigUtil.isTest || this.getUserId(request, null) != null) {
			return this.gameAfterAuth(request, response, modelMap, gameSecret,
					gameOpenId);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 信步游戏授权后跳转进入游戏
	 * 
	 * @author lfq
	 * @param gameSecret
	 *            游戏凭证加密串
	 * @param gameOpenId
	 *            游戏openId
	 * @return 游戏进入的地址
	 */
	@RequestMapping("gameAfterAuth")
	public String gameAfterAuth(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap, String gameSecret,
			String gameOpenId) {
		Integer userId = this.getUserId(request, null);
		String msg = "抱歉！授权失败，请刷新界面重试";
		if (userId != null) {// 授权成功重定向进入游戏并带上参数openId
			WUserEntity userEntity = this.systemService.get(WUserEntity.class,
					userId);
			if (userEntity != null) {
				GameEntity gameEntity = this.gameService
						.getByGameOpenId(gameOpenId);
				if (gameEntity != null) {
					return "redirect:" + gameEntity.getUrl() + "?openId="
							+ userEntity.getUnionId() + "&gameSecret="
							+ gameSecret + "&gameOpenId=" + gameOpenId;
				} else {
					msg = "抱歉!该游戏未在1号外卖平台注册";
				}
			}
		}
		modelMap.put("msg", msg);
		return "takeout/error";
	}

	/**
	 * 信步游戏积分兑换 输出json格式结果
	 * 
	 * @author lfq
	 * @param openId
	 *            1号外卖公众号里微信用户的openId
	 * @param gameSecret
	 *            游戏凭证加密串
	 * @param gameOpenId
	 *            游戏openId
	 * @param gameScore
	 *            游戏分数
	 */
	@RequestMapping("synchroGameScore")
	public void synchroGameScoreAfterAuth(HttpServletRequest request,
			HttpServletResponse response, String openId, String gameSecret,
			String gameOpenId, Integer gameScore) {
		Integer userId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", "-1");
		resultMap.put("gameSecret", gameSecret);
		resultMap.put("gameOpenId", gameOpenId);
		resultMap.put("gameScore", gameScore);
		resultMap.put("score", 0);
		try {
			String unionid = openId;
			if (StringUtil.isEmpty(unionid)) {
				resultMap.put("code", 8);
				resultMap.put("msg", "参数有误");
			} else {
				userId = this.getUserId(request, unionid);
			}
			if (userId != null) {
				if (StringUtil.isEmpty(gameSecret)) {
					resultMap.put("code", 1);
					resultMap.put("msg", "参数有误");
				} else if (!gameSecret.equals(systemService
						.getSystemConfigValue("GAME_SECRET"))) {
					resultMap.put("code", 2);
					resultMap.put("msg", "参数有误");
				} else if (StringUtil.isEmpty(gameOpenId)) {
					resultMap.put("code", 3);
					resultMap.put("msg", "参数有误");
				} else if (gameScore == null) {
					resultMap.put("code", 4);
					resultMap.put("msg", "参数有误");
				} else {
					GameEntity gameEntity = this.gameService
							.getByGameOpenId(gameOpenId);
					if (gameEntity == null) {
						resultMap.put("code", 5);
						resultMap.put("msg", "该游戏未注册1号外卖平台");
					} else if ("0".equals(gameEntity.getEnabled())) {
						resultMap.put("code", 6);
						resultMap.put("msg", "该游戏未在1号外卖平台启用积分规则");
					} else if (gameScore < gameEntity.getGameScore()) {
						resultMap.put("code", 7);
						resultMap
								.put("msg",
										"亲！再努力"
												+ (gameEntity.getGameScore() - gameScore)
												+ "分送1号外卖积分哦!");
					} else {
						resultMap.put("code", "0");
						Integer score = (gameEntity.getScore() * gameScore)
								/ gameEntity.getGameScore();// 用户得到的积分
						WUserEntity userEntity = systemService.get(
								WUserEntity.class, userId);
						userEntity.setScore(userEntity.getScore() + score);
						systemService.updateEntitie(userEntity);// 更新积分

						CreditEntity creditEntity = new CreditEntity();
						creditEntity.setAction("game");
						creditEntity.setCreateTime(DateUtils.getSeconds());
						creditEntity.setDetailId(gameEntity.getId());
						creditEntity.setDetail("游戏积分");
						creditEntity.setScore(score);
						creditEntity.setWuser(userEntity);
						systemService.save(creditEntity);

						resultMap.put("score", score);
						resultMap.put("msg", "非常棒，奖励你1号外卖" + score + "积分");
					}
				}
			} else {
				resultMap.put("code", -1);
				resultMap.put("msg", "获取用户登录凭证失效");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			resultMap.put("code", 500);
			resultMap.put("msg", "程序异常啦");
		}
		JSONUtil.printToHTML(response, resultMap);
	}

	/**
	 * 信步游戏每天签到送积分 输出json格式结果
	 * 
	 * @author lfq
	 * @param openId
	 *            1号外卖公众号里微信用户的openId
	 * @param gameSecret
	 *            游戏凭证加密串
	 * @param gameOpenId
	 *            游戏openId
	 */
	@RequestMapping("signin")
	public void signin(HttpServletRequest request,
			HttpServletResponse response, String openId, String gameSecret,
			String gameOpenId) {
		Integer userId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", "-1");
		resultMap.put("gameSecret", gameSecret);
		resultMap.put("gameOpenId", gameOpenId);
		resultMap.put("score", 0);
		try {
			if (StringUtil.isEmpty(openId)) {
				resultMap.put("code", 8);
				resultMap.put("msg", "参数有误");
			} else {
				userId = this.getUserId(request, openId);
			}

			if (userId != null) {
				if (StringUtil.isEmpty(gameSecret)) {
					resultMap.put("code", 1);
					resultMap.put("msg", "参数有误");
				} else if (!gameSecret.equals(systemService
						.getSystemConfigValue("GAME_SECRET"))) {
					resultMap.put("code", 2);
					resultMap.put("msg", "参数有误");
				} else if (StringUtil.isEmpty(gameOpenId)) {
					resultMap.put("code", 3);
					resultMap.put("msg", "参数有误");
				} else {
					GameEntity gameEntity = this.gameService
							.getByGameOpenId(gameOpenId);
					if (gameEntity == null) {
						resultMap.put("code", 5);
						resultMap.put("msg", "该游戏未注册1号外卖平台");
					} else if ("0".equals(gameEntity.getEnabled())) {
						resultMap.put("code", 6);
						resultMap.put("msg", "该游戏未在1号外卖平台启用积分规则");
					} else {
						List<CreditEntity> cList = this.creditService
								.getListByCondition(userId, gameEntity.getId(),
										"signin", true);
						if (cList != null && cList.size() > 0) {
							resultMap.put("code", 4);
							resultMap.put("msg", "亲,您今天已经签到过了，明天再来吧");
						} else {
							WUserEntity userEntity = systemService.get(
									WUserEntity.class, userId);
							resultMap.put("code", "0");
							Integer score = Integer
									.parseInt(systemService
											.getSystemConfigValue("waimai_singin_score"));// 用户签到得到的积分

							userEntity.setScore(userEntity.getScore() + score);
							systemService.updateEntitie(userEntity);// 更新积分

							CreditEntity creditEntity = new CreditEntity();
							creditEntity.setAction("signin");
							creditEntity.setCreateTime(DateUtils.getSeconds());
							creditEntity.setDetailId(gameEntity.getId());
							creditEntity.setDetail("签到积分");
							creditEntity.setScore(score);
							creditEntity.setWuser(userEntity);
							systemService.save(creditEntity);

							resultMap.put("score", score);
							resultMap.put("msg", "恭喜你，签到成功，获得1号外卖" + score
									+ "积分");
						}
					}
				}
			} else {
				resultMap.put("code", -1);
				resultMap.put("msg", "获取用户登录凭证失效");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
			resultMap.put("code", 500);
			resultMap.put("msg", "程序异常啦");
		}
		JSONUtil.printToHTML(response, resultMap);
	}

	/**
	 * 进入手机绑定界面授权跳转
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @return
	 */
	@RequestMapping(params = "signmobile")
	public String signmobile(HttpServletRequest request, ModelMap modelMap) {
		String requestUrl = ConfigUtil.SIGNMOBILE_URI;
		if (ConfigUtil.isTest || this.getUserId(request, null) != null) {
			return this.signmobileAfter(request, modelMap);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 进入手机绑定界面
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @return
	 */
	@RequestMapping(params = "signmobileAfter")
	public String signmobileAfter(HttpServletRequest request, ModelMap modelMap) {
		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			return "takeout/login";
		} else {
			return "takeout/signmobile";
		}
	}

	/**
	 * 根据订单id取消未付款订单 输出json格式:{code:结果代码,msg:结果消息} 结果代码code:-1
	 * 表示未登录;0表示操作成功;1表示操作失败
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderId
	 *            订单id
	 */
	@RequestMapping(params = "cancelUnpayOrder")
	public void cancelUnpayOrder(HttpServletRequest request,
			HttpServletResponse response, Integer orderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", -1);
		result.put("msg", "未登录");
		result.put("orderId", orderId);
		try {
			Integer userId = this.getUserId(request, null);
			if (userId == null || userId < 1) {
				result.put("code", -1);
				result.put("msg", "未登录");
			} else if (orderId == null || orderId < 0) {
				result.put("code", 2);
				result.put("msg", "订单有误");
			} else {
				Boolean flag = this.orderService.cancelUnpayOrder(orderId,
						userId);
				if (flag) {
					result.put("code", 0);
					result.put("msg", "成功取消订单");
				} else {
					result.put("code", 1);
					result.put("msg", "取消订单失败");
				}
			}
		} catch (Exception e) {
			result.put("code", 500);
			result.put("msg", "操作异常");
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		JSONUtil.printToHTML(response, result);
	}

	/**
	 * 用户未出单订单退款 输出json格式:{code:结果代码,msg:结果消息} 结果代码code:-1 表示未登录;0表示退款成功;1表示退款失败
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderid
	 *            订单id
	 * @param refundReason
	 *            退款原因
	 */
	@RequestMapping(params = "orderRefund")
	public void orderRefund(HttpServletRequest request,
			HttpServletResponse response, Integer orderId, String refundReason) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Integer userId = this.getUserId(request, null);
			if (userId == null || userId < 1) {
				result.put("code", -1);
				result.put("msg", "未登录");
			} else if (orderId == null || orderId < 0) {
				result.put("code", 2);
				result.put("msg", "操作有误");
			} else if (StringUtil.isEmpty(refundReason)) {
				result.put("code", 3);
				result.put("msg", "申请退款原因不能为空");
			} else {
				Boolean flag = orderService.orderRefund(orderId, refundReason,
						userId);
				if (flag) {
					result.put("code", 0);
					result.put("msg", "申请退款成功,订单金额已退回您的帐号");
				} else {
					result.put("code", 1);
					result.put("msg", "申请退款失败");
				}
			}
		} catch (Exception e) {
			result.put("code", 500);
			result.put("msg", "操作异常");
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		JSONUtil.printToHTML(response, result);
	}

	/**
	 * 用户申请退单 输出json格式:{code:结果代码,msg:结果消息} 结果代码code:-1
	 * 表示未登录;0表示申请退单成功;1表示申请退单失败
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param orderId
	 *            订单id
	 * @param refundReason
	 *            退款原因
	 */
	@RequestMapping(params = "askRefund")
	public void askRefund(HttpServletRequest request,
			HttpServletResponse response, Integer orderId, String refundReason) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			Integer userId = this.getUserId(request, null);
			if (userId == null) {
				result.put("code", -1);
				result.put("msg", "未登录");
			} else if (orderId == null || orderId < 0) {
				result.put("code", 2);
				result.put("msg", "操作有误");
			} else if (StringUtil.isEmpty(refundReason)) {
				result.put("code", 3);
				result.put("msg", "申请退款原因不能为空");
			} else {
				Boolean flag = orderService.askRefund(orderId, refundReason,
						userId);
				if (flag) {
					result.put("code", 0);
					result.put("msg",
							"订单申请退款已提交，请耐心等待商家的审核，商家审核通过后订单的金额将直接退回到您的帐号");
				} else {
					result.put("code", 1);
					result.put("msg", "订单申请退款提交失败");
				}
			}
		} catch (Exception e) {
			result.put("code", 500);
			result.put("msg", "操作异常");
			e.printStackTrace();
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		JSONUtil.printToHTML(response, result);
	}

	/**
	 * 获取我的订单分页列表 输出json格式:{code:结果代码,msg:结果消息,orderList:[{每个订单数据},...]}
	 * code：-1用户未登录，0正常返回数据，500请求异常
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param start
	 *            开始位置
	 * @param pageSize
	 *            每次请求记录数量
	 */
	@RequestMapping(params = "getOrderList")
	public void getOrderList(HttpServletRequest request,
			HttpServletResponse response, Integer start, Integer pageSize,
			String[] state) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Integer userId = this.getUserId(request, null);
			if (userId == null || userId < 1) {
				result.put("code", -1);
				result.put("msg", "用户未登录");
			} else {
				List<Map<String, Object>> orderList = this.orderService
						.getOrderList(start, pageSize, userId, null, state);
				result.put("orderList", orderList);
				result.put("code", 0);
				result.put("msg", "成功返回数据");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 500);
			result.put("msg", "请求异常");
			logger.error(e.getMessage(), e.fillInStackTrace());
		}
		JSONUtil.printToHTML(response, result);
	}

	

	/**
	 * 传入一个开始秒数得到时间段,每段时隔15分钟
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param start
	 *            开始秒数
	 * @return
	 */
	public static List<String> getTimeList(Integer start) {
		List<String> timeList = new ArrayList<String>();
		int c_h = 60 * 60; // 一小时的秒数
		int c_m = 60; // 一分钟的秒数

		Calendar c = Calendar.getInstance();

		int startTime = c.get(Calendar.SECOND) + c.get(Calendar.MINUTE) * c_m
				+ c.get(Calendar.HOUR_OF_DAY) * c_h;// 开始秒数
		int oneTime = 15 * c_m; // 每段时隔
		int endTime = 24 * c_h - oneTime;// 结束描述
		if (start > startTime) { // 如果传入的开始秒数大于当前秒数则以传入的秒数为开始时间否则以当前描述为开始时间
			startTime = start;
		}

		for (int i = startTime; i < endTime; i = i + oneTime) {
			int h = i / c_h; // 小时
			int m = (i - h * c_h) / c_m; // 分数
			if (m < 15) {
				m = 15;
			} else if (m < 30) {
				m = 30;
			} else if (m < 45) {
				m = 45;
			} else {
				m = 0;
				h++;
			}
			int h2 = (i + oneTime) / c_h; // 小时
			int m2 = ((i + oneTime) - h2 * c_h) / c_m; // 分数
			if (m2 < 15) {
				m2 = 15;
			} else if (m2 < 30) {
				m2 = 30;
			} else if (m2 < 45) {
				m2 = 45;
			} else {
				m2 = 0;
				h2++;
			}
			String temp = (h < 10 ? "0" + h : "" + h) + ":"
					+ (m < 10 ? "0" + m : "" + m) + "-"
					+ (h2 < 10 ? "0" + h2 : "" + h2) + ":"
					+ (m2 < 10 ? "0" + m2 : "" + m2);
			timeList.add(temp);
		}
		return timeList;
	}
	

	/**
	 * 我的-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mine")
	public String mine(HttpServletRequest request, HttpServletResponse response) {
		if(null != getUserId(request, null)) {
			return this.mineAfterAuth(request);
		}else if (isWeixin(request)) {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.MINE_URL);
		} else {
			return "main/warn";
		}
	}

	/**
	 * 我的
	 * @return
	 */
	@RequestMapping(params = "mineAfterAuth")
	public String mineAfterAuth(HttpServletRequest request) {
		Integer userId = this.getUserId(request, null);
		// 我的
		// 查询用户
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if(user != null){
			setUserInfo2RequestAttr(request, user);
		}
		return "takeout/mine";
	}
	

	/**
	 * 我的-设置-微信跳转
	 * 
	 */
	@RequestMapping(params = "mineSetting")
	public String mineSetting(HttpServletRequest request,
			HttpServletResponse response) {
		return "takeout/mineSetting";
	}

	/**
	 * 我的-关于-微信跳转
	 * 
	 */
	@RequestMapping(params = "mineAbout")
	public String mineAbout(HttpServletRequest request,
			HttpServletResponse response) {
		return "takeout/mineAbout";
	}

	/**
	 * 我的-帐户信息-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineAccountInfo")
	public String mineAccountInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.MINE_ACCOUNT_INFO_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineAccountInfoAfterAuth(request,response);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-帐户信息
	 * 
	 */
	@RequestMapping(params = "mineAccountInfoAfterAuth")
	public String mineAccountInfoAfterAuth(HttpServletRequest request, HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_ACCOUNT_INFO_URI);
			}
		}
		
		// 我的
		// 查询用户
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if(user != null){
			setUserInfo2RequestAttr(request, user);
		}
		request.setAttribute("openid", user.getOpenId());
		return "takeout/mineAccountInfo";
	}

	private void setUserInfo2RequestAttr(HttpServletRequest request,
			WUserEntity user) {
		request.setAttribute("balance",StringUtils.defaultString( String.valueOf(user.getMoney()), "0.0"));//查询用户余额
		request.setAttribute("score", StringUtils.defaultString( String.valueOf(user.getScore()), "0"));//查询用户积分
		request.setAttribute("nickname", user.getNickname());
		request.setAttribute("mobile", StringUtils.stripToNull(user.getMobile()));
		request.setAttribute("photourl", user.getPhotoUrl());
	}

	/**
	 * 我的-消息-微信跳转
	 * 
	 */
	@RequestMapping(params = "mineMsg")
	public String mineMsg(HttpServletRequest request,
			HttpServletResponse response) {
		return "takeout/mineMsg";
	}


	/**
	 * 我的-帐号-手机绑定-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineMobileBind")
	public String mineMobileBind(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.MINE_MOBILE_BIND_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineMobileBindAfterAuth(request,response);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-帐号-手机绑定
	 * 
	 */
	@RequestMapping(params = "mineMobileBindAfterAuth")
	public String mineMobileBindAfterAuth(HttpServletRequest request,
			HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_MOBILE_BIND_URI);
			}
		}
		// 我的-帐号-手机绑定
		return "takeout/mineMobileBind";
	}


	
	/**
	 * 我的-地址列表-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineAddresses")
	public String mineAddresses(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.MINE_ADDREESSES_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineAddressesAfterAuth(request,response);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-地址列表
	 * 
	 */
	@RequestMapping(params = "mineAddressesAfterAuth")
	public String mineAddressesAfterAuth(HttpServletRequest request,
			HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_ADDREESSES_URI);
			}
		}
		
		// 我的-地址管理-列出地址列表
		List<AddressEntity> addressList = systemService.findHql(
				"from AddressEntity where userId = ? ",
				userId);
		if (addressList == null) {
			addressList = Lists.newArrayList();
		}
		request.setAttribute("addressList", addressList);
		return "takeout/mineAddresses";
	}
	
	/**
	 *  我的-地址-新增或编辑地址
	 * 
	 */
	@RequestMapping(params = "mineAddressesNewOrEdit")
	public String mineAddressesNewOrEdit(HttpServletRequest request, HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_ADDREESSES_URI);
			}
		}
		AddressEntity ae = new AddressEntity();
		String addressId = request.getParameter("addressId");
		if(!StringUtils.isBlank(addressId)){
			ae = systemService.singleResult(
					"from AddressEntity where id =  " + addressId);
		}
		// 我的-地址
		List<BuildingEntity> buildingList = queryMineNearbyBuildingsList(request, response);
		request.setAttribute("addressEntity", ae);
		request.setAttribute("buildingList", JSON.toJSONString(buildingList));
		
		return "takeout/mineAddressesNewOrEdit";
	}
	
	/**
	 * 我的-地址-保存或更新地址
	 * 
	 */
	@RequestMapping(params = "mineAddressesSave")
	@ResponseBody
	public AjaxJson mineAddressesSave(HttpServletRequest request, HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		String message = null;
		int addressId = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("addressId"), "0"));
		// 我的-地址管理-保存
		
		Location location = getLocation(request);

		AjaxJson j = new AjaxJson();
		//取消已有默认地址状态
		addressService.cancelAddrDefault(userId);
		if (addressId != 0) {
			AddressEntity ad = addressService.get(AddressEntity.class, addressId);
			try {
				ad.setName(StringUtils.defaultIfBlank(request.getParameter("name"), "无名"));
				ad.setSex(StringUtils.defaultIfBlank(request.getParameter("sex"), "m"));
				ad.setMobile(StringUtils.defaultIfBlank(request.getParameter("mobile"), "未填写号码"));
				ad.setAddressDetail(StringUtils.defaultIfBlank(request.getParameter("addressDetail"), "未填写具体地址"));
				ad.setBuildingName(StringUtils.defaultIfBlank(request.getParameter("buildName"), "无大厦名"));
				String floorStr = request.getParameter("floor");
				if(!StringUtils.isBlank(floorStr) && !floorStr.equals("null") && !floorStr.equals("")){
					ad.setBuildingFloor(Integer.parseInt(floorStr));
				}
				ad.setBuildingId(Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("buildPrimaryId"), "0")));
				ad.setIsDefault("Y");
				if(location != null ){
					ad.setLocation(location.getLng()+ "," + location.getLat());
				}
				addressService.updateEntitie(ad);
				message = "更新成功";
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			AddressEntity ad = new AddressEntity();
			ad.setName(StringUtils.defaultIfBlank(request.getParameter("name"), "无名"));
			ad.setSex(StringUtils.defaultIfBlank(request.getParameter("sex"), "m"));
			ad.setMobile(StringUtils.defaultIfBlank(request.getParameter("mobile"), "未填写号码"));
			ad.setAddressDetail(StringUtils.defaultIfBlank(request.getParameter("addressDetail"), "未填写具体地址"));
			ad.setBuildingName(StringUtils.defaultIfBlank(request.getParameter("buildName"), "无大厦名"));
			String floorStr = request.getParameter("floor");
			if(!StringUtils.isBlank(floorStr) && !floorStr.equals("null") && !floorStr.equals("")){
				ad.setBuildingFloor(Integer.parseInt(floorStr));
			}
			ad.setBuildingId(Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("buildPrimaryId"), "0")));
			ad.setUserId(userId);
			ad.setCreateTime(new Date());
			ad.setIsDefault("Y");
			if(location != null ){
				ad.setLocation(location.getLng()+ "," + location.getLat());
			}
//			ad.setCity(city);
			addressService.save(ad);
			message = "添加成功";
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	
	/**
	 * 我的-地址-删除地址
	 * 
	 */
	@RequestMapping(params = "mineAddressesDelete")
	@ResponseBody
	public AjaxJson mineAddressesDelete(HttpServletRequest request,
			HttpServletResponse response) {
		// 我的-地址管理-删除地址
		String addressId = request.getParameter("addressId");
		Integer id = Integer.valueOf(StringUtils.defaultIfBlank(addressId, "0"));
		AjaxJson j = new AjaxJson();
		String message = null;
		if(id == 0){
			message = "删除失败";
		}else{
			AddressEntity address = systemService.get(AddressEntity.class, id);
			if(address != null) {
				systemService.delete(address);
				if("Y".equals(address.getIsDefault())) {
					List<AddressEntity> list = systemService.findByProperty(AddressEntity.class, "userId", address.getUserId());
					if(list.size() > 0) {
						AddressEntity a = list.get(0);
						a.setIsDefault("Y");
						systemService.updateEntitie(a);
					}
				}
			}
			message = "删除成功";
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	private List<BuildingEntity> queryMineNearbyBuildingsList(HttpServletRequest request, HttpServletResponse response){
		Location location = getLocation(request);
		List<BuildingEntity> buildingList = Lists.newArrayList();
//		String serviceScope = p.readProperty("serviceScope", "0.01");
//		double precision = AddressLocationCons.location_precision;
		Double precision = Double.parseDouble(serviceScope);
		// 查询用户附近的大厦列表
		if(location != null){
			buildingList = systemService.findHql(
					"from BuildingEntity where latitude between ? and ? and longitude between ? and ? ",
					location.getLat() - precision, location.getLat() + precision , location.getLng() - precision, location.getLng() + precision);
		}
		if(buildingList == null || buildingList.isEmpty()){
			if(ConfigUtil.isTest){//方便测试使用
				buildingList = systemService.findHql("from BuildingEntity");
			}
		}
		return buildingList;
		
	}
	/**
	 * 我的-地址-根据用户所在定们的经伟度查询用户附近的大厦列表
	 */
	@RequestMapping(params = "mineNearbyBuildingsList")
	@ResponseBody
	public AjaxJson mineNearbyBuildingsList(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		Location location = getLocation(request);
		// 查询用户附近的大厦列表
		if(location != null){
			Double precision = Double.parseDouble(serviceScope);
			List<BuildingEntity> buildingList = systemService.findHql(
					"from BuildingEntity where latitude between ? and ? and longitude between ? and ? ",
					location.getLat() - precision, location.getLat() + precision , location.getLng() - precision, location.getLng() + precision);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("获取附近的大厦列表成功。");
			j.setObj(buildingList);
			return j;
		}else{
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("获取附近的大厦列表出错，原因：无法获取您的位置信息，请允许应用获取您的位置，或手动设置您的位置。");
			return j;
		}
	}

	
	
	/**
	 * 我的-收藏-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineFavorites")
	public String mineFavorites(HttpServletRequest request, HttpServletResponse response) {
		String requestUrl = ConfigUtil.MINE_FAVORITES_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineFavoritesAfterAuth(request,response);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-收藏
	 * 
	 */
	@RequestMapping(params = "mineFavoritesAfterAuth")
	public String mineFavoritesAfterAuth(HttpServletRequest request,
			HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_FAVORITES_URI);
			}
		}
		
		// 我的
		// 查询用户
		/*WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if(user != null){
			request.setAttribute("balance",StringUtils.defaultString( String.valueOf(user.getMoney()), "0.0"));//查询用户余额
			request.setAttribute("score", StringUtils.defaultString( String.valueOf(user.getScore()), "0"));//查询用户积分
			request.setAttribute("nickname", user.getNickname());
			request.setAttribute("mobile", StringUtils.defaultString( String.valueOf(user.getMobile()), ""));
			request.setAttribute("photourl", user.getPhotoUrl());
		}*/
		
		return "takeout/mineFavorites";
	}


	/**
	 * 我的-待付款-微信授权跳转
	 * 
	 */
	@RequestMapping(value="/mineWaitingPay.do")
	public String mineWaitingPay(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {
		String requestUrl = ConfigUtil.MINE_WAITING_PAY_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineWaitingPayAfterAuth(request,response, page, rows);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-待付款
	 * 
	 */
	@RequestMapping(value="/mineWaitingPayAfterAuth.do")
	public String mineWaitingPayAfterAuth(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_WAITING_PAY_URI);
			}
		}
		
		// 我的-查询未支付的订单状态列表
		AjaxJson j = queryOrderListByState(OrderStateEnum.UNPAY.getOrderStateEn(), page, rows, userId);
		request.setAttribute("omvList", j.getObj());
		return "takeout/mineWaitingPay";
	}

	private AjaxJson queryOrderListByState(String state, int page, int rows, Integer userId) {
		List<OrderMerchantVo> omvList = orderService.queryMineOrderByState(state, userId, page, rows);
		AjaxJson j = new AjaxJson();
		j.setObj(JSON.toJSONString(omvList));
		j.setSuccess(true);
		j.setMsg("查询订单列表成功");
		return j;
	}

	@RequestMapping(params = "queryMineOrderListByState")
	@ResponseBody
	public AjaxJson queryMineOrderListByState(HttpServletRequest request,
			HttpServletResponse response, String state,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows){
		Integer userId = this.getUserId(request, null);
		return queryOrderListByState(state, page, rows, userId);
	}


	/**
	 * 我的-待收货-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineWaitingReceipt")
	public String mineWaitingReceipt(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {
		String requestUrl = ConfigUtil.MINE_WAITING_RECEIPT_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineWaitingReceiptAfterAuth(request,response, page, rows);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-待收货
	 * 
	 */
	@RequestMapping(params = "mineWaitingReceiptAfterAuth")
	public String mineWaitingReceiptAfterAuth(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_WAITING_RECEIPT_URI);
			}
		}

		// 我的-查询待收货的订单状态列表
		AjaxJson j = queryOrderListByState(OrderStateEnum.CUSTOM_RECEIVING.getOrderStateEn() , page, rows, userId);
		request.setAttribute("omvList", j.getObj());
		return "takeout/mineWaitingReceipt";
	}


	/**
	 * 我的-待评价-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineWaitingEvaluates")
	public String mineWaitingEvaluates(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {
		String requestUrl = ConfigUtil.MINE_WAITING_EVALUATES_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineWaitingEvaluatesAfterAuth(request,response,page, rows);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-待评价
	 * 
	 */
	@RequestMapping(params = "mineWaitingEvaluatesAfterAuth")
	public String mineWaitingEvaluatesAfterAuth(HttpServletRequest request,
			HttpServletResponse response, 
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_WAITING_EVALUATES_URI);
			}
		}
		

		// 我的-查询待评价的订单状态列表
		AjaxJson j = queryOrderListByState(OrderStateEnum.CONFIRM.getOrderStateEn() , page, rows, userId);
		request.setAttribute("omvList", j.getObj());
		
		return "takeout/mineWaitingEvaluates";
	}

	/**
	 * 我的-新建评价
	 * 
	 */
	@RequestMapping(params = "mineEvaluateNew")
	public String mineEvaluateNew(
			@RequestParam Integer orderId, 
			@RequestParam Integer merchantId, 
			@RequestParam Integer courierId, 
			Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("merchantId", merchantId);
		model.addAttribute("courierId", courierId);
		model.addAttribute("callbackUrl", "/takeOutController.do?mineWaitingEvaluates");
		return "takeout/mineEvaluateNew";
	}


	/**
	 * 
	 * 我的-保存评价
	 * @param request
	 * @param orderId 订单id
	 * @param courierId 快递员id
	 * @param merchantId 商户id
	 * @param generalContent 商户评价
	 * @param courierContent 快递员评价
	 * @param generalEvaScore 商户分数
	 * @param courierEvaScore 快递分数
	 * @param commentDisplay, Y,N 表示用户已经评价；S为系统自动生成
	 * @return
	 */
	@RequestMapping(params = "mineEvaluateSave")
	@ResponseBody
	public AjaxJson mineEvaluateSave(HttpServletRequest request, Integer orderId, Integer courierId, Integer merchantId, 
			String generalContent, String courierContent, Integer generalEvaScore, Integer courierEvaScore, String commentDisplay) {
		Integer userId = this.getUserId(request, null);
		AjaxJson j = new AjaxJson();
		//系统评论
		if("S".equalsIgnoreCase(commentDisplay)) {
			//保存或更新商家的评价
			orderCommentService.createOrUpdateOrderComment(orderId, userId, 1, merchantId, 
					generalContent, commentDisplay, 5);
			//保存快递员的评价
			orderCommentService.createOrUpdateOrderComment(orderId, userId, 0, courierId, 
					courierContent, commentDisplay, 5);
			
			return j;
			
		} else {
			
			String message = "";
			//保存或更新商家的评价
			boolean f1 = orderCommentService.createOrUpdateOrderComment(orderId, userId, 1, merchantId, 
					generalContent, commentDisplay, generalEvaScore);
			//保存快递员的评价
			boolean f2 = orderCommentService.createOrUpdateOrderComment(orderId, userId, 0, courierId, 
					courierContent, commentDisplay, courierEvaScore);
			
			if (f1 && f2) {
				int score = orderCommentService.orderCommentScore(orderId, userId);
				message = "评论成功,1号外卖送你 "+score+" 积分";
				Map<String, String> pushMap = new HashMap<String, String>();
				pushMap.put("orderId", orderId.toString());
				String title = "您有一条新的评论";
				pushMap.put("title", title);
				pushMap.put("content", title);
				pushMap.put("voiceFile", SoundFile.SOUND_NEW_COMMENT);
				jpushService.push(courierId, pushMap);
				j.setObj(score);
			} else {
				message = "修改评论成功";
			}
			j.setSuccess(true);
			j.setMsg(message);
			return j;
			
		}

	}

	
	/**
	 * 我的-退款-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineRefunds")
	public String mineRefunds(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {
		String requestUrl = ConfigUtil.MINE_REFUNDS_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineRefundsAfterAuth(request,response, page, rows);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-退款
	 * 
	 */
	@RequestMapping(params = "mineRefundsAfterAuth")
	public String mineRefundsAfterAuth(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int rows) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_REFUNDS_URI);
			}
		}
		
		// 我的-查询退款的订单状态列表
		AjaxJson j = queryOrderListByState(OrderStateEnum.REFUND.getOrderStateEn() , page, rows, userId);
		request.setAttribute("omvList", j.getObj());
		return "takeout/mineRefunds";
	}

	/**
	 * 我的-充值-微信授权跳转
	 * 
	 */
	@RequestMapping(params = "mineRecharge")
	public String mineRecharge(HttpServletRequest request,
			HttpServletResponse response) {
		String requestUrl = ConfigUtil.MINE_RECHARGE_URI;
		if (ConfigUtil.isTest || !this.isWeixin(request)
				|| this.getUserId(request, null) != null) {
			return this.mineRechargeAfterAuth(request,response);
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(requestUrl);
		}
	}

	/**
	 * 我的-充值
	 * 
	 */
	@RequestMapping(params = "mineRechargeAfterAuth")
	public String mineRechargeAfterAuth(HttpServletRequest request,
			HttpServletResponse response) {

		Integer userId = this.getUserId(request, null);
		if (userId == null) {
			if (ConfigUtil.isTest || !this.isWeixin(request)) {
				return "takeout/login";
			} else {
				return "redirect:"
						+ AdvancedUtil
								.getWeiXinRedirectUrl(ConfigUtil.MINE_RECHARGE_URI);
			}
		}
		
		// 我的
		// 查询用户
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if(user != null){
			request.setAttribute("balance",StringUtils.defaultString( String.valueOf(user.getMoney()), "0.0"));//查询用户余额
			request.setAttribute("score", StringUtils.defaultString( String.valueOf(user.getScore()), "0"));//查询用户积分
			request.setAttribute("nickname", user.getNickname());
			request.setAttribute("mobile", StringUtils.defaultString( String.valueOf(user.getMobile()), ""));
			request.setAttribute("photourl", user.getPhotoUrl());
		}
		
		return "takeout/mineRecharge";
	}
	
	/**
	 * 我的-待收货-确认收货
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(params = "mineOrderConfirmReceipt")
	@ResponseBody
	public AjaxJson mineOrderConfirmReceipt(HttpServletRequest request, Integer orderId, Integer courierId, Integer merchantId, 
			String generalContent, String courierContent, Integer generalEvaScore, Integer courierEvaScore, String commentDisplay) throws Exception {
		AjaxJson j = new AjaxJson();
		String message = null;
		if(orderId != null){
			boolean flag = orderService.confirmOrder(orderId, getUserId(request, null));
			if(flag) {
				message = "确认收货成功";
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
				//添加系统评价为此订单
				int userId = this.getUserId(request, null);
				orderCommentService.createSystemOrderComment(orderId, userId, 0, courierId);
				orderCommentService.createSystemOrderComment(orderId, userId, 1, merchantId);
				j.setSuccess(true);
				return j;
			}
			
		}
		message = "确认收货失败, 请稍后重试。";
		j.setSuccess(false);
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 查询此手机号是否已经绑定到某个帐号，如果已经绑定，则提示已经绑定到
	 * 
	 */
	@RequestMapping(params = "isMobileBinded")
	@ResponseBody
	public AjaxJson isMobileBinded(@RequestParam String mobile, 
			@RequestParam String userType, 
			HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String message = null;
		if(StringUtils.isBlank(mobile) || mobile.length() != 11){
			j.setSuccess(false);
			message = "手机号不正确。";
			j.setMsg(message);
		}else{
			if(StringUtils.isBlank(userType)){
				userType = "user";
			}
			WUserEntity wuser = wUserService.getUserByMobile(mobile, userType);
			if(wuser == null){
				message = "手机号正确。";	
				j.setSuccess(true);
			}else{
				message = "手机号已经绑定到其它帐号，请换个手机号进行绑定。";
				j.setSuccess(false);
			}
			j.setMsg(message);
		}
		return j;
	}
	
	
	
	/**
	 * 获取当前订单排号,可供第三方调用
	 * @return
	 */
	@RequestMapping(params = "getOrderNum")
	@ResponseBody
	public String getOrderNum() {
		long orderNum = AliOcs.getAndIncrOrderNum();
		logger.info("takeOutController getOrderNum: {}", orderNum);
		return String.valueOf(orderNum);
	}

	/**
	 * 根据商家编号获取订单排号
	 * @param merchantId
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/genOrderNum/{merchantId}.do", method=RequestMethod.POST)
	public String genOrderNum(@PathVariable String merchantId) throws Exception {
		logger.info("takeOutController genOrderNum merchantId: {}", merchantId);
		if(StringUtil.isEmpty(merchantId)){
			logger.warn("genOrderNum merchantId is empty !!!!");
			return "";
		}
		MerchantEntity merchant = merchantService.get(MerchantEntity.class, Integer.parseInt(merchantId));
		if(merchant == null){
			logger.error("genOrderNum merchant:{} not exist !!!!", merchantId);
			throw new RuntimeException("merchant not exist.");
		}
		return AliOcs.genOrderNum(merchantId);
	}
	
	@ResponseBody
	@RequestMapping(value="/openGenOrderSortNum", method=RequestMethod.POST)
	public String openGenOrderSortNum(String key, String mark) throws Exception {
		logger.info("Take out controller gen order number mark : {}", mark);
		if (StringUtil.isEmpty(mark)) {
			logger.warn("Gen order number mark is empty !!!!");
			return "";
		}
		if (!sortKey.containsKey(key.trim())) {
			return "";
		}
		mark = key + mark;
		return AliOcs.genOrderNum(mark);
	}
	
	/**
	 * 获取微信AccessToken
	 * @return
	 */
	@RequestMapping(params = "getAccessToken")
	@ResponseBody
	public String getAccessToken() {
		return AccessTokenContext.getAccessToken();
	}
	
	static int AccessTokenCount = 0;
	@RequestMapping(params = "setAccessToken")
    @ResponseBody
    public String setAccessToken(HttpServletRequest req, String token, String timestamp, String sign) {
	    State check = checkSign(token, timestamp, sign);
	    if(null != check) return check.ret().toString();
	    if(AccessTokenCount > 3) return State.Error.ret("每日最多刷新3次!").toString();
	    AccessTokenCount ++;
        return AccessTokenContext.setAccessToken();
    }
	
	/**
	 * 获取威富通渠道广告素材
	 * @return
	 */
	@RequestMapping(params = "getWftAdv")
	@ResponseBody
	public String getWftAdv() {
		return WftAdvertisementUtils.getAdBody();
	}
	
	@RequestMapping(params = "setJsapiTicket")
    @ResponseBody
    public String setJsapiTicket(HttpServletRequest req, String token, String timestamp, String sign) {
	    State check = checkSign(token, timestamp, sign);
        if(null != check) return check.ret().toString();
        return AccessTokenContext.setJsapiTicket();
    }
	
	/**
	 * 获取微信JsapiTicket
	 * @return
	 */
	@RequestMapping(params = "getJsapiTicket")
	@ResponseBody
	public String getJsapiTicket() {
		return AccessTokenContext.getJsapiTicket();
	}
	
	/**
	 * 乡村鸡扫码取餐
	 * @param request
	 * @param response
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params = "qrCode")
	@ResponseBody
	public AjaxJson qrCode(HttpServletRequest request,HttpServletResponse response,Integer merchantId){
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setMsg("扫码失败,数据错误"+merchantId);
		j.setSuccess(false);

		UserInfo u = getUserInfo(request);
		if(null != u){
			List<Map<String,Object>> orderList = mealService.qrCodeGetDineinOrder(u.getUserId(), merchantId);
			if(orderList.size()<=0){
				j.setStateCode("00");
				j.setMsg("查询后无匹配数据!");
				j.setSuccess(true);
			}else{
				for(Map<String,Object> m:orderList){
					Integer orderId = Integer.parseInt(m.get("order_id").toString());
					mealService.qrCodeUpdateDineinOrder(orderId);
					printService.mealPrint(orderId);
				}
				j.setStateCode("00");
				j.setMsg("扫码成功,更新状态!");
				j.setSuccess(true);
			}
		}else{
			j.setStateCode("01");
			j.setMsg("扫码失败,用户为空!");
			j.setSuccess(false);
		}
		return j;
	}
	

//    public static void main(String[] args) {
//        String timestamp = "";
//        String token = "asdf1235245lkms;dfk";
//        final String pwd = "5736E918F6794070996248BC139B761B";
//        
//        String paramStr = pwd + timestamp;
//        paramStr += "token" + token;
//        System.out.println(paramStr);
//        String md5Param = DigestUtils.md5Hex(paramStr);
//        System.out.println(md5Param);
//    }
    
    /**
     * 校验签名
     * @param req
     * @return
     */
    private State checkSign(String token, String timestamp, String sign) {
        final String pwd = "5736E918F6794070996248BC139B761B";
        String paramStr = pwd + timestamp;
        paramStr += "token" + token;

        String md5Param = DigestUtils.md5Hex(paramStr);
        if (!sign.equals(md5Param.toUpperCase())) return State.SignError;
        return null;
    }
    
    /**
     * 补支付宝支付的订单
     */
    @RequestMapping(value="/reAliPayDoneOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String reAliPayDoneOrder(HttpServletRequest request, HttpServletResponse response, 
                    String orderIds, String payType){
        return rePayDoneOrder(request, response, orderIds, "aliPay");
    }
    
    
    /**
     * 补威付通支付的订单
     */
    @RequestMapping(value="/rePayDoneOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String rePayDoneOrder(HttpServletRequest request, HttpServletResponse response, 
                    String orderIds){
        return rePayDoneOrder(request, response, orderIds, "wftPay");
    }
    
    /**
     * 补单
     * @param request
     * @param response
     * @return
     */
    private String rePayDoneOrder(HttpServletRequest request, HttpServletResponse response, 
                    String orderIds, String payType){
        JSONObject ret = new JSONObject();
        try {
            String[] orderIdStrs = orderIds.split(",");
            for (String orderIdStr : orderIdStrs) {
                try {
                	OrderEntity order = orderService.get(OrderEntity.class, Integer.parseInt(orderIdStr));
                    if("aliPay".equals(payType)){
                        logger.info(">>>>>支付宝补单:{}", orderIdStr);
                        payService.orderAlipayScanDone(order, "2088202410600942");
                    }
                    else if("wftPay".equals(payType)){
                        logger.info(">>>>>威付通补单:{}", orderIdStr);
                        payService.orderPayCallback(order);
                    }
                    else logger.error("补单未找到payType:{}", payType);
                    ret.put(orderIdStr, "success!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    logger.error("订单{}补单异常{}", orderIdStr, e);
                    ret.put(orderIdStr, "error!");
                }
            }
        }
        catch (Exception e) {
            logger.error("补单异常, 订单:{} 异常:{}", orderIds, e);
            ret.put("error", "补单异常!" + orderIds);
        }
        return ret.toJSONString();
    }
    
    @Autowired
	private AgentIncomeTimerDao agentIncomeTimerDao;
    
    @RequestMapping(value="/orderAgentStati", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String orderAgentStati(String date){
    	JSONObject ret = new JSONObject();
    	try {
    		agentIncomeTimerDao.orderIncomeDayly(date);
    	}
        catch (Exception e) {
            logger.error("补单异常, 日期:{} 异常:{}", date, e);
            ret.put("error", "补单异常!" + date);
        }
        return ret.toJSONString();
    }
}