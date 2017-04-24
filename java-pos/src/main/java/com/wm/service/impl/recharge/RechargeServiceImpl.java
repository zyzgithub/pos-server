package com.wm.service.impl.recharge;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.PayEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.recharge.RechargeEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.recharge.RechargeServiceI;

@Service("rechargeService")
@Transactional
public class RechargeServiceImpl extends CommonServiceImpl implements RechargeServiceI {

	/**
	 * 保存充值记录并更新用余额
	 * 
	 * @param outTradeNo
	 * @param transactionId
	 * @param totalFee
	 * @param opUserId
	 */
	public boolean saveRechargeAndUserMoney(String outTradeNo,
			String transactionId, String totalFee, String opUserId) {
		List<RechargeEntity> recharges = this.findByProperty(
				RechargeEntity.class, "outTradeNo", outTradeNo);
		if (recharges != null && !recharges.isEmpty()) {
			RechargeEntity recharge = recharges.get(0);
			recharge.setTransactionId(transactionId);
			recharge.setTotalFee(Integer.parseInt(totalFee));
			recharge.setOpUserId(opUserId);
			recharge.setNotifyTime(DateUtils.getSeconds());
			recharge.setStatus("Y");
			this.save(recharge);
			WUserEntity user = this
					.get(WUserEntity.class, recharge.getUserId());
			if (user != null) {
				user.setMoney((Math.rint(user.getMoney() * 100) + Integer
						.parseInt(totalFee)) / 100);
				this.save(user);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 保存充值记录
	 * 
	 * @param outTradeNo
	 * @param transactionId
	 * @param totalFee
	 * @param opUserId
	 */
	public boolean saveRecharge(String outTradeNo,
			String transactionId, String totalFee, String opUserId) {
		List<RechargeEntity> recharges = this.findByProperty(
				RechargeEntity.class, "outTradeNo", outTradeNo);
		if (recharges != null && !recharges.isEmpty()) {
			RechargeEntity recharge = recharges.get(0);
			recharge.setTransactionId(transactionId);
			recharge.setTotalFee(Integer.parseInt(totalFee));
			recharge.setOpUserId(opUserId);
			recharge.setNotifyTime(DateUtils.getSeconds());
			recharge.setStatus("Y");
			this.save(recharge);
		}
		return false;
	}
	
	/**
	 * 保存预充值记录和订单流水
	 * 
	 * @param outTradeNo
	 * @param body
	 * @param totalFee
	 * @param userId
	 */
	public boolean saveRechargeAndOrder(String outTradeNo, String body, String totalFee, int userId,String payType){
		int origin = Integer.parseInt(totalFee);
		double  doubleOrigin = origin;
		doubleOrigin = doubleOrigin/100;
		long time = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date(time);
		String orderDatetime = formatter.format(nowDate);
		// 添加订单信息
		String sql = "INSERT INTO `order`(user_id,state,origin,create_time,order_type,title)"
				+ " values(?,'unpay',?,UNIX_TIMESTAMP(),'recharge',?)";
		this.executeSql(sql, userId, doubleOrigin,body);
		sql = "select LAST_INSERT_ID() lastInsertId";
		Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
		int orderid = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
		String payid = outTradeNo;
		sql = "update `order` set pay_id=? where id=?";
		this.executeSql(sql, payid, orderid);
		OrderEntity orderEntity =this.get(OrderEntity.class, orderid);
		if(orderEntity != null){
			this.executeSql("update `order` set online_money=?,pay_type=?,pay_time=UNIX_TIMESTAMP(?) where id=?",
					doubleOrigin, payType, orderDatetime, orderid);
			this.executeSql("insert into pay(id,order_id,bank,money,currency,service,create_time) values(?,?,?,?,'CNY',?,UNIX_TIMESTAMP())",
					payid, orderid, PayEnum.getCn(payType), doubleOrigin, payType);
		}
		RechargeEntity recharge = new RechargeEntity();
		recharge.setOutTradeNo(outTradeNo);
		recharge.setPayType(payType);
		recharge.setTotalFee(Integer.parseInt(totalFee));
		recharge.setUserId(userId);
		recharge.setCreateTime(DateUtils.getSeconds());
		recharge.setStatus("N");
		this.save(recharge);
		return false;
	}

	public void updateRechargeState(String outTradeNo) {
		String sql = "UPDATE `0085_recharge` SET status ='Y',notify_time=? WHERE out_trade_no=?";
		this.executeSql(sql, DateUtils.getSeconds(), outTradeNo);
		
	}
}