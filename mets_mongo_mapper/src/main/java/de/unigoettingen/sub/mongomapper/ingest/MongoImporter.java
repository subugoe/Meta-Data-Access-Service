package de.unigoettingen.sub.mongomapper.ingest;


import de.unigoettingen.sub.jaxb.*;
import de.unigoettingen.sub.medas.model.RelatedItem;
import de.unigoettingen.sub.mongomapper.helper.ShortDocInfo;

import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 * <p/>
 * <p/>
 * On ingest of a new embedded tei document, the docinfo needs to modified too.
 */
@Component
public class MongoImporter {

    private final Logger logger = LoggerFactory.getLogger(MongoImporter.class);

    private String filename;
    private String handling;
    private String appUrlString;


    //---


    @Autowired()
    private MongoDbMetsRepository metsRepo;
    private boolean isCollection = true;


    public MongoImporter() {
    }


    /**
     * Processes a Mets structure and store these to mongoDB.
     *
     * @param metsFile     The METS file to store in mongodb.
     * @param handling     If a document is already in the db it will be replaced. The existence test will be performed
     *                     via the (recordInfo) recordIdentifier element. Possibilities values:
     *                     reject:     Rejects the request, the file will not be stored.
     *                     replace:    The new METS file will replace an existing document in mongoDB (default).
     * @param appUrlString The application URL (schema://host:port/).
     */
    public void storeMetsDocument(MultipartFile metsFile, String handling, String appUrlString) {

        this.appUrlString = appUrlString;
        this.filename = metsFile.getOriginalFilename();
        this.handling = handling;


        // TODO execution of validate fails, because of the used mets schema in METSWrapper.validate()
        //metswrapper.validate();


        InputStream inputStream = null;

        // TODO better exception handling required
        try {

            inputStream = metsFile.getInputStream();

            try {

                JAXBContext jaxbctx = JAXBContext.newInstance(Mets.class);

                Unmarshaller um = jaxbctx.createUnmarshaller();

                Mets mets = (Mets) um.unmarshal(inputStream);

                ShortDocInfo shortDocInfo = this.checkIfExist(mets);

                if (shortDocInfo != null) {
                    if (handling.equals("reject")) {
                        logger.info("ingest is rejected, because there is already a document with the recordIdentifier: " + shortDocInfo.getRecordIdentifier());
                        return;
                    }

                    removeMetsDocument(shortDocInfo);

                    mets.setID(shortDocInfo.getDocid());
                }

                saveMods(mets);
                saveMets(mets);
                addType(mets);



            } catch (JAXBException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void removeMetsDocument(ShortDocInfo shortDocInfo) {
        this.logger.info("removeMets Mets document with recordIdentifier: " + shortDocInfo.getRecordIdentifier());

        removeReferencedModsDocuments(shortDocInfo.getDocid());
        metsRepo.removeMets(shortDocInfo.getDocid());
    }

    private void removeReferencedModsDocuments(String docid) {

        Mets mets = metsRepo.findOneMets(docid);
        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                metsRepo.removeMods(mods.getID());
            }
        }

    }

    private void getRecordIdentifier(Mets mets) {

    }

    private void saveMets(Mets mets) {

        metsRepo.saveMets(mets);
    }


    private void saveMods(Mets mets) {

        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSecType : dmdSecs) {
            MdSecType.MdWrap mdWrap = mdSecType.getMdWrap();
            MdSecType.MdWrap.XmlData xmlData = mdWrap.getXmlData();
            List<Mods> modsList = xmlData.getMods();

            for (Mods mods : modsList) {
                metsRepo.saveMods(mods);
            }
        }
    }

    private void addType(Mets mets) {

        metsRepo.findAndModifyMets(mets.getID(), this.isCollection);

    }



