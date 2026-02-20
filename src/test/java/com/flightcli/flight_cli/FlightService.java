package com.flightcli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flightcli.model.Aircraft;
import com.flightcli.model.Airport;
import com.flightcli.model.City;
import com.flightcli.model.Passenger;

import java.io.IOException;
import java.util.List;

public class FlightService {

    private final ApiClient apiClient;

    public FlightService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    // Q1: What airports are there in each city?
    public List<Airport> getAirportsForCity(Long cityId) throws IOException, InterruptedException {
        return apiClient.getList("/cities/" + cityId + "/airports", new TypeReference<List<Airport>>() {});
    }

    // Q2: What aircraft has each passenger flown on?
    public List<Aircraft> getAircraftForPassenger(Long passengerId) throws IOException, InterruptedException {
        return apiClient.getList("/passengers/" + passengerId + "/aircraft", new TypeReference<List<Aircraft>>() {});
    }

    // Q3: What airports do aircraft take off from and land at?
    public List<Airport> getAirportsForAircraft(Long aircraftId) throws IOException, InterruptedException {
        return apiClient.getList("/aircraft/" + aircraftId + "/airports", new TypeReference<List<Airport>>() {});
    }

    // Q4: What airports have passengers used?
    public List<Airport> getAirportsForPassenger(Long passengerId) throws IOException, InterruptedException {
        return apiClient.getList("/passengers/" + passengerId + "/airports", new TypeReference<List<Airport>>() {});
    }

    // Helper: get all cities (for listing)
    public List<City> getAllCities() throws IOException, InterruptedException {
        return apiClient.getList("/cities", new TypeReference<List<City>>() {});
    }

    // Helper: get all passengers (for listing)
    public List<Passenger> getAllPassengers() throws IOException, InterruptedException {
        return apiClient.getList("/passengers", new TypeReference<List<Passenger>>() {});
    }

    // Helper: get all aircraft (for listing)
    public List<Aircraft> getAllAircraft() throws IOException, InterruptedException {
        return apiClient.getList("/aircraft", new TypeReference<List<Aircraft>>() {});
    }
}
