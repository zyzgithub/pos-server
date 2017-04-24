package com.wm.service.impl.order;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.Constants;
import com.wm.controller.order.dto.OrderFromSupplyDTO;
import com.wm.dao.order.OrderDao;
import com.wm.dao.order.OrderMenuDao;
import com.wm.entity.order.SupplyChainOrderInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.SupplyOrderServiceI;
import com.wm.util.AliOcs;
import com.wm.util.HttpProxy;

@Service("supplyOrderService")
public class SupplyOrderServiceImpl extends CommonServiceImpl implements SupplyOrderServiceI {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderMenuDao orderMenuDao;
	@Autowired
	private OrderServiceI orderService;

	private final static Logger logger = LoggerFactory.getLogger(SupplyOrderServiceImpl.class);

	/**
	 * 创建一条简单的供应链订单
	 * 
	 * @param orderFromSupplyDTO
	 * @param type
	 * @return
	 */
	private Integer createSupplyChainOrder(OrderFromSupplyDTO orderFromSupplyDTO, int type, String payId) {
		// 找到城市ID
		int cityId = 0;
		Integer orderId = orderDao.createSupplyChainOrder(cityId, orderFromSupplyDTO.getDestId(), orderFromSupplyDTO.getTotalMoney());

		// 设置订单的payId、orderNum
		// String payId = generatePayId(orderId);
		String orderNum = AliOcs.genOrderNum(orderFromSupplyDTO.getDestId().toString());
		orderDao.setPayIdAndOrderNum(orderId, payId, orderNum);

		// 创建一个订单对应的默认菜品或商品列表
		orderMenuDao.createDefaultOrderMenu(orderId);
		return orderId;
	}

	private Integer createSupplyChainMerchantOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) {
		return createSupplyChainOrder(orderFromSupplyDTO, Constants.SUPPLYCHAIN_MERCHANT_ORDER, payId);
	}

	private Integer createSupplyChainWarehouseOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) {
		return createSupplyChainOrder(orderFromSupplyDTO, Constants.SUPPLYCHAIN_WAREHOUSE_ORDER, payId);
	}

	/**
	 * 保存供应链订单的其他信息
	 * 
	 * @param orderFromSupplyDTO
	 * @param orderId
	 * @param type
	 * @throws Exception
	 */
	private void saveSupplyChainOrderInfo(OrderFromSupplyDTO orderFromSupplyDTO, Integer orderId, int type)
			throws Exception {
		SupplyChainOrderInfoEntity entity = new SupplyChainOrderInfoEntity();

		BeanUtils.copyProperties(entity, orderFromSupplyDTO);
		entity.setOrderId(orderId);
		entity.setSupplyChainOrderId(orderFromSupplyDTO.getOrderId());
		entity.setOrderType(type);
		save(entity);
	}

	@Override
	public AjaxJson receiveSupplyChainMerchantOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) throws Exception {
		String sql = "select id from `order` where pay_id = ?";
		
		Integer orderId = findOneForJdbc(sql, Integer.class, payId);
		if (orderId == null) {
			// 创建供应链商家订单
			orderId = createSupplyChainMerchantOrder(orderFromSupplyDTO, payId);
			
			// 保存供应链订单的其他详细信息
			saveSupplyChainOrderInfo(orderFromSupplyDTO, orderId, Constants.SUPPLYCHAIN_MERCHANT_ORDER);
		}
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setStateCode(AjaxJson.STATE_CODE_SUCCESS);
		ajaxJson.setMsg("成功");
		ajaxJson.setObj(orderId);
		return ajaxJson;
	}

	/**
	 * 
	 * @param orderFromSupplyDTO
	 * @return
	 */
	@Override
	public AjaxJson receiveSupplyChainWarehouseOrder(OrderFromSupplyDTO orderFromSupplyDTO, String payId) throws Exception {
		String sql = "select id from `order` where pay_id = ?";
		
		Integer orderId = findOneForJdbc(sql, Integer.class, payId);
		if (orderId == null) {
			// 创建供应链仓库订单
			orderId = createSupplyChainWarehouseOrder(orderFromSupplyDTO, payId);
			// 保存供应链订单的其他详细信息
			saveSupplyChainOrderInfo(orderFromSupplyDTO, orderId, Constants.SUPPLYCHAIN_WAREHOUSE_ORDER);
		}
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(true);
		ajaxJson.setStateCode(AjaxJson.STATE_CODE_SUCCESS);
		ajaxJson.setMsg("成功");
		ajaxJson.setObj(orderId);
		return ajaxJson;
	}

	/**
	 * 通知供应链系统订单的状态
	 * 
	 * @param couierId
	 * @param supplyOrderId
	 * @param state
	 * @return
	 */
	@Override
	public boolean noticeSupplyChain(Integer userId, Integer orderId, String state) {
		// 是供应链订单
		if (((OrderServiceImpl) orderService).isSupplyChainOrder(orderId)) {
			SupplyChainOrderInfoEntity supplyChainOrderInfo = null;
			try {
				supplyChainOrderInfo = findUniqueByProperty(SupplyChainOrderInfoEntity.class, "orderId", orderId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("根据orderId:{}查找供应链订单的其他信息失败");
				return false;
			}
			if (supplyChainOrderInfo != null) {
				String url = SupplyChainNotifyUtils.getNotifyUrl(supplyChainOrderInfo.getOrderType(), state);

				WUserEntity user = this.get(WUserEntity.class, userId);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("courierId", userId);
				params.put("supplyOrderId", supplyChainOrderInfo.getSupplyChainOrderId());
				params.put("courierPhone", user.getMobile());
				params.put("courierName", user.getUsername());

				try {
					HttpProxy proxy = HttpProxy.createInstance(url, null, params);
					logger.info("通知供应链系统，url:{}, params:{}", url, JSON.toJSON(params));
					String response = proxy.doGet();
					logger.info("通知供应链系统，return:{}", JSON.toJSON(response));
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("通知供应链系统失败。。。");
					return false;
				}
			}
		}
		return false;
	}

}
