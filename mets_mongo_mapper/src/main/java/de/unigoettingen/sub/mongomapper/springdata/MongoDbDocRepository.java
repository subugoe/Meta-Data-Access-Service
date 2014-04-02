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

        List<Doc> docList = operations.findAll(Doc.class);

        return docList;
    }

    public Doc findAndRemoveDocForMets(String docid) {
        Query query = query(where("_id").is(new ObjectId(docid)));
        return operations.findAndRemove(query, Doc.class);
    }
}
