package com.wm.aop.lock.distribute;

/**
 * 分布式锁key提供接口
 */
public interface LockKeySupplier {

	/**
	 * 提供一个key（申请分布式锁用的key）
	 * @param lockTag 注解值
	 * @param methodParameters 方法参数
	 * @return 返回key字符串
	 * @throws IllegalArgumentException，非法的注解值或参数值
	 */
	String supply(LockTag lockTag, TagMethodParameter... methodParameters);

}
