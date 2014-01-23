package de.unigoettingen.sub.mongomapper.helper;

import com.mongodb.BasicDBObject;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 * <p/>
 * <p/>
 * The methods of this class help to construct a BasicDBObject (JSON-like) just
 * if the information to wrap is not null or empty.
 */
public class BasicDBObjectHelper {

    /**
     * Instances of BasicDBObject store key-value-pairs like a JSON object
     * {key : value}.
     *
     * @param key           The key or name of the information to store.
     * @param value         The String value.
     * @param basicDBObject The parent BasicDBObject to which append
     *                      the content.
     */
    public void createBasicDBObject(String key, String value, BasicDBObject basicDBObject) {

        if (value != null && !value.equals(""))
            basicDBObject.append(key, value);
    }


    /**
     * Instances of BasicDBObject store key-value-pairs like a JSON object
     * {key : value}.
     *
     * @param key           The key or name of the information to store.
     * @param value         The long value.
     * @param basicDBObject The parent BasicDBObject to which append
     *                      the content.
     */
    public void createBasicDBObject(String key, long value, BasicDBObject basicDBObject) {

        if (value > -1)
            basicDBObject.append(key, value);
    }

    /**
     * Instances of BasicDBObject store key-value-pairs like a JSON object
     * {key : value}.
     *
     * @param key           The key or name of the information to store.
     * @param value         The byte array value.
     * @param basicDBObject The parent BasicDBObject to which append
     *                      the content.
     */
    public void createBasicDBObject(String key, byte[] value, BasicDBObject basicDBObject) {

        if (value.length > 0)
            basicDBObject.append(key, value);
    }
}
