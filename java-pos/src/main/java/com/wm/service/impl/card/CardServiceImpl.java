package com.wm.service.impl.card;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.service.card.CardServiceI;

@Service("cardService")
@Transactional
public class CardServiceImpl extends CommonServiceImpl implements CardServiceI {

	@Override
	public void registerCard(int userId) {
		SystemconfigEntity cardNum=this.findUniqueByProperty(SystemconfigEntity.class, "code", "card_num");//注册送代金券的张数
		SystemconfigEntity cardEndtime=this.findUniqueByProperty(SystemconfigEntity.class, "code", "card_endtime");//注册送代金券有效天数
		SystemconfigEntity cardCredit=this.findUniqueByProperty(SystemconfigEntity.class, "code", "card_credit");//注册送代金券金额
		
		int num=Integer.parseInt(cardNum.getValue());//注册送代金券的张数
		int Endtime=Integer.parseInt(cardEndtime.getValue())*86400+DateUtils.getSeconds();//注册送代金券有效时间
		int credit=Integer.parseInt(cardCredit.getValue());//注册送代金券金额
		
		for (int i = 0; i < num; i++) {
			
			String sql="INSERT INTO card(code,merchant_id,order_id,credit,consume,end_time,title,user_id) VALUES('注册红包',0,0,?,'N',?,?,?)";
			String title="注册送红包，全场商品和全场商家通用，每次只能使用一张";
			this.executeSql(sql, credit,Endtime,title,userId);
//			CardEntity card=new CardEntity();
//			card.setCode("注册红包");
//			card.setConsume("N");
//			card.setCredit(credit);
//			card.setEndTime((DateUtils.getSeconds()+Endtime*86400));
//			card.setMerchant(null);
//			card.setOrderId(0);
//			card.setTitle("注册送红包，全场商品和全场商家通用，每次只能使用一张");
//			card.setUserId(userId);
//			this.save(card);
			
		}
		
		
		
	}
	
}