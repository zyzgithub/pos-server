package com.dianba.pos.extended.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.extended.config.ExtendedUrlConstant;
import com.dianba.pos.extended.mapper.Charge19eMapper;
import com.dianba.pos.extended.po.PhoneInfo;
import com.dianba.pos.extended.service.Charge19eManager;
import com.dianba.pos.extended.service.PhoneInfoManager;
import com.dianba.pos.extended.service.TsmCountryAreaManager;
import com.dianba.pos.extended.util.FlowCharge19EApi;
import com.dianba.pos.extended.util.FlowCharge19EUtil;
import com.dianba.pos.extended.util.FlowOrderStatus;
import com.dianba.pos.extended.vo.ChargeCallBack;
import com.dianba.pos.extended.vo.FlowChargeCallBack;
import com.dianba.pos.extended.vo.Product;
import com.dianba.pos.extended.vo.ProductListDto;
import com.dianba.pos.menu.mapper.MenuMapper;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.service.MenuManager;
import com.dianba.pos.menu.vo.MenuDto;
import com.dianba.pos.order.mapper.OrderMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

@Controller
@SuppressWarnings("all")
@RequestMapping(ExtendedUrlConstant.CHARGE_19E_INFO)
public class Charge19EController {

    private static Logger logger = LogManager.getLogger(Charge19EController.class);
    @Autowired
    private MenuManager menuManager;
    @Autowired
    private PhoneInfoManager phoneInfoManager;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private TsmCountryAreaManager tsmCountryAreaManager;

    @Autowired
    private Charge19eMapper charge19eMapper;

    @Autowired
    private Charge19eManager charge19eManager;


    /**
     * 19e 话费充值平台
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "hfChargeBy19e")
    public AjaxJson hfChargeBy19e(HttpServletResponse response, String chargeNumber, String chargeMoney,
                                  String fillType, String chargeType) {

        AjaxJson aj = new AjaxJson();

        charge19eManager.orderListHfCharge();

        return new AjaxJson();
    }

    /**
     * 19e 话费充值平台
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "flowChargeBy19e")
    public AjaxJson flowChargeBy19e(HttpServletResponse response, String chargeNumber, String chargeMoney,
                                    String fillType, String chargeType) {

        AjaxJson aj = new AjaxJson();

        charge19eManager.orderListFlowCharge();

        return new AjaxJson();
    }

    /**
     * 话费充值回调方法
     **/
    @RequestMapping("hfChargeBack")
    @ResponseBody
    public String hfChargeBack(ChargeCallBack chargeCallBack) {
        if (!StringUtil.isEmpty(chargeCallBack.getChargeStatus())) {
            logger.info("话费充值回调类：========");
            String merchantOrderId = chargeCallBack.getMerchantOrderId();
           // Long times = Long.parseInt(DateUtil.currentTimeMillis().toString());
            if (chargeCallBack.getChargeStatus().equals("SUCCESS")) {

                //查询此订单是否更新完毕
                Object ob = orderMapper.getByPayId(merchantOrderId);
                if (ob!=null&!ob.equals("success")) {
                    //修改订单信息为success
                    String date = DateUtil.getCurrDate("yyyyMMddHHmmss");
                    orderMapper.editOrderInfoBy19e("success", merchantOrderId, 0);
                    //改变第三方订单状态
                    charge19eMapper.editCharge19e("success", date, merchantOrderId);
                    logger.info("话费订单充值成功!" + ",订单号为：" + chargeCallBack.getMerchantOrderId() + ",充值金额为：");
                }

            } else {
                logger.info("话费充值回调返回：ERROR=====================");
                orderMapper.editOrderInfoBy19e("error", merchantOrderId, 0);
            }
        }

        return chargeCallBack.callback();

    }

