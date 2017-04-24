package org.jeecgframework.codegenerate.pojo.onetomany;

import java.util.List;

public class SubTableEntity
{
  private String jdField_a_of_type_JavaLangString;
  private String b;
  private String c;
  private String d;
  private String e;
  private String f;
  private String[] jdField_a_of_type_ArrayOfJavaLangString;
  @SuppressWarnings("rawtypes")
private List jdField_a_of_type_JavaUtilList;

  public String getEntityPackage()
  {
    return this.jdField_a_of_type_JavaLangString;
  }

  public String getTableName()
  {
    return this.b;
  }

  public String getEntityName()
  {
    return this.c;
  }

  public String getFtlDescription()
  {
    return this.f;
  }

  @SuppressWarnings("rawtypes")
public List getSubColums()
  {
    return this.jdField_a_of_type_JavaUtilList;
  }

  public void setEntityPackage(String paramString)
  {
    this.jdField_a_of_type_JavaLangString = paramString;
  }

  public void setTableName(String paramString)
  {
    this.b = paramString;
  }

  public void setEntityName(String paramString)
  {
    this.c = paramString;
  }

  public void setFtlDescription(String paramString)
  {
    this.f = paramString;
  }

  @SuppressWarnings("rawtypes")
public void setSubColums(List paramList)
  {
    this.jdField_a_of_type_JavaUtilList = paramList;
  }

  public String[] getForeignKeys()
  {
    return this.jdField_a_of_type_ArrayOfJavaLangString;
  }

  public void setForeignKeys(String[] paramArrayOfString)
  {
    this.jdField_a_of_type_ArrayOfJavaLangString = paramArrayOfString;
  }

  public String getPrimaryKeyPolicy()
  {
    return this.d;
  }

  public String getSequenceCode()
  {
    return this.e;
  }

  public void setPrimaryKeyPolicy(String paramString)
  {
    this.d = paramString;
  }

  public void setSequenceCode(String paramString)
  {
    this.e = paramString;
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.pojo.onetomany.SubTableEntity
 * JD-Core Version:    0.6.0
 */