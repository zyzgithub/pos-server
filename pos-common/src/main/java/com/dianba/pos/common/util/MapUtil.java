package com.dianba.pos.common.util;

import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}

class MapValueComparator implements Comparator<Map.Entry<String, String>> {

    @Override
    public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}

@SuppressWarnings("all")
public class MapUtil {

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param
     * @return
     */
    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, String>> iter = entryList.iterator();
        Map.Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param
     * @return
     */
    public static String buildSign(Map<String, String> map, String key) {
        Set<Map.Entry<String, String>> set = map.entrySet();
        StringBuffer sb = new StringBuffer();
        //取出排序后的参数，逐一连接起来
        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext(); ) {
            Map.Entry<String, String> me = it.next();
            sb.append(me.getValue() + "|");
        }
        sb.append(key);
        return sb.toString();//返回最终排序后的结果，这里key不参与排序中，具体看接口规约
        //return (Md5Encrypt.encrypt(sb.toString())).toUpperCase();//这个带有md5加密的，算法就不贴了，网上很多
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, Object> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);

            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
}
