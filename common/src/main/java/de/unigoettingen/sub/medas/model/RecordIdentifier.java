package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.Set;

/**
 * Created by jpanzer on 04.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordIdentifier", propOrder = {
        "value"
})
public class RecordIdentifier {

    public RecordIdentifier() {
    }

    public RecordIdentifier(String value, String source, String docid) {
        this.value = value;
        this.source = source;
        this.relatedDocid = docid;
    }

    @XmlValue
    private String value;

    @XmlAttribute(name = "source")
    private String source;

    @XmlAttribute(name = "relatedDocid")
    private String relatedDocid;

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

    public String getRelatedDocid() {
        return relatedDocid;
    }

    public void setRelatedDocid(String relatedDocid) {
        this.relatedDocid = relatedDocid;
    }
}
