package it.polito.ai.springserver.resource.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import it.polito.ai.springserver.resource.model.PaginationSupportClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  /**
   * Method for retrieving the list of users signed to this application and the links to there archives
   * @param page Page number to be retrieved on the db
   * @param limit The number of element to be retrieved
   * @return PaginationSupportClass object containing:
   *         items: list of users with their links.
   *         totalElements: total number of users signed
   *         links: links to prev and next page
   */
  @ApiOperation(
          value="Admin endpoint to retrieve the list of users signed to this application and the links to there archives")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code= 401, message="User unauthorized"),
          @ApiResponse(code= 500, message = "An error occur on server. Try again")
  })
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping(produces = "application/json")
  public ResponseEntity<PaginationSupportClass> getUsers(
          @RequestParam(value = "page", defaultValue = "1") Integer page,
          @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
    page = page < 1 ? 1 : page;
    limit = limit < 1 ? 20 : limit;
    PageRequest pageRequest = new PageRequest(page - 1, limit, Sort.Direction.ASC, "id");
    List<User> users = userRepositoryInterface.findAllByUserRolesContaining(Role.ROLE_USER, pageRequest);
    if (users.size() == 0)
      return new ResponseEntity<>(new PaginationSupportClass(), HttpStatus.NOT_FOUND);
    int totalElements = userRepositoryInterface.countAllByUserRolesContaining(Role.ROLE_USER);
    List<Resource> resourceList = new ArrayList<>();
    for (User u : users) {
      Resource<Link> resource = new Resource<>(linkTo(methodOn(UserArchiveController.class)
              .getUserArchives(u.getId(), null, null))
              .withRel(u.getId().toString()));
      resourceList.add(resource);
    }
    List<Link> links = new ArrayList<>();
    if (resourceList.size() == limit) {
      Link next = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("next");
      links.add(next);
    }
    if (page != 1) {
      Link prev = linkTo(methodOn(this.getClass()).getUsers(page + 1, limit)).withRel("prev");
      links.add(prev);
    }
    PaginationSupportClass pg = new PaginationSupportClass(resourceList, totalElements, links);
    return new ResponseEntity<>(pg, HttpStatus.OK);
  }
}
