package Classes;

import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.*;

/**
 * Created by rkurbanov on 28.02.2017.
 */
public class Cofigure {

    public static String port="";
    public static String savepath="";
    public static String savepathtxt="";
    public static String savebadpath="";

    public static void UpdateConfiguration()
    {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = null;
        try {
            builder = f.newDocumentBuilder();
            Document doc = builder.parse(new File("config.xml"));
            NodeList nodeList = doc.getElementsByTagName("config");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;

                    port=element.getElementsByTagName("port").item(0).getTextContent();
                    savepath=element.getElementsByTagName("savepath").item(0).getTextContent();
                    savepathtxt=element.getElementsByTagName("savepathtxt").item(0).getTextContent();
                    savebadpath=element.getElementsByTagName("savebadpath").item(0).getTextContent();
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
