package de.unigoettingen.sub.mongomapper.access;

import com.mongodb.*;
import de.unigoettingen.sub.medas.metsmods.jaxb.*;
import de.unigoettingen.sub.medas.model.*;

import de.unigoettingen.sub.medas.model.ShortDocInfo;
import de.unigoettingen.sub.mongomapper.helper.DocHelper;
import de.unigoettingen.sub.mongomapper.helper.DocidLookupService;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbDocRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbModsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.*;

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
    DocidLookupService lookupService;

    @Autowired
    private MongoDbMetsRepository metsRepo;

    @Autowired
    private MongoDbDocRepository docRepo;

    @Autowired
    private MongoDbModsRepository modsRepo;

    @Autowired
    private DocHelper docHelper;

    @Autowired
    private MongoTemplate mongoTemplate;


//    // TODO we have currently no collection data in the db. to be continued if we get the data.
//    public Docs getCollections(List<String> props, int skip, int limit, HttpServletRequest request) {
//
//        Docs docs = new Docs();
//        List<Mets> metsList = metsRepo.findAllCollections();
//
//        for (Mets mets : metsList) {
//            String metsUrl = this.getUrlString(request) + "/documents/" + mets.getID() + "/mets";
//            docs.addDocuments(retrieveCollInfo(mets, metsUrl));
//        }
//
//        return docs;
//    }

//    public Doc getCollection(String docid, List<String> props, HttpServletRequest request) {
//
//        Mets mets = metsRepo.findOneMets(docid);
//
//        String metsUrl = this.getUrlString(request);
//
//        long start = System.currentTimeMillis();
//        Doc doc = retrieveCollInfo(mets, metsUrl + "/documents/" + mets.getID() + "/mets");
//        System.out.println(System.currentTimeMillis()-start);
//
//        start = System.currentTimeMillis();
//        doc.setContent(retrieveCollContents(mets, metsUrl + "/documents/"));
//        System.out.println(System.currentTimeMillis() - start);
//
//        return doc;
//    }


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

        Docs docs = new Docs(docRepo.findAllDocs());

        return docs;
    }


    public Doc getDocument(String recordIdentifier,
                           List<String> props, HttpServletRequest request) {

        Mets mets = null;

        String docid = lookupService.findDocid(recordIdentifier);

        try {
            long start = System.currentTimeMillis();
            mets = metsRepo.findOneMets(docid);
            System.out.println("findOneMets: " + (System.currentTimeMillis() - start));
        } catch (IllegalArgumentException e) {
            logger.info("The requested docid [" + docid + "] is an invalid ObjectId");
        }

        if (mets == null)
            return null;

        return this.docHelper.retrieveFullDocInfo(mets, request);
    }


    public Doc getDocumentWithRecordIdentifier(String recordIdentifier, List<String> props, HttpServletRequest request) {

        Mods mods = modsRepo.findModsByRecordIdentifier(recordIdentifier);
        Mets mets = metsRepo.findMetsByModsId(mods.getID());

        if (mets == null)
            return null;

        return this.docHelper.retrieveFullDocInfo(mets, request);
    }


