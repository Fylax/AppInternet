package it.polito.ai.springserver.resource.model;


public class PurchaseSummary extends Purchase {
  private int countPosition;

  public PurchaseSummary(long customerid, long timestamp, long start, long end, int countPosition, TransactionStatus status) {
    super(customerid, timestamp, start, end, countPosition * 1d); // TODO conversione in IOTA
    this.countPosition = countPosition;
    this.status = status;
  }

  public int getCountPosition() {
    return countPosition;
  }

}
