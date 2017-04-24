package org.jeecgframework.codegenerate.pojo.onetomany;

import java.util.List;

public class CodeParamEntity
{
  private String jdField_a_of_type_JavaLangString;
  private String tableName;
  private String entityName;
  private String d;
  private String sequence;
  private String f;
  private String g = "A";
  @SuppressWarnings("rawtypes")
List jdField_a_of_type_JavaUtilList;

  @SuppressWarnings("rawtypes")
public List getSubTabParam()
  {
    return this.jdField_a_of_type_JavaUtilList;
  }

  @SuppressWarnings("rawtypes")
public void setSubTabParam(List paramList)
  {
    this.jdField_a_of_type_JavaUtilList = paramList;
  }

  public String getEntityPackage()
  {
    return this.jdField_a_of_type_JavaLangString;
  }

  public String getTableName()
  {
    return this.tableName;
  }

  public String getEntityName()
  {
    return this.entityName;
  }

  public String getFtlDescription()
  {
    return this.d;
  }

  public void setEntityPackage(String paramString)
  {
    this.jdField_a_of_type_JavaLangString = paramString;
  }

  public void setTableName(String paramString)
  {
    this.tableName = paramString;
  }

  public void setEntityName(String paramString)
  {
    this.entityName = paramString;
  }

  public void setFtlDescription(String paramString)
  {
    this.d = paramString;
  }

  public String getFtl_mode()
  {
    return this.g;
  }

  public void setFtl_mode(String paramString)
  {
    this.g = paramString;
  }

  public String getPrimaryKeyPolicy()
  {
    return this.sequence;
  }

  public String getSequenceCode()
  {
    return this.f;
  }

  public void setPrimaryKeyPolicy(String paramString)
  {
    this.sequence = paramString;
  }

  public void setSequenceCode(String paramString)
  {
    this.f = paramString;
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.pojo.onetomany.CodeParamEntity
 * JD-Core Version:    0.6.0
 */