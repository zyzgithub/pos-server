package com.wm.dao.coupons.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.controller.coupons.vo.ReceiverVo;
import com.wm.dao.coupons.CouponsDao;
import com.wm.entity.coupons.CouponsEntity;
import com.wm.entity.coupons.CouponsPublishEntity;
import com.wm.entity.coupons.CouponsUserEntity;

@Repository("couponsDao")
@SuppressWarnings("unchecked")
public class CouponsDaoImpl extends GenericBaseCommonDao<CouponsEntity, Serializable> implements CouponsDao{

	@Override
	public List<CouponsUserEntity> queryMyCoupons(Integer userId,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		String hql =  " select c "
					+ " from CouponsUserEntity c, WUserEntity u "
					+ " where u.id = ? and u.mobile = c.userMobile "
					+ " and c.couponsNum > 0 and (UNIX_TIMESTAMP(CURDATE()) <= c.endTime or c.endTime = 0 ) "
					+ " order by c.createTime asc ";
		Query query = createHqlQuery(hql, userId);
		query.setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public boolean queryIsExistBySerial(String serial) {
		// TODO Auto-generated method stub
		String hql =  " select count(c.id) "
					+ " from CouponsPublishEntity c "
					+ " where c.couponsSerial = ? and (UNIX_TIMESTAMP(CURDATE()) <= c.endTime or c.endTime = 0 ) "
					+ " and c.status = ? ";
		Query query = createHqlQuery(hql, serial, "Y");
		Object obj = query.uniqueResult();
		if(null != obj) {
			long c = Long.valueOf(obj.toString());
			return c > 0 ? true : false;
		}
		return false;
	}
	
	@Override
	public boolean queryIsReceive(String serial, String mobile) {
		// TODO Auto-generated method stub
		String hql =  " select count(c.id) "
					+ " from CouponsUserEntity c "
					+ " where c.userMobile = ? and c.couponsSerial = ? ";
		Query query = createHqlQuery(hql, mobile, serial);
		Object obj = query.uniqueResult();
		if(null != obj) {
			long c = Long.valueOf(obj.toString());
			return c > 0 ? true : false;
		}
		return false;
	}
	
	@Override
	public CouponsPublishEntity queryCouponsPublishBySerial(String serial) {
		// TODO Auto-generated method stub
		String hql =  " from CouponsPublishEntity c "
					+ " where c.couponsSerial = ? and (UNIX_TIMESTAMP(CURDATE()) <= c.endTime or c.endTime = 0 ) "
					+ " and c.status = ? ";
		Query query = createHqlQuery(hql, serial, "Y");
		List<CouponsPublishEntity> c = query.list();
		if(c.size() > 0)
			return c.get(0);
		return null;
	}
	
	@Override
	public List<CouponsEntity> queryCouponsByType(String type) {
		// TODO Auto-generated method stub
		String hql =  " from CouponsEntity c "
					+ " where c.type = ? and c.status = ? ";
		Query query = createHqlQuery(hql, type, "Y");
		return query.list();
	}
	
	@Override
	public List<ReceiverVo> queryReceiverBySerial(String serial,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		String sql =  " select cu.user_mobile mobile, cu.coupons_money money, FROM_UNIXTIME(cu.create_time) time, u.photoUrl header "
					+ " from 0085_coupons_user cu "
					+ " left join user u "
					+ " on cu.user_mobile = u.mobile and u.user_type = ?  "
					+ " where cu.coupons_serial = ? "
					+ " order by cu.create_time desc, cu.create_time desc ";
		SQLQuery query = createSqlQuery(sql, "user", serial);
		query.setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize);
		query.setResultTransformer(Transformers.aliasToBean(ReceiverVo.class));
		return query.list();
	}
	
	@Override
	public CouponsUserEntity queryAvailableForOrder(Integer userId, int money) {
		// TODO Auto-generated method stub
		String hql =  " select c "
					+ " from CouponsUserEntity c, WUserEntity u "
					+ " where u.id = ? and u.mobile = c.userMobile "
					+ " and c.couponsNum > ? and c.couponsLimitMoney <= ? "
					+ " and (UNIX_TIMESTAMP(CURDATE()) <= c.endTime or c.endTime = 0 ) "
					+ " order by c.couponsMoney desc ";
		Query query = createHqlQuery(hql, userId, 0, money);
		query.setFirstResult(0).setMaxResults(1);
		List<CouponsUserEntity> l = query.list();
		if(l.size()>0)
			return l.get(0);
		return null;
	}
	
	@Override
	public CouponsUserEntity queryCouponsById(Integer couponsId, Integer userId) {
		// TODO Auto-generated method stub
		String hql =  " select c "
					+ " from CouponsUserEntity c "
					+ " where c.id = ? and c.couponsNum > ?  "
					+ " and (UNIX_TIMESTAMP(CURDATE()) <= c.endTime or c.endTime = 0 ) ";
		Query query = createHqlQuery(hql, couponsId, 0);
		Object obj = query.uniqueResult();
		if(null != obj)
			return (CouponsUserEntity)obj;
		return null;
	}
}
