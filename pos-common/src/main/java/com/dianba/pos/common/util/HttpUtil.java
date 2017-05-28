package com.dianba.pos.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpUtils;

public class HttpUtil {

    /**
     * @param url
     * @param params
     * @return
     */
    @Deprecated
    public static JSONObject sendGet(String url, JSONObject params) {
        return HttpUtils.get(url, params);
    }

    public static JSONObject get(String url, Object objects){
        String requestJson = JSONObject.toJSON(objects).toString();
        JSONObject jsonObject = HttpUtils.get(url, JSON.parseObject(requestJson));
        return jsonObject;
    }


    public static JSONObject post(String url, Object  objects) {
        String requestJson = JSONObject.toJSON(objects).toString();
        JSONObject par=JSON.parseObject(requestJson);
        JSONObject jsonObject = HttpUtils.postForm(url,par);
        return jsonObject;
    }
}
