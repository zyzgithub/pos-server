package com.dianba.pos.payment.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class XMLUtil {
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

    public static void main(String[] args) throws JDOMException, IOException {
        String string = PayService.createFailXml("ERROR");
        System.out.println(doXMLParse(string));
    }

}
