package it.polito.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.server.model.InvalidLoginException;
import it.polito.server.model.PostgresUserDAO;
import it.polito.server.model.User;
import it.polito.server.model.UserDAO;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String username;
    String password;

    //create ObjectMapper instance and read JSON
    ObjectMapper objectMapper = new ObjectMapper();

    Reader reader;
    try {
      reader = request.getReader();
    } catch (IOException io) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }
    try {
      JsonNode rootNode = objectMapper.readTree(reader);
      JsonNode idNode = rootNode.path("user");
      username = idNode.asText();

      idNode = rootNode.path("pwd");
      password = idNode.asText();
    } catch (IOException io) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    UserDAO userDAO = new PostgresUserDAO();
    try {
      User user = userDAO.getUser(username, password);
      HttpSession session = request.getSession();
      session.setAttribute("user", user);
      //setting session to expiry in 30 mins
      session.setMaxInactiveInterval(30 * 60);
    } catch (InvalidLoginException e) {
      response.sendError(401, " * The user name or password is incorrect!!! * ");
    }
  }
}