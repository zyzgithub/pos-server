package com.dianba.pos.casher.controller;

import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.menu.mapper.MenuMapper;
import com.dianba.pos.menu.mapper.MerchantMapper;
import com.dianba.pos.menu.mapper.OrderMapper;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.po.Merchant;
import com.dianba.pos.menu.po.Order;

import com.dianba.pos.menu.service.MerchantServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/25 0025.
 * zyz
 * 商家控制器
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 获取商家盈利信息
     * 使用商米POS超过6个月，最近6个月内商家每月盈利额=（月营业额-月成本）的平均数
     * 使用商米POS未超过6个月,已开店月数商家每月盈利额=（月营业额-月成本）的平均数
     * 注册商家端超过6个月，最近6个月内商家每月进货额的平均数
     * 注册商家端未超过6个月,已开店月数商家每月进货额的平均数
     * 注册商家端超过6个月，最近6个月内商家每月进货次数的平均数
     * 注册商家端未超过6个月,已开店月数商家每月进货次数的平均数
     * 使用商米POS超过6个月，最近6个月内商家每日余额的平均数
     * 使用商米POS未超过6个月,已开店月数商家每日余额的平均数
     */
    @ResponseBody
    @RequestMapping(value = "getPosMerchantProfit")
    public String getPosMerchantProfit(String merchant_id,String month) {

        Map<String,Object> map=new HashMap<>();
        if(StringUtil.isEmpty(merchant_id)||StringUtil.isEmpty(month)){

            map.put("code","406");
            map.put("msg","参数输入有误!");
            map.put("date",new Object());

        }else {
            Integer months=Integer.parseInt(month);
            //先获取商家开始使用pos的时间
            Long createTime=orderMapper.getPosStrtTimeByMerchant(Long.parseLong(merchant_id));

            //获取当前时间戳
            Long newTime=Long.parseLong(DateUtil.currentTimeMillis().toString().substring(0,10));
            //获取最近几个月的时间戳
            Integer month_=Integer.parseInt("-"+month);
            Long time= DateUtil.getMillisByMonth(month_);
            Map<String,Object> ordermap=orderMapper.queryOrderList(Long.parseLong(merchant_id),createTime,newTime);
            String user_id=ordermap.get("user_id").toString();
            String user_phone=ordermap.get("user_mobile").toString();
            String user_name=ordermap.get("name").toString();
            String card=ordermap.get("card").toString();
            Double money=Double.parseDouble(ordermap.get("sum_money").toString());
            BigDecimal a = new BigDecimal(Double.toString(money));
            BigDecimal b = new BigDecimal(Double.toString(months));


          //  Double AS=Double.parseDouble(b);
            if(time>=createTime){//判断使用商米pos的时间是否在预算的范围之内

                Double value=a.divide(b,2, BigDecimal.ROUND_UP).doubleValue();
              //  Double d=Math.round(money/months)/100.0;
                Map m=new HashMap();
                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);
                m.put("money",value);
                m.put("id_card",card);
                m.put("merchant_id",merchant_id);

              map.put("code","200");
              map.put("msg","获取信息成功!");
              map.put("data",m);

            }else{
//                //查询使用了几个月
              Integer iv=   DateUtil.getDateByYueDiff(createTime);
              BigDecimal ivb = new BigDecimal(Double.toString(iv));
                Map m=new HashMap();
               if(!"0".equals(iv)&&"1".equals(iv)){
                    Double db= a.divide(ivb,2, BigDecimal.ROUND_UP).doubleValue();
                    m.put("money",db);
                }else {
                   m.put("money",money);
               }


                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);
                m.put("id_card",card);
                m.put("merchant_id",merchant_id);
                map.put("code","200");
                map.put("msg","获取信息成功!");
                map.put("data",m);


            }


        }
       return map.toString();
    }
    @ResponseBody
    @RequestMapping(value = "getMerchantProfit")
    public  String getMerchantProfit(String merchant_id,String month){
        Map<String,Object> map=new HashMap<>();
        if(StringUtil.isEmpty(merchant_id)||StringUtil.isEmpty(month)){

            map.put("code","406");
            map.put("msg","参数输入有误!");
            map.put("date",new Object());

        }else {
            Integer months=Integer.parseInt(month);
            //先获取商家开始使用pos的时间
            Long createTime=merchantMapper.getMerchantCreate(Long.parseLong(merchant_id));

            //获取当前时间戳
            Long newTime=Long.parseLong(DateUtil.currentTimeMillis().toString().substring(0,10));
            //获取最近几个月的时间戳
            Integer month_=Integer.parseInt("-"+month);
            Long time= DateUtil.getMillisByMonth(month_);
            Map<String,Object> merchantmap=merchantMapper.getMerchantProfit(Long.parseLong(merchant_id),createTime,newTime);
            String user_id=merchantmap.get("user_id").toString();
            String user_phone=merchantmap.get("user_mobile").toString();
            String user_name=merchantmap.get("user_name").toString();
            String card=merchantmap.get("id_card").toString();
            String money=merchantmap.get("total_money").toString();
            BigDecimal a = new BigDecimal(money);
            BigDecimal b = new BigDecimal(Double.toString(months));
            if(time>=createTime){//判断注册商家的时间是否在预算的范围之内

                Double db= a.divide(b,2, BigDecimal.ROUND_UP).doubleValue();
                Map m=new HashMap();
                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);
                m.put("money",db);
                m.put("id_card",card);
                m.put("merchant_id",merchant_id);

                map.put("code","200");
                map.put("msg","获取信息成功!");
                map.put("data",m);

            }else{
                //查询使用了几个月
                Integer num=   DateUtil.getDateByYueDiff(createTime);
                Map m=new HashMap();
                Double sumMoney=null;
                if("1".equals(num)&&"0".equals(num))
                {
                    BigDecimal ivb = new BigDecimal(Double.toString(num));
                    Double db= a.divide(ivb,2, BigDecimal.ROUND_UP).doubleValue();
                    m.put("total_money",db);
                }else{
                    m.put("total_money",money);
                }

                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);

                m.put("id_card",card);
                m.put("merchant_id",merchant_id);
                map.put("code","200");
                map.put("msg","获取信息成功!");
                map.put("data",m);


            }


        }
        return map.toString();
    }
    @ResponseBody
    @RequestMapping(value = "getMerchantStockCount")
    public  String getMerchantStockCount(String merchant_id,String month){
        Map<String,Object> map=new HashMap<>();
        if(StringUtil.isEmpty(merchant_id)||StringUtil.isEmpty(month)){

            map.put("code","406");
            map.put("msg","参数输入有误!");
            map.put("date",new Object());

        }else {
            Integer months=Integer.parseInt(month);
            //先获取商家注册的时间
            Long createTime=merchantMapper.getMerchantCreate(Long.parseLong(merchant_id));

            //获取当前时间戳
            Long newTime=Long.parseLong(DateUtil.currentTimeMillis().toString().substring(0,10));
            //获取最近几个月的时间戳
            Integer month_=Integer.parseInt("-"+month);
            Long time= DateUtil.getMillisByMonth(month_);
            Map<String,Object> merchantmap=merchantMapper.getMerchantStockCount(Long.parseLong(merchant_id),createTime,newTime);
            String user_id=merchantmap.get("user_id").toString();
            String user_phone=merchantmap.get("user_mobile").toString();
            String user_name=merchantmap.get("user_name").toString();
            String card=merchantmap.get("id_card").toString();
            Integer stock_count=Integer.parseInt(merchantmap.get("stock_count").toString());
            if(time>=createTime){//判断注册商家的时间是否在预算的范围之内
                BigDecimal bd=new BigDecimal(stock_count);
                BigDecimal bb=new BigDecimal(months);
                int count=(bd.divide(bb,0).intValue());

                Map m=new HashMap();
                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);
                m.put("stock_count",count);
                m.put("id_card",card);
                m.put("merchant_id",merchant_id);

                map.put("code","200");
                map.put("msg","获取信息成功!");
                map.put("data",m);

            }else{
                //查询使用了几个月
                Integer a=   DateUtil.getDateByYueDiff(createTime);
                Map m=new HashMap();
                if(!"0".equals(a)&&!"1".equals(a)){
                    BigDecimal bd=new BigDecimal(stock_count);
                    BigDecimal bb=new BigDecimal(a);

                    int count=(bd.divide(bb,0).intValue());
                    m.put("stock_count",count);
                }else {
                    //商家进货次数
                    m.put("stock_count",stock_count);
                }


                m.put("user_id",user_id);
                m.put("user_name",user_name);
                m.put("user_phone",user_phone);
                m.put("stock_count",stock_count);
                m.put("id_card",card);
                m.put("merchant_id",merchant_id);
                map.put("code","200");
                map.put("msg","获取信息成功!");
                map.put("data",m);


            }


        }
        return map.toString();
    }

    /**
     * 商家每天的营业额
     * @return
     */
    public String getMerchantBusinessByEvery(String merchant_id){

        return  "";
    }
}
