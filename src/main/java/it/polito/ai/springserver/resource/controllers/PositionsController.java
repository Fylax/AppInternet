package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.Purchase;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepository;
import javassist.tools.web.BadHttpRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/positions")
public class PositionsController {

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private PurchaseRepository purchaseRepository;

  @GetMapping
  //@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
  public ResponseEntity<String> getPolygonPositions(@RequestParam(value = "geoJson", required = false) String params) throws BadHttpRequest {
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
    Map<String, Object> claims = accessToken.getAdditionalInformation();
    long customer_id = Long.valueOf((String) claims.get("user_id"));
    long countPositions = 0;
    CustomerRequest currRequest = null;
    try {
      currRequest = (new ObjectMapper()).readValue((new String(Base64.decode(params))), CustomerRequest.class);
      List<Position> positions = positionRepository.findByPointWithinAndTimestampBetween(
              currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
      List<Purchase> purchaseList = purchaseRepository.findByCustomeridAndStartBeforeAndEndAfter(customer_id,
              currRequest.getEnd(), currRequest.getStart());
      countPositions = countPositions(positions, purchaseList);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return new ResponseEntity(Long.toString(countPositions), new HttpHeaders(), HttpStatus.FOUND);
  }

  @PostMapping
  //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<List<Position>> bookPositions(@RequestBody CustomerRequest currRequest) {
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
    Map<String, Object> claims = accessToken.getAdditionalInformation();
    long customer_id = Long.valueOf((String) claims.get("user_id"));

    List<Purchase> purchaseList = new ArrayList<>();
    List<Position> positions = new ArrayList<>();
    try {
      positions = positionRepository.findByPointWithinAndTimestampBetween(
              currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
      purchaseList = purchaseRepository.findByCustomeridAndStartBeforeAndEndAfter(customer_id,
              currRequest.getEnd(), currRequest.getStart());
      positions = getPositionsToBuy(positions, purchaseList);
      if(positions.size() != 0) {
        Purchase purchase = new Purchase(customer_id, System.currentTimeMillis(), currRequest.getStart(), currRequest.getEnd(), positions);
        purchaseRepository.save(purchase);
      }
    } catch (Exception e) {
      return ResponseEntity.unprocessableEntity().build();
    }
    return new ResponseEntity(positions, new HttpHeaders(), HttpStatus.CREATED);
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