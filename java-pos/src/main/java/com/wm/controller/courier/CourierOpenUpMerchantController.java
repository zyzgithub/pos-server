package com.wm.controller.courier;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.NonUniqueResultException;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.wm.controller.courier.dto.MerchantInfoDTO;
import com.wm.entity.merchantapply.MerchantApplyEntity;
import com.wm.entity.notice.NoticeEntity;
import com.wm.service.category.CategoryServiceI;
import com.wm.service.courier.CourierOpenUpMerchantServiceI;
import com.wm.service.notice.NoticeServiceI;
import com.wm.service.syscode.SysCodeServiceI;
import com.wm.util.AliOcs;
import com.wm.util.StringUtil;

import jeecg.system.service.SystemService;

@Controller
@RequestMapping("ci/courierOpenUpMerchantController")
public class CourierOpenUpMerchantController extends BasicController {

	private static final Logger logger = LoggerFactory.getLogger(CourierOpenUpMerchantController.class);

	@Autowired
	private CourierOpenUpMerchantServiceI courierOpenUpMerchantServiceI;

	@Autowired
	private SysCodeServiceI sysCodeService;

	@Autowired
	private CategoryServiceI categoryService;

	@Autowired
	private NoticeServiceI noticeService;

	@Autowired
	private SystemService systemService;

