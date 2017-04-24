package com.wm.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * com.fasterxml.jackson工具类
 * 
 * @author 王喜文
 * @since 2016-3-15
 */
public class JacksonUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String writeValueAsString(Object value) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(value);
	}

	public static <T> T readValue(String value, Class<? extends T> c) throws JsonParseException, JsonMappingException,
			IOException {
		return OBJECT_MAPPER.readValue(value, c);
	}

}
