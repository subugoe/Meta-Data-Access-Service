package de.unigoettingen.sub.mongomapper;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jpanzer on 31.03.14.
 */
@Service
public class CacheService {


    @CacheEvict(value = "test", allEntries = true)
    public void deleteAll() {

        System.out.println("in deleteAll");
        return;
    }

    @Cacheable(value = "test", key = "#p0")
    public long addValue(long value) {

        System.out.println("in getValue");
        return value*value;
    }


    @CachePut(value = "test", key = "#p0")
    public long putValue(long value) {

        System.out.println("in putValue");
        return value*value;
    }


    @CacheEvict(value = "test", key = "#p0", allEntries = false)
    public long  delete(long value) {

        System.out.println("in delete");
        return value;
    }



}
