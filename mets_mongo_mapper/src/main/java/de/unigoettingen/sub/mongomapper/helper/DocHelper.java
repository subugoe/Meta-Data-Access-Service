package de.unigoettingen.sub.mongomapper.helper;

import de.unigoettingen.sub.medas.metsmods.jaxb.*;
import de.unigoettingen.sub.medas.model.*;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jpanzer on 26.03.14.
 */
@Component
public class DocHelper {

    private final Logger logger = LoggerFactory.getLogger(DocHelper.class);

    @Autowired
    private MongoDbMetsRepository metsRepo;

    public Doc retrieveBasicDocInfo(Mets mets, HttpServletRequest request) {

        int pagenumber = 0;
        String metsId = mets.getID();

        List<MdSecType> dmdSecs = mets.getDmdSecs();

        // TODO currently just the first mdSec element will be examined - is this sufficient?
        // TODO currently just the first mods element will be examined - is this sufficient?

        Mods mods = dmdSecs.get(0).getMdWrap().getXmlData().getMods().get(0);
        List<Object> objectList = mods.getElements();
        Doc doc = new Doc();

        // add docid, the same as in original mets documente (related)
        doc.setDocid(metsId);

        // add metsURL
        String metsUrl = this.getUrlString(request) + "/documents/" + metsId + "/mets";
        doc.setMets(metsUrl);

        for (Object obj : objectList) {

            if (obj instanceof RecordInfoType) {

                // add recordIdentifiers
                doc.addRecordIdentifiers(getRecordIdentifiers((RecordInfoType) obj));
            }

            if (obj instanceof IdentifierType) {

                // add recordIdentifiers
                doc.addIdentifier(getIdentifier((IdentifierType) obj));
            }

            if (obj instanceof TitleInfoType) {
                TitleInfoType titleInfoType = (TitleInfoType) obj;
                List<Object> objectList1 = titleInfoType.getElements();
                for (Object o : objectList1) {
                    if (o instanceof Title) {
                        Title title = (Title) o;
                        // add title
                        doc.setTitle(title.getValue());
                    }
                    if (o instanceof BaseTitleInfoType.SubTitle) {
                        BaseTitleInfoType.SubTitle subTitle = (BaseTitleInfoType.SubTitle) o;
                        // add subTitle
                        doc.setSubTitle(subTitle.getValue());
                    }
                }
            }

            if (obj instanceof RelatedItemType) {
                RelatedItem relatedItem = new RelatedItem();

                RelatedItemType relatedItemType = (RelatedItemType) obj;
                relatedItem.setType(relatedItemType.getType());

                List<Object> objectList1 = relatedItemType.getElements();
                for (Object o2 : objectList1) {
                    if (o2 instanceof RecordInfoType) {
                        relatedItem.addRecordIdentifiers(this.getRecordIdentifiers((RecordInfoType) o2));
                        // add relatedItem
                        doc.addRelatedItem(relatedItem);


                        // add docid to relatedItem
                        Set<RecordIdentifier> recordIdentifiers = relatedItem.getRecordIdentifier();
                        for (RecordIdentifier recordIdentifier : recordIdentifiers) {
                            ShortDocInfo shortDocInfo = metsRepo.findDocidByRecordIdentifier(recordIdentifier.getValue());
                            if (shortDocInfo != null) {
                                recordIdentifier.setRelatedDocid(shortDocInfo.getDocid());
                            } else {
                                logger.error("No record found for reordIdentifier " + recordIdentifier.getValue());
                            }
                        }


                    }
                }
            }

            if (obj instanceof ClassificationType) {
                Classification classification = new Classification();

                ClassificationType classificationType = (ClassificationType) obj;
                classification.setAuthority(classificationType.getAuthority());

                classification.setValue(classificationType.getValue());

                // add classification
                doc.addClassifications(classification);

            }

            if (obj instanceof PhysicalDescriptionType) {
                PhysicalDescriptionType physicalDescriptionType = (PhysicalDescriptionType) obj;

                List<Object> objectList1 = physicalDescriptionType.getElements();
                for (Object o3 : objectList1) {
                    if (o3 instanceof PhysicalDescriptionType.Extent) {
                        PhysicalDescriptionType.Extent extent = (PhysicalDescriptionType.Extent) o3;
                        String extend = extent.getValue();
                        String[] extendArray = extend.split(" ");
                        // required extend format: "200 pages"
                        pagenumber = Integer.valueOf(extendArray[0]);
                        String type = extendArray[1];
                        if (type.equals("pages")) {
                            // add pagenumber
                            doc.setPageCount(pagenumber);
                        }
                    }
                }
            }

            if (obj instanceof PartType) {
                PartType partType = (PartType) obj;
                // add partorder
                doc.setPartOrder(partType.getOrder());

                // TODO possibly use the more precise number: part -> detail -> number value

            }
        }

        if (pagenumber == 0) {
            List<StructMapType> structMapTypeList = mets.getStructMaps();
            for (StructMapType structMapType : structMapTypeList) {
                if (structMapType.getTYPE().equalsIgnoreCase("PHYSICAL")) {
                    // add relatedItem
                    doc.setPageCount(structMapType.getDiv().getDivs().size());
                }
            }
        }

        return doc;
    }


