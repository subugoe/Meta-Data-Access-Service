package de.unigoettingen.sub.mongomapper.springdata;


import de.unigoettingen.sub.medas.metsmods.jaxb.Mets;
import de.unigoettingen.sub.medas.metsmods.jaxb.Mods;
import de.unigoettingen.sub.medas.model.Doc;
import de.unigoettingen.sub.medas.model.ShortDocInfo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by jpanzer on 17.02.14.
 */
@Repository
public class MongoDbMetsRepository implements MetsRepository {

    private final MongoOperations operations;

    @Autowired
    public MongoDbMetsRepository(MongoOperations operations) {
        this.operations = operations;
    }

    //--- Mets section


    @Override
    public Mets findOneMets(String docid) {

        Query query = query(where("id").is(new ObjectId(docid)));
        return operations.findOne(query, Mets.class);
    }


    public Mets findOneMetsWithRecordIderntifier(String recordIdentifier) {
        Mods mods = this.findModsByRecordIdentifier(recordIdentifier);
        return this.findMetsByModsId(mods.getID());
    }

    @Override
    public List<Mets> findAllMets() {
        return operations.findAll(Mets.class);
    }

//    @Override
//    public List<Mets> findMetsByDocid() {
//
//        return operations.findAll(Mets.class);
//    }

    @Override
    public List<Mods> findAllModsWithRelatedItem() {

        Query query = query(where("elements").elemMatch(new Criteria().andOperator(
                where("_class").is("RelatedItemType").and("type").is("host"))));
        List<Mods> modsList = operations.find(query, Mods.class);

//        Set<Mets> metsSet = new HashSet<>();

//        if (modsList != null) {
//            for (Mods mods : modsList) {
//
//                Mets mets = this.findMetsByModsId(mods.getID());
//                if (mets != null)
//                    metsSet.add(mets);
//            }
//        }
        return modsList;

    }

//    public List<Mets> findAllCollections() {
//
//        Query query = query(where("isCollection").is(true));
//        List<Mets> metsList = operations.find(query, Mets.class);
//
//        return metsList;
//    }

//    public List<Mets> findAllDocuments() {
//
//        Query query = query(where("isCollection").is(false));
//        List<Mets> metsList = operations.find(query, Mets.class);
//
//        return metsList;
//    }


//    @Override
//    public List<Mods>findAllModsWithoutRelatedItem () {
//
//        Query query = query(where("elements._class").is("RelatedItemType").not());
//        List<Mods> modsList = operations.find(query, Mods.class);
//
//        Set<Mets> metsSet = new HashSet<>();
//
////        if (modsList != null) {
////            for (Mods mods : modsList) {
////
////                Mets mets = this.findMetsByModsId(mods.getID());
////                if (mets != null)
////                    metsSet.add(mets);
////            }
////        }
//        return modsList;
//
//    }

    @Override
    public Mets findMetsByModsId(String docid) {

        Query query = query(where("dmdSecs.mdWrap.xmlData.mods.$id").is(new ObjectId(docid)));
        return operations.findOne(query, Mets.class);
    }


    @Override
    public Mets saveMets(Mets mets) {
        operations.save(mets);
        return mets;
    }

    @Override
    public void removeMets(String docid) {
        Query query = query(where("_id").is(new ObjectId(docid)));
        operations.remove(query, Mets.class);
    }


//    @Override
//    public void findAndModifyMets(String docid, boolean isCollection) {
//        Query query = query(where("_id").is(new ObjectId(docid)));
//        Update update = new Update();
//        update.set("isCollection", isCollection);
//        operations.findAndModify(query, update, Mets.class);
//    }


    //--- Mods section

    @Override
    public Mods findFirstMods(String docid) {

        Query query = query(where("id").is(new ObjectId(docid)));
        return operations.findOne(query, Mods.class);
    }

    @Override
    public Mods findModsByRecordIdentifier(String ppn) {

        Query query = query(where("elements.elements").elemMatch(new Criteria().andOperator(
                where("_class").is("de.unigoettingen.sub.medas.metsmods.jaxb.RecordInfoType$RecordIdentifier").and("value").is(ppn))));
        return operations.findOne(query, Mods.class);

    }


    @Override
    public Mods saveMods(Mods mods) {
        operations.save(mods);
        return mods;
    }

    @Override
    public void removeMods(String docid) {

        Query query = query(where("_id").is(new ObjectId(docid)));
        operations.remove(query, Mods.class);
    }

    //--- Doc

    public Doc saveDoc(Doc doc) {
        operations.save(doc);
        return doc;
    }

    public List<Doc> findAllDocs() {
        return operations.findAll(Doc.class);
    }

    public Doc findAndRemoveDocForMets(String docid) {
        Query query = query(where("_id").is(new ObjectId(docid)));
        return operations.findAndRemove(query, Doc.class);
    }


    //--- allgemein

    @Override
    public ShortDocInfo findDocidByRecordIdentifier(String recId) {

//        Query query = query(where("elements.elements").elemMatch(new Criteria().andOperator(
//                where("_class").is("RecordInfoType$RecordIdentifier").and("value").is(ppn))));
//        Mods mods = operations.findOne(query, Mods.class);

        Mods mods = this.findModsByRecordIdentifier(recId);

        Mets mets;

        if (mods != null) {
            mets = this.findMetsByModsId(mods.getID());
            if (mets != null)
                return new ShortDocInfo(mets.getID(), recId);
        }

        return null;

    }


}
