package de.unigoettingen.sub.mets.mongomapper.helper;

import au.edu.apsr.mtk.base.*;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
class DivHelper {
    private final Logger logger = LoggerFactory.getLogger(DivHelper.class);

    private final BasicDBObjectHelper basicDBObjectHelper = new BasicDBObjectHelper();
    private final MiscHelper miscHelper = new MiscHelper();

    public DivHelper() {

    }

    /**
     * Retrieves the elements from METS div and return these wrapped
     * as a BasicDBObject.
     *
     * @param div The div element to process.
     * @return The (JSON-like) object in which the retrieved content is stored.
     * @throws au.edu.apsr.mtk.base.METSException
     */
    BasicDBObject handleDiv(Div div) throws METSException {

        // handle divs
        BasicDBObject div_basicDBObject = new BasicDBObject();

        basicDBObjectHelper.createBasicDBObject("ID", div.getID(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ORDER", div.getOrder(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ORDERLABEL", div.getOrderLabel(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("LABEL", div.getLabel(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("DMDID", div.getDmdID(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("ADMID", div.getAdmID(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("TYPE", div.getType(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("CONTENTIDS", div.getContentIDs(), div_basicDBObject);
        basicDBObjectHelper.createBasicDBObject("xlinklabel", div.getXLinkLabel(), div_basicDBObject);

        // mptrs
        List<Mptr> mptrs = div.getMptrs();
        List<BasicDBObject> mprtr_baBasicDBObjectList = new ArrayList<BasicDBObject>();
        for (Mptr mptr : mptrs) {
            BasicDBObject mptr_basicDBObject1 = new BasicDBObject();
            basicDBObjectHelper.createBasicDBObject("ID", mptr.getID(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("LOCTYPE", mptr.getLocType(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("OTHERLOCTYPE", mptr.getOtherLocType(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("type", mptr.getType(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("href", mptr.getHref(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("role", mptr.getRole(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("arcrole", mptr.getArcRole(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("title", mptr.getTitle(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("show", mptr.getShow(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("actuate", mptr.getActuate(), mptr_basicDBObject1);
            basicDBObjectHelper.createBasicDBObject("CONTENTIDS", mptr.getContentIDs(), mptr_basicDBObject1);
            mprtr_baBasicDBObjectList.add(mptr_basicDBObject1);

        }
        if (!mprtr_baBasicDBObjectList.isEmpty())
            div_basicDBObject.append("mptr", mprtr_baBasicDBObjectList);

        // fptrs
        List<Fptr> fptrs = div.getFptrs();
        List<BasicDBObject> fptr_basicDBObjectList = new ArrayList<BasicDBObject>();
        for (Fptr fptr : fptrs) {

            BasicDBObject fptr_basicDBObject = new BasicDBObject();
            basicDBObjectHelper.createBasicDBObject("ID", fptr.getID(), fptr_basicDBObject);
            basicDBObjectHelper.createBasicDBObject("FILEID", fptr.getFileID(), fptr_basicDBObject);
            basicDBObjectHelper.createBasicDBObject("CONTENTIDS", fptr.getContentIDs(), fptr_basicDBObject);

            // TODO process Par with TargetID
            Par par = fptr.getPar();
            if (par != null)
                fptr_basicDBObject.append("par", miscHelper.handlePar(par));

            // TODO process Area with TargetID
            Area parArea = fptr.getArea();
            if (parArea != null)
                fptr_basicDBObject.append("area", miscHelper.handleArea(parArea));

            // TODO process Seq with TargetID
            Seq seq = fptr.getSeq();
            if (seq != null)
                fptr_basicDBObject.append("seq", miscHelper.handleSeq(seq));

            fptr_basicDBObjectList.add(fptr_basicDBObject);

        }
        if (!fptr_basicDBObjectList.isEmpty())
            div_basicDBObject.append("fptr", fptr_basicDBObjectList);

        // divs
        if (!div.getDivs().isEmpty()) {

            List<BasicDBObject> div_basicDBObjects_list = new ArrayList<BasicDBObject>();
            List<Div> divs1 = div.getDivs();
            for (Div div1 : divs1)
                div_basicDBObjects_list.add(handleDiv(div1));

            div_basicDBObject.append("div", div_basicDBObjects_list);
        }

        return div_basicDBObject;
    }
}