package com.flightcli.flight_cli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flightcli.flight_cli.model.Aircraft;
import com.flightcli.flight_cli.model.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private ApiClient apiClient;

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightService = new FlightService(apiClient);
    }

    // Returns a list of airports for a valid city ID
    @Test
    void getAirportsForCity_returnsListOfAirports() throws IOException, InterruptedException {
        Airport a1 = new Airport();
        a1.setId(1L); a1.setName("JFK Airport"); a1.setCode("JFK");

        Airport a2 = new Airport();
        a2.setId(2L); a2.setName("LaGuardia Airport"); a2.setCode("LGA");

        when(apiClient.getList(eq("/cities/1/airports"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(a1, a2));

        List<Airport> result = flightService.getAirportsForCity(1L);

        assertEquals(2, result.size());
        assertEquals("JFK Airport", result.get(0).getName());
        assertEquals("LGA", result.get(1).getCode());
        verify(apiClient, times(1)).getList(eq("/cities/1/airports"), any(TypeReference.class));
    }

    // Returns empty list when city has no airports
    @Test
    void getAirportsForCity_returnsEmptyList_whenNoAirports() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/cities/99/airports"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Airport> result = flightService.getAirportsForCity(99L);

        assertTrue(result.isEmpty());
    }

    // Throws IOException when the API call fails
    @Test
    void getAirportsForCity_throwsIOException_whenApiFails() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/cities/1/airports"), any(TypeReference.class)))
                .thenThrow(new IOException("Connection refused"));

        assertThrows(IOException.class, () -> flightService.getAirportsForCity(1L));
    }

    // Returns a single airport and checks its name and code are correct
    @Test
    void getAirportsForCity_returnsSingleAirport_withCorrectDetails() throws IOException, InterruptedException {
        Airport a = new Airport();
        a.setId(5L); a.setName("O'Hare International"); a.setCode("ORD");

        when(apiClient.getList(eq("/cities/5/airports"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(a));

        List<Airport> result = flightService.getAirportsForCity(5L);

        assertEquals(1, result.size());
        assertEquals("O'Hare International", result.get(0).getName());
        assertEquals("ORD", result.get(0).getCode());
    }

    // Returns a list of aircraft a passenger has flown on
    @Test
    void getAircraftForPassenger_returnsListOfAircraft() throws IOException, InterruptedException {
        Aircraft aircraft = new Aircraft();
        aircraft.setId(1L); aircraft.setType("Boeing 737");
        aircraft.setAirlineName("Delta"); aircraft.setNumberOfPassengers(180);

        when(apiClient.getList(eq("/passengers/1/aircraft"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(aircraft));

        List<Aircraft> result = flightService.getAircraftForPassenger(1L);

        assertEquals(1, result.size());
        assertEquals("Boeing 737", result.get(0).getType());
        assertEquals("Delta", result.get(0).getAirlineName());
    }

    // Returns empty list when passenger has no flight history
    @Test
    void getAircraftForPassenger_returnsEmptyList_whenNoFlights() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/5/aircraft"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Aircraft> result = flightService.getAircraftForPassenger(5L);

        assertTrue(result.isEmpty());
    }

    // Throws InterruptedException when the request is interrupted
    @Test
    void getAircraftForPassenger_throwsInterruptedException() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/1/aircraft"), any(TypeReference.class)))
                .thenThrow(new InterruptedException("Request interrupted"));

        assertThrows(InterruptedException.class, () -> flightService.getAircraftForPassenger(1L));
    }

    // Returns multiple aircraft and checks passenger capacity on each
    @Test
    void getAircraftForPassenger_returnsMultipleAircraft_withCorrectCapacity() throws IOException, InterruptedException {
        Aircraft a1 = new Aircraft();
        a1.setId(1L); a1.setType("Boeing 737"); a1.setAirlineName("Delta"); a1.setNumberOfPassengers(180);

        Aircraft a2 = new Aircraft();
        a2.setId(2L); a2.setType("Airbus A320"); a2.setAirlineName("United"); a2.setNumberOfPassengers(150);

        when(apiClient.getList(eq("/passengers/2/aircraft"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(a1, a2));

        List<Aircraft> result = flightService.getAircraftForPassenger(2L);

        assertEquals(2, result.size());
        assertEquals(180, result.get(0).getNumberOfPassengers());
        assertEquals(150, result.get(1).getNumberOfPassengers());
    }

    // Returns a list of airports used by a given aircraft
    @Test
    void getAirportsForAircraft_returnsListOfAirports() throws IOException, InterruptedException {
        Airport airport = new Airport();
        airport.setId(3L); airport.setName("Heathrow"); airport.setCode("LHR");

        when(apiClient.getList(eq("/aircraft/2/airports"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(airport));

        List<Airport> result = flightService.getAirportsForAircraft(2L);

        assertEquals(1, result.size());
        assertEquals("Heathrow", result.get(0).getName());
        assertEquals("LHR", result.get(0).getCode());
    }

    // Returns multiple airports for one aircraft
    @Test
    void getAirportsForAircraft_returnsMultipleAirports() throws IOException, InterruptedException {
        Airport a1 = new Airport(); a1.setId(1L); a1.setName("JFK"); a1.setCode("JFK");
        Airport a2 = new Airport(); a2.setId(2L); a2.setName("Heathrow"); a2.setCode("LHR");
        Airport a3 = new Airport(); a3.setId(3L); a3.setName("Charles de Gaulle"); a3.setCode("CDG");

        when(apiClient.getList(eq("/aircraft/1/airports"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(a1, a2, a3));

        List<Airport> result = flightService.getAirportsForAircraft(1L);

        assertEquals(3, result.size());
    }

    // Returns empty list when aircraft has no associated airports
    @Test
    void getAirportsForAircraft_returnsEmptyList_whenNoAirports() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/aircraft/99/airports"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Airport> result = flightService.getAirportsForAircraft(99L);

        assertTrue(result.isEmpty());
    }

    // Returns a list of airports a passenger has used
    @Test
    void getAirportsForPassenger_returnsListOfAirports() throws IOException, InterruptedException {
        Airport airport = new Airport();
        airport.setId(1L); airport.setName("JFK Airport"); airport.setCode("JFK");

        when(apiClient.getList(eq("/passengers/1/airports"), any(TypeReference.class)))
                .thenReturn(Arrays.asList(airport));

        List<Airport> result = flightService.getAirportsForPassenger(1L);

        assertEquals(1, result.size());
        assertEquals("JFK Airport", result.get(0).getName());
    }

    // Returns empty list when passenger has no airport history
    @Test
    void getAirportsForPassenger_returnsEmptyList_whenNoHistory() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/10/airports"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Airport> result = flightService.getAirportsForPassenger(10L);

        assertTrue(result.isEmpty());
    }

    // Throws IOException when API is unreachable
    @Test
    void getAirportsForPassenger_throwsIOException_whenApiFails() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/1/airports"), any(TypeReference.class)))
                .thenThrow(new IOException("API unavailable"));

        assertThrows(IOException.class, () -> flightService.getAirportsForPassenger(1L));
    }
}