package de.unigoettingen.sub.mongomapper.access;

import com.mongodb.*;
import de.unigoettingen.sub.jaxb.*;
import de.unigoettingen.sub.medas.model.*;

import de.unigoettingen.sub.mongomapper.helper.ShortDocInfo;
import de.unigoettingen.sub.mongomapper.springdata.MetsRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
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


    private DB db = null;
    private DBCollection coll = null;
    private MongoClient mongoClient = null;

    private ApplicationContext context;


    @Autowired
    private MongoDbMetsRepository metsRepo;


    @Autowired
    private MongoTemplate mongoTemplate;


//    public MongoExporter() {
//
//        context = new ClassPathXmlApplicationContext("spring-config.xml");
//        mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
//
//    }

    // TODO we have currently no collection data in the db. to be continued if we get the data.
    public Docs getCollections(List<String> props, int skip, int limit, HttpServletRequest request) {

        Docs docs = new Docs();
//
//        //System.out.println("anzahl Mets: " + mongoTemplate.count(new BasicQuery(new BasicDBObject()), Mets.class));
//
//        List<Mods> modsList = metsRepo.findAllModsWithRelatedItem();
//        // key      -> recordIdentifier
//        // value    -> mods docid
//        HashMap<String, String> ids = new HashMap<String, String>();
//
//        for (Mods mods : modsList) {
//            List<Object> objectList = mods.getElements();
//            for (Object o : objectList) {
//                if (o instanceof RelatedItemType) {
//                    RelatedItemType relatedItemType = (RelatedItemType) o;
//                    String type = relatedItemType.getType();
//                    List<Object> relatedItemTypes = relatedItemType.getElements();
//                    for (Object o1 : relatedItemTypes) {
//                        if (o1 instanceof RecordInfoType) {
//                            RecordInfoType recordInfoType = (RecordInfoType) o1;
//                            List<Object> objectList1 = recordInfoType.getElements();
//                            for (Object o2 : objectList1) {
//                                if (o2 instanceof RecordInfoType.RecordIdentifier) {
//                                    RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) o2;
//                                    recordIdentifier.getSource();
//                                    Set<String> recids = recordIdentifier.getValue();
//                                    for (String id : recids)
//                                        ids.put(id, mods.getID());
//                                }
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//
//
//        //List<String> recIdList = ids.keySet().;
//        // Todo sort
//        //Collections.
//
//        for (String recId : ids.keySet()) {
//            Mods mods = metsRepo.findModsByRecordIdentifier(recId);
//
//
//            if (mods != null) {
//                Mets mets = metsRepo.findMetsByModsId(mods.getID());
//                String metsUrl = this.getUrlString(request)  + "/collections/" + mets.getID();
//                docs.addDocs(retrieveDocInfo(mets, metsUrl));
//            }
//        }


        List<Mets> metsList = metsRepo.findAllCollections(new PageRequest(0, 10));


        for (Mets mets : metsList) {

            String metsUrl = this.getUrlString(request) + "/collections/" + mets.getID();
            docs.addDocs(retrieveDocInfo(mets, metsUrl));
        }


        return docs;

    }


    /**
     * Collects information about the documents in the repository.
     *
     * @param props
     * @param skip    The number of dokuments to skip.
     * @param limit   The number of documents to get.
     * @param request
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
    public Docs getDocuments(List<String> props, int skip, int limit, HttpServletRequest request) {

        List<Mets> metsList = metsRepo.findAllMets();

        Docs docs = new Docs();

        for (Mets mets : metsList) {
            Doc doc = this.getDocument(mets.getID(), props, request);
            docs.addDocs(doc);
        }

        return docs;
    }


    public Doc getDocument(String docid,
                           List<String> props, HttpServletRequest request) {


        Mets mets = metsRepo.findOneMets(docid);

        String metsUrl = this.getUrlString(request) + "/documents/" + mets.getID();

        Doc doc = retrieveDocInfo(mets, metsUrl);


        return doc;
    }


    private String getUrlString(HttpServletRequest request) {

        String schema = request.getScheme();
        String server = request.getServerName();
        int port = request.getServerPort();
        String contextpath = request.getContextPath();

        StringBuffer strb = new StringBuffer();

        if (schema != null)
            strb.append(schema + "://");
        if (server != null)
            strb.append(server);
        if (port > 0)
            strb.append(":" + port);
        if (contextpath != null)
            strb.append(contextpath);


        return strb.toString();
    }

    private Doc retrieveDocInfo(Mets mets, String metsUrlString) {


        //List<Doc> docList = new ArrayList<>();
        List<MdSecType> dmdSecs = mets.getDmdSecs();


        // TODO currently just the first mdSec element will be examined - is this sufficient?
        // TODO currently just the first mods element will be examined - is this sufficient?

        Mods mods = dmdSecs.get(0).getMdWrap().getXmlData().getMods().get(0);

        List<Object> objectList = mods.getElements();

        Doc doc = new Doc();

        // add docid
        doc.setDocid(mets.getID());

        // add metsURL
        doc.setMets(metsUrlString);

        for (Object obj : objectList) {

            if (obj instanceof RecordInfoType) {

                // add recordIdentifiers
                doc.addRecordIdentifiers(getRecordIdentifiers((RecordInfoType) obj));
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
                        relatedItem.addRecordIdentifiers(this.getRecordIdentifiers((RecordInfoType) o2));
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

        // Todo
        // page-count

        return doc;
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

        Mets resultsMets = metsRepo.findOneMets(docid);

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

        Mets mets = metsRepo.findOneMets(docid);
        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                metsRepo.removeMods(mods.getID());
            }
        }
    }

    public void removeMets(String docid) {
        Mets mets = metsRepo.findOneMets(docid);


        List<MdSecType> dmdSecs = mets.getDmdSecs();

        // TODO currently just the first mods element will be examined - is this sufficient?
        for (MdSecType mdSec : dmdSecs) {
            Mods mods = mdSec.getMdWrap().getXmlData().getMods().get(0);

            List<Object> objectList = mods.getElements();
            for (Object obj : objectList) {
                if (obj instanceof RecordInfoType) {

                    Set<RecordIdentifier> recordIdentifiers = getRecordIdentifiers((RecordInfoType) obj);
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

    private Set<RecordIdentifier> getRecordIdentifiers(RecordInfoType recordInfoType) {

        List<Object> objectList2 = recordInfoType.getElements();
        Set<RecordIdentifier> recordIdentifiers = new HashSet<>();

        for (Object o2 : objectList2) {
            if (o2 instanceof RecordInfoType.RecordIdentifier) {

                RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) o2;

                String source = recordIdentifier.getSource();
                Set<String> value = recordIdentifier.getValue();
                recordIdentifiers.add(new RecordIdentifier(value, source));
            }
        }

        return recordIdentifiers;
    }

    private Set<Set<String>> getRecordIdentifier(Mods mods) {
        Set<Set<String>> recordIdentifierList = new HashSet<>();

        List<Object> modsList = mods.getElements();
        for (Object modsElement : modsList) {
            if (modsElement instanceof RecordInfoType) {
                RecordInfoType recordInfoType = (RecordInfoType) modsElement;
                List<Object> recordInfoList = recordInfoType.getElements();
                for (Object recordInfoElement : recordInfoList) {
                    if (recordInfoElement instanceof RecordInfoType.RecordIdentifier) {
                        RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) recordInfoElement;
                        recordIdentifierList.add(recordIdentifier.getValue());
                    }

                }
            }
        }

        return recordIdentifierList;
    }

    public Set<String> getRelatedItemRecordIdentifier(Mods mods) {
        Set<String> recordIdentifierList = new HashSet<>();

        List<Object> modsList = mods.getElements();
        for (Object modsElement : modsList) {

            if (modsElement instanceof RelatedItemType) {

                RelatedItemType relatedItemType = (RelatedItemType) modsElement;

                List<Object> objectList1 = relatedItemType.getElements();
                for (Object o2 : objectList1) {
                    if (o2 instanceof RecordInfoType) {
                        RecordInfoType recordInfoType = (RecordInfoType) o2;
                        List<Object> recordInfoList = recordInfoType.getElements();
                        for (Object recordInfoElement : recordInfoList) {
                            if (recordInfoElement instanceof RecordInfoType.RecordIdentifier) {
                                RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) recordInfoElement;
                                recordIdentifierList.addAll(recordIdentifier.getValue());
                            }

                        }
                    }
                }
            }
        }


        return recordIdentifierList;
    }

    public RecordIdentifier getRcordIdentifier(RelatedItem relatedItem) {

        return null;
    }

    public String isInDB(String recordIdentifier) {

        return metsRepo.findDocidByRecordIdentifier(recordIdentifier);
    }

}
