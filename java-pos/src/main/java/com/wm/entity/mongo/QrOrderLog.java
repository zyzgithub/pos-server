package com.wm.entity.mongo;

import java.util.Date;

import javax.persistence.Id;

import org.jeecgframework.core.util.DateUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wm.entity.order.OrderEntity;

@Document(collection = "qr_order_log")
public class QrOrderLog {
    @Id public String id;
    
    @Indexed public Long order_id;
    
    @Indexed public Integer uid;
    
    @Indexed public Integer merchantId;
    
    @Indexed public Integer sale_type;
    
    @Indexed public String pay_type;
    
    @Indexed public Double order_money;
    
    @Indexed public String created_time;
    
    /** -1=未支付 1=已支付 */
    @Indexed public Integer status;

    public QrOrderLog() {
        
    }
    
    public QrOrderLog(OrderEntity order, Integer merchantId) {
        this.order_id = (long) order.getId();
        this.uid = order.getWuser().getId();
        this.merchantId = merchantId;
        this.sale_type = order.getSaleType();
        this.pay_type = order.getPayType();
        this.order_money = order.getOrigin().doubleValue();
        this.created_time = DateUtils.formatDate(new Date());
    }
    
    @Override
    public String toString() {
        return "id:" + id;
    }
}
