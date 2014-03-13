package de.unigoettingen.sub.mongomapper.springdata;


import de.unigoettingen.sub.jaxb.Mets;
import de.unigoettingen.sub.jaxb.Mods;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;


/**
 * Created by jpanzer on 17.02.14.
 */
public interface MetsRepository extends Repository<Mets, Long> {

    //--- Mets section
    Mets findOneMets(String docid);
    List<Mets> findAllMets();
    List<Mods> findAllModsWithRelatedItem();
//    List<Mods>findAllModsWithoutRelatedItem ();
    Mets findMetsByModsId(String docid);
    Mets saveMets(Mets mets);
    void removeMets(String docid);


    //--- Mods section
    Mods findFirstMods(String docid);
    Mods findModsByRecordIdentifier(String recordIdentifier);
    Mods saveMods(Mods mods);
    void removeMods(String docid);

    //--- allgemein
    String findDocidByRecordIdentifier(String ppn);

}
