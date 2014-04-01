package de.unigoettingen.sub.mongomapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jpanzer on 31.03.14.
 */
@Controller
public class TestRedis {

    private static String str;

    @Autowired
    CacheService cacheService;


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public
    @ResponseBody
    void deleteAll() {

        cacheService.deleteAll();
        return;
    }


    @RequestMapping(value = "/add/{value}", method = RequestMethod.GET)
    public
    @ResponseBody
    long addValue(@PathVariable("value") long value) {


        return cacheService.addValue(value);
    }



    @RequestMapping(value = "/put/{value}", method = RequestMethod.GET)
    public
    @ResponseBody
    long putValue(@PathVariable("value") long value) {

        //cacheService.putValue(value);
        return cacheService.putValue(value);
    }


    @RequestMapping(value = "/delete/{value}", method = RequestMethod.GET)
    public
    @ResponseBody
    long  delete(@PathVariable("value") long value) {

        //cacheService.delete(value);
        return cacheService.delete(value);
    }




//    @CacheEvict(value = "test", allEntries = true)
//    @RequestMapping(value = "/delete", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    void deleteAll() {
//
//        System.out.println("in deleteAll");
//        return;
//    }
//
//    @Cacheable(value = "test", key = "#p0")
//    @RequestMapping(value = "/add/{value}", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getValue(@PathVariable("value") String value) {
//
//        System.out.println("in getValue");
//        return value;
//    }
//
//
//    @CachePut(value = "test", key = "#p0")
//    @RequestMapping(value = "/put/{value}", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String putValue(@PathVariable("value") String value) {
//
//        System.out.println("in putValue");
//        return value;
//    }
//
//
//    @CacheEvict(value = "test", key = "#p0", allEntries = false)
//    @RequestMapping(value = "/delete/{value}", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String  delete(@PathVariable("value") String value) {
//
//        System.out.println("in delete");
//        return value;
//    }


}
