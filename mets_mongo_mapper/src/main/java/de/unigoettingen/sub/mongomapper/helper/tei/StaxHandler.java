package de.unigoettingen.sub.mongomapper.helper.tei;

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
public class StaxHandler {

    private final XMLInputFactory factory;
    //private final XMLEventReader xmlEventReader;
    private final XMLStreamReader xmlStreamReader;
    private XMLElementHandler xmlElementHandler;


    public StaxHandler(InputStream inputStream) throws FileNotFoundException, XMLStreamException {

        this.factory = XMLInputFactory.newFactory();
        this.xmlStreamReader = factory.createXMLStreamReader(inputStream);
    }


    public BasicDBObject processXMLFile() throws XMLStreamException {

        int eventType;

        List<BasicDBObject> hdrBasicDBObjectLst = new ArrayList<BasicDBObject>();
        List<BasicDBObject> bodyBasicDBObjectLst = new ArrayList<BasicDBObject>();
        BasicDBObject basicDBObject = new BasicDBObject();
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

                        basicDBObject.append("teiHeader", this.xmlElementHandler.hanldeXMLElement(this.xmlStreamReader));
                        break;
                    }
                    if (this.xmlStreamReader.getLocalName().equalsIgnoreCase("body")) {
                        this.xmlElementHandler = new TeiBodyXMLElementHandler(bodyBasicDBObjectLst);

                        basicDBObject.append("body", this.xmlElementHandler.hanldeXMLElement(this.xmlStreamReader));
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

        return basicDBObject;
    }

}