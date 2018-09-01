package it.polito.ai.springserver.resource.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
  private SortedSet<Point> positions;

  public ApproximatedArchive(ObjectId archiveId,
                             String username,
                             SortedSet<Long> timestamps,
                             SortedSet<Point> positions) {
    this.archiveId = archiveId;
    this.username = username;
    this.timestamps = timestamps;
    this.positions = positions;
  }

  public ApproximatedArchive(ObjectId archiveId, String username, List<Position> positions) {
    this.archiveId = archiveId;
    this.username = username;
    this.timestamps = new TreeSet<>();
    this.positions = new TreeSet<>();
    for (Position p : positions) {
      this.timestamps.add(p.getTimestamp() - (p.getTimestamp() % 60));
      this.positions.add(new Point(p.getLonApproximated(), p.getLatApproximated()));
    }
  }

  public ApproximatedArchive() {
  }


  public String getArchiveId() {
    return archiveId.toString();
  }

  public String getUsername() {
    return username;
  }


  public SortedSet<Long> getTimestamps() {
    return timestamps;
  }

  public SortedSet<Point> getPositions() {
    return positions;
  }

}
