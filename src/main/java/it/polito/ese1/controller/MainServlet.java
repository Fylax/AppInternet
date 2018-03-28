package it.polito.ese1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ese1.model.GlobalPosition;
import it.polito.ese1.model.GlobalPositions;
import it.polito.ese1.model.PositionException;
import it.polito.ese1.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainServlet extends HttpServlet {

  private static final Map<String, User> USER_MAP = new HashMap<>();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String userSession = req.getSession(false).getAttribute("user").toString();
    ObjectMapper objectMapper = new ObjectMapper();

    resp.setContentType("application/json");

    var currentUser = USER_MAP.get(userSession);
    objectMapper.writeValue(resp.getWriter(), currentUser.getPositions());
    //for (GlobalPosition pos: referencePositions.get(userSession)) {    }

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    String userSession = req.getSession(false).getAttribute("user").toString();
    ObjectMapper objectMapper = new ObjectMapper();

    try {

      List<GlobalPosition> listPos = objectMapper.readValue(req.getReader(),
                                                            new TypeReference<List<GlobalPosition>>() {
                                                            });

      var currentUser = USER_MAP.get(userSession);
      currentUser.addPositions(listPos);
    } catch (IOException io) {
      // TODO internal server error?
      throw new RuntimeException(io);
    } catch (PositionException e) {
      // TODO bad request!
      throw new RuntimeException(e);
    }

  }

  @Override
  public void init() throws ServletException {

    //  init...
    BufferedReader br = null;
    FileReader fr = null;
    String[] parts;

    try {

      fr = new FileReader(this.getServletContext().getRealPath("/WEB-INF") + "/users.txt");
      br = new BufferedReader(fr);

      String line;

      while ((line = br.readLine()) != null) {
        parts = line.split(" ");
        USER_MAP.put(parts[0], new User(parts[0], parts[1]));
      }

    } catch (IOException e) {

      throw new ServletException();   // better RuntimeException ???

    } finally {

      try {

        if (br != null) {
          br.close();
        }

        if (fr != null) {
          fr.close();
        }

      } catch (IOException ex) {

        ex.printStackTrace();

      }
    }

  }

  public static boolean checkUser(String u, String p) {

    return (USER_MAP.containsKey(u) && USER_MAP.get(u).equals(p));
  }

}
