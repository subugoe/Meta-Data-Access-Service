package de.unigoettingen.sub.mongomapper;

import de.unigoettingen.sub.mets.mongomapper.ingest.MongoImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
@Controller
public class IngestController {

    private final Logger logger = LoggerFactory.getLogger(IngestController.class);

    @Autowired
    private MongoImporter mongoImporter;

    public IngestController() {
    }

    /**
     * Takes the given METS file and processes the migration to mongodb
     * <p/>
     * Request:     /documents/ingest?handling={reject|replace}
     *
     * @param file     The METS file to store in mongodb.
     * @param handling Specifies what to do if a METS file with the same PID already
     *                 exist. Possibilities are:
     *                 reject:     Rejects the request, the file will not be stored.
     *                 replace:    The new METS file will replace an existing one in mongoDB (default).
     * @param request  The HttpServletRequest object.
     */
    @RequestMapping(value = "/documents/ingest", method = RequestMethod.POST)
    public
    @ResponseBody
    void ingestMetsDoc(@RequestParam("file") MultipartFile file,
                       @RequestParam(value = "handling", defaultValue = "replace") String handling,
                       HttpServletRequest request) {

        long start = System.currentTimeMillis();
        mongoImporter.processMetsAndStore(file, handling, this.getUrlString(request));
        System.out.println(System.currentTimeMillis() - start + " millisec");

    }


    /**
     * Takes the given TEI file and stores it in relation to the document with docid.
     * A repeated ingest will replace the current TEI or enriched TEI file.
     * <p/>
     * Request: /documents/{docid}/ingest/{teiType}
     *
     * @param file    The TEI file to store in mongodb.
     * @param docid   The MongoDB id of the related mongoDB object, or any PID.
     * @param request The HttpServletRequest object.
     */
    @RequestMapping(value = "/documents/{docid}/ingest/tei", method = RequestMethod.POST)
    public
    @ResponseBody
    void ingestTeiDoc(@RequestParam("file") MultipartFile file,
                      @PathVariable("docid") String docid,
                      HttpServletRequest request) {

        long start = System.currentTimeMillis();
        mongoImporter.processTeiAndStore(file, docid, "tei", "tei", this.getUrlString(request));
        System.out.println(System.currentTimeMillis() - start + " millisec");
    }

    /**
     * Takes the given enriched TEI file and stores it in relation to the document with docid.
     * A repeated ingest will replace the current TEI or enriched TEI file.
     * <p/>
     * Request: /documents/{docid}/ingest/{teiType}
     *
     * @param file    The enriched TEI file to store in mongodb.
     * @param docid   The MongoDB id of the related mongoDB object, or any PID.
     * @param request The HttpServletRequest object.
     */
    @RequestMapping(value = "/documents/{docid}/ingest/teiEnriched", method = RequestMethod.POST)
    public
    @ResponseBody
    void ingestTeiEnrichedDoc(@RequestParam("file") MultipartFile file,
                              @PathVariable("docid") String docid,
                              HttpServletRequest request) {

        long start = System.currentTimeMillis();
        mongoImporter.processTeiAndStore(file, docid, "tei", "teiEnriched", this.getUrlString(request));
        System.out.println(System.currentTimeMillis() - start + " millisec");
    }


    public String getUrlString(HttpServletRequest request) {

        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
