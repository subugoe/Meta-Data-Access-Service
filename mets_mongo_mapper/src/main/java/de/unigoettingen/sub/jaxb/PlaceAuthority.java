//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.24 at 04:05:56 PM CET 
//


package de.unigoettingen.sub.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for placeAuthority.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="placeAuthority">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="marcgac"/>
 *     &lt;enumeration value="marccountry"/>
 *     &lt;enumeration value="iso3166"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "placeAuthority")
@XmlEnum
public enum PlaceAuthority {

    @XmlEnumValue("marcgac")
    MARCGAC("marcgac"),
    @XmlEnumValue("marccountry")
    MARCCOUNTRY("marccountry"),
    @XmlEnumValue("iso3166")
    ISO_3166("iso3166");
    private final String value;

    PlaceAuthority(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlaceAuthority fromValue(String v) {
        for (PlaceAuthority c: PlaceAuthority.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
