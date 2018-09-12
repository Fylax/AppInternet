package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a purchased archive by a user. Each purchasedArchive is linked to the user who buy the archive.
 */
@Document
public class PurchasedArchive {

  @Id
  private ObjectId purchaseId;
  private String archiveId;
  private String owner;
  private Long userId; //who buy this archive
  private Long timestamp;
  private Double amount;

  @Transient
  private boolean purchased;

  public PurchasedArchive() {}

  public PurchasedArchive(ApproximatedArchive approximatedArchive, Long userId){
    this.archiveId = approximatedArchive.getArchiveId();
    this.owner = approximatedArchive.getUsername();
    this.timestamp = System.currentTimeMillis() / 1000;
    this.amount = approximatedArchive.getAmount();
    this.userId = userId;
    this.purchased = false;
  }


  public PurchasedArchive(String archiveId, Long userId, Long timestamp, Double amount) {
    this.archiveId = archiveId;
    this.userId = userId;
    this.timestamp = timestamp;
    this.amount = amount;
  }

  @JsonIgnore
  public ObjectId getPurchaseId() {
    return purchaseId;
  }

  public void setPurchaseId(ObjectId purchaseId) {
    this.purchaseId = purchaseId;
  }

  public String getArchiveId() {
    return archiveId;
  }

  public void setArchiveId(String archiveId) {
    this.archiveId = archiveId;
  }

  @JsonIgnore
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public boolean isPurchased() {
    return purchased;
  }

  public void setPurchased(boolean purchased) {
    this.purchased = purchased;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
