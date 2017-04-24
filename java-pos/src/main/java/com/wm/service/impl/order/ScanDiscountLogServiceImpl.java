package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.mongo.QrOrderLog;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.MongoService;
import com.wm.service.order.ScanDiscountLogServiceI;
@Service("scanDiscountLogService")
@Transactional
public class ScanDiscountLogServiceImpl extends CommonServiceImpl implements ScanDiscountLogServiceI{
    
    @Autowired MongoService mongoService;
	
	private static Logger logger = LoggerFactory.getLogger(ScanDiscountLogServiceImpl.class);
	
	@Override
	public void processSanDiscount(OrderEntity order) {
	    saveQrOrderLog(order);//保存扫码支付记录
        //已打折订单操作
        if(!order.getOrigin().equals(order.getOnlineMoney())){
            createLog(order);//记录扫码订单折扣记录
            updateScanPaySetEveryDayCount(order);//已打折订单 需要更新次数
        }
	    
	}
	
	/**
	 * 保存扫码支付记录
	 * @param order
	 */
	private void saveQrOrderLog(OrderEntity order){
	    //记录订单优惠
        QrOrderLog orderLog = new QrOrderLog();
        orderLog.order_id = (long) order.getId();
        orderLog = mongoService.findOne(orderLog);
        logger.info("【QrOrderLog】 orderId:{} orderLog:{}", order.getId(), orderLog);
        if(null != orderLog){
            try {
                QrOrderLog updateLog = new QrOrderLog();
                updateLog.status = 1;
                mongoService.updateFirstById(orderLog.id, updateLog);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	/**
	 * 记录扫码订单折扣记录
	 * @param order
	 */
    private void createLog(OrderEntity order) {
        String insertSQL = "insert scan_discount_log(order_id,origin_money,online_money,discount_money,create_time,merchant_id,user_id) "
                        + "values(?,?,?,?,now(),?,?)";
        this.executeSql(insertSQL, 
                        order.getId(), 
                        (int) Math.rint(order.getOrigin() * 100),
                        (int) Math.rint(order.getOnlineMoney() * 100),
                        ((int) Math.rint(order.getOrigin() * 100) - (int) Math.rint(order.getOnlineMoney() * 100)),
                        order.getMerchant().getId(), 
                        order.getWuser().getId());
    }
    
    /**
     * 更新tpm_scanpay_set 的 everyday_count 数量 -1
     * @param order
     */
    private void updateScanPaySetEveryDayCount(OrderEntity order){
        try{
            String sql ="UPDATE tpm_scanpay_set SET everyday_count = everyday_count - 1 WHERE id = ( SELECT a.id FROM ( SELECT tssm.scanpay_id AS id FROM tpm_scanpay_set_merchant AS tssm LEFT JOIN tpm_scanpay_set AS tss ON tssm.scanpay_id = tss.id WHERE merchant_id = ? AND UNIX_TIMESTAMP(NOW()) BETWEEN UNIX_TIMESTAMP(tss.begin_time) AND UNIX_TIMESTAMP(tss.end_time) AND tss.is_delete = '0' ) a )";
            this.executeSql(sql, order.getMerchant().getId());
        }catch(Exception e){
            logger.error("商家扫码设置有问题--错误表 tpm_scanpay_set   商家ID:" + order.getMerchant().getId());
        }
    }
}
