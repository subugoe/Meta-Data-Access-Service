package de.unigoettingen.sub.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer on 21.02.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "objectPart"
})
public class LanguageOfCataloging {

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
