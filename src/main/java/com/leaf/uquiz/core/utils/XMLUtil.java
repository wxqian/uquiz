package com.leaf.uquiz.core.utils;

import com.google.common.collect.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2014/12/2
 */
public final class XMLUtil {
    public static Map<String, String> parse(InputStream body) {
        try {
            InputSource source = new InputSource(new InputStreamReader(body, "UTF-8"));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document document = dbf.newDocumentBuilder().parse(source);
            NodeList childNodes = document.getElementsByTagName("xml").item(0).getChildNodes();
            Map<String, String> requestMap = Maps.newHashMap();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                requestMap.put(item.getNodeName(), item.getTextContent());
            }
            return requestMap;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static String toXML(Object obj) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj,writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return "";
        }
    }
}
