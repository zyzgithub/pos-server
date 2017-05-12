package com.dianba.pos.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(JsonUtil.class);

    public static String toJsonString(Object returnValue) {
        return JSONObject.toJSONString(returnValue, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse);
    }

    /**
     * 转换String格式的Json数据为指定类型的Bean
     *
     * @param content String格式的Json数据
     * @param clazz   目标Bean类型
     * @param <T>     目标Bean的具体类型
     * @return <T>类型的Bean
     */
    public static <T> T toBean(String content, Class<T> clazz) {
        T bean = null;
        try {
            bean = OBJECT_MAPPER.readValue(content, clazz);
        } catch (Exception ex) {
            logger.warn("\"" + content + "\" is not valid JSON");
        }


        return bean;
    }

    /**
     * 转换String格式的Json数据为指定类型的Bean, 仅支持构造方法参数存在自定义类型的情形
     *
     * @param content           String格式的Json数据
     * @param parametrizedClass 目标Bean类型
     * @param parameterClasses  构造方法参数类型清单
     * @param <T>               目标Bean的具体类型
     * @return <T>类型的Bean
     */
    public static <T> T toBean(String content, Class<T> parametrizedClass, Class<?>... parameterClasses) {
        T bean = null;
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrizedClass, parameterClasses);
            bean = OBJECT_MAPPER.readValue(content, type);
        } catch (Exception ex) {
            logger.warn("\"" + content + "\" is not valid JSON");
        }

        return bean;
    }

    /**
     * Bean转换为String类型的Json数据,忽略空或者''
     *
     * @param bean 要转换的Bean实例
     * @param <T>  要转换的Bean类型
     * @return String类型的Json数据
     */
    public static <T> String toJson(T bean) {
        String content = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator
                    = OBJECT_MAPPER.getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);

            jsonGenerator.writeObject(bean);

            content = new String(outputStream.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }

        return content;
    }

    /**
     * 转换String格式的Json数据为{@link JsonNode}类型
     *
     * @param content String格式的Json数据
     * @return {@link JsonNode}
     */
    public static JsonNode toJsonNode(String content) {
        try {
            return OBJECT_MAPPER.getJsonFactory().createJsonParser(content).readValueAsTree();
        } catch (IOException ex) {
            logger.warn(ex.getMessage(), ex);
        }
        return null;
    }

}
