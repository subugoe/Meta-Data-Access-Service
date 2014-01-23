package de.unigoettingen.sub.mongomapper.helper.mets;

import au.edu.apsr.mtk.base.MdRef;
import au.edu.apsr.mtk.base.MdWrap;
import com.mongodb.BasicDBObject;
import de.unigoettingen.sub.mongomapper.helper.BasicDBObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
class MdHelper {
    private final Logger logger = LoggerFactory.getLogger(MdHelper.class);

    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();

    public MdHelper() {

    }

    /**
     * Handles the mdWrap elements for handleDmdSec() and  handleAmdSec()
     *
     * @param mdWrap               The MdWrap element.
     * @param mdwrap_basicDBObject The (JSON-like) object in which to store the content.
     */
    void handleMdWrap(MdWrap mdWrap, BasicDBObject mdwrap_basicDBObject) {

        basicDBObjectHelper.createBasicDBObject("ID", mdWrap.getID(), mdwrap_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("MDTYPE", mdWrap.getMDType(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("OTHERMDTYPE", mdWrap.getOtherMDType(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("MDTYPEVERSION", mdWrap.getMDTypeVersion(), mdwrap_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("MIMETYPE", mdWrap.getMIMEType(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("SIZE", mdWrap.getSize(), mdwrap_basicDBObject);
        // TODO Date, not String
        basicDBObjectHelper.createBasicDBObject("CREATED", mdWrap.getCreated(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUM", mdWrap.getChecksum(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUMTYPE", mdWrap.getChecksumType(), mdwrap_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("LABEL", mdWrap.getLabel(), mdwrap_basicDBObject);
        // TODO Obtain base64 encoded data from the binData element. -> getBytes() or toString() ???


        if (mdWrap.getEncodedData() != null)
            basicDBObjectHelper.createBasicDBObject("binData", mdWrap.getEncodedData().getBytes(), mdwrap_basicDBObject);

        // Obtain the xmlData wrapper node
        Node xmldata = mdWrap.getXmlData();
        if (xmldata != null) {

            mdwrap_basicDBObject.append("xmlData", (new XmlDataHelper()).processXmlData(xmldata));
        }
    }

    /**
     * Handles the mdRef elements for handleDmdSec() and handleAmdSec()
     *
     * @param mdRef               The mdRef element.
     * @param mdref_basicDBObject The (JSON-like) object in which to store the content.
     */
    void handleMdRef(MdRef mdRef, BasicDBObject mdref_basicDBObject) {
        basicDBObjectHelper.createBasicDBObject("ID", mdRef.getID(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("LOCTYPE", mdRef.getLocType(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("OTHERLOCTYPE", mdRef.getOtherLocType(), mdref_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("type", mdRef.getType(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("href", mdRef.getHref(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("role", mdRef.getRole(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("arcrole", mdRef.getArcRole(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("title", mdRef.getTitle(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("show", mdRef.getShow(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("actuate", mdRef.getActuate(), mdref_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("MDTYPE", mdRef.getMDType(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("OTHERMDTYPE", mdRef.getOtherMDType(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("MDTYPEVERSION", mdRef.getMDTypeVersion(), mdref_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("MIMETYPE", mdRef.getMIMEType(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("SIZE", mdRef.getSize(), mdref_basicDBObject);
        // TODO Date, not String
        basicDBObjectHelper.createBasicDBObject("CREATED", mdRef.getCreated(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUM", mdRef.getChecksum(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CHECKSUMTYPE", mdRef.getChecksumType(), mdref_basicDBObject);

        basicDBObjectHelper.createBasicDBObject("LABEL", mdRef.getLabel(), mdref_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("XPTR", mdRef.getXptr(), mdref_basicDBObject);
    }
}