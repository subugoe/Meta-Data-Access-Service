package de.unigoettingen.sub.mongomapper.helper.mets;

import au.edu.apsr.mtk.base.*;
import com.mongodb.BasicDBObject;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import de.unigoettingen.sub.mongomapper.ingest.MongoImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
public class DmdSecHelper {

    private final Logger logger = LoggerFactory.getLogger(DmdSecHelper.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    private final List<String> dmdsec_id_list = new ArrayList<String>();

    public DmdSecHelper(Document doc, MongoImporter importer) {

        String metsTag = NsHelper.getNsTag(importer.getNsMap(), "mets");

        NodeList dmdsec_nodes = doc.getElementsByTagName(metsTag + ":dmdSec");

        for (int i = 0; i < dmdsec_nodes.getLength(); i++) {
            Node node = dmdsec_nodes.item(i);
            NamedNodeMap nameNodeMap = node.getAttributes();
            dmdsec_id_list.add(nameNodeMap.getNamedItem("ID").getNodeValue());
        }
    }

    /**
     * Retrieves the elements from the METS header element and
     * stores these in instance variables.
     */
    public List<BasicDBObject> handleDmdSec(METS mets) {

        List<BasicDBObject> dmdsec_json_list = new ArrayList<BasicDBObject>();
        MdHelper mdHelper = new MdHelper();


        if (!dmdsec_id_list.isEmpty()) {

            for (String dmdid : dmdsec_id_list) {

                BasicDBObject dmdsec_basicDBObject = new BasicDBObject();

                DmdSec dmdSec;
                try {
                    dmdSec = mets.getDmdSec(dmdid);
                } catch (METSException e) {
                    logger.error(e.getMessage());
                    return dmdsec_json_list;
                }

                if (dmdSec != null) {
                    // toplevel attributes
                    basicDBObjectHelper.createBasicDBObject("ID", dmdSec.getID(), dmdsec_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("GROUPID", dmdSec.getGroupID(), dmdsec_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("ADMID", dmdSec.getAdmID(), dmdsec_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("CREATED", dmdSec.getCreated(), dmdsec_basicDBObject);
                    basicDBObjectHelper.createBasicDBObject("STATUS", dmdSec.getStatus(), dmdsec_basicDBObject);

                    BasicDBObject mdref_basicDBObject = new BasicDBObject();
                    MdRef mdRef = null;
                    try {
                        mdRef = dmdSec.getMdRef();
                    } catch (METSException e) {
                        logger.error(e.getMessage());
                    }
                    if (mdRef != null) {
                        mdHelper.handleMdRef(mdRef, mdref_basicDBObject);
                        dmdsec_basicDBObject.append("mdRef", mdref_basicDBObject);
                    }

                    BasicDBObject mdwrap_basicDBObject = new BasicDBObject();
                    MdWrap mdWrap = null;
                    try {
                        mdWrap = dmdSec.getMdWrap();
                    } catch (METSException e) {
                        logger.error(e.getMessage());
                    }
                    if (mdWrap != null) {
                        mdHelper.handleMdWrap(mdWrap, mdwrap_basicDBObject);
                        dmdsec_basicDBObject.append("mdWrap", mdwrap_basicDBObject);
                    }
                }
                dmdsec_json_list.add(dmdsec_basicDBObject);
            }
        }
        return dmdsec_json_list;
    }
}