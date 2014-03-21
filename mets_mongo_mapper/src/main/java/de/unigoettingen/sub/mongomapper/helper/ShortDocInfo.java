package de.unigoettingen.sub.mongomapper.helper;

import java.util.Set;

/**
 * Created by jpanzer on 03.03.14.
 */
public class ShortDocInfo {

    private String docid;
    private String recordIdentifiers;

    public ShortDocInfo(String docid, String recordIdentifiers) {
        this.docid = docid;
        this.recordIdentifiers = recordIdentifiers;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getRecordIdentifier() {
        return recordIdentifiers;
    }

    public void setRecordIdentifiers(String recordIdentifiers) {
        this.recordIdentifiers = recordIdentifiers;
    }


}
