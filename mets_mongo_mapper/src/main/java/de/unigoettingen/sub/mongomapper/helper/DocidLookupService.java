package de.unigoettingen.sub.mongomapper.helper;

import java.util.List;

/**
 * Created by jpanzer on 01.04.14.
 */
public interface DocidLookupService {

    public void addDocid(String purl, String docid);

    public String findDocid(String purl);

    public List<String> findAllDocids();
    public List<String> findAllKeys();

    public void deleteDocid(String purl);

    public void updateDocid(String purl, String docid) throws IdentifierNotFoundException;

    public boolean containsDocid(String docid);
}