//    private Coll retrieveCollInfo(Mets mets, String metsUrlString) {
//
//
//        //List<Doc> docList = new ArrayList<>();
//        List<MdSecType> dmdSecs = mets.getDmdSecs();
//
//
//        // TODO currently just the first mdSec element will be examined - is this sufficient?
//        // TODO currently just the first mods element will be examined - is this sufficient?
//
//        Mods mods = dmdSecs.get(0).getMdWrap().getXmlData().getMods().get(0);
//
//        List<Object> objectList = mods.getElements();
//
//        Coll coll = new Coll();
//
//        // add docid
//        coll.setDocid(mets.getID());
//
//        // add metsURL
//        coll.setMets(metsUrlString);
//
//        for (Object obj : objectList) {
//
//            if (obj instanceof RecordInfoType) {
//
//                // add recordIdentifiers
//                coll.addRecordIdentifiers(getRecordIdentifiers((RecordInfoType) obj));
//            }
//
//            if (obj instanceof TitleInfoType) {
//                TitleInfoType titleInfoType = (TitleInfoType) obj;
//                List<Object> objectList1 = titleInfoType.getElements();
//                for (Object o : objectList1) {
//                    if (o instanceof Title) {
//                        Title title = (Title) o;
//                        // add title
//                        coll.setTitle(title.getValue());
//                    }
//                    if (o instanceof BaseTitleInfoType.SubTitle) {
//                        BaseTitleInfoType.SubTitle subTitle = (BaseTitleInfoType.SubTitle) o;
//                        // add subTitle
//                        coll.setSubTitle(subTitle.getValue());
//                    }
//                }
//            }
//
//            if (obj instanceof ClassificationType) {
//                Classification classification = new Classification();
//
//                ClassificationType classificationType = (ClassificationType) obj;
//                classification.setAuthority(classification.getAuthority());
//                classification.setValue(classificationType.getValue());
//                // add classification
//                coll.addClassifications(classification);
//
//            }
//        }
//
//        return coll;
//    }

//    private List<Coll.Content> retrieveCollContents(Mets mets, String metsUrlString) {
//
//        List<Coll.Content> contents = new ArrayList<>();
//
//        List<StructMapType> structMaps = mets.getStructMaps();
//        for (StructMapType structMap : structMaps) {
//            if (structMap.getTYPE().equalsIgnoreCase("LOGICAL")) {
//                DivType div1 = structMap.getDiv();
//                List<DivType> divType = div1.getDivs();
//
//                for (DivType div2 : divType) {
//
//                    Coll.Content content = new Coll.Content();
//
//                    content.setType(div2.getTYPE());
//                    content.setName(div2.getLABEL());
//
//                    String metsUrl = div2.getMptrs().get(0).getHref();
//                    int i = metsUrl.lastIndexOf("=");
//                    String recordIdentifier = metsUrl.substring(i + 1, metsUrl.length());
//
//                    content.setRecordIdentifier(recordIdentifier);
//
//                    Mods contentMods = metsRepo.findModsByRecordIdentifier(recordIdentifier);
//
//                    if (contentMods != null) {
////                        Mets contentMets = metsRepo.findMetsByModsId(contentMods.getID());
////                        if (contentMets != null) {
//                            //content.setDocid(contentMets.getID());
//                            contents.add(content);
////                        }
//                    } else {
//                        System.out.println("Mods document for recordIdentifier " + recordIdentifier +  " could not found in the DB");
//                        logger.error("Mods document for recordIdentifier " + recordIdentifier +  " could not found in the DB");
//                    }
//                }
//            }
//        }
//
//        return contents;
//    }


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
        removeReferencedDocDocuments(shortDocInfo.getDocid());

        metsRepo.removeMets(shortDocInfo.getDocid());
    }

    private void removeReferencedDocDocuments(String docid) {
        docRepo.findAndRemoveDocForMets(docid);
    }

    private void removeReferencedModsDocuments(String docid) {

        Mets mets = metsRepo.findOneMets(docid);
        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                modsRepo.removeMods(mods.getID());
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

                    List<RecordIdentifier> recordIdentifiers = this.docHelper.getRecordIdentifiers((RecordInfoType) obj, docid);
                    if (!recordIdentifiers.isEmpty()) {
                        if (recordIdentifiers.iterator().hasNext()) {
                            RecordIdentifier recordIdentifier = recordIdentifiers.iterator().next();
                            this.removeMetsDocument(new ShortDocInfo(docid, recordIdentifier.getValue(), recordIdentifier.getSource()));
                            return;
                        }

                    }
                }
            }

        }
    }


    public Mets isDocInDB(String docid) {
        return this.docHelper.isDocInDB(docid);
    }

    public ShortDocInfo isRecordInDB(String recordIdentifier, String source) {
        return this.docHelper.isRecordInDB(recordIdentifier, source);
    }

}
