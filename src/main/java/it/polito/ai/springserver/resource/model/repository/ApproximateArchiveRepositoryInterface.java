package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.ApproximatedArchive;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApproximateArchiveRepositoryInterface extends MongoRepository<ApproximatedArchive, ObjectId> {
}
