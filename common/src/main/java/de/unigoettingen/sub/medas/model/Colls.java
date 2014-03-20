package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jpanzer on 17.03.14.
 */
@XmlType
@XmlRootElement(name="colls")
public class Colls {

    private Set<Coll> colls = new HashSet<>();

    @XmlElement(name = "coll")
    public Set<Coll> getColls() {
        return colls;
    }

    public void setColls(Set<Coll> colls) {
        this.colls = colls;
    }

    public void addColls(Coll coll) {
        this.colls.add(coll);
    }
}