    /**
     * The method checks if the document is in the DB, and returns the docid and recordIdentifier packed as a
     * ShortDocInfo object or null if not in the db. The test will be performed with the recordIdentifier.
     *
     * @param mets The Mets document to check.
     * @return The docid if already stored, otherwise null.
     */
    private ShortDocInfo checkIfExist(Mets mets) {

        this.isCollection = true;

        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                List<Object> objectList = mods.getElements();
                for (Object obj : objectList) {
                    if (obj instanceof RecordInfoType) {
                        List<Object> elements = ((RecordInfoType) obj).getElements();
                        for (Object o : elements) {
                            if (o instanceof RecordInfoType.RecordIdentifier) {
                                String recId = ((RecordInfoType.RecordIdentifier) o).getValue();

                                    Mods m = metsRepo.findModsByRecordIdentifier(recId);

                                    if (m != null) {

                                        Mets resultsMets = metsRepo.findMetsByModsId(m.getID());

                                        String docid = resultsMets.getID();

                                        ShortDocInfo shortDocInfo = new ShortDocInfo(docid, recId);

                                        // System.out.println(shortDocInfo);

                                        return shortDocInfo;
                                    }


                            }
                        }
                    } else if (obj instanceof RelatedItemType) {
                        RelatedItemType relatedItemType = ((RelatedItemType) obj);
                        if (relatedItemType.getType().equalsIgnoreCase("host")) {
                            this.isCollection = false;
                        }

                    }
                }
            }
        }

        return null;

    }


//    /**
//     * Stores a TEI structure to mongoDB.
//     *
//     * @param teiFile      The TEI file to store in mongodb.
//     * @param docid        The docid of the related METS document.
//     * @param type         The object type e.g. "mets", "tei"
//     * @param teiType      The TEI type, possibilities are {tei | teiEnriched}.
//     * @param appUrlString The application URL (schema://host:port/).
//     */
//    public void processTeiAndStore(MultipartFile teiFile, String docid, String type, String teiType, String appUrlString) {
//
//        this.teiType = teiType;
//        this.appUrlString = appUrlString;
//        this.docid = docid;
//        InputStream inputStream = null;
//        StaxHandler staxHandler = null;
//
//        BasicDBObject teiBasicDBObject = null;
//
//        try {
//            inputStream = teiFile.getInputStream();
//            staxHandler = new StaxHandler(inputStream);
//            teiBasicDBObject = staxHandler.processXMLFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // because of the priorisation on METS tei will no longer be mapped
//        // saves the tei elements as fields in a mongo tei collection/document
//        //this.writeToMongo(teiBasicDBObject);
//
//        storeFileInMongo(teiFile, docid, type);
//
//        String content = String.format(this.appUrlString + "/documents/%s/tei?type=%s", docid, teiType);
//
//        if (teiType.equalsIgnoreCase("tei"))
//            this.addOrChangeDocInfoField(docid, "tei", content);
//        else if (teiType.equalsIgnoreCase("teiEnriched"))
//            this.addOrChangeDocInfoField(docid, "teiEnriched", content);
//
//    }


//    private void addOrChangeDocInfoField(String docid, String field, String content) {
//
////        DBObject dbObject = this.coll.find(this.getQueryBasicDBObject(docid)).toArray().get(0);
////
////        DBObject docinfo = (DBObject) dbObject.get("docinfo");
////        docinfo.put(field, content);
////
////        this.coll.findAndModify(this.getQueryBasicDBObject(docid), dbObject);
//    }


