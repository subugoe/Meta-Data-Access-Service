package de.unigoettingen.sub.mongomapper.access;

import com.mongodb.*;
import de.unigoettingen.sub.jaxb.*;
import de.unigoettingen.sub.medas.model.*;

import de.unigoettingen.sub.mongomapper.helper.ShortDocInfo;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
@Component
public class MongoExporter {

    private final Logger logger = LoggerFactory.getLogger(MongoExporter.class);


//    private final String db_name;
//    private final String mets_coll_name;
    //private final String tei_coll_name;                                             #

    private DB db = null;
    private DBCollection coll = null;
    private MongoClient mongoClient = null;


    //---
    @Autowired()
    private MongoDbMetsRepository metsRepo;


    public MongoExporter() {

    }

//    /**
//     * Construct the object with required parameters
//     *
//     * @param db_name        The name of the mongoDB
//     * @param mets_coll_name The name of the collection, to store the documents
//     */
//    public MongoExporter(String db_name, String mets_coll_name) {
//        this.db_name = db_name;
//        this.mets_coll_name = mets_coll_name;
//        //this.tei_coll_name = tei_coll_name;
//
//        init();
//    }


    protected void finalize() throws Throwable {
        //mongoClient.close();
    }

    /**
     * init() initializes the object, establishes the connection to the mongoDB server.
     */
    private void init() {

//        // init mongo
//        try {
//            mongoClient = new MongoClient();
//
//        } catch (UnknownHostException e) {
//            logger.error(e.getMessage());
//        }
//
//        db = mongoClient.getDB(this.db_name);
//        coll = db.getCollection(this.mets_coll_name);
    }

    // TODO we have currently no collection data in the db. to be continued if we get the data.
    public String getCollectionsAsXML(List<String> props, int skip, int limit) {

//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//
//        BasicDBObject query = new BasicDBObject();
//        query.put("docinfo.relatedItem.type", "host");
//
//        DBCursor dbCursor = coll.find(query, field).skip(skip).limit(limit);
//
//        XMLTag tag = XMLDoc.newDocument()
//                .addRoot("docs");
//
//        while (dbCursor.hasNext()) {
//
//            DBObject dbObject = dbCursor.next();
//            DocInfo docInfo = new DocInfo(props);
//            docInfo.setFromJSON(dbObject);
//
//            XMLTag t = docInfo.getAsXML();
//
//            tag.addDocument(t);
//        }
//        return tag.toString();

        return null;
    }

    // TODO we have currently no collection data in the db. to be continued if we get the data.
    public BasicDBObject getCollectionsAsJSON(List<String> props, int skip, int limit) {

        BasicDBObject docs = new BasicDBObject();
//        BasicDBList docList = new BasicDBList();
//
//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//
//
//        BasicDBObject query = new BasicDBObject();
//        QueryBuilder qb = new QueryBuilder();
////        qb.or(new QueryBuilder().start("docinfo.relatedItem").exists(false).
////                or("docinfo.relatedItem.type").notEquals("host").
////                or("docinfo.relatedItem.type").notEquals("preceding").
////                or("docinfo.relatedItem.type").notEquals("succeeding").get());//,
////                //new QueryBuilder().put("docinfo.relatedItem.type").notEquals("series").get());
//
//        qb.or(new QueryBuilder().put("docinfo.relatedItem").exists(false).get(),
//                new QueryBuilder().put("docinfo.relatedItem.type").notEquals("host").get(),
//                new QueryBuilder().put("docinfo.relatedItem.type").notEquals("preceding").get(),
//                new QueryBuilder().put("docinfo.relatedItem.type").notEquals("succeeding").get()
//        );
//
//        query.putAll(qb.get());
//
//        System.out.println(qb.toString());
//        query.putAll(qb.get());
//
//
//        DBCursor dbCursor = coll.find(query, field).skip(skip).limit(limit);
//
//        while (dbCursor.hasNext()) {
//
//
//            DBObject dbObject = dbCursor.next();
//
//            DocInfo docInfo = new DocInfo(props);
//            docInfo.setFromJSON(dbObject);
//            docList.add(docInfo.getAsJSON());
//
//            docs.append("docs", docList);
//        }

        return docs;
    }

