package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.PositionManager;
import it.polito.ai.springserver.resource.model.Positions;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("positions/user")
public class UserPositionsController {

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private UserId userId;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Position>> getPositions(
      @PathVariable(value = "id") long userId, @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
      @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end) {
    List<Position> list = positionRepositoryInterface.findByUseridAndTimestampBetween(userId, start, end);
    return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<Position>> getPositions(@RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") long start,
                                                     @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") long end) {
    long user_id = userId.getUserId();

    List<Position> list = positionRepositoryInterface.findByUseridAndTimestampBetween(user_id, start, end);
    return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
  }

  //this method does't work, maybe is necessary a deserializer
  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity addPositions(@RequestBody Positions positions) {
    long user_id = userId.getUserId();
    try {
      var positionManager = new PositionManager(user_id, positionRepositoryInterface);
      for (Position position : positions.getPositionList()) {
        position.setUserid(user_id);
        if (!positionManager.checkPositionValidity(position)) {
          return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
      }
      positionRepositoryInterface.save(positions.getPositionList());
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    return new ResponseEntity(HttpStatus.CREATED);
  }


}