	/**
	 * 获取商家来源、类型
	 *
	 * @return
	 */
	@RequestMapping(params = "getMerchantCode")
	@ResponseBody
	public AjaxJson getMerchantCode(@RequestParam String code) {
		AjaxJson json = new AjaxJson();
		try {
			List<Map<String, Object>> list = sysCodeService.getSysCodeName(code);
			json.setObj(list);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			if (code.equals("merchant_source")) {
				json.setMsg("未获取到商家来源");
			} else {
				json.setMsg("未获取到商家来类型 ");
			}
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}

	/**
	 * 获取商家类别
	 *
	 * @return
	 */
	@RequestMapping(params = "getMerchantGroup")
	@ResponseBody
	public AjaxJson getMerchantGroup(HttpServletRequest request, String zone) {
		String ct = request.getHeader("ct");

		AjaxJson json = new AjaxJson();
		try {
			List<Map<String, Object>> list = categoryService.getCategoryGroup(zone);
			if (!"9".equals(ct)) {
				Iterator<Map<String, Object>> i = list.iterator();
				Map<String, Object> tmp;
				while (i.hasNext()) {
					tmp = i.next();
					if ("9".equals((tmp.get("czone") + "").trim())) {
						i.remove();
					}
				}
			}
			json.setObj(list);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			if (zone.equals("city")) {
				json.setMsg("未获取到商家所属城市");
			} else {
				json.setMsg("未获取到商家类别");
			}
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}

	/**
	 * 保存商家申请信息
	 *
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveMerchantInformation")
	@ResponseBody
	public AjaxJson saveMerchantInformation(@RequestParam Integer courierId, @Valid MerchantInfoDTO merchantInfomation,
			Errors errors) {
		return saveMerchantInfos(courierId, "", merchantInfomation, errors);
	}

	/**
	 * 保存商家申请信息
	 *
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveMerchantInfos")
	@ResponseBody
	public AjaxJson saveMerchantInfos(@RequestParam Integer courierId, @RequestParam String remark,
			@Valid MerchantInfoDTO merchantInfomation, Errors errors) {
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(merchantInfomation));
		if (errors.hasErrors()) {
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for (int i = 0; i < errorsList.size(); i++) {
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.error("参数错误, 参数: " + JSON.toJSONString(merchantInfomation));
			json.setSuccess(false);
			json.setObj(errorsMap);
			json.setMsg("保存商家信息失败");
			json.setStateCode("02");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		// 验证身份证号码
		if (!StringUtil.isIdCard(merchantInfomation.getIdCardNo())) {
			json.setMsg("您输入的身份证号码无效");
			json.setSuccess(false);
			json.setStateCode("01");
			return json;
		}
		// 验证手机号码
		if (!StringUtil.isMobileNumber(merchantInfomation.getMobile())) {
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("您输入的手机号码无效");
			return json;
		}

		// 如果商家类别是“供应商(groupId=541)”,“门店供应商(groupId=1012)”、“平台供应商(groupId=1013)”、“九州门店供应商(groupId=1350)”，都需要绑定二维码
		int merchantGroupId = merchantInfomation.getGroupId().intValue();
		if (merchantGroupId != 541 && merchantGroupId != 1013 && merchantGroupId != 1012 && merchantGroupId != 1350) {
			// 如果没有绑二维码
			if (merchantInfomation.getQrcodeLibraryId() == null) {
				json.setSuccess(false);
				json.setStateCode("01");
				json.setMsg("请绑定二维码");
				return json;
			}
		}

		// 一个二维码只能绑定一个商家
		if (merchantInfomation.getQrcodeLibraryId() != null
				&& merchantInfomation.getQrcodeLibraryId().intValue() != 0) {
			try {
				MerchantApplyEntity merchantApplyEntity = systemService.findUniqueByProperty(MerchantApplyEntity.class,
						"qrcodeLibraryId", merchantInfomation.getQrcodeLibraryId());

				// 如果是开拓新的商家
				if (merchantInfomation.getId() == null || merchantInfomation.getId().intValue() == 0) {
					// 存在该二维码与商家绑定的记录，就不能在绑定该二维码
					if (merchantApplyEntity != null) {
						json.setSuccess(false);
						json.setStateCode("03");
						json.setMsg("该二维码已与其它商家绑定");
						logger.warn("二维码id: " + merchantInfomation.getQrcodeLibraryId() + "已与其它商家绑定");
						return json;
					}
				}
				// 如果是更新已开拓商家
				if (merchantInfomation.getId() != null && merchantInfomation.getId().intValue() != 0) {
					// 如果存在此二维码与商家绑定的记录，并且该记录的id 与 将要跟新的记录id 不一致，不能绑定该二维码
					if (merchantApplyEntity != null
							&& !merchantApplyEntity.getId().equals(merchantInfomation.getId())) {
						json.setSuccess(false);
						json.setStateCode("03");
						json.setMsg("该二维码已与其它商家绑定");
						logger.warn("二维码id: " + merchantInfomation.getQrcodeLibraryId() + "已与其它商家绑定");
						return json;
					}
				}
			} catch (Exception e) {
				if (e instanceof NonUniqueResultException) {
					json.setSuccess(false);
					json.setStateCode("03");
					json.setMsg("该二维码与多家商家绑定");
					logger.warn("数据异常，二维码id: " + merchantInfomation.getQrcodeLibraryId() + "已与多家商家绑定");
					return json;
				} else {
					throw e;
				}
			}
		}
		return courierOpenUpMerchantServiceI.saveMerchantInformation(courierId, merchantInfomation, remark);
	}

	/**
	 * 根据快递员id获取开拓商家记录
	 *
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getOpenUpMerchantRecord")
	@ResponseBody
	public AjaxJson getOpenUpMerchantRecord(@RequestParam Integer courierId, int page, int rows) {
		AjaxJson json = new AjaxJson();
		try {
			List<Map<String, Object>> list = courierOpenUpMerchantServiceI.getOpenUpMerchantRecord(courierId, page,
					rows);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String createTime = list.get(i).get("createTime").toString();
					String dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(createTime)
							.toString("yyyy年MM月dd日 HH:mm:ss");
					list.get(i).put("createTime", dateTime);
				}
				json.setObj(list);
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("获取开拓商家记录成功");
			} else {
				json.setObj(list);
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("您没有开拓商家的记录");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取开拓商家记录失败");
		}
		return json;
	}

	/**
	 * 获取开拓商家的详细资料
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getMerchantApplyInfo")
	@ResponseBody
	public AjaxJson getMerchantApplyInfo(@RequestParam Integer id) {
		logger.info("查找id为： " + id + "对应的商家申请详情");
		AjaxJson json = new AjaxJson();
		Map<String, Object> merchantApplyMap = new HashMap<String, Object>();
		try {
			merchantApplyMap = courierOpenUpMerchantServiceI.getMerchantApplyInfo(id);
			if (merchantApplyMap == null) {
				json.setObj("");
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("找不到对应的商家详情");
				logger.warn("找不到id为：" + id + "对应的商家申请详情");
			} else {
				if (merchantApplyMap.get("businessLicense") == null) {
					merchantApplyMap.put("businessLicense", "");
				}
				if (merchantApplyMap.get("businessLicenseImgUrl") == null) {
					merchantApplyMap.put("businessLicenseImgUrl", "");
				}
				if (merchantApplyMap.get("foodServiceLicense") == null) {
					merchantApplyMap.put("foodServiceLicense", "");
				}
				if (merchantApplyMap.get("foodServiceLicenseImgUrl") == null) {
					merchantApplyMap.put("foodServiceLicenseImgUrl", "");
				}
				if (merchantApplyMap.get("refuseReason") == null) {
					merchantApplyMap.put("refuseReason", "");
				}
				if (merchantApplyMap.get("bankName") == null) {
					merchantApplyMap.put("bankName", "");
				}
				if (merchantApplyMap.get("orgName") == null) {
					merchantApplyMap.put("orgName", "");
				}
				if (merchantApplyMap.get("supportSaleTypeName") == null) {
					merchantApplyMap.put("supportSaleTypeName", "");
				}
				if (merchantApplyMap.get("merchantSourceName") == null) {
					merchantApplyMap.put("merchantSourceName", "");
				}
				if (merchantApplyMap.get("groupName") == null) {
					merchantApplyMap.put("groupName", "");
				}

				// 初始化合同照为""
				for (int j = 0; j < 4; j++) {
					merchantApplyMap.put("contractImgUrl" + j, "");
				}

				if (merchantApplyMap.get("contractImgUrls") != null) {
					String contractImgUrls = merchantApplyMap.get("contractImgUrls").toString();

					// 如果有合同照
					if (!contractImgUrls.equals("")) {
						String[] strings = contractImgUrls.split(",");

						for (int i = 0; i < strings.length; i++) {
							merchantApplyMap.put("contractImgUrl" + i, strings[i]);
						}
					}
				}
				json.setObj(merchantApplyMap);
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("获取开拓商家详情成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取开拓商家详情失败");
		}
		return json;
	}

	@RequestMapping(params = "pushAudit")
	@ResponseBody
	public AjaxJson pushAudit(@RequestParam Integer userId, @RequestParam Integer noticeId) {
		AjaxJson ajaxJson = new AjaxJson();
		try {
			NoticeEntity noticeEntity = noticeService.getNotice(noticeId);

			if (noticeEntity == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("无法根据参数noticeId=" + noticeId + "获取到对应的消息");
				ajaxJson.setStateCode("02");
				return ajaxJson;
			}
			noticeService.pushAudit(noticeEntity, userId);
			ajaxJson.setSuccess(true);
			ajaxJson.setState("00");
			ajaxJson.setMsg("发送成功！");

		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setState("01");
			ajaxJson.setMsg("发送失败！");
		}
		return ajaxJson;
	}

	@RequestMapping(params = "getContractId")
	@ResponseBody
	public AjaxJson getContractId() {
		AjaxJson result = new AjaxJson();
		try {

			Calendar cal = Calendar.getInstance();
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH) + 1;
			int d = cal.get(Calendar.DATE);

			int ids = AliOcs.getContractId(d);
			if (ids == -1) {
				result = AjaxJson.failJson("获取合同编号失败");
				return result;
			}

			StringBuilder sb = new StringBuilder();
			sb.append("JZ");
			sb.append(y);// 年
			if (m < 10) {
				sb.append("0");
			}
			sb.append(m);// 月
			if (d < 10) {
				sb.append("0");
			}
			sb.append(d);// 日

			int str_length = String.valueOf(ids).length();
			int v = 5 - str_length;
			// 补零
			for (int i = 0; i < v; i++) {
				sb.append("0");
			}
			sb.append(ids);
			String cstr = sb.toString();
			result.setObj(cstr);
			result.setSuccess(true);
			result.setStateCode("00");
			logger.info("生成合同 编号：" + cstr);
		} catch (Exception e) {
			e.printStackTrace();
			result = AjaxJson.failJson("获取合同编号失败");
		}
		return result;
	}

	/**
	 * 根据 账期，获得 扫码扣点类型
	 * 
	 * @param income_date
	 * @return
	 */
	@RequestMapping(params = "getAgentMerchantBucklePoint")
	@ResponseBody
	public AjaxJson getAgentMerchantBucklePoint(@RequestParam Integer income_date) {
		AjaxJson result = new AjaxJson();
		try {
			result.setObj(courierOpenUpMerchantServiceI.getAgentMerchantBucklePoint(income_date));
			result.setSuccess(true);
			result.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			result = AjaxJson.failJson("获取扫码扣点类型失败");
		}
		return result;
	}

	/**
	 * 根据 账期，获得 扫码扣点类型
	 * 
	 * @param income_date
	 * @return
	 */
	@RequestMapping(params = "getAgentMerchantBucklePointAll")
	@ResponseBody
	public AjaxJson getAgentMerchantBucklePointAll() {
		AjaxJson result = new AjaxJson();
		try {
			result.setObj(courierOpenUpMerchantServiceI.getAgentMerchantBucklePoint());
			result.setSuccess(true);
			result.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			result = AjaxJson.failJson("获取扫码扣点类型失败");
		}
		return result;
	}
}
