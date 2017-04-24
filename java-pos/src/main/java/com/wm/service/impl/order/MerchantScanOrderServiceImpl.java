package com.wm.service.impl.order;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderLimitEntity;
import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.service.order.MerchantScanOrderServiceI;

@Service
@Transactional
public class MerchantScanOrderServiceImpl extends CommonServiceImpl implements MerchantScanOrderServiceI {

    Logger logger = Logger.getLogger(MerchantScanOrderServiceImpl.class);

    @Override
    public Integer createMerchantScanOrder(Integer merchantId, BigDecimal origin, String remark) {
        String payId = String.valueOf(System.currentTimeMillis() + Thread.currentThread().getId());
        return createMerchantScanOrder(merchantId,origin,remark,null,payId);
    }

    @Override
    public Integer createMerchantScanOrder(Integer merchantId, BigDecimal origin, String remark, String payType, String payId) {
        int result = 0;
        MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);
        payType = StringUtil.isEmpty(payType) ? "merchantpay" : payType;

        if (merchant != null) {
            String orderType = "scan_order";
            String fromType = "merchantQcCode";
            if (StringUtil.isEmpty(remark)) {
                remark = "";
            }
            int saleType = 2;
            StringBuilder insert = new StringBuilder();
            insert.append("insert into `order` ");
            insert.append("(pay_id, merchant_id, state,online_money,origin,create_time, order_type, sale_type, from_type, remark , pay_type) ");
            insert.append("values(?, ?, 'unpay', ?, ?, ?, ?, ?, ?, ? ,? )");
            origin = origin.setScale(2, BigDecimal.ROUND_HALF_UP);
            result = this.executeSql(insert.toString(),
                    new Object[]{payId, merchantId, origin.doubleValue(), origin.doubleValue(), DateUtils.getSeconds(), orderType, saleType, fromType, remark, payType});
            if (result != 0) {
                result = this.findOneForJdbc("select id from `order` where pay_id=?", Integer.class, payId);
            }
            return result;
        } else {
            logger.info("未找到商家merchantId:" + merchantId + "相关信息");
            return result;
        }
    }

    @Override
    public Map<String, Object> responseMap(Integer orderId) {
        StringBuilder query = new StringBuilder();
        query.append("select pay_id payId,FROM_UNIXTIME(complete_time, '%Y/%m/%d %H:%i:%s') completeTime ");
        query.append("from `order` ");
        query.append("where id=? ");
        Map<String, Object> map = this.findOneForJdbc(query.toString(), orderId);
        return map;
    }

    /**
     * 获取限额：
     *
     * @param type 1：微信     2：支付宝
     */
    @Override
    public BigDecimal getLimitMoney(Integer merchantId, int type) {
        OrderLimitEntity orderLimit = this.findUniqueByProperty(OrderLimitEntity.class, "merchantId", merchantId);
        BigDecimal maxMoney = null;
        if (type == 1) {
            if (orderLimit != null) {
                maxMoney = new BigDecimal(orderLimit.getWechatLimit()).divide(new BigDecimal(100));
            } else {
                SystemconfigEntity sysConfig = this.findUniqueByProperty(SystemconfigEntity.class, "code", "wechat_max");
                maxMoney = new BigDecimal(sysConfig.getValue());
            }
        } else if (type == 2) {
            if (orderLimit != null) {
                maxMoney = new BigDecimal(orderLimit.getAlipayLimit()).divide(new BigDecimal(100));
            } else {
                SystemconfigEntity sysConfig = this.findUniqueByProperty(SystemconfigEntity.class, "code", "alipay_max");
                maxMoney = new BigDecimal(sysConfig.getValue());
            }
        }
        return maxMoney;
    }

}
