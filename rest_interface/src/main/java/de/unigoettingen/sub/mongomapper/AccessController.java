package de.unigoettingen.sub.mongomapper;

import de.unigoettingen.sub.medas.metsmods.jaxb.Mets;
import de.unigoettingen.sub.medas.model.*;
import de.unigoettingen.sub.mongomapper.access.MongoExporter;
import de.unigoettingen.sub.mongomapper.helper.DocidLookupService;
import de.unigoettingen.sub.mongomapper.helper.IdentifierNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
@Controller
@Scope(value = "request")
public class AccessController {

    private final Logger logger = LoggerFactory.getLogger(AccessController.class);

    @Autowired
    private DocidLookupService lookupService;

    @Autowired
    private MongoExporter mongoExporter;

//    /**
//     * Collects a list of collections and returns a basic set of information (docid, recordInfo -> recordIdentifier,
//     * title, subTitle, classifications, mets-url).
//     *
//     * @param props
//     * @param skip
//     * @param limit
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/collections", method = RequestMethod.GET,
//            produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
//    public
//    @ResponseBody
//    Docs getCollections(@RequestParam(value = "props", required = false) List<String> props,
//                        @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
//                        @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
//                        HttpServletRequest request) {
//
//        if (props == null) {
//            props = new ArrayList<>();
//        }
//
//        long start = System.currentTimeMillis();
//        Docs docs = mongoExporter.getCollections(props, skip, limit, request);
//        System.out.println(System.currentTimeMillis() - start);
//
//        return docs;
//    }

//    /**
//     * Collects the information for a special collection and returns a basic set of information (docid, recordInfo -> recordIdentifier,
//     * title, subTitle, classifications, mets-url) and additional information about the content of this collection (docid, name, type,
//     * recordIdentifier).
//     *
//     * @param docid
//     * @param props
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/collections/{docid}", method = RequestMethod.GET,
//            produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
//    public
//    @ResponseBody
//    Doc getCollection(@PathVariable("docid") String docid,
//                       @RequestParam(value = "props", required = false) List<String> props,
//                       HttpServletRequest request) {
//
//        if (props == null) {
//            props = new ArrayList<>();
//        }
//
//        long start = System.currentTimeMillis();
//        Doc doc = mongoExporter.getCollection(docid, props, request);
//        System.out.println(System.currentTimeMillis() - start);
//
//        return doc;
//    }

