package de.unigoettingen.sub.mongomapper.access;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;
import de.unigoettingen.sub.mongomapper.helper.DocInfo;
import de.unigoettingen.sub.mongomapper.helper.IdHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
public class MongoExporter {

    private final Logger logger = LoggerFactory.getLogger(MongoExporter.class);

    
    private final String db_name;
    private final String mets_coll_name;
    private final String tei_coll_name;
    
    private DB db = null;
    private DBCollection coll = null;
    private MongoClient mongoClient = null;


    /**
     * Construct the object with required parameters
     *
     * @param db_name   The name of the mongoDB
     * @param mets_coll_name The name of the collection, to store the documents
     */
    public MongoExporter(String db_name, String mets_coll_name, String tei_coll_name) {
        this.db_name = db_name;
        this.mets_coll_name = mets_coll_name;
        this.tei_coll_name = tei_coll_name;

        init();
    }


    /**
     * init() initializes the object, establishes the connection to the mongoDB server.
     */
    private void init() {

        // init mongo
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        db = mongoClient.getDB(this.db_name);
        coll = db.getCollection(this.mets_coll_name);
    }


    /**
     * Collects information about the documents in the repository.
     *
     * @return A list of document info in JSON.
     * Format:
     * {docs : [
     * {doc:  {
     * id : string,
     * title : string,
     * titleShort : string,
     * mets : url,
     * preview : url,
     * tei : url,
     * teiEnriched : url,
     * pageCount : int,
     * fulltext : boolean
     * },
     * {
     * ...
     * },
     * ...
     * }]}
     * @param props
     */
    public BasicDBObject getDocumentsAsJSON(List<String> props) {

        BasicDBObject docs = new BasicDBObject();
        BasicDBList docList = new BasicDBList();

        // find docinfo
        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
        DBCursor dbCursor = coll.find(new BasicDBObject(), field);

        while (dbCursor.hasNext()) {


            DBObject dbObject = dbCursor.next();

            DocInfo docInfo = new DocInfo(props);
            docInfo.setFromJSON(dbObject);
            docList.add(docInfo.getAsJSON());

            docs.append("docs", docList);
        }
        return docs;
    }

    /**
     * Collects information about the documents in the repository.
     *
     * @return A list of document info in XML.
     * Format:
     * <docs>
     * <doc>
     * <id> string </id>
     * <title> string </title>
     * <titleShort> string </titleShort>
     * <mets> url </mets>
     * <preview> </preview>
     * <tei> url </tei>
     * <teiEnriched> url </teiEnriched>
     * <pageCount> int </pageCount>
     * <fulltext> boolean </fulltext>
     * </doc>
     * <doc>
     * ...
     * </doc>
     * ...
     * </docs>
     * @param props
     */
    public String getDocumentsAsXML(List<String> props) {


        // find docinfo
        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
        DBCursor dbCursor = coll.find(new BasicDBObject(), field);

        XMLTag tag = XMLDoc.newDocument()
                .addRoot("docs");

        while (dbCursor.hasNext()) {

            DBObject dbObject = dbCursor.next();
            DocInfo docInfo = new DocInfo(props);
            docInfo.setFromJSON(dbObject);

            XMLTag t = docInfo.getAsXML();

            tag.addDocument(t);
        }
        return tag.toString();
    }


    public String getDocumentOutline(String docid) {
        return null;
    }

    public String getDocumentRepresentation(String docid, String pageno) {
        return null;
    }

    public String getDocumentRepresentation(String docid) {
        return null;
    }

    public String getSearchResults(String docid, String query) {
        return null;
    }

    public String getSearchResults(String query) {
        return null;
    }

    public String getOai2Results(String query) {
        return null;
    }


    /**
     * Retrieves the related METS or TEI document if available, else null.
     *
     * @param docid The MongoDB id of the related mongoDB object or any of its PIDs.
     * @param type  The document type to get ("mets", "tei" or "reiEnriched").
     * @return The METS document as an InputStream or null.
     */
    public InputStream getEmeddedFileDocument(String docid, String type) {

        return this.getEmeddedFileDocument(docid, type, null);
    }

    /**
     * Retrieves the related METS or TEI document if available, else null.
     *
     * @param docid   The MongoDB id of the related mongoDB object or any of its PIDs.
     * @param type    The document type to get ("mets" or "tei").
     * @param teiType The TEI type ("tei" or "teiType)
     * @return The METS document as an InputStream or null.
     */
    public InputStream getEmeddedFileDocument(String docid, String type, String teiType) {


        GridFSDBFile gridFSDBFile = getGridFsDbFile(docid, type, teiType);

        if (gridFSDBFile != null) {
            return gridFSDBFile.getInputStream();
        } else {

            // search with alternative pid's

            IdHelper idHelper = new IdHelper();
            Map<String, String> idMap = idHelper.getPidsFromDB(db, mets_coll_name);


            // TODO throw an exception if the file is not available
            if (idHelper.aleadyInDB(idMap, docid)) {

                List<String> keyValuePair = idHelper.getKeyValuePairFor(idMap, docid);
                if (keyValuePair != null) {
                    String objId = idHelper.findDocid(keyValuePair, db, mets_coll_name);
                    gridFSDBFile = getGridFsDbFile(objId, type, teiType);
                    if (gridFSDBFile != null)
                        return gridFSDBFile.getInputStream();
                }
            }
        }
        return null;
    }

    /**
     * Returns the METS or TEI file object for the given object. This is a helper
     * for getEmeddedDocument, is creates the query and forwards the request to
     * mongoDB.
     *
     * @param docid   The MongoDB id of the related mongoDB object, or any PID.
     * @param type    The object type e.g. "mets", "tei"
     * @param teiType The TEI type, possibilities are {tei | teiEnriched}.
     * @return The requested file object.
     */
    private GridFSDBFile getGridFsDbFile(String docid, String type, String teiType) {

        GridFS gridFs = new GridFS(db, mets_coll_name);
        BasicDBObject query;

        if (teiType == null) {
            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type));
        } else {

            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type).
                    append("teiType", teiType));
        }

        System.out.println(query);

        return gridFs.findOne(query);

    }


    public String getDocumentTags(String docid) {
        return null;
    }

    public String getPageTags(String docid, int pageNumber) {
        return null;
    }


    public String getFacets(String docid) {
        return null;
    }

    public String getDocumentKml(String docid) {
        return null;
    }


}
