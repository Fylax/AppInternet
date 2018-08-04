package it.polito.ai.springserver.resource.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Document
public class ApproximatedArchive {

  @Id
  private ObjectId id;
  private ObjectId archiveId;
  private String username;
  private SortedSet<Long> timestamps;
  private SortedSet<GeoJsonPoint> positions;

  public ApproximatedArchive(ObjectId archiveId,
                             String username,
                             SortedSet<Long> timestamps,
                             SortedSet<GeoJsonPoint> positions) {
    this.archiveId = archiveId;
    this.username = username;
    this.timestamps = timestamps;
    this.positions = positions;
  }

  public ApproximatedArchive(ObjectId archiveId, String username, List<Position> positions){
    this.archiveId = archiveId;
    this.username = username;
    this.timestamps = new TreeSet<>();
    this.positions = new TreeSet<>((x, y) -> {
      if(x.equals(y)){
        return 0;
      }
      if(x.getX() > y.getX()){
        return 1;
      }
      else if(x.getX() == y.getX()) {
        if (x.getY() > y.getY()) {
          return 1;
        }
      }
      return -1;
    });
    for(Position p : positions){
      this.timestamps.add(p.getTimestamp() - (p.getTimestamp()%60));
      double x = ((double)((int) (p.getLatitude() * 100))) / 100;
      double y = ((double)((int) (p.getLongitude() * 100))) / 100;
      this.positions.add(new GeoJsonPoint(x, y));
    }
  }

  public ApproximatedArchive() {
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public ObjectId getArchiveId() {
    return archiveId;
  }

  public void setArchiveId(ObjectId archiveId) {
    this.archiveId = archiveId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public SortedSet<Long> getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(SortedSet<Long> timestamps) {
    this.timestamps = timestamps;
  }

  public SortedSet<GeoJsonPoint> getPositions() {
    return positions;
  }

  public void setPositions(SortedSet<GeoJsonPoint> positions) {
    this.positions = positions;
  }
}
