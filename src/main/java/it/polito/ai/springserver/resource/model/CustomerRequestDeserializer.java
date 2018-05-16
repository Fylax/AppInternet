package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.geojson.Polygon;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CustomerRequestDeserializer extends StdDeserializer<CustomerRequest> {

  public CustomerRequestDeserializer() {
    this(null);
  }

  public CustomerRequestDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public CustomerRequest deserialize(JsonParser jp, DeserializationContext dc)
      throws IOException, JsonProcessingException {
    JsonNode productNode = jp.getCodec().readTree(jp);

    var area = productNode.get("area");
    var om = new ObjectMapper();
    Polygon polygon = om.readValue(area.toString(), Polygon.class);
    List<Point> points = new LinkedList<>();
    var coordinates = polygon.getCoordinates();
    for (int i = 0; i < coordinates.size(); i++) {
      var ring = coordinates.get(i);
      for (int j = 0; j < ring.size(); j++) {
        var coordinate = ring.get(j);
        var x = coordinate.getLongitude();
        var y = coordinate.getLatitude();
        points.add(new Point(x, y));
      }
    }
    long start = productNode.get("start").longValue();
    long end = productNode.get("end").longValue();
    return new CustomerRequest(start, end, new GeoJsonPolygon(points));
  }

    /*@Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        GeoJsonObject obj = new ObjectMapper().readValue(p ,GeoJsonObject.class);
        if(!(obj instanceof Polygon)){
            throw new RuntimeException();
        }
        return obj;
    }*/

}
