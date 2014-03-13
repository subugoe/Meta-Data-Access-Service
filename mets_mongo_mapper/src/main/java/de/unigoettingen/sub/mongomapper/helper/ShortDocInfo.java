package de.unigoettingen.sub.mongomapper.helper;

import java.util.Set;

/**
 * Created by jpanzer on 03.03.14.
 */
public class ShortDocInfo {

    private String docid;
    private Set<String> recordIdentifiers;

    public ShortDocInfo(String docid, Set<String> recordIdentifiers) {
        this.docid = docid;
        this.recordIdentifiers = recordIdentifiers;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public Set<String> getRecordIdentifier() {
        return recordIdentifiers;
    }

    public void setRecordIdentifiers(Set<String> recordIdentifiers) {
        this.recordIdentifiers = recordIdentifiers;
    }

    public void addRecordIdentifier(Set<String> recordIdentifiers) {
        this.recordIdentifiers.addAll(recordIdentifiers);
    }
}
