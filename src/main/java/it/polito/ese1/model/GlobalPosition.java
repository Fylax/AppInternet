package it.polito.ese1.model;

import java.util.Date;

public class GlobalPosition {

    private double latitude;
    private double longitude;
    private Date timestamp;

    public GlobalPosition(double latitude, double longitude, long timestamp) {
        boolean valid = (latitude >= -90) &&
                (latitude <= 90) &&
                (longitude >= -180) &&
                (longitude <= 180);
        if(!valid) throw new IllegalArgumentException();
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Date(timestamp);
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

}
