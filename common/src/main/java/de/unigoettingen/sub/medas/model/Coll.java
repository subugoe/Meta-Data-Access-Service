package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 17.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documents", propOrder = {
        "content"
})
@XmlRootElement(name = "document")
public class Coll {


    //@XmlElementWrapper(name = "contents")
    @XmlElement
    private List<Coll.Content> content = new ArrayList<>();

    public List<Coll.Content> getContent() {
        return content;
    }

    public void setContent(List<Coll.Content> content) {
        this.content = content;
    }



    /**
     * Created by jpanzer on 17.03.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "docid", "name", "type", "recordIdentifier"
    })
    public static class Content {

        String docid;
        String name;
        String type;
        String recordIdentifier;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getDocid() {
            return docid;
        }

        public void setDocid(String docid) {
            this.docid = docid;
        }

        public String getRecordIdentifier() {
            return recordIdentifier;
        }

        public void setRecordIdentifier(String recordIdentifier) {
            this.recordIdentifier = recordIdentifier;
        }
    }

}
