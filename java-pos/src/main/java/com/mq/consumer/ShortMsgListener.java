package com.mq.consumer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.dianba.mq.api.CommTag;
import com.dianba.mq.api.model.Msg;
import com.dianba.mq.util.ObjectAndByte;
import com.sms.entity.SmsEntity;
import com.sms.service.SmsServiceI;

public class ShortMsgListener implements MessageListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ShortMsgListener.class);
	
	@Autowired
	private SmsServiceI smsService;

	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive message ID: " + message.getMsgID());
		try {
			String msgTag = message.getTag();
			logger.info("Receive msgTag: " + msgTag);
			if(CommTag.MQ_MSG_TAG_SHORTMSG.equals(msgTag)){
				logger.info("start handle short msg buzz ");
				Msg body = (Msg) ObjectAndByte.toObject(message.getBody());
				logger.info("message body:{}", body);
				SmsEntity sms = new SmsEntity();
				sms.setPhone(body.getPhoneNo());
				sms.setContent(body.getMsgContent());
				sms.setSendtime(new Date());
				smsService.sendSms(sms);
			} else {
				logger.warn("unknow msg!!!, msgTag:{}", msgTag);
			}
			return Action.CommitMessage;
		} catch (Exception e) {
			// 消费失败
			logger.error("Receive failed!!! ", e);
			return Action.ReconsumeLater;
		}
	}

}
