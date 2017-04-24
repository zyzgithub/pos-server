package com.wm.service.impl.partnerDevelop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.partnerDevelop.dto.PartnerDevelopDTO;
import com.wm.entity.bank.BankEntity;
import com.wm.entity.partnerDevelop.PartnerDevelopEntity;
import com.wm.service.partnerDevelop.PartnerDevelopServiceI;
import com.wm.util.StringUtil;

@Service("partnerDevelopService")
@Transactional
public class PartnerDevelopImpl extends CommonServiceImpl implements PartnerDevelopServiceI{

	@Override
	public List<Map<String, Object>> getPartnerDevelopHis(Integer userId, int page, int rows) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT partner_name ,create_date,develop_id,remark as refuseReason,");
		query.append(" state,CASE state WHEN 1 THEN '审核中' WHEN 2 THEN '审核通过'  WHEN 3 THEN '审核不通过'  END AS 'stateName' ");
		query.append(" FROM 0085_partner_develop t ");
		query.append(" where user_id=? order by create_date desc ");

		List<Map<String, Object>> list = findForJdbcParam(query.toString(),
				page, rows, userId);
		if(CollectionUtils.isNotEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				map.put("id", map.get("develop_id"));
				// 时间处理
				String createTime = list.get(i).get("create_date").toString();
				createTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S")
						.parseDateTime(createTime).toString("yyyy年MM月dd日 HH:mm:ss");
				map.put("createTime", createTime);
				list.set(i, map);
			}
		}
		return list;
	}

	@Override
	public AjaxJson savePartnerDevelop(PartnerDevelopDTO partnerDevelop) {
		AjaxJson json = new AjaxJson();
		PartnerDevelopEntity entity = null;
		try {
			if(partnerDevelop.getId() != 0){
				entity = this.get(PartnerDevelopEntity.class, partnerDevelop.getId());
			}
			if(entity == null){
				entity = new PartnerDevelopEntity();
			}
			entity.setUserId(partnerDevelop.getCourierId());
			entity.setAccountAmount(partnerDevelop.getAccountAmount());
			BankEntity bank=getEntity(BankEntity.class, Integer.parseInt(partnerDevelop.getBankId()));
			entity.setBankId(partnerDevelop.getBankId());
			if (bank!=null) {
				entity.setBank(bank.getName());
			}
			entity.setBankcardPhoto(partnerDevelop.getBankcardPhoto());
			entity.setBankOfDeposit(partnerDevelop.getBankOfDeposit());
			entity.setCardHolder(partnerDevelop.getCardHolder());
			entity.setCardNo(partnerDevelop.getCardNo());
			entity.setCityCode(partnerDevelop.getCityCode());
			entity.setContractNo(partnerDevelop.getContractNo());
			entity.setContractPhoto(partnerDevelop.getContractPhoto());
			entity.setDoneDate(DateUtils.getDate());
			entity.setIdCard(partnerDevelop.getIdCard());
			entity.setIdcardPhotoBack(partnerDevelop.getIdcardPhotoBack());
			entity.setIdcardPhotoFront(partnerDevelop.getIdcardPhotoFront());
			entity.setPartnerName(partnerDevelop.getPartnerName());
			entity.setPhone(partnerDevelop.getPhone());
			entity.setBusinessPhone(partnerDevelop.getBusinessPhone());
			entity.setProvCode(partnerDevelop.getProvCode());
			entity.setRealName(partnerDevelop.getRealName());
			entity.setRemark(partnerDevelop.getRemark());
			entity.setState(1);
			if (entity.getCreateDate()!=null||StringUtil.isEmpty(entity.getCreateDate())) {
				entity.setCreateDate(DateUtils.getDate());
			}
			//保存合作商开拓记录
			saveOrUpdate(entity);	
			json.setMsg("保存合作商开拓记录成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存合作商开拓记录失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}

	@Override
	public PartnerDevelopDTO getPartnerDevelopDtl(Integer id) {
		PartnerDevelopEntity entity = null;
		entity = this.get(PartnerDevelopEntity.class, id);
		PartnerDevelopDTO dto = null;
		if (entity != null) {
			dto = new PartnerDevelopDTO();
			dto.setCourierId(entity.getUserId());
			if (entity.getAccountAmount()!=null) {
				dto.setAccountAmount(entity.getAccountAmount());
			}else {
				dto.setAccountAmount(0);
			}
			dto.setBank(entity.getBank());
			dto.setBankId(entity.getBankId());
			dto.setBankcardPhoto(entity.getBankcardPhoto());
			dto.setBankOfDeposit(entity.getBankOfDeposit());
			dto.setCardHolder(entity.getCardHolder());
			dto.setCardNo(entity.getCardNo());
			dto.setCityCode(entity.getCityCode());
			dto.setContractNo(entity.getContractNo());
			dto.setId(entity.getDevelopId());
			dto.setIdCard(entity.getIdCard().toString());
			dto.setIdcardPhotoBack(entity.getIdcardPhotoBack());
			dto.setIdcardPhotoFront(entity.getIdcardPhotoFront());
			dto.setPartnerName(entity.getPartnerName());
			dto.setPhone(entity.getPhone());
			dto.setBusinessPhone(entity.getBusinessPhone());
			dto.setProvCode(entity.getProvCode());
			dto.setRealName(entity.getRealName());
			dto.setRemark(entity.getRemark());
			dto.setContractPhoto(entity.getContractPhoto());
			dto.setState(entity.getState().toString());
			// 拼装合同url,合同图片有4张
			if (entity.getContractPhoto() != null
					&& (!StringUtil.isEmpty(entity.getContractPhoto()))) {
				String[] urls = entity.getContractPhoto().split(",");
				if (urls != null && urls.length >= 1) {
					dto.setContractImgUrl0(urls[0]);
					if (urls.length >= 2) {
						dto.setContractImgUrl1(urls[1]);
						if (urls.length >= 3) {
							dto.setContractImgUrl2(urls[2]);
							if (urls.length >= 4) {
								dto.setContractImgUrl3(urls[3]);
							}
						}
					}
				}
			}
		}
		return dto;
	}
	
	@Override
	public List<String> getSignAmount() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT account_count ");
		query.append(" from agent_account_price order by account_count asc ");
		List<Map<String, Object>> list = findForJdbc(query.toString());
		List<String> amountList=null;
		if (!list.isEmpty()) {
			amountList=new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				amountList.add(list.get(i).get("account_count").toString());
			}
		}
		return amountList;
	}
}

