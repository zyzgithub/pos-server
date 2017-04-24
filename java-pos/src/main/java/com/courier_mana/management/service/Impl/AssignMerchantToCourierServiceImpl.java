package com.courier_mana.management.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.management.service.AssignMerchantToCourierService;

@Service
public class AssignMerchantToCourierServiceImpl extends CommonServiceImpl implements AssignMerchantToCourierService {
	private final static Logger logger = LoggerFactory.getLogger(AssignMerchantToCourierServiceImpl.class);
	
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@Override
	public List<Map<String, Object>> getAssignmentData(Integer courierId) {
		logger.info("Invoke method: getAssignmentData， params: courierId - {}", courierId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 获取管辖的快递员列表
		 */
		List<Integer> courierIds = this.getManageCouriersId(courierId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.id courierId, u.username courierName, cmd.merchant_id merchantId, cmd.title merchantName ");
		sql.append(" FROM `user` u LEFT JOIN ( ");
		sql.append(" 	SELECT cm.*, m.title ");
		sql.append(" 	FROM 0085_courier_merchant cm, merchant m ");
		sql.append(" 	WHERE cm.merchant_id = m.id AND m.is_delete = 0 ");
		sql.append(" )cmd ON cmd.courier_id = u.id ");
		sql.append(" WHERE u.id IN( ");
		sql.append(StringUtils.join(courierIds, ","));
		sql.append(" ) ");
		sql.append(" ORDER BY u.username, u.id, cmd.merchant_id ");
		
		logger.debug("Inside method: getAssignmentData, SQL: {}", sql.toString());
		
		/**
		 * 获取原始的快递员店铺关系数据
		 */
		List<Map<String, Object>> sourceAssignmentData = this.findForJdbc(sql.toString());
		
		/**
		 * 返回处理过的数据
		 */
		return this.getAssignmentDataResultList(sourceAssignmentData);
	}
	
	@Override
	public List<Map<String, Object>> getMerchantList(Integer courierId){
		logger.info("Invoke method: getMerchantList， params: courierId - {}", courierId);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT m.id merchantId, m.title merchantName ");
		sql.append(" 	,IFNULL((SELECT 1 FROM 0085_courier_merchant cm WHERE cm.courier_id = corg.courier_id AND m.id = cm.merchant_id),0)isSelected ");
		sql.append(" FROM 0085_courier_org corg, 0085_merchant_org morg, merchant m ");
		sql.append(" WHERE corg.org_id = morg.org_id AND morg.merchant_id = m.id AND m.is_delete = 0 ");
		sql.append(" 	AND corg.courier_id =  ");
		sql.append(courierId);
		sql.append(" ORDER BY m.id ");
		
		logger.debug("Inside method: getMerchantList, SQL: {}", sql.toString());
		
		return this.findForJdbc(sql.toString());
	}

	/**
	 * 将原始的快递员-店铺关系信息组装成需要的结构
	 * @param source	原始的快递员-店铺关系信息
	 * @return	返回页面需要的快递员-店铺关系数据, 包括:
	 * 			courierId		快递员ID
	 * 			courierName		快递员姓名
	 * 			merchants		已分配给此快递员的店铺列表
	 * 				merchantId		店铺ID
	 * 				merchantName	店铺名称
	 */
	private List<Map<String, Object>> getAssignmentDataResultList(List<Map<String, Object>> sourceList){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();	//用于返回结果的List
		Map<String, Object> courierData = null;			//保存每个快递员-店铺关系的Map
		List<Map<String, Object>> merchantList = null;	//存放与快递员有关店铺的List
		Map<String, Object> merchantInfo = null;		//店铺信息Map
		Integer preCourierId = null;						//标志位, 快递员ID
		/**
		 * 遍历原始数据
		 */
		for(Map<String, Object> record: sourceList){
			Integer courierId = Integer.parseInt(record.get("courierId").toString());
			/**
			 * 对比记录中的快递员ID是否和标志位(preCourierId)相同
			 * · 和标志位不同就在resultList中添加一个item(一个快递员店铺分配信息)
			 */
			if(!courierId.equals(preCourierId)){
				/**
				 * 为快递员构建新的数据项
				 */
				courierData = new HashMap<String, Object>();
				courierData.put("courierId", courierId);
				courierData.put("courierName", record.get("courierName"));
				merchantList = new ArrayList<Map<String, Object>>();
				courierData.put("merchants", merchantList);
				resultList.add(courierData);
				/**
				 * 怎么能忘记更新标志位呢?
				 */
				preCourierId = courierId;
			}
			/**
			 * 将店铺信息添加到快递员数据中
			 * 先判断记录中是否包含商家信息(因为存在快递员没有配置店铺的可能性)
			 * 如果没有商家信息，就没必要向merchantList添加数据项
			 */
			if(record.get("merchantId") != null){
				merchantInfo = new HashMap<String, Object>();
				merchantInfo.put("merchantId", record.get("merchantId"));
				merchantInfo.put("merchantName", record.get("merchantName"));
				merchantList.add(merchantInfo);
			}
		}
		
		return resultList;
	}
	
	@Override
	public List<Map<String, Object>> updateAssignmentRecord(Integer courierId, Integer[] merchantIds, Integer loginedCourierId){
		logger.info("Invoke method: updateAssignmentRecord， params: courierId - {}, merchantIds - {}", courierId, merchantIds);
		
		/**
		 * 先检查参数必要的参数
		 */
		if(merchantIds == null){
			throw new IllegalArgumentException("merchantIds不能为空");
		}
		if(courierId == null){
			throw new IllegalArgumentException("courierId不能为空");
		}
		
		/**
		 * 将前端传来的courierIds转换为Set
		 */
		Set<Integer> merchantIdsSelected = new HashSet<Integer>(Arrays.asList(merchantIds));
		
		/**
		 * 查询快递员已绑定的店铺ID
		 */
		List<Integer> merchantIdsInDB = this.getMerchantIdsByCourierId(courierId);
		
		/**
		 * 遍历已经绑定到此商家的ID, 并与前端选中的ID作比较
		 */
		for(Integer merchantIdInDB: merchantIdsInDB){
			/**
			 * 如果ID已绑定, 而且前端选定了
			 * 则不需要对数据库作任何操作
			 * 此ID从Set中删除(Set中的ID在后面会使用它进行数据库的插入操作)
			 */
			if(merchantIdsSelected.contains(merchantIdInDB)){
				merchantIdsSelected.remove(merchantIdInDB);
			}
			/**
			 * 如果ID已绑定, 但前端没有选定
			 * 则说明要对此ID进行解绑操作
			 */
			else{
				this.deleteOneMerchantAssignmentRecord(merchantIdInDB, courierId);
			}
		}
		
		/**
		 * 遍历处理后的Set
		 * 并将余下的ID绑定到指定店铺
		 */
		for(Integer merchantId: merchantIdsSelected){
			this.insertOneMerchantAssignmentRecord(merchantId, courierId);
		}
		
		/**
		 * 返回更新后的快递员和店铺之间分配情况数据
		 */
		return this.getAssignmentData(loginedCourierId);
	}

	@Override
	public Integer deleteOneMerchantAssignmentRecord(Integer merchantId, Integer courierId) {
		logger.info("Invoke method: deleteOneMerchantAssignmentRecord， params: courierId - {}, merchantId - {}", courierId, merchantId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM 0085_courier_merchant WHERE merchant_id = ? AND courier_id = ? ");
		
		logger.debug("Inside method: deleteOneMerchantAssignmentRecord, SQL: {}", sql.toString());
		
		return this.executeSql(sql.toString(), merchantId, courierId);
	}

	@Override
	public Integer insertOneMerchantAssignmentRecord(Integer merchantId, Integer courierId) {
		logger.info("Invoke method: insertOneMerchantAssignmentRecord， params: courierId - {}, merchantId - {}", courierId, merchantId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO 0085_courier_merchant (courier_id, merchant_id) VALUES(?,?) ");
		
		logger.debug("Inside method: insertOneMerchantAssignmentRecord, SQL: {}", sql.toString());
		
		return this.executeSql(sql.toString(), courierId, merchantId);
	}
	
	/**(OvO)
	 * 获取指定快递员已绑定的店铺的ID列表
	 * @param courierId	快递员ID
	 * @return
	 */
	private List<Integer> getMerchantIdsByCourierId(Integer courierId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT merchant_id ");
		sql.append(" FROM 0085_courier_merchant ");
		sql.append(" WHERE courier_id = ");
		sql.append(courierId);
		return this.findListbySql(sql.toString());
	}
	
	/**(OvO)
	 * 获取管辖的"飞行员"ID
	 * @param courierId	快递员ID
	 * @return	"飞行员"ID
	 */
	private List<Integer> getManageCouriersId(Integer courierId){
		/**
		 * 获取管辖范围的所有orgId
		 */
		List<Integer> orgIds = this.courierOrgService.getManageOrgIds(courierId);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT u.id ");
		sql.append(" FROM 0085_courier_org corg, `user` u, 0085_courier_position cp ");
		sql.append(" WHERE u.id = corg.courier_id AND u.is_delete = 0 ");
		sql.append(" 	AND cp.courier_id = u.id AND cp.position_id = 2 ");
		sql.append(" 	AND corg.org_id IN ( ");
		sql.append(StringUtils.join(orgIds, ","));
		sql.append(" ) ");
		
		return findListbySql(sql.toString());
	}
}
