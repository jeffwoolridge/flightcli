package com.flightcli.flight_cli.menu;

import com.flightcli.flight_cli.model.Airport;
import com.flightcli.flight_cli.model.Aircraft;
import com.flightcli.flight_cli.model.City;
import com.flightcli.flight_cli.model.Passenger;
import com.flightcli.flight_cli.service.FlightService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final FlightService flightService;
    private final Scanner scanner;

    public Menu(FlightService flightService) {
        this.flightService = flightService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("       ✈  FLIGHT INFO CLI APP  ✈       ");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleAirportsInCity();
                case "2" -> handleAircraftForPassenger();
                case "3" -> handleAirportsForAircraft();
                case "4" -> handleAirportsForPassenger();
                case "5" -> running = false;
                default -> System.out.println("Invalid option. Please try again.\n");
            }
        }

        System.out.println("Goodbye! ✈");
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. What airports are there in each city?");
        System.out.println("2. What aircraft has each passenger flown on?");
        System.out.println("3. What airports do aircraft take off from and land at?");
        System.out.println("4. What airports have passengers used?");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    // Q1: Airports in a city
    private void handleAirportsInCity() {
        try {
            System.out.println("\n--- AIRPORTS BY CITY ---");
            List<City> cities = flightService.getAllCities();

            if (cities.isEmpty()) {
                System.out.println("No cities found.");
                return;
            }

            System.out.println("Available cities:");
            cities.forEach(c -> System.out.println("  [" + c.getId() + "] " + c.getName() + ", " + c.getState()));

            System.out.print("Enter city ID: ");
            Long cityId = Long.parseLong(scanner.nextLine().trim());

            List<Airport> airports = flightService.getAirportsForCity(cityId);

            if (airports.isEmpty()) {
                System.out.println("No airports found for this city.");
            } else {
                System.out.println("\nAirports in city " + cityId + ":");
                airports.forEach(a -> System.out.println("  - " + a.getName() + " (" + a.getCode() + ")"));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to API: " + e.getMessage());
        }
    }

    // Q2: Aircraft a passenger has flown on
    private void handleAircraftForPassenger() {
        try {
            System.out.println("\n--- AIRCRAFT BY PASSENGER ---");
            List<Passenger> passengers = flightService.getAllPassengers();

            if (passengers.isEmpty()) {
                System.out.println("No passengers found.");
                return;
            }

            System.out.println("Available passengers:");
            passengers.forEach(p -> System.out.println(
                    "  [" + p.getId() + "] " + p.getFirstName() + " " + p.getLastName()));

            System.out.print("Enter passenger ID: ");
            Long passengerId = Long.parseLong(scanner.nextLine().trim());

            List<Aircraft> aircraft = flightService.getAircraftForPassenger(passengerId);

            if (aircraft.isEmpty()) {
                System.out.println("This passenger has not flown on any aircraft.");
            } else {
                System.out.println("\nAircraft flown by passenger " + passengerId + ":");
                aircraft.forEach(a -> System.out.println(
                        "  - " + a.getType() + " | " + a.getAirlineName() + " | Capacity: " + a.getNumberOfPassengers()));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to API: " + e.getMessage());
        }
    }

    // Q3: Airports an aircraft uses
    private void handleAirportsForAircraft() {
        try {
            System.out.println("\n--- AIRPORTS BY AIRCRAFT ---");
            List<Aircraft> aircraftList = flightService.getAllAircraft();

            if (aircraftList.isEmpty()) {
                System.out.println("No aircraft found.");
                return;
            }

            System.out.println("Available aircraft:");
            aircraftList.forEach(a -> System.out.println(
                    "  [" + a.getId() + "] " + a.getType() + " - " + a.getAirlineName()));

            System.out.print("Enter aircraft ID: ");
            Long aircraftId = Long.parseLong(scanner.nextLine().trim());

            List<Airport> airports = flightService.getAirportsForAircraft(aircraftId);

            if (airports.isEmpty()) {
                System.out.println("No airports found for this aircraft.");
            } else {
                System.out.println("\nAirports used by aircraft " + aircraftId + ":");
                airports.forEach(a -> System.out.println("  - " + a.getName() + " (" + a.getCode() + ")"));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to API: " + e.getMessage());
        }
    }

    // Q4: Airports a passenger has used
    private void handleAirportsForPassenger() {
        try {
            System.out.println("\n--- AIRPORTS USED BY PASSENGER ---");
            List<Passenger> passengers = flightService.getAllPassengers();

            if (passengers.isEmpty()) {
                System.out.println("No passengers found.");
                return;
            }

            System.out.println("Available passengers:");
            passengers.forEach(p -> System.out.println(
                    "  [" + p.getId() + "] " + p.getFirstName() + " " + p.getLastName()));

            System.out.print("Enter passenger ID: ");
            Long passengerId = Long.parseLong(scanner.nextLine().trim());

            List<Airport> airports = flightService.getAirportsForPassenger(passengerId);

            if (airports.isEmpty()) {
                System.out.println("This passenger has not used any airports.");
            } else {
                System.out.println("\nAirports used by passenger " + passengerId + ":");
                airports.forEach(a -> System.out.println("  - " + a.getName() + " (" + a.getCode() + ")"));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID entered.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to API: " + e.getMessage());
        }
    }
}