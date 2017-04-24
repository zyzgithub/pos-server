package com.wm.util.spring.json;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.wm.util.JacksonUtil;

/**
 * 对{@code JsonParam}注解的参数进行json解析
 * <br>1、以{@code JsonParam}的value为key，从http请求参数中读取对应的值进行json解析。
 * <br>2、如果未找到匹配的http请求参数key或value为空，且{@code JsonParam}的resolveBody为true，则尝试对http请求正文进行json解析
 * （http请求contentType必须为"application/json"）。此功能与{@code RequestBody}注解的功能是一致的。
 * 
 * <p>
 * <br><b>根据http请求参数进行json解析示例</b>
 * <br><b>1、java controller，为参数添加注解，value的值是http参数的key：</b>
 * <br>{@code @JsonParam("datas")}
 * <br><b>2、javascript：</b>
 * <br>var dataStr = JSON.stringify({
 * <br>		id : 123,
 * <br>		orderDetail : [{
 * <br>			goodsId : 222
 * <br>		} , {
 * <br>			goodsId : 666
 * <br>		}]
 * <br>});
 * <br>$.ajax({
 * <br>		type : "POST",
 * <br>		url : "/java_supplier/orderController.do?createOrder",
 * <br>		data : {
 * <br>			datas : dataStr
 * <br>		},
 * <br>		success : function(data) {
 * <br>			console.dir(data);
 * <br>		}
 * <br>});
 * 
 * <p>
 * <br><b>根据http请求正文进行json解析示例</b>
 * <br><b>1、java controller，为参数添加注解（可以不用value值）：</b>
 * <br>{@code @JsonParam(resolveBody = true)}
 * <br><b>2、javascript：</b>
 * <br>var dataStr = JSON.stringify({
 * <br>		id : 123,
 * <br>		orderDetail : [{
 * <br>			goodsId : 222
 * <br>		} , {
 * <br>			goodsId : 666
 * <br>		}]
 * <br>});
 * <br>$.ajax({
 * <br>		type : "POST",
 * <br>		dataType : "json",
 * <br>		contentType : "application/json",
 * <br>		url : "/java_supplier/orderController.do?createOrder",
 * <br>		data : dataStr,
 * <br>		success : function(data) {
 * <br>			console.dir(data);
 * <br>		}
 * <br>});
 */
public class JsonParamResolver implements HandlerMethodArgumentResolver{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//有jsonParam注解才去处理
//		logger.info("是否有param注解：supportsParameter:{}", parameter.hasParameterAnnotation(JsonParam.class));
		return parameter.hasParameterAnnotation(JsonParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		JsonParam jsonParam = parameter.getParameterAnnotation(JsonParam.class);
		//(1)通过请求参数Key获取参数值
		String parameterValue = getRequestParamValue(jsonParam, webRequest);  
		if (StringUtils.isNotBlank(parameterValue)) {
			return JacksonUtil.readValue(parameterValue, parameter.getParameterType());
		}
		
		//(2)如果Key为空，或者value为空，就通过请求正文获取值
		if(jsonParam.isResolveBody()){
			HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
			parameterValue = IOUtils.toString(request.getInputStream(),"utf-8");
		}
		logger.info("通过http正文获取请求参数值:{}", parameterValue);
		if(StringUtils.isBlank(parameterValue)){
			return null;
		}
		return JacksonUtil.readValue(parameterValue, parameter.getParameterType());  //把json字符串按参数类型反序列化
	}
	

	//通过请求参数Key获取参数值
	private String getRequestParamValue(JsonParam jsonParam, NativeWebRequest webRequest){
		String keyname = jsonParam.keyname();  
		logger.info("请求参数key:{}", keyname);
		if (StringUtils.isBlank(keyname)) {
			return null;
		}
		String parameterValue = webRequest.getParameter(keyname);	
		logger.info("通过key获取请求参数值value:{}", parameterValue);
		return parameterValue;
	}

}
