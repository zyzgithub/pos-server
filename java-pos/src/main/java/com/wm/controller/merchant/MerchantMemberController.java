package com.wm.controller.merchant;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.service.merchant.MerchantMemberServiceI;

/**
 * @Title: Controller
 * @author jiangpingmei
 *
 */
@Controller
@RequestMapping("ci/merchantMemberController")
public class MerchantMemberController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(MerchantMemberController.class);
	
	@Autowired
	private MerchantMemberServiceI merchantMemberService;
	
	/**
	 * 获得商家会员信息(总人数和总余额)
	 * @param merchantId 商家id
	 * @return
	 */
	@RequestMapping(params = "getMemberInfo")
	@ResponseBody
	public AjaxJson getMemberInfo(Integer merchantId){
		AjaxJson j = new AjaxJson();
		if(merchantId!=null){
			MerchantEntity merchant = merchantMemberService.get(MerchantEntity.class, merchantId);
			if(merchant==null){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家不存在");
				logger.info("商家【"+merchantId+"】不存在");
			}else{
				Map<String, Object> map = merchantMemberService.getMemberInfo(merchantId);
				j.setObj(map);
				j.setStateCode("00");
				j.setSuccess(true);
			}
		}else{
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("商家id不能为空");
		}
		return j;
	}
	
	/**
	 * 获得商家会员规则
	 * @param merchantId 商家id
	 * @return
	 */
	@RequestMapping(params = "getMemberRule")
	@ResponseBody
	public AjaxJson getMemberRule(Integer merchantId){
		AjaxJson j = new AjaxJson();
		if(merchantId!=null){
			MerchantEntity merchant = merchantMemberService.get(MerchantEntity.class, merchantId);
			if(merchant==null){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家不存在");
				logger.info("商家【"+merchantId+"】不存在");
			}else{
				Map<String, Object> map = merchantMemberService.getMemberRule(merchantId);
				j.setObj(map);
				j.setStateCode("00");
				j.setSuccess(true);
			}
		}else{
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("商家id不能为空");
		}
		return j;
	}
	
	/**
	 * 创建或更新商家规则
	 * @param merchantId 商家id
	 * @param userId 操作人员的用户id
	 * @param money 会员首次最低充值金额(单位：元)
	 * @param discount 商家默认会员折扣(单位：%)
	 */
	@RequestMapping(params = "createOrUpdateMemberRule")
	@ResponseBody
	public AjaxJson createOrUpdateMemberRule(Integer merchantId, Integer userId, Double money, Integer discount){
		AjaxJson j = new AjaxJson();
		if(merchantId!=null && discount!=null){
			MerchantEntity merchant = merchantMemberService.get(MerchantEntity.class, merchantId);
			if(merchant==null){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家不存在");
				logger.info("商家【"+merchantId+"】不存在");
			}else{
				money = 100.0;//用户端设置充值金额：100、200、500等等
				merchantMemberService.createOrUpdateMemberRule(merchantId, userId, money, discount);
				j.setMsg("操作成功");
				j.setStateCode("00");
				j.setSuccess(true);
			}
		}else{
			j.setMsg("检查参数，不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 获得商家会员列表
	 * @param merchantId 商家id
	 * @param page 起始分页 从1开始分页
	 * @param rows 分页行数
	 * @return
	 */
	@RequestMapping(params = "getMemberList")
	@ResponseBody
	public AjaxJson getMemberList(Integer merchantId, Integer page, Integer rows){
		AjaxJson j = new AjaxJson();
		if(merchantId!=null && page!=null && rows!=null){
			MerchantEntity merchant = merchantMemberService.get(MerchantEntity.class, merchantId);
			if(merchant==null){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家不存在");
				logger.info("商家【"+merchantId+"】不存在");
			}else{
				List<Map<String, Object>> list = merchantMemberService.getMemberList(merchantId, page, rows);
				j.setObj(list);
				j.setMsg("操作成功");
				j.setStateCode("00");
				j.setSuccess(true);
			}
		}else{
			j.setMsg("检查参数，不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 获得商家会员充值记录
	 * @param merchantId 商家id
	 * @param page 起始分页
	 * @param rows 分页行数
	 * @return
	 */
	@RequestMapping(params = "getMemberChargeMoneyRecords")
	@ResponseBody
	public AjaxJson getMemberChargeMoneyRecords(Integer merchantId, Integer page, Integer rows){
		AjaxJson j = new AjaxJson();
		if(merchantId!=null && page!=null && rows!=null){
			MerchantEntity merchant = merchantMemberService.get(MerchantEntity.class, merchantId);
			if(merchant==null){
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("商家不存在");
				logger.info("商家【"+merchantId+"】不存在");
			}else{
				List<Map<String, Object>> list = merchantMemberService.getMemberChargeMoneyRecords(merchantId, page, rows);
				j.setObj(list);
				j.setMsg("操作成功");
				j.setStateCode("00");
				j.setSuccess(true);
			}
		}else{
			j.setMsg("检查参数，不能为空");
			j.setStateCode("01");
			j.setSuccess(false);
		}
		return j;
	}

}
