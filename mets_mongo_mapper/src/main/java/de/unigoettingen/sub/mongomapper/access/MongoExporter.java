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
            mets = metsRepo.findOneMets(docid);
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



}
