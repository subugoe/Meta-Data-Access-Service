package de.unigoettingen.sub.mongomapper.springdata;

import de.unigoettingen.sub.medas.metsmods.jaxb.Mods;
import de.unigoettingen.sub.medas.model.Doc;
import org.springframework.data.repository.Repository;

/**
 * Created by jpanzer on 27.03.14.
 */
public interface ModsRepository extends Repository<Doc, Long> {

    //    List<Mods>findAllModsWithoutRelatedItem ();

    //--- Mods section
    Mods findFirstMods(String docid);
    Mods findModsByRecordIdentifier(String recordIdentifier);
    Mods saveMods(Mods mods);
    void removeMods(String docid);
}
