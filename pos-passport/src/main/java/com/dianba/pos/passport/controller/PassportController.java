package com.dianba.pos.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.po.PosCashierAccount;
import com.dianba.pos.passport.repository.PassportJpaRepository;
import com.dianba.pos.passport.repository.PosCashierAccountJpaRepository;
import com.dianba.pos.passport.service.PassportManager;
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

    @RequestMapping("loginPassport")
    @ResponseBody
    public BasicResult loginPassport(PassportVo passportVo) {

        logger.info("登录用户名：" + passportVo.getUsername());
        JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);
        logger.info(passportProperties.getLogin());
        JSONObject response = jsonObject.getJSONObject("response");
        String msg = jsonObject.getString("msg");
        logger.info("pos 端登录返回结果：" + jsonObject.toJSONString());
        if (jsonObject.getIntValue("code") != 0) {
            return BasicResult.createFailResult(msg);
        } else {
            return BasicResult.createSuccessResult(msg, response);
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
            logger.info("passportId:" + passportId);
            posCashierAccount.setCashierId(passportId);
            posCashierAccountJpaRepository.save(posCashierAccount);
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
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);
    }

    /**
     * 删除营业员信息
     * @param passport
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePosAccount")
    public BasicResult deletePosAccount(Passport passport){

        if(passport.getId()==null){

            return BasicResult.createFailResult("请输入要删除的主键id");
        }else {
            passportJpaRepository.delete(passport);
            return  BasicResult.createSuccessResult("删除pos营业员信息成功!");

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
            passports.add(passport);
        }
        return BasicResult.createSuccessResultWithDatas("获取商家营业员信息成功!", passports);


    }

    @ResponseBody
    @RequestMapping("getCashierById")
    public BasicResult getCashierById(Long cashierId) {

        Passport passport = passportJpaRepository.findOne(cashierId);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);

    }

    @ResponseBody
    @RequestMapping("getTest")
    public BasicResult getTest(Long cashierId) {
        Passport passport = passportManager.getPassportInfoByCashierId(100057L);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(passport);
        return BasicResult.createSuccessResult("编辑pos营业员信息成功!", jsonObject);
    }
}
