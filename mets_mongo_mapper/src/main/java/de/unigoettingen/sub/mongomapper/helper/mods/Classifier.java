package de.unigoettingen.sub.mongomapper.helper.mods;

import com.mongodb.BasicDBObject;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;

/**
 * Created by jpanzer on 27.01.14.
 *
 * Holds the important mods:classification information.
 */
public class Classifier {

    private String value;
    private String authority;
    private XMLTag asXML;


    public Classifier(String authority, String value) {
        this.value = value;
        this.authority = authority;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public XMLTag getAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("classification")

                .addTag("authority")
                .addText(this.getAuthority())
                .addTag("value")
                .addText(this.getValue());

        return tag;
    }

    public BasicDBObject getAsJSON() {
        BasicDBObject doc = new BasicDBObject();
        doc.append("authority", authority);
        doc.append("value", value);
        return doc;
    }

    public String toString() {
        return "classification -> " +
                "authority: " + this.getAuthority() + " " +
                "value: " + this.getValue();

    }
}
