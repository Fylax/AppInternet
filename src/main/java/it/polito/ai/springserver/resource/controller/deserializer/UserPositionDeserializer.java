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
import it.polito.ai.springserver.resource.model.Positions;
import org.geojson.Point;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UserPositionDeserializer extends StdDeserializer<Positions> {

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
  public Positions deserialize(JsonParser jp, DeserializationContext dc)
      throws IOException {
    JsonNode positionNode = jp.getCodec().readTree(jp);

    var om = new ObjectMapper();
    try {
      if (positionNode.isArray()) {
        List<Position> output = new LinkedList<>();
        for (final JsonNode jposition : positionNode) {
          output.add(this.parsePosition(om, jposition));
        }
        return new Positions(output);
      }
      return new Positions(this.parsePosition(om, positionNode));
    } catch (PositionException | NullPointerException e) {
      throw new JsonParseException(jp, e.getMessage());
    }
  }
}
