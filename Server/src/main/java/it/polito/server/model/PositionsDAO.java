package it.polito.server.model;

import java.util.List;


//TODO: si riesce a fare un solo metodo cghe varia la query in base al num di parametri? altrimenti c'è tanto codice duplicato
// by Nico, immagino basti solo l'ultimo, tanto i controlli vengono fatti prima
public interface PositionsDAO {

  void addPositions(User user, List<Position> positions);

  List<Position> getPositions(User user);

  List<Position> getPositions(User user, long since);

  List<Position> getPositions(User user, long start, long end);
}
