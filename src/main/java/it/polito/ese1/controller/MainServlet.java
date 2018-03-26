package it.polito.ese1.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ese1.model.GlobalPosition;
import it.polito.ese1.model.GlobalPositions;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class MainServlet extends HttpServlet {

    private static Map<String, String> user = new HashMap<String, String>();

    private static final Map<String, GlobalPositions> referencePositions = new ConcurrentHashMap<String, GlobalPositions>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userSession = req.getSession(false).getAttribute("user").toString();
        ObjectMapper objectMapper = new ObjectMapper();

        resp.setContentType("application/json");

        GlobalPositions user_positions = referencePositions.get(userSession);
        synchronized (user_positions) {
            objectMapper.writeValue(resp.getWriter(), user_positions.getPositions());
        }
        //for (GlobalPosition pos: referencePositions.get(userSession)) {    }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String jsonData;
        String userSession = req.getSession(false).getAttribute("user").toString();
        ObjectMapper objectMapper = new ObjectMapper();
        jsonData = req.getReader().readLine();

        try {

            List<GlobalPosition> listPos = objectMapper.readValue(jsonData, new TypeReference<List<GlobalPosition>>(){});

            GlobalPositions user_positions = referencePositions.get(userSession);
            synchronized (user_positions) {
                user_positions.addPositions(listPos);
            }
        }catch (IOException io){
            throw new RuntimeException(io);
        }

    }

    @Override
    public void init() throws ServletException {

        //  init...
        BufferedReader br = null;
        FileReader fr = null;
        String [] parts;

        try {

            fr = new FileReader(this.getServletContext().getRealPath("/WEB-INF") + "/users.txt");
            br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {

                parts = line.split(" ");
                user.put(parts[0], parts[1]);

                // initialize positions..
                referencePositions.put(parts[0], new GlobalPositions());

            }


        } catch (IOException e) {

            throw new ServletException();   // better RuntimeException ???

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    public static boolean checkUser(String u, String p) {

        return (user.containsKey(u) && user.get(u).equals(p));
    }

}
