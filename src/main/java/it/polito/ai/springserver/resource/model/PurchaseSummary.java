package it.polito.ai.springserver.resource.model;


import org.bson.types.ObjectId;

public class PurchaseSummary extends Purchase {
  private int countPosition;
  private String purchaseId;

  public PurchaseSummary(ObjectId id, long customerid, long timestamp, long start, long end, int countPosition, TransactionStatus status) {
    super(customerid, timestamp, start, end, countPosition * 1d); // TODO conversione in IOTA
    this.countPosition = countPosition;
    this.purchaseId = String.valueOf(id);
    this.status = status;
  }

  public int getCountPosition() {
    return countPosition;
  }

  public String getPurchaseId() {
    return this.purchaseId;
  }
}
