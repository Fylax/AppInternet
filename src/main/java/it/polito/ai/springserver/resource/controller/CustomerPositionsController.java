package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.*;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping(value = "/positions/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerPositionsController {

  @Autowired
  private UserId userId;

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private PurchaseRepositoryInterface purchaseRepositoryInterface;

  @Autowired
  private TransactionManagerComponent transactionManagerComponent;

  @GetMapping
  public ResponseEntity getPolygonPositions(@RequestParam(value = "request") Base64CustomerRequest request) {
    long customer_id = userId.getUserId();
    CustomerRequest currRequest = request.getCr();
    long countPositions = purchaseRepositoryInterface.
            countPurchasable(customer_id, currRequest.getPolygon(),
                    currRequest.getStart(), currRequest.getEnd());
    return new ResponseEntity<>(Long.toString(countPositions), new HttpHeaders(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity bookPositions(@RequestBody CustomerRequest currRequest) {

    try {
      long customer_id = userId.getUserId();
      var positions = purchaseRepositoryInterface.
              findPurchasable(customer_id, currRequest.getPolygon(),
                      currRequest.getStart(), currRequest.getEnd());
      if (positions.size() != 0) {
        PurchaseDetailed purchase = new PurchaseDetailed(customer_id, System.currentTimeMillis(),
                                                         currRequest.getStart(), currRequest.getEnd(), positions);
        var currPurchase = purchaseRepositoryInterface.save(purchase);
        transactionManagerComponent.asyncTransactionManager(currPurchase);

        return new ResponseEntity<>(positions, new HttpHeaders(), HttpStatus.CREATED); // TODO remove body, add location header
      }
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return ResponseEntity.unprocessableEntity().build();
    }
  }

  @RequestMapping(value = "/purchase", method = RequestMethod.GET )
  public ResponseEntity<List<PurchaseSummary>> getPurchases(
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end) {

    long customer_id = userId.getUserId();
    List<PurchaseDetailed> purchaseList = purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(
            customer_id, start, end);
    List<PurchaseSummary> summaries = new ArrayList<>(purchaseList.size());
    purchaseList.forEach(p -> summaries.add(p.getSummary()));
    return new ResponseEntity<>(summaries, HttpStatus.OK);
  }

  @Async
  public CompletableFuture<Boolean> veryLongMethod() throws InterruptedException  {

    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return CompletableFuture.completedFuture(true);
  }
}