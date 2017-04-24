package com.wm.controller.pay;

import com.alibaba.fastjson.JSONArray;
import com.wm.dto.post.BuyoutAlertLogs;
import com.wm.dto.post.BuyoutDetailContextDto;
import com.wm.dto.post.BuyoutDetailDto;
import com.wm.dto.post.BuyoutLogDto;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.pay.PostBuyoutEntityLog;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.MongoService;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.PostBuyoutLogService;
import com.wm.service.pay.PostBuyoutService;
import com.wm.service.user.WUserServiceI;
import com.wm.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mjorcen on 16/8/8.
 */

@Controller
@RequestMapping("ci/postBuyoutController")
public class PostBuyoutController extends BaseController {
    @Autowired
    private PostBuyoutService postBuyoutService;
    @Autowired
    private PostBuyoutLogService buyoutLogService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private MongoService mongoService;
    @Autowired
    private WUserServiceI wUserService;


    /**
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(params = "posFreezing")
    @ResponseBody
    public AjaxJson posFreezing(HttpServletRequest request, @RequestParam Long userId) {
        if (userId == null) {
            return AjaxJson.failJson("用户 ID 不能为空");
        }

        WUserEntity userEntity = wUserService.getEntity(WUserEntity.class, userId.intValue());
        BigDecimal userMoney = BigDecimal.valueOf(userEntity.getMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (userEntity == null) {
            return AjaxJson.failJson("用户不存在");
        }

        List<PostBuyoutEntityLog> logs = this.buyoutLogService.getUnpayPostBuyoutEntityLogs(userId);

        if (CollectionUtils.isEmpty(logs)) {
            /*
             select DATE_FORMAT(FROM_UNIXTIME(need_pay_time),'%Y-%m'),DATE_FORMAT( now(),'%Y-%m') from post_buyout_log ;

             */
            String hql = "from " + PostBuyoutEntityLog.class.getName() + " where  userId = ? and need_pay_time >= ?  and payState = 1  ";
            Date date = DateUtils.addDays(new Date(), -30);
            List<PostBuyoutEntityLog> paylogs = this.buyoutLogService.findHql(hql, userId, date.getTime() / 1000);
            AjaxJson aj = AjaxJson.successJson("没有冻结的金额");
            Map<String, String> result = new HashMap<>();
            result.put("freeMoney", "unFree");
            result.put("warnLine", "");
            result.put("withdrawAble", "true");
            result.put("userMoeny", userMoney.toString());
            result.put("freezingMoney", "0");
            result.put("needFreezingMoney", "0");

