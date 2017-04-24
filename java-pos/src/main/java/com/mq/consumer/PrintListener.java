package com.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.dianba.mq.api.CommTag;
import com.dianba.mq.api.model.BaseMessage;
import com.dianba.mq.util.ObjectAndByte;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;

public class PrintListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(PrintListener.class);

	@Autowired
	private OrderServiceI orderService;

	@Autowired
	private PrintServiceI printService;

	public Action consume(Message message, ConsumeContext context) {
		logger.info("Receive message ID: " + message.getMsgID());
		try {
			String msgTag = message.getTag();
			logger.info("Receive msgTag: " + msgTag);
			if(CommTag.MQ_MSG_TAG_PRINT.equals(msgTag)){
				logger.info("start handle print buzz ");
				BaseMessage body = (BaseMessage) ObjectAndByte.toObject(message.getBody());
				logger.info("message body:{}", body);
				OrderEntity order = orderService.get(OrderEntity.class, body.getBussId());
				printService.print(order,false);
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
