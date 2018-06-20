package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import it.polito.ai.springserver.resource.model.PaginationSupportClass;
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
  public ResponseEntity<PaginationSupportClass> getUsers(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "id");
    List<User> users = userRepositoryInterface.findAllByUserRolesContaining(Role.ROLE_USER, pageRequest);
    if (users.size() == 0)
      return new ResponseEntity<>(new PaginationSupportClass(), HttpStatus.NOT_FOUND);
    int totalElements = userRepositoryInterface.countAllByUserRolesContaining(Role.ROLE_USER);
    List<Resource> resourceList = new ArrayList<>();
    for (User u : users) {
      Resource<Link> resource = new Resource<>(linkTo(methodOn(UserPositionsController.class)
              .getPositions(u.getId(), null, null))
              .withRel(u.getId().toString()));
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }

  @GetMapping("/customers")
  public ResponseEntity<PaginationSupportClass> getCustomers(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 1 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "id");
    List<User> users = userRepositoryInterface.findAllByUserRolesContaining(Role.ROLE_CUSTOMER, pageRequest);
    if (users.size() == 0)
      return new ResponseEntity<>(new PaginationSupportClass(), HttpStatus.NOT_FOUND);
    int totalElements = userRepositoryInterface.countAllByUserRolesContaining(Role.ROLE_CUSTOMER);
    List<Resource> resourceList = new ArrayList<>();
    for (User u : users) {
      Resource<Link> resource = new Resource<>(linkTo(methodOn(CustomerPositionsController.class)
              .getCustomerPurchases(u.getId(), null, null, null, null))
              .withRel(u.getId().toString()));
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    Link next = linkTo(methodOn(this.getClass()).getCustomers(page + 1, limit)).withRel("next");
    links.add(next);
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getCustomers(page + 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }
}
