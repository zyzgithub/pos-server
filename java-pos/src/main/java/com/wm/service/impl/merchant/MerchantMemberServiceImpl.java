package com.wm.service.impl.merchant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.merchant.TomMerchantMemberDiscountEntity;
import com.wm.entity.merchant.TomMerchantMemberDiscountLogEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchant.MerchantMemberServiceI;

@Service("merchantMemberService")
@Transactional
public class MerchantMemberServiceImpl extends CommonServiceImpl implements MerchantMemberServiceI {
	Logger logger = Logger.getLogger(MerchantMemberServiceImpl.class);
	
	@Override
	public Map<String, Object> getMemberInfo(Integer merchantId) {
		String sql = "SELECT COUNT(id) quantity,convert(SUM(money)/100,decimal(11,2)) money "
				+ "FROM tom_merchant_member_info "
				+ "WHERE merchant_id=? ";
		Map<String, Object> map = this.findOneForJdbc(sql, merchantId);
		Integer quantity = Integer.parseInt(map.get("quantity").toString());
		if(map==null || map.size()==0 || quantity==0){
			map = new HashMap<String, Object>();
			map.put("quantity", 0);
			map.put("money", 0.00);
		}
		return map;
	}

	@Override
	public Map<String, Object> getMemberRule(Integer merchantId){
		String sql = "SELECT merchant_discount,min_recharge/100 min_recharge "
				+ "FROM tom_merchant_member_discount "
				+ "WHERE merchant_id=?";
		Map<String, Object> map = this.findOneForJdbc(sql, merchantId);
		return map;
	}
	
	@Override
	public void createOrUpdateMemberRule(Integer merchantId, Integer userId, Double money,
			Integer discount) {
		//查询商家是否设置会员规则
		TomMerchantMemberDiscountEntity discountEntity = this.findUniqueByProperty(TomMerchantMemberDiscountEntity.class, "merchantId", merchantId);
		money *= 100;
		Integer minRecharge = money.intValue();
		if(discountEntity!=null){		//更新会员规则
			Integer id = discountEntity.getId();
			TomMerchantMemberDiscountEntity tmmde = this.get(TomMerchantMemberDiscountEntity.class, id);
			tmmde.setMerchantDiscount(discount);
			tmmde.setMinRecharge(minRecharge);
			this.saveOrUpdate(tmmde);
		}else{		//添加会员规则
			TomMerchantMemberDiscountEntity tmmde = new TomMerchantMemberDiscountEntity();
			tmmde.setMinRecharge(minRecharge);
			tmmde.setMerchantId(merchantId);
			tmmde.setMerchantDiscount(discount);
			this.save(tmmde);
		}
		//查询商家会员总人数和总余额
		Map<String, Object> member = this.getMemberInfo(merchantId);
		Integer quantity = Integer.parseInt(member.get("quantity").toString());
		//添加修改记录
		TomMerchantMemberDiscountLogEntity tmmdel = new TomMerchantMemberDiscountLogEntity();
		tmmdel.setMinRecharge(minRecharge);
		tmmdel.setMerchantDiscount(discount);
		tmmdel.setMerchantId(merchantId);
		tmmdel.setUpdateTime(new Date());
		tmmdel.setUserId(userId);
		if(quantity!=0){
			Double sum = Double.parseDouble(member.get("money").toString());
			Integer sumMoney = sum.intValue()*100;
			tmmdel.setSumMoney(sumMoney);//商家会员总余额，以分为单位
		}
		tmmdel.setMembers(quantity);
		this.save(tmmdel);
	}

	@Override
	public List<Map<String, Object>> getMemberList(Integer merchantId, Integer page, Integer rows) {
		String sql = "SELECT user_id,convert(money/100,decimal(11,2)) money,DATE_FORMAT(create_time,'%Y/%m/%d') create_time  "
				+ "FROM tom_merchant_member_info "
				+ "WHERE merchant_id=? "
				+ "ORDER BY create_time DESC";
		List<Map<String, Object>> list = this.findForJdbcParam(sql, page, rows, merchantId);
		if(list!=null && list.size()>0){
			for(int i=list.size()-1;i>=0;i--){
				Map<String, Object> map = list.get(i);
				Integer userId = Integer.parseInt(map.get("user_id").toString());
				//获得用户头像和姓名
				WUserEntity user = this.get(WUserEntity.class, userId);
				if(user!=null){
					String photoUrl = user.getPhotoUrl();
					String userName = user.getNickname();
					map.put("photoUrl", photoUrl);
					map.put("userName", userName);
				}else{
					list.remove(i);
					logger.error("未找到userId:[" + userId + "]相关的用户信息！！！");
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getMemberChargeMoneyRecords(
			Integer merchantId, Integer page, Integer rows) {
		String sql = "SELECT user_id,FROM_UNIXTIME(create_time,'%Y-%m-%d %H:%i:%s') update_time,money charge_money "
				+ "FROM flow "
				+ "WHERE merchant_id=? "
				+ "AND action='merchantRecharge' "
				+ "ORDER BY create_time DESC";
		List<Map<String, Object>> list = this.findForJdbcParam(sql, page, rows, merchantId);
		if(list!=null && list.size()>0){
			for(int i=list.size()-1;i>=0;i--){
				Map<String, Object> map = list.get(i);
				Integer userId = Integer.parseInt(map.get("user_id").toString());
				//获得用户头像和姓名
				WUserEntity user = this.get(WUserEntity.class, userId);
				if(user!=null){
					String photoUrl = user.getPhotoUrl();
					String userName = user.getUsername();
					map.put("photoUrl", photoUrl);
					map.put("userName", userName);
				}else{
					list.remove(i);
					logger.error("未找到userId:[" + userId + "]相关的用户信息！！！");
				}
			}
		}
		return list;
	}

	
}
