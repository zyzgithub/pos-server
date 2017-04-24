package com.wm.service.impl.other;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.order.OrderEntity;
import com.wm.entity.other.OtherEntity;
import com.wm.entity.pay.PayEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.other.OtherServiceI;

@Service("otherService")
@Transactional
public class OtherServiceImpl extends CommonServiceImpl implements OtherServiceI {
	@Autowired
	private FlowServiceI flowService;

	@Autowired
	OrderStateServiceI orderStateService;
	

	@Autowired
	OrderServiceI orderService;

	@Override
	public void otherAlipayDone(Integer orderId, String title,Integer otherId,
			double alipayBalance, String payType) throws Exception {	
		long time = System.currentTimeMillis();
		
		OrderEntity order=this.get(OrderEntity.class, orderId);
		order.setPayType(payType);
		order.setTitle(title);
		order.setPayState("pay");
		order.setPayTime((int)(time/1000));
		this.saveOrUpdate(order);
		
		OtherEntity other = this.get(OtherEntity.class, otherId);
		
		orderStateService.payOrderState(order.getId());

		String  bank="";
		if("unionpay".equals(payType)){
			bank="银联";
		}else if("alipay".equals(payType)){
			bank="支付宝";
		}else if("tenpay".equals(payType)){
			bank="财付通";
		}else if("weixinpay".equals(payType)){
			bank="微信支付";
		}
		
		PayEntity pay = this.get(PayEntity.class, order.getPayId());
		if(pay==null){
			pay=new PayEntity();
			pay.setCreateTime((int)(time/1000));
			pay.setMoney(other.getOtherMoney());
			pay.setOrderId(order.getId());
			pay.setId(order.getPayId());
			pay.setService("unpay");
			
			pay.setState("pay");
			pay.setBank(bank);
			pay.setService(payType);
			pay.setVid(other.getVid());
			this.save(pay);
		}else{
			pay.setState("pay");
			pay.setBank(bank);
			pay.setService(payType);
			pay.setVid(other.getVid());
		}
		
		pay.setState("pay");
		this.saveOrUpdate(pay);
		
//		orderService.setOrderNum(orderTemp);	//设置排号
//		orderService.autoPrint(orderTemp);//打印订单
//		orderService.buyCount(orderTemp);//销量统计
	}


	
	
	
	
}