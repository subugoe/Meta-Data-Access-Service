package de.unigoettingen.sub.mongomapper.springdata;

import de.unigoettingen.sub.medas.model.Doc;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by jpanzer on 27.03.14.
 */
public interface DocRepository extends Repository<Doc, Long> {

    //--- Doc section
    Doc saveDoc(Doc doc);
    List<Doc> findAllDocs();
    Doc findAndRemoveDocForMets(String docid);
}
