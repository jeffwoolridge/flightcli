package com.flightcli.flight_cli.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Passenger() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return String.format("Passenger{id=%d, name='%s %s', phone='%s'}",
                id, firstName, lastName, phoneNumber);
    }
}
