package com.wm.dao.address;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.statistics.vo.AddressOrderStatisticListVo;
import com.wm.controller.statistics.vo.AddressStatisticListVo;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.entity.address.AddressEntity;

public interface AddressDao extends IGenericBaseCommonDao{

	AddressEntity queryLasted(Integer userId);

	boolean cancelAddrDefault(Integer userId);

	boolean updateAddrDefault(Integer userId, Integer addressId, String isDefault);

	AddressDetailVo queryAddressDetailById(int addressId);

	List<AddressStatisticListVo> queryStatistic(Integer pageIndex, Integer pageSize,
			String startDate, String endDate);
	
	long queryStatisticCount(Integer pageIndex, Integer pageSize,
			String startDate, String endDate);

	long queryCountByBuildingId(Integer buildingId, String startDate, String endDate);

	long queryStatisticCountByBuildingId(Integer buildingId, String startDate, String endDate);

	List<AddressOrderStatisticListVo> queryStatisticByBuildingId(
			Integer buildingId, Integer pageIndex, Integer pageSize,
			String startDate, String endDate);
}
