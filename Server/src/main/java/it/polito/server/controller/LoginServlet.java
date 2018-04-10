package it.polito.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.server.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

  static final Map<String, User> USER_MAP = new HashMap<>();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username;
    String password;

    //create ObjectMapper instance and read JSON
    ObjectMapper objectMapper = new ObjectMapper();

    try {

      JsonNode rootNode = objectMapper.readTree(request.getReader());
      JsonNode idNode = rootNode.path("user");
      username = idNode.asText();

      idNode = rootNode.path("pwd");
      password = idNode.asText();

    } catch (IOException io) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurs processing the request.");
      return;
    }

    if (LoginServlet.checkUser(username, password)) {

      // 200 OK, initializing user's session..

      HttpSession session = request.getSession();
      session.setAttribute("user", username);
      //setting session to expiry in 30 mins
      session.setMaxInactiveInterval(30 * 60);

      //TODO: il redirect cosi non funziona, dobbiamo specificare eventualmente l'intero path
      //response.sendRedirect("HelloWorld.jsp");
    } else {
      response.sendError(401, " * The user name or password is incorrect!!! * ");
    }

  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try (
        FileReader fr = new FileReader(super.getServletContext().getRealPath("/WEB-INF") + "/users.txt");
        BufferedReader br = new BufferedReader(fr)
    ) {

      String line;
      String[] parts;
      while ((line = br.readLine()) != null) {
        parts = line.split(" ");
        USER_MAP.put(parts[0], new User(parts[0], parts[1]));
      }
    } catch (IOException e) {
      throw new ServletException();
    }
  }

  private static boolean checkUser(String u, String p) {

    return (USER_MAP.containsKey(u) && USER_MAP.get(u).getPwd().equals(p));
  }
}