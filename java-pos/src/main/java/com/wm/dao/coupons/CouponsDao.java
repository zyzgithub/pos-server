package com.wm.dao.coupons;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.coupons.vo.ReceiverVo;
import com.wm.entity.coupons.CouponsEntity;
import com.wm.entity.coupons.CouponsPublishEntity;
import com.wm.entity.coupons.CouponsUserEntity;

public interface CouponsDao extends IGenericBaseCommonDao{

	List<CouponsUserEntity> queryMyCoupons(Integer userId, Integer pageIndex,
			Integer pageSize);

	boolean queryIsExistBySerial(String serial);

	boolean queryIsReceive(String serial, String mobile);

	CouponsPublishEntity queryCouponsPublishBySerial(String serial);

	List<CouponsEntity> queryCouponsByType(String type);

	List<ReceiverVo> queryReceiverBySerial(String serial, Integer pageIndex,
			Integer pageSize);

	CouponsUserEntity queryAvailableForOrder(Integer userId, int money);

	CouponsUserEntity queryCouponsById(Integer couponsId, Integer userId);

}
