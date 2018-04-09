package it.polito.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.server.model.Position;
import it.polito.server.model.PositionException;
import it.polito.server.model.User;
import it.polito.server.view.JsonPosition;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainServlet extends HttpServlet {

  private static final Map<String, User> USER_MAP = new HashMap<>();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      String userSession = req.getSession(false).getAttribute("user").toString();
      var currentUser = USER_MAP.get(userSession);
      it.polito.server.view.Position pos = new JsonPosition();
      String start = req.getParameter("start");
      String end = req.getParameter("end");

      List<Position> positionList = currentUser.getPositions(start, end);
      pos.serialize(resp, positionList);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurs processing your request.");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    String userSession = req.getSession(false).getAttribute("user").toString();
    ObjectMapper objectMapper = new ObjectMapper();

    try {

      List<Position> listPos = objectMapper.readValue(req.getReader(),
              new TypeReference<List<Position>>() {
              });

      var currentUser = USER_MAP.get(userSession);
      currentUser.addPositions(listPos);
    } catch (PositionException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Your positions are not valid.");
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurs processing your request.");
    }
    //maybe is better handle error using a servlet in order to handle directly ServletException, IoException and RunTimeException

  }



}