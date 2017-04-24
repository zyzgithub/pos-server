package com.wm.dao.address.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.controller.statistics.vo.AddressOrderStatisticListVo;
import com.wm.controller.statistics.vo.AddressStatisticListVo;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.dao.address.AddressDao;
import com.wm.entity.address.AddressEntity;

@Repository("addressDao")
@SuppressWarnings("unchecked")
public class AddressDaoImpl extends GenericBaseCommonDao<AddressEntity, Integer> implements AddressDao {

	@Override
	public AddressEntity queryLasted(Integer userId) {
		// TODO Auto-generated method stub
		String hql = "from AddressEntity a where a.userId = ? and a.isDefault = ?";
		Query query = createHqlQuery(hql, userId, "Y");
		Object obj = query.uniqueResult();
		if(null != obj)
			return (AddressEntity)obj;
		return null;
	}

	@Override
	public boolean cancelAddrDefault(Integer userId) {
		// TODO Auto-generated method stub
		String hql = "update AddressEntity a set a.isDefault = ? where a.userId = ?";
		Query query = createHqlQuery(hql, "N", userId);
		int rows = query.executeUpdate();
		return rows > 0;
	}
	
	@Override
	public boolean updateAddrDefault(Integer userId, Integer addressId, String isDefault) {
		// TODO Auto-generated method stub
		String hql = "update AddressEntity a set a.isDefault = ? where a.id = ? and a.userId = ?";
		Query query = createHqlQuery(hql, isDefault, addressId, userId);
		int rows = query.executeUpdate();
		return rows > 0;
	}
	
	@Override
	public AddressDetailVo queryAddressDetailById(int addressId) {
		// TODO Auto-generated method stub
		String sql = "select addr.id as id, addr.user_id as userId, "
					+ "addr.name as name, addr.mobile as mobile, bui.id as buildId, "
					+ "addr.building_floor as buildFloor, addr.building_name as buildName, "
					+ "addr.address_detail as addressDetail, bui.longitude as lng, bui.latitude as lat "
					+ "from address as addr "
					+ "left join 0085_building as bui "
					+ "on addr.building_id = bui.id "
					+ "where addr.id = ?";
		
		SQLQuery query = createSqlQuery(sql, addressId);
		query.setResultTransformer(Transformers.aliasToBean(AddressDetailVo.class));
		Object obj = query.uniqueResult();
		if(null != obj)
			return (AddressDetailVo)obj;
		
		return null;
	}
	
