package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jpanzer on 21.03.14.
 */
@XmlType
@XmlRootElement(name = "docs")
public class Docs {
    private List<Doc> docs = new ArrayList<>();

    public Docs() {
    }

    public Docs(List<Doc> docList) {
        this.docs = docList;
    }

    @XmlElement(name = "doc")
    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    public void addDocs(List<Doc> docs) {
        this.docs.addAll(docs);
    }

    public void addDocs(Doc doc) {
        this.docs.add(doc);
    }
}
