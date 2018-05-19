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
  public ResponseEntity getPolygonPositions(@RequestParam(value = "geoJson", required = false) String params) throws BadHttpRequest {
    long customer_id = userId.getUserId();

    long countPositions = 0;
    CustomerRequest currRequest = null;
    try {
      currRequest = (new ObjectMapper()).readValue((new String(Base64.decode(params))), CustomerRequest.class);
      List<Position> positions = positionRepositoryInterface.findByPointWithinAndTimestampBetween(
              currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
      List<Purchase> purchaseList = purchaseRepositoryInterface.findByCustomeridAndStartBeforeAndEndAfter(customer_id,
              currRequest.getEnd(), currRequest.getStart());
      countPositions = countPositions(positions, purchaseList);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return new ResponseEntity<>(Long.toString(countPositions), new HttpHeaders(), HttpStatus.FOUND);
  }

  @PostMapping
  public ResponseEntity bookPositions(@RequestBody CustomerRequest currRequest) {
    long customer_id = userId.getUserId();

    List<Purchase> purchaseList = new ArrayList<>();
    List<Position> positions = new ArrayList<>();
    try {
      positions = positionRepositoryInterface.findByPointWithinAndTimestampBetween(
              currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
      purchaseList = purchaseRepositoryInterface.findByCustomeridAndStartBeforeAndEndAfter(customer_id,
              currRequest.getEnd(), currRequest.getStart());
      positions = getPositionsToBuy(positions, purchaseList);
      if(positions.size() != 0) {
        Purchase purchase = new Purchase(customer_id, System.currentTimeMillis(), currRequest.getStart(), currRequest.getEnd(), positions);
          purchaseRepositoryInterface.save(purchase);
      }
    } catch (Exception e) {
      return ResponseEntity.unprocessableEntity().build();
    }
    return new ResponseEntity<>(positions, new HttpHeaders(), HttpStatus.CREATED);
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