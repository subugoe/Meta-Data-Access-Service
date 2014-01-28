package de.unigoettingen.sub.mongomapper.helper.mods;

import com.mongodb.BasicDBObject;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;

/**
 * Created by jpanzer on 27.01.14.
 *
 * Holds important mods:relatedItem information.
 */
public class RelatedItem {
    String type;
    String recordIdentifier;
    String source;
    private String asXML;


    public RelatedItem(String type, String recordIdentifier, String source) {
        this.type = type;
        this.recordIdentifier = recordIdentifier;
        this.source = source;
    }

    public RelatedItem(BasicDBObject docinfo) {
        this.type = docinfo.get("relatedItem.type").toString();
        this.type = docinfo.get("relatedItem.recordIdentifier").toString();
        this.type = docinfo.get("relatedItem.source").toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BasicDBObject getAsJSON() {
        BasicDBObject doc = new BasicDBObject();
        doc.append("type", type);
        doc.append("recordIdentifier", recordIdentifier);
        doc.append("source", source);
        return doc;
    }


    public XMLTag getAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("relatedItem")

                .addTag("type")
                .addText(this.getType())
                .addTag("recordIdentifier")
                .addText(this.getRecordIdentifier())
                .addTag("source")
                .addText(this.getSource());

        return tag;

    }

    public String toString() {
        return "relatedItem -> " +
            "type: " + type + " " +
            "recordIdentifier: " + recordIdentifier +  " " +
            "source: " +  source;
    }


}
