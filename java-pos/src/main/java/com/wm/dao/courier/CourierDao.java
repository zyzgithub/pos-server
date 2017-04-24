package com.wm.dao.courier;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

public interface CourierDao extends IGenericBaseCommonDao{


	/**
	 * 查询快递员的催单列表，通过区域id查询列表
	 * @param orgId 区域id
	 * @param userId 快递员id
	 * @param page 开始页, 第一页为1，不能为0
	 * @param rows 每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryCourierReminderListByOrgId(Integer orgId, Integer userId, Integer page ,Integer rows);
	
	/**
	 * 统计快递员的催单数
	 * @param orgId 区域id
	 * @param userId 快递员id
	 * @return
	 */
	Long countCourierReminder(Integer orgId, Integer userId);
	
}