    public Doc retrieveFullDocInfo(Mets mets, HttpServletRequest request) {

        Doc doc = retrieveBasicDocInfo(mets, request);
        doc.setContent(retrieveDocContents(mets));

        return doc;
    }

    private List<Doc.Content> retrieveDocContents(Mets mets) {

        List<Doc.Content> contents = new ArrayList<>();

        for (StructMapType structMap : mets.getStructMaps()) {
            if (structMap.getTYPE().equalsIgnoreCase("LOGICAL")) {
                DivType div = structMap.getDiv();

                contents.addAll(evaluateDiv(div));
            }
        }

        return contents;
    }

    private List<Doc.Content> evaluateDivs(List<DivType> divs) {

        List<Doc.Content> contents = new ArrayList<>();
        for (DivType div : divs) {
            contents.addAll(this.evaluateDiv(div));
        }

        return contents;
    }

    private List<Doc.Content> evaluateDiv(DivType div) {

        List<Doc.Content> contents = new ArrayList<>();
        Doc.Content content = new Doc.Content();

        content.setId(div.getID());

//        List<Object> objects = div.getDMDIDS();
//        if (objects != null && objects.size() > 0) {
//            for (Object obj : objects)
//                System.out.println(obj.getClass());
////                content.addAdmId((String) obj);
//        }
//
//        objects = div.getADMIDS();
//        if (objects != null && objects.size() > 0)
//            for (Object obj : objects)
//                System.out.println(obj.getClass());
//        //              content.addAdmId((String) obj);

        content.setType(div.getTYPE());
        content.setLabel(div.getLABEL());
        content.setOrder(div.getORDER());
        content.setOrderlabel(div.getORDERLABEL());

        List<DivType.Mptr> mptrs = div.getMptrs();
        if (mptrs != null && mptrs.size() > 0) {
            content.setLoctype(mptrs.get(0).getLOCTYPE());
            content.setHref(mptrs.get(0).getHref());

            String metsUrl = mptrs.get(0).getHref();
            int i = metsUrl.lastIndexOf("=");
            String recordIdentifier = metsUrl.substring(i + 1, metsUrl.length());
            content.setRecordIdentifier(recordIdentifier);

            Mods contentMods = metsRepo.findModsByRecordIdentifier(recordIdentifier);

            if (contentMods != null) {
                Mets contentMets = metsRepo.findMetsByModsId(contentMods.getID());
                if (contentMets != null) {
                    content.setDocid(contentMets.getID());
                } else {
                    content.setError("Mets document for recordIdentifier " + recordIdentifier + " could not found in the DB");
                    logger.error("Mods document for recordIdentifier " + recordIdentifier + " could not found in the DB");
                }
            } else {
                content.setError("Mods document for recordIdentifier " + recordIdentifier + " could not found in the DB");
                logger.error("Mods document for recordIdentifier " + recordIdentifier + " could not found in the DB");
            }
        } else {
            // zusammensuchen
            // contents.add(content);
        }

        contents.add(content);
        contents.addAll(this.evaluateDivs(div.getDivs()));

        return contents;
    }

