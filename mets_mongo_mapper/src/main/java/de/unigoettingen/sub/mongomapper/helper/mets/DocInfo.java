package de.unigoettingen.sub.mongomapper.helper.mets;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;
import org.bson.types.ObjectId;

/**
 * Created by jpanzer on 08.01.14.
 */
public class DocInfo {

    private String docid = "";

    private String id = "";
    private String title = "";
    private String titleShort = "";
    private String mets = "";
    private String preview = "";
    private String tei = "";
    private String teiEnriched = "";
    private String pageCount = "";
    private String fulltext = "";

    public BasicDBObject getAsJSON() {

        BasicDBObject doc = new BasicDBObject();

        // retrieve objid
        doc.append("docid", this.getDocid());
        doc.append("id", this.getId());
        doc.append("title", this.getTitle());
        doc.append("titleShort", this.getTitleShort());
        doc.append("mets", this.getMets());
        doc.append("preview", this.getPreview());
        doc.append("tei", this.getTei());
        doc.append("teiEnriched", this.getTeiEnriched());
        doc.append("pageCount", this.getPageCount());
        doc.append("fulltext", this.getFulltext());

        return doc;
    }


    /**
     * The method retrieves the embedded fields within the docinfo field and
     * stores these in instance variables.
     *
     * @param doc The docinfo filed from a mets document in mongo.
     */
    public void setFromJSON(DBObject doc) {

        // retrieve objid
        if ((doc.containsField("_id")))
            this.docid = ((ObjectId) doc.get("_id")).toString();

        // retrieve docinfo fields
        BasicDBObject docinfo = (BasicDBObject) doc.get("docinfo");

        if (docinfo.containsField("id"))
            this.id = docinfo.get("id").toString();

        if (docinfo.containsField("title"))
            this.title = docinfo.get("title").toString();

        if (docinfo.containsField("titleShort"))
            this.titleShort = docinfo.get("titleShort").toString();

        if (docinfo.containsField("mets"))
            this.mets = docinfo.get("mets").toString();

        if (docinfo.containsField("preview"))
            this.preview = docinfo.get("preview").toString();

        if (docinfo.containsField("tei"))
            this.tei = docinfo.get("tei").toString();

        if (docinfo.containsField("teiEnriched"))
            this.teiEnriched = docinfo.get("teiEnriched").toString();

        if (docinfo.containsField("pageCount"))
            this.pageCount = docinfo.get("pageCount").toString();

        if (docinfo.containsField("fulltext"))
            this.fulltext = docinfo.get("fulltext").toString();
    }

    /**
     * Builds a xml structure for the docinfo elements.
     *
     * @return The xml encoded docinfo.
     */
    public XMLTag getAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("doc")

                .addTag("docid")
                .addText(this.getDocid())
                .addTag("id")
                .addText(this.getId())
                .addTag("title")
                .addText(this.getTitle())
                .addTag("titleShort")
                .addText(this.getTitleShort())
                .addTag("mets")
                .addText(this.getMets())
                .addTag("preview")
                .addText(this.getPreview())
                .addTag("tei")
                .addText(this.getTei())
                .addTag("teiEnriched")
                .addText(this.getTeiEnriched())
                .addTag("pageCount")
                .addText(this.getPageCount())
                .addTag("fulltext")
                .addText(this.getFulltext());

        return tag;
    }


    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public String getMets() {
        return mets;
    }

    public void setMets(String mets) {
        this.mets = mets;
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


    public String toString() {
        String str;

        str = "docid: " + this.getDocid() + ", " +
                "id: " + this.getId() + ", " +
                "title: " + this.getTitle() + ", " +
                "titleShrot: " + this.getTitleShort() + ", " +
                "mets: " + this.getMets() + ", " +
                "preview: " + this.getPreview() + ", " +
                "tei: " + this.getTei() + ", " +
                "teiEnriched: " + this.getTeiEnriched() + ", " +
                "pageCount: " + this.getPageCount() + ", " +
                "fulltext" + this.getFulltext();

        return str;
    }
}
