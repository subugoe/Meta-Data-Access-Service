package de.unigoettingen.sub.mongomapper.helper;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 01.04.14.
 */
@Service
public class DocidLookupServiceImpl implements DocidLookupService {

    private final String lookupStore = "docids";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addDocid(String purl, String docid) {

        redisTemplate.opsForHash().put(lookupStore, purl, docid);

    }


    @Override
    public String findDocid(String purl) {

        //String key = buildKey(recordIdentifier, source);
        return (String) redisTemplate.opsForHash().get(lookupStore, purl);
    }

    @Override
    public List<String> findAllDocids() {
        List<String> docids = new ArrayList<>();

        for (Object obj : redisTemplate.opsForHash().values(lookupStore)) {
            docids.add((String)obj);
        }

        return docids;
    }

    @Override
    public List<String> findAllKeys() {
        List<String> docids = new ArrayList<>();

        for (Object obj : redisTemplate.opsForHash().keys(lookupStore)) {
            docids.add((String)obj);
        }

        return docids;
    }

    @Override
    public void deleteDocid(String purl) {

        redisTemplate.opsForHash().delete(lookupStore, purl);
    }

    @Override
    public void updateDocid(String purl, String docid) throws IdentifierNotFoundException {

        if (!redisTemplate.opsForHash().hasKey(lookupStore, purl))
            throw new IdentifierNotFoundException("No docid found for identifier: " + purl);

        redisTemplate.opsForHash().put(lookupStore, purl, docid);
    }

    @Override
    public boolean containsDocid(String purl) {

        for (Object obj : redisTemplate.opsForHash().values(lookupStore)) {
            if (purl.equalsIgnoreCase((String)obj))
                return true;
        }
        return false;
    }

//
//    private String buildKey(String recordIdentifier, String source) {
//
//        if (recordIdentifier == null || recordIdentifier.equals(""))
//            return null;
//
//        if (source != null || source != "")
//            return source + ":" + recordIdentifier;
//        else
//            return recordIdentifier;
//
//    }
}