	@Override
	public List<AddressStatisticListVo> queryStatistic(Integer pageIndex, Integer pageSize,
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql =  " SELECT COUNT(t.id) total, SUM(t.origin) sumOrigin, SUM(t.online_money) sumOnline, SUM(t.credit) sumCredit, SUM(t.score_money) sumScore, SUM(t.delivery_fee) sumDelivery, t.building_id buildingId, IF(t.building_id IS NULL,'其他地址',t.building_name ) buildingName ";
		 	   sql += " FROM( ";
		 	   sql += " SELECT o.id, o.origin, o.online_money, o.credit, o.score_money, o.delivery_fee, a.building_id, a.building_name ";
		 	   sql += " FROM `order` o  ";
		 	   sql += " LEFT JOIN address a ";
		 	   sql += " ON o.user_address_id=a.id ";
		 	   sql += " WHERE o.pay_state = 'pay' AND o.order_type != 'recharge' ";
		 	   if(!StringUtils.isEmpty(startDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) >= "+startDate;
		 	   if(!StringUtils.isEmpty(endDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) <= "+endDate;
		 	   sql += " ) t ";
		 	   sql += " GROUP BY t.building_id ";
		 	   sql += " ORDER BY total DESC ";
		SQLQuery query = createSqlQuery(sql);
		query.setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize);
		query.setResultTransformer(Transformers.aliasToBean(AddressStatisticListVo.class));
		List<AddressStatisticListVo> vos = query.list();
		return vos;
	}
	
	@Override
	public long queryStatisticCount(Integer pageIndex, Integer pageSize,
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql =  " SELECT COUNT(*) ";
		 	   sql += " FROM( ";
		 	   sql += " SELECT o.id, o.origin, o.online_money, o.credit, o.score_money, o.delivery_fee, a.building_id, a.building_name ";
		 	   sql += " FROM `order` o ";
		 	   sql += " LEFT JOIN address a ";
		 	   sql += " ON o.user_address_id=a.id ";
		 	   sql += " WHERE o.pay_state = 'pay' AND o.order_type != 'recharge' ";
		 	   if(!StringUtils.isEmpty(startDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) >= "+startDate;
		 	   if(!StringUtils.isEmpty(endDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) <= "+endDate;
		 	   sql += " GROUP BY a.building_id ";
		 	   sql += " ) t ";
		SQLQuery query = createSqlQuery(sql);
		Object obj = query.uniqueResult();
		if(null != obj)
			return ((BigInteger)obj).longValue();
		return 0;
	}
	
	@Override
	public long queryCountByBuildingId(Integer buildingId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql = " SELECT SUM(om.quantity) ";
			   sql +=" FROM ( ";
			   sql +=" SELECT o.id FROM `order` o, address a ";
			   sql +=" where o.user_address_id = a.id and o.pay_state='pay' AND o.order_type != 'recharge' ";
			   if(null == buildingId)
				   sql += " and a.building_id is null ";
			   else
				   sql += " and a.building_id = " + buildingId;
				   
			   if(!StringUtils.isEmpty(startDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) >= "+startDate;
		 	   if(!StringUtils.isEmpty(endDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) <= "+endDate;
			   sql +=" ) as t, order_menu as om ";
			   sql +=" where t.id = om.order_id; ";
		SQLQuery query = createSqlQuery(sql);
		Object obj = query.uniqueResult();
		if(null != obj)
			return ((BigDecimal)obj).longValue();
		return 0;
	}
	
	@Override
	public long queryStatisticCountByBuildingId(Integer buildingId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		String sql = " SELECT COUNT(o.id) "; 
			   sql +=" FROM `order` o , address a ";
			   sql +=" WHERE o.user_address_id=a.id and o.pay_state='pay' AND o.order_type != 'recharge' ";
			   if(null == buildingId)
				   sql += " AND a.building_id is null ";
			   else
				   sql += " AND a.building_id = " + buildingId;
			   if(!StringUtils.isEmpty(startDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) >= "+startDate;
		 	   if(!StringUtils.isEmpty(endDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) <= "+endDate;
		SQLQuery query = createSqlQuery(sql);
		Object obj = query.uniqueResult();
		if(null != obj)
			return ((BigInteger)obj).longValue();
		return 0;
	}
	
	@Override
	public List<AddressOrderStatisticListVo> queryStatisticByBuildingId(
			Integer buildingId, Integer pageIndex, Integer pageSize,
			String startDate, String endDate) {
		// TODO Auto-generated method stub
		String sql = " SELECT o.id orderId, o.order_num orderNum, o.pay_id payId, TIMESTAMP(FROM_UNIXTIME(o.create_time)) createTime, ";
			   sql+= " o.origin origin, o.online_money onlineMoney, o.credit credit, o.score_money scoreMoney, o.delivery_fee deliveryFee, ";
			   sql+= " o.order_type orderType, o.sale_type saleType, o.pay_type payType, o.realname userName, o.mobile mobile, o.address address, ";
			   sql+= " a.building_id buildingId, IF(a.building_id IS NULL,'其他地址',a.building_name ) buildingName  ";
			   sql+= " FROM `order` o , address a ";
			   sql+= " WHERE o.user_address_id = a.id and o.pay_state='pay' AND o.order_type != 'recharge' ";
			   if(null == buildingId)
				   sql+= " and a.building_id is null ";
			   else
				   sql+= " and a.building_id = "+buildingId;
			   if(!StringUtils.isEmpty(startDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) >= "+startDate;
		 	   if(!StringUtils.isEmpty(endDate))
		 		   sql += " AND TIMESTAMP(FROM_UNIXTIME(o.create_time)) <= "+endDate;
			   sql+= " ORDER BY o.id DESC ";
		
		SQLQuery query = createSqlQuery(sql);
		query.setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize);
		query.setResultTransformer(Transformers.aliasToBean(AddressOrderStatisticListVo.class));
		List<AddressOrderStatisticListVo> vos = query.list();
		return vos;
	}
}