    /**
     * Collects information about the documents in the repository.
     *
     * @param props
     * @param skip  The number of dokuments to skip.
     * @param limit The number of documents to get.
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
     */
    public BasicDBObject getDocumentsAsJSON(List<String> props, int skip, int limit) {
//
//        BasicDBObject docs = new BasicDBObject();
//        BasicDBList docList = new BasicDBList();
//
//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//        DBCursor dbCursor = coll.find(new BasicDBObject(), field).skip(skip);
//
//        if (limit > 0)
//            dbCursor = dbCursor.limit(limit);
//
//        while (dbCursor.hasNext()) {
//
//
//            DBObject dbObject = dbCursor.next();
//
//            DocInfo docInfo = new DocInfo(props);
//            docInfo.setFromJSON(dbObject);
//            docList.add(docInfo.getAsJSON());
//
//            docs.append("docs", docList);
//        }
//        return docs;
        return null;
    }


    public BasicDBObject getDocumentAsJSON(String docid, List<String> props) {
//
//        BasicDBObject docs = new BasicDBObject();
//
//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(docid));
//
//        DBObject dbObject = coll.findOne(query, field);
//
//
//        DocInfo docInfo = new DocInfo(props);
//        docInfo.setFromJSON(dbObject);
//
//        return docInfo.getAsJSON();
//        //return new BasicDBObject("doc", docInfo.getAsJSON());
        return null;
    }


    /**
     * Collects information about the documents in the repository.
     *
     * @param props
     * @param skip  The number of dokuments to skip.
     * @param limit The number of documents to get.
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
     */
    public String getDocumentsAsXML(List<String> props, int skip, int limit) {

        List<Mets> resultsMets = metsRepo.findAll();


//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//        DBCursor dbCursor = coll.find(new BasicDBObject(), field).skip(skip);
//
//        if (limit >0)
//            dbCursor = dbCursor.limit(limit);
//
//        XMLTag tag = XMLDoc.newDocument()
//                .addRoot("docs");
//
//        while (dbCursor.hasNext()) {
//
//            DBObject dbObject = dbCursor.next();
//            DocInfo docInfo = new DocInfo(props);
//            docInfo.setFromJSON(dbObject);
//
//            XMLTag t = docInfo.getAsXML();
//
//            tag.addDocument(t);
//        }
//        return tag.toString("UTF-8");
        return "not implemented";
    }


    public Doc getDocumentAsXML(String docid,
                                List<String> props, String metsUrlString) {

        Mets mets = metsRepo.findOne(docid);


        return retrieveDocInfo(mets, metsUrlString);

    }


