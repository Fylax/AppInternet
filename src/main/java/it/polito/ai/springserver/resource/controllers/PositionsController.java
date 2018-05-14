package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.Position;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/positions")
public class PositionsController {

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public String getPolygonPositions(@PathVariable("geoJson") String geoJson, OAuth2Authentication authentication){

        //Extracting user_id from JWT... was it put???
        Map<String, Object> additionalInformation = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        long user_id = (long) additionalInformation.get("user_id");

        List<Position> listPos = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CustomerRequest myCust = objectMapper.readValue(geoJson, CustomerRequest.class);
            myCust.setUserid(user_id);
            listPos = positionRepository.findByPositionWithin(myCust.getPolygon());
        //System.out.println(myCust.getPolygon().toString());
        }catch (Exception e){ e.printStackTrace(); }

        return String.valueOf(listPos.size());
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String bookPositions(){ return "addPositionsController"; }


}
