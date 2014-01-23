package de.unigoettingen.sub.mongomapper.helper.mets;

import au.edu.apsr.mtk.base.*;
import com.mongodb.BasicDBObject;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
public class FileSecHelper {
    private final Logger logger = LoggerFactory.getLogger(FileSecHelper.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    public FileSecHelper() {

    }

    /**
     * Retrieves the elements from the METS fileSec and
     * stores these in instance variables.
     */
    public BasicDBObject handleFileSec(METS mets) {

        BasicDBObject filesec_json = new BasicDBObject();

        try {
            FileSec fileSec = mets.getFileSec();
            if (fileSec != null) {

                basicDBObjectHelper.createBasicDBObject("ID", fileSec.getID(), filesec_json);


                List<FileGrp> fileGrps = fileSec.getFileGrps();

                // TODO should contain at least one element
                List<BasicDBObject> fileGrp_basicDBObject_List = new ArrayList<BasicDBObject>();
                for (FileGrp fileGrp : fileGrps) {
                    fileGrp_basicDBObject_List.add(handleFileGrp(fileGrp));
                }
                filesec_json.append("fileGrp", fileGrp_basicDBObject_List);

            }
        } catch (METSException e) {
            e.printStackTrace();
        }

        return filesec_json;
    }

    /**
     * Retrieves the elements from METS file and return these wrapped
     * as a BasicDBObject.
     * <p/>
     * The child elements FContent, stream, transformFiles of the file
     * element are currently not supported.
     *
     * @param file The file element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     */
    private BasicDBObject handleFile(File file) {

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObjectHelper.createBasicDBObject("ID", file.getID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("SEQ", file.getSeq(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("MIMETYPE", file.getMIMEType(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("SIZE", file.getSize(), basicDBObject);
        // TODO Date not String
        basicDBObjectHelper.createBasicDBObject("CREATED", file.getCreated(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUM", file.getChecksum(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUMTYPE", file.getChecksumType(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("OWNERID", file.getOwnerID(), basicDBObject);
        // TODO is idmid a String?
        basicDBObjectHelper.createBasicDBObject("ADMID", file.getAdmID(), basicDBObject);
        // TODO is dmdid a String?
        basicDBObjectHelper.createBasicDBObject("DMDID", file.getDmdID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("GROUPID", file.getGroupID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("USE", file.getUse(), basicDBObject);


        // embedded FLocate
        try {
            List<FLocat> fLocats = file.getFLocats();
            List<BasicDBObject> fLocat_basicDBObject_list = new ArrayList<BasicDBObject>();
            for (FLocat fLocat : fLocats) {
                BasicDBObject fLocat_basicDBObject1 = new BasicDBObject();
                basicDBObjectHelper.createBasicDBObject("ID", fLocat.getID(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("LOCTYPE", fLocat.getLocType(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("OTHERLOCTYPE", fLocat.getOtherLocType(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("USE", fLocat.getUse(), fLocat_basicDBObject1);

                // xlink
                basicDBObjectHelper.createBasicDBObject("type", fLocat.getType(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("href", fLocat.getHref(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("role", fLocat.getRole(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("arcrole", fLocat.getArcRole(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("title", fLocat.getTitle(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("show", fLocat.getShow(), fLocat_basicDBObject1);
                basicDBObjectHelper.createBasicDBObject("actuate", fLocat.getActuate(), fLocat_basicDBObject1);
                fLocat_basicDBObject_list.add(fLocat_basicDBObject1);
            }
            basicDBObject.append("FLocat", fLocat_basicDBObject_list);

            for (File file1 : file.getFiles())
                basicDBObject.append("file", handleFile(file1));

        } catch (METSException e) {
            e.printStackTrace();
        }
        return basicDBObject;
    }

    /**
     * Retrieves the elements from the METS fileGrp and return these wrapped
     * as a BasicDBObject.
     *
     * @param fileGrp The fileGrp element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     */
    private BasicDBObject handleFileGrp(FileGrp fileGrp) {

        BasicDBObject filegrp_basicDBObject = new BasicDBObject();

        basicDBObjectHelper.createBasicDBObject("ID", fileGrp.getID(), filegrp_basicDBObject);
        // TODO Date not String
        basicDBObjectHelper.createBasicDBObject("VERSDATE", fileGrp.getVersDate(), filegrp_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ADMID", fileGrp.getAdmID(), filegrp_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("USE", fileGrp.getUse(), filegrp_basicDBObject);

        // TODO  ? filegrp(s)  singular or plural
        List<FileGrp> fileGrps = null;
        try {
            fileGrps = fileGrp.getFileGrps();
        } catch (METSException e) {
            logger.error(e.getMessage());
        }
        List<BasicDBObject> fileGrp_basicDBObject_list = new ArrayList<BasicDBObject>();
        if (fileGrps != null) {
            for (FileGrp innerfilegrp : fileGrps) {
                fileGrp_basicDBObject_list.add(handleFileGrp(innerfilegrp));
            }
        }
        if (!fileGrp_basicDBObject_list.isEmpty())
            filegrp_basicDBObject.append("FileGrp", fileGrp_basicDBObject_list);

        List<File> files = null;
        try {
            files = fileGrp.getFiles();
        } catch (METSException e) {
            logger.error(e.getMessage());
        }
        List<BasicDBObject> file_basicDBObject_list = new ArrayList<BasicDBObject>();
        if (files != null) {
            for (File file : files) {
                file_basicDBObject_list.add(handleFile(file));
            }
        }
        if (!file_basicDBObject_list.isEmpty())
            filegrp_basicDBObject.append("file", file_basicDBObject_list);

        return filegrp_basicDBObject;
    }
}