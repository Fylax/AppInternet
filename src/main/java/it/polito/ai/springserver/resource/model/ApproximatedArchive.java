package it.polito.ai.springserver.resource.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Represent the approximated archive representation. It is related to the corresponding archive
 * by archiveId attribute.
 */
@Document
public class ApproximatedArchive {

  @Id
  private ObjectId id;
  private ObjectId archiveId;
  private String username;
  private SortedSet<Long> timestamps;
  private SortedSet<Point> positions;
  private int countPositionArchive;

  @Transient
  private boolean purchased;

  private double amount;

  public ApproximatedArchive(ObjectId id, ObjectId archiveId, String username, SortedSet<Long> timestamps,
                             SortedSet<Point> positions, int countPositionArchive, double amount) {
    this.id = id;
    this.archiveId = archiveId;
    this.username = username;
    this.timestamps = timestamps;
    this.positions = positions;
    this.countPositionArchive = countPositionArchive;
    this.purchased = false;
    this.amount = amount;
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
    this.countPositionArchive = positions.size();
    this.amount = this.countPositionArchive * 1.1;
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

  public int getCountPositionArchive() {
    return countPositionArchive;
  }

  public boolean isPurchased() {
    return purchased;
  }

  public void setPurchased(boolean purchased) {
    this.purchased = purchased;
  }

  public double getAmount() {
    return amount;
  }
}
