package it.polito.server.model.dao;

import it.polito.server.model.ConnectionException;
import it.polito.server.model.Position;
import it.polito.server.model.User;

import java.util.List;


//TODO: si riesce a fare un solo metodo cghe varia la query in base al num di parametri? altrimenti c'è tanto codice duplicato
// by Nico, immagino basti solo l'ultimo, tanto i controlli vengono fatti prima
public interface PositionsDAO {

  void addPositions(User user, List<Position> positions) throws ConnectionException;

  Position fetchLast(User user) throws ConnectionException;

  List<Position> fetchAll(User user) throws ConnectionException;

  List<Position> fetchSince(User user, long since) throws ConnectionException;

  List<Position> fetchUpTo(User user, long upTo) throws ConnectionException;

  List<Position> fetchInterval(User user, long start, long end) throws ConnectionException;
}
