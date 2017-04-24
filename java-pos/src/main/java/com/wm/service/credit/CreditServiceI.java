package com.wm.service.credit;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.credit.CreditEntity;

public interface CreditServiceI extends CommonService{

	/**
	 * 查询符合条件的积分列表
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param userId	用户id
	 * @param detailId	
	 * @param action	
	 * @param justToday  是否只差当天的
	 * @return
	 */
	List<CreditEntity> getListByCondition(Integer userId,Integer detailId,String action,Boolean justToday);
}
