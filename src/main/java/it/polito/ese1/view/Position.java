package it.polito.ese1.view;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface Position {
  void serialize(HttpServletResponse response, List<it.polito.ese1.model.Position> positionList) throws IOException;
}
