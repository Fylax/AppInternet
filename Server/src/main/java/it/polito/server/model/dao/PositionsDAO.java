package it.polito.server.model.dao;

import it.polito.server.model.Position;
import it.polito.server.model.User;

import java.util.List;


//TODO: si riesce a fare un solo metodo cghe varia la query in base al num di parametri? altrimenti c'è tanto codice duplicato
// by Nico, immagino basti solo l'ultimo, tanto i controlli vengono fatti prima
public interface PositionsDAO {

  void addPositions(User user, List<Position> positions);

  Position fetchLast(User user);

  List<Position> fetchAll(User user);

  List<Position> fetchSince(User user, long since);

  List<Position> fetchUpTo(User user, long upTo);

  List<Position> fetchInterval(User user, long start, long end);
}
