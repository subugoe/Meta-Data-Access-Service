package de.unigoettingen.sub.mongomapper.ingest;


import com.sun.org.apache.regexp.internal.recompile;
import de.unigoettingen.sub.medas.metsmods.jaxb.*;
import de.unigoettingen.sub.medas.model.Doc;
import de.unigoettingen.sub.medas.model.RecordIdentifier;
import de.unigoettingen.sub.medas.model.ShortDocInfo;

import de.unigoettingen.sub.mongomapper.helper.DocHelper;
import de.unigoettingen.sub.mongomapper.helper.DocidLookupService;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbDocRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbModsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
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
@Component
public class MongoImporter {

    private final Logger logger = LoggerFactory.getLogger(MongoImporter.class);


    @Autowired
    DocidLookupService lookupService;

    @Autowired
    private DocHelper docHelper;

    @Autowired()
    private MongoDbMetsRepository metsRepo;

    @Autowired()
    private MongoDbModsRepository modsRepo;

    @Autowired()
    private MongoDbDocRepository docRepo;

    public MongoImporter() {
    }


    /**
     * Processes a Mets structure and store these to mongoDB.
     *
     * @param metsFile The METS file to store in mongodb.
     * @param handling If a document is already in the db it will be replaced. The existence test will be performed
     *                 via the (recordInfo) recordIdentifier element. Possibilities values:
     *                 reject:     Rejects the request, the file will not be stored.
     *                 replace:    The new METS file will replace an existing document in mongoDB (default).
     * @param request  The HttpServletRequest object.
     */
    public void storeMetsDocument(MultipartFile metsFile, String handling, HttpServletRequest request) {

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
                saveDoc(mets, request);


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
                modsRepo.saveMods(mods);
            }
        }
    }


    private void saveDoc(Mets mets, HttpServletRequest request) {

        Doc doc = this.docHelper.retrieveBasicDocInfo(mets, request);

        for (RecordIdentifier recId : doc.getRecordIdentifier()) {
            lookupService.addDocid(recId.getValue(), recId.getSource(), recId.getRelatedDocid());
        }

        docRepo.saveDoc(doc);
    }


    /**
     * The method checks if the document is in the DB, and returns the docid and recordIdentifier packed as a
     * ShortDocInfo object or null if not in the db. The test will be performed with the recordIdentifier.
     *
     * @param mets The Mets document to check.
     * @return The docid if already stored, otherwise null.
     */
    private ShortDocInfo checkIfExist(Mets mets) {

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
                                String source = ((RecordInfoType.RecordIdentifier) o).getSource();

                                String docid = lookupService.findDocid(source+":"+recId);
                                if (docid != null)
                                    return new ShortDocInfo(docid, recId, source);
                            }
                        }
                    }

                }
            }
        }

        return null;

    }

}
