package de.unigoettingen.sub.medas.model;

import java.util.*;
import javax.xml.bind.annotation.*;

/**
 * @author doenitz@sub.uni-goettingen.de
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "doc", propOrder = {
        "relatedItem", "tei", "teiEnriched", "preview", "pageCount", "fulltext"
})
@XmlRootElement(name = "doc")
public class Doc extends Document {


    @XmlElement
    private String preview;
    @XmlElement
    private String tei;
    @XmlElement
    private String teiEnriched;


    @XmlElement
    private String pageCount;

    @XmlElement
    private String fulltext;

    //@XmlElementWrapper(name = "relatedItems")
    @XmlElement
    private Set<RelatedItem> relatedItem = new HashSet<>();


    /**
     * The PPN of the parent item. E. g. the journal a volume belongs to. If
     * there is no host item or it has no PPN
     * <code>null</code> is returned.
     *
     * @return The PPN of the parent, or <code>null</code>
     */     // TODO there are possibly several recordIdentifiers
    public Set<Set<String>> getHostPPN() {


        if (!relatedItem.iterator().hasNext()) {
            return null;
        }

        Set<Set<String>> identifiers = new HashSet<>();

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


    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTei() {
        return tei;
    }

    public void setTei(String tei) {
        this.tei = tei;
    }

    public String getTeiEnriched() {
        return teiEnriched;
    }

    public void setTeiEnriched(String teiEnriched) {
        this.teiEnriched = teiEnriched;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public Set<RelatedItem> getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(Set<RelatedItem> relatedItem) {
        this.relatedItem = relatedItem;
    }

    public void addRelatedItem(RelatedItem relatedItem) {
        this.relatedItem.add(relatedItem);
    }

}
