package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.ApproximatedArchive;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApproximatedArchiveRepositoryInterface extends MongoRepository<ApproximatedArchive, ObjectId> {
  ApproximatedArchive findByArchiveIdAndUsernameNot(ObjectId archiveId, String username);
  ApproximatedArchive findByArchiveId(ObjectId archiveId);
}
