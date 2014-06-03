package de.unigoettingen.sub.mongomapper;

import de.unigoettingen.sub.medas.metsmods.jaxb.*;
import de.unigoettingen.sub.medas.model.*;
import de.unigoettingen.sub.mongomapper.helper.DocidLookupService;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbDocRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbMetsRepository;
import de.unigoettingen.sub.mongomapper.springdata.MongoDbModsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by jpanzer on 02.04.14.
 */
@Service
public class DocService {

    private final Logger logger = LoggerFactory.getLogger(DocService.class);

    @Autowired
    protected DocidLookupService lookupService;

    @Autowired()
    protected MongoDbMetsRepository metsRepo;

    @Autowired()
    protected MongoDbModsRepository modsRepo;

    @Autowired()
    protected MongoDbDocRepository docRepo;


    protected String getPrimaryId(Map<String, String> ids) {

        if (ids.containsKey("purl") ) {
            String id = ids.get("purl");
            String hash = DigestUtils.md5DigestAsHex(id.getBytes());
            return "purl:md5hash:" + hash; //lookupService.findDocid(hash);
        } else if (ids.containsKey("gbv-ppn")) {
            String id = ids.get("gbv-ppn");
            String hash = DigestUtils.md5DigestAsHex(id.getBytes());
            return "gbv-ppn:md5hash:" + hash;
        } else if (ids.containsKey("ppn")) {
            String id = ids.get("ppn");
            String hash = DigestUtils.md5DigestAsHex(id.getBytes());
            return "ppn:md5hash:" + hash;
        }
        // TODO other types

        return null;
    }

    /**
     * The method checks if the document is in the DB, and returns the docid and recordIdentifier packed as a
     * ShortDocInfo object or null if not in the db. The test will be performed with the recordIdentifier.
     *
     * @param mets The Mets document to check.
     * @return The docid if already stored, otherwise null.
     */
    protected Map<String, String> getIdentifiers(Mets mets) {

        Map<String, String> ids = new HashMap<>();

        List<MdSecType> dmdSecs = mets.getDmdSecs();
        for (MdSecType mdSec : dmdSecs) {
            List<Mods> modsList = mdSec.getMdWrap().getXmlData().getMods();
            for (Mods mods : modsList) {
                List<Object> objectList = mods.getElements();
                for (Object obj : objectList) {

                    if (obj instanceof IdentifierType) {
                        IdentifierType id = (IdentifierType) obj;
                        ids.put(id.getType().toLowerCase(), id.getValue());
                    }


                    if (obj instanceof RecordInfoType) {
                        List<Object> elements = ((RecordInfoType) obj).getElements();
                        for (Object o : elements) {
                            if (o instanceof RecordInfoType.RecordIdentifier) {
                                String recId = ((RecordInfoType.RecordIdentifier) o).getValue();
                                String source = ((RecordInfoType.RecordIdentifier) o).getSource();
                                ids.put(source.toLowerCase(), recId);
                            }
                        }
                    }

                }
            }
        }

        return ids;
    }

