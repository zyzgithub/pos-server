package com.dianba.pos.passport.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.passport.mapper.PassportMapper;
import com.dianba.pos.passport.po.LifePassportAlias;
import com.dianba.pos.passport.po.LifePassportProperties;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosCashierAccount;
import com.dianba.pos.passport.repository.LifePassportAliasJpaRepository;
import com.dianba.pos.passport.repository.LifePassportPropertiesJpaRepository;
import com.dianba.pos.passport.repository.PassportJpaRepository;
import com.dianba.pos.passport.repository.PosCashierAccountJpaRepository;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.support.PassportRemoteService;
import com.dianba.pos.passport.vo.LoginVo;
import com.dianba.pos.passport.vo.PassportVo;
import com.dianba.pos.passport.vo.RegisterVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Service
public class DefaultPassportManager extends PassportRemoteService implements PassportManager {

    private static Logger logger = LogManager.getLogger(DefaultPassportManager.class);

    @Autowired
    private PassportMapper passportMapper;
    @Autowired
    private PosCashierAccountJpaRepository posCashierAccountJpaRepository;
    @Autowired
    private PassportJpaRepository passportJpaRepository;
    @Autowired
    private LifePassportAliasJpaRepository lifePassportAliasJpaRepository;
    @Autowired
    private LifePassportPropertiesJpaRepository lifePassportPropertiesJpaRepository;

    @Override
    public Passport findById(Long passportId) {
        Passport passport = passportJpaRepository.findOne(passportId);
        if (passport == null) {
            throw new PosNullPointerException("用户信息不存在！");
        }
        return passport;
    }

    @Override
    public Passport getPassportInfoByCashierId(Long cashierId) {
        Passport passport = passportMapper.getPassportInfoByCashierId(cashierId);
        if (passport == null) {
            throw new PosNullPointerException("商家信息不存在！");
        }
        return passport;
    }

    @Override
    public BasicResult login(PassportVo passportVo) {
        logger.info("登录用户名：" + passportVo.getUsername());

        //查询当前登录账号权限
        LifePassportAlias lifePassportAlias = lifePassportAliasJpaRepository.findLifePassportAliasByAliasName(
                passportVo.getUsername());
        if (lifePassportAlias == null) {
            return BasicResult.createFailResult("无此账号信息");
        } else {
            //普通商家权限
            LifePassportProperties a = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            Long.parseLong(lifePassportAlias.getPassportId()), "consumer"
                            , "11");
            //签约商家权限
            LifePassportProperties b = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            Long.parseLong(lifePassportAlias.getPassportId()), "consumer", "14");

