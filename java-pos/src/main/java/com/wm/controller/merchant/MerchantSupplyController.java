package com.wm.controller.merchant;

import com.wm.dto.order.ConfirmPayRequest;
import com.wm.service.merchant.MerchantSupplyServiceI;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/**
 * 供应链
 * @author syq
 *
 */ 
@Controller
@RequestMapping("ci/merchantSupplyController")
public class MerchantSupplyController extends BaseController {
	Logger logger = LoggerFactory.getLogger(MerchantSupplyController.class);

	@Autowired
	private MerchantSupplyServiceI merchantSupplyService;
	
	/**
	 * 商品采购商,H5页面通过手机号登录
	 * @param phone
	 * @return
	 */
	@RequestMapping(params = "merchantLogin")
	@ResponseBody
	public AjaxJson merchantLogin(String phone){
		logger.info("供应链，1.5项目《H5页面通过手机号登录接口》被调用，merchantLogin begin....");
		return merchantSupplyService.merchantLogin(phone);
	}
	
	/**
	 * 余额支付
	 * @param id
	 * @param payType
	 * @param payPassword
	 * @return
	 */
	@RequestMapping(params = "balanceConfirmPay")
	@ResponseBody
	public AjaxJson balanceConfirmPay(ConfirmPayRequest request) {
		AjaxJson json = new AjaxJson(true, "操作成功", "00");
		String detail = "";
		if(request==null){
			return AjaxJson.failJson("传参出错，操作失败");
		}else{
			logger.info("项目1.5余额支付,request=" + request.toString());
			if(StringUtil.isEmpty(request.getType())){
				json = merchantSupplyService.validatePayPassword(request.getUserId(), request.getPayPassword());
				logger.info("验证密码是否成功，json="+json.toString());
				if(!json.isSuccess()){
					return json;
				}
			}else{
				if("0".equals(request.getType())){
					detail = "商家供应链商品购买订单支付（主单）";
				}else if("1".equals(request.getType())){
					detail = "商家供应链商品购买订单支付（子单）";
				}
			}
		}
		try {
			json = merchantSupplyService.balanceConfirmPay(request.getId(), request.getUserId(), request.getOrigin(), request.getType(), request.getPayType(), detail);
		} catch (RuntimeException e) {
			logger.info("商家余额支付失败:" + e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			json.setStateCode("02");
			return json;
		}
		if(!json.isSuccess()){
			return json;
		}
		json.setMsg("支付成功");
		return json;
	}

	/**
	 * 微信支付
	 *
	 * @param payId
	 * @param origin              单位为分单位为分
	 * @param weixinReturnCallUrl
	 * @return
	 */
	@RequestMapping(params = "weixinConfirmPay")
	@ResponseBody
	public AjaxJson weixinConfirmPay(String payId, String origin, String weixinReturnCallUrl, HttpServletRequest request) {
		String code = request.getParameter("code");
		logger.info("payId =" + payId + " ,origin = " + origin + " ,code = " + code + " ,wexinRetuurnCallUrl = " + weixinReturnCallUrl);
		code = StringUtil.isEmpty(code) ? "0" : code;
		AjaxJson json = merchantSupplyService.weixinConfirmPay(payId, origin, weixinReturnCallUrl, code);
		return json;
	}
	
	/**
	 * 获取商家用户余额
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "getMerchantUserMoney")
	@ResponseBody
	public Double getMerchantUserMoney(Integer userId){
		return merchantSupplyService.getMerchantUserMoney(userId);
	}

	/**
	 * 获取商家账户余额
	 * @param userId
	 * @return
	 */
	@RequestMapping(params = "getMerchantAccountMoney")
	@ResponseBody
	public Double getMerchantAccountMoney(Integer userId){
		return merchantSupplyService.getMerchantAccountMoney(userId);
	}
	
	/**
	 * 筛选出已关店或者已删除的商家用户ID
	 * @author hyj
	 * @param merchantUserIds	商家用户ID
	 * @return
	 */
	@RequestMapping(params = "userIdsWhitchMerchantNotAvailable")
	@ResponseBody
	public AjaxJson userIdsWhitchMerchantNotAvailable(String merchantUserIds){
		/*
		 * 检查参数
		 */
		if(merchantUserIds == null || merchantUserIds.isEmpty()){
			return AjaxJson.failJson("入参merchantUserIds不能为空");
		}
		return this.merchantSupplyService.userIdsWhitchMerchantNotAvailable(merchantUserIds);
	}
}
