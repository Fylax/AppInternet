package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.PurchaseDetailed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepositoryInterface extends MongoRepository<PurchaseDetailed, String>,
    PurchaseRepositoryInterfaceCustom {
  List<PurchaseDetailed> findByCustomeridAndTimestampBetween(long customerid, long start, long end);
}
