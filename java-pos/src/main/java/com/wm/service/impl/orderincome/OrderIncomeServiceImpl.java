package com.wm.service.impl.orderincome;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.dao.agent.AgentIncomeTimerDao;
import com.wm.dao.agent.AgentRebateDao;
import com.wm.dto.merchant.MerchantSupplySaleOrderDto;
import com.wm.entity.agent.AgentIncomeTimerEntity;
import com.wm.entity.agent.AgentIncomeTimerHistoryEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.TomMerchantMemberDiscountEntity;
import com.wm.entity.merchantinfo.MerchantDeductionEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.DineInDiscountLogEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntity.PayType;
import com.wm.entity.order.OrderEntity.SaleType;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchantinfo.MerchantDeductionServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;

@Service("orderIncomeService")
@Transactional
public class OrderIncomeServiceImpl extends CommonServiceImpl implements OrderIncomeServiceI {

    private static final Logger logger = LoggerFactory.getLogger(OrderIncomeServiceImpl.class);

    @Autowired
    private FlowServiceI flowService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private AgentRebateDao agentRebateDao;
    @Autowired
    private AgentIncomeTimerDao agentIncomeTimer;
    @Autowired
    private SystemconfigServiceI systemconfigServiceI;
    @Autowired
    private MerchantDeductionServiceI merchantDeductionService;

    private final static double ALI_DEDUCTION = 0.0040; // 支付宝支付方式产生的扣点
    private final static Integer INCOME_DAYS_PLAN_20160706 = 160706; // T+0结算B方案，不再立即到账，而是当天晚上24点到帐

    private final static String findSettleList = "select oi.id from order_income oi left join `order` o on o.id=oi.order_id "
            + " where oi.type=0 and oi.state='unpay' and oi.pay_time<unix_timestamp(now()) and o.state='confirm' "
            + " and o.rstate in ('normal','norefund') and o.order_type<>'flashsales' order by oi.id desc";
    private final static String getFlowCount = "select count(id) from flow where action='orderIncome' and detail_id=?";
    private final static String getMchDiscountConf = "select `value` from system_config where `code`='merchant_member_discount_number'";
    private final static String getTotalMoney4JZ = "SELECT sum(u.money) from user u left join merchant m on m.user_id=u.id "
    		+ " left join 0085_merchant_info i on i.merchant_id=m.id where u.user_type='merchant' and i.platform_type=2 and u.money > 0 ";

