package com.wm.service.impl.merchant;

import com.sun.star.uno.RuntimeException;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.user.UserloginEntity;
import com.wm.entity.user.WAccountEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantSupplyServiceI;
import com.wp.ConfigUtil;
import com.wxpay.service.WxPayService;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.oConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("merchantSupplyService")
@Transactional
public class MerchantSupplyServiceImpl extends CommonServiceImpl implements MerchantSupplyServiceI {
    Logger logger = LoggerFactory.getLogger(MerchantSupplyServiceImpl.class);

    @Autowired
    private FlowServiceI flowService;

    @Override
    public AjaxJson validatePayPassword(Integer userId, String payPassword) {
        AjaxJson aj = new AjaxJson();
        WUserEntity user = this.get(WUserEntity.class, userId);
        if (user != null) {
            String pw = user.getPayPassword();
            //logger.info("商家密码：" + pw + "，用户输入密码：" + payPassword);
            if (pw != null && pw.equals(payPassword)) {
                aj.setMsg("支付密码正确");
                aj.setStateCode("00");
                aj.setSuccess(true);
            } else {
                aj.setMsg("支付密码错误");
                aj.setStateCode("02");
                aj.setSuccess(false);
            }
        } else {
            aj.setMsg("商家不存在");
            aj.setStateCode("02");
            aj.setSuccess(false);
        }
        return aj;
    }

