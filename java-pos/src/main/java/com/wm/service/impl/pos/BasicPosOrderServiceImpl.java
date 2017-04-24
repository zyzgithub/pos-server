package com.wm.service.impl.pos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.wm.controller.order.dto.ScanOrderFromPosDTO;
import com.wm.controller.supplychain.SupplyChainMenuVo;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.CashierOrderEntity;
import com.wm.entity.pos.PurchaseDetail;
import com.wm.exception.ApplicationRuntimeException;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.MongoService;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pos.BasicPosOrderServiceI;
import com.wm.util.HttpProxy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @author zhanxinming
 * @version 1.0
 * @date 创建时间：2016年8月16日 上午9:56:00
 * @return
 */
@Service
@Transactional
public class BasicPosOrderServiceImpl extends CommonServiceImpl implements
        BasicPosOrderServiceI {
    @Autowired
    private MongoService mongoService;
    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private MerchantServiceI merchantService;
    private static final Logger logger = LoggerFactory.getLogger(BasicPosOrderServiceImpl.class);

    @Autowired
    private OrderStateServiceI orderStateService;

    @Override
    public ScanOrderFromPosDTO creatScanOrderFromPos(ScanOrderFromPosDTO scanOrderDto) {

        Integer merchantId = scanOrderDto.getMerchantId();
        MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);

        logger.info("创建无码收银订单");
        String sql = "INSERT INTO `order`(user_id,city_id,merchant_id,state, online_money, origin,create_time,order_type,title,sale_type,from_type)"
                + " values(0,?,?,'unpay',?, ?,UNIX_TIMESTAMP(),'supermarket_codefree','超市无码收银订单',2,'pos')";
        this.executeSql(sql, merchant.getCityId(), merchantId, 0, scanOrderDto.getOrigin());

        sql = "select last_insert_id() lastInsertId";
        Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
        Integer orderId = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
        logger.info("========================orderid=" + orderId);

        long time = System.currentTimeMillis();
        String payId = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderId).substring(2);
        logger.info("========================payId=" + payId);

        sql = "update `order` set pay_id=? where id=?";
        this.executeSql(sql, payId, orderId);

        orderStateService.createOrderState(orderId);
        scanOrderDto.setOrderId(orderId);
        logger.info("创建order_menu,menuId为0");
        String o_msql = "insert into order_menu (menu_id, order_id, price, quantity, total_price) values(?, ?, ?, ?, ?)";
        this.executeSql(o_msql, 0, orderId, scanOrderDto.getOrigin(), 1, scanOrderDto.getOrigin());
        if (orderId != null) {
            logger.info("保存收银员与定单的对应关系，cashierId:{}, orderId：{}", scanOrderDto.getCashierId(), orderId);
            //保存收银员与订单ID的对应关系
            CashierOrderEntity cashierOrder = new CashierOrderEntity();
            cashierOrder.setCashierId(scanOrderDto.getCashierId());
            cashierOrder.setOrderId(orderId);
            cashierOrder.setCreateTime(DateUtils.getSeconds());
            this.save(cashierOrder);
        }
        scanOrderDto.setPayId(payId);
        scanOrderDto.setTotalPrice(scanOrderDto.getOrigin());
        scanOrderDto.setTotalCount(1);
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("menuId", 0);
        map.put("name", "无码商品");
        map.put("count", 1);
        map.put("price", scanOrderDto.getOrigin());
        map.put("total", scanOrderDto.getOrigin());
        map.put("salesPromotion", "N");
        map.put("errMsg", "");
        map.put("unit", "份");
        menuList.add(map);
        scanOrderDto.setMenuList(menuList);
        scanOrderDto.setMerchantName(merchant.getTitle());
        return scanOrderDto;
    }

    @Override
    public Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId) throws HttpProxy.HttpAccessException, IOException {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
        // 查询 库存小于 预警库存的商品
        String sql =
                "select om.*,m.* from (select * from menu  where merchant_id = :merchant_id and length(barcode) > 0 and is_delete = 'N' %s ) m\n" +
                        "LEFT JOIN \n" +
                        "(select SUM(om.quantity) as standard_inventory, ceil( SUM(om.quantity)/7 ) as warn_inventory,om.menu_id FROM\n" +
                        "(select * from `order` where merchant_id = :merchant_id and pay_state = 'pay' and rstate = 'normal' AND order_type != 'scan_order' AND create_time > UNIX_TIMESTAMP(DATE_SUB(now(),INTERVAL 7 DAY ))  )o\n" +
                        "left JOIN order_menu om on om.order_id = o.id where menu_id>0    GROUP BY om.menu_id ) om \n" +
                        "ON m.id = om.menu_id \n" +
                        "WHERE om.warn_inventory  >= m.today_repertory OR m.today_repertory = 0 ;";
        // 排查进货中的
        List<String> barcodes = new LinkedList<>();
        // 不再从缓存里面去拿进货中的数据
//                getPurchaseBarcodes(null, userId, null);
        List<MenuEntity> menuEntities;
        List<MenutypeEntity> menutypeEntites = new ArrayList<MenutypeEntity>();
        if (!barcodes.isEmpty()) {

            StringBuilder sb = new StringBuilder();
            for (String barcode : barcodes) {
                sb.append("'" + barcode + "'").append(",");
            }
            sb.delete(sb.length() - 1, sb.length());

            sql = String.format(sql, " and barcode not in ( " + sb + ") ");
//            List<Object> params = new LinkedList<>();
//            params.add(merchantId);
//            params.addAll(barcodes);

        } else {
            sql = String.format(sql, " ");
        }
        SQLQuery sqlQuery = this.commonDao.getSession().createSQLQuery(sql);
        sqlQuery.setInteger("merchant_id", merchantId);
        sqlQuery.addEntity(MenuEntity.class);
        menuEntities = sqlQuery.list();
//        public List<Object> warnInvenstoryList(Integer merchantId, Integer userId) throws HttpProxy.HttpAccessException, IOException {
//        // 查询 库存小于 预警库存的商品
//        String hql = "from " + MenuEntity.class.getName() + " where merchantId = ? and length(barcode) > 0 and  warnInventory > today_repertory and  warnInventory < standardInventory and isDelete = 'N'  ";
//        // 排查进货中的
//        List<String> barcodes =
//                getPurchaseBarcodes(null, userId, null);
//        List<MenuEntity> menuEntities;
//        if (!barcodes.isEmpty()) {
//
//            StringBuilder sb = new StringBuilder();
//            for (String barcode : barcodes) {
//                sb.append("?").append(",");
//            }
//            sb.delete(sb.length() - 1, sb.length());
//
//            hql = hql + " and barcode not in ( " + sb + ")";
//            List<Object> params = new LinkedList<>();
//            params.add(merchantId);
//            params.addAll(barcodes);
//            menuEntities = findHql(hql, params.toArray());
//
//        } else {
//            menuEntities = findHql(hql, merchantId);
//
//        }


//        MerchantEntity merchantEntity = merchantService.getEntity(MerchantEntity.class, merchantId);
//        Integer userId = merchantEntity.getWuser().getId();

        if (CollectionUtils.isEmpty(menuEntities)) {
            return resultMap;
        }

        // 拿出 barcodes
        StringBuilder sb = new StringBuilder();
        Map<String, MenuEntity> menuEntityMap = new HashMap<>();
        for (MenuEntity menuEntity : menuEntities) {
            if (StringUtils.isNotBlank(menuEntity.getBarcode())) {
                sb.append(menuEntity.getBarcode()).append(",");
                menuEntityMap.put(menuEntity.getBarcode(), menuEntity);
            }
            if(null != menuEntity.getMenuType()){
            	if(!menutypeEntites.contains(menuEntity.getMenuType())){
            		menutypeEntites.add(menuEntity.getMenuType());
            	}
            }
        }
        sb = sb.delete(sb.length() - 1, sb.length());

        // 用 barcodes 去 供应链查找可进货的商品,极其规格.
        String url = EnvConfig.base.SUPPLY_ERP_HOST + "/merchantGoodsOpenApi/matchItems";

        HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter("barcodes", sb.toString());
        postMethod.addParameter("userId", userId + "");
        httpClient.executeMethod(postMethod);
        String res = postMethod.getResponseBodyAsString();
//        Map<String, Object> map = new HashMap<>();
//        map.put("barcodes", sb);
//        map.put("userId", "235374");
//        HttpProxy httpProxy = HttpProxy.createInstance(url, Collections.<Header>emptyList(), map);
//        String res = httpProxy.doPost();
//        System.out.println(res);

        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray list = response.getJSONArray("result");
        
        JSONArray externalList = new JSONArray();
        for(MenuEntity menuEntity: menuEntityMap.values()){
        	boolean isNotExists = true;
        	if(CollectionUtils.isNotEmpty(list)){
        		for (Object o : list) {
        			JSONObject jo = (JSONObject) o;
        			if(menuEntity.getBarcode().equals(jo.get("barcode"))){
        				isNotExists = false;
        				break;
        			}
        		}
        	}
        	if(isNotExists){
        		JSONObject json = new JSONObject();
                int todayRepertory = menuEntity.getTodayRepertory() == null ? 0 : menuEntity.getTodayRepertory();
                int standardInventory = menuEntity.getStandardInventory() == null ? 50 : menuEntity.getStandardInventory();
                Integer standard = menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory();
                int need = standardInventory - todayRepertory;
                // 设置当前库存
                json.put("repertory", todayRepertory);
                // 这2个库存是计算出来的. 不是拿它原来的.
                json.put("standardInventory", menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
                json.put("warnInventory", menuEntity.getWarnInventory() == null ? 0 : menuEntity.getWarnInventory());
                json.put("barcode",menuEntity.getBarcode());
                if(null != menuEntity.getMenuType()){
                	json.put("menuTypeId",menuEntity.getMenuType().getId());
                }
                
                JSONArray items = new JSONArray();
                JSONObject itemJson = new JSONObject();
                itemJson.put("standard", standard);
                itemJson.put("image", menuEntity.getImage());
                itemJson.put("unit", menuEntity.getUnit());
                // 设置默认采购数量
                int defaultPurchase = need % standard == 0 ? need / standard : need / standard + 1;
                itemJson.put("defaultPurchase", defaultPurchase);
                itemJson.put("price", menuEntity.getPrice());
                itemJson.put("name", menuEntity.getName());
                itemJson.put("id", menuEntity.getId());
                itemJson.put("stock", todayRepertory);
                items.add(itemJson);
                json.put("items", items);
                
                externalList.add(json);
        	}
        }        
        
        if (CollectionUtils.isNotEmpty(list)) {
        	for (Object o : list) {
        		JSONObject jo = (JSONObject) o;
        		MenuEntity menuEntity = menuEntityMap.get(jo.get("barcode") + "");
        		int todayRepertory = menuEntity.getTodayRepertory() == null ? 0 : menuEntity.getTodayRepertory();
        		int standardInventory = menuEntity.getStandardInventory() == null ? 50 : menuEntity.getStandardInventory();
        		String name = menuEntity.getName();
        		int need = standardInventory - todayRepertory;
        		if (menuEntity == null) {
        			continue;
        		}
        		// 设置当前库存
        		jo.put("repertory", todayRepertory);
        		if(null != menuEntity.getMenuType()){
        			jo.put("menuTypeId",menuEntity.getMenuType().getId());
        		}
        		// 这2个库存是计算出来的. 不是拿它原来的.
        		jo.put("standardInventory", menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
        		jo.put("warnInventory", menuEntity.getWarnInventory() == null ? 0 : menuEntity.getWarnInventory());
        		JSONArray items = jo.getJSONArray("items");
        		if (items != null && !items.isEmpty()) {
        			for (Object item : items) {
        				JSONObject ijo = (JSONObject) item;
        				Integer standard = ijo.getInteger("standard");
        				if (standard == null) {
        					standard = 12;
        					ijo.put("standard", standard);
        				}
        				// 设置默认采购数量
        				int defaultPurchase = need % standard == 0 ? need / standard : need / standard + 1;
        				ijo.put("defaultPurchase", defaultPurchase);
        				ijo.put("name", name);
        			}
        		}
        	}
        }
        resultMap.put("preferentialList", list);
        resultMap.put("externalList", externalList);
        resultMap.put("menutypeList", JSONArray.toJSONString(menutypeEntites));
        return resultMap;
    }


    public static Logger getLogger() {
        return logger;
    }

    @Override
    public List<String> getPurchaseBarcodes(Integer orderId, Integer userId, Integer merchantId) {
        List<PurchaseDetail> purchaseDetais = getPurchaseDetail(orderId, userId, merchantId, getTimeoutTime());
        if (purchaseDetais == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> bars = new LinkedList<>();
        for (PurchaseDetail purchaseDetail : purchaseDetais) {
            bars.addAll(purchaseDetail.getBarcodePurchase().keySet());
        }
        return bars;
    }

    @Override
    public List<PurchaseDetail> getPurchaseDetail(Integer orderId, Integer userId, Integer merchantId, Long createTime) {
        Query query = new Query();
        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        if (orderId != null) {
            query.addCriteria(Criteria.where("orderId").is(orderId));
        }
        if (merchantId != null) {
            query.addCriteria(Criteria.where("merchantId").is(merchantId));
        }
        if (createTime != null) {
            query.addCriteria(Criteria.where("createTime").gte(createTime));
        }
        query.addCriteria(Criteria.where("state").gt(0));
        //
        List<PurchaseDetail> purchaseDetais = this.mongoService.getMongoTemplate().find(query, PurchaseDetail.class, "pos_purchase_detail");

        return purchaseDetais;
    }

    @Override
    public PurchaseDetail getUnUsePurchaseDetail(Integer orderId) {
        Query query = new Query();

        if (orderId != null) {
            query.addCriteria(Criteria.where("orderId").is(orderId));
        }
        query.addCriteria(Criteria.where("state").is(0));
        //
        PurchaseDetail purchaseDetail = this.mongoService.getMongoTemplate().findOne(query, PurchaseDetail.class, "pos_purchase_detail");

        return purchaseDetail;
    }

    @Override
    public void enablePurchaseDetail(Integer orderId) {
        PurchaseDetail purchaseDetail = getUnUsePurchaseDetail(orderId);
        if (purchaseDetail != null && purchaseDetail.getState() == 0) {
            purchaseDetail.setState(1);
            savePurchaseDetail(purchaseDetail);
        }
    }


    @Override
    public void savePurchaseDetail(PurchaseDetail purchaseDetail) {
        //
        this.mongoService.getMongoTemplate().save(purchaseDetail, "pos_purchase_detail");
    }

    @Override
    public Object posTryCreateOrder(Integer userId, String itemInfos, String warehouseInfos, Integer addressId) throws IOException {
        Map<String, Integer> details = new HashMap<>();

        Map<String, String> map = JSONObject.parseObject(itemInfos, Map.class);

        for (String key : map.keySet()) {
            String[] strs = map.get(key).split(";");
            details.put(strs[1], Integer.valueOf(strs[0]));
            map.put(key, strs[0]);
        }

        MerchantEntity merchant = merchantService.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId);
        if (merchant == null) {
            throw new ApplicationRuntimeException(02, "商家不存在");
        }

//        PurchaseDetail purchaseDetail = getPurchaseDetail(userId, details, merchant);

        String username = org.apache.commons.lang.StringUtils.isBlank(merchant.getWuser().getNickname()) ? merchant.getWuser().getUsername() : merchant.getWuser().getNickname();
        JSONObject obj = doPosTryCreateOrder(userId, username, map, warehouseInfos, addressId);
        /**
         *
         "obj": {
         "merchantMoney": "97890.63",
         "origin": "0.04",
         "payId": "20160826142231237650",
         "mainOrderId": 1466
         },
         */
        if (obj != null && obj.getInteger("mainOrderId") != null) {
            Integer mainOrderId = obj.getInteger("mainOrderId");

            PurchaseDetail purchaseDetail = new PurchaseDetail();
            purchaseDetail.setCreateTime(System.currentTimeMillis() / 1000);
            purchaseDetail.setBarcodePurchase(details);
            purchaseDetail.setUserId(userId);
            purchaseDetail.setMerchantId(merchant.getId());
            purchaseDetail.setState(0);
            purchaseDetail.setOrderId(mainOrderId);
            purchaseDetail.setPayId(obj.getString("payId"));
            purchaseDetail.setOrigin(obj.getDouble("origin"));
            savePurchaseDetail(purchaseDetail);
        }

        return obj;
    }

//    private PurchaseDetail getPurchaseDetail(Integer userId, Map<String, Integer> details, MerchantEntity merchant) {
//        PurchaseDetail purchaseDetail = getPurchaseDetail(null, userId, null);
//        if (purchaseDetail == null || isTimeout(purchaseDetail.getCreateTime())) {
//            purchaseDetail = new PurchaseDetail();
//            purchaseDetail.setCreateTime(System.currentTimeMillis() / 1000);
//            purchaseDetail.setBarcodePurchase(details);
//            purchaseDetail.setUserId(userId);
//            purchaseDetail.setMerchantId(merchant.getId());
//        } else {
//            for (String key : details.keySet()) {
//                Integer num = purchaseDetail.getBarcodePurchase().get(key);
//                if (num == null) {
//                    num = details.get(key);
//                } else {
//                    num = details.get(key) + num;
//                }
//                purchaseDetail.getBarcodePurchase().put(key, num);
//                purchaseDetail.setCreateTime(System.currentTimeMillis() / 1000);
//            }
//        }
//        return purchaseDetail;
//    }

    private long getTimeoutTime() {
        return System.currentTimeMillis() / 1000 - 1 * 24 * 60 * 60;

    }

    @Override
    public void doComfortOrder(Integer merchantId, Integer mainOrderId, List<SupplyChainMenuVo> menus) {
        logger.warn("清除采购缓存 merchantId = " + merchantId + " mainOrderId = " + mainOrderId + " menus =" + menus);
        if (mainOrderId == null) {
            return;
        }
        List<PurchaseDetail> purchaseDetails = getPurchaseDetail(mainOrderId, null, null, null);
        if (purchaseDetails == null || CollectionUtils.isEmpty(menus)) {
            return;
        }
        PurchaseDetail purchaseDetail = purchaseDetails.get(0);

        logger.warn("清除采购缓存 purchaseDetail = " + purchaseDetail);
        for (SupplyChainMenuVo menuVo : menus) {
            Integer number = purchaseDetail.getBarcodePurchase().get(menuVo.getBarcode());
            if (number == null) {
                continue;
            }
            number = number - menuVo.getNumber();
            if (number <= 0) {
                purchaseDetail.getBarcodePurchase().remove(menuVo.getBarcode());
            } else {
                purchaseDetail.getBarcodePurchase().put(menuVo.getBarcode(), number);
            }
        }

        purchaseDetail.setState(2);
        savePurchaseDetail(purchaseDetail);

        logger.warn("成功 清除采购缓存 merchantId = " + merchantId);
        logger.warn("成功 清除采购缓存 purchaseDetail = " + purchaseDetail);
    }


    private JSONObject doPosTryCreateOrder(Integer userId, String username, Map<String, String> itemInfos, String warehouseInfos, Integer addressId) throws IOException {
//        if (1 == 1) {
//            JSONObject res = new JSONObject();
//            res.put("code", 0);
//            res.put("msg", "请求成功");
//            JSONObject responseContent = new JSONObject();
//            res.put("response", responseContent);
//
//            responseContent.put("mainOrderId", 123);
//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            // 商家的余额 精确到小数点后两位
//            responseContent.put("merchantMoney", 1231);
//            // 支付标志位
//            responseContent.put("payId", "sfksldfjsodfjiasdjfs");
//            // 该订单的总价
//            responseContent.put("origin", 12312);
//            return res;
//        }

        String url = EnvConfig.base.SUPPLY_MC_HOST + "/supplychainOrderController/posTryCreateOrder.do";
        HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter("username", username);
        postMethod.addParameter("itemInfos", JSONObject.toJSONString(itemInfos));
        postMethod.addParameter("warehouseInfos", warehouseInfos);
        postMethod.addParameter("userId", userId + "");
        if(addressId != null){
        	postMethod.addParameter("addressId", addressId.toString());
        }
        httpClient.executeMethod(postMethod);
        String res = postMethod.getResponseBodyAsString();
        logger.info(" 供应链 doPosTryCreateOrder : " + res);
        JSONObject jo = JSONObject.parseObject(res);
        if (jo.getInteger("code") == 0) {
            JSONObject response = jo.getJSONObject("response");
            if (response.get("deliveryCost") == null || StringUtils.isBlank(response.get("deliveryCost") + "")) {
                response.put("deliveryCost", "0");
            }
            //deliveryCost
            return response;
        } else {
            throw new ApplicationRuntimeException(jo.getInteger("code"), jo.getString("msg"));
        }

    }

}