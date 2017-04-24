package com.wm.controller.merchant;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.courier_mana.remote.RemoteClient;
import com.life.commons.CommonUtils;
import com.wm.entity.menu.MenuVo;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantStatisticsMenuVo;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.withdrawals.WithdrawalsServiceI;
import com.wm.util.PageList;
import com.wp.ConfigUtil;

import jeecg.system.service.SystemService;

/**   
 * @Title: Controller
 * @Description: merchant
 * @author wuyong
 * @date 2015-01-07 09:59:42
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("ci/merchantController")
public class MerchantController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(MerchantController.class);

	@Autowired
	private MerchantServiceI merchantService;
	
	@Autowired
	private MerchantInfoServiceI MerchantInfoService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
	private MenutypeServiceI menutypeService;
	
	@Autowired
	private WithdrawalsServiceI withdrawalsService;
	
	@Value("${isMixVersion}")
	private String isMixVersion;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * merchant列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "merchant")
	public ModelAndView merchant(HttpServletRequest request) {
		return new ModelAndView("com/wm/merchant/merchantList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(MerchantEntity merchant,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MerchantEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, merchant);
		this.merchantService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除merchant
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MerchantEntity merchant, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		merchant = merchantService.getEntity(MerchantEntity.class, merchant.getId());
		message = "删除成功";
		merchantService.delete(merchant);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加merchant
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MerchantEntity merchant, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(merchant.getId())) {
			
			message = "更新成功";
			MerchantEntity t = merchantService.get(MerchantEntity.class, merchant.getId());
			
			try {
				 Date date = new Date();
				 
				if (StringUtil.isEmpty(merchant.getDisplay())) {
					 merchant.setDisplay(null);
				}
				//判断外卖电话修改
				if (!StringUtil.isEmpty(merchant.getMobile())) {
					
					if (StringUtil.compareDate(date, t.getMobileUpdateTime())) {
						j.setMsg("一天只能修改一次外卖电话");
						j.setStateCode("01");
						j.setSuccess(false);
						return j;
					}else {
						merchant.setMobileUpdateTime(new Date());
					}
				}else {
					merchant.setMobile(null);
				}
				
				//判断起送金额修改
				if (merchant.getDeliveryBegin() !=null) {
					if (StringUtil.compareDate(date, t.getDeliveryBeginUpdateTime()) ) {
						j.setMsg("一天只能修改一次起送金额");
						j.setStateCode("01");
						j.setSuccess(false);
						return j;
					}else {
						merchant.setDeliveryBeginUpdateTime(new Date());
					}
				}
				//判断公告修改
				if (merchant.getNotice() !=null) {
					if (StringUtil.compareDate(date,t.getNoticeTime())) {
						j.setMsg("一天只能修改一次公告");
						j.setStateCode("01");
						j.setSuccess(false);
						return j;
					}else {
						merchant.setNoticeTime(new Date());
					}
				}
				
				MyBeanUtils.copyBeanNotNull2Bean(merchant, t);
				merchantService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			merchantService.save(merchant);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * merchant列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(MerchantEntity merchant, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(merchant.getId())) {
			merchant = merchantService.getEntity(MerchantEntity.class, merchant.getId());
			req.setAttribute("merchantPage", merchant);
		}
		return new ModelAndView("com/wm/merchant/merchant");
	}
	
	/**
	 * 开店或关店
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(params = "openMerchant")
	@ResponseBody
	public AjaxJson openMerchant(String display, MerchantEntity merchant, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		MerchantEntity t = merchantService.get(MerchantEntity.class, merchant.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(merchant, t);
			t.setDisplay(display);
			merchantService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 商家转账
	 * @param userName
	 * @param money
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params = "MerchantTransferAccounts")
	@ResponseBody
	public AjaxJson MerchantTransferAccounts(String userName, double money,int merchantId) {
		AjaxJson j=new AjaxJson();
		j=merchantService.MerchantTransferAccounts(userName, money, merchantId);
		return j;
	}
	
	@RequestMapping(params="takeCashRuleLoader")
	@ResponseBody
	public AjaxJson takeCashRuleLoader(Integer userId) {
		return withdrawalsService.takeCashRuleLoader(userId);
	}
	
	@RequestMapping(params="beforeTakeCash")
	@ResponseBody
	public AjaxJson beforeTakeCash(Integer userId, int takeMode) {
		return withdrawalsService.beforeTakeCash(userId, takeMode);
	}
	
	@RequestMapping(params="tryTakeCash")
	@ResponseBody
	public AjaxJson tryTakeCash(Integer userId, Double takeAmount, Integer cardId, Integer takeMode) throws Exception {
		AjaxJson response = new AjaxJson();
		response.setStateCode(AjaxJson.STATE_CODE_FAIL);
		response.setSuccess(false);
		try {
			double cost = withdrawalsService.tryTakeCash(userId, takeAmount, cardId, takeMode);
			response.setStateCode(AjaxJson.STATE_CODE_SUCCESS);
			response.setSuccess(true);
			response.setMsg("您的提现申请已通过，请耐心等待");
			response.setObj(CommonUtils.unRounding(cost));
			return response;
		} catch (Exception ex) {
			response.setMsg(ex.getMessage());
			return response;
		}
	}
	
	/**
	 * 商家申请提现
	 * @param merchantId
	 * @param money
	 * @param cardId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params="merchantWithdraw")
	@ResponseBody
	public AjaxJson merchantWithdraw(Integer merchantId, Double money, Integer cardId) throws Exception {
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setSuccess(false);
		if (merchantId == null) {
			j.setMsg("商家id不能为空");
			return j;
		}
		if (cardId == null) {
			j.setMsg("请先绑定银行卡");
			return j;
		}
		MerchantEntity merchant = merchantService.get(MerchantEntity.class, merchantId);
		if (merchant == null) {
			j.setStateCode("01");
			j.setMsg("商家不存在");
			j.setSuccess(false);
			return j;
		}
//		j = merchantService.merchantWithdraw(merchantId, money, cardId);
		try {
			j = withdrawalsService.newWithDrawApply(merchant.getWuser(), money, cardId);
		} catch(Exception ex) {
			j.setStateCode("01");
			j.setMsg(ex.getMessage());
			j.setSuccess(false);
			return j;
		}
		return j;
	}
	
	/**
	 * 商家发短信
	 * @param merchantId
	 */
	@RequestMapping(params="merchanTmassTexting")
	@ResponseBody
	public AjaxJson merchanTmassTexting(int merchantId,String title,String content,HttpServletRequest request){
		AjaxJson j=new AjaxJson();
		merchantService.merchanTmassTexting(merchantId, title, content, request);
		j.setMsg("操作成功，等待后台审核");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
		
		
	}
	
	/**
	 * 审核发短信通过
	 * @param id
	 */
	@RequestMapping(params="passTmassTexting")
	@ResponseBody
	public AjaxJson passTmassTexting(int id){
		AjaxJson j=new AjaxJson();
		j=merchantService.passTmassTexting(id);
		return j;
		
	}
	/**
	 * 审核发短信不通过 
	 * @param id
	 */
	@RequestMapping(params="noPassTmassTextting")
	@ResponseBody
	public AjaxJson noPassTmassTextting(int id){
		AjaxJson j=new AjaxJson();
		merchantService.noPassTmassTextting(id);
		j.setMsg("操作成功，等待后台审核不通过");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 *  门店热销菜品统计
	 * @param title
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params="statisticsMenu")
	@ResponseBody
	public AjaxJson statisticsMenu(String title,String startTime, String endTime,@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows){
		AjaxJson j=new AjaxJson();
		PageList<MerchantStatisticsMenuVo> pageList = merchantService.statisticsMenu(title, startTime, endTime, page, rows);
		j.setObj(pageList);
		j.setMsg("查询成功");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 查询商家菜品分类
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params="findMenuTypes")
	@ResponseBody
	public AjaxJson findMenuTypes(Integer merchantId){
		AjaxJson j=new AjaxJson();
		List<MenutypeEntity> types = menutypeService.queryByMerchantId(merchantId);
		j.setObj(types);
		j.setMsg("查询成功");
		j.setStateCode("00");
		j.setSuccess(true);
		return j;
	}
	
	/**
	 * 查询商家商品列表（支持模糊查询）
	 * @param merchantId
	 * @param menuTypeId 商品分类ID
	 * @param barcode 商品二维码
	 * @param name 商品名称
	 * @param isonline 1：只支持线上，2：只支持线下，3：同时支持线上线下，以逗号拼接
	 * @return
	 */
	@RequestMapping(params="findMenus")
	@ResponseBody
	public AjaxJson findMenus(Integer merchantId, Integer menuTypeId, String barcode, String name, String isonline, Integer page, Integer rows) {
		
		MenuVo menu = new MenuVo();
		menu.setMerchantId(merchantId);
		menu.setTypeId(menuTypeId);
		menu.setBarcode(barcode);
		menu.setName(name);
		menu.setIsonline(isonline);
		menu.setDisplay("Y");
		menu.setIsDelete("N");
		
		AjaxJson j = new AjaxJson();
		PageList<com.wm.entity.menu.MenuVo> list = menuService.findByEntityList(menu, page, rows);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	@RequestMapping(params="findMechantByBarcode")
	@ResponseBody
	public AjaxJson findMechantByBarcode(Integer merchantId, String barcode) {
		logger.info("获取条形码对应的商品, merchantId:" + merchantId + ", barcode:" +  barcode);
		MenuVo menuVo = menuService.searchItemByBarcode(merchantId, barcode);
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStateCode("01");
		ajaxJson.setSuccess(false);
		if (menuVo == null) {
			ajaxJson.setMsg("条形码未录入！");
		} else {
			if ("N".equals(menuVo.getDisplay()) || "Y".equals(menuVo.getIsDelete())) {
				ajaxJson.setMsg("该商品已下架！");
			} else {
				ajaxJson.setStateCode("00");
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("成功");
				ajaxJson.setObj(menuVo);
			}
		}
		logger.info("获取条形码对应的商品, return:" + JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 获得我的商家
	 * @param id
	 * @return
	 */
	@RequestMapping(params="findMyMechant")
	@ResponseBody
	public AjaxJson findMyMechant(Long merchantId){

		AjaxJson j = new AjaxJson();
		Map<String, Object> myMerchant = null;
		try {
			myMerchant = merchantService.getMyMerchant(merchantId);
		} catch (Exception e) {
			logger.warn(e);
		}
		if (myMerchant!=null) {
			j.setObj(myMerchant);
			j.setStateCode("00");
			j.setSuccess(true);
			j.setMsg("操作成功");
		}else {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("商家不存在");
		}
		
		return j;
	}
	
	/**
	 * 获取营业时间列表
	 * @param id
	 * @return
	 */
	@RequestMapping(params="findTimeList")
	@ResponseBody
	public AjaxJson findTimeList(Long merchantId){
		AjaxJson j = new AjaxJson();
		List<Map<String, Object>> list = merchantService.getMerchantOpenTime(merchantId);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 添加或修改营业时间
	 * @param params
	 * @return
	 */
	@RequestMapping(params="saveOpenTime")
	@ResponseBody
	public AjaxJson openTimeSave(String params){
		AjaxJson j = new AjaxJson();
		try {
			j= merchantService.updateOrSaveMerchantOpenTime(params);
		} catch (Exception e) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("添加或修改营业时间异常");
		}
		
		return j;
	}
	

	/**
	 * 删除商家营业时间
	 * @param id 
	 * @param merchantId 商家id
	 * @return
	 */
	@RequestMapping(params = "deleteOpenTime")
	@ResponseBody
	public AjaxJson deleteOpentime(Integer id, Integer merchantId) {
		
		AjaxJson j = new AjaxJson();
		if(id==null && !"".equals(id) && merchantId==null && !"".equals(merchantId)){
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("删除不成功");
			return j;
		}else{
			return merchantService.deleteOpentime(id, merchantId);
		}
	}
	
	/**
	 * 修改商家配送距离
	 * @param merchantId
	 * @param deliveryScope
	 * @return
	 */
	@RequestMapping(params="updateDeliveryScope")
	@ResponseBody
	public AjaxJson updateDeliveryScope(int merchantId,BigDecimal deliveryScope){
		AjaxJson j=new AjaxJson();
		try {
			//判断外卖电话修改
			if (deliveryScope!=null) {
				if (deliveryScope.compareTo(BigDecimal.TEN)==1 || deliveryScope.compareTo(BigDecimal.ONE)==-1) {
					j.setStateCode("01");
					j.setSuccess(false);
					j.setMsg("配送距离大于等于1公里小于等于10公里");
					return j;
				}
				message = "更新成功";
//				MerchantEntity t = merchantService.get(MerchantEntity.class, merchant.getId());
//				 T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);
				 
				List<MerchantInfoEntity> findByProperty = MerchantInfoService.findByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
				MerchantInfoEntity myMerchantInfo=null;
				if (findByProperty!=null && findByProperty.size()>0) {
					 myMerchantInfo = findByProperty.get(0);
				}
				//Map<String, Object> myMerchantInfo = merchantService.getMyMerchantInfo(merchantId);
				if (myMerchantInfo!=null) {
					Date date = new Date();
					Date date2 =myMerchantInfo.getDeliveryScopeTime() == null ? null:myMerchantInfo.getDeliveryScopeTime();
					if (StringUtil.compareDate(date, date2)) {
						j.setMsg("一天只能修改一次配送范围");
						j.setStateCode("01");
						j.setSuccess(false);
						return j;
					}else {
						// 如果商家是供应商修改供应链后台的数据
						if(myMerchantInfo.getPlatformType() == 3){
							int userId = this.merchantService.getMerchantUserId(merchantId);
							this.updateERPDeliveryScope(userId, deliveryScope);
						}
						merchantService.updateMerchantDeliveryScope(merchantId, deliveryScope);
						j.setStateCode("00");
						j.setSuccess(true);
						j.setMsg("操作成功");
					}
				}else {
					j.setStateCode("01");
					j.setSuccess(false);
					j.setMsg("修改失败，没有该商家");
				}
			}else {
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("修改失败,参数有误");
			}
		} catch (Exception e) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("修改失败，操作异常");
		}
		
		return j;
	}
	
	/**
	 * 获取二维码内容
	 * @param merchantId
	 * @return
	 */
	@RequestMapping(params="getTwoDimensionalCodeUrl")
	@ResponseBody
	public AjaxJson getTwoDimensionalCodeUrl(Long merchantId,String type){
		AjaxJson j=new AjaxJson();
		Map<String, Object> map=new HashMap<String, Object>();
		String merchantHomeUrl ="";
		if ("pay".equals(type)) {
			merchantHomeUrl = ConfigUtil.QRCode_URL + merchantId;
		}else {
			merchantHomeUrl = String.format(ConfigUtil.MENULIST_URI, merchantId);
			if("false".equals(isMixVersion)){
				merchantHomeUrl = ConfigUtil.MERCHANT_HOME.replace("MERCHANT_ID", merchantId + "");
			}
		}
		map.put("url", merchantHomeUrl);
		j.setObj(map);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
	
	/**
	 * 获取商家设备列表
	 * @return
	 */
	@RequestMapping(params="getMerchantDeviceList")
	@ResponseBody
	public AjaxJson getMerchantDeviceList(String merchantSource) {
		AjaxJson j = new AjaxJson();
		try {
			if (StringUtil.isEmpty(merchantSource)) {
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家类型为空");
				return j;
			}
			List<Map<String, Object>> merchantDeviceList = merchantService.merchantDeviceList(merchantSource);
			j.setObj(merchantDeviceList);
		} catch (Exception e) {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("获取商家设备异常");
		}
		return j;
	}
	
	/**
	 * 修改ERP端的配送距离
	 * @author hyj
	 * @param userId		商家的用户ID
	 * @param deliveryScope	配送距离
	 */
	private void updateERPDeliveryScope(int userId,BigDecimal deliveryScope){
		StringBuilder url = new StringBuilder(EnvConfig.base.SUPPLY_ERP_HOST);
		url.append("/warehouse/updateDeliveryScope");
		url.append("?userId=");
		url.append(userId);
		url.append("&deliveryScope=");
		url.append(deliveryScope.toString());
		String response = RemoteClient.get(url.toString());
		JSONObject responseJson = JSONObject.parseObject(response);
		if(responseJson.getIntValue("code") != 0){
			throw new RuntimeException("调用ERP修改仓库配送距离接口失败");
		};
	}
}