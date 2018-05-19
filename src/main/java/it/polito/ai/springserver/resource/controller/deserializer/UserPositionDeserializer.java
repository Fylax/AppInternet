package it.polito.ai.springserver.resource.controller.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polito.ai.springserver.resource.exception.PositionException;
import it.polito.ai.springserver.resource.model.Position;
import org.geojson.Point;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UserPositionDeserializer extends StdDeserializer<List<Position>> {

  public UserPositionDeserializer() {
    this(null);
  }

  public UserPositionDeserializer(Class<?> vc) {
    super(vc);
  }

  private Position parsePosition(ObjectMapper om, JsonNode position)
      throws IOException, PositionException {
    long timestamp = position.get("timestamp").longValue();
    var jcoordinates = position.get("position");
    var point = om.readValue(jcoordinates.toString(), Point.class);
    var coordinates = point.getCoordinates();
    return new Position(timestamp, coordinates.getLongitude(), coordinates.getLatitude());
  }

  @Override
  public List<Position> deserialize(JsonParser jp, DeserializationContext dc)
      throws IOException {
    JsonNode positionNode = jp.getCodec().readTree(jp);

    var om = new ObjectMapper();
    List<Position> output = new LinkedList<>();
    try {
      if (positionNode.isArray()) {
        for (final JsonNode jposition : positionNode) {
          output.add(this.parsePosition(om, jposition));
        }
      } else {
        output.add(this.parsePosition(om, positionNode));
      }
    } catch (PositionException e) {
      throw new JsonParseException(jp, e.getMessage());
    }
    return output;
  }
}
