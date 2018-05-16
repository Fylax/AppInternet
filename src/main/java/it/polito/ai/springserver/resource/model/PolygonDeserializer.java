package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.GeoJsonObject;
import org.geojson.Polygon;

import java.io.IOException;

public class PolygonDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        GeoJsonObject obj = new ObjectMapper().readValue(p ,GeoJsonObject.class);
        if(!(obj instanceof Polygon)){
            throw new RuntimeException();
        }
        return obj;
    }

}
