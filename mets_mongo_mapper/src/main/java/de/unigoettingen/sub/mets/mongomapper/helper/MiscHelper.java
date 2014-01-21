package de.unigoettingen.sub.mets.mongomapper.helper;

import au.edu.apsr.mtk.base.Area;
import au.edu.apsr.mtk.base.METSException;
import au.edu.apsr.mtk.base.Par;
import au.edu.apsr.mtk.base.Seq;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
class MiscHelper {

    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();
    private final Logger logger = LoggerFactory.getLogger(MiscHelper.class);

    public MiscHelper() {

    }

    /**
     * Retrieves the elements from the METS par and return these wrapped
     * as a BasicDBObject.
     *
     * @param par The par element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     * @throws au.edu.apsr.mtk.base.METSException
     */
    BasicDBObject handlePar(Par par) throws METSException {

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObjectHelper.createBasicDBObject("ID", par.getID(), basicDBObject);
        basicDBObject.append("area", handleArea(par.getArea()));
        basicDBObject.append("seq", handleSeq(par.getSeq()));
        return basicDBObject;
    }

    /**
     * Retrieves the elements from the METS area and return these wrapped
     * as a BasicDBObject.
     *
     * @param area The area element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     */
    BasicDBObject handleArea(Area area) {

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObjectHelper.createBasicDBObject("ID", area.getID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("FILEID", area.getFileID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("SHAPE", area.getShape(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("COORDS", area.getCoords(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("BEGIN", area.getBegin(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("END", area.getEnd(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("BETYPE", area.getBEType(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("EXTENT", area.getExtent(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("EXTTYPE", area.getExtType(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ADMID", area.getAdmID(), basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CONTENTIDS", area.getContentIDs(), basicDBObject);
        return basicDBObject;
    }

    /**
     * Retrieves the elements from the METS seq and return these wrapped
     * as a BasicDBObject.
     *
     * @param seq The seq element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     * @throws au.edu.apsr.mtk.base.METSException
     */
    BasicDBObject handleSeq(Seq seq) throws METSException {

        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObjectHelper.createBasicDBObject("ID", seq.getID(), basicDBObject);
        basicDBObject.append("area", handleArea(seq.getArea()));
        basicDBObject.append("par", handlePar(seq.getPar()));
        return basicDBObject;
    }
}