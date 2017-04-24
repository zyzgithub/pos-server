package com.wm.service.impl.merchant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.AppTypeConstants;
import com.jpush.JPush;
import com.wm.dto.merchant.MerchantCashflowDto;
import com.wm.entity.merchant.MerchantCashflowEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantMultiAccoServiceI;

@Service("merchantMultiAccoServiceI")
@Transactional
public class MerchantMultiAccoServiceImpl extends CommonServiceImpl implements MerchantMultiAccoServiceI {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FlowServiceI flowServiceI;
	
	@Override
	public List<Map<String, Object>> getALLStoresPageList(Integer merchantId, Integer page, Integer rows){
		List<Map<String, Object>> totalList = new ArrayList<Map<String,Object>>();
		//获取主店和分店列表
		List<Map<String, Object>> list = this.getALLStoresAccountPageList(merchantId, page, rows);
		if(CollectionUtils.isNotEmpty(list)){
			for(Map<String, Object> m :list ){
				Map<String, Object> storeMap = new HashMap<String, Object>();
				storeMap.put("id", m.get("id"));
				storeMap.put("title", m.get("title"));
				storeMap.put("address", m.get("address"));
				storeMap.put("userId", m.get("userId"));
				totalList.add(storeMap);
			}
		}
		return totalList;
	}
	
	@Override
	public List<Map<String, Object>> getALLStoresAccountPageList(Integer merchantId, Integer page, Integer rows){
		List<Map<String, Object>> totalList = new ArrayList<Map<String,Object>>();
		//总店
		MerchantEntity mer = this.get(MerchantEntity.class, merchantId);
		if(mer==null){
			return null;
		}
		Map<String, Object> mainStoreMap = new HashMap<String, Object>();
		mainStoreMap.put("id", mer.getId());
		mainStoreMap.put("title", mer.getTitle());
		mainStoreMap.put("userId", mer.getWuser().getId());
		mainStoreMap.put("address", mer.getAddress());
		mainStoreMap.put("money", mer.getWuser().getMoney());
		totalList.add(mainStoreMap);
		
		//分店
		List<Map<String, Object>> branchList = this.getALLBranchStoresPageList(merchantId, page, rows);
		totalList.addAll(branchList);
		return totalList;
	}
	
	/**
	 * 得到所有的分店列表，包括余额为0的(分页)
	 */
	public List<Map<String, Object>> getALLBranchStoresPageList(Integer merchantId, Integer page, Integer rows){
		String sql = " select m.id, m.user_id userId, m.title, m.address, u.money from  0085_merchant_multiaccount mm, merchant m, user u "+
					 " where m.id = mm.branchstore_id and m.user_id = u.id and mm.mainstore_id = ? order by m.id";
		List<Map<String, Object>> branchList = this.findForJdbcParam(sql, page, rows, merchantId);
		return branchList;
	}
	
	/**
	 * 得到分店总数，包括余额为0的(不分页)
	 */
	@Override
	public Long getBranchStoresCount(Integer merchantId){
		String sql = " select count(*) from  0085_merchant_multiaccount mm, merchant m, user u "
				+ "where m.id = mm.branchstore_id and m.user_id = u.id and mm.mainstore_id = ? ";
		Long listSize = this.getCountForJdbcParam(sql, new Object[]{merchantId});
		return listSize;
	}
	
	@Override
	public List<Map<String, Object>> getBranchStoresPageList(Integer merchantId, Integer page, Integer rows){
		String sql = " select m.id, m.title, m.address, u.money from  0085_merchant_multiaccount mm, merchant m, user u "+
					 " where m.id = mm.branchstore_id and m.user_id = u.id and mm.mainstore_id = ? and u.money<>'0' order by m.id";
		List<Map<String, Object>> branchList = this.findForJdbcParam(sql, page, rows, merchantId);
		BigDecimal money = null;
		for(Map<String, Object> map : branchList){
			money = new BigDecimal(map.get("money").toString());
			map.put("money", money.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
		}
		return branchList;
	}
	
	
	@Override
	public List<Map<String, Object>> getMerchantCashflowPageList(Integer merchantId, Integer page, Integer rows){
		String sql = " select m1.title inMerchantName, mc.money, m2.title outMerchantName, mc.create_time createTime from 0085_merchant_cashflow mc, merchant m1, merchant m2 "+
				 	 " where mc.in_merchant_id = m1.id and mc.out_merchant_id = m2.id and mc.in_merchant_id= ? order by mc.create_time desc";
		List<Map<String, Object>> merchantCashflowList = this.findForJdbcParam(sql, page, rows, merchantId);
		if(CollectionUtils.isNotEmpty(merchantCashflowList)){
			for(Map<String, Object> m :merchantCashflowList ){
				m.put("create_time", DateUtils.timestamptoStr((Timestamp) m.get("create_time")));
			}
		}
		return merchantCashflowList;
	}
	
	@Override
	public void addMerchantCashflow(MerchantCashflowDto dto){
		try {
			BigDecimal branchTotal = new BigDecimal(0);
			Integer mainStoreId = dto.getMainStoreId();
			MerchantCashflowEntity[] branchDetail = dto.getBranchDetail();
			
			for(MerchantCashflowEntity branchStoreDetail : branchDetail){
				branchStoreDetail.setInMerchantId(mainStoreId);
				BigDecimal money = branchStoreDetail.getMoney();
				this.save(branchStoreDetail); //保存转入记录
				branchTotal = branchTotal.add(money); //转出分店余额总额
				
				MerchantEntity branchMerchant = this.get(MerchantEntity.class, branchStoreDetail.getOutMerchantId());
				flowServiceI.branchStoreAccountOut(branchMerchant.getWuser().getId(), money.setScale(2, RoundingMode.HALF_UP)); //分店转出余额并插入流水
			}
			logger.info("相加余额="+ branchTotal +"，页面传入总额="+ dto.getTotalMoney());
			if(branchTotal.equals(dto.getTotalMoney())){
				MerchantEntity mainMerchant = this.get(MerchantEntity.class, dto.getMainStoreId());
				flowServiceI.mainStoreAccountIn(mainMerchant.getWuser().getId(), branchTotal.setScale(2, RoundingMode.HALF_UP));  //主店转入余额并插入流水
			} else {
				throw new RuntimeException("[商家连锁账户,转入主店余额]插入流水失败");
			}
		} catch (Exception e) {
			logger.error("分店余额转入到主店余额失败，错误信息：" + e.getMessage());
			throw new RuntimeException("操作失败", e);
		}
		
	}

	@Override
	public void jpushMsgToMerchant(Integer userId, String title, String content) {
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("appType", AppTypeConstants.APP_TYPE_MERCHANT);
		String result = JPush.push(new String[]{userId.toString()}, title, content, null, extras);
		logger.info("极光推送文本消息给商家，返回结果："+result);
	}
	
}
