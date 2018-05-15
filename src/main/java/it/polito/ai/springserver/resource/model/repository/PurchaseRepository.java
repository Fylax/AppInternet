package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepository extends MongoRepository<Purchase, String> {
  List<Purchase> findAllByCustomerid(long customerid);
}
