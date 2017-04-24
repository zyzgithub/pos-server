package com.wm.service.provider.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.jeecgframework.core.common.dao.ICommonDao;
import org.springframework.stereotype.Service;

import com.wm.controller.takeout.vo.OrderVo;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.provider.ProviderCategoryService;

@Service
public class ProviderCategoryServiceImpl implements ProviderCategoryService {
	@Resource
	public ICommonDao commonDao = null;
	@Resource
	private OrderServiceI orderService;

	@Override
	public List<Map<String, Object>> listCategoryProviderName(int type, int pageNo, int maxPageSize) {
		String sql = "select m.title,m.id from 0085_provider_category p , merchant m where p.group_id = :group_id and  p.merchant_id = m.id limit :fristResult ,:limit ";

		SQLQuery sqlQuery = commonDao.getSession().createSQLQuery(sql);
		sqlQuery.setInteger("group_id", type);
		sqlQuery.setInteger("fristResult", PageUtil.getFristResult(pageNo, maxPageSize));
		sqlQuery.setInteger("limit", maxPageSize);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> names = sqlQuery.list();
		return names;
	}

	@Override
	public Long countCategoryProviderName(int type) {
		String sql = "select  count(*) from 0085_provider_category p  where p.group_id = :group_id   ";
		SQLQuery sqlQuery = commonDao.getSession().createSQLQuery(sql);
		sqlQuery.setInteger("group_id", type);
		Long l = Long.parseLong(sqlQuery.uniqueResult() + "");
		return l;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> listMenuType(int merchantId) {
		String sql = "select id,name from menu_type where merchant_id = :merchantId order by sort_num ";
		SQLQuery sqlQuery = commonDao.getSession().createSQLQuery(sql);
		sqlQuery.setInteger("merchantId", merchantId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	@Override
	public Object pageMenu(final Integer merchantId, final Integer groupId, final String groupName, final int pageNo,
			final int maxPageSize) {

		DoBussiness bussiness = new DoBussiness() {

			@Override
			protected void doBefore() {
				super.doBefore();
				result.put("category", Collections.EMPTY_LIST);
				result.put("notice", "");
				assertNotNull(merchantId, "merchantId 参数不能为空");
				MerchantEntity entity = commonDao.get(MerchantEntity.class, merchantId);
				assertNotNull(entity, "merchantId 参数不正确");
				// 商家支持的销售类型（1=都支持,2=外卖,3=堂食）
				String sql = "select support_sale_type from 0085_merchant_info where merchant_id =  ?";
				Map<String, Object> info = commonDao.findOneForJdbc(sql, merchantId);
				if (!info.isEmpty()) {
					result.put("supportSaleType", info.get("support_sale_type"));
				}
				result.put("notice", entity.getNotice());
			}

			@Override
			protected void doRun() {
				// 获取列表
				List<Map<String, Object>> muneTypes = listMenuType(merchantId);
				if (muneTypes == null || muneTypes.isEmpty()) {
					return;
				}
				result.put("category", muneTypes);

				// 初始化 需要查询的组 Id,如果没有默认使用第一个
				String gName = groupName;
				Integer gid = groupId;
				if (groupId == null) {
					gid = Integer.valueOf(muneTypes.get(0).get("id") + "");
					gName = (String) muneTypes.get(0).get("name");
				}
				int totalRows = countMenu(merchantId, gid);
				if (totalRows == 0) {
					return;
				}
				/*
				 * category:'饮料酒水', // 类型名 pageNo:1, // 当前页码 maxPageSize:30, //
				 * 最大叶宽 maxPageNo:200, // 最大页数
				 */
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("category", gName);
				map.put("pageNo", pageNo);
				map.put("pageSize", maxPageSize);
				map.put("maxPageNo", 0);
				map.put("data", Collections.EMPTY_LIST);

				map.put("maxPageNo", PageUtil.totalPage(totalRows, maxPageSize));
				map.put("data", listMenu(merchantId, gid, pageNo, maxPageSize));

				result.put("data", map);

			}
		};

		return bussiness.run();

	}

	@Override
	public List<ProductEntity> listMenu(int merchantId, Integer groupId, int pageNo, int maxPageSize) {
		String sql = "select image ,`name` ,price ,unit,id  from menu  where display = 'Y' and merchant_id = :merchantId and type_id = :typeId  limit :firstResult , :maxPageSize ";
		SQLQuery sqlQuery = commonDao.getSession().createSQLQuery(sql);
		sqlQuery.setInteger("merchantId", merchantId);
		sqlQuery.setInteger("typeId", groupId);
		sqlQuery.setInteger("firstResult", PageUtil.getFristResult(pageNo, maxPageSize));
		sqlQuery.setInteger("maxPageSize", maxPageSize);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(ProductEntity.class));
		List<ProductEntity> entitys = sqlQuery.list();
		return entitys;
	}

	@Override
	public int countMenu(int merchantId, int groupId) {
		String sql = "select count(*) as totalRows from menu  where display = 'Y' and merchant_id = :merchantId and type_id = :typeId  ";
		SQLQuery sqlQuery = commonDao.getSession().createSQLQuery(sql);
		sqlQuery.setInteger("merchantId", merchantId);
		sqlQuery.setInteger("typeId", groupId);
		sqlQuery.addScalar("totalRows", new IntegerType());
		return (int) sqlQuery.uniqueResult();
	}

	abstract class DoBussiness {
		protected Map<String, Object> result = new HashMap<String, Object>();

		public DoBussiness() {
			result.put("msg", "请求成功");
			result.put("state", "0000");
		}

		public Object run() {
			try {
				doBefore();
				doRun();
			} catch (IllegalArgumentException e) {
				result.put("msg", e.getMessage());
				result.put("state", "4000");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("msg", "系统繁忙");
				result.put("state", "9999");
			}
			return result;
		}

		protected void assertNotNull(Object obj, String msg) {
			if (obj == null) {
				throw new IllegalArgumentException(msg);
			}
		}

		protected void assertNotEmpty(Collection<?> collection, String msg) {
			if (collection == null || collection.isEmpty()) {
				throw new IllegalArgumentException(msg);
			}
		}

		protected abstract void doRun() throws Exception;

		protected void doBefore() {
		}

	}

	@Override
	public Object pageCategoryProviderName(final Integer groupId, final Integer pageNo, final Integer pageSize) {
		DoBussiness bussiness = new DoBussiness() {
			@Override
			protected void doRun() {
				assertNotNull(groupId, "groupId 参数不能为空");
				Long totalRows = countCategoryProviderName(groupId);
				List<Map<String, Object>> names = listCategoryProviderName(groupId, pageNo, pageSize);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("data", names);
				// result.put("totalRows", totalRows);
				data.put("totalPage", PageUtil.totalPage(totalRows.intValue(), pageSize));
				result.put("data", data);
			}
		};
		return bussiness.run();
	}

	@Override
	public Object pageUserOrder(final Integer userId, final Integer pageNo, final Integer pageSize) {
		DoBussiness bussiness = new DoBussiness() {
			StringBuilder sql = new StringBuilder();

			{
				sql.append("SELECT ");
				sql.append("	o.id, ");
				sql.append("	o.state, ");
				sql.append("	o.create_time, ");
				sql.append("	o.origin, ");
				sql.append("	o.title, ");
				sql.append("	m.title AS merchantTitle, ");
				sql.append("	ifnull( ");
				sql.append("	m.logo_url, ");
				sql.append("	'http://oss.0085.com/weixin/merchant_photo_default.png' ");
				sql.append("	) AS merchantPic ");
				sql.append("FROM ");
				sql.append("	`order` o ");
				sql.append("LEFT JOIN merchant m ON o.merchant_id = m.id ");
				sql.append("LEFT JOIN 0085_merchant_info mi ON o.merchant_id = mi.merchant_id ");
				sql.append("	WHERE ");
				sql.append("	o.user_id = ? ");
				sql.append("	ORDER BY ");
				sql.append("	o.id DESC ");
				sql.append("	limit ? , ?  ");
			}

			@Override
			protected void doRun() {
				SQLQuery sqlQuery = orderService.getSession().createSQLQuery(sql.toString());
				sqlQuery.setInteger(0, userId);
				sqlQuery.setInteger(1, PageUtil.getFristResult(pageNo, pageSize));
				sqlQuery.setInteger(2, pageSize);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> entities = sqlQuery.list();
				Long totalRows = orderService.getCountForJdbcParam("select count(*) from `order` where user_id = ? ",
						new Object[] { userId });

				Map<String, Object> obj = new HashMap<String, Object>();
				obj.put("data", entities);
				obj.put("maxPageNo", PageUtil.totalPage(totalRows.intValue(), pageSize));
				this.result.put("data", obj);
			}
		};

		return bussiness.run();
	}

	@Override
	public Object getOrderDetail(final int orderId, final int userId) {
		DoBussiness b = new DoBussiness() {

			@Override
			protected void doRun() throws IllegalAccessException, InvocationTargetException {
				OrderDetailVo detailVo = new OrderDetailVo();

				OrderEntity entity = orderService.get(OrderEntity.class, orderId);
				OrderVo orderVo = orderService.queryOrderById(orderId, userId);

				BeanUtils.copyProperties(detailVo, detailVo);
				BeanUtils.copyProperties(detailVo, entity);

				detailVo.setMerchantAddress(entity.getMerchant().getAddress());
				detailVo.setMerchantPhone(entity.getMerchant().getPhone());
				detailVo.setMerchantName(entity.getMerchant().getTitle());
				detailVo.setMenuList(orderVo.getMenus());

				result.put("data", detailVo);
			}

		};
		return b.run();
	}

}
