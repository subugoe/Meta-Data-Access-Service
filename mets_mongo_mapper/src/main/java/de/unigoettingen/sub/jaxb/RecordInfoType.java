//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.24 at 04:05:56 PM CET 
//


package de.unigoettingen.sub.jaxb;

import com.google.code.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for recordInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recordInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="recordContentSource" type="{http://www.loc.gov/mods/v3}sourceType"/>
 *         &lt;element name="recordCreationDate" type="{http://www.loc.gov/mods/v3}dateType"/>
 *         &lt;element name="recordChangeDate" type="{http://www.loc.gov/mods/v3}dateType"/>
 *         &lt;element name="recordIdentifier">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="languageOfCataloging" type="{http://www.loc.gov/mods/v3}languageType"/>
 *         &lt;element name="recordOrigin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *       &lt;attGroup ref="{http://www.loc.gov/mods/v3}language"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordInfoType", propOrder = {
    "elements"
})
@XmlRootElement(name = "recordInfo")
public class RecordInfoType {

    @XmlElements({
        @XmlElement(name = "recordChangeDate", namespace = "http://www.loc.gov/mods/v3", type = RecordChangeDate.class, required = false),
        @XmlElement(name = "languageOfCataloging", namespace = "http://www.loc.gov/mods/v3", type = LanguageOfCataloging.class, required = false),
        @XmlElement(name = "recordCreationDate", namespace = "http://www.loc.gov/mods/v3", type = RecordCreationDate.class, required = false),
        @XmlElement(name = "recordOrigin", namespace = "http://www.loc.gov/mods/v3", type = RecordOrigin.class, required = false),
        @XmlElement(name = "recordContentSource", namespace = "http://www.loc.gov/mods/v3", type = RecordContentSource.class, required = false),
        @XmlElement(name = "recordIdentifier", namespace = "http://www.loc.gov/mods/v3", type = RecordIdentifier.class, required = false)
    })
    protected List<Object> elements;

    @XmlAttribute(name = "lang")
    protected String lang;
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    protected String xmlLang;
    @XmlAttribute(name = "script")
    protected String script;
    @XmlAttribute(name = "transliteration")
    protected String transliteration;

    /**
     * Gets the value of the recordContentSourcesAndRecordCreationDatesAndRecordChangeDates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recordContentSourcesAndRecordCreationDatesAndRecordChangeDates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecordContentSourcesAndRecordCreationDatesAndRecordChangeDates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link RecordInfoType.RecordIdentifier }{@code >}
     * {@link JAXBElement }{@code <}{@link DateType }{@code >}
     * {@link JAXBElement }{@code <}{@link LanguageType }{@code >}
     * {@link JAXBElement }{@code <}{@link DateType }{@code >}
     * {@link JAXBElement }{@code <}{@link SourceType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<Object> getElements() {
        if (elements == null) {
            elements = new ArrayList<Object>();
        }
        return this.elements;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the xmlLang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlLang() {
        return xmlLang;
    }

    /**
     * Sets the value of the xmlLang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlLang(String value) {
        this.xmlLang = value;
    }

    /**
     * Gets the value of the script property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets the value of the script property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScript(String value) {
        this.script = value;
    }

    /**
     * Gets the value of the transliteration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransliteration() {
        return transliteration;
    }

    /**
     * Sets the value of the transliteration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransliteration(String value) {
        this.transliteration = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class RecordIdentifier {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "source")
        protected String source;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the source property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSource() {
            return source;
        }

        /**
         * Sets the value of the source property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSource(String value) {
            this.source = value;
        }

    }

    /**
     * Created by jpanzer on 21.02.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "recordChangeDate", propOrder = {
            "value"
    })
    public static class RecordChangeDate {

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

    /**
     * Created by jpanzer on 21.02.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "languageTerms"
    })
    public static class LanguageOfCataloging {

        @XmlElement(name = "languageTerm", required = true)
        protected List<LanguageType.LanguageTerm> languageTerms;

        @XmlAttribute(name = "objectPart")
        protected String objectPart;



        public List<LanguageType.LanguageTerm> getLanguageTerms() {
            if (languageTerms == null) {
                languageTerms = new ArrayList<LanguageType.LanguageTerm>();
            }
            return this.languageTerms;
        }

        public void setLanguageTerms(List<LanguageType.LanguageTerm> languageTerms) {
            this.languageTerms = languageTerms;
        }

        public String getObjectPart() {
            return objectPart;
        }

        public void setObjectPart(String objectPart) {
            this.objectPart = objectPart;
        }
    }

    /**
     * Created by jpanzer on 21.02.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class RecordCreationDate {

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

    /**
     * Created by jpanzer on 21.02.14.
     */
    public static class RecordOrigin extends  ValueType {


    }

    /**
     * Created by jpanzer on 21.02.14.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class RecordContentSource {

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
}
