package de.unigoettingen.sub.mongomapper.helper;

/**
 * Created by jpanzer on 03.03.14.
 */
public class ShortDocInfo{

    private String docid = "";
    private String recordIdentifier = "";

    public ShortDocInfo(String docid, String recordIdentifier) {
        this.docid = docid;
        this.recordIdentifier = recordIdentifier;
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

    @Override
    public String toString() {
        return "ShortDocInfo{" +
                "docid='" + docid + '\'' +
                ", recordIdentifier='" + recordIdentifier + '\'' +
                '}';
    }
}
