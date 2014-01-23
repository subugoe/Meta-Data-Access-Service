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
public class AmdSecHelper {
    private final Logger logger = LoggerFactory.getLogger(AmdSecHelper.class);

    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    private final List<String> techMDid_list = new ArrayList<String>();
    private final List<String> rightsMDid_list = new ArrayList<String>();
    private final List<String> sourceMDid_list = new ArrayList<String>();
    private final List<String> digiprovMDid_list = new ArrayList<String>();
    private final MdHelper mdHelper;

    /**
     * @param doc      The METS-Dokument as org.w3c.dom Document object.
     * @param importer The TeiMongoImporter instance.
     */
    public AmdSecHelper(Document doc, MongoImporter importer) {

        this.mdHelper = new MdHelper();

        String metsTag = NsHelper.getNsTag(importer.getNsMap(), "mets");

        // techMD IDs
        NodeList techMD_nodes = doc.getElementsByTagName(metsTag + ":techMD");
        for (int i = 0; i < techMD_nodes.getLength(); i++) {
            Node node = techMD_nodes.item(i);
            NamedNodeMap nameNodeMap = node.getAttributes();
            techMDid_list.add(nameNodeMap.getNamedItem("ID").getNodeValue());
        }

        // rightsMD IDs
        NodeList rightsMD_nodes = doc.getElementsByTagName(metsTag + ":rightsMD");
        for (int i = 0; i < rightsMD_nodes.getLength(); i++) {
            Node node = rightsMD_nodes.item(i);
            NamedNodeMap nameNodeMap = node.getAttributes();
            rightsMDid_list.add(nameNodeMap.getNamedItem("ID").getNodeValue());
        }

        // sourceMD IDs
        NodeList sourceMD_nodes = doc.getElementsByTagName(metsTag + ":sourceMD");
        for (int i = 0; i < sourceMD_nodes.getLength(); i++) {
            Node node = sourceMD_nodes.item(i);
            NamedNodeMap nameNodeMap = node.getAttributes();
            sourceMDid_list.add(nameNodeMap.getNamedItem("ID").getNodeValue());
        }

        // digiprovMD IDs
        NodeList digiprovMD_nodes = doc.getElementsByTagName(metsTag + ":digiprovMD");
        for (int i = 0; i < digiprovMD_nodes.getLength(); i++) {
            Node node = digiprovMD_nodes.item(i);
            NamedNodeMap nameNodeMap = node.getAttributes();
            digiprovMDid_list.add(nameNodeMap.getNamedItem("ID").getNodeValue());
        }
    }

    /**
     * Retrieves the elements from the METS amdSec and
     * stores these in instance variables.
     */
    public List<BasicDBObject> handleAmdSec(METS mets) {

        List<BasicDBObject> amdsec_basicDBObject_list = new ArrayList<BasicDBObject>();

        try {

            List<AmdSec> amdSec_list = mets.getAmdSecs();

            if (!amdSec_list.isEmpty()) {

                for (AmdSec amdSec : amdSec_list) {

                    BasicDBObject dmdsec_basicDBObject = new BasicDBObject();

                    basicDBObjectHelper.createBasicDBObject("id", amdSec.getID(), dmdsec_basicDBObject);

                    // process techMD elements
                    List<BasicDBObject> techMD_basicDBObject_list = new ArrayList<BasicDBObject>();
                    for (String techMDid : this.techMDid_list) {

                        TechMD techMD = amdSec.getTechMD(techMDid);
                        BasicDBObject techMD_basicDBObject = new BasicDBObject();
                        handleamdSecMDelements(techMD, techMD_basicDBObject);

                        techMD_basicDBObject_list.add(techMD_basicDBObject);
                    }
                    if (!techMD_basicDBObject_list.isEmpty())
                        dmdsec_basicDBObject.append("techMD", techMD_basicDBObject_list);

                    // process rightsMD elements
                    List<BasicDBObject> rightsMD_basicDBObject_list = new ArrayList<BasicDBObject>();
                    for (String rightsMDid : this.rightsMDid_list) {

                        RightsMD rightsMD = amdSec.getRightsMD(rightsMDid);
                        BasicDBObject rightsMD_basicDBObject = new BasicDBObject();
                        handleamdSecMDelements(rightsMD, rightsMD_basicDBObject);

                        rightsMD_basicDBObject_list.add(rightsMD_basicDBObject);
                    }
                    if (!rightsMD_basicDBObject_list.isEmpty())
                        dmdsec_basicDBObject.append("rightsMD", rightsMD_basicDBObject_list);

                    // process sourceMD elements
                    List<BasicDBObject> sourceMD_basicDBObject_list = new ArrayList<BasicDBObject>();
                    for (String sourceMDid : this.sourceMDid_list) {

                        SourceMD sourceMD = amdSec.getSourceMD(sourceMDid);
                        BasicDBObject sourceMD_basicDBObject = new BasicDBObject();
                        handleamdSecMDelements(sourceMD, sourceMD_basicDBObject);

                        sourceMD_basicDBObject_list.add(sourceMD_basicDBObject);
                    }
                    if (!sourceMD_basicDBObject_list.isEmpty())
                        dmdsec_basicDBObject.append("sourceMD", sourceMD_basicDBObject_list);

                    // process digiprovMD elements
                    List<BasicDBObject> digiprovMD_basicDBObject_list = new ArrayList<BasicDBObject>();
                    for (String digiprovMDid : this.digiprovMDid_list) {

                        DigiprovMD digiprovMD = amdSec.getDigiprovMD(digiprovMDid);
                        BasicDBObject digiprovMD_basicDBObject = new BasicDBObject();
                        handleamdSecMDelements(digiprovMD, digiprovMD_basicDBObject);

                        digiprovMD_basicDBObject_list.add(digiprovMD_basicDBObject);
                    }
                    if (!digiprovMD_basicDBObject_list.isEmpty())
                        dmdsec_basicDBObject.append("digiprovMD", digiprovMD_basicDBObject_list);

                    amdsec_basicDBObject_list.add(dmdsec_basicDBObject);
                }
            }
        } catch (METSException e) {
            logger.error(e.getMessage());
        }
        return amdsec_basicDBObject_list;
    }

    /**
     * Retrieves the elements from the METS mdSec and
     * stores these in instance variables.
     *
     * @param mdSec               The mdSec element to process
     * @param mdSec_basicDBObject The (JSON-like) object in which to store the content.
     * @throws METSException
     */
    private void handleamdSecMDelements(MdSec mdSec, BasicDBObject mdSec_basicDBObject) throws METSException {

        basicDBObjectHelper.createBasicDBObject("ID", mdSec.getID(), mdSec_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("GROUPID", mdSec.getGroupID(), mdSec_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ADMID", mdSec.getAdmID(), mdSec_basicDBObject);
        // TODO Date not String
        basicDBObjectHelper.createBasicDBObject("CREATED", mdSec.getCreated(), mdSec_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("STATUS", mdSec.getStatus(), mdSec_basicDBObject);

        // handle mdRef
        MdRef mdRef = mdSec.getMdRef();
        if (mdRef != null)
            this.mdHelper.handleMdRef(mdRef, mdSec_basicDBObject);

        // handle mdWrap
        MdWrap mdWrap = mdSec.getMdWrap();
        if (mdWrap != null)
            this.mdHelper.handleMdWrap(mdWrap, mdSec_basicDBObject); //, attrMap);
    }
}