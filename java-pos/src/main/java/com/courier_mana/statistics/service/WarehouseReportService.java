package com.courier_mana.statistics.service;

import java.util.List;
import java.util.Map;

import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.statistics.vo.WarehouseReportDetailVo;
import com.courier_mana.statistics.vo.WarehouseReportVo;

public interface WarehouseReportService {

	/**
	 * 获取仓库管理报表页面信息
	 * @author hyj
	 * @param timeType
	 * @param page	页码
	 * @param rows	页面容量
	 * @return
	 */
	public abstract List<WarehouseReportVo> getWarehouseReportData(long userId, SearchVo timeType, int page, int rows);
	
	/**
	 * 获取仓库管理报表详情页面
	 * @param timeType
	 * @param page
	 * @param rows
	 * @return
	 */
	public abstract List<WarehouseReportDetailVo> getWarehouseReportDetail(int warehouseId, SearchVo timeType, int page, int rows);
	
	/**
	 * 创建回访记录
	 * @param merchantId	商家ID
	 * @param userId		回访人ID
	 * @param phone			电话号码
	 * @return
	 */
	public abstract int recordCallLog(long merchantId, long userId, String phone);
	
	/**
	 * 插入仓库报表管理员记录
	 * @author hyj
	 * @param userId	用户ID
	 * @param userName	用户实名
	 * @param phone		用户电话号码
	 */
	public abstract void insertWarehouseReportAdmin(long userId, String userName, String phone);
	
	/**
	 * 判断用户是否管理员
	 * @param userId
	 * @return
	 */
	public abstract boolean ifUserAWarehouseReportAdmin(long userId);

	/**
	 * 获取仓库管理报表 未支付数据 详情页面
	 * @param warehouseId
	 * @param timeType
	 * @param page
	 * @param rows
	 * @return
	 */
	public abstract List<Map<String, Object>> getWarehouseReportNoPayDetailData(int warehouseId, SearchVo timeType, int page, int rows);
}
