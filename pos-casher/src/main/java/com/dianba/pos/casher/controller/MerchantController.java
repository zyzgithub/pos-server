package com.dianba.pos.casher.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.merchant.mapper.MerchantMapper;
import com.dianba.pos.merchant.po.Merchant;
import com.dianba.pos.merchant.service.MerchantManager;
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

    @Autowired
    private MerchantManager merchantManager;
    @ResponseBody
    @RequestMapping(value = "getMerchantProfitInfo")
    public AjaxJson getMerchantProfitInfo(String name, String phone
            , String merchantId, HttpServletRequest request) {
        boolean flag = true;
        String msg = "";
        Object obj = null;
        String stateCode = "00";

        JSONObject jo = new JSONObject();

        System.out.println("name" + name);
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(merchantId)) {
            flag = false;
            msg = "参数输入有误!";
            stateCode = "01";

        } else {

            Map<String, Object> map = merchantMapper.verifyMerchantUser(merchantId);

            //说明是不是pos商家用户


            if (map==null) {
                flag = false;
                msg = "该用户不是pos商家用户!";
                stateCode = "01";
            } else {
                //取得商家id
                 Merchant merchantTab= merchantManager.findById(Long.parseLong(merchantId));
                String userId = map.get("userId").toString();
                //获取要查询的月数
                String month = request.getParameter("month");
                Integer months = Integer.parseInt(month);
                //先获取商家开始使用pos的时间
                Long createTime = Long.parseLong(map.get("posCreateDate").toString());
                //获取商家注册时间
                Long createTime2 = merchantMapper.getMerchantCreate(Long.parseLong(merchantId));
                //获取当前时间戳
                Long nowTime = Long.parseLong(DateUtil.currentTimeMillis().toString().substring(0, 10));
                //获取最近几个月的时间戳
                Long time = DateUtil.getMillisByMonth(Integer.parseInt("-" + month));

                //查询使用商米pos每个月的盈利信息
                Map<String, Object> ordermap = orderMapper
                        .queryOrderList(Long.parseLong(merchantId), createTime, nowTime);
                BigDecimal monthb = new BigDecimal(Double.toString(months));

                if(ordermap!=null){
                    String moneypos=ordermap.get("sum_money").toString();
                    Double money = Double.parseDouble(moneypos);
                    BigDecimal orderMoney = new BigDecimal(Double.toString(money));
                    Long posCreateTime = Long.parseLong(ordermap.get("create_time").toString());

                    if (time >= posCreateTime) {//判断使用商米pos的时间是否在预算的范围之内
                        //商米pos盈利平均金额
                        Double value = orderMoney.divide(monthb, 2, BigDecimal.ROUND_UP).doubleValue();
                        jo.put("posProfitMoney", value.toString());
                    } else {
//                         //查询使用了几个月
                        int iv = DateUtil.getDateByYueDiff(posCreateTime);
                        BigDecimal ivb = new BigDecimal(Double.toString(iv));
                        Map m = new HashMap();
                        if (iv==0 || iv==1) {
                            jo.put("posProfitMoney", orderMoney.toString());

                        } else {
                            //商米pos平均金额
                            Double db = orderMoney.divide(ivb, 2, BigDecimal.ROUND_UP).doubleValue();
                            jo.put("posProfitMoney", db.toString());
                        }
                    }
                }else{
                    jo.put("posProfitMoney", "");

                }
                //先获取商家注册的时间
                Long mCreateTime =Long.parseLong(merchantTab.getCreateTime().toString());
                //商家每个月的进货的金额信息
                Map<String, Object> merchantmap = merchantMapper
                        .getMerchantProfit(Long.parseLong(merchantId), createTime2, nowTime);
                if(merchantmap!=null){

                    //商家每个月的进货的次数

                    //商家进货金额
                    String mStockMoney = merchantmap.get("total_money").toString();
                    BigDecimal a2 = new BigDecimal(mStockMoney);

                    if (time >= mCreateTime) {//判断注册商家的时间是否在预算的范围之内

                        //商家进货平均金额
                        Double db = a2.divide(monthb, 2, BigDecimal.ROUND_UP).doubleValue();
                        jo.put("mStockMoney", db.toString());

                    } else {
                        //查询商家使用了几个月
                        int num = DateUtil.getDateByYueDiff(mCreateTime);


                        if (num==0 || num==1) {
                            jo.put("mStockMoney", mStockMoney.toString());

                        } else {
                            BigDecimal ivb = new BigDecimal(Double.toString(num));
                            //商家进货平均金额
                            Double db = a2.divide(ivb, 2, BigDecimal.ROUND_UP).doubleValue();
                            jo.put("mStockMoney", db.toString());
                        }
                    }
                }else {
                    jo.put("mStockMoney", "");
                }
                Map<String, Object> merchantStockCount = merchantMapper
                        .getMerchantStockCount(Long.parseLong(merchantId), createTime2, nowTime);

                if(merchantStockCount!=null){
                    //商家进货次数
                    String mStockCouont = merchantStockCount.get("stock_count").toString();
                    if (time >= mCreateTime) {//判断注册商家的时间是否在预算的范围之内
                        BigDecimal bd = new BigDecimal(mStockCouont);
                        //进货平均数
                        int count = (bd.divide(monthb, 0).intValue());
                        jo.put("mStockCount", "" + "" + count);
                    } else {

                        //查询商家进货了几个月
                        int num = DateUtil.getDateByYueDiff(mCreateTime);
                        if (num==0|| num==1) {
                            jo.put("mStockCount", "" + "" + mStockCouont);


                        } else {
                            BigDecimal bd = new BigDecimal(mStockCouont);
                            BigDecimal bb = new BigDecimal(num);
                            //进货平均数
                            int count = (bd.divide(bb, 0).intValue());
                            jo.put("mStockCount", "" + "" + count);
                        }
                }

                }else {
                    jo.put("mStockCount", "");

                }
                jo.put("user_id", userId);

                if(StringUtil.isEmpty(map.get("userName").toString())){

                    jo.put("user_name","未知姓名");
                }else{
                    jo.put("user_name",map.get("userName"));
                }
                jo.put("phone", phone);
                jo.put("merchantId",merchantId);
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
