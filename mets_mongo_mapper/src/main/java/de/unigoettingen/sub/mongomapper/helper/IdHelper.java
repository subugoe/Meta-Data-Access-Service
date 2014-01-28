package de.unigoettingen.sub.mongomapper.helper;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdHelper {

    private final Logger logger = LoggerFactory.getLogger(IdHelper.class);

    public IdHelper() {
    }


    /**
     * Retrieves all in a special collection existing pids .
     *
     * @param db        The DB object, required for interaction with the db.
     * @param coll_name The collection name, from which to retrieve the pids.
     * @return A map object with pids pairs (pid type, value). The type
     * could be PPN, PURL, etc.
     */
    public Map<String, String> getPidsFromDB(DB db, String coll_name) {

        Map<String, String> documentIDsMap = new HashMap<String, String>();

        DBCollection coll = db.getCollection(coll_name);

        BasicDBObject keys = new BasicDBObject();
        keys.put("ids", 1);
        DBCursor cursor = coll.find(new BasicDBObject(), keys);
        while (cursor.hasNext()) {

            DBObject dbObject = cursor.next();
            for (String key : dbObject.keySet()) {

                if (key.equals("ids")) {

                    // get the possible id's for one object
                    DBObject ids = (DBObject) dbObject.get(key);
                    for (String id : ids.keySet())
                        documentIDsMap.put(id, ids.get(id).toString());
                }
            }
        }

        return documentIDsMap;
    }


    /**
     * Checks if an object is already in mongo. The pids of the document
     * will be compared with the pids in the db.
     *
     * @param idMap     The map with pid's of the object.
     * @param idsFromDB A map with pid's of objects in mongo.
     * @return The pid if found or null.
     */
    public String aleadyInDB(Map<String, String> idMap, Map<String, String> idsFromDB) {

        for (String idValue : idsFromDB.values()) {
            if (aleadyInDB(idMap, idValue))
                return idValue;
        }

        return null;
    }


    /**
     * Helper for {@link #aleadyInDB(java.util.Map, java.util.Map) aleadyInDB}.
     *
     * @param idMap    The map with pid's of the object.
     * @param idFromDB A map with pid's of objects in mongo.
     * @return True if tho document is already in the db, otherwise false.
     */
    public boolean aleadyInDB(Map<String, String> idMap, String idFromDB) {

        return idMap.containsValue(idFromDB);
    }


    /**
     * Returns List of "one pair" of id type (key) and id value.
     *
     * @param idMap    The id Map for the requested object.
     * @param idFromDB The id's in the db.
     * @return A List with one pair (type, value),e.g. (PPN, 4711)
     */
    public List<String> getKeyValuePairFor(Map<String, String> idMap, String idFromDB) {

        for (String key : idMap.keySet()) {
            String value = idMap.get(key);

            if (value.equals(idFromDB)) {

                List<String> keyValuePair = new ArrayList<String>();
                keyValuePair.add(key);
                keyValuePair.add(value);

                return keyValuePair;
            }
        }
        return null;
    }

    /**
     * Search the db for a pair (type, value) and returns the related mongo docid.
     *
     * @param keyValuePair The pair of pid type (e.g. PPN, PURL) and value.
     * @param db           The DB to search in.
     * @param coll_name    The collection to search in.
     * @return The found mongo docid or null.
     */
    public String findDocid(List<String> keyValuePair, DB db, String coll_name) {

        DBCollection coll = db.getCollection(coll_name);

        BasicDBObject keys = new BasicDBObject();
        keys.put(keyValuePair.get(0), keyValuePair.get(1));
        DBCursor cursor = coll.find(new BasicDBObject(), keys);

        if (cursor.size() > 0) {

            return cursor.next().get("_id").toString();
        }

        return null;
    }
}