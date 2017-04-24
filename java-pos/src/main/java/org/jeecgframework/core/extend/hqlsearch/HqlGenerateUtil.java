package org.jeecgframework.core.extend.hqlsearch;

import java.beans.PropertyDescriptor;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.extend.hqlsearch.util.StringUtil;


public class HqlGenerateUtil {
	
	private static final String SUFFIX_COMMA = ",";
	private static final String SUFFIX_KG = " ";
	/**模糊查询符号*/
	private static final String SUFFIX_ASTERISK = "*";
	//--update-begin--Author:coco  Date:20130520 for：模糊查询
	private static final String SUFFIX_ASTERISK_VAGUE = "%";
	//--update-begin--Author:coco  Date:20130520 for：模糊查询
	/**不等于查询符号*/
	private static final String SUFFIX_NOT_EQUAL = "!";
	private static final String SUFFIX_NOT_EQUAL_NULL = "!NULL";
	
	/**时间查询符号*/
	private static final String END = "end";
	private static final String BEGIN = "begin";
	private static final Logger logger = Logger.getLogger(HqlGenerateUtil.class);
	

	
	/**
  	 * 自动生成查询条件HQL
  	 * 模糊查询
  	 * 【只对Integer类型和String类型的字段自动生成查询条件】
  	 * @param hql
  	 * @param values
  	 * @param searchObj
  	 * @throws Exception
  	 */
  	public static void installHql(CriteriaQuery cq,Object searchObj){
  		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);
  		// 获得对象属性中的name 
  		String descriptorsNames = getDescriptorsNames(origDescriptors);
  		
        for (int i = 0; i < origDescriptors.length; i++) {
            String name = origDescriptors[i].getName();
            String type = origDescriptors[i].getPropertyType().toString();
            
            if ("class".equals(name)||"ids".equals(name)||"page".equals(name)
            		||"rows".equals(name)||"sort".equals(name)||"order".equals(name)) {
                continue; // No point in trying to set an object's class
            }
            try {
            if (PropertyUtils.isReadable(searchObj, name)) {
               if("class java.lang.String".equals(type)){
            	   Object value = PropertyUtils.getSimpleProperty(searchObj, name);
            	   String searchValue = null;
            	   if(value!=null){
            		    searchValue = value.toString().trim();
            	   }else{
            		   continue;
            	   }
            	   if(searchValue!=null&&!"".equals(searchValue)){
            		   //[1].In 多个条件查询{逗号隔开参数}
            		   if(searchValue.indexOf(SUFFIX_COMMA)>=0){
            			   //页面输入查询条件，情况（取消字段的默认条件）
            			   if(searchValue.indexOf(SUFFIX_KG)>=0){
            				   String val = searchValue.substring(searchValue.indexOf(SUFFIX_KG));
            				   cq.eq(name, val);
            			   }else{
            				   String[] vs = searchValue.split(SUFFIX_COMMA);
                			   cq.in(name, vs);
            			   }
            		   }
            		   //[2].模糊查询{带有* 星号的参数}
            		   else if(searchValue.indexOf(SUFFIX_ASTERISK)>=0){
            			   cq.like(name, searchValue.replace(SUFFIX_ASTERISK, SUFFIX_ASTERISK_VAGUE));
            		   }
            		   //[3].不匹配查询{等于！叹号}
            		   //(1).不为空字符串
            		   else if(searchValue.equals(SUFFIX_NOT_EQUAL)){
            			   cq.isNotNull(name);
            		   }
            		   //(2).不为NULL
            		   else if(searchValue.toUpperCase().equals(SUFFIX_NOT_EQUAL_NULL)){
            			   cq.isNotNull(name);
            		   }
            		   //(3).正常不匹配
            		   else if(searchValue.indexOf(SUFFIX_NOT_EQUAL)>=0){
            			   cq.notEq(name, searchValue.replace(SUFFIX_NOT_EQUAL, ""));
            		   }
            		   //[4].全匹配查询{没有特殊符号的参数}
            		   else{
            			   cq.eq(name, searchValue);
            		   }
            	   }
               }else if("class java.lang.Integer".equals(type)){
            	   Object value = PropertyUtils.getSimpleProperty(searchObj, name);
            	   if(value!=null&&!"".equals(value)){
            		   cq.eq(name, value);
            	   }
               } else if("class java.math.BigDecimal".equals(type)){
            	   //update-begin--Author:zhaojunfu  Date:20130503 for：增加对bigDecimal数据的支持
            	   Object value = PropertyUtils.getSimpleProperty(searchObj, name);
            	   if(value!=null&&!"".equals(value)){
            		   cq.eq(name, value);
            	   }
            	   //update-end--Author:zhaojunfu  Date:20130503 for：增加对bigDecimal数据的支持
               }else if("class java.lang.Short".equals(type)){
            	   //update-begin--Author:zhaojunfu  Date:20130518 for：TASK #93 增加对SHORT数据的支持
            	   Object value = PropertyUtils.getSimpleProperty(searchObj, name);
            	   if(value!=null&&!"".equals(value)){
            		   cq.eq(name, value);
            	   }
            	   //update-end--Author:zhaojunfu  Date:20130518 for：TASK #93增加对SHORT数据的支持
               }else if("class java.lang.Long".equals(type)){
            	   //update-begin--Author:zhaojunfu  Date:20130518 for：TASK #93 增加对LONG 数据的支持
            	   Object value = PropertyUtils.getSimpleProperty(searchObj, name);
            	   if(value!=null&&!"".equals(value)){
            		   cq.eq(name, value);
            	   }
            	   //update-end--Author:zhaojunfu  Date:20130518 for：TASK #93 增加对LONG 数据的支持
               }else if ("class java.util.Date".equals(type)) {
					Date value = (Date) PropertyUtils.getSimpleProperty(searchObj, name);
					if (null != value) {
						// 判断开始时间
						if (name.contains(BEGIN)) {
							String realName = StringUtil.firstLowerCase(name.substring(5, name.length()));
							if (!BEGIN.equals(name.substring(0, 5)) || !descriptorsNames.contains(realName)) {
								logger.error("该查询属性 [" + name + "] 命名不规则");
							} else {
								cq.ge(realName, value);
							}
						}
						// 判断结束时间
						else if (name.contains(END)) {
							String realName = StringUtil.firstLowerCase(name.substring(3, name.length()));
							if (!END.equals(name.substring(0, 3)) || !descriptorsNames.contains(realName)) {
								logger.error("该查询属性 [" + name + "] 命名不规则");
							} else {
								cq.le(name, value);
							}
						}
						else {
							  cq.eq(name, value);
						}
					 }
                  }
               }
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //添加选择条件
        cq.add();
  	}
  	
	/**
	 * 得到对象属性中所有name
	 * @param origDescriptors
	 * @return
	 */
  	private static String getDescriptorsNames(PropertyDescriptor origDescriptors[]) {
  		StringBuilder sb = new StringBuilder();
  		for (int i = 0; i < origDescriptors.length; i++) {
  			sb.append(origDescriptors[i].getName() + ",");
  		}
  		return sb.toString();
  	}
  	
  	public static void main(String[] args) {
  		
	}
  
}


