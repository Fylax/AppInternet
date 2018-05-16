package it.polito.ai.springserver.resource.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.geojson.Geometry;
import it.polito.ai.springserver.resource.model.CustomerRequest;
import it.polito.ai.springserver.resource.model.repository.PositionRepository;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.client.model.geojson.Polygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/positions")
public class PositionsController {

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private PositionRepository positionRepository;

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public String getPolygonPositions(@RequestParam(value = "geoJson", required = false) String params){

        long countPoisitions = 0;
        CustomerRequest currRequest = null;
            try {
                currRequest = (new ObjectMapper()).readValue((new String(Base64.decode(params))), CustomerRequest.class);
                countPoisitions = positionRepository.countPositionByPositionIsWithinAndTimestampBetween(currRequest.getPolygon(),0L,0L);
                System.out.println("*** " +currRequest.getStart());
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(currRequest.getPolygon().toString() + " - Start: " + currRequest.getStart() + " - End: " + currRequest.getEnd());
        return String.valueOf(countPoisitions);
    }

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String bookPositions(){ return "addPositionsController"; }

}
/*
        //Extracting user_id from JWT... was it put???
        Map<String, Object> additionalInformation = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        long user_id = (long) additionalInformation.get("user_id");

        Long listPos = null;
        //System.out.println(myCust.getPolygon().toString());

 */