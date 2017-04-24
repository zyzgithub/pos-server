package com.courier_mana.statistics.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.base.config.BaseConfig;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.remote.RemoteClient;
import com.courier_mana.statistics.model.WarehouseReportAdmin;
import com.courier_mana.statistics.service.WarehouseReportService;
import com.courier_mana.statistics.vo.CourierManaCallLogVo4WarehouseReportDetail;
import com.courier_mana.statistics.vo.WarehouseReportDetailVo;
import com.courier_mana.statistics.vo.WarehouseReportVo;
import com.wm.util.SqlUtils;

@Service
public class WarehouseReportServiceImpl extends CommonServiceImpl implements WarehouseReportService {
	private Logger logger = LoggerFactory.getLogger(WarehouseReportServiceImpl.class);
	@Autowired
	private BaseConfig baseConfig;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<WarehouseReportVo> getWarehouseReportData(long userId, SearchVo timeType, int page, int rows) {
		String warehouseIdsStr = this.getWarehousesIdStr(userId);
		if(warehouseIdsStr == null || warehouseIdsStr.equals("")){
			return null;
		}
		return this.getWarehouseReportData(warehouseIdsStr, timeType, page, rows);
	}

	@Override
	public List<WarehouseReportDetailVo> getWarehouseReportDetail(int warehouseId, SearchVo timeType, int page, int rows) {
		String timeWhereInDate4Order = SqlUtils.getDateTimeWhere4SQL(timeType, "o.create_time");
		StringBuilder sql = new StringBuilder(" SELECT o.merchant_name merchantName, o.merchant_id merchantId, o.target_phone phone, o.target_user merchantUserName, ");
		sql.append(" 	COUNT(CASE WHEN o.time_mark = '0' THEN 1 ELSE NULL END) appOrders, ");
		sql.append(" 	COUNT(CASE WHEN o.time_mark = '1' THEN 1 ELSE NULL END) h5Orders, ");
		sql.append(" 	COUNT(1) totalOrders, ");
		sql.append(" 	COUNT(CASE WHEN 1 AND u.create_time >= CURDATE() THEN 1 ELSE NULL END) newOrder, ");
		sql.append(" 	SUM(o.actually_price) totalIncome ");
		sql.append(" FROM supply_chain_console.supply_chain_order o, supply_chain_console.user_personal_attachment u ");
		sql.append(" WHERE 1 ");
		sql.append(timeWhereInDate4Order);
		sql.append(" AND warehouse_id = ? AND merchant_order_state > 1 AND u.user_id = o.user_id ");
		sql.append(" GROUP BY o.merchant_id ");
		sql.append(" ORDER BY totalIncome DESC ");
		List<WarehouseReportDetailVo> merchantList = this.findObjForJdbc(sql.toString(), page, rows, WarehouseReportDetailVo.class, warehouseId);
		Map<Long, WarehouseReportDetailVo> index = this.getRankThenScaleTotalIncomeAndCalculateAvgPriceFinallyMakeAIndex(merchantList, page, rows);
		this.fillDials4WarehouseReportDetail(index);
		return merchantList;
	}

	@Override
	public int recordCallLog(long merchant, long userId, String phone) {
		String sql = " INSERT INTO courier_mana_call_log(merchant_id, caller, phone_num) VALUES(?, ?, ?)";
		Long result = this.insertBySql(sql, merchant, userId, phone);
		return result.intValue();
	}

	@Override
	public void insertWarehouseReportAdmin(long userId, String userName, String phone) {
		WarehouseReportAdmin admin = new WarehouseReportAdmin(userId, userName, phone);
		this.mongoTemplate.insert(admin);
	}

	@Override
	public boolean ifUserAWarehouseReportAdmin(long userId) {
		Query query = Query.query(Criteria.where("userId").is(userId));
		long count = this.mongoTemplate.count(query, WarehouseReportAdmin.class);
		return count > 0;
	}

