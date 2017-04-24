package com.wm.service.coupons;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.coupons.vo.ReceiverVo;
import com.wm.entity.coupons.CouponsUserEntity;

public interface CouponsServiceI extends CommonService{

	List<CouponsUserEntity> queryMyCoupons(Integer userId, Integer pageIndex, Integer pageSize);

	boolean queryIsExistBySerial(String serial);

	boolean queryIsReceive(String serial, String mobile);

	CouponsUserEntity saveCoupons(String serial, String mobile);

	List<ReceiverVo> queryReceiverBySerial(String serial, Integer pageIndex, Integer pageSize);

	CouponsUserEntity queryAvailableForOrder(Integer userId, int money);

}
