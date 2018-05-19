package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import it.polito.ai.springserver.resource.security.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("positions/user")
public class UserPositionsController {

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private UserId userId;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Position>> getPositions(
          @PathVariable(value = "id") long userId, @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end){
    List<Position> list = positionRepository.findByUseridAndTimestampBetween(userId, start, end);
    return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<Position>> getPositions(@RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
                             @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end){
    long user_id = userId.getUserId();

    List<Position> list = positionRepository.findByUseridAndTimestampBetween(user_id, start, end);
    return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
  }

  //this method does't work, maybe is necessary a deserializer
  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity addPositions(@RequestBody List<Position> positions){
    long user_id = userId.getUserId();

    for(Position p : positions){
      p.setUserid(user_id);
    }
    positionRepository.save(positions);

    return new ResponseEntity(HttpStatus.CREATED);
  }





}
