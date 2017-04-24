package com.wm.service.impl.supermarket;

import com.alibaba.fastjson.JSON;
import com.base.constant.ErrorCode;
import com.base.constant.order.OrderType;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.base.exception.BusinessException;
import com.wm.controller.order.dto.OrderFromSuperMarketDTO;
import com.wm.controller.order.dto.SuperMarketOrderMenuVo;
import com.wm.controller.user.vo.CashierVo;
import com.wm.dao.menu.MenuDao;
import com.wm.dao.order.OrderDao;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuPromotionEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.CashSettlementRelationEntity;
import com.wm.entity.order.CashierOrderEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntity.SaleType;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.entity.supermarket.CashierSettlementLogEntity;
import com.wm.service.impl.pay.OrderHandleFactoryImpl;
import com.wm.service.menu.MenuPackageServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.BarcodePayServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.user.CashierServiceI;
import com.wm.util.AliOcs;
import com.wm.util.CacheKeyUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

@Service("superMarketService")
@Transactional
public class SuperMarketServiceImpl extends CommonServiceImpl implements SuperMarketServiceI {

    private static final Logger logger = LoggerFactory.getLogger(SuperMarketServiceImpl.class);

    @Autowired
    private OrderHandleFactoryImpl orderHandleService;

    @Autowired
    private MenuServiceI menuService;

    @Autowired
    private MerchantServiceI merchantService;

    @Autowired
    private OrderStateServiceI orderStateService;

    @Autowired
    private PrintServiceI printService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private BarcodePayServiceI wxBarcodePayService;

    @Autowired
    private BarcodePayServiceI aliBarcodePayService;

    @Autowired
    private CashierServiceI cashierService;

    @Autowired
    private OrderServiceI orderService;

    @Autowired
    private OrderIncomeServiceI orderIncomeService;

    @Autowired
    private MenuPackageServiceI menuPackageService;

    public static final String offlineOrderPrefix = "[offline_order] 离线订单:";
    public static final String superMarketOrderPrefix = "[market_order]  超市订单:";


    /**
     * 根据订单ID获取商家名称
     *
     * @param merchantId
     * @return
     */
    private String getMerchantTitle(Integer merchantId) {
        String key = "get_merchant_title_" + merchantId;

        Object o = AliOcs.getObject(key);
        if (o != null) {
            return (String) o;
        } else {
            String merchantName =
                    findOneForJdbc(" select m.title from merchant m where m.id = ?", String.class, merchantId);

            AliOcs.set(key, merchantName, 24 * 60 * 60);
            return merchantName;
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMenuInfo(Integer menuId) {
        String key = "get_menu_info_" + menuId;
        Object o = AliOcs.getObject(key);
        if (o != null) {
            return (Map<String, Object>) o;
        } else {
            Map<String, Object> menuInfo = findOneForJdbc("select name, unit from menu where id = ? ", menuId);

            AliOcs.set(key, menuInfo, 24 * 60 * 60);
            return menuInfo;
        }
    }

    /**
     * 根据订单ID获取商品的详情
     *
     * @param orderId
     * @return
     */
    public List<SuperMarketOrderMenuVo> getMenus(Integer orderId) {
        List<SuperMarketOrderMenuVo> menus = new ArrayList<SuperMarketOrderMenuVo>();

        List<Map<String, Object>> list = orderDao.getMenus(orderId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> menuMap : list) {
                SuperMarketOrderMenuVo sMenuVo = new SuperMarketOrderMenuVo();
                sMenuVo.setCount(Integer.parseInt(menuMap.get("quantity").toString()));
                sMenuVo.setMenuId(Integer.parseInt(menuMap.get("menu_id").toString()));
                sMenuVo.setPrice(Double.parseDouble(menuMap.get("price").toString()));
                if (menuMap.get("sales_promotion") != null) {
                    sMenuVo.setSalesPromotion(menuMap.get("sales_promotion").toString());
                }
                sMenuVo.setTotal(Double.parseDouble(menuMap.get("total_price").toString()));

                Map<String, Object> menuUnitMap = getMenuInfo(sMenuVo.getMenuId());
                if (menuUnitMap != null) {
                    sMenuVo.setName(menuUnitMap.get("name").toString());
                    sMenuVo.setUnit(menuUnitMap.get("unit").toString());
                } else {
                    logger.warn("无法根据menu_id{}找到对应的商品", sMenuVo.getMenuId());
                    sMenuVo.setName("默认商品");
                    sMenuVo.setUnit("");
                }
                menus.add(sMenuVo);
            }
        }
        return menus;
    }

    private int getTotalCount(List<SuperMarketOrderMenuVo> menuList) {
        int totalCount = 0;
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (SuperMarketOrderMenuVo menu : menuList) {
                totalCount += menu.getCount();
            }
        }
        return totalCount;
    }

