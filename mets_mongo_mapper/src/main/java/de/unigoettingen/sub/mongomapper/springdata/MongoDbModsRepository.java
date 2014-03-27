package de.unigoettingen.sub.mongomapper.springdata;

import de.unigoettingen.sub.medas.metsmods.jaxb.Mods;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by jpanzer on 27.03.14.
 */
@Repository
public class MongoDbModsRepository implements ModsRepository {

    private final Logger logger = LoggerFactory.getLogger(MongoDbModsRepository.class);
    private final MongoOperations operations;

    @Autowired
    public MongoDbModsRepository(MongoOperations operations) {
        this.operations = operations;
    }



    @Override
    public Mods findFirstMods(String docid) {

        Query query = query(where("id").is(new ObjectId(docid)));
        return operations.findOne(query, Mods.class);
    }

    @Override
    public Mods findModsByRecordIdentifier(String recordIdentifier) {

        Query query = query(where("elements.elements").elemMatch(new Criteria().andOperator(
                where("_class").is("de.unigoettingen.sub.medas.metsmods.jaxb.RecordInfoType$RecordIdentifier").and("value").is(recordIdentifier))));
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

}
