package com.flightcli.flight_cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aircraft {
    private Long id;
    private String type;
    private String airlineName;
    private int numberOfPassengers;

    public Aircraft() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public int getNumberOfPassengers() { return numberOfPassengers; }
    public void setNumberOfPassengers(int numberOfPassengers) { this.numberOfPassengers = numberOfPassengers; }

    @Override
    public String toString() {
        return String.format("Aircraft{id=%d, type='%s', airline='%s', capacity=%d}",
                id, type, airlineName, numberOfPassengers);
    }
}
