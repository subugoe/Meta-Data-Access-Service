package de.unigoettingen.sub.mongomapper.helper;

import com.mongodb.BasicDBObject;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;

/**
 * Created by jpanzer on 29.01.14.
 */
public class Id {

    private boolean isRecordIdentifier = false;
    private String type = "";
    private String value = "";
    private String source = "";

    public Id(boolean isRecordIdentifier) {
        this.isRecordIdentifier = isRecordIdentifier;
    }

    public BasicDBObject toJSON() {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("isRecordIdentifier", this.isRecordIdentifier);
        basicDBObject.append("type", this.type);
        basicDBObject.append("value", this.value);
        basicDBObject.append("source", this.source);

        return basicDBObject;
    }

    public XMLTag toXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("id");

        tag.addTag("isRecordIdentifier")
                .addText(String.valueOf(this.isRecordIdentifier))
                .addTag("type")
                .addText(this.type)
                .addTag("value")
                .addText(this.value)
                .addTag("source")
                .addText(this.source);

        return tag;
    }

    public String toString() {

        String str = "";

        str =   "id: (" +
                "isRecordIdentifier: " + String.valueOf(this.isRecordIdentifier) + ", " +
                "type: " + this.type + ", " +
                "value: " + this.value + ", " +
                "source: " + this.source + ")";


        return str;
    }



    public boolean isRecordIdentifier() {
        return isRecordIdentifier;
    }

    public void setRecordIdentifier(boolean isRecordIdentifier) {
        this.isRecordIdentifier = isRecordIdentifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
