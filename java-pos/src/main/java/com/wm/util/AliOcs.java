package com.wm.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.config.EnvConfig;

public class AliOcs {

	private static final Logger logger = LoggerFactory.getLogger(AliOcs.class);

	private static final String key = "order_num_";
	private static final int expierSeconds = 60 * 60 * 24 * 1; // 1day
	private static MemcachedClient client;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd0001");

	private static Object lock = new Object();

	private AliOcs() {
	}

	static {
		try {
			client = new MemcachedClient(new BinaryConnectionFactory(),
					AddrUtil.getAddresses(EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT));
			// client = new MemcachedClient(new BinaryConnectionFactory(),
			// AddrUtil.getAddresses("192.168.8.100:11211"));
		} catch (IOException e) {
			logger.error("初始化Memcached失败：" + EnvConfig.base.MEMCACHED_HOST + ":" + EnvConfig.base.MEMCACHED_PORT, e);
		}
	}

	private static String contractIdKey = "contractId";

	/** 9  获取合同编号，每天重置为1 */
	public static int getContractId(int day) {

		int id = 1;
		String value = day + "_" + id;

		while (true) {
			CASValue<Object> cas = client.gets(contractIdKey);
			if (null != cas) {
				Object day_id = cas.getValue();
				if (null != day_id) {
					try {
						String day_id_str = day_id.toString();
						int index = day_id_str.indexOf("_");
						int src_day = Integer.parseInt(day_id_str.substring(0, index));
						id = Integer.parseInt(day_id_str.substring(index + 1, day_id_str.length()));
						if (src_day != day) {
							// 重置
							id = 1;
						} else {
							// 递增
							id = id + 1;
						}
						// 设置进去
						value = day + "_" + id;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 重新设置
				CASResponse response = client.cas(contractIdKey, cas.getCas(), value);
				if (CASResponse.OK.name().equals(response.name())) {
					return id;
				}
			} else {
				client.add(contractIdKey, expierSeconds, value);
				return id;
			}
		}
	}

	public static void initOrderNum() {
		long num = Long.valueOf(sdf.format(new Date()));
		logger.info("初始化订单排号：" + num);
		while (true) {
			CASValue<Object> cas = client.gets(key);
			if (null != cas) {
				CASResponse response = client.cas(key, cas.getCas(), num);
				if (CASResponse.OK.name().equals(response.name())) {
					logger.info("初始化订单排号成功!");
					break;
				}
			} else {
				client.add(key, expierSeconds, num);
				logger.info("================首次初始化order_num为 " + num);
				break;
			}
		}
	}

	public static long getAndIncrOrderNum() {
		long num = Long.valueOf(sdf.format(new Date()));
		while (true) {
			CASValue<Object> cas = client.gets(key);
			if (null != cas) {
				Object orderNumObj = cas.getValue();
				if (null != orderNumObj) {
					String orderNumStr = orderNumObj.toString();
					if (orderNumStr.length() > 8) {
						String orderDate = orderNumStr.substring(0, 8);
						Integer orderNum = Integer.parseInt(orderNumStr.substring(8, orderNumStr.length()));
						orderNum++;
						num = Long.valueOf(orderDate + orderNum);
					} else {
						logger.error("订单排号的格式不正确!" + orderNumStr);
						num = Long.valueOf(sdf.format(new Date()));
					}
				} else {
					logger.error("获取Memcached订单排号异常!");
				}

				CASResponse response = client.cas(key, cas.getCas(), num);

				if (CASResponse.OK.name().equals(response.name())) {
					break;
				}
			} else {
				client.add(key, expierSeconds, num);
				logger.info("================首次初始化order_num为 " + num);
				break;
			}
		}
		return num;
	}

	/**
	 * 新增键值对，如果存在，则不做任何操作
	 * 
	 * @param key
	 * @param value
	 */
	public static void add(String key, String value) {
		client.add(key, expierSeconds, value);
	}

	/**
	 * 新建键值对，如果存在，则不做任何操作。添加失效时间
	 * 
	 * @param key
	 * @param value
	 * @param exp
	 */
	public static void add(String key, String value, int exp) {
		client.add(key, exp, value);
	}

	/**
	 * 设置键值对，如果存在，则覆盖原来的value
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		client.set(key, expierSeconds, value);
	}

	public static void set(String key, Object value) {
		client.set(key, expierSeconds, value);
	}

	public static void update(String key, String value) {
		while (true) {
			CASValue<Object> cas = client.gets(key);
			CASResponse response = client.cas(key, cas.getCas(), value);
			if (CASResponse.OK.name().equals(response.name())) {
				break;
			}
		}
	}

	/**
	 * 获取key所对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		CASValue<Object> cas = client.gets(key);
		if (cas != null) {
			Object valObj = cas.getValue();
			if (valObj != null) {
				return valObj.toString();
			}
		}
		return "";
	}

	/**
	 * 设置键值对，如果存在，则覆盖原来的value
	 * 
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public static void set(String key, Object obj, int expire) {
		client.set(key, expire, obj);
	}

	/**
	 * 同步设置键值对，在设置前需要获取锁lock。 如果存在，则覆盖原来的value.如果不是用于多线程读写，请避免使用该方法
	 * 
	 * @param key
	 * @param obj
	 * @param expire
	 */
	public static void syncSet(String key, Object obj, int expire) {
		synchronized (lock) {
			client.set(key, expire, obj);
		}

	}

	/**
	 * 获取key所对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static Object getObject(String key) {
		CASValue<Object> cas = client.gets(key);
		if (cas != null) {
			return cas.getValue();
		}
		return null;
	}

	/**
	 * 同步获取key所对应的value，如果不是用于多线程读写，请避免使用该方法。
	 * 
	 * @param key
	 * @return
	 */
	public static Object syncGetObject(String key) {
		synchronized (lock) {
			CASValue<Object> cas = client.gets(key);
			if (cas != null) {
				return cas.getValue();
			}
			return null;
		}
	}

	/**
	 * 删除key所对应的value
	 * 
	 * @param key
	 * @return
	 */
	public static boolean remove(String key) {
		try {
			return client.delete(key).get();
		} catch (InterruptedException e) {
			return false;
		} catch (ExecutionException e) {
			return false;
		}
	}

	/**
	 * 同步删除key所对应的value， 在删除key前需要获取锁lock。 避免别的线程同时删除key.如果不是用于多线程读写，请避免使用该方法
	 * 
	 * @param key
	 * @return
	 */
	public static boolean syncRemove(String key) {
		synchronized (lock) {
			try {
				return client.delete(key).get();
			} catch (InterruptedException e) {
				return false;
			} catch (ExecutionException e) {
				return false;
			}
		}
	}

	/**
	 * 获取缓存看是否存在。
	 * 
	 * @return
	 */
	public static boolean getMemcachedClient() {
		if (client != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取memcached客户端
	 * 
	 * @return
	 */
	public static MemcachedClient getClient() {
		return client;
	}

	/**
	 * 重置商家订单排号
	 * 
	 * @param merchantId
	 */
	public static void resetOrderNum(String merchantId) {
		long num = Long.valueOf(sdf.format(new Date()));
		logger.info("初始化订单排号：" + num);
		while (true) {
			CASValue<Object> cas = client.gets(key + merchantId);
			if (null != cas) {
				CASResponse response = client.cas(key + merchantId, cas.getCas(), num);
				if (CASResponse.OK.name().equals(response.name())) {
					logger.info("初始化商家{}订单排号成功!", merchantId);
					break;
				}
			} else {
				client.add(key + merchantId, expierSeconds, num);
				logger.info("================首次初始化商家{},订单排号为 {}", merchantId, num);
				break;
			}
		}
	}

	/**
	 * 根据商家生成订单排号
	 * 
	 * @param merchantId
	 * @return
	 */
	public static String genOrderNum(String merchantId) {
		String curDay = DateTime.now().toString("yyyyMMdd");
		String orderNum = curDay + "1";
		if (StringUtil.isEmpty(merchantId)) {
			logger.error("genOrderNum merchantId is empty !!!");
			return orderNum;
		}
		String memKey = key + merchantId;
		while (true) {
			CASValue<Object> cas = client.gets(memKey);
			if (null != cas) {
				Object orderNumObj = cas.getValue();
				if (null != orderNumObj) {
					String orderNumStr = orderNumObj.toString();
					if (orderNumStr.length() > 8) {
						String orderDate = orderNumStr.substring(0, 8);
						if (orderDate.equals(curDay)) {
							// 当天
							Integer lastOrderNum = Integer.parseInt(orderNumStr.substring(8, orderNumStr.length()));
							lastOrderNum++;
							orderNum = curDay + lastOrderNum;
						} else {
							// 跨天
							logger.info("商家:{}第一笔订单order_num为:{}", merchantId, orderNum);
						}
					} else {
						logger.error("订单排号:{}的格式不正确!", orderNumStr);
					}
				} else {
					logger.error("获取商家:{}订单排号异常!", merchantId);
				}
				CASResponse response = null;
				try {
					response = client.cas(memKey, cas.getCas(), orderNum);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("设置商家:{}订单排号异常，order_num为:{}", merchantId, orderNum);
				}
				if (response != null && CASResponse.OK.name().equals(response.name())) {
					break;
				}
			} else {
				client.add(memKey, expierSeconds, orderNum);
				logger.info("================首次初始化商家:{}订单排号order_num为:{}", merchantId, orderNum);
				break;
			}
		}
		logger.info("genOrderNum merchantId:{}----->orderNum:{}", merchantId, orderNum);
		return orderNum;
	}

	//////////

}
