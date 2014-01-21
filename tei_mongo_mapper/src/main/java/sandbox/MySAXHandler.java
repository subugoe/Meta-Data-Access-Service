package sandbox;

import com.mongodb.BasicDBObject;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by jpanzer on 17.01.14.
 */
public class MySAXHandler extends DefaultHandler {


    private  BasicDBObject textBasicDBObject;
    private  BasicDBObject hdrBasicDBObject;
    private  List<BasicDBObject> hdrBasicDBObjectList;
    private  List<BasicDBObject> bodyBasicDBObjectList;
    private  Map<String, String> attributeMap;
    private Hashtable tags;
    private boolean hdr = false;
    private boolean body = false;
    private String currentValue;

    public MySAXHandler(String filePath) throws SAXException, ParserConfigurationException, IOException {

        this.hdrBasicDBObject = new BasicDBObject();
        this.textBasicDBObject = new BasicDBObject();

        String path = new File(filePath).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        FileReader fileReader = new FileReader(path);
        InputSource inputSource = new InputSource(fileReader);


        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);

        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);


        xmlReader.parse(inputSource);


    }

    public void startDocument() throws SAXException {


    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {

//        if (localName.equalsIgnoreCase("teiHeader")) {
//            this.hdr = true;
//        } else if (localName.equalsIgnoreCase("body"))
//            this.body = true;
//
//        if (this.hdr) {
//            hdrBasicDBObject = buildBasicDBObject(localName);
//            hdrBasicDBObject.append("hdrAttributes", attributeMap);
//        }   else if (this.body) {
//
//        }
//
//            System.out.println(namespaceURI + " : " + localName + " : " + qName + " : " + atts.toString());

    }

    private BasicDBObject buildBasicDBObject(String localName, Map<String, String> map) {

//        hdrBasicDBObject = new BasicDBObject();
//        attributeMap = new HashMap();
//        for (int i=0; i<atts.getLength();i++) {
//            map.put(atts.getLocalName(i), atts.getValue(i));
//        }
//        hdrBasicDBObject.append("hdrAttributes", attributeMap);

        return null;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("teiHeader")) {
            this.hdr = false;
        }  else if (localName.equalsIgnoreCase("body")) {
            this.body = false;
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue = new String(ch, start, length);
    }
}
