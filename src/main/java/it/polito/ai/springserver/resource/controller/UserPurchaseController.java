package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.resource.model.*;
import it.polito.ai.springserver.resource.model.repository.ApproximatedArchiveRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PurchaseRepositoryInterface;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/positions/customer")
public class UserPurchaseController {

  @Autowired
  private UserId userId;

  @Autowired
  private PurchaseRepositoryInterface purchaseRepositoryInterface;

  @Autowired
  private PositionRepositoryInterface positionRepositoryInterface;

  @Autowired
  private ApproximatedArchiveRepositoryInterface approximatedArchiveRepositoryInterface;

  @Autowired
  private TransactionManagerComponent transactionManagerComponent;

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApproximatedArchive>> getPolygonPositions(
          @RequestParam(value = "request") Base64CustomerRequest request) {
    CustomerRequest currRequest = request.getCr();
    String username = userId.getUsername();
    List<Position> positions = positionRepositoryInterface.findByPointWithinAndTimestampBetween(
            currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
    Set<ObjectId> archiveIds = new HashSet<>();
    for(Position p: positions){
      archiveIds.add(p.getArchiveId());
    }
    List<ApproximatedArchive> approximatedArchives = new ArrayList<>();
    for(ObjectId id : archiveIds){
      ApproximatedArchive a = approximatedArchiveRepositoryInterface.findByArchiveIdAndUsernameNot(id, username);
      if (a != null) {
        approximatedArchives.add(a);
      }
    }
    return new ResponseEntity<>(approximatedArchives, HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity bookPositions(@RequestBody CustomerRequest currRequest) throws InterruptedException {
    long user_id = userId.getUserId();
    var positions = purchaseRepositoryInterface.
            findPurchasable(user_id, currRequest.getPolygon(), currRequest.getStart(), currRequest.getEnd());
    if (positions.size() != 0) {
      PurchaseDetailed purchase = new PurchaseDetailed(user_id, System.currentTimeMillis() / 1000,
              currRequest.getStart(), currRequest.getEnd(), positions);
      var currPurchase = purchaseRepositoryInterface.save(purchase);
      transactionManagerComponent.asyncTransactionManager(currPurchase);

      Link link = linkTo(methodOn(this.getClass()).getPurchase(currPurchase.getId())).withSelfRel();
      HttpHeaders header = new HttpHeaders();
      header.setLocation(URI.create(link.getHref()));

      return new ResponseEntity<>(header, HttpStatus.CREATED);
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "{id}/purchase")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaginationSupportClass> getCustomerPurchases(
          @PathVariable("id") Long customerId,
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end,
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    List<PurchaseDetailed> purchaseList = purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(
            customerId, start, end, pageRequest);
    int totalElements = purchaseRepositoryInterface.countByCustomerid(customerId);
    List<Resource> resourceList = new ArrayList<>(purchaseList.size());
    for (PurchaseDetailed pd : purchaseList) {
      Resource<PurchaseSummary> resource = new Resource<>(pd.getSummary());
      Link link = linkTo(methodOn(this.getClass())
              .getCustomerPurchase(customerId, pd.getId()))
              .withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getCustomerPurchases(customerId, start, end, page + 1, limit))
            .withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getCustomerPurchases(customerId, start, end, page - 1, limit))
              .withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  @GetMapping(value = "{id}/purchase/{purchaseId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity getCustomerPurchase(@PathVariable("id") Long customerId,
                                            @PathVariable("purchaseId") String purchaseId) {
    PurchaseDetailed purchase = purchaseRepositoryInterface.findOne(purchaseId);
    if (customerId != null && purchase != null && customerId == purchase.getCustomerid()) {
      return new ResponseEntity<>(purchase, HttpStatus.OK);
    }
    return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/purchase")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<PaginationSupportClass> getPurchases(
          @RequestParam(value = "start", required = false, defaultValue = Long.MIN_VALUE + "") Long start,
          @RequestParam(value = "end", required = false, defaultValue = Long.MAX_VALUE + "") Long end,
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
    long customer_id = userId.getUserId();
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "timestamp");
    List<PurchaseDetailed> purchaseList =
            purchaseRepositoryInterface.findByCustomeridAndTimestampBetween(customer_id, start, end, pageRequest);
    int totalElements = purchaseRepositoryInterface.countByCustomerid(customer_id);
    List<Resource> resourceList = new ArrayList<>(purchaseList.size());
    for (PurchaseDetailed pd : purchaseList) {
      Resource<PurchaseSummary> resource = new Resource<>(pd.getSummary());
      Link link = linkTo(methodOn(this.getClass()).getPurchase(pd.getId())).withSelfRel();
      resource.add(link);
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getPurchases(start, end, page + 1, limit)).withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getPurchases(start, end, page - 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  @GetMapping(value = "/purchase/{id}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity getPurchase(@PathVariable String id) {
    long customer_id = userId.getUserId();
    PurchaseDetailed purchase = purchaseRepositoryInterface.findOne(id);
    if (purchase != null && customer_id == purchase.getCustomerid()) {
      return new ResponseEntity<>(purchase, HttpStatus.OK);
    }
    return new ResponseEntity(HttpStatus.FORBIDDEN);
  }

}