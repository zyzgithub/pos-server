package com.wm.dao.menu.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wm.util.DateUtil;
import com.wm.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.controller.takeout.vo.MenuVo;
import com.wm.dao.menu.MenuDao;
import com.wm.entity.menu.MenuEntity;

@Repository("menuDao")
@SuppressWarnings("unchecked")
public class MenuDaoImpl extends GenericBaseCommonDao<MenuEntity, Integer> implements MenuDao {

    @Override
    public List<MenuVo> findByMerchantId(Integer merchantId) {
        String sql = "select m.id as id, m.name as name, m.price as price, "
                + "m.image as picUrl, m.buy_count as buyCount, m.type_id as typeId, "
                + "m.today_repertory as repertory, sec_to_time(pro.promotion_over_hour) as overHour, "
                + "pro.limit_quantity as limitCount, pro.money as money, pro.id as promotion, pro.promotion_quantity as promotionCount "
                + "from (select * "
                + "from menu as me "
                + "where me.merchant_id = ? and me.is_delete = ? and me.display = ? and isonline in (1,3)) as m "
                + "left join menu_promotion as pro "
                + "on m.id = pro.menu_id and pro.promotion = ? and pro.promotion_quantity > 0 and "
                + "from_unixtime(pro.promotion_over_date, '%Y-%m-%d') >= curdate() and from_unixtime(pro.promotion_begin_date, '%Y-%m-%d') <= curdate() and "
                + "sec_to_time(pro.promotion_begin_hour) <= curtime() and sec_to_time(pro.promotion_over_hour) >= curtime() "
                + "order by m.type_id asc,m.menu_sort asc";
        SQLQuery query = createSqlQuery(sql, merchantId, "N", "Y", "Y");
        query.setResultTransformer(Transformers.aliasToBean(MenuVo.class));
        return query.list();
    }

    @Override
    public List<MenuVo> findFirstPageMenuByMerchantId(Integer merchantId, Integer menuTypeId, Integer page, Integer rows) {
        String sql = "select m.id as id, m.name as name, m.price as price, "
                + "m.image as picUrl, m.buy_count as buyCount, m.type_id as typeId, "
                + "m.today_repertory as repertory, sec_to_time(pro.promotion_over_hour) as overHour, "
                + "pro.limit_quantity as limitCount, pro.money as money, pro.id as promotion, pro.promotion_quantity as promotionCount "
                + "from (select * "
                + "from menu as me "
                + "where me.merchant_id = ? ";
        if (null != menuTypeId) sql += "and me.type_id = ? ";
        sql += "and me.is_delete = ? and me.display = ? and isonline in (1,3)) as m "
                + "left join menu_promotion as pro "
                + "on m.id = pro.menu_id and pro.promotion = ? and pro.promotion_quantity > 0 and "
                + "from_unixtime(pro.promotion_over_date, '%Y-%m-%d') >= curdate() and from_unixtime(pro.promotion_begin_date, '%Y-%m-%d') <= curdate() and "
                + "sec_to_time(pro.promotion_begin_hour) <= curtime() and sec_to_time(pro.promotion_over_hour) >= curtime() "
                + "order by m.type_id asc,m.menu_sort asc limit ?,?";
        int start = (page <= 1 ? 0 : page - 1) * rows;
        SQLQuery query = createSqlQuery(sql, merchantId, menuTypeId, "N", "Y", "Y", start, rows);
        query.setResultTransformer(Transformers.aliasToBean(MenuVo.class));
        return query.list();
    }

    @Override
    public int findBuyCount(Integer merchantId) {
        // TODO Auto-generated method stub
        String sql = "select ifnull(sum(m.buy_count), 0) as buyCount  from menu as m where m.merchant_id = ?";
        SQLQuery query = createSqlQuery(sql, merchantId);
        return ((BigDecimal) query.uniqueResult()).intValue();

    }

    @Override
    public MenuVo findById(Integer menuId) {
        // TODO Auto-generated method stub
        String sql = "select m.id as id, m.name as name, m.price as price, "
                + "m.image as picUrl, m.buy_count as buyCount, m.type_id as typeId, "
                + "m.today_repertory as repertory, sec_to_time(pro.promotion_over_hour) as overHour, "
                + "pro.limit_quantity as limitCount, pro.money as money, pro.id as promotion, pro.promotion_quantity as promotionCount "
                + "from (select * "
                + "from menu as me "
                + "where me.id = ? and me.is_delete = ? and me.display = ? ) as m "
                + "left join menu_promotion as pro "
                + "on m.id = pro.menu_id and pro.promotion = ? and pro.promotion_quantity > 0 and "
                + "from_unixtime(pro.promotion_over_date, '%Y-%m-%d') >= curdate() and from_unixtime(pro.promotion_begin_date, '%Y-%m-%d') <= curdate() and "
                + "sec_to_time(pro.promotion_begin_hour) <= curtime() and sec_to_time(pro.promotion_over_hour) >= curtime() ";
        SQLQuery query = createSqlQuery(sql, menuId, "N", "Y", "Y");
        query.setResultTransformer(Transformers.aliasToBean(MenuVo.class));

        return (MenuVo) query.uniqueResult();
    }

