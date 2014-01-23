package de.unigoettingen.sub.tei.mongomapper.ingest;


import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import de.unigoettingen.sub.mongomapper.helper.IdHelper;
import de.unigoettingen.sub.tei.mongomapper.helper.StaxHandler;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 * <p/>
 * <p/>
 * On ingest of a new embedded tei document, the docinfo needs to modified too.
 */
@SuppressWarnings("ALL")
public class TeiMongoImporter {

    private final Logger logger = LoggerFactory.getLogger(TeiMongoImporter.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();





    private final String HOST = "localhost";
    private final int PORT = 27017;

    private MongoClient mongoClient = null;

    private DB db = null;
    private DBCollection coll = null;
    private GridFS gridFs = null;


    private String db_name = "";
    private String coll_name = "";




    private String filename;
    private boolean alreadyInDB = false;

    private String docid = null;
    private String teiType = null;
    private String appUrlString;


    /**
     * Construct the object with required parameters
     *
     * @param db_name   The name of the mongoDB
     * @param coll_name The name of the collection, to store the documents
     */
    public TeiMongoImporter(String db_name, String coll_name) {
        this.db_name = db_name;
        this.coll_name = coll_name;
        init();
    }


    /**
     * init() initializes the object, establishes the connection to the mongoDB server.
     */
    private void init() {

        // init mongo
        // TODO better exception handling required
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        db = mongoClient.getDB(this.db_name);
        coll = db.getCollection(this.coll_name);
        gridFs = new GridFS(db, coll_name);
    }


    /**
     * Stores a TEI structure to mongoDB.
     *
     * @param teiFile   The TEI file to store in mongodb.
     * @param docid     The docid of the related METS document.
     * @param type      The object type e.g. "mets", "tei"
     * @param teiType   The TEI type, possibilities are {tei | teiEnriched}.
     * @param appUrlString The application URL (schema://host:port/).
     */
    public void processTeiAndStore(MultipartFile teiFile, String docid, String type, String teiType, String appUrlString) {

        this.teiType = teiType;
        this.appUrlString = appUrlString;
        InputStream inputStream = null;
        StaxHandler staxHandler = null;

        BasicDBObject teiBasicDBObject = null;

//        this.alreadyInDB = this.process();
//
//        if (this.alreadyInDB && handling.equalsIgnoreCase("reject")) {
//            logger.info("Request rejected, because the METS file " +
//                    this.filename + " is already in the db with mongo id " + this.docid);
//            return;
//        }

        try {
            inputStream = teiFile.getInputStream();
            staxHandler = new StaxHandler(inputStream);
            teiBasicDBObject = staxHandler.processXMLFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }   finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        this.writeToMongo(teiBasicDBObject);

        storeFileInMongo(teiFile, docid, type);

        String content = String.format(this.appUrlString + "/documents/%s/tei?type=%s", docid, teiType);

        if (teiType.equalsIgnoreCase("tei"))
            this.addOrChangeDocInfoField(docid, "tei", content);
        else if (teiType.equalsIgnoreCase("teiEnriched"))
            this.addOrChangeDocInfoField(docid, "teiEnriched", content);

    }



    private void addOrChangeDocInfoField(String docid, String field, String content) {

        DBObject dbObject = this.coll.find(this.getQueryBasicDBObject(docid)).toArray().get(0);

        DBObject docinfo = (DBObject) dbObject.get("docinfo");
        docinfo.put(field, content);

        dbObject.put(field, content);

        this.coll.findAndModify(this.getQueryBasicDBObject(docid), dbObject);
    }




    public void storeFileInMongo(MultipartFile file, String docid, String type) {

        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
                file.getOriginalFilename());

        this.checkIfExistAndRemove(type);

        try {
            file.transferTo(tmpFile);

            GridFSInputFile gridFS_InputFile;

            // TODO better Exception handling required

            gridFS_InputFile = gridFs.createFile(tmpFile);
            gridFS_InputFile.setFilename(tmpFile.getName());

            BasicDBObject basicDBObject = new BasicDBObject("relatedObjId", docid).append("type", type);
            if (type.equalsIgnoreCase("tei"))
                basicDBObject.append("teiType", this.teiType);

            gridFS_InputFile.setMetaData(basicDBObject);
            gridFS_InputFile.save();

        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            tmpFile.delete();
        }
    }

