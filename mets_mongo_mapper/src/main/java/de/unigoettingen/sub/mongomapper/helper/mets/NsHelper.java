package de.unigoettingen.sub.mongomapper.helper.mets;

import java.util.Map;

/**
 * Created by jpanzer on 07.01.14.
 */
public class NsHelper {


    /**
     * Checks if a namespace is declared in the METS document, and which spelling is applied (upper/lower case)
     *
     * @param map The map of namespaces in the mets document.
     * @param tag The requiered namespace tag (METS or mets)
     * @return The tag in correct spelling.
     */
    public static String getNsTag(Map<String, String> map, String tag) {

        if (map.containsKey(tag.toLowerCase()))
            return tag.toLowerCase();
        else if (map.containsKey(tag.toUpperCase()))
            return tag.toUpperCase();

        else return null;
    }

    /**
     * Checks if a namespace is declared in the METS document, and which spelling is applied (upper/lower case)
     *
     * @param map The map of namespaces in the mets document.
     * @param tag The requiered namespace tag (METS or mets)
     * @return The tag in correct spelling.
     */
    public static String getNs(Map<String, String> map, String tag) {

        if (map.containsKey(tag.toLowerCase()))
            return map.get(tag.toLowerCase());
        else if (map.containsKey(tag.toUpperCase()))
            return map.get(tag.toUpperCase());

        else return null;
    }

}