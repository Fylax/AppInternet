package it.polito.ese1.servlet;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
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


    boolean TypeOK = false;

    if (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT"))
    {

      String contentType = request.getContentType();
      if(contentType == null)
      {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data upload requests must have content-type NOT null!");
      }
      TypeOK = contentType != null && contentType.equals(MediaType.APPLICATION_JSON);

      if (!TypeOK) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Data upload requests must have content-type set to '" + MediaType.APPLICATION_JSON + "'");
      }

    }
    else if (request.getMethod().equalsIgnoreCase("GET")){

        String acceptType = request.getHeader("Accept");
        if(acceptType.equals("application/json") || acceptType.equals("application/*") || acceptType.equals("*/*"))
        {
            TypeOK = true;
        }
        if (!TypeOK) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accept-header NOT corresponding to" + MediaType.APPLICATION_JSON + "'");
        }
    }

    if (TypeOK){

        // In questo modo privilegio la richiesta della risorsa "login", lasciando scorrere la catena...
        String loginURI = request.getContextPath() + "/login";

        //  Controllo se l'user è già loggato oppure vuole accedere a /login
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);

        if (loggedIn ^ loginRequest) {
            chain.doFilter(request, response);
        } else if (loggedIn && loginRequest) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "You are already loggedIn.");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, " * Unauthorized!!! * ");
        }
    }


  }

  public void destroy() {

  }


}
