package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Archive {

  @Id
  private ObjectId archiveId;
  private String userName;
  private long userId;
  private boolean availableForSale;
  private long timestamp;
  private long countSales;

  public Archive() {
  }

  public Archive(ObjectId archiveId, String userName, long userId, boolean availableForSale, long timestamp, long countSales) {
    this.archiveId = archiveId;
    this.userName = userName;
    this.userId = userId;
    this.availableForSale = availableForSale;
    this.timestamp = timestamp;
    this.countSales = countSales;
  }

  public Archive(String userName, long userId, boolean availableForSale, long timestamp, long countSales) {
    this.userName = userName;
    this.userId = userId;
    this.availableForSale = availableForSale;
    this.timestamp = timestamp;
    this.countSales = countSales;
  }

  public String getArchiveId() {
    return archiveId.toString();
  }


  public void setAvailableForSale(boolean availableForSale) {
    this.availableForSale = availableForSale;
  }

  public long getUserId() {
    return userId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getCountSales() {
    return countSales;
  }

  public void setCountSales(){
    this.countSales++;
  }
  @JsonIgnore
  public boolean getAvailableForSale(){
    return this.availableForSale;
  }
}
