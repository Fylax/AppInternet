package it.polito.ai.springserver.resource.model;

import it.polito.ai.springserver.resource.model.repository.PurchaseRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class TransactionManagerComponent {

  @Autowired
  private PurchaseRepositoryInterface purchaseRepositoryInterface;

  @Async
  public void asyncTransactionManager(Purchase currPurchase) {
    try {
      if (asyncMethodWithReturnType().get()) {
        currPurchase.setStatus(TransactionStatus.COMPLETED);
        currPurchase.setAmount(currPurchase.getCountPosition() /** *IOTA_UNIT_VALUE **/);
        purchaseRepositoryInterface.save(currPurchase);
      }//else{...}
    } catch (InterruptedException | ExecutionException e) {
      currPurchase.setStatus(TransactionStatus.FAILED);
      currPurchase.setAmount(0.0);
      purchaseRepositoryInterface.save(currPurchase);
      //nel momento in cui ci sarà una vera transazione gestiremo l'eccezione...
    }
  }

  @Async
  public Future<Boolean> asyncMethodWithReturnType() {
    System.out.println("I'm going to validate transaction...");
    //TODO verrà contattato IOTA per la validazione della transazione...
    return new AsyncResult<>(true);
  }
}
