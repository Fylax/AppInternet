package it.polito.ai.springserver.resource.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.ai.springserver.authorization.model.Role;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/")
public class HomeController {

  /**
   * Method for retrieving the list of resources reachable by the user
   * @return ResourceSupport object containing the list of links reachable.
   */
  @ApiOperation(value="Endpoint to retrieve the list of resources reachable by the user")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Operation terminated successfully"),
          @ApiResponse(code= 401, message="User unauthorized"),
          @ApiResponse(code= 500, message = "An error occur on server. Try again")
  })
  @GetMapping(produces = "application/json")
  public ResponseEntity<ResourceSupport> getHome() {
    ResourceSupport resource = new ResourceSupport();
    Link authLink = linkTo(this.getClass()).slash("/oauth/token").withRel("oauth");
    resource.add(authLink);
    if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      Link regLink = linkTo(this.getClass()).slash("/oauth/register").withRel("register");
      resource.add(regLink);
    } else {
      Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().
              getAuthorities();
      for (var ga : userAuthorities) {
        Role role = Role.valueOf(ga.getAuthority());
        switch (role) {
          case ROLE_USER:
            Link linkArchives = linkTo(methodOn(UserArchiveController.class).getArchives(null, null)).
                    withRel("userArchives");
            Link linkArchive = linkTo(methodOn(UserArchiveController.class).getArchive(null)).
                    withRel("userArchive");
            Link linkApproximatedArchives = linkTo(methodOn(UserArchiveController.class)
                    .getApproximatedArchives(null)).
                    withRel("userArchiveSearch");
            Link linkPurchasedArchives = linkTo((methodOn(UserPurchaseController.class)
                    .getPurchasedArchives(null, null)))
                    .withRel("userPurchasedArchives");
            Link linkPurchasedArchive = linkTo((methodOn(UserPurchaseController.class)
                    .getPurchasedArchive(null)))
                    .withRel("userPurchasedArchive");
            resource.add(linkArchives, linkArchive, linkApproximatedArchives,
                    linkPurchasedArchives, linkPurchasedArchive);
            break;
          case ROLE_ADMIN:
            Link linkuser = linkTo(methodOn(AdminController.class).getUsers(null, null))
                    .withRel("adminUsers");
            Link linkUserPurchasedArchives = linkTo(methodOn(UserPurchaseController.class)
                    .getUserPurchasedArchives(null, null, null))
                    .withRel("adminUserPurchasedArchives");
            Link linkUserPurchasedArchive = linkTo(methodOn(UserPurchaseController.class)
                    .getUserPurchasedArchive(null, null))
                    .withRel("adminUserPurchasedArchive");
            Link linkUserArchives = linkTo(methodOn(UserArchiveController.class)
                    .getUserArchives(null, null, null)).withRel("adminUserArchives");
            Link linkUserArchive = linkTo(methodOn(UserArchiveController.class)
                    .getUserArchive(null, null)).withRel("adminUserArchive");
            resource.add(linkuser, linkUserPurchasedArchives, linkUserPurchasedArchive,
                    linkUserArchive, linkUserArchives);
            break;
        }
      }
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
