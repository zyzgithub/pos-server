package com.wm.service.address;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.address.AddressEntity;
import com.wm.util.PageList;

public interface AddressServiceI extends CommonService{

	/**
	 * 新增收货地址
	 * @param name 联系人	
	 * @param mobile 手机号码
	 * @param city 城市
	 * @param address_detail 详细地址
	 * @param location 定位坐标
	 * @return
	 */
	public int addDeliveryAddr(int userId,String name,String mobile,String city,String address_detail,String location);
	
	
	
	//public int updateDeliveryAddr(int addrId,String name,String mobile,String city,String address_detail);
	
	

	/**
	 * 取消默认收货地址
	 * @param addrId
	 * @return
	 */
	public boolean updateDefaultAddr();
	/**
	 * 判断用户收货地址是否存在，true为不存在,false为不存在
	 * @param userId
	 * @param mobile
	 * @param address
	 * @return
	 */
	public boolean getAddressExist(int userId,String mobile,String address);

	boolean cancelAddrDefault(Integer userId);

	public AddressEntity queryLasted(Integer userId);
	
	public boolean updateAddrDefault(Integer userId, Integer addressId, String isDefault);


	/**
	 * 统计地址列表对应销量的
	 * @param pageIndex
	 * @param pageIndex2
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public PageList queryStatistic(Integer pageIndex, Integer pageSize,
			String startDate, String endDate);



	/**
	 * 配送地址对应的订单列表
	 * @param buildingId
	 * @param pageIndex
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public PageList queryStatisticByBuildingId(Integer buildingId,
			Integer pageIndex, Integer pageSize, String startDate,
			String endDate);
}
