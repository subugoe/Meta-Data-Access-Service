package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jdo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedItem", propOrder = {
        "recordIdentifier"
})
public class RelatedItem {

    @XmlAttribute(name = "type")
    private String type; //TODO enum

//    @XmlAttribute(name = "type")
//    private String docidOfThis;

    @XmlElementWrapper(name = "recordInfo")
    @XmlElement
    private List<RecordIdentifier> recordIdentifier = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public List<RecordIdentifier> getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(List<RecordIdentifier> recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public void addRecordIdentifier(RecordIdentifier recordIdentifier) {
        this.recordIdentifier.add(recordIdentifier);
    }


    public void addRecordIdentifiers(List<RecordIdentifier> recordIdentifiers) {

        for (RecordIdentifier recordIdentifier : recordIdentifiers)
            this.recordIdentifier.add(recordIdentifier);
    }

//    public String getDocidOfThis() {
//        return docidOfThis;
//    }
//
//    public void setDocidOfThis(String docidOfThis) {
//        this.docidOfThis = docidOfThis;
//    }
}
