package de.unigoettingen.sub.mongomapper.helper.tei;

import com.mongodb.BasicDBObject;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 21.01.14.
 */
public class TeiBodyXMLElementHandler extends XMLElementHandler {

    private List<BasicDBObject> pageParagraphBasicDBObjectList = new ArrayList<BasicDBObject>();

    private BasicDBObject pageBasicDBObject = new BasicDBObject();
    private BasicDBObject milestoneBasicDBObject = null;
    private BasicDBObject paragraphBasicDBObject = null;

    private List<BasicDBObject> pageBasicDBObjectList = new ArrayList<BasicDBObject>();


    private Paragraph paragraph = null;

    private int pageCount = 1;

    public TeiBodyXMLElementHandler(List<BasicDBObject> bodyBasicDBObjectLst) {
        super(bodyBasicDBObjectLst);
    }

    @Override
    public List<BasicDBObject> hanldeXMLElement(XMLStreamReader xmlStreamReader) {

        boolean isParagraph = false;

        try {
            for (int event = xmlStreamReader.next();
                 event != XMLStreamConstants.END_DOCUMENT;
                 event = xmlStreamReader.next()) {

                switch (event) {

                    case XMLStreamConstants.START_ELEMENT: {

                        String localName = xmlStreamReader.getLocalName();
                        if (localName.equals("p")) {
                            isParagraph = true;
                            for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                                String key = xmlStreamReader.getAttributeLocalName(i);
                                if (key.equals("id")) {
                                    newParagraph(xmlStreamReader.getAttributeValue(i));
                                }
                            }
                            break;
                        }

                        if (localName.equals("milestone")) {

                            String n = "-1";
                            String type = "";
                            for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                                if (xmlStreamReader.getAttributeLocalName(i).equals("n"))
                                    n = xmlStreamReader.getAttributeValue(i);
                                if (xmlStreamReader.getAttributeLocalName(i).equals("type"))
                                    type = xmlStreamReader.getAttributeValue(i);
                            }
                            if (n != "-1" && !type.equals(""))
                                newMilestone(n, type);
                            break;
                        }

                        if (localName.equalsIgnoreCase("pb")) {
                            newPage();
                            break;
                        }

                        break;
                    }

                    case XMLStreamConstants.CHARACTERS:

                        String text = xmlStreamReader.getText().trim();
                        if (isParagraph && text != null && !text.equals(""))
                            paragraph.addWord(text.trim());

                        break;

                    case XMLStreamConstants.END_ELEMENT:

                        String localName = xmlStreamReader.getLocalName();
                        if (localName.equals("p")) {
                            //this.pageParagraphBasicDBObjectList.add(new BasicDBObject(this.paragraph.getId(), this.paragraph.getWords()));
                            this.pageParagraphBasicDBObjectList.add(paragraph.getParagraphAsBasicDBObject());
                            isParagraph = false;
                            break;
                        }


                        if (localName.equalsIgnoreCase("body")) {
                            return this.pageBasicDBObjectList;
                        }

                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        // should not be reached
        return null;
    }

    private void newPage() {
        this.pageBasicDBObject.append("page", String.valueOf(this.pageCount++));
        this.pageBasicDBObject.append("paragraphs", pageParagraphBasicDBObjectList);
        this.pageBasicDBObjectList.add(this.pageBasicDBObject);
    }

    private void newMilestone(String n, String type) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("n", n);
        basicDBObject.append("type", type);
        this.pageBasicDBObject.append("milestone", basicDBObject);
        pageBasicDBObject = new BasicDBObject();
        pageParagraphBasicDBObjectList = new ArrayList<BasicDBObject>();
    }

    private void newParagraph(String id) {
        this.paragraph = new Paragraph(id);
    }
}