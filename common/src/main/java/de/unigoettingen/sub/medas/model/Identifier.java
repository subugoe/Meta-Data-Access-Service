package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;

/**
 * Created by jpanzer on 25.03.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identifier", propOrder = {
        "value"
})
public class Identifier {

    @XmlValue
    private String value;

    @XmlAttribute(name = "type")
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
