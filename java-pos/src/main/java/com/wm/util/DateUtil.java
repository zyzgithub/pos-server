package com.wm.util;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.entity.order.OrderEntity;

/**
 * 日期工具类
 */
public class DateUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * 判断以毫秒为单位的时间是否处于今天
	 * @param timeMillis 以毫秒为单位的时间
	 * @return 是否处于今天
	 */
	public static boolean isToday(long timeMillis) {
		Calendar input = Calendar.getInstance();
		input.setTimeInMillis(timeMillis);
		return DateUtils.isSameDay(input, Calendar.getInstance());
	}
	
	/**
	 * 获取外卖订单的预计送达时间，默认30分钟后到达
	 * @param timeRemark
	 * @param saleType
	 * @return
	 */
	public static String getOrderTimeRemark(String timeRemark, Integer saleType) {
		if (OrderEntity.SaleType.TAKEOUT.equals(saleType)) {
			DateTime now = DateTime.now();
			DateTime halfHourLater = now.plusMinutes(30);
			String curDate = now.toString("yyyy-MM-dd");
			if (!StringUtil.isEmpty(timeRemark)) {
				String remarkTime = curDate + " " + timeRemark;
				try {
					DateTime remarkDateTime = DateTime.parse(remarkTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
					if(halfHourLater.isAfter(remarkDateTime)){
						timeRemark = halfHourLater.toString("HH:mm");
					}
				} catch (Exception e) {
					logger.error("timeRemark:{} format fail !!!", remarkTime);
				}
			}
		}
		logger.info("timeRemark : {}", timeRemark);
		return timeRemark;
	}
	
	public static void main(String[] args){
		DateUtil.getOrderTimeRemark("12:44", 1);
	}

}
