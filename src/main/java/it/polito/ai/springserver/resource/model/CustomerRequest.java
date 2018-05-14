package it.polito.ai.springserver.resource.model;

import com.mongodb.client.model.geojson.Polygon;

public class CustomerRequest {
    private long userid;
    private long start;
    private long end;
    private Polygon polygon;

    public CustomerRequest(long start, long end, Polygon polygon) {
        this.start = start;
        this.end = end;
        this.polygon = polygon;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getUserid() {
        return userid;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Polygon getPolygon() {
        return polygon;
    }
}
