package com.dianba.pos.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
    public static String toJsonString(Object returnValue) {
        return JSONObject.toJSONString(returnValue, new SerializerFeature[]{SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse});
    }
}