	/**
	 * 根据userId获取所能访问的仓库ID
	 * @param userId	用户ID
	 * @return
	 */
	private String getWarehousesIdStr(long userId){
		// 213107 杜耀红 18675885525
		// 225065 钟杰 13802724327
		// 213133 余纪容 18818873952
		// 940230 李务敬 15820266319
		// 723069 聂勇华 18925088111
		// 59271 翟超锋 15889658513
		// 385842 李勇 13711157025
		// 89621 陈进聪 13751893635
		// 497603 唐贵  18664612007
		// 191427  王宇光 18802078269
		// 71451 金沐晓  13823175065
		// 129115  方超  18682971345
		// 251882 吴莉琴  13924295511
//		Long[] superUser = new Long[]{59331l,169344l,213107l,225065l,213133l,940230l,723069l,59271l,385842l,89621l,497603l,191427l,71451l,129115l,251882l};
		boolean admin = this.ifUserAWarehouseReportAdmin(userId);
		
//		List<Long> superUserList = Arrays.asList(superUser);
		if(admin){
			return "all";
		}
		StringBuilder url = new StringBuilder(this.baseConfig.SUPPLY_ERP_HOST);
		// 假设用户不是仓管, 先去查询已绑定的仓库的管理员用户ID
		String warehouseUserId = this.getBoundWarehouseHandlerId(userId);
		if(warehouseUserId != null){
			userId = Long.parseLong(warehouseUserId);
			logger.info("查询到快递员与仓库的绑定关系, 正在用" + userId + "的身份查询仓库信息");
		}
		url.append("/warehouse/getWarehouseIds?userId=");
		url.append(userId);
		String response = RemoteClient.get(url.toString());
		JSONObject json = JSONObject.parseObject(response);
		int code = json.getIntValue("code");
		if(code != 0){
			return null;
		}
		return json.getJSONObject("response").getString("string");
	}
	
	/**
	 * 假设用户不是仓库管理员就去获得已绑定的仓库的管理员ID
	 * @param userId
	 * @return
	 */
	private String getBoundWarehouseHandlerId(long userId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT warehouse_id FROM `0085_driver_warehouse` WHERE driver_id = ");
		sql.append(userId);
		String result = this.findOneForJdbc(sql.toString(), String.class);
		return result;
	}
	
	/**
	 * 从数据库获取指定仓库的信息
	 * @param warehouseIdsStr	若干个仓库ID, 使用逗号分隔
	 * @param timeType			时间参数
	 * @param page				页数
	 * @param rows				页宽
	 * @return
	 */
	private List<WarehouseReportVo> getWarehouseReportData(String warehouseIdsStr, SearchVo timeType, int page, int rows) {
		String timeWhereInDate4Order = SqlUtils.getDateTimeWhere4SQL(timeType, "o.create_time");
		String timeWhereInDate4User = SqlUtils.getDateTimeWhere4SQL(timeType, "u.create_time");
		StringBuilder sql = new StringBuilder(" SELECT w.`name` warehouseName, w.id warehouseId, (SELECT nickname FROM waimai.`user` u WHERE u.id = w.`handler`) handlerName, ");
		sql.append(" 	IFNULL(details.app,0) appOrders, ");
		sql.append(" 	IFNULL(h5,0) h5Orders,  ");
		sql.append(" 	IFNULL(totalUser,0) totalUsers, ");
		sql.append(" 	IFNULL(totalOrder,0) totalOrders, ");
		sql.append(" 	IFNULL(newOrder,0) newUserOrders, ");
		sql.append(" 	IFNULL(totalCurrent,0) totalIncome ");
		sql.append(" FROM supply_chain_console.supply_chain_warehouse w LEFT JOIN ( ");
		sql.append(" 	SELECT o.warehouse_id, COUNT(CASE WHEN time_mark = '0' THEN 1 ELSE NULL END) app ");
		sql.append(" 		,COUNT(CASE WHEN time_mark = '1' THEN 1 ELSE NULL END) h5 ");
		sql.append(" 		,COUNT(DISTINCT o.user_id) totalUser ");
		sql.append(" 		,COUNT(1) totalOrder ");
		sql.append(" 		,COUNT(CASE WHEN 1 ");
		sql.append(timeWhereInDate4User);
		sql.append(" THEN 1 ELSE NULL END) newOrder ");
		sql.append(" 		,SUM(o.actually_price) totalCurrent ");
		sql.append(" 	FROM supply_chain_console.supply_chain_order o, supply_chain_console.user_personal_attachment u ");
		sql.append(" 	WHERE o.user_id = u.user_id ");
		sql.append(" 		AND o.supply_order_state >= 2 ");
		sql.append(timeWhereInDate4Order);
		sql.append(" 	GROUP BY o.warehouse_id ");
		sql.append(" )details ON w.id = details.warehouse_id ");
		sql.append(" WHERE w.scope_type <> 0 ");
		if(!"all".equals(warehouseIdsStr)){
			sql.append(" AND  w.id IN (");
			sql.append(warehouseIdsStr);
			sql.append(") ");
		}
		sql.append(" ORDER BY totalIncome DESC, w.id ");
		List<WarehouseReportVo> warehouseReportList = this.findObjForJdbc(sql.toString(), page, rows, WarehouseReportVo.class);

		// 计算未支付数据
		this.getWarehouseReportNoPayData(warehouseReportList,warehouseIdsStr,timeType, page, rows);

		// 计算客单价
		this.getRankThenScaleTotalIncomeAndCalculateAvgPrice(warehouseReportList, page, rows);
		
		return warehouseReportList;
	}
	
