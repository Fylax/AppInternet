package it.polito.ai.springserver.resource.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import it.polito.ai.springserver.resource.model.Position;


public class PositionRepositoryInterfaceImpl implements PositionRepositoryInterfaceCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Position getUserLatestPosition(long userid) {
    Query query = new Query();
    query.limit(10);
    query.with(new Sort(Sort.Direction.DESC, "timestamp"));
    query.addCriteria(Criteria.where("userid").is(userid));

    return mongoTemplate.findOne(query, Position.class);
  }
}
