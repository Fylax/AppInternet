package it.polito.ai.springserver.resource.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.ai.springserver.resource.model.*;
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
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/userPurchasedArchives")
public class UserPurchaseController {

  @Autowired
  private UserId userId;

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private PurchasedArchiveRepositoryInterface purchasedArchiveRepositoryInterface;

  @Autowired
  private ArchiveRepositoryInterface archiveRepositoryInterface;


  /**
   * Method for book a list of archives.
   *
   * @param archives The list of approximated archives to book
   * @return ResponseEntity object containing the properly HTTP status code:
   * 201: if the operation terminate successfully
   * 500: if an error occurs
   */
  @ApiOperation(value = "Book a list of archives")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Operation terminated successfully"),
          @ApiResponse(code = 401, message = "User unauthorized"),
          @ApiResponse(code = 404, message = "Archives not found"),
          @ApiResponse(code = 500, message = "An error occur on server. Try again")
  })
  @PostMapping(value = "purchasedArchives", consumes = "application/json")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity bookPositions(@RequestBody List<ApproximatedArchive> archives) {
    long user_id = userId.getUserId();
    for (ApproximatedArchive a : archives) {
      if (!archiveRepositoryInterface.existsArchiveByArchiveId(new ObjectId(a.getArchiveId()))) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      }
    }
    for (ApproximatedArchive a : archives) {
      if (archiveRepositoryInterface.findByArchiveId(new ObjectId(a.getArchiveId()))
              .getAvailableForSale()) {
        purchasedArchiveRepositoryInterface.save(new PurchasedArchive(a, user_id));
      }
    }
    return new ResponseEntity(HttpStatus.CREATED);
  }

  /**
   * Admin method for retrieving the list of purchased archives for a particular user.
   *
   * @param user_id Path variable showing the userId of interest
   * @param page    Page number to be retrieved on the db (optional)
   * @param limit   The number of element to be retrieved (optional)
   * @return PaginationSupportClass object containing:
   * items: list of user purchased archives.
   * totalElements: total number of archives booked by the user of interest
   * links: links to prev and next page
   */
  @ApiOperation(value = "Admin endpoint for retrieving the list of purchased archives for a particular user")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code = 401, message = "User unauthorized"),
          @ApiResponse(code = 404, message = "User not found"),
          @ApiResponse(code = 500, message = "An error occur on server. Try again")
  })
  @GetMapping(value = "{id}/purchasedArchives", produces = "application/json")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaginationSupportClass> getUserPurchasedArchives(
          @PathVariable("id") Long user_id,
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    List<PurchasedArchive> purchasedArchiveList =
            purchasedArchiveRepositoryInterface.findByUserId(user_id, pageRequest);
    int totalElements = purchasedArchiveRepositoryInterface.countByUserId(user_id);
    List<Resource> resourceList = new ArrayList<>(purchasedArchiveList.size());
    for (PurchasedArchive pd : purchasedArchiveList) {
      Resource<PurchasedArchive> resource = new Resource<>(pd);
      Link link = linkTo(methodOn(this.getClass())
              .getUserPurchasedArchive(user_id, pd.getArchiveId()))
              .withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getUserPurchasedArchives(user_id, page + 1, limit))
            .withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getUserPurchasedArchives(user_id, page - 1, limit))
              .withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }


  /**
   * Method for retrieving the list of user purchased archives
   *
   * @param page  Page number to be retrieved on the db (optional)
   * @param limit The number of element to be retrieved (optional)
   * @return PaginationSupportClass object containing:
   * items: list of user  purchased archives.
   * totalElements: total number of archives booked by the user
   * links: links to prev and next page
   */
  @ApiOperation(value = "Endpoint for retrieving the list of user purchased archives")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code = 401, message = "User unauthorized"),
          @ApiResponse(code = 500, message = "An error occur on server. Try again")
  })
  @GetMapping(value = "purchasedArchives", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<PaginationSupportClass> getPurchasedArchives(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    long user_id = userId.getUserId();
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    List<PurchasedArchive> purchasedArchiveList =
            purchasedArchiveRepositoryInterface.findByUserId(user_id, pageRequest);
    int totalElements = purchasedArchiveRepositoryInterface.countByUserId(user_id);
    List<Resource> resourceList = new ArrayList<>(purchasedArchiveList.size());
    for (PurchasedArchive pd : purchasedArchiveList) {
      Resource<PurchasedArchive> resource = new Resource<>(pd);
      Link link = linkTo(methodOn(this.getClass()).getPurchasedArchive(pd.getArchiveId()))
              .withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getPurchasedArchives(page + 1, limit)).withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getPurchasedArchives(page - 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);

  }

  /**
   * Admin method for retrieving a particular purchased archive for a particular user.
   *
   * @param user_id   Path variable showing the userId of interest
   * @param archiveId Archive to be retrieved
   * @return The list of positions in the archive.
   */
  @ApiOperation(value = "Admin endpoint for retrieving a particular purchased archive for a particular user")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code = 401, message = "User unauthorized"),
          @ApiResponse(code = 404, message = "User or archive not found"),
          @ApiResponse(code = 500, message = "An error occur on server. Try again")
  })
  @GetMapping(value = "{id}/purchasedArchives/{archiveId}", produces = "application/json")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Positions> getUserPurchasedArchive(
          @PathVariable("id") Long user_id,
          @PathVariable(value = "archiveId") String archiveId) {
    Positions positions;
    try {
      List<Position> positionList = positionRepositoryInterface.findByUseridAndArchiveId(
              user_id, new ObjectId(archiveId));
      if (positionList == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      positions = new Positions(positionList);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(positions, HttpStatus.OK);
  }

  /**
   * Method for retrieving a user purchased archive.
   *
   * @param archiveId the archive to be downloaded.
   * @return ResponseEntity object containing the user purchased archive and the properly HTTP status code:
   * 200: if the operation terminate successfully
   * 500: if an error occurs
   */
  @ApiOperation(value = "Endpoint for retrieving a user purchased archive")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code = 401, message = "User unauthorized"),
          @ApiResponse(code = 403, message = "You can't access this resource"),
          @ApiResponse(code = 404, message = "Archive not found"),
          @ApiResponse(code = 500, message = "An error occur on server. Try again")
  })
  @GetMapping(value = "purchasedArchives/{archiveId}", produces = "application/json")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Positions> getPurchasedArchive(
          @PathVariable(value = "archiveId") String archiveId) {
    long user_id = userId.getUserId();
    if (purchasedArchiveRepositoryInterface.existsByArchiveIdAndUserId(archiveId, user_id)) {
      Positions positions;
      try {
        List<Position> positionList = positionRepositoryInterface.findByUseridAndArchiveId(
                user_id, new ObjectId(archiveId));
        if (positionList == null) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        positions = new Positions(positionList);
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(positions, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

}
