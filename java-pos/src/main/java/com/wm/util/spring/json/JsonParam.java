package com.wm.util.spring.json;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解适用于Controller中RequestMapping方法的参数绑定。表明该参数是由json字符串反序列化而来。
 */
@Retention(RetentionPolicy.RUNTIME)  //编译程序将Annotation存储于class档中，可由VM读入，可以通过反射的方式读取到
@Target(ElementType.PARAMETER)  //适用method上之parameter，用于其他地方会报错
@Documented
@Inherited
public @interface JsonParam {
	
	/**
	 * @return 得到请求参数key名称
	 */
	String keyname();
	
	/**
	 * 是否解析http请求正文（如果找不到匹配的http参数key 或者 value为空时），默认为false
	 * @return true拿正文body，false拿url请求行携带参数值
	 */
	boolean isResolveBody() default false;
}
