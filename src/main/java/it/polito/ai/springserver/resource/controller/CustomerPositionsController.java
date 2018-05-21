package it.polito.ai.springserver.resource.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.Purchase;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import javassist.tools.web.BadHttpRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

  @GetMapping
  public ResponseEntity getPolygonPositions(@RequestParam(value = "request") String params) {
    try {
      long customer_id = userId.getUserId();
      CustomerRequest currRequest = (new ObjectMapper()).
          readValue((new String(Base64.decode(params))), CustomerRequest.class);
      long countPositions = purchaseRepositoryInterface.
          countPurchasable(customer_id, currRequest.getPolygon(),
                           currRequest.getStart(), currRequest.getEnd());
      return new ResponseEntity<>(Long.toString(countPositions), new HttpHeaders(), HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity bookPositions(@RequestBody CustomerRequest currRequest) {

    try {
      long customer_id = userId.getUserId();
      var positions = purchaseRepositoryInterface.
          findPurchasable(customer_id, currRequest.getPolygon(),
                          currRequest.getStart(), currRequest.getEnd());
      if (positions.size() != 0) {
        Purchase purchase = new Purchase(customer_id, System.currentTimeMillis(), currRequest.getStart(), currRequest.getEnd(), positions);
        purchaseRepositoryInterface.save(purchase);
        return new ResponseEntity<>(positions, new HttpHeaders(), HttpStatus.CREATED); // TODO remove body, add location header
      }
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return ResponseEntity.unprocessableEntity().build();
    }
  }

  @RequestMapping(value = "/purchase", method = RequestMethod.GET )
  public ResponseEntity<List<Purchase>> getPurchases(
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end) {

    long customer_id = userId.getUserId();
    List<Purchase> purchaseList= purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(
            customer_id, start, end);
    return new ResponseEntity<>(purchaseList, HttpStatus.OK);
  }

  private long countPositions(List<Position> positionsInPolygon, List<Purchase> purchaseList) {
    for (Purchase p : purchaseList) {
      if (positionsInPolygon.size() != 0) {
        List<Position> positionsPurchased = p.getPositions();
        positionsInPolygon.removeAll(positionsPurchased);
      } else {
        return 0;
      }
    }
    return positionsInPolygon.size();
  }


  private List<Position> getPositionsToBuy(List<Position> positionsInPolygon, List<Purchase> purchaseList) {
    for (Purchase p : purchaseList) {
      if (positionsInPolygon.size() != 0) {
        List<Position> positionsPurchased = p.getPositions();
        positionsInPolygon.removeAll(positionsPurchased);
      } else {
        return positionsInPolygon;
      }
    }
    return positionsInPolygon;
  }
}