	/**
	 * 添加"排名"<br>
	 * 处理总销售额<br>
	 * 计算客单价<br>
	 * 客单价=总额/用户数
	 * @param warehouseReportList 仓库数据
	 * @param page 页码
	 * @param rows 页面容量
	 */
	private void getRankThenScaleTotalIncomeAndCalculateAvgPrice(List<WarehouseReportVo> warehouseReportList, int page, int rows){
		// 计算第一条记录的"排名"
		int rank = (page - 1) * rows + 1;
		for(WarehouseReportVo item: warehouseReportList){
			// 添加"排名"
			item.setRank(rank ++);
			
			BigDecimal totalIncome = new BigDecimal(item.getTotalIncome());
			
			// 计算客单价
			int totalUsers = item.getTotalUsers();
			if(totalUsers == 0){
				item.setAvgPrice("0.0");
			} else {
				BigDecimal avgPrice = totalIncome.divide(new BigDecimal(totalUsers), 2, BigDecimal.ROUND_HALF_UP);
				item.setAvgPrice(avgPrice.toString());
			}
			
			// 处理总销售额
			BigDecimal scaledTotalIncome = totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
			item.setTotalIncome(scaledTotalIncome.doubleValue());
		}
	}
	
	/**
	 * 添加"排名"<br>
	 * 处理总销售额<br>
	 * 计算客单价<br>
	 * 客单价=总额/订单数<br>
	 * 而且建立一个merchantId和warehouseReportDetail数据的索引
	 * @param warehouseReportDetailList 仓库详情数据
	 * @param page 页码
	 * @param rows 页面容量
	 */
	private Map<Long, WarehouseReportDetailVo> getRankThenScaleTotalIncomeAndCalculateAvgPriceFinallyMakeAIndex(List<WarehouseReportDetailVo> warehouseReportDetailList, int page, int rows){
		Map<Long, WarehouseReportDetailVo> result = new HashMap<Long, WarehouseReportDetailVo>(rows);
		// 计算第一条记录的"排名"
		int rank = (page - 1) * rows + 1;
		for(WarehouseReportDetailVo item: warehouseReportDetailList){
			long merchantId = item.getMerchantId();
			result.put(merchantId, item);
			
			// 添加排名
			item.setRank(rank ++); 
			
			BigDecimal totalIncome = new BigDecimal(item.getTotalIncome());
			
			// 计算客单价
			int totalOrders = item.getTotalOrders();
			if(totalOrders == 0){
				item.setAvgPrice("0.0");
			} else {
				BigDecimal avgPrice = totalIncome.divide(new BigDecimal(totalOrders), 2, BigDecimal.ROUND_HALF_UP);
				item.setAvgPrice(avgPrice.toString());
			}
			
			// 处理总销售额
			BigDecimal scaledTotalIncome = totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
			item.setTotalIncome(scaledTotalIncome.doubleValue());
		}
		return result;
	}
	
