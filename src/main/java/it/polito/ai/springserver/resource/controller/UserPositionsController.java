package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.PositionManager;
import it.polito.ai.springserver.resource.model.Positions;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("positions/user")
public class UserPositionsController {

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private UserId userId;

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Resource<Positions>> getPositions(
      @PathVariable(value = "id") Long userId,
      @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
      @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end) {
    List<Position> list = positionRepositoryInterface.findByUseridAndTimestampBetween(userId, start, end);
    Positions positions = new Positions(list);
    Resource<Positions> resource = new Resource<>(positions);
    Link linkSelf = linkTo(methodOn(UserPositionsController.class).getPositions(userId, start, end)).withSelfRel();
    Link linkGetAllUserPositions = linkTo(methodOn(UserPositionsController.class)
            .getPositions(userId, null, null)).withRel("all");
    resource.add(linkSelf, linkGetAllUserPositions);
    return new ResponseEntity<>(resource, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Resource<Positions>> getPositions(
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end) {
    long user_id = userId.getUserId();

    List<Position> list = positionRepositoryInterface.findByUseridAndTimestampBetween(user_id, start, end);
    Positions positions = new Positions(list);
    Resource<Positions> resource = new Resource<>(positions);
    Link linkSelf = linkTo(methodOn(UserPositionsController.class).getPositions(start, end)).withSelfRel();
    Link linkGetAll = linkTo(methodOn(UserPositionsController.class)
            .getPositions(null, null)).withRel("all");
    resource.add(linkSelf, linkGetAll);
    return new ResponseEntity<>(resource, new HttpHeaders(), HttpStatus.OK);
  }

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
