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
public class StructLinkHelper {

    private final Logger logger = LoggerFactory.getLogger(StructLinkHelper.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    public StructLinkHelper() {
    }

    /**
     * Retrieves the elements from the METS structLink element.
     *
     * @param mets The mets object contains the mets structures, like metsHdr,
     *             dmdSec, structMap, etc.
     * @return The mets structLink element as a BasicDBObject.
     */
    public BasicDBObject handleStructLink(METS mets) {

        BasicDBObject structLink_basicDBObject = new BasicDBObject();

        StructLink structLink = null;
        try {
            structLink = mets.getStructLink();
        } catch (METSException e) {
            logger.error(e.getMessage());
        }

        if (structLink != null) {

            basicDBObjectHelper.createBasicDBObject("ID", structLink.getID(), structLink_basicDBObject);

            // handle smLink-Element TODO should contain at least one element
            List<BasicDBObject> smLink_basicDBObjects_list = new ArrayList<BasicDBObject>();
            List<SmLink> smLinks = null;
            try {
                smLinks = structLink.getSmLinks();
            } catch (METSException e) {
                logger.error(e.getMessage());
            }
            if (smLinks != null) {
                for (SmLink smLink : smLinks) {
                    BasicDBObject smLink_basicDBObject1 = new BasicDBObject();
                    basicDBObjectHelper.createBasicDBObject("ID", smLink.getID(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("arcrole", smLink.getArcRole(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("title", smLink.getTitle(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("show", smLink.getShow(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("actuate", smLink.getActuate(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("from", smLink.getFrom(), smLink_basicDBObject1);
                    basicDBObjectHelper.createBasicDBObject("to", smLink.getTo(), smLink_basicDBObject1);
                    smLink_basicDBObjects_list.add(smLink_basicDBObject1);
                }
            }

            structLink_basicDBObject.append("smLink", smLink_basicDBObjects_list);

            // handle smLinkGrp      TODO should contain at least one element
            List<BasicDBObject> smLinkGrp_basicDBObject_list = new ArrayList<BasicDBObject>();
            List<SmLinkGrp> smLinkGrp_list = null;
            try {
                smLinkGrp_list = structLink.getSmLinkGrps();
            } catch (METSException e) {
                logger.error(e.getMessage());
            }
            if (smLinkGrp_list != null) {
                for (SmLinkGrp smLinkGrp : smLinkGrp_list) {
                    BasicDBObject smLinkGrp_basicDBObject = new BasicDBObject();
                    basicDBObjectHelper.createBasicDBObject("ID", smLinkGrp.getID(), smLinkGrp_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("ARCLINKORDER", smLinkGrp.getArcLinkOrder(), smLinkGrp_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("type", smLinkGrp.getType(), smLinkGrp_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("role", smLinkGrp.getRole(), smLinkGrp_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("title", smLinkGrp.getTitle(), smLinkGrp_basicDBObject);

                    // handle smLocatorLink  TODO should contain at least two elements
                    List<BasicDBObject> smLocatorLink_basicDBObject_list = new ArrayList<BasicDBObject>();
                    List<SmLocatorLink> smLocatorLinks = null;
                    try {
                        smLocatorLinks = smLinkGrp.getSmLocatorLinks();
                    } catch (METSException e) {
                        logger.error(e.getMessage());
                    }
                    if (smLocatorLinks != null) {
                        for (SmLocatorLink smLocatorLink : smLocatorLinks) {
                            BasicDBObject smLocatorLink_basicDBObject = new BasicDBObject();
                            basicDBObjectHelper.createBasicDBObject("ID", smLocatorLink.getID(), smLocatorLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("type", smLocatorLink.getType(), smLocatorLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("href", smLocatorLink.getHref(), smLocatorLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("role", smLocatorLink.getRole(), smLocatorLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("title", smLocatorLink.getTitle(), smLocatorLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("label", smLocatorLink.getXLinkLabel(), smLocatorLink_basicDBObject);
                            smLocatorLink_basicDBObject_list.add(smLocatorLink_basicDBObject);
                        }
                    }
                    // TODO sould contain at least 2 objects
                    smLinkGrp_basicDBObject.append("smLocatorLink", smLocatorLink_basicDBObject_list);

                    // handle smArcLink   TODO should contain at least one ements
                    List<BasicDBObject> smArcLink_basicDBObject_list = new ArrayList<BasicDBObject>();
                    List<SmArcLink> smArcLinks = null;
                    try {
                        smArcLinks = smLinkGrp.getSmArcLinks();
                    } catch (METSException e) {
                        logger.error(e.getMessage());
                    }
                    if (smArcLinks != null) {
                        for (SmArcLink smArcLink : smArcLinks) {
                            BasicDBObject smArcLink_basicDBObject = new BasicDBObject();
                            basicDBObjectHelper.createBasicDBObject("ID", smArcLink.getID(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("type", smArcLink.getArcType(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("arcrole", smArcLink.getArcRole(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("title", smArcLink.getTitle(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("show", smArcLink.getShow(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("actuate", smArcLink.getActuate(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("from", smArcLink.getFrom(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("to", smArcLink.getTo(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("ARCTYPE", smArcLink.getArcType(), smArcLink_basicDBObject);
                            basicDBObjectHelper.createBasicDBObject("ADMID", smArcLink.getAdmID(), smArcLink_basicDBObject);
                            smArcLink_basicDBObject_list.add(smArcLink_basicDBObject);
                        }
                    }
                    smLinkGrp_basicDBObject.append("smArcLink", smArcLink_basicDBObject_list);

                    smLinkGrp_basicDBObject_list.add(smLinkGrp_basicDBObject);
                }
            }
            if (!smLinkGrp_basicDBObject_list.isEmpty())
                structLink_basicDBObject.append("smLinkGrp", smLinkGrp_basicDBObject_list);
        }
        return structLink_basicDBObject;
    }
}