    protected String encodeUrl(String url) {

        try {
            return URLEncoder.encode(url, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            logger.error("Could not encode the String: " + url);
        }

        return null;
    }

    /**
     * Creates a basic Doc object
     * @param mets
     * @param primaryId
     * @param request
     * @return
     */
    protected Doc retrieveBasicDocInfo(Mets mets, String primaryId, HttpServletRequest request) {
        // why does this function need the mets and the primaryID?
        // is the request required?
        int pagenumber = 0;
        String metsId = mets.getID();

        List<MdSecType> dmdSecs = mets.getDmdSecs();

        // TODO currently just the first mdSec element will be examined - is this sufficient?
        // TODO currently just the first mods element will be examined - is this sufficient?

        Mods mods = dmdSecs.get(0).getMdWrap().getXmlData().getMods().get(0);
        List<Object> objectList = mods.getElements();
        Doc doc = new Doc();

        // add docid, the same as in original mets documente (related)
        doc.setId(primaryId);

        // add metsURL
        String metsUrl = this.getUrlString(request) + "/documents/" + primaryId + "/mets";
        doc.setMets(metsUrl);

        for (Object obj : objectList) {

            if (obj instanceof RecordInfoType) {

                // add recordIdentifiers
                doc.addRecordIdentifiers(getRecordIdentifiers((RecordInfoType) obj,metsId));
            }

            if (obj instanceof IdentifierType) {

                // add identifiers
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
                        List<RecordIdentifier> recordIdentifierList = this.getRecordIdentifiers((RecordInfoType) o2, metsId);
                        if (recordIdentifierList != null && recordIdentifierList.size() > 0) {
                            relatedItem.addRecordIdentifiers(recordIdentifierList);
                            // add relatedItem
                            doc.addRelatedItem(relatedItem);
                        }  else {
                            logger.info("Missing recordIdentifier in relatedItem for document docid: " + metsId);
                        }


//                        // add docid to relatedItem
//                        List<RecordIdentifier> recordIdentifiers = relatedItem.getRecordIdentifier();
//                        for (RecordIdentifier recordIdentifier : recordIdentifiers) {
//                            ShortDocInfo shortDocInfo = this.findDocidByRecordIdentifier(recordIdentifier.getValue(), recordIdentifier.getSource());
//                            if (shortDocInfo != null) {
//                                recordIdentifier.setId(shortDocInfo.getDocid());
//                            } else {
//                                logger.error("No record found for reordIdentifier " + recordIdentifier.getValue());
//                            }
//                        }


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
                        try {
                            pagenumber = Integer.valueOf(extendArray[0]);
                        } catch (NumberFormatException e) {
                            logger.error("Expected pagenumber in PhysicalDescriptionType.Extent for docid " + metsId + " is not a number" );
                            pagenumber = 0;
                        }
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

        // add contentCount
        List<StructMapType> structMapTypeList = mets.getStructMaps();
        for (StructMapType structMapType : structMapTypeList) {
            if (structMapType.getTYPE().equalsIgnoreCase("PHYSICAL")) {
                // add relatedItem
                doc.setContentCount(structMapType.getDiv().getDivs().size());

                if (pagenumber == 0)
                    doc.setPageCount(structMapType.getDiv().getDivs().size());

            }
        }

        return doc;
    }

    /**
     * Retrieves a basic Doc objects from the database and adds the value for the content field.
     * @param mets
     * @param primaryId
     * @param request
     * @return
     */
    protected Doc retrieveFullDocInfo(Mets mets, String primaryId, HttpServletRequest request) {

        Doc doc = retrieveBasicDocInfo(mets, primaryId, request);

        doc.setContent(retrieveDocContents(mets));

        return doc;
    }

    /**
     * From the METS the LOGICAL struct map is retrieved and the stored information is stored in a list of Content objects.
     * @param mets  The METS to parse
     * @return
     */
    protected List<Doc.Content> retrieveDocContents(Mets mets) {

        List<Doc.Content> contents = new ArrayList<>();

        for (StructMapType structMap : mets.getStructMaps()) {
            if (structMap.getTYPE().equalsIgnoreCase("LOGICAL")) {
                DivType div = structMap.getDiv();

                contents.addAll(evaluateDiv(div));
            }
        }

        return contents;
    }

    protected List<Doc.Content> evaluateDivs(List<DivType> divs) {

        List<Doc.Content> contents = new ArrayList<>();
        for (DivType div : divs) {
            contents.addAll(this.evaluateDiv(div));
        }

        return contents;
    }

    /**
     * A part (div) from the METS is parsed and the information is copied into a content object. With this content object a list is started, with is afterwards filled with optional sub divs.
     * @param div
     * @return A list of Content objects representing the given div and its sub divs.
     */
    protected List<Doc.Content> evaluateDiv(DivType div) {

        List<Doc.Content> contents = new ArrayList<>();
        Doc.Content content = new Doc.Content();

        content.setId(div.getID());

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

            RecordIdentifier recId = new RecordIdentifier();

            // TODO Since the recordIdentifier is derived from the URL, there is no source attribute! Default is "gbv-ppn"
            recId.setSource("default");
            recId.setValue(recordIdentifier);
            //recId.setId(lookupService.findDocid(recId.getSource() + ":" + recId.getValue()));

            //content.setRecordIdentifier(recId.getId());

            Mods contentMods = modsRepo.findModsByRecordIdentifier(recordIdentifier);

            if (contentMods != null) {
                Mets contentMets = metsRepo.findMetsByModsId(contentMods.getID());
                if (contentMets != null) {
                    content.setId(contentMets.getID());
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

    protected Identifier getIdentifier(IdentifierType identifierType) {
        Identifier identifier = new Identifier();

        identifier.setValue(identifierType.getValue());
        identifier.setType(identifierType.getType());

        return identifier;
    }

    protected List<RecordIdentifier> getRecordIdentifiers(RecordInfoType recordInfoType, String docid) {

        List<Object> objectList2 = recordInfoType.getElements();
        List<RecordIdentifier> recordIdentifiers = new ArrayList<>();

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

    protected Set<String> getRecordIdentifier(Mods mods) {
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

    protected Set<String> getRelatedItemRecordIdentifier(Mods mods) {
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

    protected RecordIdentifier getRcordIdentifier(RelatedItem relatedItem) {

        return null;
    }


    protected String getUrlString(HttpServletRequest request) {

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


    protected Mets isDocInDB(String docid) {
        return metsRepo.findOneMets(docid);
    }

    public ShortDocInfo isRecordInDB(String recordIdentifier, String source) {

        return findDocidByRecordIdentifier(recordIdentifier, source);
    }




    protected ShortDocInfo findDocidByRecordIdentifier(String recordIdentifier, String source) {

        String docid = lookupService.findDocid(source+":"+recordIdentifier);
        ShortDocInfo shortDocInfo = new ShortDocInfo(docid, recordIdentifier, source);

        return shortDocInfo;

    }
}
