package it.polito.ese1.view;

import it.polito.ese1.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Position {
  void serialize(HttpServletResponse response, User user) throws IOException;
}
