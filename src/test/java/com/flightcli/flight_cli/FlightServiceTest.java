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

    // Q1: Airports for city

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

    @Test
    void getAirportsForCity_returnsEmptyList_whenNoAirports() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/cities/99/airports"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Airport> result = flightService.getAirportsForCity(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAirportsForCity_throwsIOException_whenApiFails() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/cities/1/airports"), any(TypeReference.class)))
                .thenThrow(new IOException("Connection refused"));

        assertThrows(IOException.class, () -> flightService.getAirportsForCity(1L));
    }

    // Q2: Aircraft for passenger

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

    @Test
    void getAircraftForPassenger_returnsEmptyList_whenNoFlights() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/5/aircraft"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Aircraft> result = flightService.getAircraftForPassenger(5L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAircraftForPassenger_throwsInterruptedException() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/1/aircraft"), any(TypeReference.class)))
                .thenThrow(new InterruptedException("Request interrupted"));

        assertThrows(InterruptedException.class, () -> flightService.getAircraftForPassenger(1L));
    }

    // Q3: Airports for aircraft

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

    // Q4: Airports for passenger

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

    @Test
    void getAirportsForPassenger_returnsEmptyList_whenNoHistory() throws IOException, InterruptedException {
        when(apiClient.getList(eq("/passengers/10/airports"), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<Airport> result = flightService.getAirportsForPassenger(10L);

        assertTrue(result.isEmpty());
    }
}