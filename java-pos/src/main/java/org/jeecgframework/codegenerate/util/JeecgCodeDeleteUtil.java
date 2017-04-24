package org.jeecgframework.codegenerate.util;

import java.io.File;

public class JeecgCodeDeleteUtil
{
  private static String a = CodeResourceUtil.bussiPackage;
  private static final String b = "src/" + a;
  private static final String c = "WebRoot/" + a;
  private static String d;
  private static String e;
  private static String f;
  private static String g;
  private static String h;
  private static String i;
  private static String j;
  private static String k;

  public static void init(String paramString1, String paramString2)
  {
    d = b + "/" + "action" + "/" + paramString1 + "/" + paramString2 + "Action.java";
    e = b + "/" + "entity" + "/" + paramString1 + "/" + paramString2 + "Entity.java";
    f = b + "/" + "page" + "/" + paramString1 + "/" + paramString2 + "Page.java";
    g = b + "/" + "service" + "/" + paramString1 + "/" + paramString2 + "ServiceI.java";
    h = b + "/" + "service" + "/" + "impl" + "/" + paramString1 + "/" + paramString2 + "ServiceImpl.java";
    i = c + "/" + paramString1 + "/" + paramString2 + ".jsp";
    j = c + "/" + paramString1 + "/" + paramString2 + "-main-add.jsp";
    k = c + "/" + paramString1 + "/" + paramString2 + "-main-edit.jsp";
    String str = getProjectPath();
    d = str + "/" + d;
    e = str + "/" + e;
    f = str + "/" + f;
    g = str + "/" + g;
    h = str + "/" + h;
    i = str + "/" + i;
    j = str + "/" + j;
    k = str + "/" + k;
  }

  public static void main(String[] paramArrayOfString)
  {
    String str1 = "Company";
    String str2 = "test";
    delCode(str2, str1);
  }

  public static void delCode(String paramString1, String paramString2)
  {
    init(paramString1, paramString2);
    delete(d);
    delete(e);
    delete(f);
    delete(g);
    delete(h);
    delete(k);
    delete(i);
    delete(j);
    System.out.println("--------------------删除动作结束-----------------------");
  }

  public static String getProjectPath()
  {
    String str = System.getProperty("user.dir").replace("\\", "/") + "/";
    return str;
  }

  public static boolean delete(String paramString)
  {
    File localFile = new File(paramString);
    if ((!localFile.exists()) || (!localFile.isFile()))
      return false;
    System.out.println("--------成功删除文件---------" + paramString);
    return localFile.delete();
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.util.JeecgCodeDeleteUtil
 * JD-Core Version:    0.6.0
 */