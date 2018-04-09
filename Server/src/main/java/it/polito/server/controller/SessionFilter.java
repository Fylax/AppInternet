package it.polito.server.controller;
import it.polito.server.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionFilter implements Filter {



  public void init(FilterConfig filterConfig) throws ServletException {

  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;
    final HttpSession session = request.getSession(false);

    //  Controllo se l'user è già loggato oppure vuole accedere a /login
    final boolean loggedIn = session != null && session.getAttribute("user") != null;
    final boolean loginRequest = request.getRequestURI().equals(request.getContextPath() + "/login");

    if (loggedIn ^ loginRequest) {
      chain.doFilter(request, response);
    } else if (loggedIn && loginRequest) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  @Override
  public void destroy() {

  }
}
