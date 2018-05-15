package it.polito.ai.springserver.resource.model;

import com.mongodb.client.model.geojson.Polygon;

public class CustomerRequest {
    private long id;
    private long start;
    private long end;
    private Polygon polygon;

    public CustomerRequest(long start, long end, Polygon polygon) {
        this.start = start;
        this.end = end;
        this.polygon = polygon;
    }

    public void setUserid(long id) {
        this.id = id;
    }

    public long getUserid() {
        return id;
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
