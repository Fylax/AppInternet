package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        Link linkPositions;
        switch(role) {
          case ROLE_USER:
            linkPositions = linkTo(UserPositionsController.class).withRel("userPositions");
            resource.add(linkPositions);
            break;
          case ROLE_CUSTOMER:
            linkPositions = linkTo(CustomerPositionsController.class).withRel("customerPositions");
            Link linkPurchase = linkTo(methodOn(CustomerPositionsController.class)
                    .getPurchases(null, null, null, null)).withRel("customerPurchases");
            Link linkPurchaseDetails = linkTo(methodOn(CustomerPositionsController.class)
                    .getPurchase(null)).withRel("customerPurchaseDetails");
            resource.add(linkPositions, linkPurchase, linkPurchaseDetails);
            break;
          case ROLE_ADMIN:
            Link linkuser = linkTo(methodOn(AdminController.class).getUsers(null, null)).withRel("adminUsers");
            Link linkCustomer = linkTo(methodOn(AdminController.class).getCustomers(null, null)).withRel("adminCustomers");
            Link linkCustomerPurchases = linkTo(methodOn(CustomerPositionsController.class)
                    .getCustomerPurchases(null, null, null, null, null))
                    .withRel("adminCustomerPurchases");
            Link linkCustomerPurchase = linkTo(methodOn(CustomerPositionsController.class)
                    .getCustomerPurchase(null, null))
                    .withRel("adminCustomerPurchase");
            Link linkUserPositions = linkTo(methodOn(UserPositionsController.class)
                    .getPositions(null, null, null)).withRel("adminUserPositions");
            resource.add(linkuser, linkCustomer, linkCustomerPurchases, linkCustomerPurchase, linkUserPositions);
            break;
        }
      }
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
