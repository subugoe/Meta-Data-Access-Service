package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A collection of {@Doc} elements.
 * Created by jpanzer on 21.03.14.
 */
@XmlType
@XmlRootElement(name = "docs")
public class Docs {

    private List<Doc> docs = null;

    /**
     * Initializes an empty collection of {@Doc} objects. The default constructor is needed for JAXB etc.
     */
    public Docs() {
        super();
    }

    /**
     * Initializes an Docs object which has the given List of {@Docs} as collection.
     * @param docList
     */
    public Docs(List<Doc> docList) {
        super();
        this.docs = docList;
    }

    /**
     * Get all {@Doc} objects from this collection.
     * @return all Doc elements
     */
    @XmlElement(name = "doc")
    public List<Doc> getDocs() {
        return docs;
    }

    /**
     * Set a list of {@Doc} elements as the collection.
     * @param docs The elements for the collection wrapped by the {@Docs} class
     */
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

    /**
     * Adds a list of {@Doc} objects to the already existing collection. If the collection does not exists yet, it is created.
     * @param docs The elements to add.
     */
    public void addDocs(List<Doc> docs) {
        if (docs == null ){
            docs = new ArrayList<>(docs.size());
        }
        this.docs.addAll(docs);
    }
    /**
     * Adds a single {@Doc} object to the already existing collection. If the collection does not exists yet, it is created.
     * @param doc The element to add.
     */
    public void addDoc(Doc doc) {
        if (docs == null ){
            docs = new ArrayList<>();
        }
        this.docs.add(doc);
    }
}
