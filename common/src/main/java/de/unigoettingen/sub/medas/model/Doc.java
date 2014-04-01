package de.unigoettingen.sub.medas.model;

import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.*;
import javax.xml.bind.annotation.*;

/**
 * @author doenitz@sub.uni-goettingen.de
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "doc", propOrder = {
        "docid", "recordIdentifier", "identifier", "title",
        "subTitle", "classification", "mets",
        "relatedItem", "pageCount", "contentCount", "partOrder", "content"
        //"tei", "teiEnriched", "preview", "fulltext",
})
@XmlRootElement(name = "doc")
public class Doc {


    @XmlElement
    @Id
    protected String docid;

//    @XmlElement
//    protected String relatedMetsDocid;


    @XmlElementWrapper(name = "recordInfo")
    @XmlElement
    protected List<RecordIdentifier> recordIdentifier = new ArrayList<>();

    @XmlElementWrapper(name = "identifier")
    @XmlElement
    protected List<Identifier> identifier = new ArrayList<>();

    @XmlElement
    protected String title;
    @XmlElement
    protected String subTitle;
    @XmlElement
    protected String mets;

    @XmlElementWrapper(name = "classifications")
    @XmlElement
    protected Set<Classification> classification = new HashSet<>();

    //@XmlElementWrapper(name = "relatedItems")
    @XmlElement
    private List<RelatedItem> relatedItem = new ArrayList<>();

    //@XmlElementWrapper(name = "contents")
    @XmlElement
    private List<Doc.Content> content = new ArrayList<>();

    @XmlElement
    private int pageCount;

    @XmlElement
    private int contentCount;

    @XmlElement
    private BigInteger partOrder;

//    @XmlElement
//    private String preview;
//    @XmlElement
//    private String tei;
//    @XmlElement
//    private String teiEnriched;
//
//    @XmlElement
//    private String fulltext;


    /**
     * The PPN of the parent item. E. g. the journal a volume belongs to. If
     * there is no host item or it has no PPN
     * <code>null</code> is returned.
     *
     * @return The PPN of the parent, or <code>null</code>
     */     // TODO there are possibly several recordIdentifiers
    public Set<String> getHostPPN() {


        if (!relatedItem.iterator().hasNext()) {
            return null;
        }

        Set<String> identifiers = new HashSet<>();

        Iterator<RelatedItem> iter = relatedItem.iterator();
        while (iter.hasNext()) {

            RelatedItem relatedItem1 = iter.next();
            if (relatedItem1.getType().equalsIgnoreCase("host")) {

                Iterator<RecordIdentifier> identifierIterator = relatedItem1.getRecordIdentifier().iterator();
                while (identifierIterator.hasNext()) {
                    RecordIdentifier identifier = identifierIterator.next();
                    identifiers.add(identifier.getValue());
                }
            }
        }

        return identifiers;
    }

    /**
     * Get the value of the classification according to the DDC standard. If no
     * classification at all or none in the DDC style is available
     * <code>null</code> is returned.
     *
     * @return The value of the DDC classification or <code>null</code>
     */
    public String getDDC() {
        // return value or the classification object. If the authority is only the type, we don't need it.
        if (classification == null) {
            // required?
            return null;
        }
        for (Classification c : classification) {
            if ("dz".equals(c.getAuthority())) {
                return c.getValue();
            }
        }
        return null;
    }

    /**
     * Gets only the identifier of the DDC classification without the label or
     * <code>null</code> if no DDC classification could be found.
     *
     * @return The DDC number or <code>null</code>
     */
    public String getDDCNumber() {
        String ddc = getDDC();
        if (ddc == null) {
            return null;
        }
        return ddc.split(" ")[0];
    }


//    public String getPreview() {
//        return preview;
//    }
//
//    public void setPreview(String preview) {
//        this.preview = preview;
//    }
//
//    public String getTei() {
//        return tei;
//    }
//
//    public void setTei(String tei) {
//        this.tei = tei;
//    }
//
//    public String getTeiEnriched() {
//        return teiEnriched;
//    }
//
//    public void setTeiEnriched(String teiEnriched) {
//        this.teiEnriched = teiEnriched;
//    }


    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    public int getContentCount() {
        return contentCount;
    }

    public void setContentCount(int contentCount) {
        this.contentCount = contentCount;
    }

    public BigInteger getPartOrder() {
        return partOrder;
    }

    public void setPartOrder(BigInteger partOrder) {
        this.partOrder = partOrder;
    }

