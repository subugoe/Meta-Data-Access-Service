package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;
import java.util.Set;

/**
 * Created by jpanzer on 04.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordIdentifier", propOrder = {
        "value"
})
public class RecordIdentifier {

    public RecordIdentifier() {
    }

    public RecordIdentifier(Set<String> value, String source) {
        this.value = value;
        this.source = source;
    }

    @XmlValue
    private Set<String> value;

    @XmlAttribute(name = "source")
    private String source;

    public Set<String> getValue() {
        return value;
    }

    public void setValue(Set<String> value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
