package com.wm.service.impl.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.config.EnvConfig;
import com.base.constant.order.OrderType;
import com.base.schedule.ScheduledUtil;
import com.wm.controller.open_api.ThirdPlat;
import com.wm.controller.open_api.ValidUtil;
import com.wm.controller.open_api.tswj.PortConfig;
import com.wm.entity.order.FlashOrderReturnEntity;
import com.wm.entity.order.LogisticsEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.MapUtil;
import com.wp.ConfigUtil;
import com.wp.TemplateData;
import com.wp.TemplateMessageUtils;

@Service("wmessageService")
@Transactional
public class WmessageServiceImpl extends CommonServiceImpl implements WmessageServiceI {
	
	private static final Logger logger = LoggerFactory.getLogger(WmessageServiceImpl.class);
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private WUserServiceI wUserService;
	
	private static final String getOrderMenu = "select b.name,a.quantity,a.price,(a.price*a.quantity) total_price "
			+ " from order_menu a left join menu b on a.menu_id=b.id where a.order_id=?";
	
    public void sendMessage(OrderEntity order, String stateCode) {
    	logger.info("sendMessage orderId:{}, stateCode:{}", order.getId(), stateCode);
		SendTask task = new SendTask(order, stateCode);
		ScheduledUtil.runNodelayTask(task, ScheduledUtil.SENDWXMSG_POOL);
    }
    
