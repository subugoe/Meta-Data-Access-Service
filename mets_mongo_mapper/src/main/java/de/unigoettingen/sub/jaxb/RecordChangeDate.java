package de.unigoettingen.sub.jaxb;

import javax.xml.bind.annotation.*;

/**
 * Created by jpanzer on 21.02.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
public class RecordChangeDate {

    @XmlValue
    protected String value;

    @XmlAttribute(name = "encoding")
    protected String encoding;

    @XmlAttribute(name = "qualifier")
    protected String qualifier;

    @XmlAttribute(name = "point")
    protected String point;

    @XmlAttribute(name = "keyDate")
    protected String keyDate;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(String keyDate) {
        this.keyDate = keyDate;
    }
}
