package com.dianba.pos.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LogManager.getLogger(PropertiesUtil.class);

    public static Properties getPropertiesByFileOrClassPath(Class objClass, String propertiesFileName
            , boolean modelInLib) {
        Properties properties;

        String path = objClass.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path);
        String realPath = file.getParent();
        if (modelInLib) {
            realPath = new File(realPath).getParent();
        }
        File propertiesFile = new File(realPath + File.separator + propertiesFileName);
        if (propertiesFile.exists()) {
            properties = loadProperties(propertiesFile.getPath());
            logger.info("加载配置文件：" + propertiesFile.getPath());
            return properties;
        } else {
            InputStream inputStream = objClass.getResourceAsStream(File.separator+propertiesFileName);
            properties=loadProperties(inputStream);
            logger.info("加载配置文件：" + path);
            return properties;
        }
    }

    public static Properties getPropertiesByFileOrClassPath(Class objClass, String propertiesFileName) {
        return getPropertiesByFileOrClassPath(objClass, propertiesFileName, true);
    }


    private static Properties loadProperties(Object fileType) {
        Properties properties = new Properties();
        try {
            InputStream inputStream;
            if (fileType instanceof String) {
                inputStream = new FileInputStream(fileType.toString());
            } else {
                inputStream = (InputStream) fileType;
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            properties.load(bf);
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
