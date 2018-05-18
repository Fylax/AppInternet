package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.geojson.Geometry;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.Purchase;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepository;
import javassist.tools.web.BadHttpRequest;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.client.model.geojson.Polygon;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

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

    long countPositions = 0;
    CustomerRequest currRequest = null;
    try {
      currRequest = (new ObjectMapper()).readValue((new String(Base64.decode(params))), CustomerRequest.class);
      countPositions = positionRepository.countPositionByPointIsWithinAndTimestampBetween(
          currRequest.getPolygon(), 1, 100);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return new ResponseEntity(Long.toString(countPositions), new HttpHeaders(), HttpStatus.FOUND);
  }

  @PostMapping
  //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  public ResponseEntity<List<Position>> bookPositions(@RequestBody CustomerRequest currRequest, OAuth2Authentication authentication) {
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
    Map<String, Object> claims = accessToken.getAdditionalInformation();
    long user_id = Long.valueOf((String)claims.get("user_id"));

    List<Purchase>  purchaseList = new ArrayList<>();
    List<Position> positions = new ArrayList<>();
    try {
        positions = positionRepository.findByPointWithinAndTimestampBetween(
              currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
        purchaseList = purchaseRepository.findAllByCustomerid(user_id);
      for (Purchase p : purchaseList) {
        if(positions.size() != 0) {
          List<Position> positionsPurchased = p.getPositions();
          positions.removeAll(positionsPurchased);
        }
      }
      Purchase purchase = new Purchase(user_id, System.currentTimeMillis(), currRequest.getStart(), currRequest.getEnd(), positions);
      purchaseRepository.save(purchase);
    } catch (Exception e) {
        return ResponseEntity.unprocessableEntity().build();
    }
    return new ResponseEntity(positions, new HttpHeaders(), HttpStatus.CREATED);
  }

}
/*
        //Extracting user_id from JWT... was it put???
        Map<String, Object> additionalInformation = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        long user_id = (long) additionalInformation.get("user_id");

        Long listPos = null;
        //System.out.println(myCust.getPolygon().toString());

 */