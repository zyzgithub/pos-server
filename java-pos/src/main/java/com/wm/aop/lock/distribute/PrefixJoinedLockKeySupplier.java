package com.wm.aop.lock.distribute;

import org.apache.commons.lang3.StringUtils;

/**
 * 此工具提供一个由特定前缀连接指定参数值而生成的key。前缀和参数名从LockTag的value值中指定，以逗号为分隔，第一个值为字符串前缀，后续的值为方法中的各参数名。
 * <p>比如LockTag的value为："money_user,userId"，被LockTag注解的方法的参数中包含(Integer userId)，且运行时传了一个userId为110，则生成的key结果为"money_user110"。
 */
public class PrefixJoinedLockKeySupplier implements LockKeySupplier {

	@Override
	public String supply(LockTag lockTag, TagMethodParameter... methodParameters) {
		String[] split = tagValueSplit(lockTag.value());
		return getPrefix(split) + getContent(split, methodParameters);
	}

	private String[] tagValueSplit(String tagValue) {
		if (StringUtils.isBlank(tagValue)) {
			throw new IllegalArgumentException("空白的注解值");
		}
		String[] split = tagValue.split(",");
		if (split.length == 0) {
			throw new IllegalArgumentException("无法解析前缀和方法参数名：" + tagValue);
		}
		for (String s : split) {
			if (StringUtils.isBlank(s)) {
				throw new IllegalArgumentException("注解值存在空白的前缀或方法参数名");
			}
		}
		return split;
	}

	private String getPrefix(String[] split) {
		return split[0];
	}

	private String getContent(String[] split, TagMethodParameter... methodParameters) {
		if (split.length == 1) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 1; i < split.length; i++) {
			String expectedName = split[i];
			String parameterStr = getParameterStr(expectedName, methodParameters);
			stringBuilder.append(parameterStr);
		}
		return stringBuilder.toString();
	}

	private String getParameterStr(String expectedName, TagMethodParameter... methodParameters) {
		for (TagMethodParameter methodParameter : methodParameters) {
			if (!expectedName.equals(methodParameter.getName())) {
				continue;
			}
			Object value = methodParameter.getValue();
			if (value == null) {
				return "null";
			}
			return value.toString();
		}
		throw new RuntimeException("匹配不到方法参数名：" + expectedName);
	}

}
