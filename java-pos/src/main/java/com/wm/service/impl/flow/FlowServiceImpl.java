package com.wm.service.impl.flow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.PayEnum;
import com.wm.aop.lock.distribute.LockTag;
import com.wm.aop.lock.distribute.PrefixJoinedLockKeySupplier;
import com.wm.dao.flow.FlowDao;
import com.wm.dao.user.UserDao;
import com.wm.entity.agent.AgentIncomeTimerEntity;
import com.wm.entity.deduct.DeductLogEntity;
import com.wm.entity.flow.FlowEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.transfers.TransfersEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.util.IDistributedLock;
import com.wm.util.MemcachedDistributedLock;


@Service("flowService")
@Transactional (propagation=Propagation.REQUIRED)
public class FlowServiceImpl extends CommonServiceImpl implements FlowServiceI {

    private static final Logger logger = LoggerFactory.getLogger(FlowServiceImpl.class);

    private final static int EXPIRE = 60;
    private final static int TRY_TIME = 30;

    private static final String getUserMoney = " select money from `user` u where id=? for update ";
    private static final String getAccountMoney = " select balance from `account` where user_id=? for update ";
    private static final String getuserMerchantMoney = "select money from `tom_merchant_member_info` tmmi where merchant_id=? and user_id=? ";
    private static final String updateUserMoney = " update `user` u set u.money = ? where u.id = ? and money=? ";
    private static final String updateAccountMoney = " update `account` a set a.balance = ? where a.user_id = ? and a.balance=? ";
    private static final String updateUserMoneyAndScore = " update `user` u set u.money = ?, u.score = ? where u.id = ? and money=? ";
    private static final String addFlow = "insert into flow (user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
            + " values(?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
    private static final String getPayType = "select o.pay_type from `order` o where o.id = ?";

    private static final String getAgentMoney = " SELECT income from agent_info ai where ai.user_id=?  for update";
    private static final String updateAgentMoney = " update agent_info a set a.income = ? where a.user_id = ? and a.income=? ";
    private static final String selAgentMoney = " SELECT income from agent_info ai where ai.id=?  for update";
    private static final String modAgentMoney = " update agent_info a set a.income = ? where a.id = ? ";

    private static final String findWithDrawFlow = "select * from flow where user_id=? and detail_id=? and action='withdraw'";
    private static final String findTotalMoney = "select ifnull(sum(money),0) as money from flow where user_id=? and type=? and id > ? and id <?";

    @Resource
    private UserDao userDao;
    @Resource
    private FlowDao flowDao;

