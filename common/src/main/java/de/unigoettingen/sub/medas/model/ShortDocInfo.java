package de.unigoettingen.sub.medas.model;

import java.util.Set;

/**
 * Created by jpanzer on 03.03.14.
 */
public class ShortDocInfo {

    private String docid;
    private String recordIdentifier;
    private String source;

    public ShortDocInfo(String docid, String recordIdentifier, String source) {
        this.docid = docid;
        this.recordIdentifier = recordIdentifier;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