    /**
     * i玩派下单支付成功 推送微信消息
     * 
     * @param stateCode
     * @param payId
     */
    public void sendThirdMessage(ThirdPlat thirdPlat, Integer stateCode, Integer orderid) {
        logger.info("sendMessage : " + thirdPlat.cn_name + "; orderid:" + orderid);
        try {
            String sql = "select id,pay_id payId, pay_time payTime, pay_type payType,user_id,origin,title,retime from `order` where id=? ";
            Map<String, Object> orderMap = orderService.findOneForJdbc(sql, orderid);
            OrderEntity order = MapUtil.convertMapToBean(orderMap, OrderEntity.class);

            sql = "select openid from `user` where id=?";
            Map<String, Object> userMap = wUserService.findOneForJdbc(sql, Integer.valueOf(String.valueOf(orderMap.get("user_id"))));
            String openId = String.valueOf(userMap.get("openid"));

            String templateId = "";
            String payId = order.getPayId();
            if (payId.split("_").length == 2)
                payId = payId.split("_")[0];
            long payTime = ((long) order.getPayTime()) * 1000;
            String payTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(payTime));

            Map<String, TemplateData> m = new HashMap<String, TemplateData>();
            if (stateCode.equals("pay")) {// 支付成功订单
                m.put("first", createTemp("您好，您的" + thirdPlat.cn_name + "订单已支付成功！"));
                m.put("keyword1", createTemp(payId));
                m.put("keyword2", createTemp(order.getTitle()));
                m.put("keyword3", createTemp(String.valueOf(order.getOrigin())));
                m.put("keyword4", createTemp(payTimeStr));
                m.put("remark", createTemp(""));
                templateId = PortConfig.CREATE_ORDER_TEMPLATE;
            } else if (stateCode.equals("refund")) {
                m.put("first", createTemp("您好，您的" + thirdPlat.cn_name + "订单已退款成功！"));
                m.put("storeName", createTemp(thirdPlat.cn_name));
                m.put("orderId", createTemp(payId));
                m.put("orderType", createTemp("第三方订单"));
                m.put("remark", createTemp("订单金额：" + order.getOrigin()));
                templateId = PortConfig.CANCEL_ORDER_TEMPLATE;
            }
            TemplateMessageUtils.sendTemplateMessage(templateId, openId, ConfigUtil.THIRD_ORDER_DETAIL
                    .replace("THIRD_PLAT", thirdPlat.toString()).replace("ORDER_ID", order.getId().toString()), m);
        } catch (Exception e) {
            logger.error("推送第三方微信模板消息！orderId=" + orderid, e);
        }
    }
    
    /**
     * 闪购订单发送微信推送消息
     * @param order
     * @param stateCode ： delivery--订单发货,refundapply--退款申请结果通知,returnproductapply--退货申请结果通知,refuneSuccess--退款成功通知
     */
    public void flashSendMessage(OrderEntity order, String stateCode) {
    	logger.info("闪购订单微信推送...........");
        try {
        	//获取OpenId
            WUserEntity user = null;
            String openId = "";
            try { 
                user = order.getWuser(); 
                if (user != null && StringUtil.isNotEmpty(user.getOpenId())) {
                    openId = user.getOpenId();
                    logger.info("sendMessage : " + stateCode + "; openid:" + openId+ "; orderId:" + order.getId()+ "; payId:" + order.getPayId()+" ; orderNum : " + order.getOrderNum());
                    if (StringUtils.isEmpty(openId)) {
                        return;
                    }
                } else {
                    return;
                }
            }
            catch (Exception e) { return; }
            
            //模板编辑
            Map<String, TemplateData> m = new HashMap<String, TemplateData>();
            String state = "";
            String remarkstr = "";
            
            TemplateData first = new TemplateData();
            TemplateData keyword1 = new TemplateData();
            TemplateData keyword2 = new TemplateData();
            TemplateData keyword3 = new TemplateData();
            TemplateData keyword4 = new TemplateData();
            TemplateData remark = new TemplateData();
            
            first.setColor("#000000");
            keyword1.setColor("#000000");
            keyword2.setColor("#000000");
            keyword3.setColor("#000000");
            keyword4.setColor("#000000");
            remark.setColor("#000000");
            
            String logisticsName = "";
            String logisticsNumber = "";
            Double refundAmount = 0d;
            if(!ValidUtil.anyEmpty(order.getFlashOrderId().toString())){
        		LogisticsEntity logistics = orderService.getLogisticsByOrderId(order.getId(), "0");
        		if(logistics!=null){
        			logisticsName = logistics.getLogisticsName();
        			logisticsNumber = logistics.getLogisticsNumber();
        		}
        		FlashOrderReturnEntity flashOrderReturn = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
        		if(null!=flashOrderReturn){
        			refundAmount = flashOrderReturn.getRefundAmount();
        		}
        	}
            
            
            String productName = "";
            Integer quantitys = 0;
            List<Map<String, Object>> list = this.findForJdbc(
                    "select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from order_menu a left join menu b on a.menu_id=b.id where a.order_id=?",
                    order.getId());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    Integer quantity = Integer.valueOf(map.get("quantity").toString());
                    quantitys += quantity;
                    if (i != 0) {
                    	productName += ",";
                    }
                    productName += name;
                }
            }
            
            remarkstr = "点击查看订单详情";
            keyword1.setValue(order.getPayId()); //订单号
            String templateId = ""; //模板ID
            
            if ("delivery".equals(stateCode)) {
                state = "您好，您的订单已发货";
                keyword2.setValue(logisticsName);	//快递公司
                keyword3.setValue(logisticsNumber);	//快递单号
                templateId = EnvConfig.wechat.TEMPLATE_ID_FLASHSALE_DELIVERY;
                
            } else if ("refundapply".equals(stateCode)) {
            	if("acceptRefundApply".equals(order.getRstate())){
            		state = "你好，您的退款申请已受理";
            	}else{
            		state = "你好，您的退款申请已被拒绝";
            	}
                keyword2.setValue(String.valueOf(order.getOnlineMoney() + order.getCredit())); //订单实付金额
                remarkstr = "申请退款金额：" + refundAmount + ",点击查看订单详情";
                templateId = EnvConfig.wechat.TEMPLATE_ID_FLASHSALE_REFUNDAPPLY;
                
            } else if ("returnproductapply".equals(stateCode)) {
            	if("acceptRefundApply".equals(order.getRstate())){
            		state = "你好，您的退款退货申请已受理,请尽快将货物寄回";
            	}else{
            		state = "你好，您的退款退货申请已被拒绝";
            	}
                keyword2.setValue(productName); //商品名称 
                keyword3.setValue(String.valueOf(quantitys)); //商品数量
                keyword4.setValue(String.valueOf(order.getOnlineMoney() + order.getCredit())); //订单实付金额
                remarkstr = "申请退款金额：" + refundAmount + ",点击查看订单详情";
                templateId = EnvConfig.wechat.TEMPLATE_ID_FLASHSALE_RETURNPRODUCTAPPLY;
                
            } else if ("refundSuccess".equals(stateCode)) {
                state = "您好，您的退款已成功办理";
                keyword2.setValue(String.valueOf(order.getOnlineMoney() + order.getCredit())); //订单实付金额
                keyword3.setValue(String.valueOf(refundAmount)); //实退金额
                templateId = EnvConfig.wechat.TEMPLATE_ID_FLASHSALE_REFUNDSUCCESS;
                
            }
            

            first.setValue(state);
            m.put("first", first);
            m.put("keyword1", keyword1);
            m.put("keyword2", keyword2);
            m.put("keyword3", keyword3);
            m.put("keyword4", keyword4);
            remark.setValue(remarkstr);
            m.put("remark", remark);
            TemplateMessageUtils.sendTemplateMessage(templateId, openId, String.format(ConfigUtil.ORDER_DETAIL_URL, order.getId()), m);
            
        } catch (Exception e) {
            logger.error("发送微信模板消息失败！orderId=" + order.getId(), e);
        }

    }

    /**
     * 乡村基微信推送
     */
    public void sendRuralbaseMessage(OrderEntity order, String stateCode, String pin, Long orderType) {
        logger.info("**************乡村基微信推送开始************");
        WUserEntity user = order.getWuser();
        String openId = "";
        if (user != null) {
            openId = user.getOpenId();
            logger.info("sendMessage : " + stateCode + "; openid:" + openId);
            if (StringUtils.isEmpty(openId)) {
                return;
            }
        } else {
            return;
        }
        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();

        String saleType = "";
        String state = "";
        String remarkstr = "";

        state = "已出餐";
        remarkstr = "商家已将该菜品配好，请到" + pin + "区领取！";
        saleType = "门店";
        String orderNum = "0000";
        if (order.getOrderNum() != null && order.getOrderNum().length() > 8) {
            orderNum = order.getOrderNum().substring(8);
        }

        first.setColor("#000000");
        first.setValue("您好，您的" + saleType + "订单，排号（" + pin + orderNum + "）" + state);

        m.put("first", first);

        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#000000");
        keyword1.setValue(order.getMerchant().getTitle());
        m.put("keyword1", keyword1);

        long createTime = ((long) order.getCreateTime()) * 1000;
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue(yyyyMMdd.format(new Date(createTime)));
        m.put("keyword2", keyword2);

        List<Map<String, Object>> list = this.findForJdbc(
                "select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from order_menu a left join menu b on a.menu_id=b.id where a.order_id=? and b.print_type=?",
                order.getId(), orderType);
        String content = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String name = map.get("name").toString();
                String quantity = map.get("quantity").toString();
                if (i != 0) {
                    content += ",";
                }
                content += name + " " + quantity + "份";
            }
        }

        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#000000");
        keyword3.setValue(content);
        m.put("keyword3", keyword3);

        TemplateData keyword4 = new TemplateData();
        keyword4.setColor("#000000");
        // 用户实际支付金额
        String totalMoney = orderService.getOrderRealMoney(order);
        keyword4.setValue(totalMoney + "元");
        m.put("keyword4", keyword4);

        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue(remarkstr);
        m.put("remark", remark);
        TemplateMessageUtils.sendTemplateMessage(openId, String.format(ConfigUtil.ORDER_DETAIL_URL, order.getId()), m);
        logger.info("**************乡村基微信推送结束************");
    }
    
    
    private TemplateData createTemp(String color, String value) {
        TemplateData tmp = new TemplateData();
        tmp.setColor(StringUtil.isEmpty(color) ? "#000000" : color);
        tmp.setValue(StringUtil.isEmpty(value) ? "" : value);
        return tmp;
    }

    private TemplateData createTemp(String value) {
        return createTemp(null, value);
    }
    
    public class SendTask implements Runnable {
		
		private String stateCode;
		private OrderEntity order;

		public SendTask(OrderEntity order, String stateCode) {
			this.order = order;
			this.stateCode = stateCode;
		}

		public void run() {
			send();
		}

		private void send() {
			try {
	            String openId = "";
	            WUserEntity user = order.getWuser(); 
	            Integer orderId = order.getId();
                if (order.getUserId() > 0 && user != null && StringUtil.isNotEmpty(user.getOpenId())) {
                    openId = user.getOpenId();
                    logger.info("sendMessage : " + stateCode + "; openid:" + openId+ "; orderId:" + orderId + "; payId:" + order.getPayId()+" ; orderNum : " + order.getOrderNum());
                } else {
                	logger.warn("sendMessage openid is empty !!! orderId:{}", orderId);
                    return;
                }
	            
	            Map<String, TemplateData> m = new HashMap<String, TemplateData>();
	            TemplateData first = new TemplateData();

	            String saleType = "";
	            String state = "";
	            String remarkstr = "";
	            String doneContent = "";

	            if ("paySuccess".equals(stateCode)) {
	                state = "下单成功";
	                remarkstr = "等待商家接单中，" + order.getMerchant().getMobile();
	            } else if ("accept".equals(stateCode)) {
	                state = "商家已经接单";
	                remarkstr = "商家正在制作中，请稍侯！";
	            } else if ("delivery".equals(stateCode)) {
	                state = "开始配送";
	                remarkstr = "快递员正在配送中,请稍侯！";
	                if (!ValidUtil.anyEmpty(order.getCourierId())) {
	                    WUserEntity courUser = wUserService.get(WUserEntity.class, order.getCourierId());
	                    if (!ValidUtil.anyEmpty(courUser, courUser.getUsername(), courUser.getMobile())) {
	                        remarkstr = "快递员正在配送中,请稍侯！快递员姓名:" + courUser.getUsername() + ",电话:" + courUser.getMobile();
	                    }
	                }
	            } else if ("done".equals(stateCode)) {
	                state = "已送达";
	                remarkstr = "亲，你的商品已送达，满意请给个五星好评哦。";
	            }

	            if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
	                saleType = "外卖";
	            } else {
	                saleType = "门店";
	            }
	            first.setColor("#000000");
	            String firstValue = "您好，您的" + saleType + "订单，排号（"
	                    + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
	                            : Integer.parseInt(order.getOrderNum().substring(8)))
	                    + "）" + state; 
	            first.setValue(firstValue);
	            m.put("first", first);

	            TemplateData keyword1 = new TemplateData();
	            keyword1.setColor("#000000");
	            String keyword1Value = order.getMerchant().getTitle(); 
	            keyword1.setValue(keyword1Value);
	            m.put("keyword1", keyword1);

	            long createTime = ((long) order.getCreateTime()) * 1000;
	            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	            TemplateData keyword2 = new TemplateData();
	            keyword2.setColor("#000000");
	            String keyword2Value = yyyyMMdd.format(new Date(createTime)); 
	            keyword2.setValue(keyword2Value);
	            m.put("keyword2", keyword2);

	            List<Map<String, Object>> list = orderService.findForJdbc(getOrderMenu, orderId);
	            String content = "";
	            if (list != null) {
	                for (int i = 0; i < list.size(); i++) {
	                    Map<String, Object> map = list.get(i);
	                    String name = map.get("name").toString();
	                    String quantity = map.get("quantity").toString();
	                    if (i != 0) {
	                        content += ",";
	                    }
	                    content += name + " " + quantity + "份";
	                }
	            }

	            TemplateData keyword3 = new TemplateData();
	            keyword3.setColor("#000000");
	            keyword3.setValue(content);
	            m.put("keyword3", keyword3);

	            TemplateData keyword4 = new TemplateData();
	            keyword4.setColor("#000000");
	            // 用户实际支付金额
	            String totalMoney = orderService.getOrderRealMoney(order) + "元";
	            keyword4.setValue(totalMoney);
	            m.put("keyword4", keyword4);

	            TemplateData remark = new TemplateData();
	            remark.setColor("#000000");
	            remark.setValue(remarkstr);
	            m.put("remark", remark);
	            
	            logger.info("发送微信通知消息:"+remarkstr+",orderId="+orderId);
	            if ("done".equals(stateCode)) {
	            	doneContent += firstValue + "\n"
	            			+ "商家名称："+ keyword1Value + "\n"
	            			+ "下单时间："+ keyword2Value + "\n"
	            			+ "商品明细："+ content + "\n"
	            			+ "订单金额："+ totalMoney + "\n"
	            			+remarkstr;
	            	TemplateMessageUtils.sendCustomMessage(openId, state,doneContent);
	            } else if(OrderType.EATIN_ORDER.getName().equals(order.getOrderType())){
	            	TemplateMessageUtils.sendTemplateMessage(openId, String.format(ConfigUtil.EATIN_ORDER_DETAIL, orderId),m);
	            } else {
	            	TemplateMessageUtils.sendTemplateMessage(openId, String.format(ConfigUtil.ORDER_DETAIL_URL, orderId),m);
	            }
	        } catch (Exception e) {
	            logger.error("发送微信模板消息失败！orderId=" + order.getId(), e);
	        }
		} 
	}
}