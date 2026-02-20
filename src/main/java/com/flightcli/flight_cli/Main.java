package com.flightcli.flight_cli;

import com.flightcli.flight_cli.menu.Menu;
import com.flightcli.flight_cli.service.ApiClient;
import com.flightcli.flight_cli.service.FlightService;

public class Main {

    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        ApiClient apiClient = new ApiClient(BASE_URL);
        FlightService flightService = new FlightService(apiClient);
        Menu menu = new Menu(flightService);
        menu.start();
    }
}