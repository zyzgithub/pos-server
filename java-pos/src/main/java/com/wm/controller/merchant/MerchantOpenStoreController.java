package com.wm.controller.merchant;

import com.wm.dto.merchant.MerchantOpenStoreDto;
import com.wm.service.merchant.MerchantOpenStoreServiceI;
import com.wm.util.AliOcs;
import com.wm.util.spring.json.JsonParam;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 商家自助开店
 *
 * @author syq
 */
@Controller
@RequestMapping("merchantOpenStoreController")
public class MerchantOpenStoreController extends BaseController {
    Logger logger = LoggerFactory.getLogger(MerchantOpenStoreController.class);

    @Resource
    private MerchantOpenStoreServiceI merchantOpenStoreService;

    /**
     * 验证手机号是否已被商家注册
     *
     * @param phone
     * @return
     */
    @RequestMapping(params = "ifExistPhone")
    @ResponseBody
    public AjaxJson ifExistPhone(String phone) {
        logger.info("《商家自助开店注册接口》, ifExistPhone begin....");
        return merchantOpenStoreService.ifExistPhone(phone);
    }

    /**
     * 商家自助开店注册接口
     *
     * @param merchantOpenStoreDto
     * @return
     */
    @RequestMapping(params = "confirmRegister")
    @ResponseBody
    public AjaxJson confirmRegister(@JsonParam(keyname = "datas", isResolveBody = true) MerchantOpenStoreDto merchantOpenStoreDto) {
        logger.info("《商家自助开店注册接口》，confirmRegister begin....");
        return merchantOpenStoreService.confirmRegister(merchantOpenStoreDto);
    }

    /**
     * 商家忘记密码接口
     *
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(params = "retrievePassword")
    @ResponseBody
    public AjaxJson retrievePassword(String phone, String code, String password) {
        logger.info("《商家忘记密码接口》，retrievePassword begin....");
        String catch_code = AliOcs.get(phone);
        logger.info("retrievePassword catch_code : " + catch_code + " ,code : " + code);

        if (StringUtils.isBlank(catch_code)) {
            return AjaxJson.failJson("手机号码不正确.");
        }

        if (!catch_code.equals(code)) {
            return AjaxJson.failJson("验证码不正确.");
        }

        return merchantOpenStoreService.retrievePassword(phone, password);
    }

    /**
     * 获取省和市
     *
     * @return
     */
    @RequestMapping(params = "getOrgList")
    @ResponseBody
    public AjaxJson getOrgList() {
        logger.info("《商家开通外卖服务接口》，getOrgList begin....");
        return merchantOpenStoreService.getOrgList();
    }

    /**
     * 商家开通外卖服务接口
     *
     * @param merchantOpenStoreDto
     * @return
     */
    @RequestMapping(params = "openWaimai")
    @ResponseBody
    public AjaxJson openWaimai(@JsonParam(keyname = "datas", isResolveBody = true) MerchantOpenStoreDto merchantOpenStoreDto) {
        logger.info("《商家开通外卖服务接口》，openWaimai begin....");
        return merchantOpenStoreService.openWaimai(merchantOpenStoreDto);
    }

    /**
     * 获取商家申请外卖信息
     *
     * @param merchantId
     * @return
     */
    @RequestMapping(params = "getApplyWaimaiStatus")
    @ResponseBody
    public AjaxJson getApplyWaimaiStatus(Integer merchantId) {
        logger.info("《商家开通外卖服务接口》，getApplyWaimaiStatus begin....");
        return merchantOpenStoreService.getApplyWaimaiStatus(merchantId);
    }

}
