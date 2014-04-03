package de.unigoettingen.sub.mongomapper;


import de.unigoettingen.sub.medas.metsmods.jaxb.*;
import de.unigoettingen.sub.medas.model.Doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
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
public class MongoImporter extends DocService {

    private final Logger logger = LoggerFactory.getLogger(MongoImporter.class);




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

                Map<String, String> ids = this.getIdentifiers(mets);

                String primaryId = getPrimaryId(ids);
                String docid = lookupService.findDocid(primaryId);

                if (docid != null) {
                    if (handling.equals("reject")) {
                        return;
                    }

                    removeMetsDocument(docid);
                    mets.setID(docid);
                }

                saveMods(mets);
                saveMets(mets);

                saveDoc(mets, ids, request);


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




    private void removeMetsDocument(String docid) {


        removeReferencedModsDocuments(docid);
        removeReferencedDocDocuments(docid);
        metsRepo.removeMets(docid);
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


    private Mets saveMets(Mets mets) {

        return metsRepo.saveMets(mets);
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


    @CacheEvict(value = "MedasCache", key = "#p0", allEntries = false)
    private void saveDoc(Mets mets, Map<String, String> ids, HttpServletRequest request) {

        System.out.println("cache test, in saveDoc");

        String primaryId = this.getPrimaryId(ids);
        Doc doc = retrieveBasicDocInfo(mets, primaryId, request);

        lookupService.addDocid(primaryId, mets.getID());

        docRepo.saveDoc(doc);
    }





}
