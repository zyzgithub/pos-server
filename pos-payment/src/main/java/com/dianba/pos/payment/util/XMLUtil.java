package com.dianba.pos.payment.util;

import com.dianba.pos.common.util.JaxbUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XMLUtil {

    private static Logger logger = LogManager.getLogger(XMLUtil.class);

    public static Map<String, String> getRequestXML(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        try {
            String xmlData = "";
            ServletInputStream sis = request.getInputStream();
            request.setCharacterEncoding("UTF-8");
            // 取HTTP请求流长度
            int size = request.getContentLength();
            // 用于缓存每次读取的数据
            byte[] buffer = new byte[size];
            // 用于存放结果的数组
            byte[] xmldataByte = new byte[size];
            int count = 0;
            int rbyte = 0;
            // 循环读取
            while (count < size) {
                // 每次实际读取长度存于rbyte中
                rbyte = sis.read(buffer);
                System.arraycopy(buffer, 0, xmldataByte, count, rbyte);
                count += rbyte;
            }
            xmlData = new String(xmldataByte, "UTF-8");
            logger.info("解析xml为：" + xmlData);
            //将传入的xml字符串转换为map集合
            params = doXMLParse(xmlData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static void setResponseXML(HttpServletResponse response, Object object) {
        try {
            String xml = JaxbUtil.convertToXml(object);
            PrintWriter out = response.getWriter();
            ServletInputStream sis = null;
            response.setCharacterEncoding("UTF-8");
            response.setContentType("textml; charset=UTF-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            out.print(xml);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map<String, String> m = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            String k = e.getName();
            String v = "";
            List<Element> children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }
            m.put(k, v);
        }
        //关闭流
        in.close();
        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    @SuppressWarnings("unchecked")
    public static String getChildrenText(List<Element> children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator<Element> it = children.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List<Element> list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * 返回给微信服务的消息
     *
     * @param returnCode
     * @param returnMsg
     * @return
     */
    public static String setXML(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode
                + "]]></return_code><return_msg><![CDATA[" + returnMsg
                + "]]></return_msg></xml>";
    }

}
