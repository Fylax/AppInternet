package it.polito.ai.springserver.resource.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mongodb.client.model.geojson.Polygon;

public class CustomerRequest {
    private long start;
    private long end;
    @JsonDeserialize(contentUsing = PolygonDeserializer.class)
    private Polygon area;


    public CustomerRequest(long start, long end, Polygon area) {
        this.start = start;
        this.end = end;
        this.area = area;

        }
    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Polygon getPolygon() {
        return area;
    }

}
