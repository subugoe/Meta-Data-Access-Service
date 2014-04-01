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
    public void addDocid(String recordIdentifier, String source, String docid) {

        String key = buildKey(recordIdentifier, source);
        redisTemplate.opsForHash().put(lookupStore, key, docid);

    }


    @Override
    public String findDocid(String recordIdentifier, String source) {

        String key = buildKey(recordIdentifier, source);
        return (String) redisTemplate.opsForHash().get(lookupStore, key);
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
    public void deleteDocid(String recordIdentifier, String source) {
        String key = buildKey(recordIdentifier, source);
        redisTemplate.opsForHash().delete(lookupStore, key);
    }

    @Override
    public void updateDocid(String recordIdentifier, String source, String docid) throws IdentifierNotFoundException {

        String key = buildKey(recordIdentifier, source);

        if (!redisTemplate.opsForHash().hasKey(lookupStore, key))
            throw new IdentifierNotFoundException("No docid found for recordIdentifier: " + key);

        redisTemplate.opsForHash().put(lookupStore, key, docid);
    }

    @Override
    public boolean containsDocid(String docid) {

        for (Object obj : redisTemplate.opsForHash().values(lookupStore)) {
            if (docid.equalsIgnoreCase((String)obj))
                return true;
        }
        return false;
    }


    private String buildKey(String recordIdentifier, String source) {

        if (recordIdentifier == null || recordIdentifier.equals(""))
            return null;

        if (source != null || source != "")
            return source + ":" + recordIdentifier;
        else
            return recordIdentifier;

    }
}
