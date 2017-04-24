package com.courier_mana.monitor.service;



public interface CourierAttendanceServiceI {
	
	/**
	 * 根据快递员id获取他管辖范围内当前上班的快递员人数
	 * @param courierId 快递员id
	 * @return
	 */
	public Integer getCourierOndutySum(Integer courierId);

}
