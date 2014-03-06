package de.unigoettingen.sub.mongomapper.springdata;


import com.mongodb.DBRef;
import de.unigoettingen.sub.jaxb.Mets;
import de.unigoettingen.sub.jaxb.Mods;
import org.bson.types.ObjectId;
import org.springframework.data.repository.Repository;

import java.util.List;


/**
 * Created by jpanzer on 17.02.14.
 */
public interface MetsRepository extends Repository<Mets, Long> {

    //--- Mets section
    Mets findOne(String docid);
    List<Mets> findAll();
    Mets findMetsByModsId(String docid);
    Mets save(Mets mets);
    void removeMets(String docid);


    //--- Mods section
    Mods findFirstMods(String docid);
    Mods findModsByRecordIdentifier(String recordIdentifier);
    Mods save(Mods mods);
    void removeMods(String docid);

    //--- allgemein
    String findDocidByRecordIdentifier(String ppn);

}
