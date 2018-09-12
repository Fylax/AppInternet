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

  /**
   * Admin method for retrieving the list of archives for a particular user.
   * @param user_id Path variable showing the userId of interest
   * @param page Page number to be retrieved on the db (optional)
   * @param limit The number of element to be retrieved (optional)
   * @return PaginationSupportClass object containing:
   *        items: list of user archives.
   *        totalElements: total number of archives uploaded by the user of interest
   *        links: links to prev and next page
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

  /**
   * Admin method for retrieving a particular archive for a particular user.
   * @param user_id Path variable showing the userId of interest
   * @param archiveId Archive to be retrieved
   * @return The list of positions in the archive.
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

  /**
   * Method for retrieving the list of user archives
   * @param page Page number to be retrieved on the db (optional)
   * @param limit The number of element to be retrieved (optional)
   * @return PaginationSupportClass object containing:
   *        items: list of user archives.
   *        totalElements: total number of archives uploaded by the user
   *        links: links to prev and next page
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

  /**
   * Method for load an archive in db.
   * @param positions the list of positions in the archive
   * @return ResponseEntity object containing the properly HTTP status code:
   *        201: if the operation terminate successfully
   *        400: if data uploaded are not good
   *        500: if an error occurs
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

  /**
   * Method for deleting a user archive. the method deletes the corresponding approximatedArchive, the original archive
   * is still available for all users have booked it
   * @param archiveId Archive to be deleted
   * @return ResponseEntity object containing the properly HTTP status code:
   *        200: if the operation terminate successfully
   *        403: if the user is not the archive owner
   *        500: if an error occurs
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

  /**
   * Method for retrieving a user archive.
   * @param archiveId the archive to be downloaded.
   * @return ResponseEntity object containing the user archive and the properly HTTP status code:
   *        200: if the operation terminate successfully
   *        500: if an error occurs
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

  /**
   * Method for retrieving the approximated representations of archives satisfying the request
   * @param request UserRequest, encoded in base64, containing a temporal range and polygon vertices
   * @return ResponseEntity object containing the list of approximated archives
   */
  @GetMapping("/approximatedArchives")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApproximatedArchive>> getApproximatedArchives(
          @RequestParam(value = "request") Base64UserRequest request) {
    UserRequest currRequest = request.getCr();
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
