package org.jeecgframework.codegenerate.util;

import java.util.ResourceBundle;

public class CodeResourceUtil
{
  private static final ResourceBundle a = ResourceBundle.getBundle("jeecg/jeecg_database");
  private static final ResourceBundle b = ResourceBundle.getBundle("jeecg/jeecg_config");
  public static String DIVER_NAME = "com.mysql.jdbc.Driver";
  public static String URL = "jdbc:mysql://localhost:3306/waimai?useUnicode=true&characterEncoding=UTF-8";
  public static String USERNAME = "root";
  public static String PASSWORD = "123456";
  public static String DATABASE_NAME = "waimai";
  public static String DATABASE_TYPE = "mysql";
  public static String JEECG_UI_FIELD_REQUIRED_NUM = "4";
  public static String JEECG_UI_FIELD_SEARCH_NUM = "3";
  public static String web_root_package = "WebRoot";
  public static String source_root_package = "src";
  public static String bussiPackage = "sun";
  public static String entity_package = "entity";
  public static String page_package = "page";
  public static boolean JEECG_FILED_CONVERT = true;
  public static String ENTITY_URL;
  public static String PAGE_URL;
  public static String ENTITY_URL_INX;
  public static String PAGE_URL_INX;
  public static String TEMPLATEPATH;
  public static String CODEPATH;
  public static String JSPPATH;
  public static String JEECG_GENERATE_TABLE_ID;
  public static String JEECG_GENERATE_UI_FILTER_FIELDS;
  public static String SYSTEM_ENCODING;

  static
  {
    DIVER_NAME = getDIVER_NAME();
    URL = getURL();
    USERNAME = getUSERNAME();
    PASSWORD = getPASSWORD();
    DATABASE_NAME = getDATABASE_NAME();
    JEECG_FILED_CONVERT = getJEECG_FILED_CONVERT();
    SYSTEM_ENCODING = getSYSTEM_ENCODING();
    TEMPLATEPATH = getTEMPLATEPATH();
    source_root_package = getSourceRootPackage();
    web_root_package = getWebRootPackage();
    bussiPackage = a();
    JEECG_GENERATE_TABLE_ID = getJeecg_generate_table_id();
    JEECG_GENERATE_UI_FILTER_FIELDS = getJeecg_generate_ui_filter_fields();
    JEECG_UI_FIELD_SEARCH_NUM = getJeecg_ui_search_filed_num();
    if ((URL.indexOf("mysql") >= 0) || (URL.indexOf("MYSQL") >= 0))
      DATABASE_TYPE = "mysql";
    else if ((URL.indexOf("oracle") >= 0) || (URL.indexOf("ORACLE") >= 0))
      DATABASE_TYPE = "oracle";
    else if ((URL.indexOf("postgresql") >= 0) || (URL.indexOf("POSTGRESQL") >= 0))
      DATABASE_TYPE = "postgresql";
    source_root_package = source_root_package.replace(".", "/");
    web_root_package = web_root_package.replace(".", "/");
    String str = bussiPackage.replace(".", "/");
    ENTITY_URL = source_root_package + "/" + str + "/" + entity_package + "/";
    PAGE_URL = source_root_package + "/" + str + "/" + page_package + "/";
    ENTITY_URL_INX = bussiPackage + "." + entity_package + ".";
    PAGE_URL_INX = bussiPackage + "." + page_package + ".";
    CODEPATH = source_root_package + "/" + str + "/";
    JSPPATH = web_root_package + "/" + "webpage" + "/" + str + "/";
  }

  public static final String getDIVER_NAME()
  {
    return a.getString("diver_name");
  }

  public static final String getURL()
  {
    return a.getString("url");
  }

  public static final String getUSERNAME()
  {
    return a.getString("username");
  }

  public static final String getPASSWORD()
  {
    return a.getString("password");
  }

  public static final String getDATABASE_NAME()
  {
    return a.getString("database_name");
  }

  public static final boolean getJEECG_FILED_CONVERT()
  {
    String str = b.getString("jeecg_filed_convert");
    return !str.toString().equals("false");
  }

  private static String a()
  {
    return b.getString("bussi_package");
  }

  public static final String getEntityPackage()
  {
    return b.getString("entity_package");
  }

  public static final String getPagePackage()
  {
    return b.getString("page_package");
  }

  public static final String getTEMPLATEPATH()
  {
    return b.getString("templatepath");
  }

  public static final String getSourceRootPackage()
  {
    return b.getString("source_root_package");
  }

  public static final String getWebRootPackage()
  {
    return b.getString("webroot_package");
  }

  public static final String getSYSTEM_ENCODING()
  {
    return b.getString("system_encoding");
  }

  public static final String getJeecg_generate_table_id()
  {
    return b.getString("jeecg_generate_table_id");
  }

  public static final String getJeecg_generate_ui_filter_fields()
  {
    return b.getString("ui_filter_fields");
  }

  public static final String getJeecg_ui_search_filed_num()
  {
    return b.getString("jeecg_ui_search_filed_num");
  }

  public static final String getJeecg_ui_field_required_num()
  {
    return b.getString("jeecg_ui_field_required_num");
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.util.CodeResourceUtil
 * JD-Core Version:    0.6.0
 */