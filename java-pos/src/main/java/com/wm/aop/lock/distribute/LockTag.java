package com.wm.aop.lock.distribute;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁注解，表明被注解的方法可能使用AOP申请释放锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LockTag {

	/**
	 * 信息字符串，可用于提供构建分布式锁key的信息
	 * @return 信息字符串
	 */
	String value() default "";

	/**
	 * 分布式锁key的生成策略类，必须是一个具体实现类，并提供一个默认无参构造函数
	 * @return 分布式锁key的生成策略类
	 */
	Class<? extends LockKeySupplier> keySupplier() default PrefixJoinedLockKeySupplier.class;

}
