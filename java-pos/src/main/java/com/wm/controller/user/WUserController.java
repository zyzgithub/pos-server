package com.wm.controller.user;

import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import jeecg.system.service.SystemService;
import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.dao.impl.CommonDao;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.VO.UploadFileVO;
import com.courier_mana.common.Constants;
import com.sms.service.SmsByBusinessServiceI;
import com.validate.util.RandomValidateUtil;
import com.wm.controller.courier.dto.UserRegisterDTO;
import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.entity.courierapply.CourierApplyEntity;
import com.wm.entity.courierauditlog.CourierAudltLogEntity;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.recharge.RechargeEntity;
import com.wm.entity.transfers.TransfersEntity;
import com.wm.entity.user.NewAndOldUserVo;
import com.wm.entity.user.UserBudget;
import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;
import com.wm.service.courier.CourierRegisterServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.deduct.DeductServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
//import com.wm.service.pay.PayServiceI;
import com.wm.service.recharge.RechargeServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.transfers.TransfersServiceI;
//import com.wm.service.user.UserloginServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;
import com.wm.util.AliOcs;
import com.wm.util.IPUtil;
import com.wp.ConfigUtil;

/**
 * @Title: Controller
 * @Description: user
 * @author 胡争
 * @date 2015-01-07 09:12:24
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("ci/wUserController")
public class WUserController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(WUserController.class);

	@Autowired
	private WUserServiceI wUserService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private SystemconfigServiceI systemconfigService;

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private WithdrawalsServiceI withdrawalsService;

	@Autowired
	private TransfersServiceI transfersService;

	@Autowired
	private CourierServiceI courierService;
	
	@Autowired
	private RechargeServiceI rechargeService;
	
	@Autowired
	private DeductServiceI deductService;
	
	@Autowired
	private DeductLogServiceI deductLogService;
	
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	@Autowired
	private CourierRegisterServiceI courierRegisterService;
	
	@Autowired
	private MerchantServiceI merchantService;
	
	@Resource(name="smsForResetService")
	private SmsByBusinessServiceI smsForResetService;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 上传用户头像
	 * 
	 * @param request
	 * @param response
	 * @param userid
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(params = "uploadUserPhoto")
	@ResponseBody
	public AjaxJson uploadUserPhoto(HttpServletRequest request, HttpServletResponse response, int userid) {
		AjaxJson j = new AjaxJson();
		List<UploadFileVO> files = null;
		String prefixPath = systemconfigService.getValByCode("user_url");
		String filesString = request.getParameter("files");
		if (StringUtil.isNotEmpty(filesString)) {
			JSONArray jsons = JSONArray.fromObject(filesString);
			files = jsons.toList(jsons, UploadFileVO.class);
			UploadFileVO file = files.get(0);
			// 上传头像操作
			String relPathString = wUserService.uploadUserPhoto(userid, file, CommonDao.uploadPath);
			j.setObj(prefixPath + relPathString);
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("操作失败");
		}
		return j;
	}

	/**
	 * 授权注册
	 * 
	 * @param request
	 * @param username
	 * @param sns
	 * @param gender
	 * @param password
	 * @return
	 */
	@RequestMapping(params = "AuthRegister")
	@ResponseBody
	public AjaxJson AuthRegister(HttpServletRequest request, String username,
			String sns, String gender, String password) {
		AjaxJson j = new AjaxJson();

		j = wUserService.AuthRegister(request, username, sns, gender, password);

		return j;
	}

	/**
	 * 获取生成用户名
	 * @param ids
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping(params = "getUserAccount")
	@ResponseBody
	public AjaxJson getUserAccount(String ids, HttpServletRequest request, String params) {
		AjaxJson j = new AjaxJson();
		String account = AccountGenerator.getAccount();
		boolean userIsExist = true;
		long countUser = 0;
		while (userIsExist) {
			countUser = systemService.getCountForJdbcParam("select count(1) count from user where username=?", new Object[]{account});
			if (countUser == 0) {
				userIsExist = false;
			} else {
				account = AccountGenerator.getAccount();
			}
		}
		j.setObj(account);
		j.setMsg("获取账号成功");
		return j;
	}

	/**
	 * 商家或快递员登录
	 * @param username 帐号
	 * @param password	密码
	 * @param type： 用户类型0-user 1-merchant 2-courier 3-warehouse 5-supplier 6-agent 
	 * @param channel 来源（Ios, Android） 快递员管理员固定来源（courierMan）
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "userLogin")
	@ResponseBody
	public AjaxJson userLogin(String username, String password, String type, String channel, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		logger.info("username:" + username + ",password:" + password + ",type:" + type);
		if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password)){
			j.setStateCode("01");
			j.setMsg("用户名或密码为空");
			j.setSuccess(false);
			logger.error("用户名或密码为空,username:" + username + ",password:" + password);
			return j;
		}
		//快递员用中文名和密码登录验证
		Long userCount = wUserService.getCountByNameAndPwd(username, password);
		if(userCount>1){
			j.setStateCode("01");
			j.setMsg("用户名与其他用户存在重复情况，请用手机号登录");
			j.setSuccess(false);
			logger.error("用户名与其他用户存在重复情况,username:" + username + ",type:" + type);
			return j;
		}
		WUserEntity user = null;
		MerchantEntity merchant = null;
		user = wUserService.userLogin(username, password, type);

		Map<String, Object> objs = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (user != null) {
			if(1 == user.getIsDelete().intValue()){
				j.setStateCode("01");
				j.setMsg("你的账号已被禁用，有疑问请联系客服！");
				j.setSuccess(false);
				return j;				
			}
			//快递员管理端登录，需要快递员以上级别才有权限登录
			if ("courierMan".equals(channel)) {
				Long count = wUserService.getCourierPosition(user.getId());
				if (count == 0) {
					j.setStateCode("01");
					j.setMsg("用户没有登录权限");
					j.setSuccess(false);
					return j;
				}
			}
			objs = new HashMap<String, Object>();

			if (user.getUserType() != null && "merchant".equals(user.getUserType())) {
				merchant = systemService.findUniqueByProperty(MerchantEntity.class, "wuser", user);
				if(merchant != null){
					MerchantInfoEntity merchantInfoEntity = systemService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchant.getId());
					// 系统分切,终止登录服务
					if (merchantInfoEntity.getPlatformType() == 2){
						j.setSuccess(false);
						j.setMsg("系统升级,服务已停止,请下载新版APP !");
						return j;
					}

                    Long l2 = orderService.getCountForJdbcParam("select count(*) from `post_buyout` where merchant_id = ?  limit 1  ", Arrays.asList(merchant.getId()).toArray());
                    objs.put("posBuyed", l2 !=null && l2 > 0);

                    objs.put("merchantId", merchant.getId());
					objs.put("merchantName", merchant.getTitle());
					objs.put("merchantSource",merchantService.getMerchantSource(merchant.getId()));
					objs.put("shopFromType", merchantInfoEntity.getShopFromType());
					//合作供应商1012,平台供应商1013
					int storeType = merchantService.getStoreType(merchant.getId());
					objs.put("storeType", storeType);
					if(storeType==3){
						if(merchant.getCategory().getId()==1012){
							objs.put("supplyUserType", 1012);
						}else if(merchant.getCategory().getId()==1013){
							objs.put("supplyUserType", 1013);
						}
					}
					
					objs.put("printCode", merchant.getPrintCode());
					//判断商家是否自己开店
					if(user.getId().equals(user.getCreator())){
						objs.put("isCreator", "Y");
					}else{
						objs.put("isCreator", "N");
					}
				} else {
					j.setMsg("商家账号不存在");
					j.setSuccess(false);
					return j;
				}
			}
			if(user.getUserType() != null && "courier".equals(user.getUserType())){
				List<CourierOrgVo> orgs = courierService.queryOrgByUserId(user.getId());
				if(orgs != null && orgs.size() > 0){
					CourierOrgVo org = orgs.get(0);
					objs.put("orgId", org.getOrgId() + "");
				} else {
					objs.put("orgId", "0");
				}
				/*
				// 查快递员管理版的权限数据
				List<Map<String, Object>> managerFunctionList = courierService.queryCourierManagerFunction(user.getId());
				if(managerFunctionList != null && managerFunctionList.size() > 0){
					objs.put("courierManagerFunctionList", managerFunctionList);
				}
				 */
				Integer state = wUserService.getUserState(user.getId());
				objs.put("userState", state);
				CourierApplyEntity courierApplyEntity  = systemService.findUniqueByProperty(CourierApplyEntity.class, "userId", user.getId());
				// 如果快递员被审核驳回，要求重填资料，返回该快递员原先填的资料
				if (state == 4) {
					Map<String, Object> applyMap = courierRegisterService.getCourierApplyInfo(user.getId());
					if(applyMap != null){
						objs.putAll(applyMap);
					}
				}
				// 返回快递员的类型
				Integer courierType = courierInfoService.getCourierTypeByUserId(user.getId());
				if(courierType != null){

					// 停用九洲快递员
					if (courierType == 2){
						j.setSuccess(false);
						j.setMsg("系统升级,服务已停止,请下载新版APP !");
						return j;
					}

					objs.put("courierType", courierType);
				} else if(courierApplyEntity != null && courierApplyEntity.getCourierType() !=null) {
					objs.put("courierType", courierApplyEntity.getCourierType());
				} else {
					objs.put("courierType", "");
				}
			}
			objs.put("userId", user.getId());
			objs.put("username", StringUtils.isBlank(user.getUsername()) ? "" : user.getUsername());
			objs.put("nickname", StringUtils.isBlank(user.getNickname()) ? "" :user.getNickname());
			objs.put("gender", user.getGender());
			objs.put("mobile", user.getMobile());
			objs.put("money", user.getMoney());
			objs.put("score", user.getScore());
			objs.put("image", StringUtils.isBlank(user.getPhotoUrl()) ? "" : user.getPhotoUrl());
			objs.put("userType", type);
			

			// 获取用户的职位 add by Simon
			Map<String, Object> userPosition = wUserService.getUserPosition(user.getId());
			if (userPosition != null) {
				objs.put("postionname", userPosition.get("name"));
			} else {
				objs.put("postionname", "");
			}
			//获取用户所属区域，如天河区
			List<CourierOrgVo> queryOrgByUserIdList = courierService.queryOrgByUserId(user.getId());
			int positionId = 0;
			if(queryOrgByUserIdList != null && queryOrgByUserIdList.size() > 0) {
				CourierOrgVo courierOrgVo = queryOrgByUserIdList.get(0);
				positionId = courierOrgVo.getPositionId();
				objs.put("courierOrgVo", courierOrgVo);
			}
			
			// 获取快递员信息
			Map<String, Object> courierInfo = wUserService.getCourierInfo(user.getId());
			int userLevel = 1;
			if (courierInfo != null) {
//				设置快递员工号到返回对象 
				objs.put("jobId", courierInfo.get("job_id"));
				
				//获取用户等级,如1级，2级
				Object salaryObj = courierInfo.get("salary");
				int salary = 0;
				if(salaryObj != null){
					salary = Integer.parseInt(String.valueOf(salaryObj));
					userLevel = courierService.getCourierLevelBySalaryPosition(salary, positionId);
				}
			} else {
				objs.put("jobId", "");
			}
			objs.put("userLevel", userLevel);
			
			
			list.add(objs);

			if (channel == null || "".equals(channel)) {// 判断用户登录类型是否为空
				channel = "unknow";
			}

			// 添加用户登录记录
			long time = System.currentTimeMillis();
			UserloginEntity userLogin = new UserloginEntity();
			userLogin.setChannel(channel);
			userLogin.setIp(request.getRemoteAddr());
			userLogin.setLoginTime((int) time / 1000);
			userLogin.setWuser(user);
			systemconfigService.save(userLogin);
			j.setMsg("登录成功");
			if(type.equals("2") && objs.get("userState").equals(4)){
				j.setMsg("登录成功,请重新填写申请资料");
			}
			j.setSuccess(true);
			j.setStateCode("00");
			logger.info("登录成功username:{},type:{}", username, type);
		} else {
			if(Integer.parseInt(type) == 2 && courierRegisterService.isApproved(username, password) == true){
				j.setStateCode("02");
				j.setMsg("您的申请未通过审核");
				j.setSuccess(false);
				logger.error("快递员申请没有通过审核,username:" + username + ",type:" + type);
				return j;
			}
			j.setStateCode("01");
			j.setMsg("用户名或密码错误");
			j.setSuccess(false);
			logger.warn("用户名或密码错误,username:" + username + ",type:" + type);
		}
		j.setObj(list);

		return j;
	}

	@RequestMapping(params = "authLogin")
	@ResponseBody
	public AjaxJson authLogin(String sns) {
		AjaxJson j = new AjaxJson();
		WUserEntity user = null;
		MerchantEntity merchant = null;
		user = wUserService.authLogin(sns);

		Map<String, Object> objs = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (user != null) {
			objs = new HashMap<String, Object>();

			if (user.getUserType() != null && "merchant".equals(user.getUserType())) {
				merchant = systemService.findUniqueByProperty(MerchantEntity.class, "wuser", user);
				objs.put("merchantId", merchant.getId());
			}
			objs.put("userId", user.getId());
			objs.put("username", user.getUsername());
			objs.put("nickname", user.getNickname());
			objs.put("gender", user.getGender());
			objs.put("mobile", user.getMobile());
			objs.put("money", user.getMoney());
			objs.put("score", user.getScore());
			list.add(objs);

			j.setMsg("登录成功");
			j.setSuccess(true);
		} else {
			j.setMsg("sns错误");
			j.setSuccess(false);
		}
		j.setObj(list);

		return j;
	}

	@RequestMapping(params = "updateUsername")
	@ResponseBody
	public AjaxJson updateUsername(int userId, String username) {
		AjaxJson j = new AjaxJson();
		boolean flag = wUserService.updateUsername(userId, username);
		if (flag) {
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
			j.setMsg("用户名已存在或不合法");
			j.setStateCode("01");
		}
		return j;
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param username
	 * @param mobile
	 * @param password
	 * @return
	 */
	@RequestMapping(params = "userRegister")
	@ResponseBody
	public AjaxJson userRegister(HttpServletRequest request, String username,
			String mobile, String password) {
		AjaxJson j = new AjaxJson();
		j = wUserService.userRegister(request, username, mobile, password);
		return j;
	}

	/**
	 * 商家登录
	 * @return
	 */
	@RequestMapping(params = "merchantLogin")
	@ResponseBody
	public AjaxJson merchantLogin(String username) {
		AjaxJson j = new AjaxJson();
		WUserEntity user = null;
		MerchantEntity merchant = null;
		user = wUserService.merchantLogin(username);

		Map<String, Object> objs = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (user != null) {
			objs = new HashMap<String, Object>();

			if (user.getUserType() != null && "merchant".equals(user.getUserType())) {
				merchant = systemService.findUniqueByProperty(MerchantEntity.class, "wuser", user);
				objs.put("merchantId", merchant.getId());
			}
			objs.put("userId", user.getId());
			objs.put("username", user.getUsername());
			objs.put("nickname", user.getNickname());
			objs.put("gender", user.getGender());
			objs.put("mobile", user.getMobile());
			objs.put("money", user.getMoney());
			objs.put("score", user.getScore());
			list.add(objs);

			j.setMsg("登录成功");
			j.setSuccess(true);
			j.setStateCode("00");
		} else {
			j.setStateCode("01");
			j.setMsg("账号不正确，账号不存在");
			j.setSuccess(false);
		}
		j.setObj(list);

		return j;
	}

	/**
	 * 我的余额、累计收入、累计提现
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "getBalanceNew")
	@ResponseBody
	public AjaxJson getBalanceNew(Integer userId) {		 
		AjaxJson j = new AjaxJson();
		Map<String, Object> map = new HashMap<String, Object>();
		try {				
			if(userId == null){
				j.setMsg("参数错误");
				j.setSuccess(false);
			}
			else{
				//余额
				Double balance = wUserService.getBalance(userId);
				//累计提现收入
				Double withdrawal = wUserService.getSumWithdrawals(userId);
				//累计收入
				Double deduct = wUserService.getSumDeduct(userId);
				
				map.put("balance", balance);
				map.put("withdrawal", withdrawal);
				map.put("deduct", deduct);
				j.setObj(map);
				j.setMsg("操作成功");
				j.setSuccess(true);		
			}
		} catch (Exception e) {			
			e.printStackTrace();
			j.setMsg("查询出现错误");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 我的余额
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "getBalance")
	@ResponseBody
	public AjaxJson getBalance(Integer userId) {
		AjaxJson j = new AjaxJson();
		Double balance = wUserService.getBalance(userId);
		j.setObj(balance);
		j.setSuccess(true);
		return j;
	}
	
	

	/**
	 * 获取快递员成绩,“总”的是历史，“我的”的当天
	 * 
	 * @param courierId
	 * @return
	 */
	@RequestMapping(params = "getCourierReport")
	@ResponseBody
	public AjaxJson getCourierReport(Integer courierId) {
		AjaxJson j = new AjaxJson();
		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("totalRank", "0");// 总排名
		retMap.put("totalOrders", "0");// 总单数
		retMap.put("myRank", "0");// 我的排名
		retMap.put("myOrders", "0");// 我的单数
		retMap.put("myTrans", "0");// 我的派单
		retMap.put("myIncome", "0");// 我昨天的
		

		try {
			// 总排名、总单数,统计一个月
			String startDate = DateTime.now().minusMonths(1).toString("yyyy-MM-dd");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Integer totalOrders = wUserService.getMyTotalOrders(courierId, startDate, null);
			Integer totalRank = wUserService.getRank(courierId, startDate, null, Constants.CITY_LEVEL) + 1;		
			retMap.put("totalRank", totalRank.toString());
			retMap.put("totalOrders", totalOrders.toString());

			// 统计时间设置为当天
			startDate = DateTime.now().toString("yyyy-MM-dd");
			String endDate =  DateTime.now().plusDays(1).toString("yyyy-MM-dd");
			// 我的排名、我的单数
			Integer myOrders = wUserService.getMyTotalOrders(courierId, startDate, endDate);
			Integer myRank = wUserService.getRank(courierId, startDate, endDate, Constants.CITY_LEVEL) + 1;		
			retMap.put("myRank", myRank.toString());
			retMap.put("myOrders", myOrders.toString());
			// 我的派单
			list = orderService.getTransOrderByCourierId(courierId, startDate, endDate);
			if (list != null && list.size() > 0) {
				retMap.put("myTrans", list.size() + "");
			}

			// 我的收入,统计时间设置为昨天		
			String date = DateTime.now().minusDays(1).toString("yyyy-MM-dd");		
			List<Map<String, Object>> incomeOne = deductLogService.getYesterdayIncome(courierId, date);
			List<Map<String, Object>> incomeTwo = deductLogService.getYesterday(courierId, date);
			
			Double orderDeduct = (Double) incomeOne.get(0).get("orderDeduct");
			Double deduct = (Double) incomeTwo.get(0).get("deduct");
			Double reward = ((Double) incomeOne.get(0).get("reward")*100 + (Double) incomeTwo.get(0).get("reward")*100)/100;
			Double total = (orderDeduct*100 + deduct*100 +reward*100)/100;		
			retMap.put("myIncome", total.toString());
			j.setObj(retMap);
			j.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("获取快递员排名，收入，订单数失败");;
		}
		return j;
	}	
	
	/**
	 * 我的排名(本片区快递员排名)
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @param start 分页起始行
	 * @param num 分页行数
	 * @return
	 */
	@RequestMapping(params = "getRand")
	@ResponseBody
	public AjaxJson getRand(Integer courierId, String startDate,
			String endDate, String start, String num) {
		AjaxJson j = new AjaxJson();
		try {
			List<Map<String, Object>> list = wUserService.getCourierRankNew(courierId, startDate, endDate, Constants.CITY_LEVEL, start, num);
			j.setObj(list);
			j.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("获取快递员排名列表失败");
		}
		return j;
	}

	/**
	 * 收支记录
	 * @return
	 */
	@RequestMapping(params = "getBudget")
	@ResponseBody
	public AjaxJson getBudget(Integer userId) {
		AjaxJson j = new AjaxJson();
		List<UserBudget> retList = new ArrayList<UserBudget>();

		// 充值记录
		List<RechargeEntity> rechargeList = rechargeService.findByProperty(RechargeEntity.class, "userId", userId);
		for (RechargeEntity recharge : rechargeList) {
			UserBudget ub = new UserBudget();
			ub.setBudgetType("充值");
			if("Y".equals(recharge.getStatus())){
				ub.setState("成功");
			} else {
				ub.setState("失败");
			}
			ub.setStateTime(new DateTime(recharge.getCreateTime().longValue()*1000).toString("yyyy-MM-dd HH:mm:ss"));
			ub.setMoney(Double.parseDouble(recharge.getTotalFee()/100.0+""));
			retList.add(ub);
		}

		// 提现记录
		List<WithdrawalsEntity> withdList = withdrawalsService.findWithdrLog(userId);
		for (WithdrawalsEntity withd : withdList) {
			UserBudget ub = new UserBudget();
			ub.setBudgetType("提现");
			String state = withd.getState();
			if ("apply".equals(state)) {
				state = "申请中";
				ub.setStateTime(new DateTime(withd.getSubmitTime().longValue()*1000).toString("yyyy-MM-dd HH:mm:ss"));
			} else if ("done".equals(state)) {
				state = "已完成";
				ub.setStateTime(new DateTime(withd.getCompleteTime().longValue()*1000).toString("yyyy-MM-dd HH:mm:ss"));
			} else {
				state = "已取消";
				ub.setStateTime(new DateTime(withd.getCancelTime().longValue()*1000).toString("yyyy-MM-dd HH:mm:ss"));
			}
			ub.setState(state);
			ub.setMoney(withd.getMoney());
			retList.add(ub);
		}

		// 代付记录
		List<TransfersEntity> transList = transfersService.findTransLog(userId);
		for (TransfersEntity trans : transList) {
			UserBudget ub = new UserBudget();
			ub.setBudgetType("商品代付");
			ub.setState("成功");
			ub.setStateTime(new DateTime(trans.getCreateTime().longValue()*1000).toString("yyyy-MM-dd HH:mm:ss"));
			ub.setMoney(trans.getMoney());
			ub.setOrderNum(trans.getOrderNum());
			retList.add(ub);
		}
		
		// 提成记录
		List<DeductLogEntity> deductList = deductService.findByProperty(DeductLogEntity.class, "courierId", userId);
		for (DeductLogEntity deduct : deductList) {
			if(deduct.getDeduct() > 0.0){
				UserBudget ub = new UserBudget();
				ub.setBudgetType("提成记录");
				ub.setState("成功");
				ub.setStateTime(new DateTime(deduct.getAccountDate().longValue()*1000).toString("yyyy-MM-dd"));
				ub.setMoney(deduct.getTotalDeduct());
				retList.add(ub);
			}
		}

		Collections.sort(retList);// 按时间排序
		j.setObj(retList);
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 修改用户基本信息
	 * 
	 * @param userId
	 * @param nickname
	 * @param photoUrl
	 * @param mobile
	 * @return
	 */
	@RequestMapping(params = "updateUserInfo")
	@ResponseBody
	public AjaxJson updateUserInfo(int userId, String nickname,
			String photoUrl, String mobile) {
		AjaxJson j = new AjaxJson();
		boolean flag = wUserService.updateUserInfo(userId, nickname, photoUrl, mobile);
		if (flag) {
			j.setSuccess(true);
			j.setMsg("修改用户基本信息成功");
			j.setStateCode("00");
		} else {
			j.setSuccess(false);
			j.setMsg("修改用户基本信息失败");
			j.setStateCode("01");
		}
		return j;
	}

	/**
	 * 修改用户头像
	 * 
	 * @param photoUrl
	 * @return
	 */
	@RequestMapping(params = "updatePhotoUrl")
	@ResponseBody
	public AjaxJson updatePhotoUrl(int userId, String photoUrl) {
		AjaxJson j = new AjaxJson();
		boolean flag = wUserService.updatePhotoUrl(userId, photoUrl);
		if (flag) {
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
			j.setMsg("修改用户头像失败");
			j.setStateCode("01");
		}
		return j;
	}

	/**
	 * 用户修改密码
	 * 
	 * @param userId
	 * @param password
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(params = "updatePassword")
	@ResponseBody
	public AjaxJson updatePassword(int userId, String password,
			String newPassword) {
		AjaxJson j = new AjaxJson();
		boolean flag = wUserService.updatePassword(userId, password,
				newPassword);
		if (flag) {
			j.setSuccess(true);
		} else {
			j.setSuccess(false);
			j.setMsg("修改密码失败");
			j.setStateCode("01");
		}
		return j;
	}

	/**
	 * 用户重置密码
	 * @param phone
	 * @param validateCodeStr
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(params = "resetPassword")
	@ResponseBody
	public AjaxJson resetPassword(HttpServletRequest request, String phone,
			String validateCodeStr, String newPassword) {
		AjaxJson j = new AjaxJson();
		validateCodeStr = phone + validateCodeStr;
		WUserEntity user = wUserService.getUserByMobile(phone, "courier");
		
		
		if (user != null) {
			
			String validateCode = AliOcs.get(smsForResetService.getBusinessCode() + "_" + phone);
			if(validateCode == null){
				j.setMsg("验证码已过期,请重新获取");
				j.setStateCode("01");
				j.setSuccess(false);
			}
			else if (!validateCodeStr.toUpperCase().equals(validateCode)){
				j.setMsg("验证码错误");
				j.setSuccess(false);
				j.setStateCode("01");
			}
			else {
				boolean flag = wUserService.resetPassword(user.getId(),newPassword);
				if (flag) {
					j.setMsg("重置密码成功");
					j.setSuccess(true);
					j.setStateCode("00");
				} 
				else {
					j.setSuccess(false);
					j.setMsg("重置密码失败");
					j.setStateCode("01");
				}
			}
		}
		else {
			j.setSuccess(false);
			j.setMsg("用户为空");
			j.setStateCode("01");
		}
				
		return j;
	}
	
	/**
	 * 商家设置支付密码
	 * @param request
	 * @param phone 手机
	 * @param validateCode 验证码
	 * @param password 登陆密码(修改)
	 * @param payPassword 支付密码 (重置)
	 * @param confirmPassword 确认支付密码
	 * @param reset  是否重置支付密码 ：默认修改支付密码，"1"重置支付密码
	 * @return
	 */
	@RequestMapping(params = "merchantSetPaypassword")
	@ResponseBody
	public AjaxJson merchantSetPaypassword(HttpServletRequest request, Integer merchantId,String phone,String validateCode,String password, String payPassword,String confirmPassword,String reset) {
		AjaxJson json = new AjaxJson();
		logger.info("商家设置支付密码，merchantId:{},phone:{},validateCode:{},password:{},payPassword:{},confirmPassword:{},reset:{}",
				new Object[]{merchantId,phone,validateCode,password,payPassword,confirmPassword,reset});
		if (merchantId==null || StringUtil.isEmpty(phone)||StringUtil.isEmpty(validateCode)||StringUtil.isEmpty(password)||StringUtil.isEmpty(payPassword)||StringUtil.isEmpty(confirmPassword)) {
			json.setMsg("请检查必填项");
			json.setStateCode("01");
			json.setSuccess(false);
			return json;
		}
		
		MerchantEntity merchant = wUserService.get(MerchantEntity.class, merchantId);
		if (merchant!=null) {
			 Integer userId = merchant.getWuser().getId();
			 WUserEntity user = wUserService.get(WUserEntity.class, userId);
			if (user==null) {
				json.setMsg("会员不存在");
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			if (!user.getMobile().equals(phone)) {
				json.setMsg("手机号码不正确");
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			
			String session=null;
			boolean isMemcached=true;
			try {
				//业务更改验证码缓存键用手机号，时间1分钟
				 session = AliOcs.get(phone);
			} catch (Exception e) {
				if(request.getSession()!=null){
					 session = request.getSession().getAttribute(phone).toString();
					 isMemcached=false;
				}
			}
			if (StringUtil.isEmpty(session)){
				json.setMsg("验证码已过期,请重新获取");
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			if (!validateCode.equals(session)) {
				json.setMsg("验证码不正确");
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			
			
			//是否重置验证码： 默认修改验证码， 1重置验证码
			if ("1".equals(reset)) { 
				//重置验证码判断旧支付密码
				Map<String, Object> map = wUserService.findOneForJdbc("SELECT password FROM `user` WHERE id=?", user.getId());
				String userPassword = map.get("password").toString();
				logger.info("用户重置支付密码，password:{},user.getPassword:{}", new Object[] {password,userPassword});
				if (!password.equals(userPassword)) {
					json.setMsg("登陆密码不正确");
					json.setStateCode("01");
					json.setSuccess(false);
					return json;
				}
			}else {
				//修改密码
				//原支付密码
				Map<String, Object> map = wUserService.findOneForJdbc("SELECT pay_password FROM `user` WHERE id=?", user.getId());
				String userPayPassword = map.get("pay_password").toString();
				logger.info("用户修改支付密码，password:{},user.getPayPassword:{}", new Object[] {password,userPayPassword});
				if (!password.equals(userPayPassword)) {
					json.setMsg("旧支付密码不正确");
					json.setStateCode("01");
					json.setSuccess(false);
					return json;
				}
			}
			if (!payPassword.equals(confirmPassword)) {
				json.setMsg("确认密码不一致");
				json.setStateCode("01");
				json.setSuccess(false);
				return json;
			}
			
//			user.setPayPassword(payPassword);
//			wUserService.saveOrUpdate(user);
			wUserService.executeSql("UPDATE `user` SET pay_password=? WHERE id=?", payPassword, user.getId());
			json.setMsg("密码设置成功");
			json.setStateCode("00");
			json.setSuccess(true);

			if (isMemcached) {
				AliOcs.remove(phone);
			}else {
				request.getSession().removeAttribute(phone);
			}
			
		}else {
			json.setMsg("商家不存在");
			json.setStateCode("01");
			json.setSuccess(false);
		}
		
		return json;
	}

	

	/**
	 * 校验手机验证码并检查手机号是否注册
	 * @param mobile 手机号
	 * @param userType 用户类型：'courier''快递员,''merchant''商家,''user''普通用户
	 * @param validateCode 手机验证码
	 * @return 如果已经绑定，则提示已经绑定到
	 */
	@RequestMapping(params = "validCodeAndIsRegister")
	@ResponseBody
	public AjaxJson validCodeAndIsRegister(@RequestParam String mobile, 
			@RequestParam String validateCode, 
			@RequestParam String userType, HttpServletRequest request) {
		
		String msg = "";
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setSuccess(false);
		j.setMsg("验证码已过期,请重新获取");

		if(StringUtils.isBlank(mobile) || mobile.length() != 11){
			j.setSuccess(false);
			msg = "手机号不正确。";
			j.setMsg(msg);
			return j;
		}
		
		HttpSession session = request.getSession();

		// 测试环境，不需要短信验证码
		if(ConfigUtil.isTest){
			msg = "测试模式，不需要短信验证码";	
			j.setSuccess(true);
			j.setStateCode("00");
			if(session != null){
				session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
			}
			return j;
		}
		
		if(StringUtils.isBlank(validateCode)){
			j.setSuccess(false);
			msg = "验证码不能为空。";
			j.setMsg(msg);
			return j;
		}
		if(session != null){
			String validateCodeStr = StringUtils.trim(mobile) + StringUtils.trim(validateCode);
			String seesionValidateCode = (String) session.getAttribute(RandomValidateUtil.RANDOMCODEKEY);
			logger.info("seesionValidateCode:", seesionValidateCode);
			if (seesionValidateCode==null) {
				j.setMsg("验证码已过期,请重新获取");
				j.setStateCode("01");
				j.setSuccess(false);
				return j;
			}else if(!validateCodeStr.toUpperCase().equals(seesionValidateCode)){
				j.setMsg("验证码错误");
				j.setSuccess(false);
				j.setStateCode("01");
				return j;
			}
			
			if(StringUtils.isBlank(userType)){
				userType = "user";
			}
			WUserEntity wuser = wUserService.getUserByMobile(mobile, userType);
			if(wuser == null){
				msg = "验证码正确,手机号正确,验证通过。";	
				j.setSuccess(true);
				j.setStateCode("00");
				session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
			}else{
				msg = "手机号已经绑定到其它帐号，请换个手机号进行绑定。";
				j.setSuccess(false);
			}
			j.setMsg(msg);
		}
		
		return j;
	}
	
	/**
	 * 校验身份证是否已经注册，默认为快递员
	 * @param idcard 身份证号
	 * @param userType 'courier''快递员,''merchant''商家,''user''普通用户
	 * @return
	 */
	@RequestMapping(params = "isIdCardValid")
	@ResponseBody
	public AjaxJson isIdCardValid(@RequestParam String idcard, 
			@RequestParam String userType) {
		AjaxJson j = new AjaxJson();
		String message = "身份证不正确。";
		j.setSuccess(false);
		Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|[Xx])$");
		if(StringUtils.isBlank(idcard) || !p.matcher(idcard).matches()){
			message = "身份证不正确。";
		}else{
			if(StringUtils.isBlank(userType)){
				userType = "courier";
			}
			boolean valid= wUserService.checkIdCard(idcard);
			if(valid){
				message = "身份证可用。";	
				j.setSuccess(true);
			}else{
				message = "身份证已经使用过，请解绑以前的然后重新注册。";
			}
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 快递员注册
	 * @param userRegister 注册信息
	 * @return
	 */
	@RequestMapping(params = "courierRegister", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson courierRegister(@Valid UserRegisterDTO userRegister, 
			BindingResult result, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setMsg("保存失败");
		if(!result.hasErrors()) {
			CourierApplyEntity entity = new CourierApplyEntity();
			entity.setBankId(userRegister.getBankId());
			entity.setCheckStatus(userRegister.getCheckStatus());
			entity.setCreateTime(userRegister.getCreateTime());
			entity.setCardNo(userRegister.getCardNo());
			entity.setGender(userRegister.getGender());
			entity.setHoldIdCardBackImgUrl(userRegister.getHoldIdCardBackImgUrl());
			entity.setHoldIdCardFrontImgUrl(userRegister.getHoldIdCardFrontImgUrl());
			entity.setIdCard(userRegister.getIdCard());
			entity.setIdCardBackImgUrl(userRegister.getIdCardBackImgUrl());
			entity.setIdCardFrontImgUrl(userRegister.getIdCardFrontImgUrl());
			
			entity.setIp(IPUtil.getRemoteIp(request));
			entity.setMobile(userRegister.getMobile());
			entity.setPassword(userRegister.getPassword());
			entity.setSourceBank(userRegister.getSourceBank());
			entity.setUsername(userRegister.getUsername());
			entity.setUserType(userRegister.getUserType());
			entity.setExpectedDistArea(userRegister.getExpectedDistArea());
			entity.setBankCardFrontImgUrl(userRegister.getBankCardFrontImgUrl());
			systemService.save(entity);
			
			//加入记录到申请评估记录表
			CourierAudltLogEntity audltLogEntity = new CourierAudltLogEntity();
			audltLogEntity.setCourierId(0);
			audltLogEntity.setAppyId(entity.getId());
			audltLogEntity.setAuditor(0);
			audltLogEntity.setAuditResult(0);
			audltLogEntity.setCreateTime(entity.getCreateTime());
			audltLogEntity.setUpdateTime(0);
			audltLogEntity.setApplyType(1);//'申请类型，0=管理员后台添加，1=app申请',
			systemService.save(audltLogEntity);
			
			j.setSuccess(true);
			j.setMsg("保存成功,请等待审核。");
		}else{
			FieldError fieldError = result.getFieldErrors().get(0);
			j.setMsg("参数有错，请检查更新为正确的参数. " + fieldError.getField() + " 不能为 " + fieldError.getRejectedValue());
		}
		return j;
	}
	

	/**
	 * 快递员注册状态查询
	 * @param mobile 手机号
	 * @param userType 'courier''快递员,''merchant''商家,''user''普通用户
	 * @return audit_result 审核状态，0=审核中，1=审核通过，2=审核失败
	 */
	@RequestMapping(params = "registerState", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson registerState(String mobile, String userType) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		j.setMsg("无此手机号的注册申请记录");
		if(StringUtils.isBlank(mobile)){
			j.setMsg("手机号不能为空");
			return j;
		}
		if(StringUtils.isBlank(userType)){
			userType = "courier";
		}
		Map<String, Object> registerState = courierService.registerState(mobile, userType);
		if(registerState == null){
			j.setMsg("查询失败，此手机号没有注册过快递员。");
		}else{
			j.setSuccess(true);
			j.setMsg("查询成功");
			j.setObj(registerState);
		}
		return j;
	}
	
	/**
	 * 新老用户统计
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(params="statisticsNewAndOldUser")
	@ResponseBody
	public AjaxJson statisticsNewAndOldUser(String startTime, String endTime){
		AjaxJson j=new AjaxJson();
		NewAndOldUserVo  newAndOldUserVo = wUserService.statisticsNewAndOldUser(startTime, endTime);
		j.setObj(newAndOldUserVo);
		j.setMsg("查询成功");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 对账
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(params="reconciliation")
	@ResponseBody
	public AjaxJson reconciliation(Integer userId, String startDate, String endDate){
		if(StringUtils.isEmpty(startDate)){
			startDate = DateTime.now().toString("yyyy-MM-dd");
		}
		if(StringUtils.isEmpty(endDate)){
			endDate = DateTime.now().toString("yyyy-MM-dd");
		}
		return wUserService.reconciliation(userId, startDate, endDate);
	}
	
	/**
	 * 获取未读消息
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getUnreadNotice")
	@ResponseBody
	public AjaxJson getUnreadNotice(Integer userId, @RequestParam(defaultValue="1")int page, int rows){
		AjaxJson json = new AjaxJson();
		try {
			if(userId == null){
				json.setMsg("参数错误");
				json.setSuccess(false);
			}
			else{
				if(page == 0){
					page = 1;
				}
				Map<String, Object> noticeMap = new HashMap<String, Object>();
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				//获取未读消息数
				Map<String, Object> map = wUserService.countNotice(userId);
				if(map != null){
					//获取未读消息内容
					list = wUserService.getUnreadNotice(userId, page, rows);
					if(list.size() > 0){
						for(int i = 0; i<list.size(); i++){
							String time = list.get(i).get("time").toString();
							String dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(time).toString("yyyy-MM-dd HH:mm:ss");
							list.get(i).put("time", dateTime);
						}
					}
					noticeMap.putAll(map);
					noticeMap.put("Notice", list);
					json.setObj(noticeMap);
					json.setSuccess(true);
				}
				else {
					json.setMsg("系统没有发送消息");
					json.setObj("");
					json.setSuccess(true);					
				}
			}
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("获取未读消息失败");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 获取消息
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getNotice")
	@ResponseBody
	public AjaxJson getNotice(Integer userId, @RequestParam(defaultValue="1")int page, int rows){
		AjaxJson json = new AjaxJson();		
		try {
			if(userId == null){
				json.setMsg("参数错误");
				json.setSuccess(false);
			}
			else{
				if(page == 0){
					page = 1;
				}			
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();			
				list = wUserService.getNotice(userId, page, rows);
				if(list.size() > 0){
					for(int i = 0; i<list.size(); i++){
						String time = list.get(i).get("time").toString();
						String dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(time).toString("yyyy-MM-dd HH:mm:ss");
						list.get(i).put("time", dateTime);
					}
				json.setObj(list);
				json.setSuccess(true);
				}
				else {
					json.setObj("");
					json.setMsg("系统没有发送消息");
					json.setSuccess(true);
				}
							
			}
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("获取消息失败");
			json.setSuccess(false);
		}
		return json;
	}
	
	/**
	 * 改变消息状态
	 * @param userId
	 * @param noticeIds
	 * @return
	 */
	@RequestMapping(params = "readNotice")
	@ResponseBody
	public AjaxJson readNotice(Integer userId, String noticeIds){
		AjaxJson json = new AjaxJson();
		
		if(userId == null){
			json.setMsg("用户参数错误");
			json.setSuccess(false);
			return json;
		}
		if(noticeIds == null){
			json.setMsg("消息参数错误");
			json.setSuccess(false);
			return json;
		}		
		try {
			Date readTime = DateTime.now().toDate();
			wUserService.readNotice(userId, noticeIds, readTime);
			json.setSuccess(true);
			json.setMsg("操作成功");
			return json;
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("操作失败");
			json.setSuccess(false);
			return json;
		}
	}
	
	/**
	 * 获取条例名称
	 * @return
	 */
	@RequestMapping(params = "getRegulationName")
	@ResponseBody
	public AjaxJson getRegulationName(){
		AjaxJson json = new AjaxJson();		
		try {
			json.setObj(wUserService.getRegulationName());
			json.setMsg("获取管理条例名称成功");
			json.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("获取管理条例名称失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
	
	/**
	 * 获取条例内容
	 * @param id	 
	 * @return
	 */
	@RequestMapping(params = "getRegulationContent")
	@ResponseBody
	public AjaxJson getRegulationConent(@RequestParam Integer id){
		AjaxJson json = new AjaxJson();			
		try {
			json.setObj(wUserService.getRegulationContent(id));
			json.setMsg("获取管理条例内容成功");
			json.setSuccess(true);
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("获取管理条例内容失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
	
	/**
	 * 根据用户ID获取用户信息
	 * @author hyj
	 * @param userId	用户ID
	 * @return
	 */
	@RequestMapping(params = "getUserInfo")
	@ResponseBody
	public AjaxJson getUserInfo(@RequestParam Integer userId){
		AjaxJson json = new AjaxJson();
		try {
			Map<String, Object> result = this.wUserService.getUserInfo(userId);
			if(result == null){
				json.setMsg("获取用户信息失败: 没有匹配的信息");
				json.setSuccess(false);
				json.setStateCode("01");
			} else {
				json.setObj(result);
				json.setMsg("获取用户信息成功");
				json.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("获取用户信息失败: " + e.getMessage());
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
}
