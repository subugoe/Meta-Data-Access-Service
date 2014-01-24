package de.unigoettingen.sub.mongomapper.ingest;

import au.edu.apsr.mtk.base.*;
import au.edu.apsr.mtk.ch.METSReader;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import de.unigoettingen.sub.mongomapper.helper.IdHelper;
import de.unigoettingen.sub.mongomapper.helper.mets.*;

import de.unigoettingen.sub.mongomapper.helper.tei.StaxHandler;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
public class MongoImporter {

    private final Logger logger = LoggerFactory.getLogger(MongoImporter.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();


    private final MetsHdrHelper metsHdrHelper = new MetsHdrHelper();
    private final BehaviorSecHelper behaviorSecHelper = new BehaviorSecHelper();
    private final FileSecHelper fileSecHelper = new FileSecHelper();
    private final StructMapHelper structMapHelper = new StructMapHelper();
    private final StructLinkHelper structLinkHelper = new StructLinkHelper();


    private final String HOST = "localhost";
    private final int PORT = 27017;

    private MongoClient mongoClient = null;
    private METSWrapper metswrapper = null;

    private DB db = null;
    private DBCollection coll = null;
    private GridFS gridFs = null;
    private DBCollection tei_coll = null;


    private String db_name = "";
    private String mets_coll_name = "";
    private String tei_coll_name = "";

    private METS mets = null;
    private Document document;

    private BasicDBObject docinfo_json = null;
    private BasicDBObject mets_json = null;
    private BasicDBObject id_json = null;
    private BasicDBObject hdr_json = null;
    private BasicDBObject namespace_json = null;
    private List<BasicDBObject> dmdsec_json_list = null;
    private List<BasicDBObject> amdsec_json_list = null;
    private BasicDBObject filesec_json = null;
    private List<BasicDBObject> structmap_json_list = null;
    private BasicDBObject structlink_json = null;
    private List<BasicDBObject> behavior_json_list = null;

    private Map<String, String> idMap = null;
    private Map<String, String> nsMap = null;
    private List<String> titleList = null;

    private String filename;
    private boolean alreadyInDB = false;
    private String handling;
    private String docid = null;
    private String teiType = null;
    private String appUrlString;


    /**
     * Construct the object with required parameters
     *
     * @param db_name        The name of the mongoDB
     * @param mets_coll_name The name of the collection, to store the documents
     */
    public MongoImporter(String db_name, String mets_coll_name, String tei_coll_name) {
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
        // TODO better exception handling required
        try {
            mongoClient = new MongoClient();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        db = mongoClient.getDB(this.db_name);

        coll = db.getCollection(this.mets_coll_name);
        gridFs = new GridFS(db, mets_coll_name);

        tei_coll = db.getCollection(this.tei_coll_name);

    }


    /**
     * Stores a TEI structure to mongoDB.
     *
     * @param teiFile      The TEI file to store in mongodb.
     * @param docid        The docid of the related METS document.
     * @param type         The object type e.g. "mets", "tei"
     * @param teiType      The TEI type, possibilities are {tei | teiEnriched}.
     * @param appUrlString The application URL (schema://host:port/).
     */
    public void processTeiAndStore(MultipartFile teiFile, String docid, String type, String teiType, String appUrlString) {

        this.teiType = teiType;
        this.appUrlString = appUrlString;
        this.docid = docid;
        InputStream inputStream = null;
        StaxHandler staxHandler = null;

        BasicDBObject teiBasicDBObject = null;

//        this.alreadyInDB = this.isTeiFileAlreadyInDB(docid);

        try {
            inputStream = teiFile.getInputStream();
            staxHandler = new StaxHandler(inputStream);
            teiBasicDBObject = staxHandler.processXMLFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
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

        System.out.println(docid);
        DBObject dbObject = this.coll.find(this.getQueryBasicDBObject(docid)).toArray().get(0);

        DBObject docinfo = (DBObject) dbObject.get("docinfo");
        docinfo.put(field, content);

//        dbObject.put(field, content);

        this.coll.findAndModify(this.getQueryBasicDBObject(docid), dbObject);
    }


    /**
     * Processes a Mets structure and store these to mongoDB.
     *
     * @param metsFile     The METS file to store in mongodb.
     * @param handling     Specifies what to do if a METS file with the same PID already
     *                     exist. Possibilities are:
     *                     reject:     Rejects the request, the file will not be stored.
     * @param appUrlString The application URL (schema://host:port/).
     */
    public void processMetsAndStore(MultipartFile metsFile, String handling, String appUrlString) {

        this.appUrlString = appUrlString;
        this.filename = metsFile.getOriginalFilename();

        idMap = new HashMap<String, String>();
        nsMap = new HashMap<String, String>();

        this.handling = handling;

        // TODO there should be only one METS file (overwrite) !!!

        METSReader metsreader = new METSReader();

        // TODO better exception handling required
        try {
            InputStream inputStream = metsFile.getInputStream();
            metsreader.mapToDOM(inputStream);
            inputStream.close();

        } catch (SAXException e) {
            logger.error(e.getMessage());
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        // TODO better exception handling required
        try {
            Document document = metsreader.getMETSDocument();

            metswrapper = new METSWrapper(document);

        } catch (METSException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        // TODO execution of validate fails, because of the used mets schema in METSWrapper.validate()
        //metswrapper.validate();

        mets = metswrapper.getMETSObject();
        document = metswrapper.getMETSDocument();

        this.alreadyInDB = this.process();

        if (this.alreadyInDB && handling.equalsIgnoreCase("reject")) {
            logger.info("Request rejected, because the METS file " +
                    this.filename + " is already in the db with mongo id " + this.docid);
            return;
        }

        this.writeToMongo(prepareForStorage(), metsFile);

        String content = String.format(this.appUrlString + "/documents/%s/mets", docid);
        this.addOrChangeDocInfoField(docid, "mets", content);
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

        GridFS gridFs = new GridFS(db, mets_coll_name);
        BasicDBObject query;

        if (type.equalsIgnoreCase("mets")) {
            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type));
        } else {
            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
                    append("type", type).
                    append("teiType", teiType));
        }

        System.out.println(query);

        gridFs.remove(query);
    }

    /**
     * Retrieves the pids of the current METS file and stores these in the local
     * Map idMap. See also {@link #idNodeListToMap(org.w3c.dom.NodeList, java.util.Map) idNodeListToMap}.
     */
    private Map<String, String> retrievePids() {


        Map<String, String> idMap = new HashMap<String, String>();
        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
        NodeList identifier = this.document.getElementsByTagNameNS(modsNsUri, "identifier");
        NodeList recordIdentifier = this.document.getElementsByTagNameNS(modsNsUri, "recordIdentifier");

        if (identifier != null)
            idNodeListToMap(identifier, idMap);
        if (recordIdentifier != null) {
            idNodeListToMap(recordIdentifier, idMap);
        }

        if (idMap.isEmpty())
            idMap.put("filename", this.getFilename());

        return idMap;
    }


    /**
     * Retrieves the titles of the document.
     *
     * @return A list of titles, retrieved from the document.
     */
    private List<String> retrieveDocumentTitles() {

        List<String> titleList = new ArrayList<String>();
        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
        NodeList titleNodeList = this.document.getElementsByTagNameNS(modsNsUri, "title");

        if (titleNodeList.getLength() > 0) {
            for (int i = 0; i < titleNodeList.getLength(); i++) {
                Node node = titleNodeList.item(i);
                if (node.getParentNode().getParentNode().getNodeName().equalsIgnoreCase("mods:mods"))
                    titleList.add(titleNodeList.item(i).getFirstChild().getNodeValue());
            }
        }
        return titleList;
    }

    /**
     * Helper for {@link #retrievePids() retrievePids}. Retrieves the type (PURL, PPN, ...) and the value of
     * the identifiers in the given NodeList. For the recordIdentifier it sets the
     * type to "recordIdentifier".
     *
     * @param identifier The NodeList of identifiers, found in the MEST file.
     * @param idMap      The map which takes the pid-type-value pairs.
     */
    private void idNodeListToMap(NodeList identifier, Map<String, String> idMap) {

        for (int i = 0; i < identifier.getLength(); i++) {


            Node node = identifier.item(i);

            // use just the identifiers of "this" object, not e.g. from "mods:relatedItems"
            if (isObjectIdentifierOfThisObject(node)) {

                NamedNodeMap attributes = node.getAttributes();
                Node attributeNode = attributes.getNamedItem("type");


                if (attributeNode != null) {
                    String attrKey = attributeNode.getNodeValue();
                    if (!idMap.containsKey(attrKey))
                        idMap.put(attrKey, node.getFirstChild().getNodeValue());
                }

                if (node.getNodeName().contains("recordIdentifier")) {
                    if (!idMap.containsKey("recordIdentifier"))
                        idMap.put("recordIdentifier", node.getFirstChild().getNodeValue().trim().replaceAll("\\s+", " "));
                }
            }
        }
    }

    /**
     * Checks if the node describes "this" object and not e.g. a "mods:relatedItem"
     *
     * @param node The node to check.
     * @return true, if the node describes "this" object, otherwise false.
     */
    private boolean isObjectIdentifierOfThisObject(Node node) {

        if (node.getNodeName().contains("recordIdentifier") &&
                node.getParentNode().getParentNode().getNodeName().equalsIgnoreCase("mods:mods"))
            return true;

        if (node.getNodeName().contains("identifier") &&
                node.getParentNode().getNodeName().equalsIgnoreCase("mods:mods"))
            return true;


        return false;
    }

    /**
     * Initiates the precessing of the METS document. The retrieved
     * elements are stored in instance variables as BasicDBObjects
     * (JSON-like).
     */
    private boolean process() {

        Boolean alreadyInDB = false;

        // find namespaces
        namespace_json = handleNamespaces();

        // find the pids within the mets
        idMap = retrievePids();

        titleList = retrieveDocumentTitles();


        // checks if the doc is already in the db. If the request contains
        // the flag "reject" the controll gets back to the caller.
        if (alreadyInDB = isMetsFileAlreadyInDB()) {
            if (handling.equalsIgnoreCase("reject"))
                return true;

            if (handling.equalsIgnoreCase("replace"))
                this.removeFileFromMongo("mets", this.docid);
        }

        // process dmdSec
        DmdSecHelper dmdSecHelper = new DmdSecHelper(document, this);
        dmdsec_json_list = dmdSecHelper.handleDmdSec(mets);

        // summary about the object
        docinfo_json = handleDocinfo();

        // process the mets root element
        mets_json = handleMetsRoot();

        // process MetsHdr
        hdr_json = metsHdrHelper.handleMetsHdr(mets);


        // process id's retrieved from dmdSec
        id_json = handleIds();

        // process amdSec
        AmdSecHelper amdSecHelper = new AmdSecHelper(document, this);
        amdsec_json_list = amdSecHelper.handleAmdSec(mets);

        // process fileSec
        filesec_json = fileSecHelper.handleFileSec(mets);

        // process structMap
        structmap_json_list = structMapHelper.handleStructMap(mets);

        // process structLink
        structlink_json = structLinkHelper.handleStructLink(mets);

        // process behaviorSec
        behavior_json_list = behaviorSecHelper.handleBehaviorSec(mets);

        return alreadyInDB;
    }


    /**
     * Stores the elements of a METS document as fields of the related mongo document. It
     * also stores the METS file in mongo special collections for metadata and files
     * (metadata: <db>.<coll>.files, file: <db>.<coll>.chunks).
     *
     * @param doc      The METS document to store in the db, as an BasicDBObject.
     * @param metsFile The METS file to store in mongodb.
     */
    private void writeToMongo(BasicDBObject doc, MultipartFile metsFile) {

        if (this.docid != null && handling.equalsIgnoreCase("replace")) {
            this.writeToMongo_Replace(doc, metsFile);

        } else {
            // initial ingest
            this.writeToMongo_Initial(doc, metsFile);
        }

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


    /**
     * Helper for {@link #writeToMongo(com.mongodb.BasicDBObject, org.springframework.web.multipart.MultipartFile) writeToMongo}.
     * The method handles the case to replace an existing db document.
     *
     * @param doc      The METS document to store in the db, as an BasicDBObject.
     * @param metsFile The METS file to store in mongodb.
     */
    private void writeToMongo_Replace(BasicDBObject doc, MultipartFile metsFile) {

        WriteResult wr = coll.update(getQueryBasicDBObject(this.docid), doc, true, false);
        storeFileInMongo(metsFile, docid, "mets");
    }

    private void writeToMongo(BasicDBObject teiBasicDBObject) {

        WriteResult wr = tei_coll.update(teiBasicDBObject, teiBasicDBObject, true, false);
        if (wr.getField("upserted") != null) {
            System.out.println((ObjectId) wr.getField("upserted"));

        }
    }

    private void removeFileFromMongo(String type, String docid) {

        gridFs.remove(new BasicDBObject("metadata.relatedObjId", docid).append("metadata.type", type));
    }

    /**
     * Checks if the object is already in the db. The check is based on the pids
     * in the METS file. If the object is already in the db the docid will be
     * retrieved and set for the current instance.
     *
     * @return If any of the contained pids can be found in the db it returns true,
     * otherwise false.
     */
    private boolean isMetsFileAlreadyInDB() {


        IdHelper idHelper = new IdHelper();
        Map<String, String> idsFromDB = idHelper.getPidsFromDB(db, mets_coll_name);


        String pid = idHelper.aleadyInDB(idMap, idsFromDB);

        if (pid != null) {
            this.alreadyInDB = true;
            List<String> keyValuePair = idHelper.getKeyValuePairFor(this.idMap, pid);
            this.docid = idHelper.findDocid(keyValuePair, db, mets_coll_name);
            return true;
        }
        return false;
    }

    private boolean isTeiFileAlreadyInDB(String docid) {


        IdHelper idHelper = new IdHelper();
        Map<String, String> idsFromDB = idHelper.getPidsFromDB(db, mets_coll_name);


        String pid = idHelper.aleadyInDB(idMap, idsFromDB);

        if (pid != null) {
            this.alreadyInDB = true;
            List<String> keyValuePair = idHelper.getKeyValuePairFor(this.idMap, pid);
            this.docid = idHelper.findDocid(keyValuePair, db, mets_coll_name);
            return true;
        }
        return false;
    }


    /**
     * The method reads the namespaces from the METS file.
     * <p/>
     * A definition like: xmlns:METS="http://..." results in a
     * file {"METS":"http://..."}
     *
     * @return The namespaces packed as an BasicDBObject.
     */
    private BasicDBObject handleNamespaces() {

        //List<BasicDBObject> basicDBObjectList = new ArrayList<BasicDBObject>();
        BasicDBObject basicDBObject = new BasicDBObject();

        NamedNodeMap namedNodeMap = document.getFirstChild().getAttributes();
        int i = namedNodeMap.getLength();
        for (int j = 0; j < i - 1; j++) {
            String tag = namedNodeMap.item(j).getNodeName();
            String url = namedNodeMap.item(j).getNodeValue();
            if (tag.contains("xmlns")) {
                tag = tag.split(":")[1];
                nsMap.put(tag, url);
                basicDBObject.append(tag, url);
            }
        }
        return basicDBObject;
    }

    /**
     * Retrieves the info from the METS root element an saves these in a BasicDBObject.
     *
     * @return The METS root info as an BesicDBObject.
     */
    private BasicDBObject handleMetsRoot() {

        BasicDBObject doc = new BasicDBObject();
        basicDBObjectHelper.createBasicDBObject("ID", mets.getID(), doc);
        basicDBObjectHelper.createBasicDBObject("OBJID", mets.getObjID(), doc);
        basicDBObjectHelper.createBasicDBObject("LABEL", mets.getLabel(), doc);
        basicDBObjectHelper.createBasicDBObject("TYPE", mets.getType(), doc);
        basicDBObjectHelper.createBasicDBObject("PROFILE", mets.getProfile(), doc);
        return doc;
    }

    /**
     * Retrieves the pid's and saves these in a BasicDBObject.
     *
     * @return The pid's of the described object.
     */
    private BasicDBObject handleIds() {

        BasicDBObject doc = new BasicDBObject();
        doc.putAll(idMap);

        return doc;
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

        if (this.idMap.containsKey("recordIdentifier")) {
            doc.append("id", this.idMap.get("recordIdentifier"));
        } else if (this.idMap.containsKey("PPN")) {
            doc.append("id", this.idMap.get("PPN"));
        } else if (this.idMap.containsKey("PPNanalog")) {
            doc.append("id", this.idMap.get("PPNanalog"));
        } else {
            doc.append("id", this.getFilename());
        }

        if (!this.titleList.isEmpty())
            doc.append("title", this.titleList.get(0));


        // TODO After TEI mapping
//            if (this.docinfoMap.containsKey("preview"))
//                doc.append("preview", this.docinfoMap.get("preview"));
//
//            if (this.docinfoMap.containsKey("pageCount"))
//                doc.append("pageCount", this.docinfoMap.get("pageCount"));
//
//            if (this.docinfoMap.containsKey("fulltext"))
//                doc.append("fulltext", this.docinfoMap.get("fulltext"));


        return doc;
    }

    /**
     * Prepares the structures for the storage.
     *
     * @return The Id of the new/updated document.
     */

    private BasicDBObject prepareForStorage() {

        BasicDBObject doc = new BasicDBObject();

        // TODO shouldn't be empty
        if (!id_json.isEmpty())
            doc.append("ids", id_json);

        if (!mets_json.isEmpty())
            doc.append("mets", mets_json);

        if (!docinfo_json.isEmpty())
            doc.append("docinfo", docinfo_json);

        if (!namespace_json.isEmpty())
            doc.append("namespaces", namespace_json);

        if (!hdr_json.isEmpty())
            doc.append("metsHdr", hdr_json);

        if (!dmdsec_json_list.isEmpty())
            doc.append("dmdSec", dmdsec_json_list);


        if (!amdsec_json_list.isEmpty())
            doc.append("amdSec", amdsec_json_list);

        if (!filesec_json.isEmpty())
            doc.append("fileSec", filesec_json);

        doc.append("structMap", structmap_json_list);

        if (!structlink_json.isEmpty())
            doc.append("structLink", structlink_json);

        if (!behavior_json_list.isEmpty())
            doc.append("behaviorSec", "not supported");

        return doc;
    }


    public void setNsMap(Map<String, String> nsMap) {
        this.nsMap = nsMap;
    }

    public Map<String, String> getNsMap() {
        return nsMap;
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
