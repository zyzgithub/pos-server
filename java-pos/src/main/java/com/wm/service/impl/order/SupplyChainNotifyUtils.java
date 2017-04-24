package com.wm.service.impl.order;

import org.apache.commons.lang3.StringUtils;

import com.base.config.EnvConfig;
import com.courier_mana.common.Constants;

public class SupplyChainNotifyUtils {

	public static final String SUPPLYCHAIN_URL = EnvConfig.base.SUPPLY_ERP_HOST;
//	public static final String SUPPLYCHAIN_URL = "http://120.76.100.1:8081";

	public static final String MERCHANT_ORDER_ACCEPT = "/logisticsOpenApi/grabOrder";
	public static final String MERCHANT_ORDER_DELIVERING = "/logisticsOpenApi/deliveryOrder";
	public static final String MERCHANT_ORDER_CONFIERM = "/logisticsOpenApi/receiptedOrder";

	public static final String MAREHOUSE_ORDER_ACCEPT = "/logisticsPurchaseOpenApi/grabOrder";
	public static final String MAREHOUSE_ORDER_DELIVERING = "/logisticsPurchaseOpenApi/deliveryOrder";
	public static final String MAREHOUSE_ORDER_CONFIERM = "/logisticsPurchaseOpenApi/receiptedOrder";

	public static String getNotifyUrl(int orderType, String state) {
		if (orderType == Constants.SUPPLYCHAIN_MERCHANT_ORDER) {
			if (StringUtils.equalsIgnoreCase(state, Constants.ACCEPTED)) {
				return SUPPLYCHAIN_URL + MERCHANT_ORDER_ACCEPT;
			} else if (StringUtils.equalsIgnoreCase(state, Constants.DELIVERYING)) {
				return SUPPLYCHAIN_URL + MERCHANT_ORDER_DELIVERING;
			}
			return SUPPLYCHAIN_URL + MERCHANT_ORDER_CONFIERM;
		}
		if (StringUtils.equalsIgnoreCase(state, Constants.ACCEPTED)) {
			return SUPPLYCHAIN_URL + MAREHOUSE_ORDER_ACCEPT;
		} else if (StringUtils.equalsIgnoreCase(state, Constants.DELIVERYING)) {
			return SUPPLYCHAIN_URL + MAREHOUSE_ORDER_DELIVERING;
		}
		return SUPPLYCHAIN_URL + MAREHOUSE_ORDER_CONFIERM;
	}
}