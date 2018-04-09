package it.polito.server.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //invalidate the session if exists
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

}