    /**
     * Checks if an mets or tei or teiEnriched file is already associated with
     * the current docid and stored in the db. If so, the existing file will
     * removed and the new one will be stored.
     *
     * @param type The documentype {mets | tei | teiEnriched}
     */
    private void checkIfExistAndRemove(String type) {

        GridFS gridFs = new GridFS(db, coll_name);
        BasicDBObject query;

        if (type.equalsIgnoreCase("mets")) {
            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type));
        } else {
            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type).
                    append("teiType", teiType));
        }

        gridFs.remove(query);
    }







    /**
     * Initiates the precessing of the METS document. The retrieved
     * elements are stored in instance variables as BasicDBObjects
     * (JSON-like).
     */
    private boolean process() {



        // checks if the doc is already in the db. If the request contains
        // the flag "reject" the controll gets back to the caller.
        alreadyInDB = isAlreadyInDB();

//                this.removeFileFromMongo("mets", this.docid);



        return alreadyInDB;
    }


    /**
     * Stores the elements of a METS document as fields of the related mongo document. It
     * also stores the METS file in mongo special collections for metadata and files
     * (metadata: <db>.<coll>.files, file: <db>.<coll>.chunks).
     *
     * @param doc The METS document to store in the db, as an BasicDBObject.
     * @param metsFile The METS file to store in mongodb.
     */
    private void writeToMongo(BasicDBObject doc, MultipartFile metsFile) {


//            this.writeToMongo_Replace(doc, metsFile);



        // TODO create index on objId and label (only if it not yet exist)
    }


    /**
     * Helper for {@link #writeToMongo(com.mongodb.BasicDBObject, org.springframework.web.multipart.MultipartFile)}  writeToMongo}.
     * The method handles the case of the initial ingest.
     *
     * @param doc      The METS document to store in the db, as an BasicDBObject.
     * @param metsFile The METS file to store in mongodb.
     */
    private void writeToMongo_Initial(BasicDBObject doc, MultipartFile metsFile) {

        WriteResult wr = coll.update(doc, doc, true, false);
        if (wr.getField("upserted") != null) {
            this.docid = ((ObjectId) wr.getField("upserted")).toString();
            storeFileInMongo(metsFile, docid, "mets");
        }
    }




    private void writeToMongo(BasicDBObject teiBasicDBObject) {

        System.out.println(coll);
        WriteResult wr = coll.update(teiBasicDBObject, teiBasicDBObject, true, false);
        if (wr.getField("upserted") != null) {
            System.out.println("abc");
            ObjectId id = (ObjectId) wr.getField("upserted");
            System.out.println(id.toString());

        }
    }


    /**
     * Checks if the object is already in the db. The check is based on the pids
     * in the METS file. If the object is already in the db the docid will be
     * retrieved and set for the current instance.
     *
     * @return If any of the contained pids can be found in the db it returns true,
     * otherwise false.
     */
    private boolean isAlreadyInDB() {


//        IdHelper idHelper = new IdHelper();
//        Map<String, String> idsFromDB = idHelper.getPidsFromDB(db, coll_name);
//
//
//        String pid = idHelper.aleadyInDB(idMap, idsFromDB);
//
//        if (pid != null) {
//            this.alreadyInDB = true;
//            List<String> keyValuePair = idHelper.getKeyValuePairFor(this.idMap, pid);
//            this.docid = idHelper.findDocid(keyValuePair, db, coll_name);
//            return true;
//        }
        return false;
    }








    /**
     * Build a docInfo BasicDBObject with identifiers and title. The other info elements like
     * the url to a xml representation of mets document will be added later or if additional
     * documents will be stored (tei, teiEnriched, pageCount). See also usage of {@link #addOrChangeDocInfoField(String, String, String) addOrChangeDocInfoField}.
     *
     * @return The docinfo which can already be retrieved (title and identifiers).
     */
    private BasicDBObject handleDocinfo() {

        BasicDBObject doc = new BasicDBObject();




        return doc;
    }

    /**
     * Prepares the structures for the storage.
     *
     * @return The Id of the new/updated document.
     */

    private BasicDBObject prepareForStorage() {



        BasicDBObject doc = new BasicDBObject();




        return doc;
    }

    /**
     * Creates a query object with the docid of this instance.
     *
     * @return The query object.
     */
    private DBObject getQueryBasicDBObject(String docid) {

        if (docid == null)
            return null;

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("_id", new ObjectId(docid));

        return basicDBObject;
    }




    /**
     * Returns the filename without extension.
     *
     * @return The Filename.
     */
    public String getFilename() {
        int i = filename.lastIndexOf(".");
        return filename.substring(0, i);
    }


}
