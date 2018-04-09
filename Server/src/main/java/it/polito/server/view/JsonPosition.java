package it.polito.server.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JsonPosition implements Position {

  @Override
  public void serialize(HttpServletResponse response, List<it.polito.server.model.Position> positionList) throws IOException
  {
    var objectMapper = new ObjectMapper();
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), positionList);
  }
}
