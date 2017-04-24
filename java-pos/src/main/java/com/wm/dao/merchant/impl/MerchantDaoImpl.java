package com.wm.dao.merchant.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.wm.controller.takeout.dto.WXHomeDTO;
import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.controller.takeout.vo.OrderSimpleVo;
import com.wm.dao.merchant.MerchantDao;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantStatisticsMenuVo;
import com.wm.util.Dialect;
import com.wm.util.PageList;

@Repository("merchantDao")
@SuppressWarnings("unchecked")
public class MerchantDaoImpl extends GenericBaseCommonDao<MerchantEntity, Integer> implements MerchantDao{
	
	@Value("${serviceScope}")
	private String serviceScope;

	@Override
	public List<MerchantSimpleVo> findByLocation(int cityId, WXHomeDTO wx) {
		Double precision = Double.parseDouble(serviceScope);
		String sql =  "select merc.id as merchantId, "
					+ "merc.title as name, "
					+ "merc.logo_url as logo, "
					+ "merc.longitude as lng, "
					+ "merc.latitude as lat, "
					+ "merc.promotion as promotion, "
					+ "merc.delivery_begin as deliveryPrice, "
					+ "sec_to_time(merc.start_time) as start, "
					+ "sec_to_time(merc.end_time) as end, "
					+ "sec_to_time(merc.delivery_time) as delivery, "
					+ "curtime() as nowTime, "
					+ "cate.name as type "
					+ "from (select * "
					+ "from merchant as mer "
					+ "where mer.city_id = ? and mer.display = ? and abs(mer.longitude - ?) < ? and abs(mer.latitude - ?) < ? ";
		//筛选店铺类型
		if(wx.getGroup() > 0) {
			sql += " and mer.group_id = " + wx.getGroup();
		}
		//是否促销
		if(wx.getPromote() == 2) {
			sql += " and mer.promotion = \"Y\" ";
		}
					
				sql += " ) as merc "
					+ "left join category as cate "
					+ "on merc.group_id = cate.id ";
		SQLQuery query = createSqlQuery(sql, cityId, "Y", wx.getLng(), precision, wx.getLat(), precision);
		//暂时取消分页
		//query.setFirstResult(wx.getStart()).setMaxResults(wx.getRows());
		query.setResultTransformer(Transformers.aliasToBean(MerchantSimpleVo.class));
		return query.list();
	}

	@Override
	public OrderSimpleVo lasetOrder(Integer userId) {
		String sql = "select o1.id as orderId, o1.access_time as accessTime, o1.user_address_id as addressId, "
				+ "o1.delivery_time as deliveryTime, o1.state as orderState, o1.sale_type as saleType, "
				+ "mer.title as merchantName, mer.logo_url as merchantLogo, o1.order_num as orderNum, "
				+ "mer.longitude as lng, mer.latitude as lat, mer.mobile as merchantMobile, u.username as courier, "
				+ "u.mobile as courierMobile, u.photoUrl as courierIcon "
				+ "from (select * "
				+ "from `order` as o "
				+ "where o.user_id = ? and o.state in('accept','delivery') and o.order_type = 'normal' "
				+ "order by o.pay_time desc limit 0,1) as o1 "
				+ "left join  merchant as mer "
				+ "on o1.merchant_id = mer.id "
				+ "left join user as u "
				+ "on o1.courier_id = u.id";
		
		SQLQuery query = createSqlQuery(sql, userId);
		query.setResultTransformer(Transformers.aliasToBean(OrderSimpleVo.class));
		Object obj = query.uniqueResult();
		if(null != obj)
			return (OrderSimpleVo) obj;
		return null;
	}

	@Override
	public List<MerchantSimpleVo> queryUserFavMerchantByUserId(Integer userId, int page, int rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select merc.id as merchantId, ")
			.append(" merc.title as name, ")
			.append(" merc.logo_url as logo, ")
			.append(" merc.longitude as lng, ")
			.append(" merc.latitude as lat, ")
			.append(" merc.promotion as promotion, ")
			.append(" merc.delivery_begin as deliveryPrice, ")
			.append(" sec_to_time(merc.start_time) as start, ")
			.append(" sec_to_time(merc.end_time) as end, ")
			.append(" sec_to_time(merc.delivery_time) as delivery, ")
			.append(" curtime() as nowTime, ")
			.append(" cate.name as type ")
			.append(" from ")
			.append("     (select  mer.*  from ")
			.append(".merchant as mer, ")
			.append(".favorites as fav ")
			.append("	   where mer.id = fav.item_id and fav.userid = ?) as merc ")
			.append(" left join ")
			.append(".category as cate on merc.group_id = cate.id ");
		SQLQuery query = createSqlQuery(sbsql.toString(), userId);
		query.setFirstResult((page-1)*rows);
		query.setMaxResults(rows);
		
		
//		String pageSql = JdbcDao.jeecgCreatePageSql(sbsql.toString(), page,rows);	
//		List<Map<String, Object>> listRes = this.findForJdbc(pageSql, userId);
//		List<MerchantSimpleVo> result = MyBeanUtils.convertMap2List(listRes, MerchantSimpleVo.class);
		query.setResultTransformer(Transformers.aliasToBean(MerchantSimpleVo.class));
		return query.list();
	}
	
	@Override
	public MerchantEntity queryByMenuId(Integer menuId) {
		String hql = "select mer from MerchantEntity mer, MenuEntity m where mer.id = m.merchantId and m.id = ?";
		Query query = createHqlQuery(hql, menuId);
		Object obj = query.uniqueResult();
		if(null != obj)
			return (MerchantEntity) obj;
		return null;
	}
	
	@Override
	public PageList<MerchantStatisticsMenuVo> statisticsMenu(String title, String startTime,
			String endTime, Integer page, Integer rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select mer.title,men.`name`,men.buy_count as buyCount from merchant mer,menu men where mer.id = men.merchant_id");
		if (StringUtil.isNotEmpty(title)) {
			sbsql.append(" AND mer.title like '%" + title + "%'");
		}
		if (StringUtil.isNotEmpty(startTime)) {
			sbsql.append(" AND DATE(FROM_UNIXTIME(men.create_time)) >= '" + startTime + "'");
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sbsql.append(" AND DATE(FROM_UNIXTIME(men.create_time)) <= '" + endTime + "'");
		}
		
		sbsql.append(" ORDER BY men.buy_count DESC");
		List<MerchantStatisticsMenuVo> list = this.findObjForJdbc(
				sbsql.toString(), page, rows, MerchantStatisticsMenuVo.class);
		Dialect dialect = new Dialect();
		String countSql = dialect.getCountString(sbsql.toString());
		List<Map<String, Object>> countList = this.findForJdbc(countSql);
		Map<String, Object> map = countList.get(0);
		String count = map.get("count").toString();
		PageList<MerchantStatisticsMenuVo> pageList = new PageList<MerchantStatisticsMenuVo>(rows, page, Integer.parseInt(count), list);
		return pageList;
	}
}
