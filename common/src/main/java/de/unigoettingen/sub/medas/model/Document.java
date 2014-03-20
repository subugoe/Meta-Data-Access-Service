package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jpanzer on 17.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "document", propOrder = {
        "docid", "recordIdentifier", "title",
        "subTitle", "classification", "mets"
})
@XmlRootElement(name = "document")
public class Document {

    @XmlElement
    protected String docid;
    @XmlElementWrapper(name = "recordInfo")
    @XmlElement
    protected Set<RecordIdentifier> recordIdentifier = new HashSet<>();
    @XmlElement
    protected String title;
    @XmlElement
    protected String subTitle;
    @XmlElement
    protected String mets;
    @XmlElementWrapper(name = "classifications")
    @XmlElement
    protected Set<Classification> classification = new HashSet<>();

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public Set<RecordIdentifier> getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(Set<RecordIdentifier> recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public void addRecordIdentifiers(Set<RecordIdentifier> recordIdentifiers) {
        for (RecordIdentifier recordIdentifier : recordIdentifiers)
            this.recordIdentifier.add(recordIdentifier);
    }

    public void addRecordIdentifier(RecordIdentifier recordIdentifier) {

        this.recordIdentifier.add(recordIdentifier);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMets() {
        return mets;
    }

    public void setMets(String mets) {
        this.mets = mets;
    }

    public Set<Classification> getClassification() {
        return classification;
    }

    public void setClassification(Set<Classification> classification) {
        this.classification = classification;
    }

    public void addClassifications(Classification classification) {
        this.classification.add(classification);
    }
}