    @Override
    public OrderFromSuperMarketDTO getSuperMarketOrderDetail(Integer orderId) {
        OrderFromSuperMarketDTO oDto = null;


        StringBuilder query = new StringBuilder();
        query.append(" SELECT o.pay_id, o.id, o.pay_type, o.origin, FROM_UNIXTIME(o.complete_time, '%Y-%m-%d %h:%i:%s') completeTime, o.merchant_id ");

        query.append(" , FROM_UNIXTIME(o.create_time, '%Y-%m-%d %h:%i:%s') createTime ,o.order_type , o.order_num , o.sale_type , o.remark ,  o.score_money ,o.card , o.member_discount_money , o.merchant_member_discount_money ,o.dine_in_discount_money ,o.cost_lunch_box ,o.delivery_fee ");
        query.append(" from `order` o");
        query.append(" where o.id = ? ");
        Map<String, Object> orderMap = findOneForJdbc(query.toString(), orderId);
        logger.info("获取订单详情：ordermap{}", JSON.toJSONString(orderMap));
        if (orderMap == null) {
            logger.warn("无法根据orderId:{}找到对应的订单信息。。。", orderId);
            return oDto;
        }

        oDto = new OrderFromSuperMarketDTO();
        oDto.setOrderId(Integer.parseInt(orderMap.get("id").toString()));
        oDto.setPayId(orderMap.get("pay_id").toString());

        oDto.setTotalPrice(Double.parseDouble(orderMap.get("origin").toString()));
        oDto.setPayType(orderMap.get("pay_type").toString());
        oDto.setPayTypeName(PayEnum.getCn(orderMap.get("pay_type").toString()));
        oDto.setCompleteTime(orderMap.get("completeTime").toString());

        oDto.setOrderType(orderMap.get("order_type") + "");
        oDto.setOrderNum(orderMap.get("order_num") + "");
        oDto.setSaleType(orderMap.get("sale_type") + "");
        oDto.setRemark(orderMap.get("remark") + "");

        oDto.setCreateTime(orderMap.get("createTime") + "");
        if (orderMap.get("score_money") != null) {
            oDto.setScoreMoney(new BigDecimal(orderMap.get("score_money") + "").setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        if (orderMap.get("card") != null) {
            oDto.setCard(new BigDecimal(orderMap.get("card") + "").setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }

        if (orderMap.get("member_discount_money") != null) {
            oDto.setMemberDiscountMoney(new BigDecimal(orderMap.get("member_discount_money") + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        }

        if (orderMap.get("merchant_member_discount_money") != null) {
            double d = oDto.getMemberDiscountMoney() + new BigDecimal(orderMap.get("merchant_member_discount_money") + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            d = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            oDto.setMemberDiscountMoney(d);
        }
        logger.info("MemberDiscountMoney is {}", oDto.getMemberDiscountMoney());

        if (orderMap.get("dine_in_discount_money") != null) {
            oDto.setDineInDiscountMoney(new BigDecimal(orderMap.get("dine_in_discount_money") + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        MerchantEntity merchantEntity = this.merchantService.findUniqueByProperty(MerchantEntity.class, "id", Integer.parseInt(orderMap.get("merchant_id") + ""));
        oDto.setMerchantAddress(merchantEntity.getAddress());
        oDto.setMerchantName(merchantEntity.getTitle());
        oDto.setMerchantMobile(StringUtils.isEmpty(merchantEntity.getMobile()) ? "" : merchantEntity.getMobile());
        //设置商家名称
        oDto.setMerchantName(getMerchantTitle(Integer.parseInt(orderMap.get("merchant_id").toString())));
        //获取商品列表
        List<SuperMarketOrderMenuVo> menuList = getMenus(orderId);
//        List<SuperMarketOrderMenuVo> menuList = new ArrayList<SuperMarketOrderMenuVo>();
        oDto.setMenuList(menuList);

        oDto.setTotalCount(getTotalCount(menuList));
        String totalPrice = new DecimalFormat("#0.00").format(oDto.getTotalPrice());
        oDto.setActuallyPaid(Double.parseDouble(totalPrice));

        double totleDiscount = oDto.getMemberDiscountMoney() + oDto.getDineInDiscountMoney();
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(oDto.getCard())) {
            totleDiscount += Double.parseDouble(oDto.getCard());
        }
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(oDto.getScoreMoney())) {
            totleDiscount += Double.parseDouble(oDto.getScoreMoney());
        }

        oDto.setTotalDiscount(new DecimalFormat("#0.00").format(totleDiscount));

        double plusPrice = 0;
        if (orderMap.get("cost_lunch_box") != null) {
            plusPrice += Double.parseDouble(orderMap.get("cost_lunch_box") + "");
            oDto.setCostLunchBox(Double.parseDouble(orderMap.get("cost_lunch_box") + ""));
        }

        if (orderMap.get("delivery_fee") != null) {
            plusPrice += Double.parseDouble(orderMap.get("delivery_fee") + "");
            oDto.setDeliveryFee(Double.parseDouble(orderMap.get("delivery_fee") + ""));
        }
        double origin = oDto.getTotalPrice() + plusPrice - totleDiscount;
        oDto.setTotalPrice(origin);
        return oDto;
    }

    @Override
    public void paySupermarketOrder(Integer orderId, Integer cashierId, String payType, int createTime) throws Exception {
        OrderEntity orderEntity = this.get(OrderEntity.class, orderId);
        orderEntity.setPayType(payType);
        saveOrUpdate(orderEntity);
        if (payType.equals(PayEnum.supermarkt_cash.getEn())) {
            if (OrderEntity.State.CONFIRM.equals(orderEntity.getState()) || OrderEntity.State.PAY.equals(orderEntity.getPayState())) {
                logger.warn("订单{}已支付，请不要重复支付", orderId);
                throw new RuntimeException("订单已支付");
            }
            //记录现金订单
            CashSettlementRelationEntity entity = new CashSettlementRelationEntity();
            entity.setCashOrderId(orderId);
            entity.setMerchantId(orderEntity.getMerchant().getId());
            entity.setSettlementOrderId(0);
            entity.setCashierId(cashierId);
            entity.setMoney(orderEntity.getOrigin());
            entity.setIsSettlemented("0");
            entity.setCreateTime(createTime == 0 ? DateUtils.getSeconds() : createTime);
            entity.setUpdateTime(DateUtils.getSeconds());
            this.save(entity);

            //更新订单的状态，及其他业务处理
            OrderHandler orderPayBackHandler = orderHandleService.getHandler(orderEntity.getOrderType());
            orderPayBackHandler.handle(orderEntity);
        }
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
        List<Map<String, Object>> paramList = JSONHelper.toList(params);
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
                Map<String, Object> menuInfo = menuDao.getMenuInfo(menuId);
                Map<String, Object> menuErrorDescription = new HashMap<String, Object>();
                menuErrorDescription.put("menuId", menuId);
                if (menuInfo == null) {
                    menuErrorDescription.put("errMsg", "商品不存在");
                    menuErrorDescription.put("menuName", m.get("name"));
                    menuErrorDescription.put("price", priceOnPos);
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }
                menuErrorDescription.put("menuName", menuInfo.get("name"));
                menuErrorDescription.put("price", menuInfo.get("price"));

                logger.info("menuId:{}, menuInfo:{}", menuId, JSON.toJSONString(menuInfo));
                double price = Double.parseDouble(menuInfo.get("price").toString());

                if (price != priceOnPos) {
                    menuErrorDescription.put("price", priceOnPos);
                    menuErrorDescription.put("errMsg", "价格不一致,实际价格:￥" + price);
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }

                int realNum = Integer.parseInt(menuInfo.get("today_repertory").toString());
//                if (num > realNum) {
//                    menuErrorDescription.put("errMsg", "库存不足,实际库存:" + menuInfo.get("today_repertory"));
//                    errMsgs.put(menuId, menuErrorDescription);
//                    continue;
//                }

                String display = menuInfo.get("display").toString();
                if (StringUtils.equals("N", display)) {
                    menuErrorDescription.put("errMsg", "商品已下架");
                    errMsgs.put(menuId, menuErrorDescription);
                    continue;
                }

                String isDelete = menuInfo.get("is_delete").toString();
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

    @Override
    public OrderFromSuperMarketDTO createOrderFromSuperMarket(Integer merchantId, Integer cashierId, String params, Integer createTime, String uuid)
            throws BusinessException {

        // 离线订单 校验 "[offline_order] 离线订单:"/ "[market_order]  超市订单:"

        if (StringUtils.isNotBlank(uuid) && uuid.startsWith(offlineOrderPrefix)) {
            String sql = "select count(*) from `order` where remark in (? ,?)";
            Long count = this.getCountForJdbcParam(sql,new String[]{uuid.replace(offlineOrderPrefix, superMarketOrderPrefix) , uuid});
            if (count > 0) {
                throw new BusinessException(12, "订单已重复");
            }
        }

        OrderFromSuperMarketDTO oDto = new OrderFromSuperMarketDTO();
        List<SuperMarketOrderMenuVo> sList = new ArrayList<SuperMarketOrderMenuVo>();
        Integer orderId = null;
        MerchantEntity merchant = merchantService.get(MerchantEntity.class, merchantId);

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
                    Entry<Integer, Map<String, Object>> entry = iterator.next();
                    Integer menuIdTemp = entry.getKey();
                    Map<String, Object> menuErrDescription = entry.getValue();

                    SuperMarketOrderMenuVo sMenuVo = new SuperMarketOrderMenuVo();
                    sMenuVo.setMenuId(menuIdTemp);
                    sMenuVo.setErrMsg(menuErrDescription.get("errMsg").toString());
                    sMenuVo.setName(menuErrDescription.get("menuName").toString());
                    sMenuVo.setPrice(Double.parseDouble(menuErrDescription.get("price").toString()));
                    sList.add(sMenuVo);
                }
                oDto.setMenuList(sList);
                return oDto;
            }


            List<Map<String, Object>> paramList = JSONHelper.toList(params);
            if (params != null && paramList.size() > 0) {
                for (Map<String, Object> m : paramList) {
                    double price = 0.0;//商品单价
                    double promotionPrice = 0.0;//商品促销单价
                    
                    salesPromotion = "N";
                    menuId = Integer.valueOf(m.get("menuId").toString());
                    num = Integer.valueOf(m.get("num").toString());
                    Map<String, Object> menuInfo = menuDao.getMenuInfo(menuId);
                    price = Double.parseDouble(menuInfo.get("price").toString());

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

                    List<Map<String, Object>> menuPromotionList = menuService.getMenuPromotionByMenuId(menuId,merchantId);
                    if (CollectionUtils.isNotEmpty(menuPromotionList)) {
                        Map<String, Object> map = menuPromotionList.get(0);
                        if (map != null) {
                        	if(Integer.parseInt(map.get("promotion_quantity").toString()) > num){
                        		promotionPrice = Double.valueOf(map.get("promotion_price").toString());
                        		salesPromotion = "Y";
                        	}
                        }
                    }
                    
                    // 计算总金额
                    totalOrigin += num * price;
                    if(promotionPrice != 0.0){
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

                String sql = "INSERT INTO `order`(user_id,city_id,merchant_id,state, online_money, origin,create_time,order_type,title,sale_type,remark)"
                        + " values(0,?,?,'unpay',?, ?, ? ,'supermarket','超市订单',2,?)";
                this.executeSql(sql, merchant.getCityId(), merchantId, 0, totalPrice, createTime == null ? System.currentTimeMillis() / 1000 : createTime, uuid);

                sql = "select last_insert_id() lastInsertId";
                Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
                orderId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
                logger.info("========================orderid=" + orderId);

                long time = System.currentTimeMillis();
                String payId = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderId).substring(2);
                logger.info("========================payId=" + payId);

                sql = "update `order` set pay_id=? where id=?";
                this.executeSql(sql, payId, orderId);

                orderStateService.createOrderState(orderId);
                oDto.setMerchantName(this.get(MerchantEntity.class, merchantId).getTitle());
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
                    MenuEntity menu = menuService.getEntity(MenuEntity.class, menuId);
                    price = menu.getPrice();
                    menuPromotionId = 0;
                    salesPromotion = "N";// 是否促销，默认为不促销
                    Double money = price;
                    double promotionPrice = 0.0;//促销价格

                    if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                        salesPromotion = m.get("salesPromotion").toString();
                    }

                    List<Map<String, Object>> menuPromotionList = menuService.getMenuPromotionByMenuId(menuId,merchantId);
                    if (CollectionUtils.isNotEmpty(menuPromotionList)) {
                        Map<String, Object> map = menuPromotionList.get(0);
                        if (map != null) {
                        	if(Integer.parseInt(map.get("promotion_quantity").toString()) > num){
                        		promotionPrice = Double.valueOf(map.get("promotion_price").toString());
                        		menuPromotionId = Integer.valueOf(map.get("promotion_activity_id").toString());
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
                    totalPromotionPrice = bigdecimalTotalPromotionPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if(promotionPrice != 0.0){
                    	discountMoney = bigdecimalTotalPrice.subtract(bigdecimalTotalPromotionPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    } else {
                    	totalPromotionPrice = total;
                    }
                    String o_msql = "insert into order_menu (menu_id, order_id, price, quantity, total_price,promotion_money,sales_promotion,menu_promotion_id) values(?, ?, ?, ?, ?,?,?,?)";
                    this.executeSql(o_msql, menuId, orderId, money, num, total, totalPromotionPrice, salesPromotion, menuPromotionId);

                    SuperMarketOrderMenuVo sMenuVo = new SuperMarketOrderMenuVo();
                    sMenuVo.setMenuId(menuId);
                    sMenuVo.setName(menu.getName());
                    sMenuVo.setPrice(price);
                    sMenuVo.setCount(num);
                    sMenuVo.setTotal(total);
                    sMenuVo.setSalesPromotion(salesPromotion);
                    sMenuVo.setPromotionPrice(totalPromotionPrice);
                    sMenuVo.setDiscountMoney(discountMoney);
                    sMenuVo.setErrMsg("");
                    String unit = findOneForJdbc("select unit from menu where id = ?", String.class, menuId);
                    sMenuVo.setUnit(unit);
                    sList.add(sMenuVo);
                }

            }

            if (orderId != null) {
                //保存收银员与订单ID的对应关系
                CashierOrderEntity cashierOrder = new CashierOrderEntity();
                cashierOrder.setCashierId(cashierId);
                cashierOrder.setOrderId(orderId);
                cashierOrder.setCreateTime(DateUtils.getSeconds());
                this.save(cashierOrder);
            }
        }
        return oDto;
    }

    /**
     * 根据订单表中orderType、payType、 saleType确定超市订单类型
     *
     * @param orderType
     * @param payType
     * @param saleType
     * @return
     */
    public String getSupermarketOrderType(Integer saleType, String orderType, String payType, String fromType) {
        if (SaleType.TAKEOUT.equals(saleType)) {
            return "外卖订单";
        } else {
            if (StringUtils.equals(OrderType.SUPERMARKET.getName(), orderType) || (StringUtils.equals("supermarket_codefree", orderType) && StringUtils.equals("pos", fromType))) {
                if (StringUtils.equals("supermarket_codefree", orderType) && StringUtils.equals("pos", fromType)) {
                    if (StringUtils.equals(PayEnum.supermarkt_cash.getEn(), payType)) {
                        return "超市无码现金";
                    } else if (StringUtils.equals(PayEnum.supermarkt_wxbarcode.getEn(), payType)) {
                        return "超市无码微信";
                    } else if (StringUtils.equals(PayEnum.supermarkt_alibarcode.getEn(), payType)) {
                        return "超市支无码付宝";
                    } else if (StringUtils.equals(PayEnum.balance.getEn(), payType)) {
                        return "无码平台会员";
                    } else if (StringUtils.equals(PayEnum.merchantpay.getEn(), payType)) {
                        return "无码商家会员";
                    } else {
                        return "";
                    }
                } else {
                    if (StringUtils.equals(PayEnum.supermarkt_cash.getEn(), payType)) {
                        return "超市现金";
                    } else if (StringUtils.equals(PayEnum.supermarkt_wxbarcode.getEn(), payType)) {
                        return "超市微信";
                    } else if (StringUtils.equals(PayEnum.supermarkt_alibarcode.getEn(), payType)) {
                        return "超市支付宝";
                    } else if (StringUtils.equals(PayEnum.balance.getEn(), payType)) {
                        return "平台会员";
                    } else if (StringUtils.equals(PayEnum.merchantpay.getEn(), payType)) {
                        return "商家会员";
                    } else {
                        return "";
                    }
                }
            } else if (StringUtils.equals(OrderType.SCAN_ORDER.getName(), orderType)) {
                return "扫码订单";
            } else if (!StringUtils.equals(OrderType.SUPERMARKET_SETTLEMENT.getName(), orderType)) {
                return "门店订单";
            } else {
                return "";
            }
        }
    }

    @Override
    public OrderEntity createSettlementOrder(Integer cashierId)
            throws BusinessException {
        CashierVo cashierVo = cashierService.get(cashierId);
        if (cashierVo == null) {
            throw new IllegalArgumentException("参数错误");
        }

        Integer merchantId = cashierVo.getMerchantId();
        MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);
        if (merchant == null) {
            logger.warn("无法根据商家ID:{}找到对应的商家", merchantId);
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT, "参数错误");
        }

        //获取所有的现金订单
        String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=?";
        List<CashSettlementRelationEntity> unSettledOrders = this.findHql(hql, cashierId);
        if (CollectionUtils.isEmpty(unSettledOrders)) {
            logger.info("没有待结算的现金订单, merchantId:{}, cashierId:{}", merchantId, cashierId);
            throw new BusinessException(ErrorCode.WRONG_DATA, "没有待结算的现金订单");
        }


        Double totalMoney = 0.0;
        for (CashSettlementRelationEntity unSettledOrder : unSettledOrders) {
            totalMoney += unSettledOrder.getMoney();
        }
        //创建结算订单
        Integer orderId = saveSettlementOrder(merchant.getCityId(), merchantId, totalMoney);

        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order.getId() == null) {
            logger.warn("创建超市现金订单的结算订单失败.....");
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "保存结算订单失败");
        }

        long time = System.currentTimeMillis();
        String payId = RandomStringUtils.random(4, "0123456789") + Long.toString(time + order.getId()).substring(2);

        order.setPayId(payId);
        this.saveOrUpdate(order);

        //保存收银员与订单ID的对应关系
        CashierOrderEntity cashierOrder = new CashierOrderEntity();
        cashierOrder.setCashierId(cashierId);
        cashierOrder.setOrderId(orderId);
        cashierOrder.setCreateTime(DateUtils.getSeconds());
        this.save(cashierOrder);

        return order;
    }

    /**
     * 保存一条结算订单
     *
     * @param merchantId
     * @param totalMoney
     * @return
     */
    private Integer saveSettlementOrder(Integer cityId, Integer merchantId, Double totalMoney) {
        //创建结算订单
        String sql = "INSERT INTO `order`(user_id,city_id,merchant_id,state,origin,create_time,order_type,title,sale_type)"
                + " values(0,?,?,'unpay',?,UNIX_TIMESTAMP(),'supermarket_settlement','超市结算订单', 2)";
        executeSql(sql, cityId, merchantId, totalMoney);

        sql = "select last_insert_id() lastInsertId";

        Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
        return Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
    }


    @Override
    public OrderEntity getLatestSettlementOrder(Integer cashierId, String beginTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select csl.settlement_order_id, csl.paid_time ");
        sql.append(" from  0085_cashier_settlement_log csl ");
        sql.append(" where csl.cashier_id = ?");
        sql.append(" and csl.is_paid = '1' ");
        sql.append(" and csl.create_time < UNIX_TIMESTAMP(?)");
        sql.append(" ORDER BY csl.create_time DESC");
        sql.append(" LIMIT 0, 1");

        Map<String, Object> map = this.findOneForJdbc(sql.toString(), cashierId, endTime);
        if (map != null) {

            Integer orderId = Integer.valueOf(map.get("settlement_order_id").toString());
            //之前结算没有现金订单，取结算完成的时间
            if (orderId.intValue() == -1) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setPayTime(Integer.valueOf(map.get("paid_time").toString()));
                return orderEntity;
            }
            return this.get(OrderEntity.class, orderId);

        }
        logger.info("无法找到收银员:{}, beginTime:{}, endTime:{}内的结算订单", cashierId, beginTime, endTime);
        return null;
    }

    /**
     * 查找某个收银员最早的没有结算的现金订单
     *
     * @param cashierId 营业员 ID
     * @return
     */
    private CashSettlementRelationEntity getEarlyestUnsettledCashOrder(Integer cashierId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.cash_order_id FROM 0085_cash_settlement_relation r ");
        sql.append(" where r.settlement_order_id=0 ");
        sql.append(" and cashier_id = ?");
        sql.append(" ORDER BY r.create_time");
        sql.append(" LIMIT 0,1 ");

        Integer cashOrderId = this.findOneForJdbc(sql.toString(), Integer.class, cashierId);
        logger.info("收银员：{} 最早的未结算现金订单：{}", cashierId, cashOrderId);

        if (cashOrderId == null) {
            logger.warn("没有一条结算的现金订单。。。。。");
            return null;
        } else {
            return this.get(CashSettlementRelationEntity.class, cashOrderId);
        }
    }

    @Override
    public String getBeginTimeForStatictis(Integer merchantId, Integer cashierId) {
        DateTime now = DateTime.now();
        String beginTime = now.minusDays(1).toString("yyyy-MM-dd HH:mm:ss");
        String endTime = now.toString("yyyy-MM-dd HH:mm:ss");

        //先找到最近的结算订单
        OrderEntity lastSettlementOrder = getLatestSettlementOrder(cashierId, beginTime, endTime);
        //统计开始时间从那个时间点开始
        String newBeginTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        //有最近的结算订单
        if (lastSettlementOrder != null) {
            newBeginTime = new DateTime((long) lastSettlementOrder.getPayTime() * 1000).toString("yyyy-MM-dd HH:mm:ss");
            logger.info("上次结算时间为:{}", newBeginTime);
        } else {
            //查找最早的没有结算的现金订单
            logger.warn("没有结算信息。。。,无法确定时间段,");
            CashSettlementRelationEntity earlyestUnsettledCashOrder = getEarlyestUnsettledCashOrder(cashierId);
            if (earlyestUnsettledCashOrder != null) {
                newBeginTime = new DateTime((long) earlyestUnsettledCashOrder.getCreateTime() * 1000).toString("yyyy-MM-dd HH:mm:ss");
            }
        }
        logger.info("确定结算时间为:{}", newBeginTime);
        return newBeginTime;
    }


    @Override
    public Map<String, Object> statisticAfterSettlement(Integer cashierId)
            throws BusinessException {
        CashierVo cashier = cashierService.get(cashierId);
        if (cashier == null) {
            logger.warn("无法根据cashierId:{}找到对应的收银员信息");
            throw new IllegalArgumentException("参数错误");
        }

        Integer merchantId = cashier.getMerchantId();
        String beginTime = getBeginTimeForStatictis(merchantId, cashierId);
        if (beginTime == null) {
            logger.warn("查不到收银员cashierId:{}对应的登录信息", cashierId);
            throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
        }
        String endTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

        //统计所有的订单
        Integer takeoutOrderNum = 0, eatinOrderNum = 0, alibarcodeOrderNum = 0, wxbarcodeOrderNum = 0, cashOrderNum = 0, scanOrderNum = 0;
        Double takeoutOrderMoney = 0.0, eatinOrderMoney = 0.0, alibarcodeOrderMoney = 0.0, wxbarcodeOrderMoney = 0.0, cashOrderMoney = 0.0, scanOrderMoney = 0.0;
        //超市非POS机上的订单
        List<Map<String, Object>> noPosOrders = orderDao.getSupermarketNoPosPayedOrders(merchantId, beginTime, endTime);
        //超市POS机上的订单
        List<Map<String, Object>> posOrders = orderDao.getSupermarketPosPayedOrders(cashierId, beginTime, endTime);
        //退款的订单
        Map<String, Object> refundOrderMap = orderDao.getSupermarketRefundOrder(merchantId, beginTime, endTime);
        List<Map<String, Object>> allOrders = new ArrayList<Map<String, Object>>();
        allOrders.addAll(noPosOrders);
        allOrders.addAll(posOrders);

        if (CollectionUtils.isNotEmpty(allOrders)) {
            for (Map<String, Object> payOrder : allOrders) {
                Integer saleType = Integer.parseInt(payOrder.get("sale_type").toString());
                String orderType = payOrder.get("order_type").toString();
                String payType = payOrder.get("pay_type").toString();
                String fromType = payOrder.get("from_type").toString();
                String realOrderType = getSupermarketOrderType(saleType, orderType, payType, fromType);

                if (StringUtils.equals(realOrderType, "外卖订单")) {
                    takeoutOrderNum++;
                    takeoutOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "门店订单")) {
                    eatinOrderNum++;
                    eatinOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市现金")) {
                    cashOrderNum++;
                    cashOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市支付宝")) {
                    alibarcodeOrderNum++;
                    alibarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市微信")) {
                    wxbarcodeOrderNum++;
                    wxbarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "扫码订单")) {
                    scanOrderNum++;
                    scanOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else {
                    logger.warn("不识别的订单类型，saleType:{}，orderType:{}, payType:{} ", saleType, orderType, payType);
                }
            }
        }


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("merchantName", this.getMerchantTitle(merchantId));

        DecimalFormat format = new DecimalFormat("#0.00");
        data.put("totalQuantity", takeoutOrderNum + eatinOrderNum + cashOrderNum + alibarcodeOrderNum + wxbarcodeOrderNum + scanOrderNum);
        data.put("totalIncome", format.format(takeoutOrderMoney + eatinOrderMoney + cashOrderMoney + alibarcodeOrderMoney + wxbarcodeOrderMoney + scanOrderMoney));
        data.put("beginTime", beginTime);
        data.put("endTime", endTime);

        List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
        Map<String, Object> cashOrder = new HashMap<String, Object>();
        cashOrder.put("orderType", "超市现金");
        cashOrder.put("quantity", cashOrderNum);
        cashOrder.put("income", format.format(cashOrderMoney));

        Map<String, Object> aliPayOrder = new HashMap<String, Object>();
        aliPayOrder.put("orderType", "超市支付宝");
        aliPayOrder.put("quantity", alibarcodeOrderNum);
        aliPayOrder.put("income", format.format(alibarcodeOrderMoney));

        Map<String, Object> wxPayOrder = new HashMap<String, Object>();
        wxPayOrder.put("orderType", "超市微信");
        wxPayOrder.put("quantity", wxbarcodeOrderNum);
        wxPayOrder.put("income", format.format(wxbarcodeOrderMoney));

        Map<String, Object> takeoutOrder = new HashMap<String, Object>();
        takeoutOrder.put("orderType", "外卖订单");
        takeoutOrder.put("quantity", takeoutOrderNum);
        takeoutOrder.put("income", format.format(takeoutOrderMoney));

        Map<String, Object> eatinOrder = new HashMap<String, Object>();
        eatinOrder.put("orderType", "门店订单");
        eatinOrder.put("quantity", eatinOrderNum);
        eatinOrder.put("income", format.format(eatinOrderMoney));

        Map<String, Object> scanOrder = new HashMap<String, Object>();
        scanOrder.put("orderType", "扫码订单");
        scanOrder.put("quantity", scanOrderNum);
        scanOrder.put("income", format.format(scanOrderMoney));

        Map<String, Object> refundOrder = new HashMap<String, Object>();
        refundOrder.put("orderType", "退单");
        refundOrder.put("quantity", refundOrderMap.get("num"));
        refundOrder.put("income", refundOrderMap.get("money") == null ? "0.00" : refundOrderMap.get("money"));

        orders.add(cashOrder);
        orders.add(aliPayOrder);
        orders.add(wxPayOrder);
        orders.add(scanOrder);
        orders.add(takeoutOrder);
        orders.add(eatinOrder);
        orders.add(refundOrder);

        data.put("orders", orders);

        return data;
    }

    @Override
    public List<Integer> getCashOrderIdsOfSettlementOrder(Integer settlementOrderId) {
        List<Integer> cashOrderIds = new ArrayList<Integer>();

        OrderEntity orderEntity = this.get(OrderEntity.class, settlementOrderId);

        if (orderEntity == null) {
            logger.warn("参数错误，无法根据settlementOrderId:{}找到对应的结算订单", settlementOrderId);
            return cashOrderIds;
        }

        List<CashSettlementRelationEntity> entities = this.findByProperty(CashSettlementRelationEntity.class, "settlementOrderId", settlementOrderId);

        if (CollectionUtils.isNotEmpty(entities)) {
            for (CashSettlementRelationEntity entity : entities) {
                cashOrderIds.add(entity.getCashOrderId());
            }
        } else {
            logger.warn("无法找到结算订单ID找到对应的现金订单,settlementOrderId:{}", settlementOrderId);
        }
        return cashOrderIds;
    }

    @Override
    public boolean existUnsettledCashOrder(Integer cashierId) {
        String hql = " from CashSettlementRelationEntity where cashierId = ? and settlementOrderId = 0";
        return this.findHql(hql, cashierId).size() > 0;
    }

    @Override
    public void saveCashierInventoryMoney(CashierVo cashierVo, String cashInventoryMoney, Integer settlementOrderId) {
        CashierSettlementLogEntity cashierSettlement = new CashierSettlementLogEntity();
        cashierSettlement.setCashierId(cashierVo.getId());
        cashierSettlement.setMerchantId(cashierVo.getMerchantId());
        cashierSettlement.setSettlementOrderId(settlementOrderId);
        cashierSettlement.setCreateTime(DateUtils.getSeconds());
        cashierSettlement.setIsPaid("0");
        cashierSettlement.setMoney(0.00);
        cashierSettlement.setCash(Double.valueOf(cashInventoryMoney));
        cashierService.save(cashierSettlement);
    }

    @Override
    public Map<String, Object> getSettlementInfo(Integer cashierId,
                                                 Integer cashierSettlementLogId) throws BusinessException {
        //获取当前营业员未付款的结算日志
        CashierSettlementLogEntity cashierSettlementLogEntity = this.get(CashierSettlementLogEntity.class, cashierSettlementLogId);
        StringBuilder query = new StringBuilder();
        //获取当前营业员上一条结算日志
        query.append("select create_time from 0085_cashier_settlement_log");
        query.append(" where cashier_id = ? and id < ? order by id desc limit 0,1");
        Map<String, Object> map = findOneForJdbc(query.toString(), cashierSettlementLogEntity.getCashierId(), cashierSettlementLogId);
        String beginTime = "";
        if (map != null) {
            beginTime = new DateTime(Long.parseLong(map.get("create_time").toString()) * 1000).toString("yyyy-MM-dd HH:mm:ss");
        } else {
            //查找收银员最早的没有结算的现金订单
            CashSettlementRelationEntity earlyestUnsettledCashOrder = getEarlyestUnsettledCashOrder(cashierId);
            if (earlyestUnsettledCashOrder != null) {
                beginTime = new DateTime((long) earlyestUnsettledCashOrder.getCreateTime() * 1000).toString("yyyy-MM-dd HH:mm:ss");
            }
        }

        String endTime = new DateTime((long) cashierSettlementLogEntity.getCreateTime() * 1000).toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endTime", endTime);
        params.put("beginTime", beginTime);
        params.put("cash", cashierSettlementLogEntity.getCash());
        params.put("merchantId", cashierSettlementLogEntity.getMerchantId());

        DecimalFormat df = new DecimalFormat("#0.00");
//		OrderEntity order = this.get(OrderEntity.class, cashierSettlementLogEntity.getSettlementOrderId());
        OrderEntity order = this.createSettlementOrder(cashierId);
        CashierVo cashier = cashierService.get(cashierId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("totalMoney", df.format(order.getOrigin()));
        result.put("orderId", order.getId());
        result.put("orderType", order.getOrderType());
        result.put("description", order.getTitle());
        result.put("cashierName", cashier.getName());
        result.put("cashierType", cashier.getCashierType());
        if (1 == cashier.getCashierType().intValue()) {
            result.put("typeName", "营业员");
        }
        if (2 == cashier.getCashierType().intValue()) {
            result.put("typeName", "店长");
        }
        Map<String, Object> newStatisticMap = newStatisticAfterSettlement(cashierId, params);
        String key = CacheKeyUtil.getCashierSettlementKey(order.getId(), cashierId);
        AliOcs.set(key, newStatisticMap, 60 * 60);
        result.putAll(newStatisticMap);
        return result;
    }

    @Override
    public Map<String, Object> cashierSettlement(Integer cashierId, String cash, String state) throws BusinessException {
        OrderEntity order = this.createSettlementOrder(cashierId);
        CashierVo cashier = cashierService.get(cashierId);

        if (cashier == null) {
            logger.warn("无法根据cashierId:{}找到对应的收银员信息");
            throw new IllegalArgumentException("参数错误");
        }

        Integer merchantId = cashier.getMerchantId();

        String beginTime = getBeginTimeForStatictis(merchantId, cashierId);
        if (beginTime == null) {
            logger.warn("查不到收银员cashierId:{}对应的登录信息", cashierId);
            throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
        }
        String endTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cash", cash);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        params.put("merchantId", merchantId);
        Map<String, Object> orderStatistics = this.newStatisticAfterSettlement(cashierId, params);

        String key = CacheKeyUtil.getCashierSettlementKey(order.getId(), cashierId);
        AliOcs.set(key, orderStatistics, 60 * 60);


        DecimalFormat df = new DecimalFormat("#0.00");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("totalMoney", df.format(order.getOrigin()));
        result.put("orderId", order.getId());
        result.put("orderType", order.getOrderType());
        result.put("description", order.getTitle());
        result.put("cashierName", cashier.getName());
        result.put("cashierType", cashier.getCashierType());
        if (1 == cashier.getCashierType().intValue()) {
            result.put("typeName", "营业员");
        }
        if (2 == cashier.getCashierType().intValue()) {
            result.put("typeName", "店长");
        }
        result.putAll(orderStatistics);

        if ("Y".equals(state)) {
            //根据收银员清点的现金创建一条结算日志
            this.saveCashierInventoryMoney(cashier, cash, order.getId());
        }

        return result;
    }


    @Override
    public Map<String, Object> newStatisticAfterSettlement(Integer cashierId, Map<String, Object> params)
            throws BusinessException {
        Integer merchantId = Integer.valueOf(params.get("merchantId").toString());
        String beginTime = params.get("beginTime").toString();
        String endTime = params.get("endTime").toString();

        //统计所有的订单
        Integer takeoutOrderNum = 0, eatinOrderNum = 0, alibarcodeOrderNum = 0, wxbarcodeOrderNum = 0, cashOrderNum = 0, scanOrderNum = 0, balanceMemberOrderNum = 0, merchantMemberOrderNum = 0, noCodeOrderNum = 0;
        Double takeoutOrderMoney = 0.0, eatinOrderMoney = 0.0, alibarcodeOrderMoney = 0.0, wxbarcodeOrderMoney = 0.0, cashOrderMoney = 0.0, scanOrderMoney = 0.0, balanceMemberOrderMoney = 0.0, merchantMemberOrderMoney = 0.0, noCodeOrderMoney = 0.0;
        //超市非POS机上的订单
//		List<Map<String, Object>> noPosOrders = orderDao.getSupermarketNoPosPayedOrders(merchantId, beginTime, endTime);
        //超市POS机上的订单
        List<Map<String, Object>> posOrders = orderDao.getSupermarketPosPayedOrders(cashierId, beginTime, endTime);
        //退款的订单
        Map<String, Object> refundOrderMap = orderDao.getSupermarketRefundOrder(cashierId, beginTime, endTime);
        List<Map<String, Object>> allOrders = new ArrayList<Map<String, Object>>();
//		allOrders.addAll(noPosOrders);
        allOrders.addAll(posOrders);

        if (CollectionUtils.isNotEmpty(allOrders)) {
            for (Map<String, Object> payOrder : allOrders) {
                Integer saleType = Integer.parseInt(payOrder.get("sale_type").toString());
                String orderType = payOrder.get("order_type").toString();
                String payType = payOrder.get("pay_type").toString();
                String fromType = payOrder.get("from_type").toString();
                String realOrderType = getSupermarketOrderType(saleType, orderType, payType, fromType);

//				if(StringUtils.equals(realOrderType, "外卖订单")){
//					takeoutOrderNum++;
//					takeoutOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
//				}
//				else if(StringUtils.equals(realOrderType, "门店订单")){
//					eatinOrderNum++;
//					eatinOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
//				}
                if (StringUtils.equals(realOrderType, "超市现金")) {
                    cashOrderNum++;
                    cashOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市支付宝")) {
                    alibarcodeOrderNum++;
                    alibarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市微信")) {
                    wxbarcodeOrderNum++;
                    wxbarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                }
//				else if(StringUtils.equals(realOrderType, "扫码订单")){
//					scanOrderNum++;
//					scanOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
//				}
                else if (StringUtils.equals(realOrderType, "平台会员")) {
                    balanceMemberOrderNum++;
                    balanceMemberOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "商家会员")) {
                    merchantMemberOrderNum++;
                    merchantMemberOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市无码现金")) {
                    cashOrderNum++;
                    cashOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                    noCodeOrderNum++;
                    noCodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市无码微信")) {
                    wxbarcodeOrderNum++;
                    wxbarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                    noCodeOrderNum++;
                    noCodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "超市支无码付宝")) {
                    alibarcodeOrderNum++;
                    alibarcodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                    noCodeOrderNum++;
                    noCodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "无码平台会员")) {
                    balanceMemberOrderNum++;
                    balanceMemberOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                    noCodeOrderNum++;
                    noCodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else if (StringUtils.equals(realOrderType, "无码商家会员")) {
                    merchantMemberOrderNum++;
                    merchantMemberOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                    noCodeOrderNum++;
                    noCodeOrderMoney += Double.parseDouble(payOrder.get("origin").toString());
                } else {
                    logger.warn("不识别的订单类型，saleType:{}，orderType:{}, payType:{} ", saleType, orderType, payType);
                }
            }
        }


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("merchantName", this.getMerchantTitle(merchantId));

        DecimalFormat format = new DecimalFormat("#0.00");
        data.put("totalQuantity", takeoutOrderNum + eatinOrderNum + cashOrderNum + alibarcodeOrderNum + wxbarcodeOrderNum + scanOrderNum + balanceMemberOrderNum + merchantMemberOrderNum);
        data.put("totalIncome", format.format(takeoutOrderMoney + eatinOrderMoney + cashOrderMoney + alibarcodeOrderMoney + wxbarcodeOrderMoney + scanOrderMoney + balanceMemberOrderMoney + merchantMemberOrderMoney));
        data.put("beginTime", beginTime);
        data.put("endTime", endTime);
        data.put("cash", Double.valueOf(params.get("cash").toString()));
        data.put("orderTypeName", "清点现金");

        List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
        Map<String, Object> cashOrder = new HashMap<String, Object>();
        cashOrder.put("orderType", "超市现金");
        cashOrder.put("quantity", cashOrderNum);
        cashOrder.put("income", format.format(cashOrderMoney));

        Map<String, Object> aliPayOrder = new HashMap<String, Object>();
        aliPayOrder.put("orderType", "支付宝");
        aliPayOrder.put("quantity", alibarcodeOrderNum);
        aliPayOrder.put("income", format.format(alibarcodeOrderMoney));

        Map<String, Object> wxPayOrder = new HashMap<String, Object>();
        wxPayOrder.put("orderType", "超市微信");
        wxPayOrder.put("quantity", wxbarcodeOrderNum);
        wxPayOrder.put("income", format.format(wxbarcodeOrderMoney));

        Map<String, Object> takeoutOrder = new HashMap<String, Object>();
        takeoutOrder.put("orderType", "外卖订单");
        takeoutOrder.put("quantity", takeoutOrderNum);
        takeoutOrder.put("income", format.format(takeoutOrderMoney));

        Map<String, Object> eatinOrder = new HashMap<String, Object>();
        eatinOrder.put("orderType", "门店订单");
        eatinOrder.put("quantity", eatinOrderNum);
        eatinOrder.put("income", format.format(eatinOrderMoney));

        Map<String, Object> scanOrder = new HashMap<String, Object>();
        scanOrder.put("orderType", "扫码订单");
        scanOrder.put("quantity", scanOrderNum);
        scanOrder.put("income", format.format(scanOrderMoney));

        Map<String, Object> refundOrder = new HashMap<String, Object>();
        refundOrder.put("orderType", "POS退单");
        refundOrder.put("quantity", refundOrderMap.get("num"));
        refundOrder.put("income", refundOrderMap.get("money") == null ? "0.00" : refundOrderMap.get("money"));

//		Map<String, Object> cashMoney = new HashMap<String, Object>();
//		cashMoney.put("orderType", "清点现金");
//		cashMoney.put("quantity", "");
//		cashMoney.put("income", Double.valueOf(params.get("cash").toString()));

        Map<String, Object> balanceMemberOrder = new HashMap<String, Object>();
        balanceMemberOrder.put("orderType", "1号会员");
        balanceMemberOrder.put("quantity", balanceMemberOrderNum);
        balanceMemberOrder.put("income", format.format(balanceMemberOrderMoney));

        Map<String, Object> merchantMemberOrder = new HashMap<String, Object>();
        merchantMemberOrder.put("orderType", "商家会员");
        merchantMemberOrder.put("quantity", merchantMemberOrderNum);
        merchantMemberOrder.put("income", format.format(merchantMemberOrderMoney));

        orders.add(cashOrder);
//		orders.add(cashMoney);
        orders.add(wxPayOrder);
        orders.add(aliPayOrder);
        orders.add(balanceMemberOrder);
        orders.add(merchantMemberOrder);
//		orders.add(scanOrder);
//		orders.add(takeoutOrder);
//		orders.add(eatinOrder);
        orders.add(refundOrder);

        Integer posEdition = cashierService.getPosEdition(cashierId);
        //基础版
        if (posEdition.intValue() == 2) {
            List<Map<String, Object>> basicPosOrders = new ArrayList<Map<String, Object>>();

            List<Map<String, Object>> basicOrderModifys = orderDao.getBasicPosOrderModifyOrder(merchantId, beginTime, endTime);
            List<Map<String, Object>> modifyPosOrders = getOrderModifyType(basicOrderModifys);

            //无码收银订单
            Map<String, Object> noCodeOrder = new HashMap<String, Object>();
            noCodeOrder.put("orderType", "无码收银");
            noCodeOrder.put("quantity", noCodeOrderNum);
            noCodeOrder.put("income", format.format(noCodeOrderMoney));

            basicPosOrders.add(noCodeOrder);
            basicPosOrders.addAll(modifyPosOrders);

            data.put("basicPosOrders", basicPosOrders);
        }

        data.put("orders", orders);
        return data;
    }

    @Override
    public Map<String, Object> isHaveCashOrder(Integer cashierId) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        //获取所有的现金订单
        String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=?";
        List<CashSettlementRelationEntity> unSettledOrders = this.findHql(hql, cashierId);
        if (CollectionUtils.isEmpty(unSettledOrders)) {
            logger.info("没有待结算的现金订单,cashierId:{}", cashierId);
            return resultMap;
        }

        String findCashierSettlementLogHql = " from CashierSettlementLogEntity where isPaid='0' and cashierId=?";
        List<CashierSettlementLogEntity> cashierSettlements = findHql(findCashierSettlementLogHql, cashierId);
        if (CollectionUtils.isEmpty(cashierSettlements)) {
            resultMap.put("state", "Y");//Y表示该收银员之前没有未付款的结算订单,此次需要输入现金
            resultMap.put("cash", "");
        } else {
            resultMap.put("state", "N");//N表示该收银员有未付款的结算订单，此次不在输入现金
            resultMap.put("cash", cashierSettlements.get(0).getCash());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> calChange(Integer orderId, Double cash) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        OrderEntity orderEntity = this.get(OrderEntity.class, orderId);
        Double originDouble = orderEntity.getOrigin();
        if (cash < originDouble) {
            logger.error("实收金额小于订单金额");
            throw new RuntimeException("实收金额小于订单金额");
        }
        Double changeDouble = cash - originDouble;
        resultMap.put("orderId", orderId);
        resultMap.put("totalPrice", originDouble);
        resultMap.put("change", String.format("%.2f", changeDouble));
        resultMap.put("actuallyPaid", cash);
        resultMap.put("memberDiscountMoney", 0.00);
        resultMap.put("minusDiscountMoney", 0.00);
        return resultMap;
    }

    @Override
    public void franchiseSettle(OrderEntity order) throws Exception {
        Integer orderId = order.getId();

        //更新订单状态
        logger.info("handle supermarket franchise settelement order. orderId={}", orderId);
        order.setPayType(PayEnum.supermarkt_cash.getEn());
        order.setState(OrderStateEnum.CONFIRM.getOrderStateEn());
        order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
        order.setPayTime(DateUtils.getSeconds());
        order.setCompleteTime(DateUtils.getSeconds());
        order.setOrderNum(AliOcs.genOrderNum(String.valueOf(order.getMerchant().getId())));
        orderService.updateEntitie(order);

        CashierOrderEntity cashierOrder = orderService.get(CashierOrderEntity.class, orderId);
        if (cashierOrder == null) {
            logger.warn("无法根据结算订单ID{}找到收银员与订单关联信息....", orderId);
            throw new Exception("无法根据结算订单ID" + orderId + "找到收银员与订单关联信息");
        }

        //查找结算订单对应的现金订单
        String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=?";
        List<CashSettlementRelationEntity> allUnSettledOrders = this.findHql(hql, cashierOrder.getCashierId());
        List<Integer> cashOrderIds = new ArrayList<Integer>();
        if (CollectionUtils.isNotEmpty(allUnSettledOrders)) {
            for (CashSettlementRelationEntity allUnSettledOrder : allUnSettledOrders) {
                cashOrderIds.add(allUnSettledOrder.getCashOrderId());
                //更新现金订单的结算订单Id
                allUnSettledOrder.setSettlementOrderId(orderId);
                allUnSettledOrder.setIsSettlemented("1");
                allUnSettledOrder.setUpdateTime(DateUtils.getSeconds());
                this.saveOrUpdate(allUnSettledOrder);
            }
        }
        logger.info("查找结算订单对应的现金订单, settleorderId:{}, cashOrderIds:{}", orderId, StringUtils.join(cashOrderIds, ","));
        if (CollectionUtils.isNotEmpty(cashOrderIds)) {
            for (Integer cashOrderId : cashOrderIds) {
                logger.info("订单对应的现金预收入, settleorderId:{}, cashOrderId:{}", orderId, cashOrderId);
                OrderIncomeEntity orderIncomeEntity = orderIncomeService.getOrderIncomeByOrderIdAndType(cashOrderId, 0);
                try {
                    orderIncomeService.franchiseUnOrderIncome(orderIncomeEntity.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("settlement unorderincome orderId:{} failed !!!!", cashOrderId);
                }
            }
        }

    }

    @Override
    public Map<String, Object> settlementAndExit(Integer cashierId, String cash, String state, Integer cashierSettlementLogId) throws BusinessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer orderIdKey = -1;//获取缓存key参数
        DecimalFormat df = new DecimalFormat("#0.00");
        //根据收银员id，获取收银员
        CashierVo cashier = cashierService.get(cashierId);
        Integer merchantId = cashier.getMerchantId();
        //获取所有的现金订单
        String hql = " from CashSettlementRelationEntity where settlementOrderId=0 and cashierId=?";
        List<CashSettlementRelationEntity> unSettledOrders = this.findHql(hql, cashierId);
        if (CollectionUtils.isEmpty(unSettledOrders)) {
            //没有现金订单
            logger.info("没有待结算的现金订单,cashierId:{}", cashierId);
            //没有现金订单，不创建结算订单，根据收银员清点的现金创建一条结算日志，结算日志的结算订单设为-1
            if ("normal".equals(state)) {
                this.saveCashierInventoryMoney(cashier, cash, -1);
            }
            result.put("totalMoney", "0.00");
            result.put("orderId", -1);
            result.put("orderType", "supermart_settlement");
            result.put("description", "超市结算订单");
        } else {
            //有现金订单，创建结算订单
            OrderEntity order = this.createSettlementOrder(cashierId);
            //根据收银员清点的现金创建一条结算日志
            if ("normal".equals(state)) {
                this.saveCashierInventoryMoney(cashier, cash, order.getId());
            }
            orderIdKey = order.getId();
            result.put("totalMoney", df.format(order.getOrigin()));
            result.put("orderId", order.getId());
            result.put("orderType", order.getOrderType());
            result.put("description", order.getTitle());
        }


        //统计的开始时间
        String beginTime = getBeginTimeForStatictis(merchantId, cashierId);
        if (beginTime == null) {
            logger.warn("查不到收银员cashierId:{}对应的登录信息", cashierId);
            throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
        }
        //统计的开始时间
        String endTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<String, Object>();
//		//登录时不需要输入现金金额,取之前输入的现金金额
//		if("login".equals(state)){
//			//获取当前营业员未付款的结算日志
//			CashierSettlementLogEntity cashierSettlementLogEntity = this.get(CashierSettlementLogEntity.class, cashierSettlementLogId);
//			cash = cashierSettlementLogEntity.getCash().toString();
//		}
        params.put("cash", cash);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        params.put("merchantId", merchantId);
        Map<String, Object> orderStatistics = this.newStatisticAfterSettlement(cashierId, params);

        String key = CacheKeyUtil.getCashierSettlementKey(orderIdKey, cashierId);
        AliOcs.set(key, orderStatistics, 60 * 60);

        result.put("cashierName", cashier.getName());
        result.put("cashierType", cashier.getCashierType());
        if (1 == cashier.getCashierType().intValue()) {
            result.put("typeName", "营业员");
        }
        if (2 == cashier.getCashierType().intValue()) {
            result.put("typeName", "店长");
        }
        result.putAll(orderStatistics);
        return result;
    }

    /**
     * 超市订单付款前校验订单金额，订单金额不能超出限额
     */
    @Override
    public void checkMaxPayMoney(Integer orderId, String payType) throws Exception {
        //默认支付宝限额1000，微信限额3000，平台限额10000，单位：元
        double alipayLimit = 1000.00;
        double wechatLimit = 3000.00;
        double platformLimit = 10000.00;

        StringBuilder query = new StringBuilder();
        query.append(" select ol.alipay_limit, ol.wechat_limit, o.order_type, o.origin from `order` o");
        query.append(" left join order_limit ol on o.merchant_id = ol.merchant_id");
        query.append(" where o.id = ?");
        Map<String, Object> map = findOneForJdbc(query.toString(), orderId);

        if (map != null) {
            double origin = Double.parseDouble(map.get("origin").toString());

            if (origin == 0D) {
                throw new BusinessException(ErrorCode.WRONG_DATA, "扫码支付金额必须大于￥0.00");
            }
            String orderType = map.get("order_type").toString();

            if (map.get("alipay_limit") != null) {
                alipayLimit = Double.valueOf(map.get("alipay_limit").toString()) / 100;
            }
            if (map.get("wechat_limit") != null) {
                wechatLimit = Double.valueOf(map.get("wechat_limit").toString()) / 100;
            }
            //超市结算订单不能限额
            if (!OrderEntity.OrderType.SUPERMARKET_SETTLEMENT.equals(orderType)) {
                if (PayEnum.supermarkt_alibarcode.getEn().equals(payType)) {
                    if (origin > alipayLimit) {
                        throw new BusinessException(ErrorCode.WRONG_DATA, "超出支付宝收款限额￥" + alipayLimit);
                    }
                }
                if (PayEnum.supermarkt_wxbarcode.getEn().equals(payType)) {
                    if (origin > wechatLimit) {
                        throw new BusinessException(ErrorCode.WRONG_DATA, "超出微信收款限额￥" + wechatLimit);
                    }
                }
                if ("platformPay".equals(payType)) {
                    if (origin > platformLimit) {
                        throw new BusinessException(ErrorCode.WRONG_DATA, "超出平台收款限额￥" + platformLimit);
                    }
                }
            }
        }
    }


    private List<Map<String, Object>> getOrderModifyType(List<Map<String, Object>> basicOrderModifys) {
        List<Map<String, Object>> modifyPosOrders = new ArrayList<Map<String, Object>>();

        //初始化整单打折、抹零、免单、打折的订单数为0，金额为0.0
        Integer freeOrderNum = 0, discountedOrderNum = 0, noPointOrderNum = 0, modifyOrderNum = 0;
        Double freeOrderMoney = 0.0, discountedOrderMoney = 0.0, noPointOrderMoney = 0.0, modifyOrderMoney = 0.0;
        if (!CollectionUtils.isEmpty(basicOrderModifys)) {
            for (Map<String, Object> map : basicOrderModifys) {
                if ("免单".equals(map.get("modifyType"))) {
                    freeOrderNum = freeOrderNum + Integer.parseInt(map.get("num").toString());
                    freeOrderMoney = freeOrderMoney + Double.parseDouble(map.get("money").toString());
                }
                if ("整单打折".equals(map.get("modifyType"))) {
                    discountedOrderNum = discountedOrderNum + Integer.parseInt(map.get("num").toString());
                    discountedOrderMoney = discountedOrderMoney + Double.parseDouble(map.get("money").toString());
                }
                if ("抹零".equals(map.get("modifyType"))) {
                    noPointOrderNum = noPointOrderNum + Integer.parseInt(map.get("num").toString());
                    noPointOrderMoney = noPointOrderMoney + Double.parseDouble(map.get("money").toString());
                }
                if ("金额修改".equals(map.get("modifyType"))) {
                    modifyOrderNum = modifyOrderNum + Integer.parseInt(map.get("num").toString());
                    modifyOrderMoney = modifyOrderMoney + Double.parseDouble(map.get("money").toString());
                }
            }
        }

        DecimalFormat format = new DecimalFormat("#0.00");

        //整单打折、抹零、免单统计
        //免单
        Map<String, Object> freeOrder = new HashMap<String, Object>();
        freeOrder.put("orderType", "免单");
        freeOrder.put("quantity", freeOrderNum);
        freeOrder.put("income", format.format(freeOrderMoney));

        //打折
        Map<String, Object> discountedOrder = new HashMap<String, Object>();
        discountedOrder.put("orderType", "整单打折");
        discountedOrder.put("quantity", discountedOrderNum);
        discountedOrder.put("income", format.format(discountedOrderMoney));

        //抹零
        Map<String, Object> noPointOrder = new HashMap<String, Object>();
        noPointOrder.put("orderType", "抹零");
        noPointOrder.put("quantity", noPointOrderNum);
        noPointOrder.put("income", format.format(noPointOrderMoney));

        //修改
        Map<String, Object> modifyOrder = new HashMap<String, Object>();
        modifyOrder.put("orderType", "金额修改");
        modifyOrder.put("quantity", modifyOrderNum);
        modifyOrder.put("income", format.format(modifyOrderMoney));

        modifyPosOrders.add(noPointOrder);
        modifyPosOrders.add(freeOrder);
        modifyPosOrders.add(discountedOrder);
        modifyPosOrders.add(modifyOrder);

        return modifyPosOrders;
    }
}
