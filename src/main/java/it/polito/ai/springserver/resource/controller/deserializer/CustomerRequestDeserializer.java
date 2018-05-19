package it.polito.ai.springserver.resource.controller.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polito.ai.springserver.resource.model.CustomerRequest;
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
    JsonNode requestNode = jp.getCodec().readTree(jp);

    var area = requestNode.get("area");
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
    long start = requestNode.get("start").longValue();
    long end = requestNode.get("end").longValue();
    return new CustomerRequest(start, end, new GeoJsonPolygon(points));
  }
}
