package com.wm.aop.lock.distribute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wm.aop.lock.ApplyLockFailException;
import com.wm.util.IDistributedLock;
import com.wm.util.MemcachedDistributedLock;

/**
 * 分布式锁申请advisor，在申请不到锁的时候抛出ApplyLockFailException
 */
@Component
@Aspect
public class LockAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(LockAdvisor.class);

	private static final int EXPIRE = 60;

	private static final int TRY_TIME = 30;

	private volatile IDistributedLock lock;

	/**
	 * 环绕切面通知。在执行前申请锁，执行切入点，在执行后释放锁。
	 * @param point 处理切入点
	 * @param lockTag LockTag注解
	 * @return 切入点返回值
	 * @throws ApplyLockFailException 申请锁失败异常
	 * @throws Throwable 其他异常信息
	 */
	@Around("execution(public * * (..)) && @annotation(lockTag)")
	public Object around(ProceedingJoinPoint point, LockTag lockTag) throws ApplyLockFailException, Throwable {
		String lockKey = getLockKey(point, lockTag);
		IDistributedLock distributedLock = getDistributedLock();
		String uuid = null;

		try {
			logger.info("即将申请分布式锁, key：{}", lockKey);
			uuid = distributedLock.tryAcquireLock(lockKey, EXPIRE, TRY_TIME);
			logger.info("申请到分布式锁, value：{}", uuid);

			if (uuid == null) {
				logger.info("竞争不到分布式锁, key:{}", lockKey);
				throw new ApplyLockFailException("key:" + lockKey);
			}
			
			return point.proceed();
		} finally {
			logger.info("申请分布式锁进入finally, key:{}", lockKey);

			try {
				if (uuid != null) {
					logger.info("即将释放分布式锁, key:{}, value:{}", lockKey, uuid);
					distributedLock.releaseLock(lockKey, uuid);
				}
			} catch (Exception e) {
				logger.info("", e);
			}
		}
	}

	private String getLockKey(ProceedingJoinPoint point, LockTag lockTag)
			throws InstantiationException, IllegalAccessException {
		TagMethodParameter[] methodParameters = getMethodParameters(point);
		LockKeySupplier keySupplier = lockTag.keySupplier().newInstance();
		return keySupplier.supply(lockTag, methodParameters);
	}

	private TagMethodParameter[] getMethodParameters(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Class<?>[] parameterTypes = signature.getParameterTypes();
		Object[] args = point.getArgs();
		if (ArrayUtils.getLength(args) != ArrayUtils.getLength(parameterNames)
				|| ArrayUtils.getLength(args) != ArrayUtils.getLength(parameterTypes)) {
			throw new UnsupportedOperationException("AOP无法正确的识别参数名和参数类型");
		}
		return getMethodParameters(parameterNames, parameterTypes, args);
	}

	private TagMethodParameter[] getMethodParameters(String[] parameterNames, Class<?>[] parameterTypes,
			Object[] args) {
		List<TagMethodParameter> methodParameterList = new ArrayList<TagMethodParameter>(args.length);
		for (int i = 0; i < args.length; i++) {
			TagMethodParameter methodParameter = new DefaultTagMethodParameter(parameterTypes[i], parameterNames[i],
					args[i]);
			methodParameterList.add(methodParameter);
		}
		return methodParameterList.toArray(new TagMethodParameter[methodParameterList.size()]);
	}

	private IDistributedLock getDistributedLock() {
		if (lock == null) {
			synchronized (this) {
				if (lock == null) {
					lock = new MemcachedDistributedLock();
				}
			}
		}
		return lock;
	}

	private class DefaultTagMethodParameter implements TagMethodParameter {

		private Class<?> type;

		private String name;

		private Object value;

		public DefaultTagMethodParameter(Class<?> type, String name, Object value) {
			this.type = type;
			this.name = name;
			this.value = value;
		}

		@Override
		public Class<?> getType() {
			return type;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Object getValue() {
			return value;
		}

	}

}
