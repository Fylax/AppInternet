package it.polito.server.controller;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;


public class TypeFilter implements Filter {

  private ServletContext context;

  public void init(FilterConfig filterConfig) throws ServletException {
    this.context = filterConfig.getServletContext();
  }

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (request.getMethod().equalsIgnoreCase("POST") ||
        request.getMethod().equalsIgnoreCase("PUT")) {
      String contentType = request.getContentType();
      if (contentType == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }
      if (!contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON)) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    } else if (request.getMethod().equalsIgnoreCase("GET")) {
      String acceptType = request.getHeader(HttpHeaders.ACCEPT);
      if (!acceptType.equalsIgnoreCase(MediaType.APPLICATION_JSON) &&
          !acceptType.equalsIgnoreCase("application/*") &&
          !acceptType.equalsIgnoreCase(MediaType.WILDCARD)) {
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
      }
    }
  }

  public void destroy() {

  }


}
