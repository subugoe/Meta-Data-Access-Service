package de.unigoettingen.sub.mets.mongomapper.helper;

import au.edu.apsr.mtk.base.METS;
import com.mongodb.BasicDBObject;
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
public class BehaviorSecHelper {
    private final Logger logger = LoggerFactory.getLogger(BehaviorSecHelper.class);

    public BehaviorSecHelper() {

    }

    /**
     * Retrieves the elements from the METS bahaviorSec and
     * stores these in instance variables.
     * <p/>
     * The behaviorSec element is currently not implemented.
     */
    public List<BasicDBObject> handleBehaviorSec(METS mets) {

        List<BasicDBObject> behavior_json_list = new ArrayList<BasicDBObject>();

        return behavior_json_list;
    }
}