package com.wm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * 读取config.xml配置工具类
 * @author lfq
 * @email  545987886@qq.com
 * @2015-5-8
 */
public class Init {

	private static LinkedHashMap<String, Object> MAP = new LinkedHashMap<String, Object>();
	private static final String rootPath = Init.class.getResource("").getPath();
	private static String xmlFileName = "config.xml";

	static{
		loadKeyValue();
	}

	public static void reload(ServletContext context){
		loadKeyValue();
		if(!MAP.isEmpty()){
			for (String key : MAP.keySet()) {
				context.setAttribute(key, get(key).get("value"));
			}
		}
	}
	
	
	/**
	 * 读取xml文件，初始化数据
	 * @param elementName
	 * @return
	 */
	public static void loadKeyValue(){
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath+xmlFileName);
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			MAP = parseElement(root);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static LinkedHashMap<String, Object> parseElement(Element element){
		if(element==null)return null;
		List<Element> children = element.elements();
		if(children==null || children.size()<=0){
			List list = element.attributes();
			if(list==null || list.size()<=0)return null;
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				Attribute attr = (Attribute) list.get(i);
				if(attr!=null)map.put(attr.getName(), attr.getValue());
			}
			return map;
		}else{
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			for (int i = 0; i < children.size(); i++) {
				Element e = children.get(i);
				if(e==null)continue;
				Attribute attr = e.attribute("name");
				if(attr==null || attr.getValue()==null || attr.getValue().trim().equals("")){
					map.putAll(parseElement(e));
				}else{
					map.put(attr.getValue(), parseElement(e));
				}
			}
			return map;
		}
	}

	public static LinkedHashMap<String, Object> get(String...key){
		return get(MAP, key, 0);
	}
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> get(LinkedHashMap<String, Object> map, String[] key, int cursor){
		if(map==null || map.isEmpty() || key==null || key.length<=0 || cursor>=key.length)return map;
		Object obj = map.get(key[cursor]);
		if(obj instanceof LinkedHashMap){
			LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) obj;
			return get(tmp, key, cursor+1);
		}else{
			return (LinkedHashMap<String, Object>) map.clone();
		}
	}

	/**
	 * 操作失败的标志
	 */
	public static final String FAIL="fail";
	/**
	 * 操作成功的标志
	 */
	public static final String SUCCEED="succeed";

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	/**
	 *  JSON 判断错误
	 */
	public static final String ERROR = "error";

	
	/**
	 * 内置日期格式
	 *
	 */
	public interface DataFormat{
		
		public static final String YEAR_TO_MONTH = "yyyy年MM月";
		
		public static final String YEAR_TO_SECOND = "yyyy-MM-dd HH:mm:ss";
		
		public static final String YEAR_TO_DATA = "yyyy-MM-dd";
		
		public static final String MONTH_TO_SECOND = "MM-dd HH:mm:ss";
		
	}
	
	/*public static Object getValue(String...key){
		return Init.get(key).get("value");
	}*/
	public static Integer getCurrentPage(Integer currentPage){
		return currentPage==null||currentPage<1?1:currentPage;
	}
	public static Integer getPageSize(Integer pageSize){
		Integer defaultPageSize = 10;
		Object obj = Init.get("pageSize").get("value");
		if(obj!=null && !obj.toString().trim().equals("")){
			defaultPageSize = Integer.parseInt(obj.toString().trim());
		}
		return (pageSize==null||pageSize<1?defaultPageSize:pageSize);
	}
	
}
