package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Archive;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArchiveRepositoryInterface extends MongoRepository<Archive, ObjectId> {
  Archive findByArchiveId(ObjectId archiveId);
  List<Archive> findByUserId(long userId, Pageable pageable);
  int countByUserId(long userId);
}
