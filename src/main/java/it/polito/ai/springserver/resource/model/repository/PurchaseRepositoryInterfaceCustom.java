package it.polito.ai.springserver.resource.model.repository;

import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.Purchase;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface PurchaseRepositoryInterfaceCustom {
  long countPurchasable(long customerid, GeoJsonPolygon polygon, long start, long end);
  List<Position> findPurchasable(long customerid, GeoJsonPolygon polygon, long start, long end);
}