    /**
     * Collects information about the documents in the repository and returns a basic set of information (docid,
     * recordInfo -> recordIdentifier, title, subTitle, classifications, mets-url).
     * <p/>
     * <p/>
     * request: /documents ? props=id & props=...}
     * header:  Accept: application/xml
     *
     * @param props   Reduce the docinfo to a required infoset. Possible values for
     *                props are:
     *                {id | title | titleShort | mets | preview | tei | teiEnriched | ralatedItems | classifications}
     * @param skip    The number of documents to skip (default = 0).
     * @param limit   The number of documents to get (default = 25).
     * @param request The HttpServletRequest object.
     * @return A List of documents with a set of desciptive information, encoded in XML.
     */
    @RequestMapping(value = "/documents", method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
    public
    @ResponseBody
    Docs getDocuments(@RequestParam(value = "props", required = false) List<String> props,
                      @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                      @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                      HttpServletRequest request) {

        if (props == null) {
            props = new ArrayList<>();
        }

        long start = System.currentTimeMillis();
        Docs docs = mongoExporter.getDocuments(props, skip, limit, request);
        System.out.println(System.currentTimeMillis() - start);

        return docs;
    }


    /**
     * Collects the information for a special document in the repository and returns a basic set of information
     * (docid, recordInfo -> recordIdentifier, title, subTitle, classifications, mets-url, relatedItem ->
     * recordInfo -> recordIdentifier) and additional information about the content of this document (docid, name,
     * type, recordIdentifier).
     * <p/>
     * <p/>
     * request: /documents/{recordIdentifier} ? props=id & props=...}
     * header:  Accept: application/json, application/xml
     *
     * @param props   Reduce the docinfo to a required infoset. Possible values for
     *                props are:
     *                {id | title | titleShort | mets | preview | tei | teiEnriched | ralatedItems | classifications}
     * @param request The HttpServletRequest object.
     * @return A List of documents with a set of desciptive information, encoded in XML.
     */
    @RequestMapping(value = "/documents/{recordIdentifier}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
    public
    @ResponseBody
    Doc
    getDocument(@PathVariable("recordIdentifier") String recordIdentifier,
                @RequestParam(value = "props", required = false) List<String> props,
                HttpServletRequest request) {

        if (props == null) {
            props = new ArrayList<>();
        }

        long start = System.currentTimeMillis();
        Doc document = mongoExporter.getDocument(recordIdentifier, props, request);
        System.out.println(System.currentTimeMillis() - start);

        return document;
    }


    /**
     * Returns an  outline of a document.
     *
     * @param docid The MongoDB id of the related mongoDB object, or any PID.
     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
     * @return A document with outline info.
     */
    @RequestMapping(value = "/documents/{docid}/outline", method = RequestMethod.GET)
    public
    @ResponseBody
    String getDocumentOutline(@PathVariable("docid") String docid, Model model) {

        return mongoExporter.getDocumentOutline(docid);
    }



    /**
     * Searchterm based search in a special document.
     * <p/>
     * request: /documents/{docid}/search?query=...
     *
     * @param docid The MongoDB id of the related mongoDB object, or any PID.
     * @param query The string based searchterm.
     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
     * @return A list of pages which match the query.
     */
    @RequestMapping(value = "/documents/{docid}/search", method = RequestMethod.GET)
    public
    @ResponseBody
    String getDocumentSearchResults(@PathVariable("docid") String docid,
                                    @RequestParam(value = "query", defaultValue = "") String query, Model model) {

        // TODO with higlightning or code snipped ?

        return mongoExporter.getSearchResults(docid, query);

    }

    /**
     * Searchterm based search over all documents in the db.
     * <p/>
     * request: /documents/search?query=...
     *
     * @param query The searchterm. The requst will be forwarded to mongoDB to search
     *              within the metadata elements.
     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
     * @return A list of documents which match the query.
     */
    @RequestMapping(value = "/documents/search", method = RequestMethod.GET)
    public
    @ResponseBody
    String getDBSearchResults(@RequestParam(value = "query", defaultValue = "") String query,
                              Model model) {

        // TODO with higlightning or code snipped ?

        return mongoExporter.getSearchResults(query);
    }

    /**
     * Accepts OAI2 metadate requests. Metadata is extracted from the object metadata
     * Info is extracted http://www.openarchives.org/OAI/openarchivesprotocol.html#ProtocolMessages
     * <p/>
     * /documents/oai2/verb=..&...
     *
     * @param query Accepts one of the six oai verbs with related arguments.
     *              verb=GetRecord is used to get metadata for the object with the given id
     *              Args:   identifier      - object identifier
     *              metadataPrefix  - oai_dc or oai_mets
     *              <p/>
     *              verb=Identify is used to get information about the repository
     *              Args:   -
     *              <p/>
     *              verb=ListIdentifiers is used to retrieve just the headers of records
     *              Args:   from            - optional, defines a timestamp to restrict results
     *              until           - like from, but an upper boundary
     *              metadataPrefix  - oai_dc or oai_mets
     *              set             - optional, defines criteria to restrict the request
     *              resumptionToken  - argument to  continue a previous requiest
     *              <p/>
     *              verb=ListMetadataFormats is used to retrieve the supported metadata formats info
     *              Args:   identifier      - optional, to get the supported formats for the given object (id)
     *              <p/>
     *              verb=ListRecords is used to get the metadata for all objects (see GetRecord)
     *              Args:   same as for ListIdentifiers
     *              <p/>
     *              verb=ListSets is used to retrieve the set structure
     *              Args:   resumptionToken - see ListIdentifiers
     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
     * @return A document with OAI conform results.
     */
    @RequestMapping(value = "/documents/oai2/{query}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getPmhResults(@PathVariable("query") String query, Model model) {

        return mongoExporter.getOai2Results(query);
    }

    /**
     * Returns the METS document.
     * <p/>
     * request: /documents/{docid}/mets
     *
     * @param docid    The MongoDB id of the related mongoDB object.
     * @param response The HttpServletResponse object.
     */
    @RequestMapping(value = "/documents/{docid}/mets", method = RequestMethod.GET)
    public
    @ResponseBody
    void getDocumentMets(@PathVariable("docid") String docid,
                         HttpServletResponse response) {

        response.setContentType("application/xml");

        try {
            mongoExporter.getMetsDocument(docid, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // TODO just via an admn account !!!

    /**
     * Returns the METS document.
     * <p/>
     * request: /documents/{docid}/mets
     *
     * @param docid    The MongoDB id of the related mongoDB object.
     * @param response The HttpServletResponse object.
     */
    @RequestMapping(value = "/documents/{docid}/remove", method = RequestMethod.GET)
    public
    @ResponseBody
    void getRemoveMets(@PathVariable("docid") String docid,
                       HttpServletResponse response) {

        mongoExporter.removeMets(docid);
    }



    /**
     * Checks, if an object with the given pid is already in the db.
     *
     * @param docid The pid of the document to search.
     * @param model
     * @return The docid of the document or null if it doesn't exist.
     */
    @RequestMapping(value = "/documents/{docid}/exist", method = RequestMethod.GET)
    public
    @ResponseBody
    String isDocInDB(@PathVariable("docid") String docid, Model model) {


        Mets mets = mongoExporter.isDocInDB(docid);

        if (mets == null) {
            return null;
        } else {
            return mets.getID();
        }
    }



    @RequestMapping(value = "/documents/all", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String>
    getAllTest() {

        return lookupService.findAllDocids();
    }



    @RequestMapping(value = "/documents/get/{recId}", method = RequestMethod.GET)
    public
    @ResponseBody
    String
    getTest(@PathVariable("recId") String recId) {

        return recId + lookupService.findDocid(recId);

    }

}
