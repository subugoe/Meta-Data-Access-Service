package de.unigoettingen.sub.mongomapper.helper.mets;

//import au.edu.apsr.mtk.base.*;
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
public class MetsHdrHelper {
    private final Logger logger = LoggerFactory.getLogger(MetsHdrHelper.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    public MetsHdrHelper() {

    }

//    /**
//     * Retrieves the elements from the metsHdr element
//     *
//     * @param mets The mets object contains the mets structures, like metsHdr,
//     *             dmdSec, structMap, etc.
//     * @return The mets header structure as a BasicDBObject.
//     */
//    public BasicDBObject handleMetsHdr(METS mets) {
//
//        BasicDBObject hdr_json = new BasicDBObject();
//
//        MetsHdr metsHdr;
//        try {
//            metsHdr = mets.getMetsHdr();
//
//            if (metsHdr != null) {
//
//                // Properties to store
//                basicDBObjectHelper.createBasicDBObject("ID", metsHdr.getID(), hdr_json);
//                basicDBObjectHelper.createBasicDBObject("ADMID", metsHdr.getAdmID(), hdr_json);
//                basicDBObjectHelper.createBasicDBObject("CREATEDDATE", metsHdr.getCreateDate(), hdr_json);
//                basicDBObjectHelper.createBasicDBObject("LASTMODDATE", metsHdr.getLastModDate(), hdr_json);
//                basicDBObjectHelper.createBasicDBObject("RECORDSTATUS", metsHdr.getRecordStatus(), hdr_json);
//
//                List<Agent> agents = metsHdr.getAgents();
//                List<BasicDBObject> agentlist = new ArrayList<BasicDBObject>();
//
//                for (Agent agent : agents) {
//                    BasicDBObject basicDBObject = new BasicDBObject();
//
//                    basicDBObjectHelper.createBasicDBObject("ID", agent.getID(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("ROLE", agent.getRole(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("OTHERROLE", agent.getOtherRole(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("TYPE", agent.getType(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("OTHERTYPE", agent.getOtherType(), basicDBObject);
//
//                    basicDBObjectHelper.createBasicDBObject("name", agent.getName(), basicDBObject);
//
//                    List<BasicDBObject> note_list = new ArrayList<BasicDBObject>();
//                    for (String note : agent.getNotes()) {
//                        BasicDBObject note_basicDBObject = new BasicDBObject();
//                        basicDBObjectHelper.createBasicDBObject("note", note, note_basicDBObject);
//                        note_list.add(note_basicDBObject);
//                    }
//                    if (!note_list.isEmpty())
//                        basicDBObject.append("note", note_list);
//
//                    agentlist.add(basicDBObject);
//
//                }
//                hdr_json.append("agent", agentlist);
//
//                List<AltRecordID> altRecordIDs = metsHdr.getAltRecordIDs();
//                List<BasicDBObject> altrecordlist = new ArrayList<BasicDBObject>();
//                for (AltRecordID altRecordID : altRecordIDs) {
//                    BasicDBObject basicDBObject = new BasicDBObject();
//
//                    basicDBObjectHelper.createBasicDBObject("ID", altRecordID.getID(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("TYPE", altRecordID.getType(), basicDBObject);
//                    basicDBObjectHelper.createBasicDBObject("VALUE", altRecordID.getValue(), basicDBObject);
//
//                    altrecordlist.add(basicDBObject);
//                }
//                if (!altrecordlist.isEmpty())
//                    hdr_json.append("altRecordID", altrecordlist);
//            }
//        } catch (METSException e) {
//            logger.error(e.getMessage());
//        }
//        return hdr_json;
//    }
}