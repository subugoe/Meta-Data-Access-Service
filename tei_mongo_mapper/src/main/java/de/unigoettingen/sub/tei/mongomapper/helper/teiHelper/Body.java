
package de.unigoettingen.sub.tei.mongomapper.helper.teiHelper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Body complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Body">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="p" type="{}P"/>
 *         &lt;element name="milestone" type="{}Milestone"/>
 *         &lt;element name="figure" type="{}Figure"/>
 *         &lt;element name="pb" type="{}Pb"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Body", propOrder = {
    "pOrMilestoneOrFigure"
})
public class Body {

    @XmlElements({
        @XmlElement(name = "p", type = P.class),
        @XmlElement(name = "milestone", type = Milestone.class),
        @XmlElement(name = "figure", type = Figure.class),
        @XmlElement(name = "pb", type = Pb.class)
    })
    protected List<Object> pOrMilestoneOrFigure;

    /**
     * Gets the value of the pOrMilestoneOrFigure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pOrMilestoneOrFigure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPOrMilestoneOrFigure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link P }
     * {@link Milestone }
     * {@link Figure }
     * {@link Pb }
     * 
     * 
     */
    public List<Object> getPOrMilestoneOrFigure() {
        if (pOrMilestoneOrFigure == null) {
            pOrMilestoneOrFigure = new ArrayList<Object>();
        }
        return this.pOrMilestoneOrFigure;
    }

}
