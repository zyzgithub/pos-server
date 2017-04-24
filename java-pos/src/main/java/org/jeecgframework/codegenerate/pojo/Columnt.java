package org.jeecgframework.codegenerate.pojo;

public class Columnt
{
  public static final String OPTION_REQUIRED = "required:true";
  public static final String OPTION_NUMBER_INSEX = "precision:2,groupSeparator:','";
  private String a;
  private String b;
  private String c = "";
  private String d = "";
  private String e = "";
  private String f = "";
  private String g = "";
  private String h = "";
  private String i;
  private String j;
  private String k;

  public String getNullable()
  {
    return this.k;
  }

  public void setNullable(String paramString)
  {
    this.k = paramString;
  }

  public String getPrecision()
  {
    return this.i;
  }

  public String getScale()
  {
    return this.j;
  }

  public void setPrecision(String paramString)
  {
    this.i = paramString;
  }

  public void setScale(String paramString)
  {
    this.j = paramString;
  }

  public String getOptionType()
  {
    return this.g;
  }

  public void setOptionType(String paramString)
  {
    this.g = paramString;
  }

  public String getClassType()
  {
    return this.e;
  }

  public void setClassType(String paramString)
  {
    this.e = paramString;
  }

  public String getFieldType()
  {
    return this.d;
  }

  public void setFieldType(String paramString)
  {
    this.d = paramString;
  }

  public String getFieldName()
  {
    return this.b;
  }

  public void setFieldName(String paramString)
  {
    this.b = paramString;
  }

  public String getFiledComment()
  {
    return this.c;
  }

  public void setFiledComment(String paramString)
  {
    this.c = paramString;
  }

  public String getClassType_row()
  {
    if ((this.e != null) && (this.e.indexOf("easyui-") >= 0))
      return this.e.replaceAll("easyui-", "");
    return this.f;
  }

  public void setClassType_row(String paramString)
  {
    this.f = paramString;
  }

  public String getCharmaxLength()
  {
    if ((this.h == null) || ("0".equals(this.h)))
      return "";
    return this.h;
  }

  public void setCharmaxLength(String paramString)
  {
    this.h = paramString;
  }

  public String getFieldDbName()
  {
    return this.a;
  }

  public void setFieldDbName(String paramString)
  {
    this.a = paramString;
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.pojo.Columnt
 * JD-Core Version:    0.6.0
 */