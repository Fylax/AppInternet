package it.polito.ai.springserver.resource.model;


public class PurchaseSummary extends Purchase {
  private int countPosition;

  public PurchaseSummary(long customerid, long timestamp, long start, long end, int countPosition) {
    super(customerid, timestamp, start, end, countPosition * 1d); // TODO conversione in IOTA
    this.countPosition = countPosition;
  }

  public int getCountPosition() {
    return countPosition;
  }

}