    @Override
    public MenuVo findIsPromotionById(Integer menuId, Boolean promotion) {
        // TODO Auto-generated method stub
        if (promotion)
            return findById(menuId);

        String sql = "select m.id as id, m.name as name, m.price as price, "
                + "m.image as picUrl, m.buy_count as buyCount, m.type_id as typeId, "
                + "m.today_repertory as repertory "
                + "from menu as m "
                + "where m.id = ? and m.is_delete = ? and m.display = ? ";
        SQLQuery query = createSqlQuery(sql, menuId, "N", "Y");
        query.setResultTransformer(Transformers.aliasToBean(MenuVo.class));

        return (MenuVo) query.uniqueResult();
    }

    @Override
    public int queryTodayCountWhenPromoting(Integer userId, Integer menuId) {
        // TODO Auto-generated method stub
        // 限购，每人每天限购是否超出限制
        String sql = " select ifnull(sum(m.quantity),0) quantity ";
        sql += " from `order` o, order_menu m ";
        sql += " where o.user_id = ? and o.id = m.order_id and m.menu_id = ? and o.pay_state = ? and DATE(FROM_UNIXTIME(o.pay_time)) = CURDATE()";

        Map<String, Object> map = findOneForJdbc(sql, userId, menuId, "pay");
        BigDecimal sumQuantity = (BigDecimal) map.get("quantity");
        return sumQuantity.intValue();
    }

    @Override
    public Map<String, Object> getMenuInfo(Integer menuId) {
        String sql = "SELECT m.name, m.today_repertory, m.display, m.is_delete, m.price, m.merchant_id from menu m where m.id=?";
        return findOneForJdbc(sql, menuId);
    }

    @Override
    public 	List<Map<String,Object>> getTopSales(String merchantId, String typeId, String searchKey,  Integer startTime, Integer endTime, Integer num, Integer pageSize){
    List<Object> args = new LinkedList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder subQuery = new StringBuilder();
        subQuery.append(" SELECT id FROM `order` WHERE merchant_id = ? AND pay_state = 'pay' AND rstate = 'normal' ");
        args.add(merchantId);

        if (!StringUtil.isEmpty(startTime, endTime)) {
            subQuery.append("and create_time BETWEEN ? and  ? ");
            args.add(startTime);
            args.add(endTime);
        }

        sqlBuilder.append("SELECT m.id, m.name ,m.barcode ,m.type_id  ,sum(om.quantity) total_quantity , SUM(om.total_price) total_price FROM menu m left join  order_menu om  on om.menu_id = m.id WHERE order_id IN( %s )  ");
        if (StringUtils.isNumeric(typeId)) {
            sqlBuilder.append("and m.type_id = ? ");
            args.add(typeId);
        }
        if (!StringUtil.isEmpty(searchKey)) {
            sqlBuilder.append("and ( m.name like ? )");
            args.add("%" + searchKey + "%");
        }
        sqlBuilder.append("GROUP BY om.menu_id order by total_quantity desc  LIMIT ? , ?");

        int fs = (num - 1) * pageSize < 0 ? 0 : (num - 1) * pageSize;
        args.add(fs);
        args.add(pageSize);
        String sql = String.format(sqlBuilder.toString(), subQuery);

        return findForJdbc(sql, args.toArray());
    }

    @Override
    public List<Map<String, Object>> getStockPrice(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Collections.EMPTY_LIST;
        }
        String sql = "select m.* , s.stock_price , mt.`name` typeName from( select tm.id , tm. name , tm.barcode , tm.type_id from menu tm where tm.id in( %s )) m LEFT JOIN 0085_stock s on m.id = s.menu_id LEFT JOIN menu_type mt on m.type_id = mt.id";
        StringBuilder ids = new StringBuilder();
        for (Long id : menuIds) {
            ids.append(id).append(",");
        }
        ids.delete(ids.length() - 1, ids.length());
        return findForJdbc(String.format(sql, ids));
    }

}
