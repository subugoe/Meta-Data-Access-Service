package de.unigoettingen.sub.mongomapper.springdata;

import de.unigoettingen.sub.medas.model.Doc;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by jpanzer on 27.03.14.
 */
@Repository
public class MongoDbDocRepository implements DocRepository {

    private final Logger logger = LoggerFactory.getLogger(MongoDbDocRepository.class);
    private final MongoOperations operations;

    @Autowired
    public MongoDbDocRepository(MongoOperations operations) {
        this.operations = operations;
    }

    public Doc saveDoc(Doc doc) {
        operations.save(doc);
        return doc;
    }

    public List<Doc> findAllDocs() {

        //long start = System.currentTimeMillis();
        List<Doc> docList = operations.findAll(Doc.class);
        //System.out.println("findAllDocs: " + (System.currentTimeMillis()-start));

        // retrieve docid of relatedItem
        // TODO is the docid required for a relatedItem?
        //start = System.currentTimeMillis();
//        for (Doc doc : docList) {
//            Set<RelatedItem> relatedItems = doc.getRelatedItem();
//            for (RelatedItem relatedItem : relatedItems) {
//                Set<RecordIdentifier> recordIdentifiers = relatedItem.getRecordIdentifier();
//                for (RecordIdentifier recordIdentifier : recordIdentifiers) {
//                    ShortDocInfo shortDocInfo = this.findDocidByRecordIdentifier(recordIdentifier.getValue());
//                    if (shortDocInfo != null) {
//                        recordIdentifier.setRelatedDocid(shortDocInfo.getDocid());
//                    }  else {
//                        logger.error("No record found for reordIdentifier " + recordIdentifier.getValue());
//                    }
//                }
//            }
//        }
        //System.out.println("add docid to all relatedItems: " + (System.currentTimeMillis()-start));

        return docList;
    }

    public Doc findAndRemoveDocForMets(String docid) {
        Query query = query(where("_id").is(new ObjectId(docid)));
        return operations.findAndRemove(query, Doc.class);
    }
}