	/**
	 * 填充回访次数
	 * @param index
	 */
	private void fillDials4WarehouseReportDetail(Map<Long, WarehouseReportDetailVo> index){
		Set<Long> merchantIds = index.keySet();
		
		if(merchantIds.isEmpty()){
			// 如果merchantIds为空, 什么都不做
			return;
		}
		
		// 查出索引中的商家的回访情况
		String merchantIdsStr = StringUtils.join(merchantIds, ",");
		StringBuilder sql = new StringBuilder(" SELECT merchant_id merchantId, COUNT(1) dials ");
		sql.append(" FROM courier_mana_call_log ");
		sql.append(" WHERE merchant_id IN (");
		sql.append(merchantIdsStr);
		sql.append(") ");
		sql.append(" GROUP BY merchant_id ");
		List<CourierManaCallLogVo4WarehouseReportDetail> callLogList = this.findObjForJdbc(sql.toString(), CourierManaCallLogVo4WarehouseReportDetail.class);
		
		// 将回访情况写入到仓库详情报表的Vo里
		for(CourierManaCallLogVo4WarehouseReportDetail item: callLogList){
			long merchantId = item.getMerchantId();
			WarehouseReportDetailVo vo = index.get(merchantId);
			// 正常情况下应该不会空指针吧
			vo.setDials(item.getDials());
		}
	}


	/**
	 * 计算未支付数据
	 * @param warehouseReportList
	 * @param warehouseIdsStr
	 * @param timeType
	 * @param page
	 * @param rows
	 */
	private void getWarehouseReportNoPayData(List<WarehouseReportVo> warehouseReportList, String warehouseIdsStr, SearchVo timeType, int page, int rows) {
		String timeWhereInDate4Order = SqlUtils.getDateTimeWhere4SQL(timeType, "o.create_time");
		StringBuilder sql = new StringBuilder(" ");
		sql.append("SELECT");
		sql.append(" 	o.warehouse_id as warehouseId, ");
		sql.append(" 	IFNULL(ROUND(count(o.warehouse_id),2),0) noTotalOrder, ");
		sql.append(" 	IFNULL(ROUND(SUM(o.actually_price),2),0) noTotalIncome ");
		sql.append("FROM supply_chain_console.supply_chain_order o ");
		sql.append("WHERE o.supply_order_state = 0 ");
		sql.append(timeWhereInDate4Order);
		if(!"all".equals(warehouseIdsStr)){
			sql.append(" AND  o.warehouse_id IN (");
			sql.append(warehouseIdsStr);
			sql.append(") ");
		}
		sql.append("GROUP BY o.warehouse_id");

		List<Map<String, Object>> warehouseNoPayReportList = this.findForJdbcParam(sql.toString(), page, rows);

		Map<String, Map<String, Object>> Mmap = new HashMap<String , Map<String, Object>>();
		for (Map<String, Object> m : warehouseNoPayReportList){
			Mmap.put(m.get("warehouseId").toString(),m);
		}
		if (warehouseNoPayReportList.size()  > 0){
			for (WarehouseReportVo vo : warehouseReportList){
				Map<String, Object> m = Mmap.get(vo.getWarehouseId()+"");
				if(m == null) continue;
				vo.setNoTotalIncome(Double.parseDouble(m.get("noTotalIncome").toString()));
				vo.setNoTotalOrder(Integer.parseInt(m.get("noTotalOrder").toString()));
			}
		}
	}

	/**
	 * 未支付详情数据
	 * @param warehouseId
	 * @param timeType
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getWarehouseReportNoPayDetailData(int warehouseId, SearchVo timeType, int page, int rows) {

		String timeWhereInDate4Order = SqlUtils.getDateTimeWhere4SQL(timeType, "o.create_time");
		StringBuilder sql = new StringBuilder(" ");
		sql.append("SELECT ");
		sql.append("	ROUND(o.actually_price,2) as actuallyPrice, ");
		sql.append("	o.target_phone as targetPhone, ");
		sql.append("	o.target_user as targetUser, ");
		sql.append("	o.merchant_name as merchantName, ");
		sql.append("	o.supply_order_num as supplyOrderNum, ");
		sql.append("	o.target_address as targetAddress, ");
		sql.append("	o.create_time as createTime ");
		sql.append("FROM supply_chain_console.supply_chain_order o ");
		sql.append("WHERE o.supply_order_state = 0 ");
		sql.append(timeWhereInDate4Order);
		sql.append(" AND  o.warehouse_id  = "+warehouseId);
		sql.append(" ORDER BY o.actually_price desc ");

		logger.info(sql.toString());
		return this.findForJdbcParam(sql.toString(), page, rows);
	}
}
