package de.unigoettingen.sub.medas.model;

import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author jdo
 */
@XmlType
@XmlRootElement(name="docs")
public class Docs {

//    @XmlElementWrapper(name = "docs")
    
    private Set<Doc> docs;

    @XmlElement(name = "docs")
    public Set<Doc> getDocs() {
        return docs;
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    public void addDocs(Doc doc) {
        this.docs.add(doc);
    }
}
