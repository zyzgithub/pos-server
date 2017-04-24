package com.wm.service.impl.menu;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.wm.controller.supplychain.SupplyChainMenuVo;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.vo.MenuVo;
import com.wm.dao.menu.MenuDao;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuPromotionEntity;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrdermenuEntity;
import com.wm.entity.pos.MarketingReportLine;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.menu.MenutypeServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pos.BasicPosOrderServiceI;
import com.wm.util.Dialect;
import com.wm.util.PageList;

@Service("menuService")
@Transactional
public class MenuServiceImpl extends CommonServiceImpl implements MenuServiceI {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private static final String findOrderMenuByOrderId = "from OrdermenuEntity m where m.order.id=?";
    private static final String findNeedResetRespMerchIds = "select id from merchant where is_delete='N' and group_id not in (511,547)";
    private static final String updateResp = "update menu set today_repertory = repertory where merchant_id = ?";

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private BasicPosOrderServiceI basicPosOrderService;
    @Resource
    private MenutypeServiceI menutypeService;

    @Override
    public List<Map<String, Object>> getMenuList(int merchant_id, int userId) {
        String sql = "SELECT mt.id AS 'typeId',mt.name AS 'typeName' FROM menu_type mt WHERE mt.merchant_id="
                + merchant_id + " order by mt.sort_num asc ";

        List<Map<String, Object>> types = this.findForJdbc(sql);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> menuDetail = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();

        for (int i = 0; i < types.size(); i++) {
            m = types.get(i);
            String typeId = m.get("typeId").toString();

            // 查询商家菜品
            sql = "SELECT m.today_repertory,m.id AS 'menuId',m.`name`,m.price,m.buy_count,m.limit_today,CONCAT(s.`value`,m.image) AS 'image',m.intro,t.name typename FROM `menu` m,"
                    + "system_config s,menu_type t WHERE t.id = m.type_id and m.merchant_id=" + merchant_id
                    + " AND s.`code`='menu_url'" + "AND m.type_id=" + typeId
                    + " and m.display='Y' order by m.menu_sort asc ";

            menuDetail = this.findForJdbc(sql);
            List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();
            if (menuDetail != null && menuDetail.size() > 0) {
                for (int j = 0; j < menuDetail.size(); j++) {
                    Map<String, Object> map = menuDetail.get(j);
                    int menuId = Integer.valueOf(map.get("menuId").toString());
                    List<Map<String, Object>> menuPromotionList = new ArrayList<Map<String, Object>>();
                    // menuPromotionList = this.findForJdbc(sql, menuId);
                    menuPromotionList = this.getMenuPromotionByMenuId(menuId);
                    double promotionMoney = 0;// 促销价
                    int menuPromotionId = 0;// 促销ID
                    String salesPromotion = "N";// 是否促销
                    int limitPromotion = 10;// 限购量
                    int sumQuantity = 0;// 用户在促销时间里该菜品购买的份数
                    int salesVolume = 0;// 剩余促销菜品数量
                    int promotionQuantity = 0;
                    if (menuPromotionList.size() > 0) {
                        Map<String, Object> menuPromtion = menuPromotionList.get(0);
                        if (menuPromtion != null) {
                            limitPromotion = Integer.parseInt(menuPromtion.get("limit_quantity").toString());// 结束时间（时分秒）
                            salesVolume = Integer.parseInt(menuPromtion.get("sales_volume").toString());// 促销销量
                            promotionMoney = Double.valueOf(menuPromtion.get("money").toString());// 促销价
                            menuPromotionId = Integer.valueOf(menuPromtion.get("id").toString());// 促销ID
                            salesPromotion = menuPromtion.get("promotion").toString();// 是否促销
                            promotionQuantity = Integer.parseInt(menuPromtion.get("promotion_quantity").toString());// 促销库存
                            // 统计用户在该菜品促销时间里购买了多少份菜品
                            sql = "SELECT ifnull(sum(m.quantity),0) quantity from `order` o JOIN order_menu m on o.id =m.order_id JOIN menu u on m.menu_id=u.id "
                                    + "where  o.state in ('pay','accept','done','confirm','evaluated') and o.order_type in ('normal','mobile')"
                                    + " and FROM_UNIXTIME(o.pay_time,'%y%m%d')>=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%y%m%d') and m.sales_promotion='Y'"
                                    + " and m.menu_id =" + menuId + " and o.user_id=" + userId;

                            List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();
                            countList = this.findForJdbc(sql);
                            sumQuantity = Integer.parseInt(countList.get(0).get("quantity").toString());// 用户在促销时间里该菜品购买的份数
                        }
                    }
                    int residuelimitPromotion = limitPromotion - sumQuantity;// 用户还能购买的份数
                    if (residuelimitPromotion <= 0 || promotionQuantity <= 0) {
                        salesPromotion = "N";
                        menuPromotionId = 0;
                    }
                    map.put("residueQuantity", residuelimitPromotion);// 用户还能购买的份数
                    map.put("promotionMoney", promotionMoney);// 促销价
                    map.put("menuPromotionId", menuPromotionId);// 促销ID
                    map.put("salesPromotion", salesPromotion);// 是否促销
                    map.put("promotionQuantity", promotionQuantity);// 促销数量
                    map.put("salesVolume", salesVolume);// 促销菜品销量
                    obj.add(map);
                }

                m.put("menuDetail", obj);
                list.add(m);
            }
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> getMenuPromotionByMenuId(int menuId) {
        String sql = "select m.id,FROM_UNIXTIME(m.promotion_begin_date,'%y-%m-%d') promotion_begin_date,FROM_UNIXTIME(m.promotion_over_date,'%y-%m-%d') promotion_over_date,"
                + "FROM_UNIXTIME(m.promotion_begin_hour,'%H:%i:%s') promotion_begin_hour,FROM_UNIXTIME(m.promotion_over_hour,'%H:%i:%s') promotion_over_hour,"
                + "ifnull(m.promotion_quantity,0) promotion_quantity,ifnull(m.sales_volume,0) sales_volume,m.promotion,ifnull(m.limit_quantity,0) limit_quantity,ifnull(m.money,0) money,"
                + "m.menu_id FROM menu_promotion m "
                + "where m.menu_id=? and m.promotion_begin_date<=UNIX_TIMESTAMP(DATE_FORMAT(NOW(),'%Y%m%d')) "
                + "and m.promotion_over_date>=UNIX_TIMESTAMP(DATE_FORMAT(NOW(),'%Y%m%d')) "
                + "and m.promotion_begin_hour<= TIME_TO_SEC(NOW()) and m.promotion_over_hour>= TIME_TO_SEC(NOW()) "
                + "and m.promotion='Y' ";
        return this.findForJdbc(sql, menuId);
    }
    
    @Override
    public List<Map<String, Object>> getMenuPromotionByMenuId(int menuId, int merchantId) {
        String sql = "SELECT pm.menu_id,pm.promotion_activity_id,pm.promotion_price,pm.promotion_quantity FROM promotion_menu pm, promotion_activity pa WHERE pm.menu_id = ? "
        		+ "AND pa.merchant_id = ? AND pm.merchant_id = ? "
        		+ "AND ( pa.deadline_type = 1 OR "
					+ "(pa.deadline_type = 2 and pa.start_date <= UNIX_TIMESTAMP(current_date()) AND pa.end_date >= UNIX_TIMESTAMP(current_date()) "
						+ "AND pa.start_hour <= TIME_TO_SEC(NOW()) and pa.end_hour >= TIME_TO_SEC(NOW())) "
        		+ ") AND pa.state = 1 AND pa.range_type IN (1,2) and pm.promotion_activity_id = pa.id";
        return this.findForJdbc(sql, menuId, merchantId, merchantId);
    }

    public void buyCount(Integer orderId) {
        List<OrdermenuEntity> orderMenus = this.findHql(findOrderMenuByOrderId, orderId);
        for (OrdermenuEntity orderMenu : orderMenus) {
            this.updateMenuRepertory(orderMenu.getMenu().getId(), orderMenu.getQuantity());
            this.updatePromotionMenuRepertory(orderMenu.getMenu().getId(), orderMenu.getMenuPromotionId(), orderMenu.getQuantity());
        }
        this.buyCountMenuPromotion(orderId);// 促销剩余数量
    }

    public void updateMenuRepertory(Integer menuId, Integer quantity) {
        MenuEntity menu = this.get(MenuEntity.class, menuId);
        // 记录销量
        menu.setBuyCount(menu.getBuyCount() + quantity);
        // 库存菜品数量减去销量
        Integer number = menu.getTodayRepertory() - quantity;
        if (number <= 0) {
            logger.warn("menu repertory is not enough !!! menuId={}", menu.getId());
            // 圈圈说库存可以是负数的. 2016年12月20日17:48:16
            // number = 0;
        }
        menu.setTodayRepertory(number);
        menu.setUpdateTime((int) (System.currentTimeMillis() / 1000));

        this.updateEntitie(menu);
    }

    public void updatePromotionMenuRepertory(Integer menuId, Integer menuPromotionId, Integer quantity) {
    	if(null == menuPromotionId || null == menuId){
    		return;
    	}
    	String sql = "update promotion_menu set promotion_quantity = promotion_quantity - "+quantity
    			+" where menu_id = "+menuId+" and promotion_activity_id = "+ menuPromotionId; 
        this.updateBySqlString(sql);
    }
    
    @Override
    public void buyCountMenuPromotion(int orderId) {
        String sql = "SELECT o.menu_promotion_id,o.quantity  from order_menu o where o.sales_promotion='Y' and o.order_id=?";
        List<Map<String, Object>> list = this.findForJdbc(sql, orderId);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int quantity = Integer.parseInt(map.get("quantity").toString());
            MenuPromotionEntity menuPromotion = this.get(MenuPromotionEntity.class,
                    Integer.valueOf(map.get("menu_promotion_id").toString()));
            if (null == menuPromotion)
                continue;

            menuPromotion.setSalesVolume(menuPromotion.getSalesVolume() + quantity);// 加促销销量
            menuPromotion.setPromotion_quantity(menuPromotion.getPromotion_quantity() - quantity);// 减促销数量
            this.saveOrUpdate(menuPromotion);
        }

    }

    @Override
    public List<Map<String, Object>> getMenuPromotionById(int id) {
        List<Map<String, Object>> menuPromotionList = new ArrayList<Map<String, Object>>();
        String sql = "select m.id,FROM_UNIXTIME(m.promotion_begin_date,'%y-%m-%d') promotion_begin_date,FROM_UNIXTIME(m.promotion_over_date,'%y-%m-%d') promotion_over_date,"
                + "FROM_UNIXTIME(m.promotion_begin_hour,'%H:%i:%s') promotion_begin_hour,FROM_UNIXTIME(m.promotion_over_hour,'%H:%i:%s') promotion_over_hour,"
                + "ifnull(m.promotion_quantity,0) promotion_quantity,ifnull(m.sales_volume,0) sales_volume,m.promotion,ifnull(m.limit_quantity,0) limit_quantity,ifnull(m.money,0) money,"
                + "m.menu_id FROM menu_promotion m "
                + "where m.promotion_begin_date<=UNIX_TIMESTAMP(DATE_FORMAT(NOW(),'%Y%m%d')) "
                + "and m.promotion_over_date>=UNIX_TIMESTAMP(DATE_FORMAT(NOW(),'%Y%m%d')) "
                + "and m.promotion_begin_hour<= TIME_TO_SEC(NOW()) and m.promotion_over_hour>= TIME_TO_SEC(NOW()) "
                + "and m.promotion='Y' and m.promotion_quantity>0";
        if (id > 0) {
            sql = sql + " and m.id=" + id;
        }
        menuPromotionList = this.findForJdbc(sql);
        return menuPromotionList;

    }

    @Override
    public void subtractMenuPromotion(int orderId) {
        // 修改促销菜品促销数量
        List<Map<String, Object>> menuPromotionList = orderService.getOrderMenuByOrderId(orderId);
        for (int i = 0; i < menuPromotionList.size(); i++) {
            Map<String, Object> map = menuPromotionList.get(i);
            if (map != null) {
                int menuPromotionId = 0;
                int quantity = 0;
                // String salesPromotion = "N";
                if (map.get("menu_promotion_id") != null && !"".equals(map.get("menu_promotion_id"))) {
                    menuPromotionId = Integer.parseInt(map.get("menu_promotion_id").toString());
                    quantity = Integer.parseInt(map.get("quantity").toString());
                    // salesPromotion = map.get("sales_promotion").toString();
                    // 促销状态可为null
                    if ("Y".equals(map.get("sales_promotion"))) {
                        MenuPromotionEntity menuPromtion = this.get(MenuPromotionEntity.class, menuPromotionId);
                        if (StringUtil.isNotEmpty(menuPromtion)) {
                            menuPromtion.setSalesVolume(menuPromtion.getSalesVolume() - quantity);// 减促销数量
                            menuPromtion.setPromotion_quantity(menuPromtion.getPromotion_quantity() + quantity);// 加促销数量
                            this.saveOrUpdate(menuPromtion);
                        }
                    }
                }
            }
        }

    }

    @Override
    public AjaxJson verificationPayMenuPromotion(int orderId) {
        AjaxJson j = new AjaxJson();
        OrderEntity order = this.get(OrderEntity.class, orderId);
        // 统计订单金额
        List<Map<String, Object>> list = orderService.getOrderMenuByOrderId(orderId);
        double money = 0;// 总金额
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int num = Integer.parseInt(map.get("quantity").toString());
            String salesPromotion = map.get("sales_promotion").toString();
            double price = 0;
            int menuPromotionId = 0;
            if ("Y".equals(salesPromotion)) {// 判断是否是促销
                price = Double.valueOf(map.get("promotion_money").toString());// 促销价
                if (map.get("menu_promotion_id") != null && !"".equals(map.get("menu_promotion_id").toString())) {
                    menuPromotionId = Integer.parseInt(map.get("menu_promotion_id").toString());// 促销表ID
                    MenuPromotionEntity menuPromotion = this.get(MenuPromotionEntity.class, menuPromotionId);// 促销菜品信息
                    MenuEntity menu = this.get(MenuEntity.class, menuPromotion.getMenuId()); // 促销的菜品信息

                    // 统计用户购买过多少该促销菜
                    String sql = "SELECT ifnull(sum(m.quantity),0) quantity from `order` o JOIN order_menu m on o.id =m.order_id JOIN menu u on m.menu_id=u.id "
                            + "where  o.state in ('pay','accept','done','confirm','evaluated') and o.order_type in ('normal','mobile')"
                            + " and FROM_UNIXTIME(o.pay_time,'%y%m%d')>=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%y%m%d') and m.sales_promotion='Y'"
                            + " and m.menu_id =" + menuPromotion.getMenuId() + " and o.user_id="
                            + order.getWuser().getId();

                    List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();
                    countList = this.findForJdbc(sql);
                    int sumQuantity = 0;// 用户在促销时间里该菜品购买的份数
                    if (countList.get(0).get("quantity") != null
                            && !"".equals(countList.get(0).get("quantity").toString())) {
                        sumQuantity = Integer.parseInt(countList.get(0).get("quantity").toString());// 用户在促销时间里该菜品购买的份数
                    }
                    int residuelimitPromotion = menuPromotion.getLimitQuantity() - sumQuantity;// 用户还能购买的份数
                    if (num > residuelimitPromotion) {
                        j.setMsg(menu.getName() + "超出限购数量，你还能购买" + residuelimitPromotion + "份");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }

                    // 判断促销菜品的剩余促销是足够用户购买
                    if (menuPromotion.getPromotion_quantity() <= 0 || menuPromotion.getPromotion_quantity() < num) {
                        j.setMsg(menu.getName() + "促销库存不足，还剩" + menuPromotion.getPromotion_quantity() + "份");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }

                    // 判断促销菜品是否还在促销
                    if (this.getMenuPromotionById(menuPromotionId).size() == 0) {
                        j.setMsg(menu.getName() + "已经促销完了");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }

                    if (menuPromotion.getMoney() != price) {
                        j.setMsg("非法支付," + menu.getName() + "促销已过期");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }
                }
            } else {
                price = Double.valueOf(map.get("price").toString());// 原价
            }

            money += num * price;
        }
        BigDecimal bmoney = new BigDecimal(money);
        System.out.println(bmoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + ":" + order.getOrigin());
        if (bmoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() != order.getOrigin()) {// 判断支付金额是否一致
            j.setMsg("非法支付,订单已经过期");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }

        j.setMsg("验证通过");
        j.setStateCode("00");
        j.setSuccess(true);
        return j;
    }

