//package com.dianba.pos.extended.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.JSONArray;
//import com.dianba.pos.common.util.AjaxJson;
//import com.dianba.pos.common.util.DateUtil;
//import com.dianba.pos.common.util.StringUtil;
//
//import com.dianba.pos.extended.config.ExtendedUrlConstant;
//import com.dianba.pos.extended.po.PhoneInfo;
//import com.dianba.pos.extended.service.PhoneInfoManager;
//import com.dianba.pos.extended.service.TsmCountryAreaManager;
//import com.dianba.pos.extended.util.FlowCharge19EApi;
//import com.dianba.pos.extended.util.FlowCharge19EUtil;
//import com.dianba.pos.extended.vo.*;
//import com.dianba.pos.menu.mapper.MenuMapper;
//import com.dianba.pos.menu.po.Menu;
//import com.dianba.pos.menu.service.MenuManager;
//import com.dianba.pos.menu.vo.MenuDto;
//import com.dianba.pos.order.mapper.OrderMapper;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.reflect.TypeToken;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Administrator on 2017/5/8 0008.
// */
//
//@Controller
//@SuppressWarnings("all")
//@RequestMapping(ExtendedUrlConstant.Charge19E_INFO)
//public class Charge19EController {
//
//    private static Logger logger = LogManager.getLogger(Charge19EController.class);
//    @Autowired
//    private MenuManager menuManager;
//    @Autowired
//    private PhoneInfoManager phoneInfoManager;
//
//    @Autowired
//    private MenuMapper menuMapper;
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//    @Autowired
//    private TsmCountryAreaManager tsmCountryAreaManager;
//
//    /**
//     * 19e 话费充值平台
//     *
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "hfChargeBy19e")
//    public AjaxJson hfChargeBy19e(HttpServletResponse response, String chargeNumber, String chargeMoney, String fillType, String chargeType) {
//        AjaxJson aj = new AjaxJson();
//
//
////        String result=   Charge19EApi.hfCharge(Charge19EUtil.HF_CHARGE_19E_URL,ch);
////        JSONObject jo= JSONObject.parseObject(result);
////        String resultCode=jo.getString("resultCode");
////        String resultDesc=jo.getString("resultDesc");
////        String ehfOrderId=jo.getString("ehfOrderId");
////        String queryResultUrl=jo.getString("queryResultUrl");
////        String payMoney=jo.getString("payMoney");
////        String merchantOrderId=jo.getString("merchantOrderId");
////
////        if(resultCode.equals("success")){
////
////
////        }
////        System.out.println(result);
//        return new AjaxJson();
//    }
//
//    /**
//     * 话费充值回调方法
//     **/
//    @RequestMapping("hfChargeBack")
//    @ResponseBody
//    public String hfChargeBack(ChargeCallBack chargeCallBack) {
//
//        logger.info("话费充值回调类：" + chargeCallBack.callback().toString());
//
//        if (chargeCallBack.getChargeStatus().equals("SUCCESS")) {
//            Long orderId = Long.parseLong(chargeCallBack.getMerchantOrderId());
//            //查询此订单是否更新完毕
//            Object ob = orderMapper.getByPayId(orderId);
//            if (!ob.equals("success")) {
//                //修改订单信息为success
//                Integer times = Integer.parseInt(DateUtil.currentTimeMillis().toString());
//                orderMapper.editOrderInfoBy19e("success", orderId, times);
//            }
//
//        }
//        return chargeCallBack.callback();
//
//
//    }
//
//    /**
//     * 增值服务商品信息
//     *
//     * @param type
//     */
//    @RequestMapping("chargeMenu")
//    @ResponseBody
//    public AjaxJson ChargeMenu(String type, String phone) {
//        AjaxJson aj = new AjaxJson();
//        JSONObject jo = new JSONObject();
//        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(phone)) {
//            aj.setSuccess(false);
//            aj.setMsg("参数不能为空!");
//            aj.setStateCode("01");
//            logger.error("参数不能为空!");
//        } else {
//            Integer isFlash = Integer.parseInt(type);
//            String mobilePrefix = phone.substring(0, 7);
//            Long phonel = Long.parseLong(mobilePrefix);
//            PhoneInfo phoneInfo = phoneInfoManager.findByMobileNumber(phonel);
//            if (type.equals("3")) {
//
//
//                if (phoneInfo == null) {
//
//                    aj.setMsg("手机号码不存在!");
//                    aj.setSuccess(false);
//                } else {
//                    // 关联menu表中的print_type ; 1.电信2.联通3.移动;
//                    Long id = Long.parseLong(phoneInfo.getPrintType().toString());
//
//                    List<MenuDto> menulst = menuMapper.getMenuListByPhoneAndType(-1L, phonel, isFlash);
//
//                    jo.put("phoneInfo", phoneInfo);
//                    jo.put("menuList", menulst);
//                }
//            } else if (type.equals("4")) {
//                Product pd = new Product();
//                pd.setMobile(phone);
//                pd.setMerchantId(FlowCharge19EUtil.MERCHANT_ID);
//                String result = FlowCharge19EApi.queryProduct(FlowCharge19EUtil.QUERY_PRODUCT, pd);
//                JSONObject jb = JSON.parseObject(result);
//
//                if (jb.get("resultCode").equals("00000") && jb.get("resultDesc").equals("SUCCESS")) {
//
//                    JSONArray ja = jb.getJSONArray("productList");
//                    Gson gson = new Gson();
//                    List<ProductListDto> lst  = gson.fromJson(ja.toString(), new TypeToken<List<ProductListDto>>() {
//                    }.getType());
//                    List<MenuDto> menulst =new ArrayList<>();
//                    for(ProductListDto pl : lst){
//                        MenuDto menuDto=new MenuDto();
//                        menuDto.setMenuId(pl.getProductId());
//                        menuDto.setType(4);
//                        menuDto.setMenuName(pl.getFlowValue()+"M");
//                        menuDto.setPrice(Double.parseDouble(pl.getProductPrice()));
//                        menuDto.setStockPrice(Double.parseDouble(pl.getSalePrice()));
//                        menulst.add(menuDto);
//
//
//                     }
//
//                    jo.put("phoneInfo", phoneInfo);
//                    jo.put("menuList", menulst);
//                }
//
//            }
//
//        }
//        aj.setObj(jo);
//
//        return aj;
//    }
//}
