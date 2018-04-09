package it.polito.server.view;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface Position {
  void serialize(HttpServletResponse response, List<it.polito.server.model.Position> positionList) throws IOException;
}
