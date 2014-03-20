package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jpanzer on 17.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coll", propOrder = {
        "content"
})
@XmlRootElement(name = "coll")
public class Coll extends Document {


    //@XmlElementWrapper(name = "contents")
    @XmlElement
    private List<Content> content = new ArrayList<>();

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }
}
