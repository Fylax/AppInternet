package it.polito.server.model;

import java.util.List;


//TODO: si riesce a fare un solo metodo cghe varia la query in base al num di parametri? altrimenti c'è tanto codice duplicato
public interface PositionsDAO {
    List<Position> getPositions(String user_id);
    List<Position> getPositions(String user_id, long since);
    List<Position> getPositions(String user_id, long start, long end);
}
