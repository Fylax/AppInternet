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
  private boolean availableForSale;
  private long timestamp;
  private long countSales;

  public Archive() {
  }

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


  public void setAvailableForSale(boolean availableForSale) {
    this.availableForSale = availableForSale;
  }

  public String getUserName() {
    return userName;
  }
}
