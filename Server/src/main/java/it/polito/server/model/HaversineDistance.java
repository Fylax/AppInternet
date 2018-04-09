package it.polito.server.model;

class HaversineDistance implements Distance {

  private static final double R = 6371e3;

  @Override
  public double getDistance(Position src, Position dst) {
    double lat1 = Math.toRadians(src.getLatitude());
    double lat2 = Math.toRadians(dst.getLatitude());
    double deltaLng = Math.toRadians(dst.getLongitude() - src.getLongitude());

    double latHaversine = (1 - Math.cos(lat2 - lat1)) / 2;
    double lngHaversine = (1 - Math.cos(deltaLng)) / 2;

    double sqrtArg = latHaversine + (Math.cos(lat1) * Math.cos(lat2) * lngHaversine);

    return 2 * R * Math.asin(Math.sqrt(sqrtArg));
  }
}
