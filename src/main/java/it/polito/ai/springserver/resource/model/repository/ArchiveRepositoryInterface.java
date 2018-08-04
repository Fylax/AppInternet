package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Archive;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchiveRepositoryInterface extends MongoRepository<Archive, ObjectId> {
  Archive findByArchiveId(ObjectId archiveId);
}