    private Identifier getIdentifier(IdentifierType identifierType) {
        Identifier identifier = new Identifier();

        identifier.setValue(identifierType.getValue());
        identifier.setType(identifierType.getType());

        return identifier;
    }

    public Set<RecordIdentifier> getRecordIdentifiers(RecordInfoType recordInfoType) {

        List<Object> objectList2 = recordInfoType.getElements();
        Set<RecordIdentifier> recordIdentifiers = new HashSet<>();

        for (Object o2 : objectList2) {
            if (o2 instanceof RecordInfoType.RecordIdentifier) {

                RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) o2;

                String source = recordIdentifier.getSource();
                String value = recordIdentifier.getValue();
                recordIdentifiers.add(new RecordIdentifier(value, source));
            }
        }

        return recordIdentifiers;
    }

    public Set<String> getRecordIdentifier(Mods mods) {
        Set<String> recordIdentifierList = new HashSet<>();

        List<Object> modsList = mods.getElements();
        for (Object modsElement : modsList) {
            if (modsElement instanceof RecordInfoType) {
                RecordInfoType recordInfoType = (RecordInfoType) modsElement;
                List<Object> recordInfoList = recordInfoType.getElements();
                for (Object recordInfoElement : recordInfoList) {
                    if (recordInfoElement instanceof RecordInfoType.RecordIdentifier) {
                        RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) recordInfoElement;
                        recordIdentifierList.add(recordIdentifier.getValue());
                    }

                }
            }
        }

        return recordIdentifierList;
    }

    public Set<String> getRelatedItemRecordIdentifier(Mods mods) {
        Set<String> recordIdentifierList = new HashSet<>();

        List<Object> modsList = mods.getElements();
        for (Object modsElement : modsList) {

            if (modsElement instanceof RelatedItemType) {

                RelatedItemType relatedItemType = (RelatedItemType) modsElement;

                List<Object> objectList1 = relatedItemType.getElements();
                for (Object o2 : objectList1) {
                    if (o2 instanceof RecordInfoType) {
                        RecordInfoType recordInfoType = (RecordInfoType) o2;
                        List<Object> recordInfoList = recordInfoType.getElements();
                        for (Object recordInfoElement : recordInfoList) {
                            if (recordInfoElement instanceof RecordInfoType.RecordIdentifier) {
                                RecordInfoType.RecordIdentifier recordIdentifier = (RecordInfoType.RecordIdentifier) recordInfoElement;
                                recordIdentifierList.add(recordIdentifier.getValue());
                            }

                        }
                    }
                }
            }
        }


        return recordIdentifierList;
    }

    public RecordIdentifier getRcordIdentifier(RelatedItem relatedItem) {

        return null;
    }


    public String getUrlString(HttpServletRequest request) {

        String schema = request.getScheme();
        String server = request.getServerName();
        int port = request.getServerPort();
        String contextpath = request.getContextPath();

        StringBuffer strb = new StringBuffer();

        if (schema != null)
            strb.append(schema + "://");
        if (server != null)
            strb.append(server);
        if (port > 0)
            strb.append(":" + port);
        if (contextpath != null)
            strb.append(contextpath);

        return strb.toString();
    }


    public Mets isDocInDB(String docid) {
        return metsRepo.findOneMets(docid);
    }

    public ShortDocInfo isRecordInDB(String recordIdentifier) {

        return metsRepo.findDocidByRecordIdentifier(recordIdentifier);
    }


//    public void setDocidForRelatedItems(Docs docs) {
//        for (Doc doc : docs.getDocs()) {
//            for (RelatedItem relatedItem : doc.getRelatedItem()) {
//                for (RecordIdentifier recordIdentifier : relatedItem.getRecordIdentifier()) {
//                    String recId = recordIdentifier.getValue();
//                    ShortDocInfo shortDocInfo = metsRepo.findDocidByRecordIdentifier(recId);
//                    if (shortDocInfo != null && shortDocInfo.getDocid() != null)
//                        recordIdentifier.setRelatedDocid(shortDocInfo.getDocid());
//                    else
//                        logger.error("could not find docid for recordidentifier: " + recId + " (record is possibly not in the db");
//                }
//            }
//        }
//    }
}
