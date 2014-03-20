package de.unigoettingen.sub.mongomapper;

import de.unigoettingen.sub.medas.model.Coll;
import de.unigoettingen.sub.medas.model.Colls;
import de.unigoettingen.sub.medas.model.Doc;
import de.unigoettingen.sub.medas.model.Docs;
import de.unigoettingen.sub.mongomapper.access.MongoExporter;
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
    private MongoExporter mongoExporter;


    @RequestMapping(value = "/collections", method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
    public
    @ResponseBody
    Colls getCollections(@RequestParam(value = "props", required = false) List<String> props,
                        @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                        @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                        HttpServletRequest request) {

        if (props == null) {
            props = new ArrayList<>();
        }

        long start = System.currentTimeMillis();
        Colls colls = mongoExporter.getCollections(props, skip, limit, request);
        System.out.println(System.currentTimeMillis() - start);

        return colls;
    }

    @RequestMapping(value = "/collections/{docid}", method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
    public
    @ResponseBody
    Coll getCollection(@PathVariable("docid") String docid,
                       @RequestParam(value = "props", required = false) List<String> props,
                       @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                       @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                       HttpServletRequest request) {

        if (props == null) {
            props = new ArrayList<>();
        }

        long start = System.currentTimeMillis();
        Coll coll = mongoExporter.getCollection(docid, props, request);
        System.out.println(System.currentTimeMillis() - start);

        return coll;
    }

    /**
     * Collects information about the documents in the repository.
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
     * Collects information about the documents in the repository.
     * <p/>
     * request: /documents ? props=id & props=...}
     * header:  Accept: application/json, application/xml
     *
     * @param props   Reduce the docinfo to a required infoset. Possible values for
     *                props are:
     *                {id | title | titleShort | mets | preview | tei | teiEnriched | ralatedItems | classifications}
     * @param request The HttpServletRequest object.
     * @return A List of documents with a set of desciptive information, encoded in XML.
     */
    @RequestMapping(value = "/documents/{docid}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8", "application/xml; charset=UTF-8"})
    public
    @ResponseBody
    Doc
    getDocument(@PathVariable("docid") String docid,
                @RequestParam(value = "props", required = false) List<String> props,
                HttpServletRequest request) {

        if (props == null) {
            props = new ArrayList<>();
        }

        long start = System.currentTimeMillis();
        Doc doc = mongoExporter.getDocument(docid, props, request);
        System.out.println(System.currentTimeMillis() - start);

        return doc;
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


//    /**
//     * Returns the full text of a single page.
//     *
//     * @param docid  The MongoDB id of the related mongoDB object, or any PID.
//     * @param pageno The pagenumber of the requested page.
//     * @param model  The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A special representation (e.g. PDF, HTML) for the requested page.
//     */
//    @RequestMapping(value = "/documents/{docid}/text/{pageno}", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getDocumentPageText(@PathVariable("docid") String docid,
//                               @PathVariable("pageno") String pageno, Model model) {
//
//        return mongoExporter.getDocumentRepresentation(docid, pageno);
//    }

//    /**
//     * Returns a page representation of a document.
//     *
//     * @param docid The MongoDB id of the related mongoDB object, or any PID.
//     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A special representation (e.g. PDF, HTML) for the requested document.
//     */
//    @RequestMapping(value = "/documents/{docid}/text", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getDocumentPage(@PathVariable("docid") String docid, Model model) {
//
//        return mongoExporter.getDocumentRepresentation(docid);
//    }


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


//    /**
//     * Returns the related TEI document.
//     * <p/>
//     * request: /documents/{docid}/tei?type={tei | enriched}
//     *
//     * @param docid    The MongoDB id of the related mongoDB object, or any PID.
//     * @param teiType  The TEI type, possibillities are tei (default) or teiEnriched.
//     * @param response The HttpServletResponse object.
//     */
//    @RequestMapping(value = "/documents/{docid}/tei", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    void getDocumentTEI(@PathVariable("docid") String docid,
//                        @RequestParam(value = "teiType", defaultValue = "tei") String teiType,
//                        HttpServletResponse response) {
//
//        response.setContentType("application/xml");
//
//        InputStream in = mongoExporter.getEmeddedFileDocument(docid, "tei", teiType);
//
//        try {
//            if (in != null) {
//                FileCopyUtils.copy(in, response.getOutputStream());
//                response.flushBuffer();
//            } else {
//                // TODO better return required, e.g. a redirect to an error page
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Requested tei document is not available for Id: " + docid);
//            }
//        } catch (IOException e) {
//            logger.error("Error on writing tei file to the output stream.");
//            //throw new RuntimeException("IOError on writing mets file to the output stream.");
//        }
//    }


//    /**
//     * Retrieves a list of values for a special tag used in the document, e.g. tei:persName, tei:placeName
//     *
//     * @param docid The MongoDB id of the related mongoDB object, or any PID.
//     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A document with special tag info.
//     */
//    @RequestMapping(value = "/documents/{docid}/tags", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getDocumentTags(@PathVariable("docid") String docid, Model model) {
//
//        return mongoExporter.getDocumentTags(docid);
//    }

//    /**
//     * Retrieves a list of values for a special tag used on a page, e.g. tei:persName, tei:placeName
//     *
//     * @param docid      The MongoDB id of the related mongoDB object, or any PID.
//     * @param pageNumber The page number of the for which to get the tags.
//     * @param model      The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A document with special tag info.
//     */
//    @RequestMapping(value = "/documents/{docid}/tags/{pageNumber}", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getDocumentTags(@PathVariable("docid") String docid, @PathVariable("pageNumber") int pageNumber, Model model) {
//
//        return mongoExporter.getPageTags(docid, pageNumber);
//    }

//    /**
//     * Retrieves a list of possible tags.
//     *
//     * @param docid The MongoDB id of the related mongoDB object, or any PID.
//     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A document with tag info.
//     */
//    @RequestMapping(value = "/documents/{docid}/facets", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getFacets(@PathVariable("docid") String docid, Model model) {
//
//        return mongoExporter.getFacets(docid);
//    }

//    /**
//     * Returns the KML for places, contained in the document (KML: geographic annotation).
//     *
//     * @param docid The MongoDB id of the related mongoDB object, or any PID.
//     * @param model The Spring-Model objekt, required for transmission of parameters within the request scope.
//     * @return A document with KML info.
//     */
//    @RequestMapping(value = "/documents/{docid}/kml", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String getDocumentKml(@PathVariable("docid") String docid, Model model) {
//
//        return mongoExporter.getDocumentKml(docid);
//    }

    /**
     * Checks, if an object with the given pid is already in the db.
     *
     * @param pid   The pid of the document to search.
     * @param model
     * @return The docid of the document or null if it doesn't exist.
     */
    @RequestMapping(value = "/documents/{pid}/exist", method = RequestMethod.GET)
    public
    @ResponseBody
    String isPidInDB(@PathVariable("pid") String pid, Model model) {


        String docid = mongoExporter.isInDB(pid);

        if (docid == null) {
            System.out.println("null");
            return null;
        } else {
            System.out.println(docid);
            return docid;
        }
    }


//    /**
//     * Checks, if an METS file is already in the db.
//     *
//     * @param filename The name of the file for which to check.
//     * @param model
//     * @return The docid of the document or null if it doesn't exist.
//     */
//    @RequestMapping(value = "/documents/{filename}/exist", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    String isFileInDB(@PathVariable("filename") String filename, Model model) {
//
//        String docid = mongoExporter.isFileInDB(filename);
//
//        if (docid == null) {
//            System.out.println("docid==null für -> " + filename);
//            writeFileNameToFile(filename);
//            return "";
//        }
//       // else
//       //     return docid;
//        return "";
//    }

//    private void writeFileNameToFile(String filename) {
//
//        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/Users/jpanzer/Documents/data/digizeit/out.txt", true)))) {
//            out.println(filename);
//        }catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }
//
//
//    }


//    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
//    public ModelAndView handleException(NoSuchRequestHandlingMethodException ex) {
//        ModelAndView mav = new ModelAndView();
//        logger.error("Exception found: " + ex);
//        return mav;
//    }

}
