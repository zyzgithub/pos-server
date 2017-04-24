package org.jeecgframework.codegenerate.generate.onetomany;

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
import org.jeecgframework.codegenerate.generate.CodeGenerate;
import org.jeecgframework.codegenerate.generate.ICallBack;
import org.jeecgframework.codegenerate.pojo.Columnt;
import org.jeecgframework.codegenerate.pojo.CreateFileProperty;
import org.jeecgframework.codegenerate.pojo.onetomany.CodeParamEntity;
import org.jeecgframework.codegenerate.pojo.onetomany.SubTableEntity;
import org.jeecgframework.codegenerate.util.CodeDateUtils;
import org.jeecgframework.codegenerate.util.CodeResourceUtil;
import org.jeecgframework.codegenerate.util.NonceUtils;
import org.jeecgframework.codegenerate.util.def.FtlDef;
import org.jeecgframework.codegenerate.util.def.JeecgKey;

public class CodeGenerateOneToMany
  implements ICallBack
{
  private static final Log jdField_a_of_type_OrgApacheCommonsLoggingLog = LogFactory.getLog(CodeGenerateOneToMany.class);
  private static String entityPackage = "test";
  private static String entityName = "Person";
  private static String tableName = "person";
  private static String ftlDescription = "用户";
  private static String e = "uuid";
  private static String f = "";
  @SuppressWarnings("unused")
private static String g;
  public static String FTL_MODE_A = "A";
  public static String FTL_MODE_B = "B";
  @SuppressWarnings("rawtypes")
private static List jdField_a_of_type_JavaUtilList = new ArrayList();
  private static CreateFileProperty jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty = new CreateFileProperty();
  public static int FIELD_ROW_NUM = 4;
  @SuppressWarnings("rawtypes")
private List jdField_b_of_type_JavaUtilList = new ArrayList();
  @SuppressWarnings("rawtypes")
private List jdField_c_of_type_JavaUtilList = new ArrayList();
  @SuppressWarnings("rawtypes")
private List jdField_d_of_type_JavaUtilList = new ArrayList();
  private static JeecgReadTable jdField_a_of_type_OrgJeecgframeworkCodegenerateDatabaseJeecgReadTable = new JeecgReadTable();

  static
  {
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setActionFlag(true);
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setServiceIFlag(true);
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setJspFlag(true);
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setServiceImplFlag(true);
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setPageFlag(true);
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.setEntityFlag(true);
  }

  public CodeGenerateOneToMany()
  {
  }

  @SuppressWarnings("rawtypes")
public CodeGenerateOneToMany(String paramString1, String paramString2, String paramString3, List paramList, String paramString4, CreateFileProperty paramCreateFileProperty, String paramString5, String paramString6)
  {
    entityName = paramString2;
    entityPackage = paramString1;
    tableName = paramString3;
    ftlDescription = paramString4;
    jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty = paramCreateFileProperty;
    jdField_a_of_type_JavaUtilList = paramList;
    e = StringUtils.isNotBlank(paramString5) ? paramString5 : "identity";
    f = paramString6;
  }

  public CodeGenerateOneToMany(CodeParamEntity paramCodeParamEntity)
  {
    entityName = paramCodeParamEntity.getEntityName();
    entityPackage = paramCodeParamEntity.getEntityPackage();
    tableName = paramCodeParamEntity.getTableName();
    ftlDescription = paramCodeParamEntity.getFtlDescription();
    jdField_a_of_type_JavaUtilList = paramCodeParamEntity.getSubTabParam();
    g = paramCodeParamEntity.getFtl_mode();
    e = StringUtils.isNotBlank(paramCodeParamEntity.getPrimaryKeyPolicy()) ? paramCodeParamEntity.getPrimaryKeyPolicy() : "identity";
    f = paramCodeParamEntity.getSequenceCode() == null?"id":paramCodeParamEntity.getSequenceCode();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public Map execute()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("bussiPackage", CodeResourceUtil.bussiPackage);
    localHashMap.put("entityPackage", entityPackage);
    localHashMap.put("entityName", entityName);
    localHashMap.put("tableName", tableName);
    localHashMap.put("ftl_description", ftlDescription);
    localHashMap.put("jeecg_table_id", CodeResourceUtil.JEECG_GENERATE_TABLE_ID);
    localHashMap.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, e);
    localHashMap.put(FtlDef.JEECG_SEQUENCE_CODE, f);
    localHashMap.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));
    localHashMap.put(FtlDef.FIELD_REQUIRED_NAME, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) : -1));
    localHashMap.put(FtlDef.SEARCH_FIELD_NUM, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) : -1));
    localHashMap.put(FtlDef.FIELD_ROW_NAME, Integer.valueOf(FIELD_ROW_NUM));
    try
    {
      this.jdField_b_of_type_JavaUtilList = jdField_a_of_type_OrgJeecgframeworkCodegenerateDatabaseJeecgReadTable.readTableColumn(tableName);
      localHashMap.put("mainColums", this.jdField_b_of_type_JavaUtilList);
      localHashMap.put("columns", this.jdField_b_of_type_JavaUtilList);
      this.jdField_c_of_type_JavaUtilList = jdField_a_of_type_OrgJeecgframeworkCodegenerateDatabaseJeecgReadTable.readOriginalTableColumn(tableName);
      localHashMap.put("originalColumns", this.jdField_c_of_type_JavaUtilList);
      Iterator localIterator = this.jdField_c_of_type_JavaUtilList.iterator();
      Object localObject;
      while (localIterator.hasNext())
      {
        localObject = (Columnt)localIterator.next();
        if (!((Columnt)localObject).getFieldName().toLowerCase().equals(CodeResourceUtil.JEECG_GENERATE_TABLE_ID.toLowerCase()))
          continue;
        localHashMap.put("primary_key_type", ((Columnt)localObject).getFieldType());
      }
      this.jdField_d_of_type_JavaUtilList.clear();
      localIterator = jdField_a_of_type_JavaUtilList.iterator();
      while (localIterator.hasNext())
      {
        localObject = (SubTableEntity)localIterator.next();
        SubTableEntity localSubTableEntity = new SubTableEntity();
        List localList = jdField_a_of_type_OrgJeecgframeworkCodegenerateDatabaseJeecgReadTable.readTableColumn(((SubTableEntity)localObject).getTableName());
        localSubTableEntity.setSubColums(localList);
        localSubTableEntity.setEntityName(((SubTableEntity)localObject).getEntityName());
        localSubTableEntity.setFtlDescription(((SubTableEntity)localObject).getFtlDescription());
        localSubTableEntity.setTableName(((SubTableEntity)localObject).getTableName());
        localSubTableEntity.setEntityPackage(((SubTableEntity)localObject).getEntityPackage());
        String[] arrayOfString1 = ((SubTableEntity)localObject).getForeignKeys();
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString2 = arrayOfString1;
        int i = 0;
        int j = arrayOfString2.length;
        while (i < j)
        {
          String str1 = arrayOfString2[i];
          if (CodeResourceUtil.JEECG_FILED_CONVERT)
          {
            localArrayList.add(JeecgReadTable.formatFieldCapital(str1));
          }
          else
          {
            String str2 = str1.toLowerCase();
            String str3 = str2.substring(0, 1).toUpperCase() + str2.substring(1);
            localArrayList.add(str3);
          }
          i++;
        }
        localSubTableEntity.setForeignKeys((String[])localArrayList.toArray(new String[0]));
        this.jdField_d_of_type_JavaUtilList.add(localSubTableEntity);
      }
      localHashMap.put("subTab", this.jdField_d_of_type_JavaUtilList);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.exit(-1);
    }
    long l = NonceUtils.randomLong() + NonceUtils.currentMills();
    localHashMap.put("serialVersionUID", String.valueOf(l));
    return (Map)localHashMap;
  }

  public void generateToFile()
  {
    CodeFactoryOneToMany localCodeFactoryOneToMany = new CodeFactoryOneToMany();
    localCodeFactoryOneToMany.setCallBack(new CodeGenerateOneToMany());
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isJspFlag())
    {
      localCodeFactoryOneToMany.invoke("onetomany/jspListTemplate.ftl", "jspList");
      localCodeFactoryOneToMany.invoke("onetomany/jspTemplate.ftl", "jsp");
    }
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isServiceImplFlag())
      localCodeFactoryOneToMany.invoke("onetomany/serviceImplTemplate.ftl", "serviceImpl");
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isServiceIFlag())
      localCodeFactoryOneToMany.invoke("onetomany/serviceITemplate.ftl", "service");
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isActionFlag())
      localCodeFactoryOneToMany.invoke("onetomany/controllerTemplate.ftl", "controller");
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isEntityFlag())
      localCodeFactoryOneToMany.invoke("onetomany/entityTemplate.ftl", "entity");
    if (jdField_a_of_type_OrgJeecgframeworkCodegeneratePojoCreateFileProperty.isPageFlag())
      localCodeFactoryOneToMany.invoke("onetomany/pageTemplate.ftl", "page");
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
public static void main(String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList();
    SubTableEntity localSubTableEntity1 = new SubTableEntity();
    localSubTableEntity1.setTableName("jeecg_order_custom");
    localSubTableEntity1.setEntityName("OrderCustom");
    localSubTableEntity1.setEntityPackage("order");
    localSubTableEntity1.setFtlDescription("订单客户明细");
    localSubTableEntity1.setPrimaryKeyPolicy(JeecgKey.UUID);
    localSubTableEntity1.setSequenceCode(null);
    localSubTableEntity1.setForeignKeys(new String[] { "GORDER_OBID", "GO_ORDER_CODE" });
    localArrayList.add(localSubTableEntity1);
    SubTableEntity localSubTableEntity2 = new SubTableEntity();
    localSubTableEntity2.setTableName("jeecg_order_product");
    localSubTableEntity2.setEntityName("OrderProduct");
    localSubTableEntity2.setEntityPackage("order");
    localSubTableEntity2.setFtlDescription("订单产品明细");
    localSubTableEntity2.setForeignKeys(new String[] { "GORDER_OBID", "GO_ORDER_CODE" });
    localSubTableEntity2.setPrimaryKeyPolicy(JeecgKey.UUID);
    localSubTableEntity2.setSequenceCode(null);
    localArrayList.add(localSubTableEntity2);
    CodeParamEntity localCodeParamEntity = new CodeParamEntity();
    localCodeParamEntity.setTableName("jeecg_order_main");
    localCodeParamEntity.setEntityName("OrderMain");
    localCodeParamEntity.setEntityPackage("order");
    localCodeParamEntity.setFtlDescription("订单抬头");
    localCodeParamEntity.setFtl_mode(FTL_MODE_B);
    localCodeParamEntity.setPrimaryKeyPolicy(JeecgKey.UUID);
    localCodeParamEntity.setSequenceCode(null);
    localCodeParamEntity.setSubTabParam(localArrayList);
    oneToManyCreate(localArrayList, localCodeParamEntity);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public static void oneToManyCreate(List paramList, CodeParamEntity paramCodeParamEntity)
  {
    jdField_a_of_type_OrgApacheCommonsLoggingLog.info("----jeecg----Code-----Generation-----[一对多数据模型：" + paramCodeParamEntity.getTableName() + "]------- 生成中。。。");
    CreateFileProperty localCreateFileProperty = new CreateFileProperty();
    localCreateFileProperty.setActionFlag(false);
    localCreateFileProperty.setServiceIFlag(false);
    localCreateFileProperty.setJspFlag(true);
    localCreateFileProperty.setServiceImplFlag(false);
    localCreateFileProperty.setPageFlag(false);
    localCreateFileProperty.setEntityFlag(true);
    localCreateFileProperty.setJspMode("03");
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SubTableEntity localSubTableEntity = (SubTableEntity)localIterator.next();
      String[] arrayOfString1 = localSubTableEntity.getForeignKeys();
      ArrayList localArrayList = new ArrayList();
      String[] arrayOfString2 = arrayOfString1;
      int i = 0;
      int j = arrayOfString2.length;
      while (i < j)
      {
        String str1 = arrayOfString2[i];
        if (CodeResourceUtil.JEECG_FILED_CONVERT)
        {
          localArrayList.add(JeecgReadTable.formatFieldCapital(str1));
        }
        else
        {
          String str2 = str1.toLowerCase();
          String str3 = str2.substring(0, 1).toUpperCase() + str2.substring(1);
          localArrayList.add(str3);
        }
        i++;
      }
      new CodeGenerate(localSubTableEntity.getEntityPackage(), localSubTableEntity.getEntityName(), localSubTableEntity.getTableName(), localSubTableEntity.getFtlDescription(), localCreateFileProperty, StringUtils.isNotBlank(localSubTableEntity.getPrimaryKeyPolicy()) ? localSubTableEntity.getPrimaryKeyPolicy() : "identity", localSubTableEntity.getSequenceCode(), (String[])localArrayList.toArray(new String[0])).generateToFile();
    }
    new CodeGenerateOneToMany(paramCodeParamEntity).generateToFile();
    jdField_a_of_type_OrgApacheCommonsLoggingLog.info("----jeecg----Code----Generation------[一对多数据模型：" + paramCodeParamEntity.getTableName() + "]------ 生成完成。。。");
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.generate.onetomany.CodeGenerateOneToMany
 * JD-Core Version:    0.6.0
 */