    private Doc retrieveDocInfo(Mets mets, String metsUrl) {

        //List<Doc> docList = new ArrayList<>();
        List<MdSecType> dmdSecs = mets.getDmdSecs();


        // TODO currently just the first mdSec element will be examined - is this sufficient?
        // TODO currently just the first mods element will be examined - is this sufficient?

        Mods mods = dmdSecs.get(0).getMdWrap().getXmlData().getMods().get(0);

        List<Object> objectList = mods.getElements();

        Doc doc = new Doc();

        doc.setDocid(mets.getID());
        doc.setMets(metsUrl);

        processModsElements(objectList, doc);

        return doc;


        // mets-url
        //String metsUrl = String.format(this.getUrlString(request) + "/documents/%s/mets", docid);

        // page-count
        // Todo

        // related items


        // classification


//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(docid));
//
//        DBObject dbObject = coll.findOne(query, field);
//
//        DocInfo docInfo = new DocInfo(props);
//        docInfo.setFromJSON(dbObject);
//
//        return docInfo.getAsXML().toString("UTF-8");


        // ---

//        OutputStream out = null;
//
//        Mets resultsMets = metsRepo.findOne(docid);
//
//        JAXBContext jaxbctx = null;
//        Marshaller m = null;
//
//        try {
//            jaxbctx = JAXBContext.newInstance(Mets.class);
//            m = jaxbctx.createMarshaller();
//            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//            m.marshal(resultsMets, response.getOutputStream());
//            response.flushBuffer();
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void processModsElements(List<Object> objectList, Doc doc) {

        for (Object obj : objectList) {

            if (obj instanceof RecordInfoType) {
                RecordInfoType recordInfoType = (RecordInfoType) obj;
                List<RecordIdentifier> recordIdentifiers = this.getRecordIdentifiers(recordInfoType);
                doc.addRecordIdentifiers(recordIdentifiers);
            }

            if (obj instanceof TitleInfoType) {
                TitleInfoType titleInfoType = (TitleInfoType) obj;
                List<Object> objectList1 = titleInfoType.getElements();
                for (Object o : objectList1) {
                    if (o instanceof Title) {
                        Title title = (Title) o;
                        doc.setTitle(title.getValue());
                    }
                    if (o instanceof BaseTitleInfoType.SubTitle) {
                        BaseTitleInfoType.SubTitle subTitle = (BaseTitleInfoType.SubTitle) o;
                        doc.setSubTitle(subTitle.getValue());
                    }
                }
            }

            if (obj instanceof RelatedItemType) {
                RelatedItem relatedItem = new RelatedItem();

                RelatedItemType relatedItemType = (RelatedItemType) obj;
                relatedItem.setType(relatedItemType.getType());

                List<Object> objectList1 = relatedItemType.getElements();
                for (Object o2 : objectList1) {
                    if (o2 instanceof RecordInfoType) {
                        RecordInfoType recordInfoType = (RecordInfoType) o2;
                        List<RecordIdentifier> recordIdentifiers = this.getRecordIdentifiers(recordInfoType);
                        relatedItem.addRecordIdentifiers(recordIdentifiers);
                        doc.addRelatedItem(relatedItem);
                    }
                }
            }

            if (obj instanceof ClassificationType) {
                Classification classification = new Classification();

                ClassificationType classificationType = (ClassificationType) obj;
                classification.setAuthority(classification.getAuthority());
                classification.setValue(classificationType.getValue());

                doc.addClassifications(classification);

            }
        }

    }


    public void getMetsDocumentAsXML(String docid, HttpServletResponse response) {

//        // find docinfo
//        BasicDBObject field = new BasicDBObject().append("docinfo", 1);
//
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(docid));
//
//        DBObject dbObject = coll.findOne(query, field);
//
//        DocInfo docInfo = new DocInfo(props);
//        docInfo.setFromJSON(dbObject);
//
//        return docInfo.getAsXML().toString("UTF-8");

        OutputStream out = null;

        Mets resultsMets = metsRepo.findOne(docid);

        JAXBContext jaxbctx = null;
        Marshaller m = null;

        try {
            jaxbctx = JAXBContext.newInstance(Mets.class);
            m = jaxbctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File f = new File("/Users/jpanzer/Documents/projects/dev/meta_data_access_service/tmp/out.xml");

            m.marshal(resultsMets, f);
            //  response.flushBuffer();
        } catch (JAXBException e) {
            e.printStackTrace();
        }


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
//    public InputStream getEmeddedFileDocument(String docid, String type) {
//
//        return this.getEmeddedFileDocument(docid, type, null);
//    }

//    /**
//     * Retrieves the related METS or TEI document if available, else null.
//     *
//     * @param docid   The MongoDB id of the related mongoDB object.
//     * @param type    The document type to get ("mets" or "tei").
//     * @param teiType The TEI type ("tei" or "teiType)
//     * @return The METS document as an InputStream or null.
//     */
//    public InputStream getEmeddedFileDocument(String docid, String type, String teiType) {
//
//
//        GridFSDBFile gridFSDBFile = getGridFsDbFile(docid, type, teiType);
//
//        if (gridFSDBFile != null)
//            return gridFSDBFile.getInputStream();
//
//        return null;
////
////        } else {
////
////            // search with alternative pid's
////
////            IdHelper idHelper = new IdHelper();
////            Map<String, String> idMap = idHelper.getPidsFromDB(db, mets_coll_name);
////
////
////
////            if (idHelper.aleadyInDB(this.getM, docid)) {
////
////                List<String> keyValuePair = idHelper.getKeyValuePairFor(idMap, docid);
////                if (keyValuePair != null) {
////                    String objId = idHelper.findDocid(keyValuePair, db, mets_coll_name);
////                    gridFSDBFile = getGridFsDbFile(objId, type, teiType);
////                    if (gridFSDBFile != null)
////                        return gridFSDBFile.getInputStream();
////                }
////            }
////        }
////        return null;
//    }

//    /**
//     * Returns the METS or TEI file object for the given object. This is a helper
//     * for getEmeddedDocument, is creates the query and forwards the request to
//     * mongoDB.
//     *
//     * @param docid   The MongoDB id of the related mongoDB object.
//     * @param type    The object type e.g. "mets", "tei"
//     * @param teiType The TEI type, possibilities are {tei | teiEnriched}.
//     * @return The requested file object.
//     */
//    private GridFSDBFile getGridFsDbFile(String docid, String type, String teiType) {
//
////        GridFS gridFs = new GridFS(db, mets_coll_name);
////        BasicDBObject query;
////
////        if (teiType == null) {
////            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
////                    append("type", type));
////        } else {
////
////            query = new BasicDBObject("metadata", new BasicDBObject("relatedObjId", docid).
////                    append("type", type).
////                    append("teiType", teiType));
////        }
////
////        return gridFs.findOne(query);
//
//        return null;
//    }


//    public String getDocumentTags(String docid) {
//        return null;
//    }
//
//    public String getPageTags(String docid, int pageNumber) {
//        return null;
//    }
//
//
//    public String getFacets(String docid) {
//        return null;
//    }
//
//    public String getDocumentKml(String docid) {
//        return null;
//    }


//    public String isInDB(String pid) {
//
//
////        IdHelper idHelper = new IdHelper();
////        String docid = idHelper.findDocid(pid, db, mets_coll_name);
////        this.mongoClient.close();
////
////        return docid;
//
//        return "";
//    }
//
//
//    public String isFileInDB(String filename) {
//
////        IdHelper idHelper = new IdHelper();
////        String docid = idHelper.checkIfExist(filename, db, mets_coll_name);
////
////        this.mongoClient.close();
////
////        return docid;
//
//        return null;
//    }
//
//    public String convertToUTF8(String s) {
//        String out = null;
//        try {
//            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
//        } catch (java.io.UnsupportedEncodingException e) {
//            return null;
//        }
//        return out;
//    }
//
//    public String convertFromUTF8(String s) {
//        String out = null;
//        try {
//            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
//        } catch (java.io.UnsupportedEncodingException e) {
//            return null;
//        }
//        return out;
//    }
    public void getMetsDocument(String docid, ServletOutputStream outputStream) {

        Mets resultsMets = metsRepo.findOne(docid);

        if (resultsMets != null) {
            JAXBContext jaxbctx = null;
            Marshaller m = null;

            try {
                jaxbctx = JAXBContext.newInstance(Mets.class);
                m = jaxbctx.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

                m.marshal(resultsMets, outputStream);

            } catch (JAXBException e) {
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

        Mets mets = metsRepo.findOne(docid);
        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                metsRepo.removeMods(mods.getID());
            }
        }
    }

    public void removeMets(String docid) {
        Mets mets = metsRepo.findOne(docid);


        List<MdSecType> dmdSecs = mets.getDmdSecs();

        // TODO currently just the first mods element will be examined - is this sufficient?
        for (MdSecType mdSec : dmdSecs) {
            Mods mods = mdSec.getMdWrap().getXmlData().getMods().get(0);

            List<Object> objectList = mods.getElements();
            for (Object obj : objectList) {
                if (obj instanceof RecordInfoType) {

                    List<RecordIdentifier> recordIdentifiers = getRecordIdentifiers((RecordInfoType) obj);
                    if (!recordIdentifiers.isEmpty()) {
                        if (recordIdentifiers.iterator().hasNext()) {
                            RecordIdentifier recordIdentifier = recordIdentifiers.iterator().next();
                            this.removeMetsDocument(new ShortDocInfo(docid, recordIdentifier.getValue()));
                            return;
                        }

                    }
                }
            }

        }
    }

    private List<RecordIdentifier> getRecordIdentifiers(RecordInfoType recordInfoType) {

        List<Object> objectList2 = recordInfoType.getElements();
        List<RecordIdentifier> recordIdentifiers = new ArrayList<>();

        for (Object o2 : objectList2) {
            if (o2 instanceof RecordInfoType.RecordIdentifier) {

                RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) o2;

                String source = recordIdentifier.getSource();
                String value = recordIdentifier.getValue();
                recordIdentifiers.add(new RecordIdentifier(value, source));
            }
        }

        return recordIdentifiers;
    }
}