    @Override
    public MenuVo getMenuById(Integer menuId, Integer userId) {
        MenuVo vo = menuDao.findById(menuId);
        if (null != vo) {
            if (vo.isPromoting()) {
                // 限购，每人每天限购是否超出限制
                int count = menuDao.queryTodayCountWhenPromoting(userId, menuId);
                vo.setLimitCount(vo.getLimitCount() - count);
            }
        }
        return vo;
    }

    @Override
    public Map<String, Object> getMenu(Integer menuId, Integer userId) {
        Map<String, Object> menuMap = new HashMap<String, Object>();
        // 查询商家菜品
        String sql = " SELECT m.*," + "		m.id as menuId," + "		CONCAT(s.`value`,m.image) AS 'image', "
                + "		t.name AS typename, " + "		mt.title merchantName, "
                + "		mt.display merchantDisplay, " + "		mt.delivery_begin  deliveryBegin " + " FROM `menu` m "
                + " LEFT JOIN system_config s ON s.`code`='menu_url' " + " LEFT JOIN menu_type t ON t.id = m.type_id "
                + " LEFT JOIN merchant mt ON m.merchant_id = mt.id " + " WHERE m.id= " + menuId + " AND m.display='Y' ";
        menuMap = this.findOneForJdbc(sql);// 菜品信息
        if (menuMap == null) {
            return null;
        }
        List<Map<String, Object>> menuPromotionList = this.getMenuPromotionByMenuId(menuId);// 该菜品促销列表

        Double promotionMoney = 0D;// 促销价
        Long menuPromotionId = 0L;// 促销ID
        String salesPromotion = "N";// 是否促销
        Long limitPromotion = 100L;// 限购量
        BigDecimal sumQuantity = new BigDecimal(0);// 用户在促销时间里该菜品购买的份数
        Long salesVolume = 0L;// 剩余促销菜品数量
        Long promotionQuantity = 0L;

        if (menuPromotionList.size() > 0) {
            Map<String, Object> menuPromtion = menuPromotionList.get(0);
            menuMap.put("promotionBeginDate", menuPromtion.get("promotion_begin_date"));// 开始日期
            menuMap.put("promotionOverDate", menuPromtion.get("promotion_over_date")); // 结束日期
            menuMap.put("promotionBeginHour", menuPromtion.get("promotion_begin_hour"));// 开始时间
            menuMap.put("promotionOverHour", menuPromtion.get("promotion_over_hour")); // 结束时间

            limitPromotion = (Long) menuPromtion.get("limit_quantity");// 结束时间（时分秒）
            salesVolume = (Long) menuPromtion.get("sales_volume");// 促销销量
            promotionMoney = (Double) menuPromtion.get("money");// 促销价
            menuPromotionId = (Long) menuPromtion.get("id");// 促销ID
            salesPromotion = (String) menuPromtion.get("promotion");// 是否促销
            promotionQuantity = (Long) menuPromtion.get("promotion_quantity");// 促销库存

            // 统计用户在该菜品促销时间里购买了多少份菜品
            sql = "SELECT ifnull(sum(m.quantity),0) quantity from `order` o JOIN order_menu m on o.id =m.order_id JOIN menu u on m.menu_id=u.id "
                    + "where  o.state in ('pay','accept','done','confirm','evaluated') and o.order_type in ('normal','mobile')"
                    + " and FROM_UNIXTIME(o.pay_time,'%y%m%d')>=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%y%m%d') and m.sales_promotion='Y'"
                    + " and m.menu_id = " + menuId + " and o.user_id= " + userId;

            Map<String, Object> countMap = this.findOneForJdbc(sql);
            sumQuantity = (BigDecimal) countMap.get("quantity");// 用户在促销时间里该菜品购买的份数
        }
        Long residuelimitPromotion = limitPromotion - sumQuantity.longValue();// 用户还能购买的份数
        if (residuelimitPromotion <= 0 || promotionQuantity <= 0) {
            salesPromotion = "N";
            menuPromotionId = 0L;
        }
        menuMap.put("residueQuantity", residuelimitPromotion);// 用户还能购买的份数
        menuMap.put("promotionMoney", promotionMoney);// 促销价
        menuMap.put("menuPromotionId", menuPromotionId);// 促销ID
        menuMap.put("salesPromotion", salesPromotion);// 是否促销
        menuMap.put("promotionQuantity", promotionQuantity);// 促销数量
        menuMap.put("salesVolume", salesVolume);// 促销菜品销量

        return menuMap;
    }

