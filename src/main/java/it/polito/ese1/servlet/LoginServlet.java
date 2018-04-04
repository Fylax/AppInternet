package it.polito.ese1.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ese1.controller.MainServlet;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /*@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("login.jsp");

    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
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

        }catch (IOException io){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurs processing the request.");
            return;
        }

        //System.out.println("User: " + username + "  -  Password: " + password);

        if (MainServlet.checkUser(username, password)) {

            // 200 OK, initializing user's session..

            HttpSession session = request.getSession();
            session.setAttribute("user", username);   //TODO: diamo per scontato siano univoci in users.txt ?!?!
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30*60);

            //TODO: il redirect cosi non funziona, dobbiamo specificare eventualmente l'intero path
            //response.sendRedirect("HelloWorld.jsp");
        }
        else {
            response.sendError(401, " * The user name or password is incorrect!!! * ");
        }

    }

}