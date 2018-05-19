package it.polito.ai.springserver.resource.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import it.polito.ai.springserver.resource.model.Position;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


//@Repository("LatestUserPositionRepositoryImpl")
public class LatestUserPositionRepositoryImpl implements LatestUserPositionRepositoryCustom {

  @Autowired
  private MongoOperations operations;

  @Override
  public Position getUserLatestPosition(long userid) {
    Criteria c = Criteria.where("userid");
    Query query = new Query();
    query.limit(10);
    query.with(new Sort(Sort.Direction.DESC, "timestamp"));
    query.addCriteria(c);

    return operations.findOne(query, Position.class);
  }
}
