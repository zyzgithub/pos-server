package com.wm.service.provider;

import java.util.List;
import java.util.Map;

import com.wm.service.provider.impl.ProductEntity;

public interface ProviderCategoryService {

	/**
	 * 查询相应类型的服务提供商的数量
	 * 
	 * @param groupId
	 *            组ID
	 * @return
	 */
	Long countCategoryProviderName(int groupId);

	/**
	 * 查询相应ID的服务提供商的商品类型列表
	 * 
	 * @param merchantId
	 *            组ID
	 * @return
	 */
	List<Map<String, Object>> listMenuType(int merchantId);

	/**
	 * 查询相应ID的服务提供商的商品列表
	 * 
	 * @param merchantId
	 *            组ID
	 * @param groupName
	 * @return
	 */
	List<ProductEntity> listMenu(int merchantId, Integer groupId, int pageNo, int maxPageSize);

	/**
	 * 查询相应ID的服务提供商的商品数量
	 * 
	 * @param merchantId
	 *            组ID
	 * @return
	 */
	int countMenu(int merchantId, int groupId);

	/**
	 * 查询相应类型的服务提供商的名字
	 * 
	 * @param groupId
	 *            组 ID
	 * @param pageNo
	 *            页码
	 * @param maxPageSize
	 *            叶宽
	 * @author zhoucong
	 * @return
	 */
	List<Map<String, Object>> listCategoryProviderName(int groupId, int pageNo, int maxPageSize);

	/**
	 * 查询相应ID的服务提供商的商品分页列表
	 * 
	 * @param merchantId
	 *            组ID
	 * @param groupName
	 * @return
	 */
	Object pageMenu(Integer merchantId, Integer groupId, String groupName, int pageNo, int maxPageSize);

	Object pageCategoryProviderName(Integer groupId, Integer pageNo, Integer pageSize);

	Object pageUserOrder(Integer userId, Integer pageNo, Integer pageSize);

	Object getOrderDetail(int orderId, int userId);

}
