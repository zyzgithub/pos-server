package com.wm.service.impl.coupons;

import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.coupons.vo.ReceiverVo;
import com.wm.dao.coupons.CouponsDao;
import com.wm.entity.coupons.CouponsEntity;
import com.wm.entity.coupons.CouponsPublishEntity;
import com.wm.entity.coupons.CouponsUserEntity;
import com.wm.service.coupons.CouponsServiceI;

@Service("couponsService")
@Transactional
public class CouponsServiceImpl extends CommonServiceImpl implements CouponsServiceI{

	//private static final Logger logger = Logger.getLogger(CouponsServiceImpl.class);
	
	@Autowired
	private CouponsDao couponsDao;

	@Override
	public List<CouponsUserEntity> queryMyCoupons(Integer userId,
			Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return couponsDao.queryMyCoupons(userId, pageIndex, pageSize);
	}
	
	@Override
	public boolean queryIsExistBySerial(String serial) {
		// TODO Auto-generated method stub
		return couponsDao.queryIsExistBySerial(serial);
	}
	
	@Override
	public boolean queryIsReceive(String serial, String mobile) {
		// TODO Auto-generated method stub
		return couponsDao.queryIsReceive(serial, mobile);
	}

	@Override
	public CouponsUserEntity saveCoupons(String serial, String mobile) {
		// TODO Auto-generated method stub
		//step 1 根据序列号,查询发布的优惠券信息
		CouponsPublishEntity publish = couponsDao.queryCouponsPublishBySerial(serial);
		if(null != publish) {
			//step 2  查询所有优惠券，然后随机抽取一张
			List<CouponsEntity> coupons = couponsDao.queryCouponsByType(publish.getCouponsType());
			if(coupons.size() > 0) {
				int i = (int)(Math.random()*coupons.size());
				CouponsEntity c = coupons.get(i);
				
				//step 3 写人优惠券
				CouponsUserEntity cu = new CouponsUserEntity();
				cu.setUserMobile(mobile);
				cu.setCouponsNum(1);
				cu.setCouponsMoney(c.getMoney());
				cu.setCouponsLimitMoney(c.getLimitMoney());
				cu.setCouponsSerial(publish.getCouponsSerial());
				cu.setCouponsType(publish.getCouponsType());
				cu.setEndTime(publish.getEndTime());
				cu.setCreateTime(DateUtils.getSeconds());
				
				couponsDao.save(cu);
				
				return cu;
			}
		}
		
		return null;
	}
	
	@Override
	public List<ReceiverVo> queryReceiverBySerial(String serial, Integer pageIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return couponsDao.queryReceiverBySerial(serial, pageIndex, pageSize);
	}
	
	@Override
	public CouponsUserEntity queryAvailableForOrder(Integer userId, int money) {
		// TODO Auto-generated method stub
		return couponsDao.queryAvailableForOrder(userId, money);
	}
}
