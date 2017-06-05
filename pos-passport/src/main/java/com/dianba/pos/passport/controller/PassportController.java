package com.dianba.pos.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.po.LifePassportAlias;
import com.dianba.pos.passport.po.LifePassportProperties;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosCashierAccount;
import com.dianba.pos.passport.repository.LifePassportAliasJpaRepository;
import com.dianba.pos.passport.repository.LifePassportPropertiesJpaRepository;
import com.dianba.pos.passport.repository.PassportJpaRepository;
import com.dianba.pos.passport.repository.PosCashierAccountJpaRepository;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.vo.LoginVo;
import com.dianba.pos.passport.vo.PassportVo;
import com.dianba.pos.passport.vo.RegisterVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(PassportURLConstant.BASE_URL)
public class PassportController {


    private static Logger logger = LogManager.getLogger(PassportController.class);

    @Autowired
    private PassportProperties passportProperties;


    @Autowired
    private PosCashierAccountJpaRepository posCashierAccountJpaRepository;

    @Autowired
    private PassportJpaRepository passportJpaRepository;

    @Autowired
    private PassportManager passportManager;

    @Autowired
    private LifePassportAliasJpaRepository lifePassportAliasJpaRepository;

    @Autowired
    private LifePassportPropertiesJpaRepository lifePassportPropertiesJpaRepository;

    @RequestMapping("loginPassport")
    @ResponseBody
    public BasicResult loginPassport(PassportVo passportVo) {

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

            if (a != null) {

                return BasicResult.createFailResult("您还没有签约，请联系本公司。");
            } else if (b != null) {
                passportVo.setClientType(4);
                JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);
                logger.info(passportProperties.getLogin());
                JSONObject response = jsonObject.getJSONObject("response");
                String msg = jsonObject.getString("msg");
                logger.info("pos 端登录返回结果：" + jsonObject.toJSONString());
                if (jsonObject.getIntValue("code") != 0) {
                    return BasicResult.createFailResult(msg);
                } else {
                    LoginVo loginVo = (LoginVo) JSONObject.parseObject(response.toString(), LoginVo.class);
                    loginVo.setAccountType(0);
                    loginVo.setAccountTypeName("店长");
                    PosCashierAccount posCashierAccount=posCashierAccountJpaRepository.findPosCashierAccountByCashierId(
                            loginVo.getPassportId());
                    loginVo.setPassportId(posCashierAccount.getMerchantId());
                    JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(loginVo);
                    return BasicResult.createSuccessResult(msg, jsonObject1);
                }
            } else if (c != null) {
                passportVo.setClientType(2);
                JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);
                logger.info(passportProperties.getLogin());
                JSONObject response = jsonObject.getJSONObject("response");

