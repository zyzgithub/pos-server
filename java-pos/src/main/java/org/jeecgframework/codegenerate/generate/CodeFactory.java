package org.jeecgframework.codegenerate.generate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.codegenerate.util.CodeResourceUtil;
import org.jeecgframework.codegenerate.util.CodeStringUtils;

public class CodeFactory
{
  private ICallBack a;
  
  
  public enum CodeType
  {
	serviceImpl("ServiceImpl"),serviceI("ServiceI"),service("ServiceI"),jsp(""),jspList("List"),controller("Controller"),entity("Entity"),page("Page");
	private String jdField_a_of_type_JavaLangString;

    @SuppressWarnings("unused")
	private CodeType(String arg3)
    {
      Object localObject;
//      this.jdField_a_of_type_JavaLangString = localObject;
    	this.jdField_a_of_type_JavaLangString = arg3;
    }

    public String getValue()
    {
      return this.jdField_a_of_type_JavaLangString;
    }
  }
  @SuppressWarnings("deprecation")
public Configuration getConfiguration() throws IOException
  {
    Configuration localConfiguration = new Configuration();
    String str = getTemplatePath();
    File localFile = new File(str);
    localConfiguration.setDirectoryForTemplateLoading(localFile);
    localConfiguration.setLocale(Locale.CHINA);
    localConfiguration.setDefaultEncoding("UTF-8");
    return localConfiguration;
  }

  
  @SuppressWarnings("rawtypes")
public void generateFile(String ftlTemplateName, String fileType, Map paramMap)
  {
    try
    {
      String entityPackage = paramMap.get("entityPackage").toString();
      String entityName = paramMap.get("entityName").toString();
      String str3 = getCodePath(fileType, entityPackage, entityName);
      String str4 = StringUtils.substringBeforeLast(str3, "/");
      Template localTemplate = getConfiguration().getTemplate(ftlTemplateName);
      FileUtils.forceMkdir(new File(str4 + "/"));
      OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(str3), CodeResourceUtil.SYSTEM_ENCODING);
      localTemplate.process(paramMap, localOutputStreamWriter);
      localOutputStreamWriter.close();
    }
    catch (TemplateException localTemplateException)
    {
      localTemplateException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public static String getProjectPath()
  {
    String str = System.getProperty("user.dir").replace("\\", "/") + "/";
    return str;
  }

  public String getClassPath()
  {
    String str = Thread.currentThread().getContextClassLoader().getResource("./").getPath();
    return str;
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println(getProjectPath());
  }

  public String getTemplatePath()
  {
    String str = getClassPath() + CodeResourceUtil.TEMPLATEPATH;
    return str;
  }

  /**
   * 根据指定的类型返回需要生成的文件路径(seviceImpl,Jsp)
   * 
   * @param fileType
   * @param entityPackage
   * @param entityName
   * @return
   * @time 2013-7-22 下午2:54:20   @author WEIZHANG_CHEN
   */
  public String getCodePath(String fileType, String entityPackage, String entityName)
  {
    String projectPath = getProjectPath();
    StringBuilder localStringBuilder = new StringBuilder();
    if (StringUtils.isNotBlank(fileType))
    {
//    String str2 = paramString1;
      localStringBuilder.append(projectPath);
      String str2 = ((CodeFactory.CodeType)Enum.valueOf(CodeFactory.CodeType.class, fileType)).getValue();
      if (("jsp".equals(fileType)) || ("jspList".equals(fileType)))
        localStringBuilder.append(CodeResourceUtil.JSPPATH);
      else
        localStringBuilder.append(CodeResourceUtil.CODEPATH);
      if ("Action".equalsIgnoreCase(str2))
        localStringBuilder.append(StringUtils.lowerCase("action"));
      else if ("ServiceImpl".equalsIgnoreCase(str2))
        localStringBuilder.append(StringUtils.lowerCase("service/impl"));
      else if ("ServiceI".equalsIgnoreCase(str2))
        localStringBuilder.append(StringUtils.lowerCase("service"));
      else if (!"List".equalsIgnoreCase(str2))
        localStringBuilder.append(StringUtils.lowerCase(str2));
      localStringBuilder.append("/");
      localStringBuilder.append(StringUtils.lowerCase(entityPackage));
      localStringBuilder.append("/");
      if (("jsp".equals(fileType)) || ("jspList".equals(fileType)))
      {
        String str3 = StringUtils.capitalize(entityName);
        localStringBuilder.append(CodeStringUtils.getInitialSmall(str3));
        localStringBuilder.append(str2);
        localStringBuilder.append(".jsp");
      }
      else
      {
        localStringBuilder.append(StringUtils.capitalize(entityName));
        localStringBuilder.append(str2);
        localStringBuilder.append(".java");
      }
    }
    else
    {
      throw new IllegalArgumentException("type is null");
    }
    return localStringBuilder.toString();
  }

  
  @SuppressWarnings("rawtypes")
public void invoke(String ftlTemplateName, String fileType)
  {
    Object localObject = new HashMap();
    localObject = this.a.execute();
    generateFile(ftlTemplateName, fileType, (Map)localObject);
  }

  public ICallBack getCallBack()
  {
    return this.a;
  }

  public void setCallBack(ICallBack paramICallBack)
  {
    this.a = paramICallBack;
  }
}

/* Location:           E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB-INF\lib\org.jeecgframework.codegenerate.jar
 * Qualified Name:     org.jeecgframework.codegenerate.generate.CodeFactory
 * JD-Core Version:    0.6.0
 */