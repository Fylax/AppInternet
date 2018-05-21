package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public class PurchaseRepositoryInterfaceImpl implements PurchaseRepositoryInterfaceCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  private Query getQuery(long customerid, GeoJsonPolygon polygon, long start, long end) {
    Query purchasedQuery = new Query();
    purchasedQuery.addCriteria(Criteria.where("customerid").is(customerid));
    List<Purchase> purchased = mongoTemplate.find(purchasedQuery, Purchase.class);

    Query purchasableQuery = new Query();
    purchasableQuery.addCriteria(Criteria.where("timestamp").gte(start).
        andOperator(Criteria.where("timestamp").lte(end)));
    purchasableQuery.addCriteria(Criteria.where("point").within(polygon));
    for (var purchase : purchased) {
      for (var position : purchase.getPositions()) {
        purchasableQuery.addCriteria(Criteria.where("_id").ne(position.getId()));
      }
    }
    return purchasableQuery;
  }

  @Override
  public long countPurchasable(long customerid, GeoJsonPolygon polygon, long start, long end) {
    var purchasableQuery = this.getQuery(customerid, polygon, start, end);
    return mongoTemplate.count(purchasableQuery, Position.class);
  }

  @Override
  public List<Position> findPurchasable(long customerid, GeoJsonPolygon polygon, long start, long end) {
    var purchasableQuery = this.getQuery(customerid, polygon, start, end);
    return mongoTemplate.find(purchasableQuery, Position.class);
  }


}
