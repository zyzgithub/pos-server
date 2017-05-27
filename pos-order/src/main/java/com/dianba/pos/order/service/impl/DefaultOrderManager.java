package com.dianba.pos.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.menu.mapper.PromotionMenuMapper;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.po.PromotionMenu;
import com.dianba.pos.menu.repository.MenuJpaRepository;
import com.dianba.pos.merchant.po.Merchant;
import com.dianba.pos.merchant.repository.MerchantJpaRepository;
import com.dianba.pos.order.config.OrderStateConstant;
import com.dianba.pos.order.exception.BusinessException;
import com.dianba.pos.order.mapper.OrderMapper;
import com.dianba.pos.order.po.CashierOrder;
import com.dianba.pos.order.po.Order;
import com.dianba.pos.order.po.OrderMenu;
import com.dianba.pos.order.po.OrderState;
import com.dianba.pos.order.repository.CashierOrderJpaRepository;
import com.dianba.pos.order.repository.OrderJpaRepository;
import com.dianba.pos.order.repository.OrderMenuJpaRepository;
import com.dianba.pos.order.repository.OrderStateJpaRepository;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.order.support.OrderRemoteService;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DefaultOrderManager extends OrderRemoteService implements OrderManager {

    private static Logger logger = LogManager.getLogger(DefaultOrderManager.class);

    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MerchantJpaRepository merchantJpaRepository;
    @Autowired
    private MenuJpaRepository menuJpaRepository;
    @Autowired
    private PromotionMenuMapper promotionMenuMapper;
    @Autowired
    private OrderStateJpaRepository orderStateJpaRepository;
    @Autowired
    private OrderMenuJpaRepository orderMenuJpaRepository;
    @Autowired
    private CashierOrderJpaRepository cashierOrderJpaRepository;

    @Transactional
    public Order createOrderFromSuperMarket(Integer merchantId, Integer cashierId, String mobile
            , String params, Integer createTime, String uuid) throws BusinessException {
        // 离线订单 校验 "[offline_order] 离线订单:"/ "[market_order]  超市订单:"
        if (StringUtils.isNotBlank(uuid) && uuid.startsWith(OFFLINE_ORDER_PREFIX)) {
            List<String> remarks = new ArrayList<>();
            remarks.add(uuid.replace(OFFLINE_ORDER_PREFIX, SUPERMARKET_ORDER_PREFIX));
            remarks.add(uuid);
            Integer count = orderMapper.getRemarkCount(remarks);
            if (count != null && count > 0) {
                throw new BusinessException(12, "订单已重复");
            }
        }
        Order oDto = new Order();
        List<OrderMenu> sList = new ArrayList<>();
        Integer orderId = null;
        Merchant merchant = merchantJpaRepository.findOne((long) merchantId);

        if (merchant != null) {
            int menuId = 0;//商品id
            int num = 0;//单间商品数量
            int totalNum = 0;//所有商品总数量
            double totalOrigin = 0.0;//订单总价
            double totalPrice = 0.0;//订单优惠后总价
            double totalDiscount = 0.0;//订单总优惠
            int menuPromotionId = 0;
            String salesPromotion = "N"; //是否促销
            double cost = 0;
            double dough = 0;

            //商品信息预先检查
            Map<Integer, Map<String, Object>> errMsgs = new HashMap<Integer, Map<String, Object>>();
            if (!menuPreCheck(params, errMsgs)) {
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = errMsgs.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Map<String, Object>> entry = iterator.next();
                    Integer menuIdTemp = entry.getKey();
                    Map<String, Object> menuErrDescription = entry.getValue();

                    OrderMenu sMenuVo = new OrderMenu();
                    sMenuVo.setMenuId(menuIdTemp);
                    sMenuVo.setErrMsg(menuErrDescription.get("errMsg").toString());
                    sMenuVo.setName(menuErrDescription.get("menuName").toString());
                    sMenuVo.setPrice(Double.parseDouble(menuErrDescription.get("price").toString()));
                    sList.add(sMenuVo);
                }
                oDto.setMenuList(sList);
                return oDto;
            }


            List<Map<String, Object>> paramList = JsonHelper.toList(params);
            if (params != null && paramList.size() > 0) {
                for (Map<String, Object> m : paramList) {
                    double price = 0.0;//商品单价
                    double promotionPrice = 0.0;//商品促销单价

                    salesPromotion = "N";
                    menuId = Integer.valueOf(m.get("menuId").toString());
                    num = Integer.valueOf(m.get("num").toString());
                    Menu menuInfo = menuJpaRepository.findOne(menuId);
                    price = menuInfo.getPrice();

                    if (m.get("menuPromotionId") != null && !"".equals(m.get("menuPromotionId").toString())) {
                        menuPromotionId = Integer.valueOf(m.get("menuPromotionId").toString());
                    }

                    if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                        salesPromotion = m.get("salesPromotion").toString();
                    }

                    if (m.get("dough") != null && !"".equals(m.get("dough").toString())) {
                        dough = Double.valueOf(m.get("dough").toString());
                    }

                    cost += dough * num;

                    List<PromotionMenu> menuPromotionList
                            = promotionMenuMapper.findByMenuIdAndMerchantId((long) menuId, (long) merchantId);
                    if (CollectionUtils.isNotEmpty(menuPromotionList)) {
                        PromotionMenu promotionMenu = menuPromotionList.get(0);
                        if (promotionMenu != null) {
                            if (promotionMenu.getPromotionQuantity() > num) {
                                promotionPrice = promotionMenu.getPromotionPrice().doubleValue();
                                salesPromotion = "Y";
                            }
                        }
                    }

                    // 计算总金额
                    totalOrigin += num * price;
                    if (promotionPrice != 0.0) {
                        totalPrice += num * promotionPrice;
                        totalDiscount += num * (price - promotionPrice);
                    } else {
                        totalPrice += num * price;
                    }
                    //计算商品总量
                    totalNum += num;
                }
                BigDecimal bigdecimal = new BigDecimal(totalPrice);
                totalPrice = bigdecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal bigdecimalCost = new BigDecimal(cost);
                cost = bigdecimalCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                BigDecimal bigdecimalTotalDiscount = new BigDecimal(totalDiscount);
                totalDiscount = bigdecimalTotalDiscount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                Order order = new Order();
                order.setUserId(0);
                order.setCityId(merchant.getCityId());
                order.setMerchantId(merchantId);
                order.setState("unpay");
                order.setOnlineMoney(0.0);
                order.setOrigin(totalPrice);
                order.setCreateTime((createTime == null ? System.currentTimeMillis() / 1000 : createTime) + "");
                order.setOrderType("supermarket");
                order.setTitle("超市订单");
                order.setSaleType("2");
                order.setRemark(uuid);
                if (StringUtils.isNotBlank(mobile)) {
                    order.setFundType(OrderStateConstant.UN_SUPPORT_REFUND);
                    order.setMobile(mobile);
                }
                // make not null
                order.setCommentCourierContent("");
                order.setCommentDisplay("Y");
                order.setCredit(0.00);
                order.setEndSendTime(0);

                order = orderJpaRepository.save(order);
                orderId = order.getOrderId();
                logger.info("========================orderid=" + orderId);

                long time = System.currentTimeMillis();
                String payId = RandomStringUtils.random(4, "0123456789")
                        + Long.toString(time + orderId).substring(2);
                logger.info("========================payId=" + payId);

                order.setPayId(payId);
                orderJpaRepository.save(order);


                OrderState orderState = new OrderState();
                orderState.setDealTime((int) (System.currentTimeMillis() / 1000));
                orderState.setOrderId(orderId);
                orderState.setState("未支付");
                orderState.setDetail("新增一个订单");
                orderStateJpaRepository.save(orderState);

                oDto.setMerchantName(merchant.getTitle());
                oDto.setOrderId(orderId);
                oDto.setTotalOrigin(totalOrigin);
                oDto.setTotalPrice(totalPrice);
                oDto.setTotalDiscount(Double.toString(totalDiscount));
                oDto.setPayId(payId);
                oDto.setTotalCount(totalNum);
                oDto.setPayType("");
                oDto.setPayTypeName("");
                oDto.setCompleteTime("");

                oDto.setMenuList(sList);

                if (dough != 0 && cost != 0) {// 判断是否是抢购订单
                    if (cost != totalPrice) {
                        return null;
                    }
                }
                // 金额校验
                if (totalPrice <= 0) {
                    return null;
                }

                // 添加order_menu关联信息
                for (Map<String, Object> m : paramList) {
                    double price = 0.0;//商品单价

                    menuId = Integer.valueOf(m.get("menuId").toString());
                    num = Integer.valueOf(m.get("num").toString());
                    // 根据菜单id获取单价
                    Menu menu = menuJpaRepository.findOne(menuId);
                    price = menu.getPrice();
                    menuPromotionId = 0;
                    salesPromotion = "N";// 是否促销，默认为不促销
                    Double money = price;
                    double promotionPrice = 0.0;//促销价格

                    if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                        salesPromotion = m.get("salesPromotion").toString();
                    }

                    List<PromotionMenu> menuPromotionList
                            = promotionMenuMapper.findByMenuIdAndMerchantId((long) menuId, (long) merchantId);
                    if (CollectionUtils.isNotEmpty(menuPromotionList)) {
                        PromotionMenu promotionMenu = menuPromotionList.get(0);
                        if (promotionMenu != null) {
                            if (promotionMenu.getPromotionQuantity() > num) {
                                promotionPrice = promotionMenu.getPromotionPrice().doubleValue();
                                menuPromotionId = promotionMenu.getPromotionActivityId();
                                salesPromotion = "Y";// 改为促销
                            }
                        }
                    }

                    // 总价
                    double total = num * price;
                    double totalPromotionPrice = num * promotionPrice;
                    double discountMoney = 0.0;
                    BigDecimal bigdecimalTotalPrice = new BigDecimal(total);
                    BigDecimal bigdecimalTotalPromotionPrice = new BigDecimal(totalPromotionPrice);
                    total = bigdecimalTotalPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    totalPromotionPrice = bigdecimalTotalPromotionPrice
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (promotionPrice != 0.0) {
                        discountMoney = bigdecimalTotalPrice.subtract(bigdecimalTotalPromotionPrice)
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    } else {
                        totalPromotionPrice = total;
                    }
                    OrderMenu orderMenu = new OrderMenu();
                    //COLUMN
                    orderMenu.setMenuId(menuId);
                    orderMenu.setOrderId((long) orderId);
                    orderMenu.setPrice(money);
                    orderMenu.setQuantity(num);
                    orderMenu.setTotalPrice(total);
                    orderMenu.setPromotionMoney(totalPromotionPrice);
                    orderMenu.setSalesPromotion(salesPromotion);
                    orderMenu.setMenuPromotionId(menuPromotionId);
                    orderMenuJpaRepository.save(orderMenu);

                    //JSON
                    orderMenu.setMenuId(menuId);
                    orderMenu.setName(menu.getName());
                    orderMenu.setPrice(price);
                    orderMenu.setCount(num);
                    orderMenu.setTotal(total);
                    orderMenu.setSalesPromotion(salesPromotion);
                    orderMenu.setPromotionPrice(totalPromotionPrice);
                    orderMenu.setDiscountMoney(discountMoney);
                    orderMenu.setErrMsg("");
                    orderMenu.setUnit(menu.getUnit());
                    sList.add(orderMenu);
                }

            }

            if (orderId != null) {
                //保存收银员与订单ID的对应关系
                CashierOrder cashierOrder = new CashierOrder();
                cashierOrder.setCashierId(cashierId);
                cashierOrder.setOrderId(orderId);
                cashierOrder.setCreateTime((int) (System.currentTimeMillis() / 1000));
                cashierOrderJpaRepository.save(cashierOrder);
            }
        }
        return oDto;
    }

    /**
     * 商品预检查
     *
     * @param params
     * @param errMsgs
     * @return
     */
    private boolean menuPreCheck(String params, Map<Integer, Map<String, Object>> errMsgs) {
        if (errMsgs == null) {
            errMsgs = new HashMap<Integer, Map<String, Object>>();
        }
        List<Map<String, Object>> paramList = JsonHelper.toList(params);
        if (params != null && paramList.size() > 0) {
            for (Map<String, Object> m : paramList) {
                Integer menuId = Integer.valueOf(m.get("menuId").toString());
                int num = Integer.valueOf(m.get("num").toString());
                //如果是旧版本就不校验
                if (m.get("name") == null || m.get("price") == null) {
                    return true;
                }
                double priceOnPos = Double.parseDouble(m.get("price").toString());

                // 根据菜单id获取单价
                Menu menuInfo = menuJpaRepository.findOne(menuId);
                Map<String, Object> menuErrorDescription = new HashMap<String, Object>();
                menuErrorDescription.put("menuId", menuId);
                if (menuInfo == null) {
                    menuErrorDescription.put("errMsg", "商品不存在");
                    menuErrorDescription.put("menuName", m.get("name"));
                    menuErrorDescription.put("price", priceOnPos);
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }
                menuErrorDescription.put("menuName", menuInfo.getName());
                menuErrorDescription.put("price", menuInfo.getPrice());

                logger.info("menuId:{}, menuInfo:{}", menuId, JSON.toJSONString(menuInfo));
                double price = menuInfo.getPrice();

                if (price != priceOnPos) {
                    menuErrorDescription.put("price", priceOnPos);
                    menuErrorDescription.put("errMsg", "价格不一致,实际价格:￥" + price);
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }

                int realNum = menuInfo.getTodayRepertory();
//                if (num > realNum) {
//                    menuErrorDescription.put("errMsg", "库存不足,实际库存:" + menuInfo.get("today_repertory"));
//                    errMsgs.put(menuId, menuErrorDescription);
//                    continue;
//                }

                String display = menuInfo.getDisplay();
                if (StringUtils.equals("N", display)) {
                    menuErrorDescription.put("errMsg", "商品已下架");
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }

                String isDelete = menuInfo.getIsDelete();
                if (StringUtils.equals("Y", isDelete)) {
                    menuErrorDescription.put("errMsg", "商品未录入");
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }
            }
        }
        logger.info("errorMsg:{}", JSON.toJSONString(errMsgs));
        return errMsgs.size() == 0;
    }

    public OrderEntry getOrder(long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        BasicResult basicResult = postOrder(GET_ORDER, params);
        if (basicResult.isSuccess()) {
            JSONObject jsonObject = basicResult.getResponse();
            OrderEntry orderEntry = jsonObject.toJavaObject(OrderEntry.class);
            return orderEntry;
        }
        return null;
    }


    public BasicResult prepareCreateOrder(long passportId, String orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("partnerUserId", passportId + "");
        params.put("orderType", orderType);
        return postOrder(PREPARE_CREATE_ORDER, params);
    }

    public BasicResult generateOrder(long passportId, String sequenceNumber
            , OrderTypeEnum orderType, long actualPrice, long totalPrice
            , List<Map<String, Object>> orderItems) {
        Map<String, String> params = new HashMap<>();
        params.put("sequenceNumber", sequenceNumber);
        params.put("partnerUserId", passportId + "");

        params.put("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey() + "");
//        params.put("transType", TransTypeEnum.PAYMENT.getKey() + "");
        params.put("shippingPassportId", passportId + "");
        params.put("shippingNickName", "");
        params.put("receiptUserId", passportId + "");
        params.put("totalAmount", totalPrice+"");
        params.put("actualAmount", actualPrice+"");
        params.put("discountAmount", "0");
        params.put("priceLogger", "0");
        return postOrder(GENERATE_ORDER, params);
    }

    public BasicResult paymentOrder(long orderId, int transType) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("transType", transType + "");
        return postOrder(PAYMENT_ORDER, params);
    }

    public BasicResult confirmOrder(long passportId, long orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId + "");
        params.put("partnerUserId", passportId + "");
        return postOrder(CONFIRM_ORDER, params);
    }
}
