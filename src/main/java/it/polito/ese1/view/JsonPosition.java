package it.polito.ese1.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ese1.model.Positions;
import it.polito.ese1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JsonPosition implements Position {

  @Override
  public void serialize(HttpServletResponse response, List<it.polito.ese1.model.Position> positionList) throws IOException
  {
    var objectMapper = new ObjectMapper();
    response.setContentType("application/json");
    objectMapper.writeValue(response.getWriter(), positionList);
  }
}
