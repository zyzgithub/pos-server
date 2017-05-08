package com.dianba.pos.extended.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.extended.config.Charge19EApi;
import com.dianba.pos.extended.config.Charge19EUtil;
import com.dianba.pos.extended.vo.Charge_19E;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.service.MenuManager;
import com.dianba.pos.menu.vo.MenuDto;
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
@RequestMapping("/charge19e/")
public class Charge19EController {


    @Autowired
    private MenuManager menuManager;
    /**
     * 19e 话费充值平台
     * @return
     */
    @ResponseBody
    @RequestMapping(value="hfChargeBy19e")
    public AjaxJson hfChargeBy19e(HttpServletResponse response,String chargeNumber,String chargeMoney,String fillType,String chargeType) {
        AjaxJson aj=new AjaxJson();
        Charge_19E ch=new Charge_19E();
        ch.setChargeNumber(chargeNumber);
        ch.setChargeMoney(chargeMoney);
        ch.setFillType(fillType);
        ch.setChargeType(chargeType);
        //订单号
        ch.setMerchantOrderId("eqwewqewqeqwewqewqeqwewqeqweqweqweqweqweqwewqeqweqweqwe");
        ch.setSendNotifyUrl("32423423");
        ch.setIspId("0");
        ch.setProvinceId("");
        ch.setFillType("0");

        String result=   Charge19EApi.hfCharge(Charge19EUtil.HF_CHARGE_19E_URL,ch);
        JSONObject jo= JSONObject.parseObject(result);
        String resultCode=jo.getString("resultCode");
        String resultDesc=jo.getString("resultDesc");
        String ehfOrderId=jo.getString("ehfOrderId");
        String queryResultUrl=jo.getString("queryResultUrl");
        String payMoney=jo.getString("payMoney");
        String merchantOrderId=jo.getString("merchantOrderId");

        if(resultCode.equals("success")){


        }
        System.out.println(result);
        return new AjaxJson();
    }

    /**话费充值回调方法**/
    @RequestMapping("hfChargeBack")
    public void hfChargeBack(){


    }

    /**
     * 增值服务商品信息
     * @param type
     */
    @RequestMapping("chargeMenu")
    @ResponseBody
    public AjaxJson ChargeMenu(String type){

            Integer is_flash=Integer.parseInt(type);
            List<Menu> menulst=menuManager.findAllByIsFlashAndMerchantId(is_flash,-1);

            List<MenuDto> mdlst=new ArrayList<>();
            for (Menu menu : menulst){

                MenuDto md=new MenuDto();
                md.setMenu_id(menu.getId());
                md.setMenu_name(menu.getName());
                md.setPrice(menu.getPrice());
                md.setStock_price(menu.getOriginalPrice());
                md.setType(menu.getIsFlash());
                mdlst.add(md);
            }


            return new AjaxJson(true,"获取数据成功!",mdlst,"00");
    }
}
