package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Position;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepositoryInterface extends MongoRepository<Position, String>,
        PositionRepositoryInterfaceCustom {
  List<Position> findByPointApproximatedWithinAndTimestampBetweenAndUseridNot(GeoJsonPolygon polygon,
                                                                              long start, long end, long userId);

  List<Position> findByUseridAndArchiveId(long userId, ObjectId archiveId);
  List<Position> findByArchiveId(ObjectId archiveId);
}