    @Override
    public void createOrderIncome(OrderEntity order) throws Exception {
        long startTime = System.currentTimeMillis();
        Integer orderId = order.getId();
        order = orderService.get(OrderEntity.class, orderId);
        logger.info("订单号:{}的支付途径:{}", orderId, order.getPayType());

        // 判断哪种支付途径
        String orderPayType = order.getPayType();

        // 众包订单商家自己下单不用进预收入， 直接返回
        if (OrderEntity.FromType.CROWDSOURCING.equals(order.getFromType())) {
            logger.info("众包订单 orderId:[{}]不进入预收入！", orderId);
            return;
        }

        boolean isoffline = StringUtils.isNotEmpty(order.getRemark()) && order.getRemark().startsWith("[offline_order]");

        // 构建OrderIncomeEntity实体 type 预收入类型，0 普通预收入，1 供应链预收入
        OrderIncomeEntity orderIncome = getOrderIncomeByOrderIdAndType(orderId, 0);
        if (orderIncome == null) {
            orderIncome = new OrderIncomeEntity();
        }

        MerchantEntity merchant = order.getMerchant();
        Integer merchantId = merchant.getId();

        // 商家会员折扣
        TomMerchantMemberDiscountEntity merchantMember = this
                .findUniqueByProperty(TomMerchantMemberDiscountEntity.class, "merchantId", merchantId);
        Integer merchantMemberDiscount = 100;
        if (merchantMember != null) {
            merchantMemberDiscount = merchantMember.getMerchantDiscount();
        }

        MerchantInfoEntity merchantInfo = new MerchantInfoEntity();
        Integer platType = 1;
        double money = 0.0000;

        List<MerchantInfoEntity> list = this.findByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
        if (CollectionUtils.isNotEmpty(list)) {
            merchantInfo = list.get(0);
            platType = merchantInfo.getPlatformType();

            double deduction = getMerchantDeduction(order, merchant, platType);
            // 判断订单是外卖订单 or 堂食订单 1为外卖订单，2为堂食订单
            if (SaleType.TAKEOUT.equals(order.getSaleType())) {
                money = orderBalancePay(order, orderPayType, merchantInfo, merchantMemberDiscount, deduction);
            } else {
                // 判断订单是否使用堂食折扣
                // 商家会员卡支付和平台会员卡支付不能使用堂食折扣(一个订单只能使用一种折扣)
                if (merchantInfo.getIsDineInDiscount() == 1 && !PayType.MERCHANTPAY.equals(orderPayType)
                        && !PayType.BALANCE.equals(orderPayType) && order.getDineInDiscountMoney() != 0) {
                    money = orderHallFoodPay(order, deduction);
                } else {
                    money = orderBalancePay(order, orderPayType, merchantInfo, merchantMemberDiscount, deduction);
                }
            }

            // 判断是否是代理商商家
            if (platType.equals(2)) {
                // 创建代理商扣点返点定时器
            	Integer creator = merchant.getWuser().getCreator();
            	if(creator > 0){
            		WUserEntity user = this.get(WUserEntity.class, creator);
                    if (user != null && "agent".equals(user.getUserType())) {
                        createAgentIncomeTimer(order, user, merchantId, merchantInfo.getDeductionType());
                    } else {
                        logger.warn("未找到代理商相关userId:[" + creator + "]用户信息");
                    }
            	} else {
            		logger.warn("未找到代理商相关userId:[" + creator + "]用户信息");
            	}
            } else {
                logger.info("非合作商商家{}", merchantId);
            }

        } else {
            logger.warn("未找到商家{}对应的拓展信息！", merchantId);
            money = orderBalancePay(order, orderPayType, merchantInfo, merchantMemberDiscount, 0.0);
        }

        orderIncome.setMerchantId(merchantId);// 商家ID
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);// 关闭分组显示
        String origin = numberFormat.format(order.getOrigin() + order.getDeliveryFee() + order.getCostLunchBox());
        orderIncome.setOrigin(Double.parseDouble(origin));// 总金额
        orderIncome.setOrderId(orderId);// 订单ID
        orderIncome.setPayId(order.getPayId());// 支付ID
        orderIncome.setDeliveryMoney(0.00);// 快递员收入
        orderIncome.setType(0);
        int incomeTime = getIncomeTime(order, merchant, platType);
        orderIncome.setPayTime(order.getCompleteTime() + incomeTime);// 支付到账时间
        // 判断如果是闪购订单则快递费全部结算到商家预预收入
        if (OrderEntity.OrderType.FLASHSALES.equals(order.getOrderType())) {
            money += order.getDeliveryFee();
        }

        // 商家实际收入
        orderIncome.setMoney(money);

        if (null == orderIncome.getId()) {
            orderIncome.setState("unpay");
            orderIncome.setCreateTime(isoffline ? order.getCreateTime() : DateUtils.getSeconds());
            this.save(orderIncome);
        } else {
            this.updateEntitie(orderIncome);
        }

        //加盟店现金订单结算状态为已结算状态
        if ((OrderEntity.PayType.SUPERMARKT_CASH.equals(order.getPayType()) && merchantInfo.getShopFromType().intValue() == 1)
                || OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(order.getOrderType())) {
            orderIncome.setState("pay");
            this.updateEntitie(orderIncome);
        }

        // 闪购订单确认收货5天之后才结算
        if (OrderEntity.OrderType.FLASHSALES.equals(order.getOrderType())) {
            return;
        }

