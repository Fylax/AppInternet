package it.polito.ai.springserver.resource.model;

public abstract class Purchase {
  protected long customerid;
  protected long timestamp;
  protected long start;
  protected long end;
  protected TransactionStatus status;
  protected double amount;

  public Purchase(long customerid, long timestamp, long start, long end, double amount) {
    this.customerid = customerid;
    this.timestamp = timestamp;
    this.start = start;
    this.end = end;
    this.status = TransactionStatus.PENDING;
    this.amount = amount;
  }

  public long getCustomerid() {
    return customerid;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getStart() {
    return start;
  }

  public long getEnd() {
    return end;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public double getAmount() {
    return amount;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

}
