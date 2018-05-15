package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import it.polito.ai.springserver.authorization.model.UserDetailsImpl;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.Security;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/positions")
public class PositionsController {

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private PositionRepository positionRepository;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
  public String getPolygonPositions(@RequestParam(value = "geoJson", required = false) String geoJson) {

    //Extracting user_id from JWT... was it put???
    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
    Map<String, Object> claims = accessToken.getAdditionalInformation();
    //long user_id = (long) additionalInformation.get("user_id");
    System.out.println(claims.get("user_id"));
    List<Position> listPos = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      //CustomerRequest myCust = objectMapper.readValue(geoJson, CustomerRequest.class);
      //myCust.setUserid(user_id);
      //listPos = positionRepository.findByPositionWithinAndTimestampBetween(myCust.getPolygon());
      //System.out.println(myCust.getPolygon().toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  @PostMapping
  //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
  public String bookPositions() {
    return "addPositionsController";
  }


}
