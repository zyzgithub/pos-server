package com.dianba.pos.scheduler.service;


/**
 * Created by zhangyong on 2017/7/5.
 * 异常订单--
 * 刷单管理
 *
 */

public interface ExceptionOrderManager {

    /**
     * 检测出异常订单并把商家添加至黑名单
     */
    void checkBlackPassport();


}
