package org.jeecgframework.codegenerate.util;

import java.util.List;
import org.apache.commons.lang.StringUtils;

public class CodeStringUtils
{
  public static String getStringSplit(String[] paramArrayOfString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String[] arrayOfString = paramArrayOfString;
    int i = 0;
    int j = arrayOfString.length;
    while (i < j)
    {
      String str = arrayOfString[i];
      if (StringUtils.isNotBlank(str))
      {
        localStringBuffer.append(",");
        localStringBuffer.append("'");
        localStringBuffer.append(str.trim());
        localStringBuffer.append("'");
      }
      i++;
    }
    return localStringBuffer.toString().substring(1);
  }

  public static String getInitialSmall(String paramString)
  {
    if (StringUtils.isNotBlank(paramString))
      paramString = paramString.substring(0, 1).toLowerCase() + paramString.substring(1);
    return paramString;
  }

  public static Integer getIntegerNotNull(Integer paramInteger)
  {
    if (paramInteger == null)
      return Integer.valueOf(0);
    return paramInteger;
  }

  public static boolean isIn(String paramString, String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return false;
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      String str = paramArrayOfString[i];
      if (str.equals(paramString))
        return true;
    }
    return false;
  }

  @SuppressWarnings("rawtypes")
public static boolean isIn(String paramString, List paramList)
  {
    String[] arrayOfString = new String[0];
    if (paramList != null)
      arrayOfString = (String[])paramList.toArray();
    if ((arrayOfString == null) || (arrayOfString.length == 0))
      return false;
    for (int i = 0; i < arrayOfString.length; i++)
    {
      String str = arrayOfString[i];
      if (str.equals(paramString))
        return true;
    }
    return false;
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.util.CodeStringUtils
 * JD-Core Version:    0.6.0
 */