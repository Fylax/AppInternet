package it.polito.ese1.servlet;

import java.sql.Timestamp;

public class GlobalPosition {

    private double latitude;
    private double longitude;
    private int tempMark;       //TODO: TIMESTAMP ???

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTempMark() {
        return tempMark;
    }

    public void setTempMark(int tempMark) {
        this.tempMark = tempMark;
    }


}