            //pos商家权限
            LifePassportProperties c = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            Long.parseLong(lifePassportAlias.getPassportId()), "pos", "41");
            if (b != null || a != null) {
                passportVo.setClientType(3);
                BasicResult basicResult = post(LOGIN, passportVo);
                logger.info("pos 端登录返回结果：" + basicResult.getResponse().toJSONString());
                if (basicResult.isSuccess()) {
                    LoginVo loginVo = JSONObject.parseObject(basicResult.getResponse().toJSONString(), LoginVo.class);
                    loginVo.setAccountType(0);
                    loginVo.setAccountTypeName("店长");
                    basicResult.setResponse(loginVo);
                    return basicResult;
                }
                return basicResult;
            } else if (c != null) {
                passportVo.setClientType(2);
                BasicResult basicResult = post(LOGIN, passportVo);
                logger.info("pos 端登录返回结果：" + basicResult.getResponse().toJSONString());
                if (basicResult.isSuccess()) {
                    LoginVo loginVo = JSONObject.parseObject(basicResult.getResponse().toJSONString(), LoginVo.class);
                    loginVo.setAccountType(1);
                    loginVo.setAccountTypeName("店员");
                    JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(loginVo);
                    Passport passport = passportMapper.getPassportInfoByCashierId(loginVo.getPassportId());
                    loginVo.setShowName(passport.getShowName());
                    return BasicResult.createSuccessResult("登录成功!", jsonObject1);
                }
                return basicResult;
            } else {
                return BasicResult.createFailResult("登录出现异常");
            }

        }
    }

    @Override
    public BasicResult register(RegisterVo registerVo) {
        logger.info("====================收银员账号注册==================");

        registerVo.setClientType(2);
        logger.info("注册账号：" + registerVo.getName());
        BasicResult basicResult = post(REGISTER, registerVo);
        logger.info("注册返回：" + basicResult.getResponse().toJSONString());
        if (basicResult.isSuccess()) {
            Long passportId = basicResult.getResponse().getLong("passportId");
            PosCashierAccount posCashierAccount = new PosCashierAccount();
            posCashierAccount.setMerchantId(registerVo.getAccountId());
            posCashierAccount.setAccountType(1);
            posCashierAccount.setCashierId(passportId);
            posCashierAccount.setCashierPhoto(registerVo.getCashierPhoto());
            posCashierAccount.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
            posCashierAccountJpaRepository.save(posCashierAccount);

            PosCashierAccount posCashierAccount1 = posCashierAccountJpaRepository
                    .findPosCashierAccountByMerchantIdAndAccountType(registerVo.getAccountId(), 0);
            //如果店长没注册就注册店长信息
            if (posCashierAccount1 == null) {
                posCashierAccount1 = new PosCashierAccount();
                posCashierAccount1.setAccountType(0);
                posCashierAccount1.setCashierPhoto(registerVo.getCashierPhoto());
                posCashierAccount1.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                posCashierAccount1.setCashierId(registerVo.getAccountId());
                posCashierAccount1.setMerchantId(registerVo.getAccountId());
                //注册店长
                posCashierAccountJpaRepository.save(posCashierAccount1);
            }
        }
        return basicResult;
    }

    @Override
    public BasicResult editPosAccount(RegisterVo registerVo) {
        Passport passport = passportJpaRepository.getPassportById(registerVo.getAccountId());
        if (passport != null) {

            LifePassportAlias passportAlias = lifePassportAliasJpaRepository.findLifePassportAliasByAliasName(
                    passport.getPhoneNumber());
            LifePassportAlias passportAlias1 = lifePassportAliasJpaRepository.findLifePassportAliasByAliasName(
                    passport.getDefaultName());
            //真实姓名
            if (!StringUtil.isEmpty(registerVo.getRealName())) {
                passport.setRealName(registerVo.getRealName());
            }
            //身份证号码
            if (!StringUtil.isEmpty(registerVo.getIdNumber())) {
                passport.setIdNumber(registerVo.getIdNumber());
            }

            //手机号码
            if (!StringUtil.isEmpty(registerVo.getPhoneNumber())) {
                passport.setPhoneNumber(registerVo.getPhoneNumber());
            }

            //账号
            if (!StringUtil.isEmpty(registerVo.getName())) {
                passport.setDefaultName(registerVo.getName());
            }
            //密码
            if (!StringUtil.isEmpty(registerVo.getPassword())) {
                passport.setPassword(registerVo.getPassword());
            }

            passportJpaRepository.save(passport);

            if (passportAlias != null) {
                //手机号码
                if (!StringUtil.isEmpty(registerVo.getPhoneNumber())) {
                    passportAlias.setAliasName(registerVo.getPhoneNumber());
                    lifePassportAliasJpaRepository.save(passportAlias);
                }

            }
            if (passportAlias1 != null) {
                //账号
                if (!StringUtil.isEmpty(registerVo.getName())) {
                    passportAlias1.setAliasName(registerVo.getName());
                    lifePassportAliasJpaRepository.save(passportAlias1);
                }


            }
            //签约商家权限
            LifePassportProperties b = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            registerVo.getAccountId(), "consumer", "14");

            //pos商家权限
            LifePassportProperties c = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            registerVo.getAccountId(), "pos", "41");

            if (b != null) {
                PosCashierAccount posCashierAccount = posCashierAccountJpaRepository
                        .findPosCashierAccountByMerchantIdAndAccountType(passport.getId(), 0);
                if (!StringUtil.isEmpty(registerVo.getCashierPhoto())) {
                    posCashierAccount.setCashierPhoto(registerVo.getCashierPhoto());
                    posCashierAccountJpaRepository.save(posCashierAccount);
                }
            } else if (c != null) {
                PosCashierAccount posCashierAccount = posCashierAccountJpaRepository.findPosCashierAccountByCashierId(
                        passport.getId());

                if (!StringUtil.isEmpty(registerVo.getCashierPhoto())) {
                    posCashierAccount.setCashierPhoto(registerVo.getCashierPhoto());
                    posCashierAccountJpaRepository.save(posCashierAccount);
                }
            }

            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
            return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);
        } else {
            return BasicResult.createFailResult("没有此营业员信息");

        }


    }

    @Override
    public BasicResult deletePosAccount(RegisterVo registerVo) {
        if (registerVo.getAccountId() == null) {
            return BasicResult.createFailResult("accountId 不能为空");
        } else {
            //签约商家权限
            LifePassportProperties b = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            registerVo.getAccountId(), "consumer", "14");

            //pos商家权限
            LifePassportProperties c = lifePassportPropertiesJpaRepository
                    .findLifePassportPropertiesByPassportIdAndKAndV(
                            registerVo.getAccountId(), "pos", "41");

            if (b != null) {
                //签约商家账号不能删除
                logger.info("此账号是签约商家账号不能删除。。" + registerVo.getAccountId());
                return BasicResult.createFailResult("签约商家账号不能删除。");
            } else if (c != null) {
                //pos 用户
                //删除原账号
                Passport passport = passportJpaRepository.getPassportById(registerVo.getAccountId());
                //passportJpaRepository.delete(passport);


                //删除2个登录账号

//                if (passport.getPhoneNumber().equals(passport.getDefaultName())) {
//                    LifePassportAlias lifePassportAlias = lifePassportAliasJpaRepository
//                            .findLifePassportAliasByAliasName(passport.getDefaultName());
//                    lifePassportAliasJpaRepository.delete(lifePassportAlias);
//                } else {
//                    LifePassportAlias lifePassportAlias = lifePassportAliasJpaRepository
//                            .findLifePassportAliasByAliasName(passport.getDefaultName());
//                    if (lifePassportAlias != null) {
//                        lifePassportAliasJpaRepository.delete(lifePassportAlias);
//                    }
//
//                    LifePassportAlias lifePassportAlias1 = lifePassportAliasJpaRepository
//                            .findLifePassportAliasByAliasName(passport.getPhoneNumber());
//                    if (lifePassportAlias1 != null) {
//                        lifePassportAliasJpaRepository.delete(lifePassportAlias1);
//                    }
//
//                }
                //删除账号表
                List<LifePassportAlias> aliases=lifePassportAliasJpaRepository.findAllByPassportId(passport.getId());
                for(LifePassportAlias alias : aliases){
                    lifePassportAliasJpaRepository.delete(alias);
                }
//
//                LifePassportProperties lifePassportProperties = lifePassportPropertiesJpaRepository
//                        .findLifePassportPropertiesByPassportId(registerVo.getAccountId());
//
//                lifePassportPropertiesJpaRepository.delete(lifePassportProperties);
                //删除商家pos 关联表
                PosCashierAccount posCashierAccount = posCashierAccountJpaRepository.findPosCashierAccountByCashierId(
                        registerVo.getAccountId());
                posCashierAccountJpaRepository.delete(posCashierAccount);
                //删除账号权限
                List<LifePassportProperties> passportProperties=lifePassportPropertiesJpaRepository
                        .findAllByPassportId(passport.getId());
                for(LifePassportProperties properties : passportProperties){
                    lifePassportPropertiesJpaRepository.delete(properties);
                }
                return BasicResult.createSuccessResult("删除成功");
            } else {
                logger.error("要删除的用户id为：" + registerVo.getAccountId() + "删除出现异常。");
                return BasicResult.createSuccessResult("没有删除权限");
            }
        }
    }

    protected RegisterVo getMerchantPosListVo(PosCashierAccount posCashierAccount, Passport passport) {
        RegisterVo registerVo = new RegisterVo();
        registerVo.setAccountType(posCashierAccount.getAccountType());
        registerVo.setVersionIndex(1);
        registerVo.setDeviceName(passport.getDeviceName());
        registerVo.setDeviceType(passport.getDeviceType());
        registerVo.setFromChannel(passport.getFromChannel());
        registerVo.setIdNumber(passport.getIdNumber());
        registerVo.setName(passport.getDefaultName());
        registerVo.setShowName(passport.getShowName());
        registerVo.setRealName(passport.getRealName());
        registerVo.setPassword(passport.getPassword());
        registerVo.setPhoneNumber(passport.getPhoneNumber());
        registerVo.setSex(passport.getSex());
        registerVo.setSmsCode("");
        if (posCashierAccount.getAccountType().equals(0)) {
            registerVo.setAccountTypeName("店长");
            registerVo.setAccountId(posCashierAccount.getMerchantId());
            registerVo.setClientType(3);
        } else if (posCashierAccount.getAccountType().equals(1)) {
            registerVo.setAccountTypeName("店员");
            registerVo.setAccountId(posCashierAccount.getCashierId());
            registerVo.setClientType(2);
        }
        if (StringUtil.isEmpty(posCashierAccount.getCashierPhoto())) {
            registerVo.setCashierPhoto("");
        } else {
            registerVo.setCashierPhoto(posCashierAccount.getCashierPhoto());
        }
        return registerVo;
    }

    @Override
    public BasicResult getMerchantPosList(RegisterVo registerVo) {
        List<PosCashierAccount> posCashierAccountList = posCashierAccountJpaRepository
                .findAllByMerchantId(registerVo.getAccountId());
        Passport passport = null;
        List<RegisterVo> registerVos = new ArrayList<>();
        boolean flag = false;
        Long userId = registerVo.getAccountId();
        if (posCashierAccountList.size() > 0) {
            for (PosCashierAccount posCashierAccount : posCashierAccountList) {
                if (posCashierAccount.getAccountType() == 0) {
                    passport = passportJpaRepository.findOne(posCashierAccount.getMerchantId());
                    flag = true;
                } else if (posCashierAccount.getAccountType() == 1) {
                    passport = passportJpaRepository.findOne(posCashierAccount.getCashierId());
                }
                if (passport != null) {
                    registerVo = getMerchantPosListVo(posCashierAccount, passport);
                    registerVos.add(registerVo);
                }


            }
            return BasicResult.createSuccessResultWithDatas("获取商家营业员信息成功!", registerVos);
        } else {
            if (!flag) { //注册一个店长信息
                PosCashierAccount posCashierAccount = new PosCashierAccount();
                posCashierAccount.setAccountType(0);
                posCashierAccount.setCashierPhoto("");
                posCashierAccount.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                posCashierAccount.setCashierId(userId);
                posCashierAccount.setMerchantId(userId);
                //注册店长
                posCashierAccountJpaRepository.save(posCashierAccount);
                Passport passport1 = passportJpaRepository.findOne(userId);
                registerVo = getMerchantPosListVo(posCashierAccount, passport1);
                registerVos.add(registerVo);
            }
            return BasicResult.createSuccessResultWithDatas("获取商家营业员信息成功!", registerVos);
        }

    }

    @Override
    public BasicResult getCashierById(RegisterVo registerVo) {
        PosCashierAccount posCashierAccount = posCashierAccount = posCashierAccountJpaRepository
                .findPosCashierAccountByCashierId(registerVo.getAccountId());
        if (posCashierAccount == null) {
            posCashierAccount = posCashierAccountJpaRepository.findPosCashierAccountByMerchantIdAndAccountType(
                    registerVo.getAccountId(), 0);
        }
        Passport passport = passportJpaRepository.findOne(registerVo.getAccountId());
        registerVo = new RegisterVo();
        registerVo.setVersionIndex(1);
        registerVo.setDeviceName(passport.getDeviceName());
        registerVo.setDeviceType(passport.getDeviceType());
        registerVo.setFromChannel(passport.getFromChannel());
        registerVo.setIdNumber(passport.getIdNumber());
        registerVo.setName(passport.getDefaultName());
        registerVo.setShowName(passport.getShowName());
        registerVo.setRealName(passport.getRealName());
        registerVo.setPassword(passport.getPassword());
        registerVo.setPhoneNumber(passport.getPhoneNumber());
        registerVo.setSex(passport.getSex());
        registerVo.setSmsCode("");
        if (posCashierAccount != null) {
            if (posCashierAccount.getAccountType().equals(0)) {

                registerVo.setAccountTypeName("店长");
                registerVo.setAccountId(posCashierAccount.getMerchantId());
                registerVo.setClientType(3);
            } else if (posCashierAccount.getAccountType().equals(1)) {

                registerVo.setAccountTypeName("店员");
                registerVo.setAccountId(posCashierAccount.getCashierId());
                registerVo.setClientType(2);
            }

            registerVo.setAccountType(posCashierAccount.getAccountType());
            if (StringUtil.isEmpty(posCashierAccount.getCashierPhoto())) {
                registerVo.setCashierPhoto("");
            } else {
                registerVo.setCashierPhoto(posCashierAccount.getCashierPhoto());
            }

            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(registerVo);
            return BasicResult.createSuccessResult("获取pos营业员信息成功!", jsonObject);
        } else {
            return BasicResult.createSuccessResult("此账号是商家账号还没有被创建pos店长账号");
        }

    }
}