//    public void storeFileInMongo(MultipartFile file, String docid, String type) {
//
//        File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
//                file.getOriginalFilename());
//
//        this.checkIfExistAndRemove(type);
//
//        try {
//            file.transferTo(tmpFile);
//
//            GridFSInputFile gridFS_InputFile;
//
//            // TODO better Exception handling required
//
//            gridFS_InputFile = gridFs.createFile(tmpFile);
//            gridFS_InputFile.setFilename(tmpFile.getName());
//
//            BasicDBObject basicDBObject = new BasicDBObject("relatedObjId", docid).append("type", type);
//            if (type.equalsIgnoreCase("tei"))
//                basicDBObject.append("teiType", this.teiType);
//
//            gridFS_InputFile.setMetaData(basicDBObject);
//            gridFS_InputFile.save();
//
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        } finally {
//            tmpFile.delete();
//        }
//    }


//    /**
//     * Checks if an mets or tei or teiEnriched file is already associated with
//     * the current docid and stored in the db. If so, the existing file will
//     * removed and the new one will be stored.
//     *
//     * @param type The documentype {mets | tei | teiEnriched}
//     */
//    private void checkIfExistAndRemove(String type) {
//
////        GridFS gridFs = new GridFS(db, mets_coll_name);
////        BasicDBObject query;
////
////        if (type.equalsIgnoreCase("mets")) {
////            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
////                    append("type", type));
////        } else {
////            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
////                    append("type", type).
////                    append("teiType", teiType));
////        }
////
////        gridFs.removeMets(query);
//    }


//    /**
//     * Retrieves the pids of the current METS file and stores these in the local
//     * Map idMap. See also {@link #idNodeListToList(org.w3c.dom.NodeList, Boolean) idNodeListToList}.
//     */
//    private List<Id> retrievePids() {
//
//        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
//        NodeList recordIdentifier = null;
//
//        if (modsNsUri != null && !modsNsUri.equals(""))
//            recordIdentifier = this.document.getElementsByTagNameNS(modsNsUri, "recordIdentifier");
//
//        if (recordIdentifier == null || recordIdentifier.getLength() == 0)
//            recordIdentifier = this.document.getElementsByTagName("mods:recordIdentifier");
//
//        if (recordIdentifier != null && recordIdentifier.getLength() > 0)
//            return idNodeListToList(recordIdentifier, true);
//
//
//        NodeList identifier = null;
//
//        if (modsNsUri != null && !modsNsUri.equals(""))
//            identifier = this.document.getElementsByTagNameNS(modsNsUri, "identifier");
//
//        if (identifier == null || identifier.getLength() == 0)
//            identifier = this.document.getElementsByTagName("mods:identifier");
//
//        if (identifier != null && identifier.getLength() > 0)
//            return idNodeListToList(identifier, false);
//
//        return new ArrayList<Id>();
//    }


//    /**
//     * Retrieves the classifiers of the current METS file and stores these in the local
//     * Map classifiersMap.
//     */
//    private List<Classifier> retrieveClassifiers() {
//
//
//        List<Classifier> classificationList = new ArrayList<>();
//
//        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
//        NodeList classifiers = null;
//
//        if (modsNsUri != null && !modsNsUri.equals(""))
//            classifiers = this.document.getElementsByTagNameNS(modsNsUri, "classification");
//
//        if (classifiers == null || classifiers.getLength() == 0)
//            classifiers = this.document.getElementsByTagName("mods:classification");
//
//        if (classifiers != null && classifiers.getLength() > 0) {
//            for (int i = 0; i < classifiers.getLength(); i++) {
//                Node classifier = classifiers.item(i);
//                Node authorityNode = classifier.getAttributes().getNamedItem("authority");
//                String authority = "";
//                if (authorityNode != null)
//                    authority = authorityNode.getNodeValue();
//
//                String value = classifier.getFirstChild().getNodeValue();
//
//                classificationList.add(new Classifier(authority, value));
//            }
//        }
//
//        return classificationList;
//    }


//    /**
//     * Retrieves the titles of the document.
//     *
//     * @return A list of titles, retrieved from the document.
//     */
//    private List<String> retrieveDocumentTitles() {
//
//        List<String> titleList = new ArrayList<String>();
//
//        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
//        NodeList titles = null;
//
//        if (modsNsUri != null && !modsNsUri.equals(""))
//            titles = this.document.getElementsByTagNameNS(modsNsUri, "title");
//
//        if (titles == null || titles.getLength() == 0)
//            titles = this.document.getElementsByTagName("mods:title");
//
//
//        // must check, because the mods:part element detail, has also an element title
//        if (titles != null && titles.getLength() > 0) {
//            for (int i = 0; i < titles.getLength(); i++) {
//                Node node = titles.item(i);
//                if (node.getParentNode().getParentNode().getNodeName().equalsIgnoreCase("mods:mods")) {
//
//                    Node titleNode = titles.item(i).getFirstChild();
//                    String title = "";
//                    if (titleNode != null)
//                        title = titleNode.getNodeValue();
//
//                    titleList.add(title);
//                }
//            }
//        }
//        return titleList;
//    }


//    /**
//     * Retrieves the related items of the document.
//     *
//     * @return A list of titles, retrieved from the document.
//     */
//    private List<RelatedItem> retrieveRelatedItems() {
//
//        List<RelatedItem> relatedItemList = new ArrayList<RelatedItem>();
//
//        String modsNsUri = NsHelper.getNs(getNsMap(), "mods");
//        NodeList relatedItems = null;
//
//        if (modsNsUri != null && !modsNsUri.equals(""))
//            relatedItems = this.document.getElementsByTagNameNS(modsNsUri, "relatedItem");
//
//        if (relatedItems == null || relatedItems.getLength() == 0)
//            relatedItems = this.document.getElementsByTagName("mods:relatedItem");
//
//        if (relatedItems != null && relatedItems.getLength() > 0) {
//
//            for (int i = 0; i < relatedItems.getLength(); i++) {
//                Node node = relatedItems.item(i);
//
//                if (node.getParentNode().getNodeName().equalsIgnoreCase("mods:mods")) {
//
//                    NamedNodeMap attributes = node.getAttributes();
//
//                    String type = "";
//                    Node typeNode = attributes.getNamedItem("type");
//
//                    if (typeNode != null)
//                        type = typeNode.getNodeValue();
//
//                    //if (type != null && !type.equals("")) {
//
//                    // mods:recordIdentifier
//                    Node relatedItemIdNode = node.getFirstChild().getFirstChild();
//                    Node sourceNode = relatedItemIdNode.getAttributes().getNamedItem("source");
//                    String source = "";
//                    String relatedItemRecordIdentifier = "";
//
//                    if (sourceNode != null) {
//                        source = sourceNode.getNodeValue();
//                    }
//
//                    relatedItemRecordIdentifier = relatedItemIdNode.getFirstChild().getNodeValue();
//                    relatedItemList.add(new RelatedItem(type, relatedItemRecordIdentifier, source));
//
//                    //}
//                }
//            }
//        }
//        return relatedItemList;
//    }


//    /**
//     * Helper for {@link #retrievePids() retrievePids}. Retrieves the type (PURL, PPN, ...) and the value of
//     * the identifiers in the given NodeList. For the recordIdentifier it sets the
//     * type to "recordIdentifier".
//     *
//     * @param identifier The NodeList of identifiers, found in the MEST file.
//     */
//    private List<Id> idNodeListToList(NodeList identifier, Boolean isRecordIdentifier) {
//
//        List<Id> idList = new ArrayList<Id>();
//
//        for (int i = 0; i < identifier.getLength(); i++) {
//
//
//            Node node = identifier.item(i);
//
//            // use just the identifiers of "this" object, not e.g. from "mods:relatedItems"
//            if (isObjectIdentifierOfThisObject(node)) {
//
//                NamedNodeMap attributes = node.getAttributes();
//                Node typeAttr = attributes.getNamedItem("type");
//                Node sourceAttr = attributes.getNamedItem("source");
//
//                Id id = new Id(isRecordIdentifier);
//
//                if (typeAttr != null)
//                    id.setType(typeAttr.getNodeValue());
//
//                if (sourceAttr != null)
//                    id.setSource(sourceAttr.getNodeValue());
//
//                id.setValue(node.getFirstChild().getNodeValue().trim().replaceAll("\\s+", " "));
//
//                idList.add(id);
//            }
//        }
//
//        return idList;
//    }


//    /**
//     * Checks if the node describes "this" object and not e.g. a "mods:relatedItem"
//     *
//     * @param node The node to check.
//     * @return true, if the node describes "this" object, otherwise false.
//     */
//    private boolean isObjectIdentifierOfThisObject(Node node) {
//
//
//        if (node.getNodeName().contains("recordIdentifier") &&
//                node.getParentNode().getParentNode().getNodeName().equalsIgnoreCase("mods:mods"))
//            return true;
//
//        if (node.getNodeName().contains("identifier") &&
//                node.getParentNode().getNodeName().equalsIgnoreCase("mods:mods"))
//            return true;
//
//
//        return false;
//    }


//    /**
//     * Initiates the precessing of the METS document. The retrieved
//     * elements are stored in instance variables as BasicDBObjects
//     * (JSON-like).
//     */
//    private boolean process() {
////
////        Boolean alreadyInDB = false;
////
////        // find namespaces
////        namespace_json = handleNamespaces();
////
////        // find the pids within the mets
////        idList = retrievePids();
////
////
////        titleList = retrieveDocumentTitles();
////
////        classificationList = retrieveClassifiers();
////
////        relatedItemList = retrieveRelatedItems();
////
////        // checks if the doc is already in the db. If the request contains
////        // the flag "reject" the controll gets back to the caller.
////
////        if (alreadyInDB = isMetsFileAlreadyInDB()) {
////
////            if (handling.equalsIgnoreCase("reject"))
////                return true;
////
////            if (handling.equalsIgnoreCase("replace"))
////                this.removeFileFromMongo("mets", this.docid);
////        }
////
////        // process dmdSec
////        DmdSecHelper dmdSecHelper = new DmdSecHelper(document, this);
////        dmdsec_json_list = dmdSecHelper.handleDmdSec(mets);
////
////        // summary about the object
////        docinfo_json = handleDocinfo();
////
////        // process the mets root element
////        mets_json = handleMetsRoot();
////
////        // process MetsHdr
////        hdr_json = metsHdrHelper.handleMetsHdr(mets);
////
////
////        // process id's retrieved from dmdSec
////        //id_json = handleIds();
////
////        // process amdSec
////        AmdSecHelper amdSecHelper = new AmdSecHelper(document, this);
////        amdsec_json_list = amdSecHelper.handleAmdSec(mets);
////
////        // process fileSec
////        filesec_json = fileSecHelper.handleFileSec(mets);
////
////        // process structMap
////        structmap_json_list = structMapHelper.handleStructMap(mets);
////
////        // process structLink
////        structlink_json = structLinkHelper.handleStructLink(mets);
////
////        // process behaviorSec
////        behavior_json_list = behaviorSecHelper.handleBehaviorSec(mets);
////
////        return alreadyInDB;
//        return false;
//    }


//    /**
//     * Stores the elements of a METS document as fields of the related mongo document. It
//     * also stores the METS file in mongo special collections for metadata and files
//     * (metadata: <db>.<coll>.files, file: <db>.<coll>.chunks).
//     *
//     * @param doc      The METS document to store in the db, as an BasicDBObject.
//     * @param metsFile The METS file to store in mongodb.
//     */
//    private void writeToMongo(BasicDBObject doc, MultipartFile metsFile) {
//
//        if (this.docid != null && handling.equalsIgnoreCase("replace")) {
//            this.writeToMongo_Replace(doc, metsFile);
//
//        } else {
//            // initial ingest
//            this.writeToMongo_Initial(doc, metsFile);
//        }
//
//        // TODO create index on objId and label (only if it not yet exist)
//    }


//    /**
//     * Helper for {@link #writeToMongo(com.mongodb.BasicDBObject, org.springframework.web.multipart.MultipartFile)}  writeToMongo}.
//     * The method handles the case of the initial ingest.
//     *
//     * @param doc      The METS document to store in the db, as an BasicDBObject.
//     * @param metsFile The METS file to store in mongodb.
//     */
//    private void writeToMongo_Initial(BasicDBObject doc, MultipartFile metsFile) {
//
////        WriteResult wr = coll.update(doc, doc, true, false, WriteConcern.SAFE);
////        if (wr.getField("upserted") != null) {
////            this.docid = ((ObjectId) wr.getField("upserted")).toString();
////            storeFileInMongo(metsFile, docid, "mets");
////        }
//    }


//    /**
//     * Helper for {@link #writeToMongo(com.mongodb.BasicDBObject, org.springframework.web.multipart.MultipartFile) writeToMongo}.
//     * The method handles the case to replace an existing db document.
//     *
//     * @param doc      The METS document to store in the db, as an BasicDBObject.
//     * @param metsFile The METS file to store in mongodb.
//     */
//    private void writeToMongo_Replace(BasicDBObject doc, MultipartFile metsFile) {
//
////        WriteResult wr = coll.update(getQueryBasicDBObject(this.docid), doc, true, false, WriteConcern.SAFE);
////        storeFileInMongo(metsFile, docid, "mets");
//    }


//    private void writeToMongo(BasicDBObject teiBasicDBObject) {
//
//        WriteResult wr = tei_coll.update(teiBasicDBObject, teiBasicDBObject, true, false, WriteConcern.SAFE);
////        if (wr.getField("upserted") != null) {
////            System.out.println((ObjectId) wr.getField("upserted"));
////
////        }
//    }


//    private void removeFileFromMongo(String type, String docid) {
//
//        gridFs.remove(new BasicDBObject("metadata.relatedObjId", docid).append("metadata.type", type));
//    }


//    /**
//     * Checks if the object is already in the db. The check is based on the pids
//     * in the METS file. If the object is already in the db the docid will be
//     * retrieved and set for the current instance.
//     *
//     * @return If any of the contained pids can be found in the db it returns true,
//     * otherwise false.
//     */
//    private boolean isMetsFileAlreadyInDB() {
//
////        Stri        ng value = this.getMajorId().getValue();
////        IdHelper idHelper = new IdHelper();
////        this.docid = idHelper.findDocid(value, db, mets_coll_name);
////
////        if (this.docid != null)
////            return true;
////        else
//        return false;
//
////        IdHelper idHelper = new IdHelper();
////        Map<String, String> idsFromDB = idHelper.getPidsFromDB(db, mets_coll_name);
////
////        String pid = idHelper.aleadyInDB(this.getMajorId(), idsFromDB);
////
////        if (pid != null) {
////            this.alreadyInDB = true;
////            //List<String> keyValuePair = idHelper.getKeyValuePairFor(this.idMap, pid);
////            this.docid = idHelper.findDocid(this.getMajorId(), db, mets_coll_name);
////
////            return true;
////        }
////        return false;
//    }


//    private boolean isTeiFileAlreadyInDB(String docid) {
//
//
//        IdHelper idHelper = new IdHelper();
//        Map<String, String> idsFromDB = idHelper.getPidsFromDB(db, mets_coll_name);
//
//
//        String pid = idHelper.aleadyInDB(idMap, idsFromDB);
//
//        if (pid != null) {
//            this.alreadyInDB = true;
//            List<String> keyValuePair = idHelper.getKeyValuePairFor(this.idMap, pid);
//            this.docid = idHelper.findDocid(keyValuePair, db, mets_coll_name);
//            return true;
//        }
//        return false;
//    }


//    /**
//     * The method reads the namespaces from the METS file.
//     * <p/>
//     * A definition like: xmlns:METS="http://..." results in a
//     * file {"METS":"http://..."}
//     *
//     * @return The namespaces packed as an BasicDBObject.
//     */
//    private BasicDBObject handleNamespaces() {
//
//        //List<BasicDBObject> basicDBObjectList = new ArrayList<BasicDBObject>();
//        BasicDBObject basicDBObject = new BasicDBObject();
//
//
//        NamedNodeMap namedNodeMap = document.getFirstChild().getAttributes();
//        int i = namedNodeMap.getLength();
//        for (int j = 0; j < i - 1; j++) {
//            String tag = namedNodeMap.item(j).getNodeName();
//            String url = namedNodeMap.item(j).getNodeValue();
//            if (tag.contains("xmlns")) {
//                tag = tag.split(":")[1];
//                nsMap.put(tag, url);
//                basicDBObject.append(tag, url);
//            }
//        }
//        return basicDBObject;
//    }

//    /**
//     * Retrieves the info from the METS root element an saves these in a BasicDBObject.
//     *
//     * @return The METS root info as an BesicDBObject.
//     */
//    private BasicDBObject handleMetsRoot() {
//
//        BasicDBObject doc = new BasicDBObject();
////        basicDBObjectHelper.createBasicDBObject("ID", mets.getID(), doc);
////        basicDBObjectHelper.createBasicDBObject("OBJID", mets.getObjID(), doc);
////        basicDBObjectHelper.createBasicDBObject("LABEL", mets.getLabel(), doc);
////        basicDBObjectHelper.createBasicDBObject("TYPE", mets.getType(), doc);
////        basicDBObjectHelper.createBasicDBObject("PROFILE", mets.getProfile(), doc);
//        return doc;
//    }

//    /**
//     * Retrieves the pid and saves these in a BasicDBObject.
//     *
//     * @return The major pid's of the described object.
//     */
//    private BasicDBObject handleIds() {
//
//        return getMajorId().toJSON();
//    }

//    /**
//     * Build a docInfo BasicDBObject with identifiers and title. The other info elements like
//     * the url to a xml representation of mets document will be added later or if additional
//     * documents will be stored (tei, teiEnriched, pageCount). See also usage of {@link #addOrChangeDocInfoField(String, String, String) addOrChangeDocInfoField}.
//     *
//     * @return The docinfo which can already be retrieved (title and identifiers).
//     */
//    private BasicDBObject handleDocinfo() {
//
//        BasicDBObject doc = new BasicDBObject();
//
//        doc.append("id", this.getMajorId().toJSON());
//
//        if (!this.titleList.isEmpty())
//            doc.append("title", this.titleList.get(0));
//
//
//        if (!this.relatedItemList.isEmpty()) {
//            BasicDBList list = new BasicDBList();
//            for (RelatedItem item : relatedItemList) {
//                list.add(item.getAsJSON());
//            }
//
//            doc.append("relatedItem", list);
//        }
//
//        if (!this.classificationList.isEmpty()) {
//            BasicDBList classifierList = new BasicDBList();
//            for (Classifier classifier : classificationList) {
//                BasicDBObject basicDBObject = new BasicDBObject();
//                basicDBObject.append("authority", classifier.getAuthority());
//                basicDBObject.append("value", classifier.getValue());
//                classifierList.add(basicDBObject);
//            }
//            doc.append("classification", classifierList);
//        }
//
//        // TODO After TEI mapping
////            if (this.docinfoMap.containsKey("preview"))
////                doc.append("preview", this.docinfoMap.get("preview"));
////
////            if (this.docinfoMap.containsKey("pageCount"))
////                doc.append("pageCount", this.docinfoMap.get("pageCount"));
////
////            if (this.docinfoMap.containsKey("fulltext"))
////                doc.append("fulltext", this.docinfoMap.get("fulltext"));
//
//
//        return doc;
//    }

//    /**
//     * Prepares the structures for the storage.
//     *
//     * @return The Id of the new/updated document.
//     */
//
//    private BasicDBObject prepareForStorage() {
//
//        BasicDBObject doc = new BasicDBObject();
//
////        if (!id_json.isEmpty())
////            doc.append("ids", id_json);
//
//        if (!mets_json.isEmpty())
//            doc.append("mets", mets_json);
//
//        if (!docinfo_json.isEmpty())
//            doc.append("docinfo", docinfo_json);
//
//        if (!namespace_json.isEmpty())
//            doc.append("namespaces", namespace_json);
//
//        if (!hdr_json.isEmpty())
//            doc.append("metsHdr", hdr_json);
//
//        if (!dmdsec_json_list.isEmpty())
//            doc.append("dmdSec", dmdsec_json_list);
//
//
//        if (!amdsec_json_list.isEmpty())
//            doc.append("amdSec", amdsec_json_list);
//
//        if (!filesec_json.isEmpty())
//            doc.append("fileSec", filesec_json);
//
//        doc.append("structMap", structmap_json_list);
//
//        if (!structlink_json.isEmpty())
//            doc.append("structLink", structlink_json);
//
//        if (!behavior_json_list.isEmpty())
//            doc.append("behaviorSec", "not supported");
//
//        return doc;
//    }


//    public void setNsMap(Map<String, String> nsMap) {
//        this.nsMap = nsMap;
//    }
//
//    public Map<String, String> getNsMap() {
//        return nsMap;
//    }


//    /**
//     * Creates a query object with the docid of this instance.
//     *
//     * @return The query object.
//     */
//    private DBObject getQueryBasicDBObject(String docid) {
//
//        if (docid == null)
//            return null;
//
//        BasicDBObject basicDBObject = new BasicDBObject();
//        basicDBObject.append("_id", new ObjectId(docid));
//
//        return basicDBObject;
//    }

//    /**
//     * Returns the filename without extension.
//     *
//     * @return The Filename.
//     */
//    public String getFilename() {
//        int i = filename.lastIndexOf(".");
//        return filename.substring(0, i);
//    }


//    public Id getMajorId() {
//
//        Map<String, Id> ids = new HashMap<>();
//
//        for (Id id : this.idList) {
//
//            if (id.isRecordIdentifier() && !ids.containsKey("recordIdentifier"))
//                ids.put("recordIdentifier", id);
//            else
//                ids.put(id.getType(), id);
//        }
//
//
//        if (ids.containsKey("recordIdentifier"))
//            return ids.get("recordIdentifier");
//        else if (ids.containsKey("PPN"))
//            return ids.get("PPN");
//        else if (ids.containsKey("PPNanalog"))
//            return ids.get("PPNanalog");
//        else {
//
//            Id filenameBasedId = new Id(false);
//            filenameBasedId.setValue(this.getFilename());
//            filenameBasedId.setType("fileNameBased");
//            return filenameBasedId;
//        }
//    }


}