    @ResponseBody
    @RequestMapping("flowChargeCallBack")
    public String flowChargeCallBack(FlowChargeCallBack chargeCallBack) {
        logger.info("进入流量充值回调：");
        String result = "";
        if (!StringUtil.isEmpty(chargeCallBack.getMerOrderNo())) {
            Map map = new HashMap<>();
            map.put("merOrderNo", chargeCallBack.getMerOrderNo());
            map.put("orderNo", chargeCallBack.getOrderNo());
            map.put("orderStatus", chargeCallBack.getOrderStatus());
            //签名认证通过
            //充值成功
            String merOrderNo = chargeCallBack.getMerOrderNo();
            //Integer times = Integer.parseInt(DateUtil.currentTimeMillis().toString());
            if (FlowOrderStatus.ChargeSuccess.getIndex().equals(chargeCallBack.getOrderStatus())) {
                logger.info("流量充值回调返回为SUCCESS状态：==========");
                //修改订单信息为success
                String date = DateUtil.getCurrDate("yyyyMMddHHmmss");
                Object ob = orderMapper.getByPayId(merOrderNo);
                if (ob!=null&!ob.equals("success")) {
                    //改变原订单状态
                    orderMapper.editOrderInfoBy19e("success", merOrderNo, 0);
                    //改变第三方订单状态
                    charge19eMapper.editCharge19e("success", date, merOrderNo);
                }
                result = "resultCode=SUCCESS";
                logger.info("流量充值回调操作成功!SUCCESS，");
            } else {
                orderMapper.editOrderInfoBy19e("error", merOrderNo, 0);
                logger.info("流量充值回调返回充值失败!ERROR");
            }

        } else {
            result = "resultCode=ERROR";
        }

        return result;
    }

    /**
     * 增值服务商品信息
     *
     * @param type
     */
    @ResponseBody
    @RequestMapping("chargeMenu")
    public AjaxJson chargeMenu(String type, String phone) {
        AjaxJson aj = new AjaxJson();
        JSONObject jo = new JSONObject();
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(phone)) {
            aj.setSuccess(false);
            aj.setMsg("参数不能为空!");
            aj.setStateCode("01");
            logger.error("参数不能为空!");
        } else {
            Integer isFlash = Integer.parseInt(type);
            String mobilePrefix = phone.substring(0, 7);
            Long phonel = Long.parseLong(mobilePrefix);
            PhoneInfo phoneInfo = phoneInfoManager.findByMobileNumber(phonel);
            if (phoneInfo == null) {
                aj.setMsg("手机号码不存在!");
                aj.setSuccess(false);
            } else {
                if (type.equals("3")) {
                    // 关联menu表中的print_type ; 1.电信2.联通3.移动;
                    Long id = Long.parseLong(phoneInfo.getPrintType().toString());
                    List<MenuDto> menulst = menuMapper.getMenuListByPhoneAndType(-1L, phonel, isFlash);
                    jo.put("phoneInfo", phoneInfo);
                    jo.put("menuList", menulst);
                } else if (type.equals("4")) {
                    Product pd = new Product();
                    pd.setMobile(phone);
                    pd.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
                    String result = FlowCharge19EApi.queryProduct(FlowCharge19EUtil.QUERY_PRODUCT, pd);
                    JSONObject jb = JSON.parseObject(result);
                    List<MenuDto> menulst = new ArrayList<>();
                    if (jb.get("resultCode").equals("00000") && jb.get("resultDesc").equals("SUCCESS")) {
                        JSONArray ja = jb.getJSONArray("productList");
                        List<ProductListDto> lst = JSONArray.parseArray(ja.toString(), ProductListDto.class);
                        logger.info("流量充值商品列表信息：====" + ja.toString());
                        for (ProductListDto pl : lst) {
                            //根据第三方商品id获取本地商品信息
                            String productId = pl.getProductId();
                            logger.info("根据第三方商品id获取本地商品:====" + productId);
                            Menu menu = menuManager.findByMenuKeyAndDisplayAndIsDelete(productId, "Y", "N");
                            MenuDto menuDto = new MenuDto();
                            if (menu != null) {
                                menuDto.setMenuId(menu.getId().toString());
                                menuDto.setType(4);
                                menuDto.setMenuName(menu.getName());
                                menuDto.setPrice(menu.getPrice());
                                menuDto.setStockPrice(menu.getOriginalPrice());
                                menulst.add(menuDto);
                            }
                        }
                    }
                    jo.put("phoneInfo", phoneInfo);
                    jo.put("menuList", menulst);
                }
            }
        }
        aj.setObj(jo);
        return aj;
    }
}
