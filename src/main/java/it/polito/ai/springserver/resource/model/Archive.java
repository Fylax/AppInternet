package it.polito.ai.springserver.resource.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Archive {

  @Id
  private ObjectId archiveId;
  private String userName;
  private boolean availableForSale;
  private long timestamp;
  private long countSales;

  public Archive(){  }

  public Archive(ObjectId archiveId, String userName, boolean availableForSale, long timestamp, long countSales) {
    this.archiveId = archiveId;
    this.userName = userName;
    this.availableForSale = availableForSale;
    this.timestamp = timestamp;
    this.countSales = countSales;
  }

  public Archive(String userName, boolean availableForSale, long timestamp, long countSales) {
    this.userName = userName;
    this.availableForSale = availableForSale;
    this.timestamp = timestamp;
    this.countSales = countSales;
  }

  public ObjectId getArchiveId() {
    return archiveId;
  }

  public void setArchiveId(ObjectId archiveId) {
    this.archiveId = archiveId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isAvailableForSale() {
    return availableForSale;
  }

  public void setAvailableForSale(boolean availableForSale) {
    this.availableForSale = availableForSale;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getCountSales() {
    return countSales;
  }

  public void setCountSales(long countSales) {
    this.countSales = countSales;
  }
}
