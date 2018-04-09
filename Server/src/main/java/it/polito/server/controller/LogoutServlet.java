package it.polito.server.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //invalidate the session if exists
    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }

}