package it.polito.ese1.servlet;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class FilterPosition implements Filter {

  private ServletContext context;

  public void init(FilterConfig filterConfig) throws ServletException {
    this.context = filterConfig.getServletContext();
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    HttpSession session = request.getSession(false);

    // In questo modo privilegio la richiesta della risorsa "login", lasciando scorrere la catena...
    String loginURI = request.getContextPath() + "/login";

    //  Controllo se l'user è già loggato oppure vuole accedere a /login
    boolean loggedIn = session != null && session.getAttribute("user") != null;
    boolean loginRequest = request.getRequestURI().equals(loginURI);

    if (loggedIn ^ loginRequest) {
      chain.doFilter(request, response);
    } else if (loggedIn && loginRequest) {

    } else {
      response.sendError(401, " * Unauthorized!!! * ");
    }

  }

  public void destroy() {

  }


}