    @Override
    public List<MenuVo> findByMerchantId(Integer merchantId, Integer userId) {
        List<MenuVo> vos = menuDao.findByMerchantId(merchantId);
        for (MenuVo vo : vos) {
            if (vo.isPromoting()) {
                // 限购，每人每天限购是否超出限制
                int count = menuDao.queryTodayCountWhenPromoting(userId, vo.getId());
                vo.setLimitCount(vo.getLimitCount() - count);
            }
        }
        return vos;
    }

    @Override
    public List<MenuVo> findFirstPageMenuByMerchantId(Integer merchantId, Integer menuTypeId, Integer userId,
                                                      Integer page, Integer rows) {
        List<MenuVo> vos = menuDao.findFirstPageMenuByMerchantId(merchantId, menuTypeId, page, rows);
        for (MenuVo vo : vos) {
            if (vo.isPromoting()) {
                // 限购，每人每天限购是否超出限制
                int count = menuDao.queryTodayCountWhenPromoting(userId, vo.getId());
                vo.setLimitCount(vo.getLimitCount() - count);
            }
        }
        return vos;
    }

    @Override
    public int findBuyCount(Integer merchantId) {
        return menuDao.findBuyCount(merchantId);
    }

    @Override
    public boolean judgePriceAndCount(List<Shopcart> carts) {
        for (Shopcart cart : carts) {
            MenuVo vo = menuDao.findById(cart.getMenuId());
            if (null == vo)
                return false;
            cart.promoting(vo.isPromoting());
            cart.setPromoteId(vo.getPromotion());
            if (cart.getCount() > vo.getRepertory() || cart.getPrice() != vo.getPrice()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PageList<com.wm.entity.menu.MenuVo> findByEntityList(com.wm.entity.menu.MenuVo menu, Integer page,
                                                                Integer rows) {
    	return findByEntityList(menu, null, page, rows);
    }
    
    @Override
    public PageList<com.wm.entity.menu.MenuVo> findByEntityList(com.wm.entity.menu.MenuVo menu, List<Integer> activityIds, Integer page,
                                                                Integer rows) {
        StringBuffer sql = new StringBuffer();
        if(CollectionUtils.isEmpty(activityIds)){
            sql.append(
                    " select m.id,m.name,m.price,m.image,m.type_id as typeId,m.merchant_id as merchantId,m.create_time as createTime,m.buy_count as buyCount,m.intro,m.print_type as printType,"
                            + "m.display,m.repertory,m.today_repertory as todayRepertory,m.begin_time as beginTime,m.update_time as updateTime,m.end_time as endTime,m.limit_today limitToday,m.is_delete isDelete,m.menu_sort as menuSort,"
                            + "m.price_online as priceOnline,m.isonline,m.barcode from menu m  where 1=1");
        } else {
            sql.append(
                    " select m.id,m.name,m.price,m.image,m.type_id as typeId,m.merchant_id as merchantId,m.create_time as createTime,m.buy_count as buyCount,m.intro,m.print_type as printType,"
                            + "m.display,m.repertory,m.today_repertory as todayRepertory,m.begin_time as beginTime,m.update_time as updateTime,m.end_time as endTime,m.limit_today limitToday,m.is_delete isDelete,m.menu_sort as menuSort,"
                            + "m.price_online as priceOnline,m.isonline,m.barcode, pm.promotion_price as promotionPrice from menu m LEFT JOIN promotion_menu pm ON m.id = pm.menu_id and pm.promotion_quantity > 0 and pm.promotion_activity_id in (" + StringUtils.join(activityIds, ",") + ")  where 1=1");        	
        }
        if (StringUtil.isNotEmpty(menu.getId())) {
            sql.append(" and m.id = ");
            sql.append(menu.getId());
        }
        if (StringUtil.isNotEmpty(menu.getName())) {
            sql.append(" and m.name like ");
            sql.append("'");
            sql.append(menu.getName());
            sql.append("%'");
        }
        if (StringUtil.isNotEmpty(menu.getPrice())) {
            sql.append(" and m.price = ");
            sql.append("'");
            sql.append(menu.getPrice());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getImage())) {
            sql.append(" and m.image = ");
            sql.append(menu.getImage());
        }
        if (StringUtil.isNotEmpty(menu.getTypeId())) {
            sql.append(" and m.type_id = ");
            sql.append(menu.getTypeId());
        }
        if (StringUtil.isNotEmpty(menu.getMerchantId())) {
            sql.append(" and m.merchant_id = ");
            sql.append(menu.getMerchantId());
        }
        if (StringUtil.isNotEmpty(menu.getCreateTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(m.create_time)) = ");
            sql.append("'");
            sql.append(menu.getCreateTime());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getBuyCount())) {
            sql.append(" and m.buy_count = ");
            sql.append("'");
            sql.append(menu.getBuyCount());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getIntro())) {
            sql.append(" and m.intro = ");
            sql.append("'");
            sql.append(menu.getIntro());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getPrintType())) {
            sql.append(" and m.print_type = ");
            sql.append("'");
            sql.append(menu.getPrintType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getDisplay())) {
            sql.append(" and m.display = ");
            sql.append("'");
            sql.append(menu.getDisplay());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getRepertory())) {
            sql.append(" and m.repertory = ");
            sql.append("'");
            sql.append(menu.getRepertory());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getTodayRepertory())) {
            sql.append(" and m.today_repertory = ");
            sql.append("'");
            sql.append(menu.getTodayRepertory());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getBeginTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(m.begin_time) = ");
            sql.append("'");
            sql.append(menu.getBeginTime());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getEndTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(m.end_time) = ");
            sql.append(menu.getEndTime());
        }
        if (StringUtil.isNotEmpty(menu.getLimitToday())) {
            sql.append(" and m.limit_today = ");
            sql.append(menu.getLimitToday());
        }
        if (StringUtil.isNotEmpty(menu.getIsDelete())) {
            sql.append(" and m.is_delete = ");
            sql.append("'");
            sql.append(menu.getIsDelete());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(menu.getMenuSort())) {
            sql.append(" and m.menu_sort = ");
            sql.append(menu.getMenuSort());
        }
        if (StringUtil.isNotEmpty(menu.getPriceOnline())) {
            sql.append(" and m.price_online = ");
            sql.append(menu.getPriceOnline());
        }
        if (StringUtil.isNotEmpty(menu.getIsonline())) {
            sql.append(" and m.isonline in(");
            sql.append(menu.getIsonline());
            sql.append(")");
        }
        if (StringUtil.isNotEmpty(menu.getBarcode())) {
            sql.append(" and m.barcode like ");
            sql.append("'");
            sql.append(menu.getBarcode());
            sql.append("%'");
        }

        if (menu.getUpdateTime() != null && menu.getUpdateTime() > 0) {
            sql.append(" and m.update_time >= ");
            sql.append(menu.getUpdateTime());
        }
        sql.append(" order by m.menu_sort asc ");

        List<com.wm.entity.menu.MenuVo> list = new ArrayList<com.wm.entity.menu.MenuVo>();
        if (page != null && rows != null) {
            list = this.findObjForJdbc(sql.toString(), page, rows, com.wm.entity.menu.MenuVo.class);
        } else {
            list = this.findObjForJdbc(sql.toString(), com.wm.entity.menu.MenuVo.class);
        }
        Dialect dialect = new Dialect();
        String countSql = dialect.getCountString(sql.toString());
        List<Map<String, Object>> countList = this.findForJdbc(countSql);
        Map<String, Object> map = countList.get(0);
        String count = map.get("count").toString();
        PageList<com.wm.entity.menu.MenuVo> pageList = new PageList<com.wm.entity.menu.MenuVo>(rows, page,
                Integer.parseInt(count), list);

        List<com.wm.entity.menu.MenuVo> result = pageList.getResultList();
        if (CollectionUtils.isNotEmpty(result)) {
            for (com.wm.entity.menu.MenuVo menuVo : result) {
                if (menuVo.getName() != null) {
                    menuVo.setPinyin(PinyinHelper.convertToPinyinString(menuVo.getName(), "", PinyinFormat.WITHOUT_TONE));
                    menuVo.setFirstLetter(PinyinHelper.getShortPinyin(menuVo.getName()));
                } else {
                    logger.warn("商品名为空, menuVo:{}", JSON.toJSONString(menuVo));
                }

            }
        }
        return pageList;
    }

    @Override
    public PageList<com.wm.entity.menu.MenuVo> findByPromotionEntityList(com.wm.entity.menu.MenuVo menu,
    							List<Integer> activityIds, Integer page,Integer rows) {
    	if(CollectionUtils.isEmpty(activityIds)){
    		return null;
    	}
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ")
	        .append("m.id,")
	        .append("m.name,")
	        .append("m.price,")
	        .append("m.image,")
	        .append("m.type_id AS typeId,")
	        .append("m.merchant_id AS merchantId,")
	        .append("m.create_time AS createTime,")
	        .append("m.buy_count AS buyCount,")
	        .append("m.intro,")
	        .append("m.print_type AS printType,")
	        .append("m.display,")
	        .append("m.repertory,")
	        .append("m.today_repertory AS todayRepertory,")
		    .append("m.begin_time AS beginTime,")
	        .append("m.update_time AS updateTime,")
	        .append("m.end_time AS endTime,")
	        .append("m.limit_today limitToday,")
	        .append("m.is_delete isDelete,")
	        .append("m.menu_sort AS menuSort,")
	        .append("m.price_online AS priceOnline,")
	        .append("m.isonline,")
	        .append("m.barcode,")
	        .append("pm.promotion_price as promotionPrice ")
	        .append("FROM ")
			    .append("promotion_menu pm,")
		        .append("menu m ")
		    .append("where ")
		        .append("pm.menu_id = m.id and pm.promotion_quantity > 0 ")
		        .append("and pm.promotion_activity_id in (" + StringUtils.join(activityIds, ",") + ") ");    
        if (StringUtil.isNotEmpty(menu.getMerchantId())) {
            sql.append("and pm.merchant_id = ");
            sql.append(menu.getMerchantId());
        }

        List<com.wm.entity.menu.MenuVo> list = new ArrayList<com.wm.entity.menu.MenuVo>();
        if (page != null && rows != null) {
            list = this.findObjForJdbc(sql.toString(), page, rows, com.wm.entity.menu.MenuVo.class);
        } else {
            list = this.findObjForJdbc(sql.toString(), com.wm.entity.menu.MenuVo.class);
        }
        Dialect dialect = new Dialect();
        String countSql = dialect.getCountString(sql.toString());
        List<Map<String, Object>> countList = this.findForJdbc(countSql);
        Map<String, Object> map = countList.get(0);
        String count = map.get("count").toString();
        PageList<com.wm.entity.menu.MenuVo> pageList = new PageList<com.wm.entity.menu.MenuVo>(rows, page,
                Integer.parseInt(count), list);

        List<com.wm.entity.menu.MenuVo> result = pageList.getResultList();
        if (CollectionUtils.isNotEmpty(result)) {
            for (com.wm.entity.menu.MenuVo menuVo : result) {
                if (menuVo.getName() != null) {
                    menuVo.setPinyin(PinyinHelper.convertToPinyinString(menuVo.getName(), "", PinyinFormat.WITHOUT_TONE));
                    menuVo.setFirstLetter(PinyinHelper.getShortPinyin(menuVo.getName()));
                } else {
                    logger.warn("商品名为空, menuVo:{}", JSON.toJSONString(menuVo));
                }

            }
        }
        return pageList;
    }
    
    @Override
    public com.wm.entity.menu.MenuVo searchItemByBarcode(Integer merchantId, String barcode) {
        String sql = "select id, name, price, image, type_id as typeId, merchant_id as merchantId, create_time as createTime, buy_count as buyCount, intro, print_type as printType,"
                + "display, repertory, today_repertory as todayRepertory, begin_time as beginTime, end_time as endTime, limit_today limitToday, is_delete isDelete, menu_sort as menuSort,"
                + "price_online as priceOnline, isonline, barcode from menu where barcode = ? and merchant_id = ?";
        List<com.wm.entity.menu.MenuVo> menuVos = findObjForJdbc(sql, com.wm.entity.menu.MenuVo.class,
                new Object[]{barcode, merchantId});
        return (menuVos == null || menuVos.isEmpty()) ? null : menuVos.get(0);
    }

    @Override
    public void initRep() {
        List<Map<String, Object>> meIdList = findForJdbc(findNeedResetRespMerchIds);
        if (CollectionUtils.isNotEmpty(meIdList)) {
            logger.info("initRep total size:{}", meIdList.size());
            for (Map<String, Object> map : meIdList) {
                Integer merchantId = Integer.parseInt(map.get("id").toString());
                try {
                    executeSql(updateResp, merchantId);
                    logger.info("更新库存完成，merchantId:{}", merchantId);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("更新库存失败！！！merchantId:{}", merchantId);
                }
            }
        }
        /*
         * List<Map<String, Object>> menuList = findForJdbc(
		 * "select id, merchant_id from menu");
		 * if(CollectionUtils.isNotEmpty(menuList)){ for(Map<String, Object> map
		 * : menuList){ try { logger.info("查询菜品menuId：{},商家id:{}的类型",
		 * map.get("id"), map.get("merchant_id")); String groupId =
		 * findOneForJdbc("select group_id from merchant where id = ?",
		 * String.class, map.get("merchant_id"));
		 * logger.info("更新库存,查询group_id:{}", groupId); //超市订单库存 不更新
		 * groupId=511表超市 if(!"511".equals(groupId)){ String sql =
		 * "update menu m set m.today_repertory=m.repertory where m.id = ?";
		 * executeSql(sql, Integer.parseInt(map.get("id").toString()));
		 * logger.info("更新库存，menu_id:{}", map.get("id").toString()); } } catch
		 * (Exception e) { e.printStackTrace(); } } }
		 */
    }

    public void doAddMenus(List<SupplyChainMenuVo> menus, Integer merchantId, Integer mainOrderId) {
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        for (SupplyChainMenuVo supplyChainMenuVo : menus) {
            addMenuRepertory(merchantId, supplyChainMenuVo);
        }
        this.basicPosOrderService.doComfortOrder(merchantId, mainOrderId, menus);
    }

    public void addMenuRepertory(Integer merchantId, SupplyChainMenuVo menuVo) {
        logger.info("addMenuRepertory -> merchantId = " + merchantId + " ,menuVo " + JSONObject.toJSONString(menuVo));
        try {

            if (addExistsMenuRepertory(merchantId, menuVo.getBarcode(), menuVo.getNumber())) return;
            MenuEntity menu;

            String typeName = "新入库商品";
            List<MenutypeEntity> menutypes = this.menutypeService
                    .findHql("from " + MenutypeEntity.class.getName() + " where name = ? ", typeName);
            MenutypeEntity menutypeEntity;
            if (CollectionUtils.isEmpty(menutypes)) {
                menutypeEntity = new MenutypeEntity();
                menutypeEntity.setCostLunchBox(0D);
                menutypeEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
                menutypeEntity.setMerchantId(merchantId);
                menutypeEntity.setName(typeName);
                menutypeEntity.setSortNum(0);

                this.menutypeService.save(menutypeEntity);
            } else {
                menutypeEntity = menutypes.get(0);
            }
            menu = new MenuEntity();
            menu.setBarcode(menuVo.getBarcode());
            menu.setName(menuVo.getName());
            menu.setImage(menuVo.getImg());
            menu.setMenuType(menutypeEntity);
            menu.setRepertory(menuVo.getNumber());
            menu.setPrice(menuVo.getMarketPrice());
            menu.setPriceOnline(menuVo.getMarketPrice());
            menu.setMerchantId(merchantId);
            menu.setDisplay("N");
            menu.setIsSync(0);
            // 默认值 待完善
            menu.setBuyCount(0);
            menu.setTodayRepertory(0);
            menu.setBeginTime(0);
            menu.setCreateTime((int) (System.currentTimeMillis() / 1000));
            menu.setSyncTime((int) (System.currentTimeMillis() / 1000));
            menu.setUpdateTime((int) (System.currentTimeMillis() / 1000));
            save(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addExistsMenuRepertory(Integer merchantId, String barcode, Integer num) {
        List<MenuEntity> menus = findHql(
                "from " + MenuEntity.class.getName() + " where merchantId = ? and barcode = ? ", merchantId,
                barcode);
        MenuEntity menu = null;
        if (!CollectionUtils.isEmpty(menus)) {
            menu = menus.get(0);
            // 库存菜品数量减去销量
            if (menu.getTodayRepertory() == null) {
                menu.setTodayRepertory(0);
            }

            Integer number = menu.getTodayRepertory() + num;
            if (number <= 0) {
                logger.warn("menu repertory is not enough !!! menuId={}", menu.getId());
                number = 0;
            }
            logger.info("add todayRepertory : num : " + num + " , perTodayRepertory :" + menu.getRepertory() + " , number : " + number);
            menu.setTodayRepertory(number);

            number = menu.getRepertory() + num;
            if (number <= 0) {
                logger.warn("menu repertory is not enough !!! menuId={}", menu.getId());
                number = 0;
            }
            logger.info("add repertory : num : " + num + " , perRepertory :" + menu.getRepertory() + " , number : " + number);
            menu.setRepertory(number);
            menu.setUpdateTime((int) (System.currentTimeMillis() / 1000));
            if (menu.getSyncTime() == null) {
                menu.setSyncTime((int) (System.currentTimeMillis() / 1000));
            }
            updateEntitie(menu);
            return true;
        }
        return false;
    }

    @Override
    public void revertRepertory(int orderId) {
        String sql = "select m.menu_id,m.quantity from order_menu m where m.order_id=?";
        List<Map<String, Object>> menuList = this.findForJdbc(sql, orderId);

        if (menuList.size() > 0) {
            for (int i = 0; i < menuList.size(); i++) {
                Map<String, Object> maptwo = menuList.get(i);
                Integer menuId = Integer.parseInt(maptwo.get("menu_id").toString());
                Integer quantity = Integer.parseInt(maptwo.get("quantity").toString());
                MenuEntity menu = this.get(MenuEntity.class, menuId);
                if (menu != null) {
                    int number = menu.getTodayRepertory();// 修改今日库存
                    menu.setTodayRepertory(number + quantity);
                    menu.setUpdateTime((int) (System.currentTimeMillis() / 1000));
                    this.saveOrUpdate(menu);
                }
            }
        }
    }

    /**
     * 更新商品信息
     *
     * @param menuVo
     */
    @Override
    public void updateMenu(com.wm.entity.menu.MenuVo menuVo) {
        MenuEntity menu = this.get(MenuEntity.class, menuVo.getId());
        if (menuVo.getTodayRepertory() != null) {
            menu.setTodayRepertory(menuVo.getTodayRepertory());
        }
        if (menuVo.getName() != null) {
            menu.setName(menuVo.getName());
        }
        if (menuVo.getBarcode() != null) {
            menu.setBarcode(menuVo.getBarcode());
        }
        if (menuVo.getPrice() != null) {
            menu.setPrice(menuVo.getPrice());
        }
        menu.setUpdateTime((int) (System.currentTimeMillis() / 1000));
        this.updateEntitie(menu);
    }

    @Override
    public List<Map<String, Object>> getAnotherMenuByBarcode(com.wm.entity.menu.MenuVo menuVo) {
        logger.info("barcode:{}, name:{}", menuVo.getBarcode(), menuVo.getName());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (menuVo.getBarcode() == null && menuVo.getName() == null) {
            return list;
        }
        StringBuilder query = new StringBuilder();
        query.append(" select m.id , m.name, m.price, m.image as picUrl, m.buy_count as buyCount, m.type_id as typeId,");
        query.append(" m.today_repertory as todayRepertory, m.barcode from menu m");
        query.append(" where m.merchant_id = ?  and m.id <> ? and m.is_delete = 'N'");
        if (StringUtil.isNotEmpty(menuVo.getBarcode()) && StringUtil.isNotEmpty(menuVo.getName())) {
            query.append(" and (m.barcode = ? and m.name = ?)");
            list = findForJdbc(query.toString(), menuVo.getMerchantId(), menuVo.getId(), menuVo.getBarcode(), menuVo.getName());
        } else if (StringUtil.isNotEmpty(menuVo.getBarcode()) && StringUtil.isEmpty(menuVo.getName())) {
            query.append(" and m.barcode = ? ");
            list = findForJdbc(query.toString(), menuVo.getMerchantId(), menuVo.getId(), menuVo.getBarcode());
        } else if (StringUtil.isEmpty(menuVo.getBarcode()) && StringUtil.isNotEmpty(menuVo.getName())) {
            query.append(" and  m.name = ?");
            list = findForJdbc(query.toString(), menuVo.getMerchantId(), menuVo.getId(), menuVo.getName());
        }
        return list;
    }

    @Override
    public List<MarketingReportLine> marketingReport(String merchantId, String typeId, String searchKey, String startTime, String endTime, Integer num, Integer pageSize) {
        // m.id, m.name ,m.barcode ,m.type_id  ,sum(om.quantity) total_quantity , SUM(om.total_price) total_price
        long start = 0;
        long end = System.currentTimeMillis() / 1000;
        if (!com.wm.util.StringUtil.isEmpty(startTime, endTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                start = sdf.parse(startTime).getTime() / 1000;
                end = sdf.parse(endTime).getTime() / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Calendar todayStart = Calendar.getInstance();
            todayStart.set(Calendar.HOUR, 0);
            todayStart.set(Calendar.MINUTE, 0);
            todayStart.set(Calendar.SECOND, 0);
            todayStart.set(Calendar.MILLISECOND, 0);
            start = todayStart.getTime().getTime() / 1000;
        }
        Double totalOrderMoney = this.orderService.getTotalMoney(Long.valueOf(merchantId), start, end);
        if (totalOrderMoney == null || totalOrderMoney == 0) {
            return Collections.EMPTY_LIST;
        }
        List<Map<String, Object>> mapList = this.menuDao.getTopSales(merchantId, typeId, searchKey, (int) start, (int) end, num, pageSize);
        if (CollectionUtils.isEmpty(mapList)) {
            return Collections.EMPTY_LIST;
        }
        List<Long> ids = new LinkedList<>();
        List<MarketingReportLine> lines = new LinkedList<>();

        Map<String, Object> map;
        MarketingReportLine line = null;
        {
            for (int i = 0; i < mapList.size(); i++) {
                map = mapList.get(i);
                ids.add(Long.valueOf(map.get("id") + ""));

                line = new MarketingReportLine();

                BigDecimal selectedMoney = new BigDecimal(map.get("total_price") + "").setScale(2, BigDecimal.ROUND_HALF_UP);

                line.setMoneyRatio(selectedMoney.divide(BigDecimal.valueOf(totalOrderMoney), 4, BigDecimal.ROUND_HALF_UP).toString());
                line.setPosition(i + 1);
                line.setMenuId(map.get("id") + "");
                line.setMenuName(map.get("name") + "");
                line.setBarcode(map.get("barcode") + "");
                line.setMenuTypeId(map.get("type_id") + "");
                line.setTotalSales(Long.valueOf(map.get("total_quantity") + ""));
                line.setTotalMoney(selectedMoney.toString());
                lines.add(line);
            }
        }

        //tm.id , tm.name , tm.barcode , tm.type_id , s.stock_price , mt.`name` typeName


        List<Map<String, Object>> stockPrices = this.menuDao.getStockPrice(ids);
        Map<String, Map<String, Object>> stockPriceMap = new HashMap<>();

        for (int i = 0; i < stockPrices.size(); i++) {
            map = stockPrices.get(i);
            stockPriceMap.put(map.get("id") + "", map);
        }

        for (MarketingReportLine tline : lines) {
            map = stockPriceMap.get(tline.getMenuId());
            tline.setGrossMargin("0");
            tline.setGrossProfit("0");
            tline.setMenuTypeName("");
            if (map != null && !map.isEmpty()) {
                tline.setMenuTypeName(map.get("typeName") == null ? "" : map.get("typeName") + "");
                if (map.get("stock_price") == null) {
                    continue;
                }
                double grossMargin = Double.parseDouble(tline.getTotalMoney()) - tline.getTotalSales() * Double.parseDouble(map.get("stock_price") + "");
                double grossProfit = grossMargin / Double.parseDouble(tline.getTotalMoney());

                tline.setGrossMargin(BigDecimal.valueOf(grossMargin).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                tline.setGrossProfit(BigDecimal.valueOf(grossProfit).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        }
        return lines;
    }
}