    public void balancePayFlowCreate(Integer detailId, WUserEntity user, Double money) throws Exception {
        Integer userId = user.getId();
        logger.info("余额支付balancePayFlowCreate：userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        String lockName = "money_user" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }
                Double postMoney = preMoney > money ? (preMoney * 100 - money * 100) / 100 : 0;
                // Integer postScore = user.getScore() + order.getCredit().intValue();
                Integer postScore = user.getScore(); // + order.getCredit().intValue();
                // 不送积分了;
                Integer updateRows = executeSql(updateUserMoneyAndScore,
                        new Object[]{postMoney, postScore, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, preScore:{}, postScore:{}, ",
                        new Object[]{userId, preMoney, postMoney, money, user.getScore(), postScore});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}, preScore:{}, postScore:{},",
                            new Object[]{userId, preMoney, postMoney, money, user.getScore(), postScore});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow,
                        new Object[]{userId, detailId, "[余额支付]：￥" + money, money, "buy", "pay", preMoney, postMoney});
                logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("balancePayFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                            new Object[]{userId, detailId, money});
                    throw new RuntimeException(
                            "balancePayFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }

            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
    }

    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void rechargeFlowCreate(Integer userId, Double money, Integer detailId, Integer merchantId) throws Exception {
        logger.info("充值 rechargeFlowCreate: userId={}, money={}, detailId={}", new Object[]{userId, money, detailId});
//		String merchantIdSQL = "select merchant_id from `order` where id =?";
//		List<Map<String, Object>> list = this.findForJdbc(merchantIdSQL, detailId);
//		int merchantId = Integer.parseInt(list.get(0).get("merchant_id").toString());
        Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }
        Double postMoney = (preMoney * 100 + money * 100) / 100;
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", money:" + money);
        }
        String addFlowSQL = " insert into flow (merchant_id, user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
                + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
        Integer insertRows = executeSql(addFlowSQL, new Object[]{merchantId, userId, detailId, "[充值]：￥" + money,
                money, "recharge", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                new Object[]{userId, detailId, preMoney, postMoney, money});
        if (insertRows != 1) {
            logger.error("rechargeFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                    new Object[]{userId, detailId, money});
            throw new RuntimeException(
                    "rechargeFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
        }
    }

    public void merchantRechargeFlowCreate(int userId, double money, int detailId) throws Exception {
        logger.info("充值 merchantRechargeFlowCreate: userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        String merchantIdSQL = "select merchant_id from `order` where id =?";
        List<Map<String, Object>> list = this.findForJdbc(merchantIdSQL, detailId);
        int merchantId = Integer.parseInt(list.get(0).get("merchant_id").toString());
        //List<Map<String, Object>> list2 = this.findForJdbc(getuserMerchantMoney, merchantId, userId);
        String lockName = "money_user" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                SQLQuery sqlQuery = this.commonDao.getSession().createSQLQuery(getuserMerchantMoney);
                sqlQuery.setInteger(0, merchantId);
                sqlQuery.setInteger(1, userId);
                sqlQuery.addScalar("money", new IntegerType());
                Integer moneyd = (Integer) sqlQuery.uniqueResult();
                BigDecimal preMoney = new BigDecimal(0);
                BigDecimal postMoney = new BigDecimal(0);
                BigDecimal bdcm = new BigDecimal(100);
                logger.info("用户在该商家的会员余额, userId:{}, moneyd: {}", new Object[]{userId, moneyd});
                if (moneyd != null) {
                    preMoney = new BigDecimal(moneyd).divide(bdcm).setScale(4, BigDecimal.ROUND_HALF_UP);
                    postMoney = preMoney.add(new BigDecimal(money)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    Double rechargeMoney = postMoney.doubleValue() * 100;
                    String updateSQL = "update `tom_merchant_member_info` tmmi set tmmi.money = ? where tmmi.merchant_id = ? and tmmi.user_id=?";
                    Integer updateRows = this.executeSql(updateSQL, rechargeMoney, merchantId, userId);
                    logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                            new Object[]{userId, preMoney, postMoney, money});
                    if (updateRows != 1) {
                        logger.error("更新商家会员余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                                new Object[]{userId, preMoney, postMoney, money});
                        throw new RuntimeException("更新商家会员余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                                + ", money:" + money);
                    }
                } else {
                    postMoney = preMoney.add(new BigDecimal(money)).setScale(4, BigDecimal.ROUND_HALF_UP);
                    Double rechargeMoney = postMoney.doubleValue() * 100;
                    String insertSQL = "insert into `tom_merchant_member_info` (merchant_id, user_id, money, create_time) values(?, ?, ?, now())";
                    Integer insertRows = this.executeSql(insertSQL, merchantId, userId, rechargeMoney);
                    logger.info("insert into tom_merchant_member_info, userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    if (insertRows != 1) {
                        logger.error("rechargeFlowCreate 插入商家会员记录失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                                new Object[]{userId, preMoney, postMoney, money});
                        throw new RuntimeException(
                                "rechargeFlowCreate 插入商家会员记录失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                    }
                }
                String addFlowSQL = " insert into flow (merchant_id, user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
                        + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
                Integer insertRows = this.executeSql(addFlowSQL, new Object[]{merchantId, userId, detailId, "[商家会员充值]：￥" + money, money,
                        "merchantRecharge", "income", preMoney, postMoney});
                logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("rechargeFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                            new Object[]{userId, detailId, money});
                    throw new RuntimeException(
                            "rechargeFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
    }

    /**
     * 代理商充值
     */
    public void agentRechargeFlowCreate(int userId, double money, int detailId) throws Exception {
        logger.info("代理商充值 agentRechargeFlowCreate: userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        String agentIdSQL = "select agent_id from `order` where id =?";
        List<Map<String, Object>> list = this.findForJdbc(agentIdSQL, detailId);
        int agentId = Integer.parseInt(list.get(0).get("agent_id").toString());//代理商ID

        String agentUserIdSQL = "select user_id from `agent_info` where id =?";
        List<Map<String, Object>> agentlist = this.findForJdbc(agentUserIdSQL, agentId);
        int agentUserId = Integer.parseInt(agentlist.get(0).get("user_id").toString());//代理商USER_ID

        String merchantIdSQL = "select merchant_id from `order` where id =?";
        List<Map<String, Object>> alist = this.findForJdbc(merchantIdSQL, detailId);
        int merchantId = Integer.parseInt(alist.get(0).get("merchant_id").toString());//商家的ID
        logger.info("start--------userId:{},agentId:{},agentUserId:{},merchantId:{}", new Object[]{userId, agentId, agentUserId, merchantId});
        String lockName = "money_user" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Integer preMoney = findOneForJdbc(selAgentMoney, Integer.class, agentId);//代理商的金额
                logger.info("get user balance, agentId:{}, income: {}", new Object[]{agentId, preMoney});
                if (null == preMoney) {
                    logger.error("获取代理商信息失败, agentId:{} not exists !!!", agentId);
                    throw new RuntimeException("获取代理商信息失败, agentId:" + agentId + " not exists !!!");
                }
                Double postMoney = (preMoney + money * 100) / 100;
                Integer updateRows = executeSql(modAgentMoney, new Object[]{postMoney * 100, agentId});
                logger.info("update agent balance, agentId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{agentId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，agentId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{agentId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，agentId:" + agentId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }
                //新增用户充值记录
                String addUserFlowSQL = " insert into flow (merchant_id, user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
                        + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
                Integer insertUserRows = executeSql(addUserFlowSQL, new Object[]{merchantId, userId, detailId, "[代理商充值]-用户支出：￥" + money,
                        money, "agent_recharge", "pay", preMoney / 100, postMoney});
                logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney / 100, postMoney, money});
                if (insertUserRows != 1) {
                    logger.error("userRechargeFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                            new Object[]{userId, detailId, money});
                    throw new RuntimeException(
                            "userRechargeFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
                //新增代理商收入日志
                String addAgentFlowSQL = " insert into flow (merchant_id, user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time) "
                        + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()))";
                Integer insertAgentRows = executeSql(addAgentFlowSQL, new Object[]{merchantId, agentUserId, detailId, "[代理商充值]-代理商收入：￥" + money,
                        money, "agent_recharge", "income", preMoney / 100, postMoney});
                logger.info("add flow, agentUserId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney, postMoney / 100, money});
                if (insertAgentRows != 1) {
                    logger.error("agentRechargeFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                            new Object[]{userId, detailId, money});
                    throw new RuntimeException(
                            "agentRechargeFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }

    }

    @Override
    public void refundFlowCreate(int userId, double money, int detailId) throws Exception {
        logger.info("订单退款 refundFlowCreate: userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        OrderEntity order = this.get(OrderEntity.class, detailId);
        Integer merchantId = order.getMerchant().getId();
        Double preMoney = 0.00;
        String payType = order.getPayType();

        String lockName = "money_user" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                if (("merchantpay").equals(payType)) {
                    String sql = "SELECT money money FROM tom_merchant_member_info WHERE user_id=? AND merchant_id=?";
                    Map<String, Object> map = this.findOneForJdbc(sql, userId, merchantId);
                    preMoney = Double.parseDouble(map.get("money").toString());
                    payType = order.getPayType();
                } else {
                    preMoney = findOneForJdbc(getUserMoney, Double.class, userId) * 100;
                    payType = findOneForJdbc(getPayType, String.class, detailId);
                    logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                }
                logger.info("付款方式来源：" + payType);
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }
                Integer updateRows = 0;
                Integer insertRows = 0;
                Double postMoney = (preMoney + money * 100) / 100.0;
                preMoney = preMoney / 100.0;
                if (("merchantpay").equals(payType)) {
                    Double postmoney = postMoney * 100;
                    String sql = "UPDATE tom_merchant_member_info SET money=? WHERE user_id=? AND merchant_id=?";
                    updateRows = this.executeSql(sql, postmoney.intValue(), userId, merchantId);
                    logger.info("update tom_merchant_member_info money, userId:{}, preMoney:{}, postMoney:{}, money:{}, merchantId:{}",
                            new Object[]{userId, preMoney, postMoney, money, merchantId});


                    if (updateRows != 1) {
                        logger.error("更新商家会员余额失败，userId:{},preMoney:{},postMoney:{}, money:{}, merchantId:{}",
                                new Object[]{userId, preMoney, postMoney, money, merchantId});
                        throw new RuntimeException("更新商家会员余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                                + ", money:" + money);
                    }


                    String addFlow1 = " insert into flow (user_id, detail_id, detail, money, action, type, pre_money, post_money, create_time, merchant_id) "
                            + " values(?, ?, ?, ?, ?, ?, ?, ?, unix_timestamp(now()), ?)";

                    insertRows = executeSql(addFlow1, new Object[]{userId, detailId, "[商家会员订单退款]：￥" + money, money,
                            "merchantrefund", "income", preMoney, postMoney, merchantId});

                    logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                            new Object[]{userId, detailId, preMoney, postMoney, money});
                    if (insertRows != 1) {
                        logger.error("refundFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}", new Object[]{userId, detailId, money});
                        throw new RuntimeException("refundFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                    }

                } else {
                    updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                    logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                            new Object[]{userId, preMoney, postMoney, money});


                    if (updateRows != 1) {
                        logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                                new Object[]{userId, preMoney, postMoney, money});
                        throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                                + ", money:" + money);
                    }


                    insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[订单退款]：￥" + money, money,
                            "refund", "income", preMoney, postMoney});


                    logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                            new Object[]{userId, detailId, preMoney, postMoney, money});
                    if (insertRows != 1) {
                        logger.error("refundFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}", new Object[]{userId, detailId, money});
                        throw new RuntimeException("refundFlowCreate 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                    }
                }

            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
    }

    public void merchantOrderIncome(int userId, double addedMoney, int detailId) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("商家订单收入流水 merchantOrderIncome: userId={}, money={}, detailId={}", new Object[]{userId, addedMoney, detailId});
        FlowVo flow = updateUserMoney(userId, addedMoney);
        if (flow != null) {
            flow.setDetailId(detailId);
            saveFLow(flow);
        }
        long costTime = System.currentTimeMillis() - startTime;
        logger.info("orderId:{} merchantOrderIncome costtime:{} ms", detailId, costTime);
    }

    /**
     * @param userId
     * @param addedMoney
     */
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    private FlowVo updateUserMoney(int userId, double addedMoney) {
        Double oldMoney = findOneForJdbc(getUserMoney, Double.class, userId);
        if (null == oldMoney) {
            logger.error("user:{} not exists !!!", userId);
            throw new RuntimeException("user:" + userId + " not exists !!!");
        }
        BigDecimal preMoney = BigDecimal.valueOf(oldMoney).setScale(4, BigDecimal.ROUND_HALF_UP);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});

        BigDecimal money = BigDecimal.valueOf(addedMoney).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal postMoney = preMoney.add(money).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (money.compareTo(BigDecimal.ZERO) != 0) {
            Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
            logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                    new Object[]{userId, preMoney, postMoney, money});

            if (updateRows != 1) {
                logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                throw new RuntimeException("更新余额失败，userId:" + userId);
            }
            return new FlowVo(userId, FlowEntity.Type.INCOME, FlowEntity.Action.ORDERINCOME, preMoney, money, postMoney, "[商家订单收入]：￥" + money);
        } else {
            logger.warn("拒绝更新余额，userId:{},addedMoney:{}", userId, addedMoney);
        }
        return null;
    }

    /**
     * @param userId
     * @param subMoney
     */
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    private FlowVo subUserMoney(int userId, double subMoney) {
        Double oldMoney = findOneForJdbc(getUserMoney, Double.class, userId);
        if (null == oldMoney) {
            logger.error("user:{} not exists !!!", userId);
            throw new RuntimeException("user:" + userId + " not exists !!!");
        }
        BigDecimal preMoney = BigDecimal.valueOf(oldMoney).setScale(4, BigDecimal.ROUND_HALF_UP);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});

        BigDecimal money = BigDecimal.valueOf(subMoney).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal postMoney = preMoney.subtract(money).setScale(4, BigDecimal.ROUND_HALF_UP);
        if (postMoney.compareTo(BigDecimal.ZERO) >= 0) {
            Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
            logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                    new Object[]{userId, preMoney, postMoney, money});

            if (updateRows != 1) {
                logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                throw new RuntimeException("更新余额失败，userId:" + userId);
            }
            return new FlowVo(userId, FlowEntity.Type.PAY, FlowEntity.Action.POSBUYOUT, preMoney, money, postMoney, "[POS 购买]：￥" + money);
        } else {
            logger.warn("拒绝更新余额，userId:{},addedMoney:{}", userId, subMoney);
        }
        return null;
    }

    public static void main(String[] s) {
        System.out.println(Integer.valueOf(0).compareTo(Integer.valueOf(1)));
    }

    /**
     * 保存流水记录
     *
     * @param flow
     */
    private void saveFLow(FlowVo flow) {
        if (flow == null) {
            logger.warn("flow is empty !!!");
            return;
        }
        if (flow.getMoney().compareTo(BigDecimal.ZERO) != 0) {
            Integer insertRows = executeSql(addFlow, new Object[]{flow.getUserId(), flow.getDetailId(), flow.getDetail(), flow.getMoney(),
                    flow.getAction(), flow.getType(), flow.getPreMoney(), flow.getPostMoney()});
            logger.info("save flow:{}", flow);
            if (insertRows != 1) {
                throw new RuntimeException("save flow failed, flow:" + flow);
            }
        } else {
            logger.warn("余额未更新,不记录流水,userId:{},detailId:{}", flow.getUserId(), flow.getDetailId());
        }
    }

    public Integer merchantWithdraw(Integer userId, Double money, Integer withdrawId) throws Exception {
//		Integer userId = user.getId();
        logger.info("商家提现 merchantWithdraw: userId={}, money={}", new Object[]{userId, money});
        String lockName = "money_user_" + userId;

        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }

                Double postMoney = (preMoney * 100 - money * 100) / 100;
                if (postMoney < 0) {
                    logger.error("余额不足，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("余额不足");
                }
                Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

//				Integer insertRows = executeSql(addFlow,
//						new Object[] { userId, withdrawId, "["+userId+"提现]：￥" + money, money, "withdraw", "pay", preMoney, postMoney });
//				logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
//						new Object[] { userId, preMoney, postMoney, money });
//				if (insertRows != 1) {
//					logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[] { userId, money });
//					throw new RuntimeException("merchantWithdraw 插入流水失败， userId:" + userId + ",money:" + money);
//				}

                Long flowId = insertBySql(addFlow, new Object[]{userId, withdrawId, "[" + userId + "提现]：￥" + money, money, "withdraw", "pay", preMoney, postMoney});
                logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                if (flowId == null) {
                    logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
                    throw new RuntimeException("merchantWithdraw 插入流水失败， userId:" + userId + ",money:" + money);
                }
                return flowId.intValue();
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
    }

    @Override
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void agentWithdraw(Integer userId, Double money) throws Exception {
        logger.info("代理商提现 agentWithdraw: userId={}, money={}", new Object[]{userId, money * 100});

        Double preMoney = findOneForJdbc(getAgentMoney, Double.class, userId);//单位：分
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }

        Double postMoney = preMoney - money * 100;//单位：分
        if (postMoney < 0) {
            throw new RuntimeException("代理商余额不足，无法提现");
        }
        Integer updateRows = executeSql(updateAgentMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money * 100});

        if (updateRows != 1) {
            logger.error("代理商更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money * 100});
            throw new RuntimeException("代理商更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", money:" + money * 100);
        }

        Integer insertRows = executeSql(addFlow,
                new Object[]{userId, null, "代理商[" + userId + "提现]：￥" + money, money, "withdraw", "pay", preMoney * 0.01, postMoney * 0.01});
        logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, preMoney, postMoney, money * 100});
        if (insertRows != 1) {
            logger.error("agentWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money * 100});
            throw new RuntimeException("agentWithdraw 插入流水失败， userId:" + userId + ",money:" + money * 100);
        }
    }

    public void merchantCrowdsourcing(WUserEntity user, Double money, Integer detailId) throws Exception {
        Integer userId = user.getId();
        logger.info("众包订单结算运费 merchantCrowdsourcing: userId={}, money={}", new Object[]{userId, money});
        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }

                Double postMoney = (preMoney * 100 - money * 100) / 100;
                Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[众包订单结算运费]：￥" + money, money,
                        "crowdsourcing", "pay", preMoney, postMoney});
                logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("merchantCrowdsourcing 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
                    throw new RuntimeException(
                            "merchantCrowdsourcing 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }

    }

    public void crowdsourcingRefund(WUserEntity user, Integer detailId) throws Exception {
        Integer userId = user.getId();
        Double money = this.findUniqueByProperty(FlowEntity.class, "detailId", detailId).getMoney();
        logger.info("众包订单退款 crowdsourcingRefund: userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对商家【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }
                Double postMoney = (preMoney * 100 + money * 100) / 100;
                Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[众包订单退款]：￥" + money, money,
                        "crowdRefund", "income", preMoney, postMoney});
                logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("crowdsourcingRefund 插入流水失败， userId:{}, detailId:{}, money:{}",
                            new Object[]{userId, detailId, money});
                    throw new RuntimeException(
                            "crowdsourcingRefund 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }

    }

    public void deductFlowCreate(DeductLogEntity deductLog) throws Exception {
        Integer userId = deductLog.getCourierId();
        Double money = deductLog.getTotalDeduct();
        logger.info("快递员提成 deductFlowCreate: userId={}, money={}", new Object[]{userId, money});
        if (money <= 0) {
            logger.warn("快递员{}提成金额money为: {}, 不处理", new Object[]{userId, money});
            return;
        }
        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }
                if (money > 0) {
                    Double postMoney = (preMoney * 100 + money * 100) / 100;
                    Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                    logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                            new Object[]{userId, preMoney, postMoney, money});

                    if (updateRows != 1) {
                        logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                                new Object[]{userId, preMoney, postMoney, money});
                        throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                                + ", money:" + money);
                    }

                    Integer insertRows = executeSql(addFlow, new Object[]{userId, null, "[快递员提成收入]：￥" + money, money,
                            "deduct", "income", preMoney, postMoney});
                    logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    if (insertRows != 1) {
                        logger.error("deductFlowCreate 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
                        throw new RuntimeException("deductFlowCreate 插入流水失败， userId:" + userId + ",money:" + money);
                    }
                } else {
                    logger.warn("快递员【" + userId + "】提成金额为0！");
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }
    }

    public void courierPayOrderFlowCreate(TransfersEntity transfers) throws Exception {
        Double money = transfers.getMoney();
        if (null == transfers.getPayUser()) {
            logger.error("获取转出用户信息失败, transfer id:{} not exists !!!", transfers.getId());
            throw new RuntimeException("获取转出用户信息失败, transfer id:" + transfers.getId() + " not exists !!!");
        }
        Integer userId = transfers.getPayUser().getId();
        logger.info("快递员代付 transferFlowCreate: userId={}, money={}", new Object[]{userId, money});

        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }

                Double postMoney = (preMoney * 100 - money * 100) / 100;
                Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, transfers.getId(), "[快递员代付]：￥" + money, money,
                        "transfer", "pay", preMoney, postMoney});
                logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("courierPayOrderFlowCreate 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
                    throw new RuntimeException("courierPayOrderFlowCreate 插入流水失败， userId:" + userId + ",money:" + money);
                }
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
            }
        }

    }

    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void courierPayOrderReturnFlowCreate(Integer userId, Double money, Integer detailId) throws Exception {
        logger.info("快递员代购对冲 courierPayOrderReturnFlowCreate: userId={}, money={}", new Object[]{userId, money});

        Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }

        Double postMoney = preMoney + money;
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", money:" + money);
        }

        Integer insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[快递员代购对冲]：￥" + money, money,
                "purchaseReturn", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, preMoney, postMoney, money});
        if (insertRows != 1) {
            logger.error("courierPayOrderReturnFlowCreate 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
            throw new RuntimeException("courierPayOrderReturnFlowCreate 插入流水失败， userId:" + userId + ",money:" + money);
        }
    }

    /**
     * 返点奖励补贴发放流水
     *
     * @param userId
     * @param money
     * @param action 类型：merchantRebate商家返点收入，courierRebate快递员返点
     * @throws Exception
     */
    public void userRebateIncome(int userId, double money, String action) {
        String msg = "商家";
        if ("courierRebate".equals(action)) {
            msg = "快递员";
        }
        logger.info(msg + "返点奖励补贴发放流水 userRebateIncome: userId={}, money={}", new Object[]{userId, money});
        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
                logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
                if (null == preMoney) {
                    logger.error("获取用户信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
                }

                Double postMoney = (preMoney * 100 + money * 100) / 100;
                Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                            new Object[]{userId, preMoney, postMoney, money});
                    throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, null, "[" + msg + "返点奖励补贴发放]：￥" + money, money,
                        action, "income", preMoney, postMoney});
                logger.info("add flow, userId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, preMoney, postMoney, money});
                if (insertRows != 1) {
                    logger.error("userRebateIncome 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
                    throw new RuntimeException("userRebateIncome 插入流水失败， userId:" + userId + ",money:" + money);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("", e);
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
                logger.info("{}释放锁", Thread.currentThread().getName());
            }
        }
    }

    /**
     * 代理商商家返点奖励补贴发放流水
     *
     * @param userId
     * @param money
     * @throws Exception
     */
    public void agentMerchantRebateIncome(int userId, double money) {
        logger.info("代理商返点奖励给商家流水 agentMerchantRebateIncome: userId={}, money={}", new Object[]{userId, money});
        String lockName = "money_user_" + userId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                logger.info("{}获得锁", Thread.currentThread().getName());
                Integer pmoney = findOneForJdbc(getAgentMoney, Integer.class, userId);//pmoney以分为单位
                if (null == pmoney) {
                    logger.error("获取代理商信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取代理商信息失败, user:" + userId + " not exists !!!");
                }

                Double preMoney = pmoney * 0.01;//preMoney以元为单位
                logger.info("get agent_info income, userId:{}, income: {}", new Object[]{userId, preMoney});
                Double postMoney = preMoney + money;
                if (postMoney < 0) {
                    logger.error("余额不足，无法扣除，userId:{},preMoney:{},postMoney:{}, money:{}", userId, preMoney, postMoney, money);
                    throw new RuntimeException("余额不足，无法扣除，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney + ", money:" + money);
                }
                Double pm = postMoney * 100;
                Integer post = pm.intValue();
                Integer updateRows = executeSql(updateAgentMoney, new Object[]{post, userId, pmoney});
                logger.info("update agent_info income, userId:{}, preMoney:{}, postMoney:{}, money:{}, ", userId, preMoney, postMoney, money);

                if (updateRows != 1) {
                    logger.error("更新代理商收入余额失败，userId:{},preMoney:{},postMoney:{}, money:{}", userId, preMoney, postMoney, money);
                    throw new RuntimeException("更新代理商收入余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney + ", money:" + money);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, null, "[代理商收入]：￥" + money, money,
                        "merchantRebate", "income", preMoney, postMoney});
                logger.info("add flow, userId:{}, preMoney:{}, postMoney:{},money:{}", userId, preMoney, postMoney, money);
                if (insertRows != 1) {
                    logger.error("代理商余额返点商家 插入流水失败， userId:{}, money:{}", userId, money);
                    throw new RuntimeException("代理商余额返点商家 插入流水失败， userId:" + userId + ",money:" + money);
                }
            }
        } catch (Exception e) {
            logger.info("代理商余额返点商家失败：" + e.getMessage());
            throw new RuntimeException("", e);
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
                logger.info("{}释放锁", Thread.currentThread().getName());
            }
        }
    }

    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void courierRechargePromotion(Integer orderId, Integer userId, Double money) throws Exception {
        logger.info("快递员充值推广奖励：userId={}, money={}, detailId={}", new Object[]{userId, money, orderId});
        Double preMoney = findOneForJdbc(getUserMoney, Double.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }
        Double postMoney = preMoney + money;
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{} ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("充值推广奖励，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", money:" + money);
        }

        Integer insertRows = executeSql(addFlow,
                new Object[]{userId, orderId, "快递员扫码支付奖励" + money, money, "recharge_deduct", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                new Object[]{userId, orderId, preMoney, postMoney, money});

        if (insertRows != 1) {
            logger.error("courierRechargePromotion 插入流水失败， userId:{}, detailId:{}, money:{}",
                    new Object[]{userId, orderId, money});
            throw new RuntimeException("courierRechargePromotion 插入流水失败， userId:" + userId + ",detailId:" + orderId + ",money:" + money);
        }
    }

    @Override
    public void agentIncomeFlowCreate(AgentIncomeTimerEntity agentIncome) throws Exception {
        //结算类型   1 分销返点   2 直营返点   3 商家收入
        String type = "直营返点";
        String action = "agentDeduction";
        if (1 == agentIncome.getType()) {
            type = "分销返点";
            action = "agentRebate";
        } else if (3 == agentIncome.getType()) {
            type = "商家收入";
            action = "income";
        }

        Integer userId = agentIncome.getUserId();
        WUserEntity user = this.get(WUserEntity.class, userId);
        if (user == null) {
            logger.error("查询不到代理商userId:[" + userId + "]的用户信息");
            return;
        }

        Double money = agentIncome.getIncome();//以元为单位
        Integer detailId = agentIncome.getOrderId();

        logger.info("代理商" + type + "收入流水 agentIncome: userId={}, money={}, detailId={}",
                new Object[]{userId, money, detailId});
        IDistributedLock lock = new MemcachedDistributedLock();
        String lockName = "money_user" + userId;
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
            if (uuid == null) {
                logger.error("其它线程也在对用户【{}】余额进行更新， 获取不到锁", userId);
                throw new RuntimeException("其它线程也在对用户【" + userId + "】余额进行更新， 获取不到锁");
            } else {
                logger.info("{}获得锁", Thread.currentThread().getName());
                Double preMoney = findOneForJdbc(getAgentMoney, Double.class, userId);//收入 1元=100，单位：分
                if (null == preMoney) {
                    logger.error("获取代理商信息失败, user:{} not exists !!!", userId);
                    throw new RuntimeException("获取代理商信息失败, user:" + userId + " not exists !!!");
                }

                logger.info("get agent_info income, userId:{}, income: {}", new Object[]{userId, preMoney});
                Double postMoney = preMoney + 100 * money;
                if (postMoney < 0) {
                    logger.error("余额不足，无法扣除，userId:{},preMoney:{},postMoney:{}, money:{}", userId, preMoney, postMoney, 100 * money);
                    throw new RuntimeException("余额不足，无法扣除，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney + ", money:" + money);
                }
                Integer updateRows = executeSql(updateAgentMoney, new Object[]{postMoney, userId, preMoney});
                logger.info("update agent_info income, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                        new Object[]{userId, preMoney, postMoney, money});

                if (updateRows != 1) {
                    logger.error("更新代理商收入余额失败，userId:{},preMoney:{},postMoney:{}, money:{}, detailId:{}",
                            userId, preMoney, postMoney, 100 * money, detailId);
                    throw new RuntimeException("更新代理商收入余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                            + ", money:" + 100 * money + ", detailId:" + detailId);
                }

                Integer insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[代理商" + type + "收入]：￥" + money, money,
                        action, "income", preMoney / 100, postMoney / 100});
                logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},money:{}",
                        new Object[]{userId, detailId, preMoney / 100, postMoney / 100, money});
                if (insertRows != 1) {
                    logger.error(action + " 插入流水失败， userId:{}, detailId:{}, money:{}", userId, detailId, money);
                    throw new RuntimeException(
                            action + " 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",money:" + money);
                }
            }
        } catch (Exception e) {
            logger.error("agentIncomeId:[" + agentIncome.getId() + "]结算失败");
            throw new RuntimeException("", e);
        } finally {
            if (uuid != null) {
                lock.releaseLock(lockName, uuid);
                logger.info("{}释放锁", Thread.currentThread().getName());
            }
        }


    }

    @Override
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void dineInOrderMerchantRefund(Integer orderId, Integer userId, BigDecimal reducedMoney) throws NotEnoughBalanceException {
        logger.info("堂食订单退单-商家余额扣除：orderId={}", orderId);

//		OrderIncomeEntity orderIncome = orderIncomeService.getOrderIncomeByOrderIdAndType(order.getId(), 0);
//		if (orderIncome == null) {
//			throw new RuntimeException("can't find orderIncomeEntity.");
//		}

//		BigDecimal reducedMoney = BigDecimal.valueOf(orderIncome.getMoney());
        logger.info("get orderIncome money, orderId:{}, money:{}", orderId, reducedMoney);
        BigDecimal preMoney = userDao.getMoneyForUpdate(userId);
        logger.info("get user balance, userId:{}, balance:{}", new Object[]{userId, preMoney});

        if (preMoney.compareTo(reducedMoney) < 0) {
            throw new NotEnoughBalanceException("merchant's money is not enough.");
        }
        BigDecimal postMoney = preMoney.subtract(reducedMoney);

        int updateRows = userDao.updateMoney(postMoney, userId, preMoney);
        logger.info("update user balance, userId:{}, preMoney:{}, reducedMoney:{}, postMoney:{} ",
                new Object[]{userId, preMoney, reducedMoney, postMoney});
        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{}, preMoney:{}, reducedMoney:{}, postMoney:{}",
                    new Object[]{userId, preMoney, reducedMoney, postMoney});
            throw new RuntimeException("update user's money fail.");
        }

        Integer insertRows = flowDao.add(userId, orderId, "[门店订单退款]：￥" + reducedMoney, reducedMoney, "refund",
                "pay", preMoney, postMoney);
        logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, orderId, preMoney, postMoney, reducedMoney});
        if (insertRows != 1) {
            logger.error("refundFlowCreate 插入流水失败， userId:{}, detailId:{}, money:{}",
                    new Object[]{userId, orderId, reducedMoney});
            throw new RuntimeException("add flow fail.");
        }
    }

    /**
     * 订单配送费结算进代理商收入流水
     *
     * @throws Exception
     */
    @Override
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void agentMerchantDeliveryIncome(int userId, double deliveryMoney, int detailId) throws Exception {
        logger.info("订单配送费结算进代理商收入流水 agentMerchantDeliveryIncome: userId={}, money={}, detailId={}",
                new Object[]{userId, deliveryMoney, detailId});

        Integer pmoney = findOneForJdbc(getAgentMoney, Integer.class, userId);//pmoney以分为单位
        if (null == pmoney) {
            logger.error("获取代理商信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取代理商信息失败, user:" + userId + " not exists !!!");
        }

        Double preMoney = pmoney * 0.01;//preMoney以元为单位
        logger.info("get agent_info income, userId:{}, income: {}", new Object[]{userId, preMoney});
        Double postMoney = preMoney + deliveryMoney;
        if (postMoney < 0) {
            logger.error("余额不足，无法扣除，userId:{},preMoney:{},postMoney:{}, deliveryMoney:{}", userId, preMoney, postMoney, deliveryMoney);
            throw new RuntimeException("余额不足，无法扣除，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney + ", deliveryMoney:" + deliveryMoney);
        }
        Double pm = postMoney * 100;
        Integer post = pm.intValue();
        Integer updateRows = executeSql(updateAgentMoney, new Object[]{post, userId, pmoney});
        logger.info("update agent_info income, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, deliveryMoney});

        if (updateRows != 1) {
            logger.error("更新代理商收入余额失败，userId:{},preMoney:{},postMoney:{}, money:{}, detailId:{}",
                    userId, preMoney, postMoney, deliveryMoney, detailId);
            throw new RuntimeException("更新代理商收入余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", deliveryMoney:" + deliveryMoney + ", detailId:" + detailId);
        }

        Integer insertRows = executeSql(addFlow, new Object[]{userId, detailId, "[订单配送费结算进代理商收入]：￥" + deliveryMoney, deliveryMoney,
                "orderIncome", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, detailId:{}, preMoney:{}, postMoney:{},deliveryMoney:{}",
                new Object[]{userId, detailId, preMoney, postMoney, deliveryMoney});
        if (insertRows != 1) {
            logger.error("merchantOrderIncome 插入流水失败， userId:{}, detailId:{}, deliveryMoney:{}",
                    new Object[]{userId, detailId, deliveryMoney});
            throw new RuntimeException(
                    "merchantOrderIncome 插入流水失败， userId:" + userId + ",detailId:" + detailId + ",deliveryMoney:" + deliveryMoney);
        }
    }

    @Override
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void merchantSupplyOrderPay(Long orderId, Integer userId, BigDecimal money, String type, String payType, String detail) throws RuntimeException {
        logger.info("商家供应链商品购买订单支付流水 merchantSupplyOrderPay: detailId={}, userId={}, money={}, String detail",
                new Object[]{orderId, userId, money, detail});
        if (StringUtil.isEmpty(detail)) {
            detail = "商家供应链商品购买订单支付";
        }
        
        String action = "supplyOrderPay";
        if(PayEnum.merchant_account_banlance_pay.getEn().equals(payType)){
        	action = "supplyOrderPayMerchantAccount";
        }
        if ("1".equals(type)) {
        	action = action + "_1";
        }
        
        Criteria criteria = flowDao.getSession().createCriteria(FlowEntity.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("detailId", orderId.intValue()));
        criteria.add(Restrictions.eq("action", action));
        List<?> list = criteria.list();
        if (CollectionUtils.isNotEmpty(list)) {
            // 目前只在供应链商家进行采购的时候，才可能出现这个商家的用户对应的action是buy
            logger.info("已经存在流水记录");
            throw new RuntimeException("已经存在流水记录");
        }

        if(PayEnum.merchant_account_banlance_pay.getEn().equals(payType)){
        	BigDecimal preMoney = findOneForJdbc(getAccountMoney, BigDecimal.class, userId);
        	logger.info("get account balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        	if (null == preMoney) {
        		logger.error("获取账户信息失败, user:{} not exists !!!", userId);
        		throw new RuntimeException("获取账户信息失败, user:" + userId + " not exists !!!");
        	}
        	
        	BigDecimal postMoney = preMoney.subtract(money);  //减去
        	if (postMoney.doubleValue() < 0) {
        		throw new RuntimeException("余额不足");
        	}
        	Integer updateRows = executeSql(updateAccountMoney, new Object[]{postMoney, userId, preMoney});
        	logger.info("update account balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
        			new Object[]{userId, preMoney, postMoney, money});
        	
        	if (updateRows != 1) {
        		logger.error("更新账户余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
        				new Object[]{userId, preMoney, postMoney, money});
        		throw new RuntimeException("更新账户余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
        				+ ", money:" + money);
        	}
        	
        	Integer insertRows = executeSql(addFlow,
        			new Object[]{userId, orderId, "[" + detail + "]：￥" + money, money, action, "pay", preMoney, postMoney});
        	logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
        			new Object[]{userId, preMoney, postMoney, money});
        	if (insertRows != 1) {
        		logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
        		throw new RuntimeException("merchantWithdraw 插入流水失败， userId:" + userId + ",money:" + money);
        	}   
        } else {
        	BigDecimal preMoney = findOneForJdbc(getUserMoney, BigDecimal.class, userId);
        	logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        	if (null == preMoney) {
        		logger.error("获取用户信息失败, user:{} not exists !!!", userId);
        		throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        	}
        	
        	BigDecimal postMoney = preMoney.subtract(money);  //减去
        	if (postMoney.doubleValue() < 0) {
        		throw new RuntimeException("余额不足");
        	}
        	Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        	logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
        			new Object[]{userId, preMoney, postMoney, money});
        	
        	if (updateRows != 1) {
        		logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
        				new Object[]{userId, preMoney, postMoney, money});
        		throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
        				+ ", money:" + money);
        	}
        	
        	Integer insertRows = executeSql(addFlow,
        			new Object[]{userId, orderId, "[" + detail + "]：￥" + money, money, action, "pay", preMoney, postMoney});
        	logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
        			new Object[]{userId, preMoney, postMoney, money});
        	if (insertRows != 1) {
        		logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
        		throw new RuntimeException("merchantWithdraw 插入流水失败， userId:" + userId + ",money:" + money);
        	}   
        }
        
    }

    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void merchantSupplyOrderIncome(Long orderId, Integer userId, BigDecimal money) {
        logger.info("商家供应链商品订单收入流水 merchantSupplyOrderIncome: detailId={}, userId={}, money={}", new Object[]{orderId, userId, money});

        Criteria criteria = flowDao.getSession().createCriteria(FlowEntity.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("detailId", orderId.intValue()));
        criteria.add(Restrictions.eq("action", "supplyOrderIncome"));
        List<?> list = criteria.list();
        if (CollectionUtils.isNotEmpty(list)) {
            // 目前只在供应链商家进行采购的时候，才可能出现这个商家的用户对应的action是supplyOrderIncome
            throw new RuntimeException("已经存在流水记录");
        }

        BigDecimal preMoney = findOneForJdbc(getUserMoney, BigDecimal.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }

        BigDecimal postMoney = preMoney.add(money);  //加上
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败，userId:" + userId + ",preMoney:" + preMoney + ",postMoney:" + postMoney
                    + ", money:" + money);
        }

        Integer insertRows = executeSql(addFlow,
                new Object[]{userId, orderId, "[商家供应链商品订单收入]：￥" + money, money, "supplyOrderIncome", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, preMoney, postMoney, money});
        if (insertRows != 1) {
            logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
            throw new RuntimeException("merchantWithdraw 插入流水失败， userId:" + userId + ",money:" + money);
        }
    }

    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void mainStoreAccountIn(Integer userId, BigDecimal money) {
        logger.info("商家连锁账户（mainStoreAccountIn），转入主店余额流水，主店用户ID：userId={}, money={}", new Object[]{userId, money});

        BigDecimal preMoney = findOneForJdbc(getUserMoney, BigDecimal.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }

        BigDecimal postMoney = preMoney.add(money);  //加上
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败");
        }

        Integer insertRows = executeSql(addFlow,
                new Object[]{userId, null, "[商家连锁账户,转入主店余额]：￥" + money, money, "multiIncome", "income", preMoney, postMoney});
        logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, preMoney, postMoney, money});
        if (insertRows != 1) {
            logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
            throw new RuntimeException("[商家连锁账户,转入主店余额]插入流水失败");
        }
    }

    @Override
    @LockTag(value = "money_user,userId", keySupplier = PrefixJoinedLockKeySupplier.class)
    public void branchStoreAccountOut(Integer userId, BigDecimal money) {
        logger.info("商家连锁账户（branchStoreAccountOut），分店余额转出流水，分店用户ID：userId={}, money={}", new Object[]{userId, money});

        BigDecimal preMoney = findOneForJdbc(getUserMoney, BigDecimal.class, userId);
        logger.info("get user balance, userId:{}, balance: {}", new Object[]{userId, preMoney});
        if (null == preMoney) {
            logger.error("获取用户信息失败, user:{} not exists !!!", userId);
            throw new RuntimeException("获取用户信息失败, user:" + userId + " not exists !!!");
        }

        BigDecimal postMoney = preMoney.subtract(money);  //减去
        if (postMoney.doubleValue() < 0) {
        	logger.error("分店余额不足，操作失败");
            throw new RuntimeException("分店余额不足，操作失败");
        }
        Integer updateRows = executeSql(updateUserMoney, new Object[]{postMoney, userId, preMoney});
        logger.info("update user balance, userId:{}, preMoney:{}, postMoney:{}, money:{}, ",
                new Object[]{userId, preMoney, postMoney, money});

        if (updateRows != 1) {
            logger.error("更新余额失败，userId:{},preMoney:{},postMoney:{}, money:{}",
                    new Object[]{userId, preMoney, postMoney, money});
            throw new RuntimeException("更新余额失败");
        }

        Integer insertRows = executeSql(addFlow,
                new Object[]{userId, null, "[商家连锁账户,分店余额转出]：￥" + money, money, "multiIncome", "pay", preMoney, postMoney});
        logger.info("add flow, userId:{}, preMoney:{}, postMoney:{}, money:{}",
                new Object[]{userId, preMoney, postMoney, money});
        if (insertRows != 1) {
            logger.error("merchantWithdraw 插入流水失败， userId:{}, money:{}", new Object[]{userId, money});
            throw new RuntimeException("[商家连锁账户,分店余额转出]插入流水失败");
        }
    }


    @Override
    public FlowVo payPOSBuyout(int userId, double subMoney, int detailId, String payIndex) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("POS 购买支付订单 payPOSBuyout: userId={}, money={}, detailId={}", new Object[]{userId, subMoney, detailId});
        FlowVo flow = subUserMoney(userId, subMoney);
        if (flow != null) {
            flow.setDetailId(detailId);
            flow.setDetail(payIndex);
            saveFLow(flow);
        }
        long costTime = System.currentTimeMillis() - startTime;
        logger.info("orderId:{} merchantOrderIncome costtime:{} ms", detailId, costTime);
        return flow;
    }

    @Override
    public Map<String, Object> findWithDrawFlow(Integer userId, Integer withDrawId) {
        return this.findOneForJdbc(findWithDrawFlow, userId, withDrawId);
    }

    @Override
    public Double findTotalMoney(Integer userId, String flowType, Integer withDrawOldId, Integer withDrawNewId) {
        return findOneForJdbc(findTotalMoney, Double.class, userId, flowType, withDrawOldId, withDrawNewId);
    }

}
