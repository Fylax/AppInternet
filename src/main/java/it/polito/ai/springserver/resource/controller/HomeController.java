package it.polito.ai.springserver.resource.controller;

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

  @GetMapping
  public ResponseEntity<ResourceSupport> getHome(){
    ResourceSupport resource = new ResourceSupport();
    Link authLink = linkTo(this.getClass()).slash("/oauth/token").withRel("oauth");
    resource.add(authLink);
    if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
      for(var ga : userAuthorities) {
        Role role = Role.valueOf(ga.getAuthority());
        switch(role) {
          case ROLE_USER:
            Link linkArchives = linkTo(methodOn(UserArchiveController.class).getArchives(null, null)).
                    withRel("userArchives");
            Link linkArchive =  linkTo(methodOn(UserArchiveController.class).getArchive(null)).
                    withRel("userArchive");
            resource.add(linkArchives, linkArchive);
            break;
          case ROLE_ADMIN:
            Link linkuser = linkTo(methodOn(AdminController.class).getUsers(null, null))
                    .withRel("adminUsers");
            Link linkCustomer = linkTo(methodOn(AdminController.class).getCustomers(null, null))
                    .withRel("adminCustomers");
            Link linkCustomerPurchases = linkTo(methodOn(UserPurchaseController.class)
                    .getCustomerPurchases(null, null, null, null, null))
                    .withRel("adminCustomerPurchases");
            Link linkCustomerPurchase = linkTo(methodOn(UserPurchaseController.class)
                    .getCustomerPurchase(null, null))
                    .withRel("adminCustomerPurchase");
            Link linkUserArchives = linkTo(methodOn(UserArchiveController.class)
                    .getUserArchives( null, null, null)).withRel("adminUserArchives");
            Link linkUserArchive = linkTo(methodOn(UserArchiveController.class)
                    .getUserArchive( null, null)).withRel("adminUserArchive");
            resource.add(linkuser, linkCustomer, linkCustomerPurchases, linkCustomerPurchase,
                    linkUserArchive, linkUserArchives);
            break;
        }
      }
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
