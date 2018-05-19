package it.polito.ai.springserver.resource.model.repository;

import com.mongodb.client.model.geojson.Polygon;
import it.polito.ai.springserver.resource.model.Position;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends MongoRepository<Position, String> {
  List<Position> findByUseridAndTimestampBetween(long userid, long start, long end);
  long countPositionByPointIsWithinAndTimestampBetween(GeoJsonPolygon polygon, long start, long end);
  List<Position> findByPointWithinAndTimestampBetween(GeoJsonPolygon polygon, long start, long end);
}
