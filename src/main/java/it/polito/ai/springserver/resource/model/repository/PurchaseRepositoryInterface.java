package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepositoryInterface extends MongoRepository<Purchase, String>,
    PurchaseRepositoryInterfaceCustom {
  //with this query only if there is a temporal intersection retrieve the purchased positions
  List<Purchase> findByCustomeridAndTimestampBetween(long customerid, long start, long end);
}