                String msg = jsonObject.getString("msg");
                logger.info("pos 端登录返回结果：" + jsonObject.toJSONString());
                if (jsonObject.getIntValue("code") != 0) {
                    return BasicResult.createFailResult(msg);
                } else {
                    LoginVo loginVo = (LoginVo) JSONObject.parseObject(response.toString(), LoginVo.class);
                    loginVo.setAccountType(1);
                    loginVo.setAccountTypeName("店员");
                    JSONObject jsonObject1=(JSONObject)JSONObject.toJSON(loginVo);
                    return BasicResult.createSuccessResult(msg, response);
                }
            } else {
                return BasicResult.createFailResult("登录出现异常");
            }

        }


    }

    /***
     * 注册收银员账号
     * @return passportId - long 通行证ID
    showName - String 可用于展示的名字
    phoneNumber - String 手机号（已隐藏部分字符，前端直接展示即可）
    headImage - String 头像地址
    roleValue - int 角色类型值，参考：PassportRoleTypeEnum
    accessToken - String 访问令牌(暂时未使用)
     */
    @ResponseBody
    @RequestMapping("registerPosAccount")
    public BasicResult registerPosAccount(RegisterVo registerVo) {

        logger.info("====================收银员账号注册==================");

        registerVo.setClientType(2);
        logger.info("注册账号：" + registerVo.getName());
        JSONObject jsonObject = HttpUtil.post(passportProperties.getRegister(), registerVo);

        logger.info("注册返回：" + jsonObject.toJSONString());
        JSONObject response = jsonObject.getJSONObject("response");
        String msg = jsonObject.getString("msg");
        if (jsonObject.getIntValue("code") == 0) {
            Long passportId = response.getLong("passportId");
            logger.info("accouontId:" + registerVo.getAccountId().toString());
            PosCashierAccount posCashierAccount = new PosCashierAccount();
            posCashierAccount.setMerchantId(registerVo.getAccountId());
            posCashierAccount.setAccountType(1);
            logger.info("passportId:" + passportId);
            posCashierAccount.setCashierId(passportId);
            posCashierAccount.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
            posCashierAccountJpaRepository.save(posCashierAccount);

            PosCashierAccount posCashierAccount1 = posCashierAccountJpaRepository
                    .findPosCashierAccountByMerchantIdAndAccountType(registerVo.getAccountId(), 0);
            if (posCashierAccount1 == null) {
                posCashierAccount1 = new PosCashierAccount();
                posCashierAccount1.setAccountType(0);
                posCashierAccount1.setCashierPhoto(registerVo.getCashierPhoto());
                posCashierAccount1.setCreateTime(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                posCashierAccount1.setCashierId(passportId);
                posCashierAccount1.setMerchantId(registerVo.getAccountId());
            }
            return BasicResult.createSuccessResult("注册成功", response);
        } else {

            return BasicResult.createFailResult(msg);
        }
    }

    /**
     * 编辑商家营业员信息
     *
     * @param passport
     * @return
     */
    @ResponseBody
    @RequestMapping("editPosAccount")
    public BasicResult editPosAccount(Passport passport) {
        passportJpaRepository.save(passport);
        PosCashierAccount posCashierAccount = posCashierAccountJpaRepository.findPosCashierAccountByCashierId(
                passport.getId());
        posCashierAccount.setCashierPhoto(passport.getCashierPhoto());
        posCashierAccount.setAccountType(passport.getAccountType());
        posCashierAccountJpaRepository.save(posCashierAccount);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);
    }


    /**
     * 删除营业员信息
     *
     * @param passport
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePosAccount")
    public BasicResult deletePosAccount(Passport passport) {

        if (passport.getId() == null) {

            return BasicResult.createFailResult("请输入要删除的主键id");
        } else {
            passportJpaRepository.delete(passport);
            return BasicResult.createSuccessResult("删除pos营业员信息成功!");

        }

    }

    /**
     * 获取商家营业员信息成功。
     *
     * @param passportId
     * @return
     */
    @ResponseBody
    @RequestMapping("getMerchantPosList")
    public BasicResult getMerchantPosList(Long passportId) {

        List<PosCashierAccount> posCashierAccountList = posCashierAccountJpaRepository.findAllByMerchantId(passportId);

        List<Passport> passports = new ArrayList<>();
        for (PosCashierAccount posCashierAccount : posCashierAccountList) {

            Passport passport = passportJpaRepository.findOne(posCashierAccount.getCashierId());
            passport.setAccountType(posCashierAccount.getAccountType());
            passports.add(passport);
        }
        return BasicResult.createSuccessResultWithDatas("获取商家营业员信息成功!", passports);


    }


    @ResponseBody
    @RequestMapping("getCashierById")
    public BasicResult getCashierById(Long cashierId) {
        PosCashierAccount posCashierAccount = posCashierAccountJpaRepository
                .findPosCashierAccountByCashierId(cashierId);
        Passport passport = passportJpaRepository.findOne(cashierId);
        passport.setAccountType(posCashierAccount.getAccountType());
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("获取pos营业员信息成功!", jsonObject);

    }

    @ResponseBody
    @RequestMapping("getTest")
    public BasicResult getTest(Long cashierId) {
        Passport passport = passportManager.getPassportInfoByCashierId(100057L);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);
    }
}
