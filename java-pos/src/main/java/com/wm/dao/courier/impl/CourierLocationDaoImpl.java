package com.wm.dao.courier.impl;

import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.dao.courier.CourierLocationDao;
import com.wm.entity.courier.CourierLocationEntity;

@Repository("courierLocationDao")
public class CourierLocationDaoImpl extends GenericBaseCommonDao<CourierLocationEntity, Integer> implements CourierLocationDao {

}
