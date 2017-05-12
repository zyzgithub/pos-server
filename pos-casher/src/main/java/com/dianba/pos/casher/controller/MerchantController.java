package com.dianba.pos.casher.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.merchant.mapper.MerchantMapper;
import com.dianba.pos.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/25 0025.
 * zyz
 * 商家控制器
 */
@Controller
@RequestMapping("/merchant")
@SuppressWarnings("all")
public class MerchantController {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchantMapper merchantMapper;


    @ResponseBody
    @RequestMapping(value = "getMerchantProfitInfo")
    public AjaxJson getMerchantProfitInfo(String name, String phone
            , @RequestParam(name = "id_card") String idCard, HttpServletRequest request) {
        boolean flag = true;
        String msg = "";
        Object obj = null;
        String stateCode = "00";

        JSONObject jo = new JSONObject();

        System.out.println("name" + name);
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(idCard)) {
            flag = false;
            msg = "参数输入有误!";
            stateCode = "01";

        } else {

            Map<String, Object> map = orderMapper.verifyMerchantUser(name, idCard, phone);

            //说明是不是pos商家用户

            if (map.get("num").toString().equals("0")) {
                flag = false;
                msg = "该用户不是pos商家用户!";
                stateCode = "01";
            } else {
                //取得商家id
                String merchantId = map.get("merchant_id").toString();

                String userId = map.get("user_id").toString();
                //获取要查询的月数
                String month = request.getParameter("month");
                Integer months = Integer.parseInt(month);
                //先获取商家开始使用pos的时间
                Long createTime = orderMapper.getPosStrtTimeByMerchant(Long.parseLong(merchantId));
                //获取商家注册时间
                Long createTime2 = merchantMapper.getMerchantCreate(Long.parseLong(merchantId));
                //获取当前时间戳
                Long nowTime = Long.parseLong(DateUtil.currentTimeMillis().toString().substring(0, 10));
                //获取最近几个月的时间戳
                Long time = DateUtil.getMillisByMonth(Integer.parseInt("-" + month));

                //查询使用商米pos每个月的盈利信息
                Map<String, Object> ordermap = orderMapper
                        .queryOrderList(Long.parseLong(merchantId), createTime, nowTime);
                //先获取商家注册的时间
                // Long createTime3 = merchantMapper.getMerchantCreate(Long.parseLong(merchantId));
                //商家每个月的进货的金额信息
                Map<String, Object> merchantmap = merchantMapper
                        .getMerchantProfit(Long.parseLong(merchantId), createTime2, nowTime);
                //商家每个月的进货的次数
                Map<String, Object> merchantStockCount = merchantMapper
                        .getMerchantStockCount(Long.parseLong(merchantId), createTime2, nowTime);

                //商米pos盈利金额
                Double money = Double.parseDouble(ordermap.get("sum_money").toString());
                Long posCreateTime = Long.parseLong(ordermap.get("create_time").toString());
                //商家进货金额
                String mStockMoney = merchantmap.get("total_money").toString();
                Long mCreateTime = Long.parseLong(ordermap.get("create_time").toString());
                //商家进货次数
                String mStockCouont = merchantStockCount.get("stock_count").toString();


                BigDecimal orderMoney = new BigDecimal(Double.toString(money));
                //查询yuef月份
                BigDecimal monthb = new BigDecimal(Double.toString(months));
                //  Double AS=Double.parseDouble(b);
                if (time >= posCreateTime) {//判断使用商米pos的时间是否在预算的范围之内
                    //商米pos盈利平均金额
                    Double value = orderMoney.divide(monthb, 2, BigDecimal.ROUND_UP).doubleValue();
                    jo.put("posProfitMoney", value.toString());
                } else {
//                         //查询使用了几个月
                    Integer iv = DateUtil.getDateByYueDiff(posCreateTime);
                    BigDecimal ivb = new BigDecimal(Double.toString(iv));
                    Map m = new HashMap();
                    if (!"0".equals(iv) || "1".equals(iv)) {
                        //商米pos平均金额
                        Double db = orderMoney.divide(ivb, 2, BigDecimal.ROUND_UP).doubleValue();
                        jo.put("posProfitMoney", db.toString());
                    } else {
                        jo.put("posProfitMoney", orderMoney.toString());
                    }

                }

                BigDecimal a2 = new BigDecimal(mStockMoney);

                if (time >= mCreateTime) {//判断注册商家的时间是否在预算的范围之内

                    //商家进货平均金额
                    Double db = a2.divide(monthb, 2, BigDecimal.ROUND_UP).doubleValue();
                    jo.put("mStockMoney", db.toString());

                } else {
                    //查询商家使用了几个月
                    Integer num = DateUtil.getDateByYueDiff(mCreateTime);


                    if (!"1".equals(num) || !"0".equals(num)) {
                        BigDecimal ivb = new BigDecimal(Double.toString(num));
                        //商家进货平均金额
                        Double db = a2.divide(ivb, 2, BigDecimal.ROUND_UP).doubleValue();
                        jo.put("mStockMoney", db.toString());
                    } else {
                        jo.put("mStockMoney", mStockMoney.toString());
                    }
                }
                if (time >= mCreateTime) {//判断注册商家的时间是否在预算的范围之内
                    BigDecimal bd = new BigDecimal(mStockCouont);
                    //进货平均数
                    int count = (bd.divide(monthb, 0).intValue());
                    jo.put("mStockCount", "" + "" + count);
                } else {

                    //查询商家进货了几个月
                    Integer num = DateUtil.getDateByYueDiff(mCreateTime);
                    if (!"0".equals(num) && !"1".equals(num)) {
                        BigDecimal bd = new BigDecimal(mStockCouont);
                        BigDecimal bb = new BigDecimal(num);
                        //进货平均数
                        int count = (bd.divide(bb, 0).intValue());
                        jo.put("mStockCount", "" + "" + count);
                    } else {
                        jo.put("mStockCount", "" + "" + mStockCouont);
                    }

                }
                jo.put("user_id", userId);
                jo.put("user_name", name);
                jo.put("id_card", idCard);
                jo.put("phone", phone);
                flag = true;
                msg = "获取信息成功！";
                obj = jo;
            }
        }


        return new AjaxJson(flag, msg, obj, stateCode);
    }

    /**
     * 商家每天的营业额
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getMerchantBusinessByEvery")
    public String getMerchantBusinessByEvery(@RequestParam(name = "merchant_id") String merchantId) {


        Long posProfitByDayEntity = orderMapper.getPosStrtTimeByMerchant(Long.parseLong(merchantId));

        Map m = new HashMap();
        m.put("data", posProfitByDayEntity);

        return m.toString();
    }


}
