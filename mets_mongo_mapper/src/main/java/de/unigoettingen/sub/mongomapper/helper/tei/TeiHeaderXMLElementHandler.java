package de.unigoettingen.sub.mongomapper.helper.tei;

import com.mongodb.BasicDBObject;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 21.01.14.
 */
public class TeiHeaderXMLElementHandler extends XMLElementHandler {
    public TeiHeaderXMLElementHandler(List<BasicDBObject> hdrBasicDBObjectLst) {
        super(hdrBasicDBObjectLst);
    }

    @Override
    public List<BasicDBObject> hanldeXMLElement(XMLStreamReader xmlStreamReader) {
        // currently not supported
        return new ArrayList<BasicDBObject>();
    }
}
