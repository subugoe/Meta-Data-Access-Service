package de.unigoettingen.sub.tei.mongomapper.helper;

import com.mongodb.BasicDBObject;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

/**
 * Created by jpanzer on 21.01.14.
 */
public class TeiHeaderXMLElementHandler extends XMLElementHandler {
    public TeiHeaderXMLElementHandler(List<BasicDBObject> hdrBasicDBObjectLst) {
        super(hdrBasicDBObjectLst);
    }

    @Override
    public void hanldeXMLElement(XMLStreamReader xmlStreamReader) {
        // currently not supported
    }
}
