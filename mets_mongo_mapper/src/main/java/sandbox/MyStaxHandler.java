package sandbox;

import com.mongodb.BasicDBObject;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 20.01.14.
 */
public class MyStaxHandler {


    //private final String filePath;
    //private final FileInputStream fileReader;
    private final XMLInputFactory factory;
    //private final XMLEventReader xmlEventReader;
    private final XMLStreamReader xmlStreamReader;
    private XMLElementHandler xmlElementHandler;


    public MyStaxHandler(InputStream fileInputStream) throws FileNotFoundException, XMLStreamException {
        //this.filePath = filePath;

        //this.fileReader = new FileInputStream(filePath);

        this.factory = XMLInputFactory.newFactory();
        //this.xmlEventReader = factory.createXMLEventReader(fileReader);
        this.xmlStreamReader = factory.createXMLStreamReader(fileInputStream);

    }

    public BasicDBObject processXMLFile() throws XMLStreamException {

        int eventType;

        List<BasicDBObject> hdrBasicDBObjectLst = new ArrayList<BasicDBObject>();
        List<BasicDBObject> bodyBasicDBObjectLst = new ArrayList<BasicDBObject>();
        BasicDBObject basicDBObject = null;
        XMLElementHandler xmlElementHandler = null;

        int count = 0;

        for (int event = this.xmlStreamReader.next();

             event != XMLStreamConstants.END_DOCUMENT;
             event = this.xmlStreamReader.next()) {

            if (count++ == 15)
                break;

//            if (!(this.xmlStreamReader.getEventType() == XMLStreamConstants.CHARACTERS))
//                System.out.println(this.xmlStreamReader.getLocalName());

            switch (event) {

                case XMLStreamConstants.START_ELEMENT: {

                    if (this.xmlStreamReader.getLocalName().equalsIgnoreCase("teiHeader")) {
                        this.xmlElementHandler = new TeiHeaderXMLElementHandler(hdrBasicDBObjectLst);
                        //basicDBObject = new BasicDBObject();
                        this.xmlElementHandler.hanldeXMLElement(this.xmlStreamReader);
                        break;
                    }
                    if (this.xmlStreamReader.getLocalName().equalsIgnoreCase("body")) {
                        this.xmlElementHandler = new TeiBodyXMLElementHandler(bodyBasicDBObjectLst);
                        //basicDBObject = new BasicDBObject();
                        this.xmlElementHandler.hanldeXMLElement(this.xmlStreamReader);
                        break;
                    }

                    break;
                }

//                case XMLStreamConstants.END_ELEMENT:
//                    break;
//
//                case XMLStreamConstants.CHARACTERS:
//                    break;
//
//                case XMLStreamConstants.CDATA:
//                    break;

            }
        }

        this.xmlStreamReader.close();

        return null;
    }


    /**
     * Determine if this is an XHTML heading element or not
     *
     * @param name tag name
     * @return boolean true if this is h1, h2, h3, h4, h5, or h6;
     * false otherwise
     */
    private static boolean isHeader(String name) {
        if (name.equals("h1")) return true;
        if (name.equals("h2")) return true;
        if (name.equals("h3")) return true;
        if (name.equals("h4")) return true;
        if (name.equals("h5")) return true;
        if (name.equals("h6")) return true;
        return false;
    }
}