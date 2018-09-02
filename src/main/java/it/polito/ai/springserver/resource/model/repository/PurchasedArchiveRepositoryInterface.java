package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.PurchasedArchive;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchasedArchiveRepositoryInterface extends MongoRepository<PurchasedArchive, ObjectId> {
  List<PurchasedArchive> findByUserId(Long userId, Pageable pageable);

  List<PurchasedArchive> findByUserId(Long userId);

  boolean existsByArchiveIdAndUserId(String archiveId, Long userId);

  int countByUserId(Long userId);

}
