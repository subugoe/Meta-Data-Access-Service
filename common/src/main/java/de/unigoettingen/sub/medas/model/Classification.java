package de.unigoettingen.sub.medas.model;

import javax.xml.bind.annotation.*;

/**
 *
 * @author jdo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classification", propOrder = {
        "value"
})
public class Classification {

    @XmlValue
    private String value;

    @XmlAttribute(name = "authority")
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
