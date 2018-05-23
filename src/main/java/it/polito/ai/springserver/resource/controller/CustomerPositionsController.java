package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.*;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/positions/customer")
public class CustomerPositionsController {

  @Autowired
  private UserId userId;

  @Autowired
  private PurchaseRepositoryInterface purchaseRepositoryInterface;

  @Autowired
  private TransactionManagerComponent transactionManagerComponent;

  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity getPolygonPositions(@RequestParam(value = "request") Base64CustomerRequest request) {
    long customer_id = userId.getUserId();
    CustomerRequest currRequest = request.getCr();
    long countPositions = purchaseRepositoryInterface.
            countPurchasable(customer_id, currRequest.getPolygon(),
                    currRequest.getStart(), currRequest.getEnd());
    return new ResponseEntity<>(Long.toString(countPositions), new HttpHeaders(), HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity bookPositions(@RequestBody CustomerRequest currRequest) {

    long customer_id = userId.getUserId();
    var positions = purchaseRepositoryInterface.
            findPurchasable(customer_id, currRequest.getPolygon(),
                    currRequest.getStart(), currRequest.getEnd());
    if (positions.size() != 0) {
      PurchaseDetailed purchase = new PurchaseDetailed(customer_id, System.currentTimeMillis(),
              currRequest.getStart(), currRequest.getEnd(), positions);
      var currPurchase = purchaseRepositoryInterface.save(purchase);
      transactionManagerComponent.asyncTransactionManager(currPurchase);

      Link link = linkTo(methodOn(this.getClass()).getPurchase(currPurchase.getId())).withSelfRel();
      HttpHeaders header = new HttpHeaders();
      header.setLocation(URI.create(link.getHref()));

      return new ResponseEntity<>(header, HttpStatus.CREATED);
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "{id}/purchase")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Resource<PurchaseSummary>>> getCustomerPurchases(
          @PathVariable("id") Long customerId,
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end) {
    List<PurchaseDetailed> purchaseList = purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(
            customerId, start, end);
    List<Resource<PurchaseSummary>> resourceList = new ArrayList<>(purchaseList.size());
    for(PurchaseDetailed pd : purchaseList){
      Resource<PurchaseSummary> resource = new Resource<>(pd.getSummary());
      Link link = linkTo(methodOn(this.getClass())
              .getCustomerPurchase(customerId, pd.getId()))
              .withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    return new ResponseEntity<>(resourceList, HttpStatus.OK);
  }

  @GetMapping(value = "{id}/purchase/{purchaseId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity getCustomerPurchase(@PathVariable("id") Long customerId, @PathVariable("purchaseId") String purchaseId) {
    PurchaseDetailed purchase = purchaseRepositoryInterface.findOne(purchaseId);
    if (customerId != null && customerId == purchase.getCustomerid()) {
      return new ResponseEntity<>(purchase, HttpStatus.OK);
    }
    return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/purchase")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<Resource<PurchaseSummary>>> getPurchases(
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end) {
    long customer_id = userId.getUserId();
    List<PurchaseDetailed> purchaseList = purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(
            customer_id, start, end);
    List<Resource<PurchaseSummary>> resourceList = new ArrayList<>(purchaseList.size());
    for(PurchaseDetailed pd : purchaseList){
      Resource<PurchaseSummary> resource = new Resource<>(pd.getSummary());
      Link link = linkTo(methodOn(this.getClass()).getPurchase(pd.getId())).withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    return new ResponseEntity<>(resourceList, HttpStatus.OK);
  }

  @GetMapping(value = "/purchase/{id}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity getPurchase(@PathVariable String id) {
    long customer_id = userId.getUserId();
    PurchaseDetailed purchase = purchaseRepositoryInterface.findOne(id);
    if (customer_id == purchase.getCustomerid()) {
      return new ResponseEntity<>(purchase, HttpStatus.OK);
    }
    return new ResponseEntity(HttpStatus.FORBIDDEN);
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