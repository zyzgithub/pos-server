package com.wm.service.impl.address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.statistics.vo.AddressOrderStatisticListVo;
import com.wm.controller.statistics.vo.AddressStatisticListVo;
import com.wm.dao.address.AddressDao;
import com.wm.entity.address.AddressEntity;
import com.wm.service.address.AddressServiceI;
import com.wm.util.PageList;

@Service("addressService")
@Transactional
public class AddressServiceImpl extends CommonServiceImpl implements AddressServiceI {
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private SystemService systemService;

	@SuppressWarnings("rawtypes")
	@Override
	public int addDeliveryAddr(int userId,String name, String mobile, String city,
			String address_detail,String location) {
		
		if("0".equals(location)){
			return 0;
		}else{
			this.executeSql("update address set is_default='N' where user_id=?", userId);
			AddressEntity address = new AddressEntity();
			address.setUserId(userId);
			address.setName(name);
			address.setMobile(mobile);
			address.setCity(city);
			address.setAddressDetail(address_detail);
			address.setIsDefault("Y");
			address.setCreateTime(new Date());
			address.setLocation(location);
			this.save(address);
			
			String sql = "select LAST_INSERT_ID() lastInsertId";
			Map lastInsertIdMap = this.findOneForJdbc(sql);
			int addrId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
			
			
			return addrId;
		}
	}

	@Override
	public boolean updateDefaultAddr() {
		AddressEntity address = systemService.findUniqueByProperty(AddressEntity.class, "isDefault", "Y");
		if(address!=null){
			address.setIsDefault("N");
			this.saveOrUpdate(address);
		}
		return true;
	}

	@Override
	public boolean getAddressExist(int userId, String mobile, String address) {
		String sql="select * from address a where a.user_id=? and a.mobile=? and a.address_detail=? ";
		List<Map<String, Object>> list=this.findForJdbc(sql, userId,mobile,address);
		if(list.size()==0){
			return true;
		}
		return false;
	}

	@Override
	public AddressEntity queryLasted(Integer userId) {
		// TODO Auto-generated method stub
		return addressDao.queryLasted(userId);
	}
	
	@Override
	public boolean updateAddrDefault(Integer userId, Integer addressId,
			String isDefault) {
		// TODO Auto-generated method stub
		addressDao.cancelAddrDefault(userId);
		return addressDao.updateAddrDefault(userId, addressId, isDefault);
	}
	
	@Override
	public boolean cancelAddrDefault(Integer userId) {
		// TODO Auto-generated method stub
		return addressDao.cancelAddrDefault(userId);
	}
	
	@Override
	public PageList queryStatistic(Integer pageIndex, Integer pageSize,
			String startDate, String endDate) {
		long totalRows = addressDao.queryStatisticCount(pageIndex, pageSize, startDate, endDate);
		List<AddressStatisticListVo> vos = null;
		if(totalRows > 0) {
			vos = addressDao.queryStatistic(pageIndex, pageSize, startDate, endDate);
			for (AddressStatisticListVo vo : vos) {
				vo.setTotalNum(addressDao.queryCountByBuildingId(vo.getBuildingId(), startDate, endDate));
			}
		} else {
			vos = new ArrayList<AddressStatisticListVo>(0);
		}
		
		PageList page = new PageList(pageSize, pageIndex, (int)totalRows, vos);
		return page;
	}
	
	@Override
	public PageList queryStatisticByBuildingId(Integer buildingId,
			Integer pageIndex, Integer pageSize, String startDate,
			String endDate) {
		long totalRows = addressDao.queryStatisticCountByBuildingId(buildingId, startDate, endDate);
		List<AddressOrderStatisticListVo> vos = null;
		if(totalRows > 0) {
			vos = addressDao.queryStatisticByBuildingId(buildingId, pageIndex, pageSize, startDate, endDate);
		} else {
			vos = new ArrayList<AddressOrderStatisticListVo>(0);
		}
		
		PageList page = new PageList(pageSize, pageIndex, (int)totalRows, vos);
		return page;
	}
}