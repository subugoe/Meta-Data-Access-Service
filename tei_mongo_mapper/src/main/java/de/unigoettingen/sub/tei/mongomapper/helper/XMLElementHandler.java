package de.unigoettingen.sub.tei.mongomapper.helper;

import com.mongodb.BasicDBObject;

import javax.xml.stream.XMLStreamReader;
import java.util.List;

/**
 * Created by jpanzer on 21.01.14.
 */
public abstract class XMLElementHandler {

    private final List<BasicDBObject> basicDBObjectList;

    public XMLElementHandler(List<BasicDBObject> basicDBObjectList) {

        this.basicDBObjectList = basicDBObjectList;
    }

    public abstract void hanldeXMLElement(XMLStreamReader xmlStreamReader);
}

