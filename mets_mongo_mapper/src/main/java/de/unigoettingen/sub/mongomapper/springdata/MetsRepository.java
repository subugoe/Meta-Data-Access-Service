package de.unigoettingen.sub.mongomapper.springdata;


import de.unigoettingen.sub.medas.metsmods.jaxb.Mets;
import de.unigoettingen.sub.medas.metsmods.jaxb.Mods;
import de.unigoettingen.sub.medas.model.Doc;
import de.unigoettingen.sub.medas.model.ShortDocInfo;
import org.springframework.data.repository.Repository;

import java.util.List;


/**
 * Created by jpanzer on 17.02.14.
 */
public interface MetsRepository  extends Repository<Mets, Long> {

    //--- Mets section
    //Long countMets();
    Mets findOneMets(String docid);
    List<Mets> findAllMets();
    List<Mods> findAllModsWithRelatedItem();

    //List<Mets> findAllCollections();
    //List<Mets> findAllDocuments();

//    List<Mods>findAllModsWithoutRelatedItem ();
    Mets findMetsByModsId(String docid);
    Mets saveMets(Mets mets);
    void removeMets(String docid);
    //void findAndModifyMets(String docid, boolean isCollection);


    //--- Mods section
    Mods findFirstMods(String docid);
    Mods findModsByRecordIdentifier(String recordIdentifier);
    Mods saveMods(Mods mods);
    void removeMods(String docid);


    //--- Doc section
    Doc saveDoc(Doc doc);
    List<Doc> findAllDocs();
    Doc findAndRemoveDocForMets(String docid);

    //--- allgemein
    ShortDocInfo findDocidByRecordIdentifier(String ppn);


}
