package org.jeecgframework.codegenerate.generate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jeecgframework.codegenerate.database.JeecgReadTable;
import org.jeecgframework.codegenerate.pojo.Columnt;
import org.jeecgframework.codegenerate.pojo.CreateFileProperty;
import org.jeecgframework.codegenerate.util.CodeDateUtils;
import org.jeecgframework.codegenerate.util.CodeResourceUtil;
import org.jeecgframework.codegenerate.util.NonceUtils;
import org.jeecgframework.codegenerate.util.def.FtlDef;

public class CodeGenerate
  implements ICallBack
{
  private static final Log log = LogFactory.getLog(CodeGenerate.class);
  private static String packName = "test";
  private static String entityName = "Person";
  private static String tableName = "person";
  private static String description = "公告";
  private static String sequence = "identity";
  private static String f = "";
  private static String[] foreignKeys;
  @SuppressWarnings("rawtypes")
private List jdField_a_of_type_JavaUtilList = new ArrayList();
  public static int FIELD_ROW_NUM = 1;
  private static CreateFileProperty createFileProperty = new CreateFileProperty();
  @SuppressWarnings("rawtypes")
private List jdField_b_of_type_JavaUtilList = new ArrayList();
  private JeecgReadTable jeecgReadTable = new JeecgReadTable();

  static
  {
    createFileProperty.setActionFlag(true);
    createFileProperty.setServiceIFlag(true);
    createFileProperty.setJspFlag(true);
    createFileProperty.setServiceImplFlag(true);
    createFileProperty.setJspMode("01");
    createFileProperty.setPageFlag(true);
    createFileProperty.setEntityFlag(true);
  }

  public CodeGenerate()
  {
  }

  @SuppressWarnings("static-access")
public CodeGenerate(String packName, String entityName, String tableName, String description, CreateFileProperty paramCreateFileProperty, int paramInt, String sequence, String paramString6)
  {
    this.entityName = entityName;
    this.packName = packName;
    this.tableName = tableName;
    this.description = description;
    this.createFileProperty = paramCreateFileProperty;
    FIELD_ROW_NUM = paramInt;
    this.sequence = sequence;
    f = paramString6;
  }

  @SuppressWarnings("static-access")
public CodeGenerate(String packName, String entityName, String tableName, String description, CreateFileProperty paramCreateFileProperty, String sequence, String paramString6)
  {
	this.entityName = entityName;
	this.packName = packName;
    this.tableName = tableName;
    this.description = description;
    this.createFileProperty = paramCreateFileProperty;
    this.sequence = sequence;
    f = paramString6;
  }

  @SuppressWarnings("static-access")
public CodeGenerate(String packName, String entityName, String tableName, String description, CreateFileProperty paramCreateFileProperty, String sequence, String paramString6, String[] foreignKeys)
  {
	this.entityName = entityName;
	this.packName = packName;
    this.tableName = tableName;
    this.description = description;
    this.createFileProperty = paramCreateFileProperty;
    this.sequence = sequence;
    f = paramString6;
    this.foreignKeys = foreignKeys;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public Map execute()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("bussiPackage", CodeResourceUtil.bussiPackage);
    localHashMap.put("entityPackage", packName);
    localHashMap.put("entityName", entityName);
    localHashMap.put("tableName", tableName);
    localHashMap.put("ftl_description", description);
    localHashMap.put(FtlDef.JEECG_TABLE_ID, CodeResourceUtil.JEECG_GENERATE_TABLE_ID);
    localHashMap.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, sequence);
    localHashMap.put(FtlDef.JEECG_SEQUENCE_CODE, f);
    localHashMap.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));
    localHashMap.put("foreignKeys", foreignKeys);
    localHashMap.put(FtlDef.FIELD_REQUIRED_NAME, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) : -1));
    localHashMap.put(FtlDef.SEARCH_FIELD_NUM, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) : -1));
    localHashMap.put(FtlDef.FIELD_ROW_NAME, Integer.valueOf(FIELD_ROW_NUM));
    try
    {
      this.jdField_b_of_type_JavaUtilList = this.jeecgReadTable.readTableColumn(tableName);
      localHashMap.put("columns", this.jdField_b_of_type_JavaUtilList);
      this.jdField_a_of_type_JavaUtilList = this.jeecgReadTable.readOriginalTableColumn(tableName);
      localHashMap.put("originalColumns", this.jdField_a_of_type_JavaUtilList);
      Iterator localIterator = this.jdField_a_of_type_JavaUtilList.iterator();
      while (localIterator.hasNext())
      {
        Columnt localColumnt = (Columnt)localIterator.next();
        if (!localColumnt.getFieldName().toLowerCase().equals(CodeResourceUtil.JEECG_GENERATE_TABLE_ID.toLowerCase()))
          continue;
        localHashMap.put("primary_key_type", localColumnt.getFieldType());
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.exit(-1);
    }
    long l = NonceUtils.randomLong() + NonceUtils.currentMills();
    localHashMap.put("serialVersionUID", String.valueOf(l));
    return localHashMap;
  }

  public void generateToFile()
  {
    log.info("----QDPlat---Code----Generation----[单表模型:" + tableName + "]------- 生成中。。。");
    CodeFactory localCodeFactory = new CodeFactory();
    localCodeFactory.setCallBack(new CodeGenerate());
    if (createFileProperty.isJspFlag())
      if ("03".equals(createFileProperty.getJspMode()))
      {
        localCodeFactory.invoke("onetomany/jspSubTemplate.ftl", "jspList");
      }
      else
      {
        if ("01".equals(createFileProperty.getJspMode()))
          localCodeFactory.invoke("jspTableTemplate.ftl", "jsp");
        if ("02".equals(createFileProperty.getJspMode()))
          localCodeFactory.invoke("jspDivTemplate.ftl", "jsp");
        localCodeFactory.invoke("jspListTemplate.ftl", "jspList");
      }
    if (createFileProperty.isServiceImplFlag())
      localCodeFactory.invoke("serviceImplTemplate.ftl", "serviceImpl");
    if (createFileProperty.isServiceIFlag())
      localCodeFactory.invoke("serviceITemplate.ftl", "service");
    if (createFileProperty.isActionFlag())
      localCodeFactory.invoke("controllerTemplate.ftl", "controller");
    if (createFileProperty.isEntityFlag())
      localCodeFactory.invoke("entityTemplate.ftl", "entity");
    log.info("----QDPlat----Code----Generation-----[单表模型：" + tableName + "]------ 生成完成。。。");
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("----QDPlat--------- Code------------- Generation -----[单表模型]------- 生成中。。。");
    new CodeGenerate().generateToFile();
    System.out.println("----QDPlat--------- Code------------- Generation -----[单表模型]------- 生成完成。。。");
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.generate.CodeGenerate
 * JD-Core Version:    0.6.0
 */