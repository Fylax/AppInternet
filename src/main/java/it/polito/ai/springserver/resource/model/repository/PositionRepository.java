package it.polito.ai.springserver.resource.model.repository;

import com.mongodb.client.model.geojson.Polygon;
import it.polito.ai.springserver.resource.model.Position;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PositionRepository extends MongoRepository<Position, String> {
  List<Position> findByUseridAndTimestampBetween(long userid, long start, long end);
  long countPositionByPositionIsWithinAndTimestampBetween(GeoJsonPolygon polygon, long start, long end);
  List<Position> findByPositionWithinAndPositionNotInAndTimestampBetween(GeoJsonPolygon polygon,
                                                                         List<Position> except,
                                                                         long start, long end);
}
