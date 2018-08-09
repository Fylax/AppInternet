package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.*;
import it.polito.ai.springserver.resource.model.repository.ApproximatedArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.ArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("positions/user")
public class UserPositionsController {

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private ArchiveRepositoryInterface archiveRepositoryInterface;

  @Autowired
  private ApproximatedArchiveRepositoryInterface approximatedArchiveRepositoryInterface;

  @Autowired
  private UserId userId;

//  @GetMapping(value = "/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
//  public ResponseEntity<Resource<Positions>> getPositions(
//          @PathVariable(value = "id") Long userId,
//          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
//          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end) {
//    List<Position> list = positionRepositoryInterface.findByUseridAndTimestampBetween(userId, start, end);
//    Positions positions = new Positions(list);
//    Resource<Positions> resource = new Resource<>(positions);
//    Link linkSelf = linkTo(methodOn(UserPositionsController.class).getPositions(userId, start, end)).withSelfRel();
//    Link linkGetAllUserPositions = linkTo(methodOn(UserPositionsController.class)
//            .getPositions(userId, null, null)).withRel("all");
//    resource.add(linkSelf, linkGetAllUserPositions);
//    return new ResponseEntity<>(resource, new HttpHeaders(), HttpStatus.OK);
//  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<PaginationSupportClass> getArchives(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit){
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    String user_name = userId.getUsername();
    List<Archive> archives = archiveRepositoryInterface.findByUserName(user_name,pageRequest);
    int totalElements = archiveRepositoryInterface.countByUserName(user_name);
    List<Resource> resourceList = new ArrayList<>(archives.size());
    for (Archive a: archives) {
      Resource<Archive> resource = new Resource<>(a);
      Link link = linkTo(methodOn(this.getClass()).getArchive(a.getArchiveId().toString())).withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getArchives(page + 1, limit)).withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getArchives(page - 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity addArchive(@RequestBody Positions positions) {
    long user_id = userId.getUserId();
    String user_name = userId.getUsername();
    try {
      Archive archive = new Archive(user_name, true, (System.currentTimeMillis() / 1000), 0);
      archive = archiveRepositoryInterface.save(archive);
      var positionManager = new PositionManager(user_id, positionRepositoryInterface);
      for (Position position : positions.getPositionList()) {
        position.setUserid(user_id);
        position.setUsername(user_name);
        position.setArchiveId(archive.getArchiveId());
        if (!positionManager.checkPositionValidity(position)) {
          archiveRepositoryInterface.delete(archive);
          return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
      }
      approximatedArchiveRepositoryInterface.save(
              new ApproximatedArchive(archive.getArchiveId(), user_name, positions.getPositionList()));
      positionRepositoryInterface.save(positions.getPositionList());
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

    }
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{archiveId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity deleteArchive(@PathVariable(value = "archiveId") String archiveId) {
    String user_name = userId.getUsername();
    try {
      Archive archive = archiveRepositoryInterface.findByArchiveId(new ObjectId(archiveId));
      if (!archive.getUserName().equals(user_name)) {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
      }
      archive.setAvailableForSale(false);
      ApproximatedArchive apArchive = approximatedArchiveRepositoryInterface.findByArchiveId(new ObjectId(archiveId));
      approximatedArchiveRepositoryInterface.delete(apArchive);
      archiveRepositoryInterface.save(archive);
    } catch (Exception e) {
      return new ResponseEntity((HttpStatus.INTERNAL_SERVER_ERROR));
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/{archiveId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Positions> getArchive(
          @PathVariable(value = "archiveId") String archiveId) {
//    String user_name = userId.getUsername();
    List<Position> positionList = positionRepositoryInterface.findByArchiveId(new ObjectId(archiveId));
    Positions positions = new Positions(positionList);
    return new ResponseEntity<>(positions, HttpStatus.OK);
  }
}
