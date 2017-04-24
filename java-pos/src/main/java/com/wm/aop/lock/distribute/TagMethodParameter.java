package com.wm.aop.lock.distribute;

/**
 * 被注解的方法的参数（方法参数）
 */
public interface TagMethodParameter {

	/**
	 * 该参数的类型
	 * @return 该参数的类型
	 */
	Class<?> getType();

	/**
	 * 该参数的形参名
	 * @return 该参数的形参名
	 */
	String getName();

	/**
	 * 该参数的值
	 * @return 该参数的值
	 */
	Object getValue();

}
