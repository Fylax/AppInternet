package it.polito.ai.springserver.resource.controller;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public ResponseEntity<ResourceSupport> getHome(@RequestHeader(value = "Authorization", required = false) String bearerToken){
    ResourceSupport resource = new ResourceSupport();
    Link authLink = linkTo(this.getClass()).slash("/oauth/token").withRel("oauth");
    resource.add(authLink);
    // it is the only way that I've found to check the roles
    Collection<? extends GrantedAuthority> userAuthorities=  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    List<String> userRoles = new ArrayList<>();
    userAuthorities.forEach(r -> userRoles.add(r.getAuthority()));
    if(userRoles.contains(Role.ROLE_USER.getAuthority())){
      Link linkPositions = linkTo(UserPositionsController.class).withRel("userPositions");
      resource.add(linkPositions);
    }
    if(userRoles.contains(Role.ROLE_CUSTOMER.getAuthority())){
      Link linkPositions = linkTo(CustomerPositionsController.class).withRel("customerPositions");
      Link linkPurchase = linkTo(methodOn(CustomerPositionsController.class)
              .getPurchases(null, null, null, null)).withRel("purchases");
      Link linkPurchaseDetails = linkTo(methodOn(CustomerPositionsController.class)
              .getPurchase(null)).withRel("purchaseDetails");
      resource.add(linkPositions, linkPurchase, linkPurchaseDetails);
    }
    if(userRoles.contains(Role.ROLE_ADMIN.getAuthority())){
      Link linkuser = linkTo(methodOn(AdminController.class).getUsers(null, null)).withRel("users");
      Link linkCustomer = linkTo(methodOn(AdminController.class).getCustomers(null, null)).withRel("customers");
      resource.add(linkuser, linkCustomer);
    }
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }
}