//    public String getFulltext() {
//        return fulltext;
//    }
//
//    public void setFulltext(String fulltext) {
//        this.fulltext = fulltext;
//    }

    public List<RelatedItem> getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(List<RelatedItem> relatedItem) {
        this.relatedItem = relatedItem;
    }

    public void addRelatedItem(RelatedItem relatedItem) {
        this.relatedItem.add(relatedItem);
    }

    public List<Doc.Content> getContent() {
        return content;
    }

    public void setContent(List<Doc.Content> content) {
        this.content = content;
    }

    public String getRecordIdentifier(String identifierType) {

        for (RecordIdentifier recordIdentifier : this.recordIdentifier) {
            if (recordIdentifier.getSource().equalsIgnoreCase(identifierType))
                return recordIdentifier.getValue();
        }

        return null;
    }

    public List<RecordIdentifier> getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(List<RecordIdentifier> recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public void addRecordIdentifiers(List<RecordIdentifier> recordIdentifiers) {
        for (RecordIdentifier recordIdentifier : recordIdentifiers)
            this.recordIdentifier.add(recordIdentifier);
    }

    public void addRecordIdentifier(RecordIdentifier recordIdentifier) {

        this.recordIdentifier.add(recordIdentifier);
    }


    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public void addIdentifiers(List<Identifier> identifiers) {

        this.identifier.addAll(identifiers);
    }

    public void addIdentifier(Identifier identifier) {

        this.identifier.add(identifier);
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




//    public String getRelatedMetsDocid() {
//        return relatedMetsDocid;
//    }
//
//    public void setRelatedMetsDocid(String relatedMetsDocid) {
//        this.relatedMetsDocid = relatedMetsDocid;
//    }

    /**
     * Created by jpanzer on 17.03.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "docid", "id", "type", "label", "order", "orderlabel", "loctype", "href", "recordIdentifier", "partOrder",
            "pageCount", "error" //, "dmdIds", "admIds",
    })
    public static class Content {

        private String docid;

        private String id;
        //        List<String> dmdIds = new ArrayList<>();
//        List<String> admIds = new ArrayList<>();
        private String type;
        private String label;
        private BigInteger order;
        private String orderlabel;
        private String loctype;
        private String href;

        private String recordIdentifier;

        private int pageCount;

        private BigInteger partOrder;

        String error;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getDocid() {
            return docid;
        }

        public void setDocid(String docid) {
            this.docid = docid;
        }

        public String getRecordIdentifier() {
            return recordIdentifier;
        }

        public void setRecordIdentifier(String recordIdentifier) {
            this.recordIdentifier = recordIdentifier;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

//        public List<String> getDmdIds() {
//            return dmdIds;
//        }
//
//        public void setDmdIds(List<String> dmdIds) {
//            this.dmdIds = dmdIds;
//        }
//
//        public void addDmdIds(List<String> dmdIds) {
//            this.dmdIds.addAll(dmdIds);
//        }
//
//        public void addDmdId(String dmdId) {
//            this.dmdIds.add(dmdId);
//        }
//
//        public List<String> getAdmIds() {
//            return admIds;
//        }
//
//        public void setAdmIds(List<String> admIds) {
//            this.admIds = admIds;
//        }
//
//        public void addAdmIds(List<String> admIds) {
//            this.admIds.addAll(admIds);
//        }
//
//        public void addAdmId(String admId) {
//            this.admIds.add(admId);
//        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BigInteger getOrder() {
            return order;
        }

        public void setOrder(BigInteger order) {
            this.order = order;
        }

        public String getOrderlabel() {
            return orderlabel;
        }

        public void setOrderlabel(String orderlabel) {
            this.orderlabel = orderlabel;
        }

        public String getLoctype() {
            return loctype;
        }

        public void setLoctype(String loctype) {
            this.loctype = loctype;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public BigInteger getPartOrder() {
            return partOrder;
        }

        public void setPartOrder(BigInteger partOrder) {
            this.partOrder = partOrder;
        }

    }


    public String getPrimaryRecordIdentifier() {

        //return this.recordIdentifier.get(0).getValue();

        return this.getDocid();

    }


}
