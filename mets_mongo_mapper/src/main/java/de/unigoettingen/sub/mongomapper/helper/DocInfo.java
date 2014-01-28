package de.unigoettingen.sub.mongomapper.helper;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;
import de.unigoettingen.sub.mongomapper.helper.mods.Classifier;
import de.unigoettingen.sub.mongomapper.helper.mods.RelatedItem;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 08.01.14.
 */
public class DocInfo {

    private final List<String> props;
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

    private List<RelatedItem> relatedItemList = null;
    private List<Classifier> classifierList = null;

    public DocInfo(List<String> props) {
        this.props = props;
    }

    //private String relatedItemAsXML;


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


        if (docinfo.containsField("relatedItem")) {
            this.relatedItemList = new ArrayList<>();
            BasicDBList list = (BasicDBList) docinfo.get("relatedItem");
            for (int i = 0; i < list.size(); i++) {
                BasicDBObject obj = (BasicDBObject) list.get(i);
                String type = obj.getString("type");
                String recordIdentifier = obj.getString("recordIdentifier");
                String source = obj.getString("source");
                this.relatedItemList.add(new RelatedItem(type, recordIdentifier, source));
            }
        }


        if (docinfo.containsField("classification")) {
            this.classifierList = new ArrayList<>();
            BasicDBList list = (BasicDBList) docinfo.get("classification");
            for (int i = 0; i < list.size(); i++) {
                BasicDBObject obj = (BasicDBObject) list.get(i);
                String authority = obj.getString("authority");
                String value = obj.getString("value");

                System.out.println(authority + " " + value);

                this.classifierList.add(new Classifier(authority, value));
            }
        }
    }

    /**
     * Builds a json structure for the docinfo elements.
     *
     * @return The json encoded docinfo.
     */
    public BasicDBObject getAsJSON() {

        BasicDBObject doc = new BasicDBObject();


        if (props.isEmpty()) {

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
            doc.append("relatedItem", this.getRelatedItemsAsJSON());
            doc.append("classification", this.getClassificationsAsJSON());

            return doc;

        }


        doc.append("docid", this.getDocid());

        if (props.contains("id"))
            doc.append("id", this.getId());

        if (props.contains("title"))
            doc.append("title", this.getTitle());

        if (props.contains("titleShort"))
            doc.append("titleShort", this.getTitleShort());

        if (props.contains("mets"))
            doc.append("mets", this.getMets());

        if (props.contains("preview"))
            doc.append("preview", this.getPreview());

        if (props.contains("tei"))
            doc.append("tei", this.getTei());

        if (props.contains("teiEnriched"))
            doc.append("teiEnriched", this.getTeiEnriched());

        if (props.contains("pageCount"))
            doc.append("pageCount", this.getPageCount());

        if (props.contains("fulltext"))
            doc.append("fulltext", this.getFulltext());

        if (props.contains("relatedItems"))
            doc.append("relatedItem", this.getRelatedItemsAsJSON());

        if (props.contains("classifications"))
            doc.append("classification", this.getClassificationsAsJSON());

        return doc;


    }

    /**
     * Builds a xml structure for the docinfo elements.
     *
     * @return The xml encoded docinfo.
     */
    public XMLTag getAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("doc");

        if (props.isEmpty()) {

            tag.addTag("docid")
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
                    .addText(this.getFulltext())

                    .addTag(getRelatedItemsAsXML())

                    .addTag(getClassificationsAsXML());

            return tag;
        }


        tag.addTag("docid")
                .addText(this.getDocid());

        if (props.contains("id"))
            tag.addTag("id")
                    .addText(this.getId());

        if (props.contains("title"))
            tag.addTag("title")
                    .addText(this.getTitle());

        if (props.contains("titleShort"))
            tag.addTag("titleShort")
                    .addText(this.getTitleShort());

        if (props.contains("mets"))
            tag.addTag("mets")
                    .addText(this.getMets());

        if (props.contains("preview"))
            tag.addTag("preview")
                    .addText(this.getPreview());

        if (props.contains("tei"))
            tag.addTag("tei")
                    .addText(this.getTei());

        if (props.contains("teiEnriched"))
            tag.addTag("teiEnriched")
                    .addText(this.getTeiEnriched());

        if (props.contains("pageCount"))
            tag.addTag("pageCount")
                    .addText(this.getPageCount());

        if (props.contains("fulltext"))
            tag.addTag("fulltext")
                    .addText(this.getFulltext());

        if (props.contains("relatedItems"))
            tag.addTag(getRelatedItemsAsXML());

        if (props.contains("classifications"))
            tag.addTag(getClassificationsAsXML());

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
        String str = "";

        str = "docid: " + this.getDocid() + ", " +
                    "id: " + this.getId() + ", " +
                    "title: " + this.getTitle() + ", " +
                    "titleShrot: " + this.getTitleShort() + ", " +
                    "mets: " + this.getMets() + ", " +
                    "preview: " + this.getPreview() + ", " +
                    "tei: " + this.getTei() + ", " +
                    "teiEnriched: " + this.getTeiEnriched() + ", " +
                    "pageCount: " + this.getPageCount() + ", " +
                    "fulltext" + this.getFulltext() + ", " +
                    "relatedItems: " + this.getRelatedItemsAsString() + ", " +
                    "classifications: " + this.getClassificationsAsString();

        return str;
    }

    public XMLTag getRelatedItemsAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("relatedItems");

        for (RelatedItem item : this.relatedItemList) {
            tag.addTag(item.getAsXML());
        }
        return tag;
    }

    public BasicDBList getRelatedItemsAsJSON() {

        BasicDBList list = new BasicDBList();

        for (RelatedItem item : this.relatedItemList) {
            list.add(item.getAsJSON());
        }

        return list;
    }

    public String getRelatedItemsAsString() {

        StringBuffer strb = new StringBuffer();
        strb.append("relatedItems:\n");

        for (RelatedItem item : this.relatedItemList) {
            strb.append(item);
            strb.append("\n");
        }

        return strb.toString();
    }


    public BasicDBList getClassificationsAsJSON() {

        BasicDBList list = new BasicDBList();

        for (Classifier item : this.classifierList) {
            list.add(item.getAsJSON());
        }

        return list;
    }

    public XMLTag getClassificationsAsXML() {

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("classifications");

        for (Classifier item : this.classifierList) {
            tag.addTag(item.getAsXML());
        }
        return tag;

    }

    public String getClassificationsAsString() {

        StringBuffer strb = new StringBuffer();
        strb.append("classifications:\n");

        for (Classifier item : this.classifierList) {
            strb.append(item);
            strb.append("\n");
        }

        return strb.toString();
    }
}
