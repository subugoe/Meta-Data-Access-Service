package de.unigoettingen.sub.mongomapper.helper;

import java.util.List;

/**
 * Created by jpanzer on 01.04.14.
 */
public interface DocidLookupService {

    public void addDocid(String recordIdentifier, String source, String docid);

    public String findDocid(String identifier, String source);

    public List<String> findAllDocids();

    public void deleteDocid(String identifier, String source);

    public void updateDocid(String recordIdentifier, String source, String docid) throws IdentifierNotFoundException;

    public boolean containsDocid(String docid);
}
