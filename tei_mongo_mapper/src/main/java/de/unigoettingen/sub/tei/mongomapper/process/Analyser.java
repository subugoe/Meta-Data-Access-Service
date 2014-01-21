package de.unigoettingen.sub.tei.mongomapper.process;

import javax.xml.bind.JAXBContext;
import java.io.File;

/**
 * Created by jpanzer on 17.01.14.
 */
public class Analyser {

    private final static String filePath = "/Users/jpanzer/Documents/projects/mongo_mapper/resources/dennis/digizeit/tei/PPN345572572_0053.xml";

    private String coll_name;
    private String db_name;
    private File teiFile;
    private int pageCount;


    public Analyser(String db_name, String coll_name) {
        this.db_name = db_name;
        this.coll_name = coll_name;

    }

    public void parse() {
        if (this.teiFile != null) {


        }
    }

    /**
     * Set the tei file to process.
     *
     * @param teiFile The tei file to process.
     */
    public void setTeiFile(File teiFile) {
        this.teiFile = teiFile;
    }

    /**
     * Returns the current tei file.
     *
     * @return The tei file related to this object.
     */
    public File getTeiFile() {
        return this.teiFile;
    }


    public String getTeiHeader(String docid) {
        return null;
    }

    public String getTEIBody(String docid) {
        return null;
    }

    /**
     * Returns the full text of a single page.
     *
     * @return
     */
    public String getDocumentText(String docid) {
        return null;
    }

    public String getDocumentRepresentation(String docid) {
        return null;
    }


    /**
     * Returns the full text of a single page.
     *
     * @param docid
     * @param pageNumber
     * @return
     */
    public String getPageText(String docid, int pageNumber) {
        return null;
    }


    /**
     * Searchterm based search over all documents in the db.
     *
     * @param docid
     * @param query
     * @return
     */
    public String getDBSearchResults(String docid, String query) {

        return null;

    }


    /**
     * Searchterm based search in a special document.
     *
     * @param docid
     * @param query
     * @return
     */
    public String getDocumentSearchResults(String docid, String query) {

        return null;

    }


    /**
     * Returns a list of tags used in the document.
     *
     * @param docid
     * @return
     */
    public String getDocumentTags(String docid) {
        return null;
    }

    /**
     * Returns a list of tags used on a special page.
     *
     * @param docid
     * @param pageNumber
     * @return
     */
    public String getPageTags(String docid, String pageNumber) {
        return null;
    }

    /**
     * Retrieves a list of possible tags.
     *
     * @param docid
     * @return
     */
    public String getDocumentFacetes(String docid) {
        return null;
    }


    public String getDocumentSearchResults(String docid) {

        return null;
    }

    public String getPageSearchResults(String docid) {
        return null;
    }


}
