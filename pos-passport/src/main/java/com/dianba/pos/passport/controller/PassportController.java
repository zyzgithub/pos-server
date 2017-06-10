package com.dianba.pos.passport.controller;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.service.PosProtocolManager;
import com.dianba.pos.passport.vo.PassportVo;
import com.dianba.pos.passport.vo.RegisterVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(PassportURLConstant.BASE_URL)
public class PassportController {


    private static Logger logger = LogManager.getLogger(PassportController.class);

    @Autowired
    private PassportManager passportManager;

    @Autowired
    private PosProtocolManager posProtocolManager;

    @RequestMapping("loginPassport")
    @ResponseBody
    public BasicResult loginPassport(PassportVo passportVo) {
        return passportManager.login(passportVo);
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
        return passportManager.register(registerVo);
    }

    /**
     * 编辑商家营业员信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("editPosAccount")
    public BasicResult editPosAccount(RegisterVo registerVo) {
        return passportManager.editPosAccount(registerVo);
    }


    /**
     * 删除营业员信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePosAccount")
    public BasicResult deletePosAccount(RegisterVo registerVo) {
        return passportManager.deletePosAccount(registerVo);
    }

    /**
     * 获取商家营业员信息成功。
     *
     * @param registerVo
     * @return
     */
    @ResponseBody
    @RequestMapping("getMerchantPosList")
    public BasicResult getMerchantPosList(RegisterVo registerVo) {
        return passportManager.getMerchantPosList(registerVo);
    }


    @ResponseBody
    @RequestMapping("getCashierById")
    public BasicResult getCashierById(RegisterVo registerVo) {
        return passportManager.getCashierById(registerVo);
    }

    @ResponseBody
    @RequestMapping("getPosProtocolAll")
    public BasicResult getPosProtocolAll(){

        return posProtocolManager.findAll();
    }

    @ResponseBody
    @RequestMapping("getPosProtocolById")
    public BasicResult getPosProtocolById(Long id){

        return posProtocolManager.findPosProtocolById(id);
    }
}
