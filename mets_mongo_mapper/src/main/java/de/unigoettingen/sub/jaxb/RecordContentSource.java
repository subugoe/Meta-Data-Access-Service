package de.unigoettingen.sub.jaxb;

import javax.xml.bind.annotation.*;

/**
 * Created by jpanzer on 21.02.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "value"
})
public class RecordContentSource {

    @XmlValue
    protected String value;

    @XmlAttribute(name = "authority")
    protected String authority;

    @XmlAttribute(name = "lang")
    protected String lang;

    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    protected String xmlLang;

    @XmlAttribute(name = "script")
    protected String script;

    @XmlAttribute(name = "transliteration")
    protected String transliteration;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getXmlLang() {
        return xmlLang;
    }

    public void setXmlLang(String xmlLang) {
        this.xmlLang = xmlLang;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }
}