        //超市现金订单不立即结算
        if (!OrderEntity.PayType.SUPERMARKT_CASH.equals(order.getPayType()) && incomeTime <= 0) {
            unOrderIncome(orderIncome.getId());
        }
        long costTime = System.currentTimeMillis() - startTime;
        logger.info("orderId:{} createOrderIncome costtime:{} ms", orderId, costTime);
    }

    public void unOrderIncome(Integer orderIncomeId) throws Exception {
        long startTime = System.currentTimeMillis();
        OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class, orderIncomeId);
        if (orderIncome == null) {
            logger.error("找不到对应的预收入记录！orderIncomeId={}", orderIncomeId);
            return;
        }
        if ("unpay".equals(orderIncome.getState())) {
            Integer orderId = orderIncome.getOrderId();
            OrderEntity order = orderService.get(OrderEntity.class, orderId);
            if (order == null) {
                logger.error("找不到对应的订单记录！orderId={}", orderId);
                return;
            }


            // 添加之前先验证是否已有订单收入流水，避免重复
            Integer count = flowService.findOneForJdbc(getFlowCount, Integer.class, orderId);
            if (count > 0) {
                logger.warn("商家订单收入流水不可重复！orderId={}", orderId);
            } else {
                flowService.merchantOrderIncome(order.getMerchant().getWuser().getId(), orderIncome.getMoney(), orderId);
            }

            orderIncome.setState("pay");
            this.updateEntitie(orderIncome);

            order.setStatus("Y");
            order.setState("confirm");
            orderService.updateEntitie(order);
            long costTime = System.currentTimeMillis() - startTime;
            logger.info("orderId:{} unOrderIncome costtime:{} ms", orderId, costTime);
        } else {
            logger.warn("该订单orderIncomeId={}已结算！！！", orderIncomeId);
        }
    }

    /**
     * 获取商家结算扣点
     *
     * @param order
     * @param merchant
     * @param platType
     * @return
     */
    private double getMerchantDeduction(OrderEntity order, MerchantEntity merchant, Integer platType) {
        double deduction = 0.0000;
        Integer merchantId = merchant.getId();
        MerchantInfoEntity merchantInfo = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
        if (OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())) {
            // 扫码是否需要回扣（0否，1是）
            if (1 == merchantInfo.getIsScan()) {
            	String payType = order.getPayType();
            	if(OrderEntity.PayType.DPT_PAY.equals(payType)){
            		MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 3);
                    if (merchantDeduction != null) {
                        deduction = merchantDeduction.getDeduction();
                    } else {
                    	merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 2);
                        if (merchantDeduction != null) {
                            deduction = merchantDeduction.getDeduction();
                        }
                    }
            	} else {
            		MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 2);
                    if (merchantDeduction != null) {
                        if (platType.equals(2) && OrderEntity.PayType.ALIPAY.equals(order.getPayType())) {
                            deduction = ALI_DEDUCTION;
                        } else {
                            deduction = merchantDeduction.getDeduction();
                        }
                    }
            	}
            }
        } else if (!OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())
                && OrderEntity.SaleType.DINE.equals(order.getSaleType())) {// 普通门店非扫码订单
            // 堂食是否需要回扣（0否，1是）
            if (1 == merchantInfo.getIsHallFood()) {
                MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 1);
                if (merchantDeduction != null) {
                    deduction = merchantDeduction.getDeduction();
                }
            }
        } else {// 外卖及闪购订单
            if (1 == merchantInfo.getIsTakeout()) {
                deduction = merchant.getDeduction();
            }
        }
        return deduction;
    }

    /**
     * 获取商家结算周期
     *
     * @param order
     * @param merchant
     * @param platType 平台类型
     * @return
     */
    private int getIncomeTime(OrderEntity order, MerchantEntity merchant, Integer platType) {
        Integer incomeDays = INCOME_DAYS_PLAN_20160706; //到帐天数
        if (OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())) { // 扫码
        	String payType = order.getPayType();
        	if(OrderEntity.PayType.DPT_PAY.equals(payType)){
        		MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchant.getId(), 3);
	            if (merchantDeduction != null) {
	                incomeDays = merchantDeduction.getIncomeDate();
	            }
        	} else {
	            MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchant.getId(), 2);
	            if (merchantDeduction != null) {
	                incomeDays = merchantDeduction.getIncomeDate();
	            }
        	}
        } else if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) { // 非扫码堂食
            MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchant.getId(), 1);
            if (merchantDeduction != null) {
                incomeDays = merchantDeduction.getIncomeDate();
            }
            if (OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(order.getOrderType())
                    || OrderEntity.PayType.SUPERMARKT_CASH.equals(order.getPayType())) {
                // 超市现金订单，收银员未结算之前，不让定时任务自动结算，时间设为20年之后
                incomeDays = 365 * 20;//20年后
                logger.info("incomeDays：{}", incomeDays);
            }
        } else { // 外卖订单
            incomeDays = merchant.getIncomeDate();
        }

        Integer orderId = order.getId();
        logger.info("order:{} incomeDays:{}", orderId, incomeDays);
        Integer incomeTime = 0; //到帐时间
        if (INCOME_DAYS_PLAN_20160706.equals(incomeDays)) {
            incomeTime = 1; // 加1是为了区分0，立即到账的情况
        } else if (platType.equals(2)) {
            if (incomeDays > 0) {
                incomeTime = (incomeDays - 1) * 3600 * 24 + 1;
            } else {
                logger.info("合作商T+0到帐，orderId:{}", orderId);
            }
        } else {
            incomeTime = incomeDays * 3600 * 24;
        }
        logger.info("order:{} incomeTime:{}", orderId, incomeTime);
        return incomeTime;
    }

    /**
     * 商家扣点结算方法
     */
    private double orderBalancePay(OrderEntity order, String orderPayType, MerchantInfoEntity merchantInfo,
                                   Integer merchantMemberDiscount, Double deduction) {

        // 订单总金额=订单金额+餐盒费
        double totalOrdermoney = 100 * order.getOrigin() + 100 * order.getCostLunchBox();

        // 平台商家所有配送费归平台，代理商商家则配送费计入预收入
        if (2 == merchantInfo.getPlatformType()) {
            // 订单总金额=订单金额+餐盒费+配送费
            totalOrdermoney += 100 * order.getDeliveryFee();
        }

        // 会员充值满送活动，送的这一部分由商家来承担
        if (PayType.MERCHANTPAY.equals(orderPayType)) {
            totalOrdermoney = totalOrdermoney - this.merchantMemberSettlement(order.getOrigin());
        }

        double money = 0.0000; // 精确到4位小数点
        deduction = 100 * deduction;
        if (PayType.MERCHANTPAY.equals(orderPayType) && !OrderEntity.OrderType.MOBILE.equals(order.getOrderType())) {
            money = totalOrdermoney * (merchantMemberDiscount - deduction);
            logger.info("商家会员支付");
        } else if (PayType.BALANCE.equals(orderPayType)) {
            money = totalOrdermoney * (100 - deduction);
            logger.info("平台会员支付");
        } else {
            /**
             * 在线支付 weixinpay / wft_pay / alipay 等等
             * 如果为微信支付途径，有使用‘商家优惠券’,则获取优惠券金额 ，平台按之前规则走 10 平台 20商家
             */
            if (order.getCard() != 0) {
                String cardId = order.getCardId();
                if (cardId != null && cardId.length() > 2 && "20".equals(cardId.substring(0, 2))) {
                    double discountcard = order.getCard();
                    money = totalOrdermoney * (100 - deduction) - 100 * discountcard;
                    logger.info("在线支付(商家优惠券)");
                } else {
                    // 平台优惠券
                    money = totalOrdermoney * (100 - deduction);
                    logger.info("在线支付(平台优惠券)");
                }
            } else {
                money = totalOrdermoney * (100 - deduction);
                logger.info("在线支付");
            }
        }
        money = money / 10000;
        logger.info("orderId：{}, 商家预收入金额：{}元", order.getId(), money);
        return money;
    }

    /**
     * 会员优惠，结算需要扣除的金额，单位：分
     *
     * @param origin 订单金额
     * @return
     */
    private int merchantMemberSettlement(Double origin) {
        int result = 0;
        Double discountNumber = this.findOneForJdbc(getMchDiscountConf, Double.class);
        if (null != discountNumber && discountNumber > 1) {
            result = new BigDecimal(100)
                    .multiply(new BigDecimal(origin))
                    .multiply(new BigDecimal(discountNumber - 1))
                    .divide(new BigDecimal(discountNumber), 0, BigDecimal.ROUND_HALF_UP).intValue(); // 四舍五入
        }
        logger.info("discountNumber:{}, origin:{}, result:{}", discountNumber, origin, result);
        return result;
    }

    /**
     * 堂食开启折扣结算方法
     * <p>
     * 平台扣点=订单原价*扣点 商家预收入=用户支付+平台分摊费一平台扣点 平台盈利=平台扣点—平台分摊
     * 平台盈利包含平台扣点—平台付出的补贴
     */
    private Double orderHallFoodPay(OrderEntity order, double deduction) {
        double totalOrdermoney = 100 * order.getOrigin();
        double money = order.getOrigin();
        Integer orderId = order.getId();
        DineInDiscountLogEntity didle = this.findUniqueByProperty(DineInDiscountLogEntity.class, "orderId", orderId);
        if (didle == null) {
            logger.warn("订单orderId:{}未找到堂食折扣信息", orderId);
            return money;
        }
        // 获取堂食折扣
        Integer dineInDiscount = didle.getDiscount();
        // 获取商家分摊比例
        Integer platformShare = didle.getPlatformSharePercent();
        money = totalOrdermoney * (dineInDiscount + platformShare - 100 * deduction);
        money = money / 10000;
        logger.info("堂食折扣订单orderId:{}, 商家预收入金额：{}元", orderId, money);
        return money;
    }

    /**
     * 代理商返点结算记录 代理商结构：
     * 九州(最高级别,下属只有代理商)<---一级代理商(下属有代理商和签约商家)<---二级代理商(下属只有签约商家)
     * 每笔订单用户支付总额全部计入九州账户可用余额(agent_info表的income),所有用户提现则直接从九州账户可用余额扣除(包括商家提现)
     */
    private void createAgentIncomeTimer(OrderEntity order, WUserEntity user, Integer merchantId, String deductionType) {
        Integer orderId = order.getId();
        logger.info("--------------添加代理商定时记录orderId:{}----------------", orderId);
        MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(merchantId, 2);

        int incomeDate = 0;
        if (merchantDeduction != null && merchantDeduction.getIncomeDate() != null) {
            incomeDate = merchantDeduction.getIncomeDate();
        }

        BigDecimal orderMoney = new BigDecimal(order.getOrigin());

        /**
         * 创建直营收入
         */
        createAgentIncome(user, orderId, orderMoney, 0, incomeDate, 1, 2, deductionType);

        // 代理商的上级
        WUserEntity buser = this.get(WUserEntity.class, user.getCreator());
        /**
         * 递归创建合作商分销收入
         */
        recursionCreateAgentIncome(buser, orderId, orderMoney, user.getId(), incomeDate, deductionType);
    }

    /**
     * 递归创建合作商分销收入
     *
     * @param buser
     * @param orderId
     * @param orderMoney
     * @param subUserId
     * @param incomeDate
     * @return
     */
    private void recursionCreateAgentIncome(WUserEntity buser, Integer orderId, BigDecimal orderMoney,
                                            Integer subUserId, int incomeDate, String deductionType) {
        if (buser == null) {
            return;
        }
        Integer jzUserId = Integer.valueOf(systemconfigServiceI.getValByCode("agent_user_id"));// 获取广州九州互通支付有限公司的用户id
        if (jzUserId != null && !jzUserId.equals(buser.getId()) && "agent".equals(buser.getUserType())) {
            /**
             * 创建分销收入
             */
            createAgentIncome(buser, orderId, orderMoney, subUserId, incomeDate, 2, 1, deductionType);
            buser = this.get(WUserEntity.class, buser.getCreator());
            recursionCreateAgentIncome(buser, orderId, orderMoney, subUserId, incomeDate, deductionType);
        } else {
            logger.warn("代理商userId:{}的用户类型为userType:{}", buser.getId(), buser.getUserType());
        }
    }

    /**
     * 添加定时器记录
     *
     * @param user       结算用户
     * @param orderId    订单
     * @param orderMoney 订单总金额
     * @param subUserId  子代理
     * @param records
     * @param incomeDate 结算周期
     * @param rateType   返点率类型   1:直营返点率     2:分销返点率
     * @param timerType  定时器收入类型     1:分销收入      2：直营收入
     * @return
     */
    private void createAgentIncome(WUserEntity user, Integer orderId, BigDecimal orderMoney,
                                   Integer subUserId, int incomeDate, int rateType, Integer timerType, String deductionType) {
        AgentIncomeTimerHistoryEntity aith = agentIncomeTimer.getAgentIncomeTimerHistoryEntity(orderId, user.getId(), timerType);
        if (aith != null) {
            logger.info("createAgentIncome:合作商userId:{}订单orderId:{}已结算type：{}的返点收入", new Object[]{user.getId(), orderId, timerType});
            return;
        }

        AgentInfoEntity agent = this.findUniqueByProperty(AgentInfoEntity.class, "userId", user.getId());
        if (agent == null) {
            logger.info("未找到用户userId:{}相关的代理商信息,orderId:{},共创建{}条返点结算记录",
                    new Object[]{user.getId(), orderId});
            return;
        }

        BigDecimal brebate = agentRebateDao.getAgentRebate(agent, rateType, incomeDate, deductionType);
        BigDecimal bincome = orderMoney.multiply(brebate).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
        // 添加定时器记录
        AgentIncomeTimerEntity timer = agentIncomeTimer.getAgentIncomeTimerEntity(orderId, user.getId(), timerType);
        if (timer == null) {
            timer = new AgentIncomeTimerEntity();
        }
        timer.setOrderId(orderId);
        timer.setOrderMoney(orderMoney.doubleValue());
        timer.setCreateTime(DateUtils.getSeconds());
        timer.setIncome(bincome.doubleValue());
        timer.setUserId(user.getId());
        timer.setType(timerType);// 结算类型 1 分销返点 2 直营返点
        timer.setSubUserId(subUserId);
        timer.setRate(brebate);
        timer.setRateType(deductionType);
        if (null == timer.getId()) {
            this.save(timer);
        } else {
            this.updateEntitie(timer);
        }
    }

    public void unAgentIncome(List<Map<String, Object>> list) {
        if (list == null) {
            logger.info("结算记录为空!!!");
            return;
        }
        Integer id = 0;
        WUserEntity user = null;
        AgentIncomeTimerEntity agentIncome = null;
        AgentIncomeTimerHistoryEntity history = null;
        for (Map<String, Object> map : list) {
            id = Integer.valueOf(map.get("id").toString());
            agentIncome = this.get(AgentIncomeTimerEntity.class, id);
            if (agentIncome == null) {
                logger.error("代理商定时器中不存在id={}", id);
            } else {
                // 给代理商结算
                user = this.get(WUserEntity.class, agentIncome.getUserId());
                if (user == null) {
                    logger.error("未找到代理商相关userId{}用户信息", agentIncome.getUserId());
                } else {
                    try {
                        // 代理商结算
                        if (agentIncome.getIncome() != 0) {
                            flowService.agentIncomeFlowCreate(agentIncome);
                        }
                        // 添加返点扣点结算的历史记录
                        history = new AgentIncomeTimerHistoryEntity();
                        history.setCreateTime(agentIncome.getCreateTime());
                        history.setIncome(agentIncome.getIncome());
                        history.setOrderId(agentIncome.getOrderId());
                        history.setOrderMoney(agentIncome.getOrderMoney());
                        history.setPayState("pay");
                        history.setPayTime(DateUtils.getSeconds());
                        history.setType(agentIncome.getType());
                        history.setUserId(agentIncome.getUserId());
                        history.setSubUserId(agentIncome.getSubUserId());
                        history.setRate(agentIncome.getRate());
                        history.setRateType(agentIncome.getRateType());
                        this.saveOrUpdate(history);

                        agentIncomeTimer.deleteAgentIncomeTimerRecord(id);
                    } catch (Exception e) {
                        logger.error("agent_income_timer:id=[" + id + "]结算失败!!!", e);
                    }
                }
            }

        }
    }

    public List<Map<String, Object>> findSettleList() {
        return this.findForJdbc(findSettleList);
    }

    public void settleMent(Integer orderIncomeId) throws Exception {
        OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class, orderIncomeId);
        if (orderIncome != null) {
            OrderEntity order = orderService.get(OrderEntity.class, orderIncome.getOrderId());
            if (order != null) {
                MerchantEntity merchant = order.getMerchant();
                if (merchant != null) {
                    MerchantInfoEntity merchantInfo = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId",
                            merchant.getId());
                    if (merchantInfo != null) {
                        this.unOrderIncome(orderIncomeId);
                    } else {
                        logger.error("orderIncomeId:{}结算失败！找不到merchantInfo信息", orderIncomeId);
                    }
                } else {
                    logger.error("orderIncomeId:{}结算失败！找不到merchant信息", orderIncomeId);
                }
            } else {
                logger.error("orderIncomeId:{}结算失败！找不到order信息", orderIncomeId);
            }
        } else {
            logger.error("orderIncomeId:{}结算失败！找不到orderIncome信息", orderIncomeId);
        }
    }

    /**
     * 供应链商家预收入
     *
     * @param saleOrderdto
     * @throws Exception
     */
    @Override
    public void createSupplyOrderIncome(MerchantSupplySaleOrderDto saleOrderdto) throws RuntimeException {
        logger.info("创建供应链预收入，订单ID:" + saleOrderdto.getOrderId());

        // 构建OrderIncomeEntity实体
        OrderIncomeEntity orderIncome = getOrderIncomeByOrderIdAndType(saleOrderdto.getOrderId(), 1); // type
        // 预收入类型，0
        // 普通预收入，1
        // 供应链预收入
        if (orderIncome != null && orderIncome.getPayId().equals(saleOrderdto.getPayId())) {
            logger.info("供应链商家订单结算orderId:{},payId:{}已存在记录！money:{}",
                    new Object[]{saleOrderdto.getOrderId(), saleOrderdto.getPayId(), saleOrderdto.getMoney()});
            throw new RuntimeException("供应链商家订单结算已存在记录,orderId=" + saleOrderdto.getOrderId());
        }

        orderIncome = new OrderIncomeEntity();
        MerchantEntity merchant = this.findUniqueByProperty(MerchantEntity.class, "wuser.id", saleOrderdto.getUserId());// 商家
        if (saleOrderdto.getPayId() != null) {
            // 组装数据
            Integer merchantId = 0;
            int incomeTime = 0;
            if (merchant != null) {
                merchantId = merchant.getId();
                // 结算周期
                incomeTime = merchant.getIncomeDate() * 3600 * 24;
            }
            orderIncome.setMerchantId(merchantId);// 商家ID
            orderIncome.setOrigin(saleOrderdto.getOrigin().doubleValue());// 总金额
            orderIncome.setMoney(saleOrderdto.getMoney().doubleValue());// 商家实际收入
            orderIncome.setOrderId(saleOrderdto.getOrderId());// 订单ID
            orderIncome.setPayId(saleOrderdto.getPayId());// 支付ID
            orderIncome.setDeliveryMoney(0.00);// 快递员收入
            orderIncome.setCreateTime(DateUtils.getSeconds());// 创建时间
            orderIncome.setState("unpay");// 状态为未支付
            orderIncome.setType(1);

            orderIncome.setPayTime(DateUtils.getSeconds() + incomeTime);// 支付时间

            // 保存
            this.save(orderIncome);

            if (incomeTime <= 0) {
                unSupplyOrderIncome(orderIncome.getId(), saleOrderdto.getUserId()); // 立即结算
            }

        }
    }

    /**
     * 供应链商家结算
     *
     * @throws Exception
     */
    @Override
    public void unSupplyOrderIncome(Integer orderIncomeId, Integer userId) throws RuntimeException {
        logger.info("供应链商家结算unSupplyOrderIncome---------orderIncomeId:{},userId:{}",
                new Object[]{orderIncomeId, userId});
        OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class, orderIncomeId);
        if (orderIncome == null) {
            logger.error("找不到对应的预收入记录！orderIncomeId=" + orderIncomeId);
            throw new RuntimeException("供应链商家结算,找不到对应的预收入记录！orderIncomeId=" + orderIncomeId);
        }
        if ("unpay".equals(orderIncome.getState())) {

            Integer orderId = orderIncome.getOrderId();

            // 添加之前先验证是否已有订单收入流水，避免重复
            Long count = this.getCountForJdbcParam(
                    "select count(id) from flow where action='supplyOrderIncome' and detail_id=? and user_id=?",
                    new Object[]{orderId, userId});
            if (count > 0) {
                orderIncome.setState("pay");
                this.updateEntitie(orderIncome);
                logger.error("商家订单收入流水不可重复！orderId=" + orderId);
                throw new RuntimeException("商家订单收入流水不可重复！orderId=" + orderId);
            }
            // 添加流水
            flowService.merchantSupplyOrderIncome(orderId.longValue(), userId,
                    BigDecimal.valueOf(orderIncome.getMoney()));

            orderIncome.setState("pay");
            this.updateEntitie(orderIncome);
        }
    }

    /**
     * type 预收入类型，0 普通预收入，1 供应链预收入 获取供应链的orderIncomeEntity实体时还需要判断pay_id
     */
    @Override
    public OrderIncomeEntity getOrderIncomeByOrderIdAndType(Integer orderId, int type) {
        Map<String, Object> map = this.findOneForJdbc("SELECT id FROM order_income WHERE order_id=? AND type=?",
                orderId, type);
        if (map != null && map.get("id") != null) {
            OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class,
                    Integer.valueOf(map.get("id").toString()));
            return orderIncome;
        } else {
            return null;
        }
    }

    /**
     * 超市加盟店结算    不更新余额
     *
     * @param orderIncomeId
     */
    @Override
    public void franchiseUnOrderIncome(Integer orderIncomeId) {
        OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class, orderIncomeId);
        if (orderIncome == null) {
            logger.error("找不到对应的预收入记录！orderIncomeId={}", orderIncomeId);
            return;
        }
        if ("unpay".equals(orderIncome.getState())) {
            Integer orderId = orderIncome.getOrderId();
            OrderEntity order = orderService.get(OrderEntity.class, orderId);
            if (order == null) {
                logger.error("找不到对应的订单记录！orderId={}", orderId);
                return;
            }

//			orderIncome.setState("pay");
            // 修改结算收入时间
            orderIncome.setPayTime(DateUtils.getSeconds());
            this.updateEntitie(orderIncome);

            order.setStatus("Y");
            order.setState("confirm");
            orderService.updateEntitie(order);
        } else {
            logger.warn("该订单orderIncomeId={}已结算！！！", orderIncomeId);
        }
    }

    /**
     * 直营店，收银员交班结算，超市现金订单，校验是否有账期。有账期添加账期后，通过定时任务结算。没有账期直接结算
     */
    @Override
    public void directCashierUnOrderIncome(Integer orderIncomeId) throws Exception {
        OrderIncomeEntity orderIncome = this.get(OrderIncomeEntity.class, orderIncomeId);
        Integer orderId = orderIncome.getOrderId();
        OrderEntity order = orderService.get(OrderEntity.class, orderId);
        //收银员结算
        if (orderIncome.getPayTime() > DateUtils.getSeconds()) {
            logger.info("收银员结算,orderId:{}", orderId);
            int incomeDays = 0;
            MerchantDeductionEntity merchantDeduction = merchantDeductionService.getMerchantDeduction(order.getMerchant().getId(), 1);
            if (merchantDeduction != null) {
                incomeDays = merchantDeduction.getIncomeDate().intValue();
            }
            logger.info("超市现金订单账期, incomeDays:{}", incomeDays);
            //到账时间等于0，立即到账
            if (incomeDays == 0) {
                orderIncome.setPayTime(DateUtils.getSeconds());
                this.updateEntitie(orderIncome);
                this.unOrderIncome(orderIncomeId);
            }
            //到账时间大于0，通过定时任务结算到账
            if (incomeDays > 0) {
                int incomeTime = incomeDays * 3600 * 24;
                orderIncome.setPayTime(DateUtils.getSeconds() + incomeTime);
                this.updateEntitie(orderIncome);
            }
        }
    }
    
    public Double getTotalMoney4JZ(){
    	return this.findOneForJdbc(getTotalMoney4JZ, Double.class);
    }
}