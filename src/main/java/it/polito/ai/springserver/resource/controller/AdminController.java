package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  private UserRepositoryInterface userRepositoryInterface;

  @GetMapping("/users")
  public ResponseEntity<ResourceSupport> getUsers(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "id");
    List<User> users = userRepositoryInterface.findAllByUserRolesContaining(Role.ROLE_USER, pageRequest);
    if (users.size() == 0)
      return new ResponseEntity<>(new ResourceSupport(), HttpStatus.NOT_FOUND);
    ResourceSupport resource = new ResourceSupport();
    for (User u : users) {
      Link link = linkTo(methodOn(UserPositionsController.class)
              .getPositions(u.getId(), null, null))
              .withRel("user" + u.getId());
      resource.add(link);
    }
    Link next = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("next");
    resource.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("prev");
      resource.add(prev);
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @GetMapping("/customers")
  public ResponseEntity<ResourceSupport> getCustomers(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "id");
    List<User> users = userRepositoryInterface.findAllByUserRolesContaining(Role.ROLE_CUSTOMER, pageRequest);
    if (users.size() == 0)
      return new ResponseEntity<>(new ResourceSupport(), HttpStatus.NOT_FOUND);
    ResourceSupport resource = new ResourceSupport();
    for (User u : users) {
      Link link = linkTo(methodOn(CustomerPositionsController.class)
              .getCustomerPurchases(u.getId(), null, null))
              .withRel("customer" + u.getId());
      resource.add(link);
    }
    Link next = linkTo(methodOn(this.getClass()).getCustomers(page + 1, limit)).withRel("next");
    resource.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getCustomers(page - 1, limit)).withRel("prev");
      resource.add(prev);
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
