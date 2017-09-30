package com.wanglu.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 王璐 on 2017/9/30 0030.
 */
public class XmlParseUtil {

    //xml解析
    public static Map doXMLParse(String xmlStr) {
        Map<String, String> rstMap = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xmlStr.trim());
            Element root = document.getRootElement();
            // 获取根节点下的子节点
            Iterator<?> iter = root.elementIterator();
            while (iter.hasNext()) {
                Element child_1 = (Element) iter.next();
                rstMap.put(child_1.getName(), child_1.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return rstMap;
    }
}
