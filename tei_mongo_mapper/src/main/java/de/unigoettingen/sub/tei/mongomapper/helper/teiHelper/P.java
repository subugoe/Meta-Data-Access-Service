
package de.unigoettingen.sub.tei.mongomapper.helper.teiHelper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for P complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="P">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="seq" type="{}Seq"/>
 *         &lt;element name="w" type="{}W"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "P", propOrder = {
    "seqOrW"
})
public class P {

    @XmlElements({
        @XmlElement(name = "seq", type = Seq.class),
        @XmlElement(name = "w", type = W.class)
    })
    protected List<Object> seqOrW;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the seqOrW property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the seqOrW property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeqOrW().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Seq }
     * {@link W }
     * 
     * 
     */
    public List<Object> getSeqOrW() {
        if (seqOrW == null) {
            seqOrW = new ArrayList<Object>();
        }
        return this.seqOrW;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
