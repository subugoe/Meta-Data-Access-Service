package de.unigoettingen.sub.mongomapper.helper.mets;

import au.edu.apsr.mtk.base.Div;
import au.edu.apsr.mtk.base.METS;
import au.edu.apsr.mtk.base.METSException;
import au.edu.apsr.mtk.base.StructMap;
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
public class StructMapHelper {
    private final Logger logger = LoggerFactory.getLogger(StructMapHelper.class);
    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();


    public StructMapHelper() {

    }

    /**
     * Retrieves the elements from the METS structMap.
     *
     * @param mets The mets object contains the mets structures, like metsHdr,
     *             dmdSec, structMap, etc.
     * @return The mets structMap element as a BasicDBObject.
     */
    public List<BasicDBObject> handleStructMap(METS mets) {


        List<BasicDBObject> structmap_json_list = new ArrayList<BasicDBObject>();

        try {

            List<StructMap> structMaps = mets.getStructMaps();
            DivHelper divHelper = new DivHelper();

            // TODO should be at least one struct map, ensure this with an exception
            for (StructMap structMap : structMaps) {

                BasicDBObject structMap_basicDBObject = new BasicDBObject();
                basicDBObjectHelper.createBasicDBObject("ID", structMap.getID(), structMap_basicDBObject);
                basicDBObjectHelper.createBasicDBObject("TYPE", structMap.getType(), structMap_basicDBObject);
                basicDBObjectHelper.createBasicDBObject("LABEL", structMap.getLabel(), structMap_basicDBObject);
                try {

                    // TODO there is just one div allowed at this level
                    Div div = structMap.getDivs().get(0);

                    structMap_basicDBObject.append("div", divHelper.handleDiv(div));

                } catch (METSException e) {
                    e.printStackTrace();
                }
                structmap_json_list.add(structMap_basicDBObject);
            }

        } catch (METSException e) {
            e.printStackTrace();
        }
        return structmap_json_list;
    }
}