            if (!CollectionUtils.isEmpty(paylogs)) {
                PostBuyoutEntityLog log = paylogs.get(0);
                //分期还款：331.67元，已还清
                Integer payType = Integer.valueOf(log.getPayIndex().split("/")[1]);
                String subHeader = payType == 1 ? "设备购买还款" : "分期还款";
                String warnLine = "%s：<font color='#ff8908'>%s</font>元，<font color='Lime'>已还清</font>";
                warnLine = String.format(warnLine, subHeader, log.getPaymoney().setScale(2, BigDecimal.ROUND_HALF_UP));
                result.put("warnLine", warnLine);
            } else {
                MerchantEntity merchantEntity = this.orderService.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId.intValue());
                MerchantInfoEntity merchantInfoEntity = this.orderService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantEntity.getId().intValue());
                if (merchantInfoEntity.getPlatformType() == 2) {
                    String warnLine = "最低提现<font color='#ff8908'>%s</font>元";
                    warnLine = String.format(warnLine, 100);
                    result.put("warnLine", warnLine);
                }
            }


            aj.setObj(result);
            return aj;
        }

        BigDecimal decimal = BigDecimal.valueOf(0);
        Integer payType = 1;
        for (PostBuyoutEntityLog log : logs) {
            decimal = decimal.add(log.getPaymoney());
            payType = Integer.valueOf(log.getPayIndex().split("/")[1]);
        }
        decimal = decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        String freezingMoney = decimal.toString();
        if (userEntity.getMoney() < decimal.doubleValue()) {
            freezingMoney = userMoney.toString();
        }

        Map<String, String> result = new HashMap<>();
        result.put("freeMoney", "free");
        result.put("userMoeny", userMoney.toString());
        result.put("freezingMoney", freezingMoney);
        result.put("needFreezingMoney", decimal.toString());

        if (decimal.doubleValue() <= userEntity.getMoney()) {
            result.put("withdrawAble", "true");
        } else {
            result.put("withdrawAble", "false");
        }
        // 设备购买还款：2980.00元，未还清冻结余额：215.00元
        //  "<font color='#ff8908'>¥" + subMoney + "</font>
        String subHeader = payType == 1 ? "设备购买还款" : "分期还款";
        String warnLine = "%s：<font color='#ff8908'>%s</font>元，未还清<br/>冻结余额：<font color='#ff8908'>%s</font>元";

        warnLine = String.format(warnLine, subHeader, decimal, freezingMoney);
        result.put("warnLine", warnLine);
        AjaxJson aj = AjaxJson.successJson("success");
        aj.setObj(result);
        return aj;
    }


    /**
     * 获取购买记录
     *
     * @return
     */
    @RequestMapping(params = "getBuyoutLogs")
    @ResponseBody
    public AjaxJson getBuyoutLogs(Long userId, Integer state) throws Exception {
        AjaxJson j = AjaxJson.successJson("success");
        List<BuyoutLogDto> vos = this.buyoutLogService.getBuyoutLogDtos(userId, state);

        j.setObj(vos);
        return j;
    }


    /**
     * POS 购买
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(params = "buyout")
    @ResponseBody
    public AjaxJson buyout(HttpServletRequest request, @RequestParam Long userId, String items) {
        AjaxJson ajaxJson;
        try {
            List<BuyoutDetailDto> list = JSONArray.parseArray(items, BuyoutDetailDto.class);
            List<Map<String, String>> result = postBuyoutService.buyout(userId, list);
            ajaxJson = AjaxJson.successJson("请求成功");
            Map<String, Object> map = new HashMap<>();
            map.put("items", result);
            map.put("state", "success");
            map.put("subtitle", "如需批量购买设备，请联系市场客服。");
            map.put("phone", "400-894-5917");
            map.put("title", " 购买成功");
            map.put("msg", "购买金额将在15个工作日内从账户余额扣除，扣款期间将冻结账户，为不影响您的提现功能，请在扣款期间留有足够扣款金额。");
            ajaxJson.setObj(map);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = AjaxJson.failJson("系统繁忙");
            Map<String, Object> map = new HashMap<>();
            map.put("state", "failed");
            map.put("msg", "");
            map.put("title", " 购买失败");
            ajaxJson.setObj(map);

        }
        return ajaxJson;
    }

    /**
     * @param userId
     * @return
     */
    @RequestMapping(params = "buyouted")
    @ResponseBody
    public AjaxJson buyouted(@RequestParam Long userId) {
        AjaxJson ajaxJson;
        List<Map<String, String>> result = postBuyoutService.buyouted(userId);
        ajaxJson = AjaxJson.successJson("请求成功");
        Map<String, Object> map = new HashMap<>();
        map.put("items", result);
        map.put("state", "success");
        map.put("subtitle", "如需批量购买设备，请联系市场客服。");
        map.put("phone", "400-894-5917");
        map.put("title", " 您已购买成功");
        map.put("msg", "购买金额将在15个工作日内从账户余额扣除，扣款期间将冻结账户，为不影响您的提现功能，请在扣款期间留有足够扣款金额。");
        ajaxJson.setObj(map);
        return ajaxJson;
    }

    /**
     * 卖断资料
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(params = "buyoutDetail")
    @ResponseBody
    public AjaxJson buyoutDetail(HttpServletRequest request, @RequestParam Integer userId, Integer itemId) {
        AjaxJson ajaxJson;

        try {
            BuyoutDetailContextDto detailContextDto = new BuyoutDetailContextDto();

            Map<Integer, BuyoutDetailDto> map;
            if (itemId == 1) {
                map = postBuyoutService.gethardwareData();
            } else {
                map = postBuyoutService.getSoftwareData();
            }

            map.remove(0);

            Collection<BuyoutDetailDto> list = map.values();
            detailContextDto.setItems(new LinkedList<>(list));
            detailContextDto.setTitle("3980");
            detailContextDto.setImage("http://qidianimg.oss-cn-shenzhen.aliyuncs.com/merchants/pos/POSBuyoutDetailTopic.png");
            ajaxJson = AjaxJson.successJson("请求成功");
            ajaxJson.setObj(detailContextDto);

        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = AjaxJson.failJson("系统繁忙");

        }

        return ajaxJson;
    }

    /**
     * 卖断资料
     *
     * @param request
     * @param merchantId
     * @return
     */
    @RequestMapping(params = "alertMsg")
    @ResponseBody
    public AjaxJson alertMsg(HttpServletRequest request, @RequestParam Long merchantId) {
        AjaxJson ajaxJson = AjaxJson.successJson("请求成功");

        try {
            MerchantInfoEntity merchantEntity = this.buyoutLogService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId.intValue());
            if (merchantEntity == null || merchantEntity.getShopFromType() == 2) {
                //   `shop_from_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '商店加盟类型，1加盟店  2直营店',
                ajaxJson.setObj("failed");
                return ajaxJson;
            }
            BuyoutAlertLogs buyoutAlertLogs = getBuyoutAlertLogs(merchantId);
            // 一天一次, 弹够7次. (圈圈 日:)

//            Long l = orderService.getCountForJdbcParam("select count(*) from `order` where merchant_id = ? and order_type in ('supermarket','supermarket_settlement') limit 1  ", Arrays.asList(merchantId).toArray());
//            Long l2 = orderService.getCountForJdbcParam("select count(*) from `post_buyout` where merchant_id = ?  limit 1  ", Arrays.asList(merchantId).toArray());
//
//            if (l > 0 && l2 == 0) {
//                ajaxJson.setObj("success");
//                saveOrUpdateLog(merchantId.longValue());
//                return ajaxJson;
//            } else {
//                ajaxJson.setObj("failed");
//
//            }

            if (buyoutAlertLogs == null || (!isToday(buyoutAlertLogs) && buyoutAlertLogs.getCount() < 7)) {//&& System.currentTimeMillis() - buyoutAlertLogs.getLastAccessTime() > 60 * 60 * 1000)
                Long l = orderService.getCountForJdbcParam("select count(*) from `order` where merchant_id = ? and order_type in ('supermarket','supermarket_settlement') limit 1  ", Arrays.asList(merchantId).toArray());
                Long l2 = orderService.getCountForJdbcParam("select count(*) from `post_buyout` where merchant_id = ?  limit 1  ", Arrays.asList(merchantId).toArray());

                if (l > 0 && l2 == 0) {
                    ajaxJson.setObj("success");
                    ajaxJson.setMsg("为提供更好的服务与系统质量,POS机使用将正式收费。提前购买立减1000,如需购买前往购买界面。");
                    saveOrUpdateLog(merchantId.longValue());
                    return ajaxJson;
                } else {
                    ajaxJson.setObj("failed");
                    saveOrUpdateLog(merchantId.longValue());
                }
            } else {
                ajaxJson.setObj("failed");

            }


        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson.setObj("failed");
            ajaxJson = AjaxJson.failJson("系统繁忙");

        }

        return ajaxJson;
    }

    private boolean isToday(BuyoutAlertLogs buyoutAlertLogs) {
        Long aLong = buyoutAlertLogs.getLastPushTime();
        return DateUtil.isToday(aLong);

    }

    private void saveOrUpdateLog(Long merchantId) {
        BuyoutAlertLogs buyoutAlertLogs = getBuyoutAlertLogs(merchantId);

        if (buyoutAlertLogs == null) {
            buyoutAlertLogs = new BuyoutAlertLogs();
            buyoutAlertLogs.setCount(1L);
            buyoutAlertLogs.setCreateTime(System.currentTimeMillis());
            buyoutAlertLogs.setLastPushTime(buyoutAlertLogs.getCreateTime());
            buyoutAlertLogs.setLastAccessTime(buyoutAlertLogs.getCreateTime());
            buyoutAlertLogs.setMerchantId(merchantId);
            this.mongoService.getMongoTemplate().save(buyoutAlertLogs, "POS_BUYOUT_ALERT_LOGS");
        } else {
            buyoutAlertLogs.setCount(buyoutAlertLogs.getCount() + 1L);
            buyoutAlertLogs.setLastPushTime(System.currentTimeMillis());
            buyoutAlertLogs.setLastAccessTime(System.currentTimeMillis());
            this.mongoService.getMongoTemplate().save(buyoutAlertLogs, "POS_BUYOUT_ALERT_LOGS");
        }


    }

    private void saveOrUpdateNoPushLog(Long merchantId) {
        BuyoutAlertLogs buyoutAlertLogs = getBuyoutAlertLogs(merchantId);

        if (buyoutAlertLogs == null) {
            buyoutAlertLogs = new BuyoutAlertLogs();
            buyoutAlertLogs.setCount(0L);
            buyoutAlertLogs.setLastPushTime(0L);
            buyoutAlertLogs.setCreateTime(System.currentTimeMillis());
            buyoutAlertLogs.setLastAccessTime(buyoutAlertLogs.getCreateTime());
            buyoutAlertLogs.setMerchantId(merchantId);
            this.mongoService.getMongoTemplate().save(buyoutAlertLogs, "POS_BUYOUT_ALERT_LOGS");
        } else {
            buyoutAlertLogs.setLastAccessTime(System.currentTimeMillis());
            this.mongoService.getMongoTemplate().save(buyoutAlertLogs, "POS_BUYOUT_ALERT_LOGS");
        }


    }


    private BuyoutAlertLogs getBuyoutAlertLogs(Long merchantId) {
        Query query = Query.query(Criteria.where("merchantId").is(merchantId));
        return this.mongoService.getMongoTemplate().findOne(query, BuyoutAlertLogs.class, "POS_BUYOUT_ALERT_LOGS");
    }

}
