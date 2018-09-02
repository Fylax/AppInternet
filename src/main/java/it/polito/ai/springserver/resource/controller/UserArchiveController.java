package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.*;
import it.polito.ai.springserver.resource.model.repository.ApproximatedArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.ArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PurchasedArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.security.UserId;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("userArchives")
public class UserArchiveController {

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private ArchiveRepositoryInterface archiveRepositoryInterface;

  @Autowired
  private ApproximatedArchiveRepositoryInterface approximatedArchiveRepositoryInterface;

  @Autowired
  private UserId userId;

  @Autowired
  private PurchasedArchiveRepositoryInterface purchasedArchiveRepositoryInterface;

  /*
  this method is used to manage request from admin. The request presents a path variable, {id}, the userId of interest.
  Return the user archives list. The pagination is available by means of two request params, page and limit
  which specify the page and the number of element per page.
   */
  @GetMapping(value = "/{id}/archives")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaginationSupportClass> getUserArchives(
          @PathVariable(value = "id") Long user_id,
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    List<Archive> archives = archiveRepositoryInterface.findByUserIdAndAvailableForSale(user_id,
            true, pageRequest);
    int totalElements = archiveRepositoryInterface.countByUserIdAndAvailableForSale(user_id, true);
    List<Resource> resourceList = new ArrayList<>(archives.size());
    for (Archive a : archives) {
      Resource<Archive> resource = new Resource<>(a);
      Link link = linkTo(methodOn(this.getClass()).getUserArchive(user_id, a.getArchiveId())).
              withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    if (totalElements > limit) {
      Link next = linkTo(methodOn(this.getClass()).getArchives(page + 1, limit)).withRel("next");
      links.add(next);
    }
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getArchives(page - 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  /*
  this method is used to manage request from admin. The request presents two path variable, {id} and {archiveId},
  respectively the userId and the archiveId of interest.
  Return the user archive.
   */
  @GetMapping(value = "/{id}/archives/{archiveId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Positions> getUserArchive(
          @PathVariable(value = "id") Long user_id,
          @PathVariable String archiveId) {
    List<Position> positionList = positionRepositoryInterface.findByUseridAndArchiveId(
            user_id, new ObjectId(archiveId));
    Positions positions = new Positions(positionList);
    return new ResponseEntity<>(positions, HttpStatus.OK);
  }

  /*
  this method is used to manage request from user.
  Return the user archives list (the userId is take from the token).
  The pagination is available by means of two request params, page and limit
  which specify the page and the number of element per page.
  */
  @GetMapping("/archives")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<PaginationSupportClass> getArchives(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    long user_id = userId.getUserId();
    List<Archive> archives = archiveRepositoryInterface.findByUserIdAndAvailableForSale(user_id,
            true, pageRequest);
    int totalElements = archiveRepositoryInterface.countByUserIdAndAvailableForSale(user_id,
            true);
    List<Resource> resourceList = new ArrayList<>(archives.size());
    for (Archive a : archives) {
      Resource<Archive> resource = new Resource<>(a);
      Link link = linkTo(methodOn(this.getClass()).getArchive(a.getArchiveId())).withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    if (totalElements > limit) {
      Link next = linkTo(methodOn(this.getClass()).getArchives(page + 1, limit)).withRel("next");
      links.add(next);
    }
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getArchives(page - 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  /*
   this method is used to manage request from user.
   It saves the archive uploaded by user if all data are good or return a badRequest.
  */
  @PostMapping("/archives")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity addArchive(@RequestBody Positions positions) {
    long user_id = userId.getUserId();
    String user_name = userId.getUsername();
    try {
      Archive archive = new Archive(user_name, user_id,
              true, (System.currentTimeMillis() / 1000), 0);
      archive = archiveRepositoryInterface.save(archive);
      var positionManager = new PositionManager(user_id, positionRepositoryInterface);
      for (Position position : positions.getPositionList()) {
        position.setUserid(user_id);
        position.setUsername(user_name);
        position.setArchiveId(new ObjectId(archive.getArchiveId()));
        if (!positionManager.checkPositionValidity(position)) {
          archiveRepositoryInterface.delete(archive);
          return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
      }
      approximatedArchiveRepositoryInterface.save(
              new ApproximatedArchive(new ObjectId(archive.getArchiveId()),
                      user_name, positions.getPositionList()));
      positionRepositoryInterface.save(positions.getPositionList());
    } catch (Exception e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

    }
    return new ResponseEntity(HttpStatus.CREATED);
  }

  /*
   this method is used to manage request from user. The request presents a path variable, {ArchiveId},
   the archiveId of interest.
   It deletes the specified archive, deleting the approximatedArchive
   (the archive is still available for who have booked it).
  */
  @DeleteMapping(value = "archives/{archiveId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity deleteArchive(@PathVariable(value = "archiveId") String archiveId) {
    long user_id = userId.getUserId();
    try {
      Archive archive = archiveRepositoryInterface.findByArchiveId(new ObjectId(archiveId));
      if (archive.getUserId() != user_id) {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
      }
      archive.setAvailableForSale(false);
      ApproximatedArchive apArchive = approximatedArchiveRepositoryInterface.findByArchiveId(
              new ObjectId(archiveId));
      approximatedArchiveRepositoryInterface.delete(apArchive);
      archiveRepositoryInterface.save(archive);
    } catch (Exception e) {
      return new ResponseEntity((HttpStatus.INTERNAL_SERVER_ERROR));
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  /*
   this method is used to manage request from user. The request presents a path variable, {archiveId},
   the archiveId of interest.
   Return the user archive.
  */
  @GetMapping("archives/{archiveId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Positions> getArchive(
          @PathVariable(value = "archiveId") String archiveId) {
    long user_id = userId.getUserId();
    Positions positions;
    try {
      List<Position> positionList = positionRepositoryInterface.findByUseridAndArchiveId(
              user_id, new ObjectId(archiveId));
      positions = new Positions(positionList);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(positions, HttpStatus.OK);
  }

  @GetMapping("/approximatedArchives")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApproximatedArchive>> getApproximatedArchives(
          @RequestParam(value = "request") Base64CustomerRequest request) {
    CustomerRequest currRequest = request.getCr();
    long user_id = userId.getUserId();
    String username = userId.getUsername();
    List<Position> positions = positionRepositoryInterface.
            findByPointApproximatedWithinAndTimestampBetweenAndUseridNot(currRequest.getPolygon(),
                    currRequest.getStart(), currRequest.getEnd(), user_id);
    Set<ObjectId> archiveIds = new HashSet<>();
    for (Position p : positions) {
      archiveIds.add(p.getArchiveId());
    }
    List<ApproximatedArchive> approximatedArchives = new ArrayList<>();
    List<PurchasedArchive> purchasedArchives =
            purchasedArchiveRepositoryInterface.findByUserId(user_id);
    List<ObjectId> purchasedArchivesId = new ArrayList<>();
    for (PurchasedArchive p : purchasedArchives) {
      purchasedArchivesId.add(new ObjectId(p.getArchiveId()));
    }
    for (ObjectId id : archiveIds) {
      ApproximatedArchive a = approximatedArchiveRepositoryInterface
              .findByArchiveIdAndUsernameNot(id, username);
      if (a != null) {
        if (purchasedArchivesId.contains(id)) {
          a.setPurchased(true);
        }
        approximatedArchives.add(a);
      }
    }
    return new ResponseEntity<>(approximatedArchives, HttpStatus.OK);
  }
}
