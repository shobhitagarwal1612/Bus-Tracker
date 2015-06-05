package in.silive.bustracker.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class BusItem {

    private String bus_name;
    private String bus_no;
    private String source;
    private String destination;
    private JSONArray bus_routes;

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBus_name() {
        return this.bus_name;
    }

    public String getBus_no() {
        return this.bus_no;
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setBus_routes(JSONArray bus_routes) {
        this.bus_routes = bus_routes;
    }

    public JSONArray getBus_routes() {
        return bus_routes;
    }
}
