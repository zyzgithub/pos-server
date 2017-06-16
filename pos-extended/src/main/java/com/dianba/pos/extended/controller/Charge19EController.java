package com.dianba.pos.extended.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.DateUtil;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.extended.config.ExtendedUrlConstant;
import com.dianba.pos.extended.mapper.Charge19eMapper;
import com.dianba.pos.extended.service.Charge19eManager;
import com.dianba.pos.extended.util.FlowOrderStatus;
import com.dianba.pos.extended.vo.ChargeCallBack;
import com.dianba.pos.extended.vo.FlowChargeCallBack;
import com.dianba.pos.item.mapper.MenuMapper;
import com.dianba.pos.item.service.MenuManager;
import com.dianba.pos.item.service.PosItemManager;
import com.dianba.pos.order.mapper.LifeOrderMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

@Api("Pos增值服务模块")
@Controller
@RequestMapping(ExtendedUrlConstant.CHARGE_19E_INFO)
public class Charge19EController {

    private static Logger logger = LogManager.getLogger(Charge19EController.class);
    @Autowired
    private MenuManager menuManager;


    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private LifeOrderMapper orderMapper;


    @Autowired
    private Charge19eMapper charge19eMapper;

    @Autowired
    private Charge19eManager charge19eManager;

    @Autowired
    private PosItemManager posItemManager;

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
                if (ob != null & !ob.equals("success")) {
                    //修改订单信息为success
                    String date = DateUtil.getCurrDate("yyyyMMddHHmmss");
                    //发货完成
                    orderMapper.editOrderInfoBy19e(8, merchantOrderId);
                    //改变第三方订单状态
                    charge19eMapper.editCharge19e("success", date, merchantOrderId);
                    logger.info("话费订单充值成功!" + ",订单号为：" + chargeCallBack.getMerchantOrderId() + ",充值金额为：");
                }

            } else {
                logger.info("话费充值回调返回：ERROR=====================");
                orderMapper.editOrderInfoBy19e(3, merchantOrderId);
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
                if (ob != null & !ob.equals("success")) {
                    //改变原订单状态
                    orderMapper.editOrderInfoBy19e(8, merOrderNo);
                    //改变第三方订单状态
                    charge19eMapper.editCharge19e("success", date, merOrderNo);
                }
                result = "resultCode=SUCCESS";
                logger.info("流量充值回调操作成功!SUCCESS，");
            } else {
                orderMapper.editOrderInfoBy19e(3, merOrderNo);
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

    @ApiOperation("增值服务商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1话费充值 2流量充值", paramType = "query", required = true)
            , @ApiImplicitParam(name = "phone", value = "要操作的手机号码", paramType = "query", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "chargeMenu", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult chargeMenu(String type, String phone) {

        return charge19eManager.chargeMenu(type, phone);
    }
}
