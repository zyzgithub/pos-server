package com.wm.service.impl.courier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.courier.dto.MerchantInfoDTO;
import com.wm.entity.merchantapply.MerchantApplyEntity;
import com.wm.service.courier.CourierOpenUpMerchantServiceI;
import com.wm.service.org.OrgServiceI;

@Service
@Transactional
public class CourierOpenUpMerchantServiceImpl extends CommonServiceImpl implements CourierOpenUpMerchantServiceI {

	@Autowired
	private OrgServiceI orgService;
	/**
	 *保存开拓商家信息
	 */
	@Override
	public AjaxJson saveMerchantInformation(Integer courierId, MerchantInfoDTO merchantInfomation, String remark) {
		AjaxJson json = new AjaxJson();
		MerchantApplyEntity merchantApplyEntity = null;
		try {
			if (merchantInfomation.getMerchantSource() == 2) {
				json.setMsg("开拓私厨还在玩命开发中，敬请期待");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
			if (merchantInfomation.getId() != null) {
				merchantApplyEntity = this.get(MerchantApplyEntity.class, merchantInfomation.getId());
			}
			if (merchantApplyEntity != null) {
				merchantApplyEntity.setUpdateTime(DateUtils.getSeconds());
			}
			if (merchantApplyEntity == null) {
				merchantApplyEntity = new MerchantApplyEntity();
			}
			merchantApplyEntity.setGroupId(merchantInfomation.getGroupId());
			merchantApplyEntity.setMobile(merchantInfomation.getMobile());
			merchantApplyEntity.setBusinessLicense(merchantInfomation.getBusinessLicense());
			merchantApplyEntity.setFoodServiceLicense(merchantInfomation.getFoodServiceLicense());
			merchantApplyEntity.setBusinessLicenseImgUrl(merchantInfomation.getBusinessLicenseImgUrl());
			merchantApplyEntity.setFoodServiceLicenseImgUrl(merchantInfomation.getFoodServiceLicenseImgUrl());
			merchantApplyEntity.setAddress(merchantInfomation.getAddress());
			merchantApplyEntity.setLongitude(new BigDecimal(merchantInfomation.getLongitude()));
			merchantApplyEntity.setLatitude(new BigDecimal(merchantInfomation.getLatitude()));
			merchantApplyEntity.setMerchantName(merchantInfomation.getMerchantName());
			merchantApplyEntity.setShopkeeper(merchantInfomation.getShopkeeper());
			merchantApplyEntity.setSupportSaleType(merchantInfomation.getSupportSaleType());
			merchantApplyEntity.setMerchantSource(merchantInfomation.getMerchantSource());
			merchantApplyEntity.setIdCardNo(merchantInfomation.getIdCardNo());
			merchantApplyEntity.setIdCardFrontImgUrl(merchantInfomation.getIdCardFrontImgUrl());
			merchantApplyEntity.setIdCardBackImgUrl(merchantInfomation.getIdCardBackImgUrl());
			merchantApplyEntity.setMerchantImgUrl(merchantInfomation.getMerchantImgUrl());
			merchantApplyEntity.setContractNo(merchantInfomation.getContractNo());
			merchantApplyEntity.setCourierId(courierId);
			merchantApplyEntity.setState(1);
			merchantApplyEntity.setCityId(merchantInfomation.getCityId());
			merchantApplyEntity.setBankId(merchantInfomation.getBankId());
			merchantApplyEntity.setAccountHolder(merchantInfomation.getAccountHolder());
			merchantApplyEntity.setSourceBank(merchantInfomation.getSourceBank());
			merchantApplyEntity.setBankCardNo(merchantInfomation.getBankCardNo());
			merchantApplyEntity.setBankCardFrontImgUrl(merchantInfomation.getBankCardFrontImgUrl());
			merchantApplyEntity.setContractImgUrls(merchantInfomation.getContractImgUrls());
			if (merchantInfomation.getQrcodeLibraryId() != null && merchantInfomation.getQrcodeLibraryId().intValue() != 0) {
				merchantApplyEntity.setQrcodeLibraryId(merchantInfomation.getQrcodeLibraryId());
			}
			merchantApplyEntity.setRemark(remark);
			
			//9扫码扣点-增加账期与扣点类型
			if(merchantInfomation.getDeduction_type() != null && merchantInfomation.getDeduction_type().length() > 0) {
				merchantApplyEntity.setIncome_date(merchantInfomation.getIncome_date());
				merchantApplyEntity.setDeduction_type(merchantInfomation.getDeduction_type());
			}else{
				merchantApplyEntity.setDeduction_type("");
				merchantApplyEntity.setIncome_date(0);
			}
			
			// 保存商家信息
			saveOrUpdate(merchantApplyEntity);
			json.setMsg("保存商家信息成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存商家信息失败");
			json.setSuccess(true);
			json.setStateCode("01");
		}
		return json;

	}

	/**
	 * 根据快递员id获取开拓商家的记录
	 */
	@Override
	public List<Map<String, Object>> getOpenUpMerchantRecord(Integer courierId, int page, int rows) {
		StringBuilder query = new StringBuilder();
		query.append(" select id, merchant_name merchantName, state, create_time createTime, ifnull(refuse_reason, '') refuseReason ");
		query.append(" from  0085_merchant_apply");
		query.append(" where courier_id = ?  ");
		query.append(" order by createTime desc ");
		return findForJdbcParam(query.toString(), page, rows, courierId);
	}

	/**
	 * 根据申请id获取开拓商家的详细信息
	 */
	@Override
	public Map<String, Object> getMerchantApplyInfo(Integer id) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT ma.id, ma.courier_id courierId, ma.group_id groupId, ma.merchant_name merchantName, ma.mobile,");
		query.append(" ma.income_date, ma.deduction_type,");
		query.append(" ma.address,	ma.longitude, ma.latitude, ma.business_license businessLicense,");
		query.append(" ma.food_service_license foodServiceLicense,	ma.support_sale_type supportSaleType,");
		query.append(" ma.merchant_source merchantSource, ma.shopkeeper, ma.id_card_no idCardNo, ma.id_card_front_img_url idCardFrontImgUrl,");
		query.append(" ma.id_card_back_img_url idCardBackImgUrl, ma.merchant_img_url merchantImgUrl,");
		query.append(" ma.business_license_img_url businessLicenseImgUrl, ma.food_service_license_img_url foodServiceLicenseImgUrl,");
		query.append(" ma.contract_no contractNo, date_format(ma.create_time,'%Y年%m月%d日  %H:%m:%s') createTime,");
		query.append(" ma.state, ma.refuse_reason refuseReason, c.`name` groupName, sc1.code_name merchantSourceName, sc2.code_name supportSaleTypeName,");
		query.append(" ma.city_id cityId, o.org_name orgName, ");
		query.append(" ma.bank_id bankId, ma.account_holder accountHolder, ma.source_bank sourceBank, ma.bank_card_no bankCardNo, ma.bank_card_front_img_url bankCardFrontImgUrl,");
		query.append(" ma.contract_img_urls contractImgUrls, b.name bankName,");
		query.append(" ifnull(ma.qrcode_library_id, '') qrcodeLibraryId");
		query.append(" FROM	0085_merchant_apply ma");
		query.append(" LEFT JOIN sys_code sc1 ON");
		query.append(" sc1.`code` = 'merchant_source'	AND sc1.code_value = ma.merchant_source");
		query.append(" LEFT JOIN sys_code sc2 ON");
		query.append(" sc2.`code` = 'support_sale_type'	AND sc2.code_value = ma.support_sale_type");
		query.append(" LEFT JOIN category c ON c.zone = 'group'AND c.id = ma.group_id");
		query.append(" LEFT JOIN 0085_org o ON o.id = ma.city_id");
		query.append(" LEFT JOIN bank b ON b.id = ma.bank_id");
		query.append(" WHERE ma.id = ?");
		return findOneForJdbc(query.toString(), id);
	}
	@Override
	public List<String> getAgentMerchantBucklePoint(Integer income_date) {
		StringBuilder query = new StringBuilder();
		query.append(" select type ");
		query.append(" from agent_merchant_deduction");
		query.append(" where income_date=" + income_date);
		return findListbySql(query.toString());
	}
	
	@Override
	public List<Map<String, Object>> getAgentMerchantBucklePoint() {
		StringBuilder query = new StringBuilder();
		query.append(" select income_date, type ");
		query.append(" from agent_merchant_deduction");
		return findForJdbc(query.toString());
	}

	/** 
	 * 获取 商家 扫码 账期 列表
	 */
	public List<Integer> getAgentMerchantIncomeDates() {
		//账期会重复，所以这里使用去重复
		StringBuilder query = new StringBuilder();
		query.append(" select income_date ");
		query.append(" from agent_merchant_deduction");
		query.append(" where id in (select max(id) from agent_merchant_deduction group by income_date)");
		return findListbySql(query.toString());
	}
}