    @Override
    public AjaxJson merchantLogin(String phone) {
        AjaxJson json = new AjaxJson(true, "登录成功", "00");

        String sql = " select id from user where username = ? and user_type = 'merchant' and is_delete = '0' ";
        List<Map<String, Object>> userMapList = this.findForJdbc(sql, phone);

        Integer userId = null;
        if (userMapList != null && userMapList.size() > 0) {
            Map<String, Object> usermap = userMapList.get(0);
            userId = Integer.parseInt(String.valueOf(usermap.get("id")));
        }
        logger.info("供应链，1.5项目《H5页面通过手机号登录接口》, 是否存在该手机号码的用户:phone={},userId={}", phone, userId);

        if (userId == null) {
            //添加商家用户 user
            WUserEntity wuser = new WUserEntity();
            wuser.setUsername(phone);
            wuser.setPassword("");
            wuser.setNickname("");
            wuser.setUserType("merchant");
            wuser.setMobile(phone);
            wuser.setUserState(1); //有效
            wuser.setIsDelete(0);
            this.save(wuser);
            userId = wuser.getId();

            //添加商家 merchant
            MerchantEntity merchant = new MerchantEntity();
            merchant.setTitle(phone);
            merchant.setMobile(phone);
            merchant.setDisplay("N"); //关店
            merchant.setWuser(wuser);
            merchant.setCreateTime(DateUtils.getSeconds());
            this.save(merchant);

            //添加 MerchantInfo
            String updateMerchantInfoSql = " insert 0085_merchant_info(merchant_id,merchant_source) values (?,?)";
            this.executeSql(updateMerchantInfoSql, merchant.getId(), 9);  //9为商品采购商

            //添加用户登录记录 user_login
            UserloginEntity userLogin = new UserloginEntity();
            userLogin.setChannel("unknow");
            userLogin.setIp("0.0.0.0");
            userLogin.setLoginTime((int) System.currentTimeMillis() / 1000);
            userLogin.setWuser(wuser);
            this.save(userLogin);
        }
        if (userId != null) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("userId", userId);
            obj.put("username", phone);
            obj.put("nickname", phone);
            obj.put("mobile", phone);
            obj.put("money", 0);
            obj.put("userType", "merchant");

            MerchantEntity merchant = this.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId);
            if (merchant != null) {
                obj.put("merchantId", merchant.getId());
                obj.put("merchantName", merchant.getTitle());
                obj.put("merchantSource", "9");  //9为商品采购商
                obj.put("storeType", "2");  //分店(0)、总店(1)、普通店铺(2)、供应链(3)
                obj.put("printCode", "");
            } else {
                logger.info("供应链，1.5项目《H5页面通过手机号登录接口》, 商家账号不存在");
                json.setMsg("商家账号不存在");
                json.setSuccess(false);
                json.setStateCode("01");
                return json;
            }
            json.setObj(obj);
        } else {
            logger.info("供应链，1.5项目《H5页面通过手机号登录接口》, 系统异常，登录失败");
            json.setSuccess(false);
            json.setMsg("系统异常，登录失败");
            json.setStateCode("01");
        }
        return json;
    }

    @Override
    public Double getMerchantUserMoney(Integer userId) {
        WUserEntity user = this.get(WUserEntity.class, userId);
        if (user != null) {
            return user.getMoney();
        }
        return null;
    }

    @Override
    public Double getMerchantAccountMoney(Integer userId) {
    	WAccountEntity account = this.findUniqueByProperty(WAccountEntity.class, "userId",userId);
        if (account != null) {
            return account.getBalance().doubleValue();
        }
        return 0.0;
    }
    
    @Override
    public AjaxJson balanceConfirmPay(Long orderId, Integer userId, BigDecimal money, String type, String payType, String detail) {
        AjaxJson json = new AjaxJson();
        try {
            flowService.merchantSupplyOrderPay(orderId, userId, money, type, payType, detail);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return json;
    }

    @Override
    public AjaxJson weixinConfirmPay(String payId, String money, String returnCallUrl, String code) {
        AjaxJson json = new AjaxJson();
        try {
            json = this.getAppWxPayParam(payId, money, returnCallUrl, code);
        } catch (Exception e) {
            json.setMsg("系统异常，余额支付失败");
            json.setSuccess(false);
            json.setStateCode("02");
        }
        return json;
    }

    /**
     * 商家APP获取微信支付参数
     *
     * @param payId
     * @param money
     * @param returnCallUrl
     * @param code 支付账号类型，0-fir市场配置（账期为7），1-appstore配置，2-新fir市场配置（账期为0）
     * @return
     * @throws Exception
     */
    public AjaxJson getAppWxPayParam(String payId, String money, String returnCallUrl, String code) throws Exception {
        AjaxJson j = new AjaxJson();
        String body = "订单支付";
        String tradeType = "APP";
        
        // 默认：fir市场配置
        String MERCHANT_APP_ID = ConfigUtil.MERCHANT_APP_ID;
        String MERCHANT_MCH_ID = ConfigUtil.MERCHANT_MCH_ID;
        String MERCHANT_API_KEY = ConfigUtil.MERCHANT_API_KEY;

        if ("1".equals(code)) {
            MERCHANT_APP_ID = ConfigUtil.APP_STORE_MERCHANT_APP_ID;
            MERCHANT_MCH_ID = ConfigUtil.APP_STORE_MERCHANT_MCH_ID;
            MERCHANT_API_KEY = ConfigUtil.APP_STORE_MERCHANT_API_KEY;
        } else if("2".equals(code)){
        	MERCHANT_APP_ID = ConfigUtil.MERCHANT_APP_ID_FIR;
            MERCHANT_MCH_ID = ConfigUtil.MERCHANT_MCH_ID_FIR;
            MERCHANT_API_KEY = ConfigUtil.MERCHANT_API_KEY_FIR;
        }

        Map<String, String> wxpay = WxPayService.getAppWxPay(MERCHANT_APP_ID, MERCHANT_MCH_ID,
                MERCHANT_API_KEY, body, money, payId, oConvertUtils.getIp(), returnCallUrl, tradeType);

        if (wxpay.get("return_code").equals("SUCCESS") && wxpay.get("result_code").equals("SUCCESS")) {
            j.setObj(wxpay);
            j.setSuccess(true);
            j.setStateCode("00");
            j.setMsg("微信APP支付参数生成成功");
            logger.info("商家供应链，订单支付操作，微信APP支付参数生成成功:{}", wxpay.toString());
        } else {
            j.setSuccess(false);
            j.setMsg("微信APP支付参数生成失败:" + wxpay.get("return_msg"));
            j.setStateCode("02");
            logger.error("商家供应链，订单支付操作，微信APP支付参数生成失败：wxpay result_code={},return_msg={}",
                    wxpay.get("result_code"), wxpay.get("return_msg"));
        }
        return j;
    }

    @Override
    public AjaxJson userIdsWhitchMerchantNotAvailable(String merchantUserIds) {
        AjaxJson result = new AjaxJson();

		/*
		 * 筛选出已关店、已删除的商家用户ID
		 */
        StringBuilder sql = new StringBuilder(" SELECT m.user_id ");
        sql.append(" FROM merchant m ");
        sql.append(" WHERE (m.display = 'N' OR m.is_delete = 1) AND m.user_id IN ( ");
        sql.append(merchantUserIds);
        sql.append(") ");
        List<Integer> userId = this.findListbySql(sql.toString());

        result.setObj(userId);
        return result;
    }

}