package com.dianba.pos.passport.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.vo.PassportVo;
import com.dianba.pos.passport.vo.RegisterVo;



/**
 * Created by zhangyong on 2017/6/1.
 */
public interface PassportManager {

    Passport getPassportInfoByCashierId(Long cashierId);

    /**
     * 登录
     * @param passportVo
     * @return
     */
    BasicResult login(PassportVo passportVo);

    /**
     * 注册
     * @param registerVo
     * @return
     */
    BasicResult register(RegisterVo registerVo);

    /**
     * 编辑商家营业员信息
     * @param registerVo
     * @return
     */
    BasicResult editPosAccount(RegisterVo registerVo);

    /**
     * 删除商家营业员
     * @param registerVo
     * @return
     */
    BasicResult deletePosAccount(RegisterVo registerVo);

    /**
     * 根据商家id获取营业员
     * @param  registerVo
     * @return
     */
    BasicResult getMerchantPosList(RegisterVo registerVo);

    /**
     * 获取营业员信息
     * @param registerVo
     * @return
     */
    BasicResult getCashierById(RegisterVo registerVo);
}
