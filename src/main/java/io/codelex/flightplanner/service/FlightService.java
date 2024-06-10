package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.request.SearchFlightsRequest;
import io.codelex.flightplanner.dto.response.SearchFlightResponse;
import io.codelex.flightplanner.exception.DuplicateFlightException;
import io.codelex.flightplanner.exception.SameAirportException;
import io.codelex.flightplanner.exception.StrangeDatesException;

import java.util.List;

public interface FlightService {

    void add(Flight flight) throws DuplicateFlightException, SameAirportException, StrangeDatesException;

    Flight getFlightById(int id);

    void deleteFlightById(int id);

    void clearFlightList();

    SearchFlightResponse searchFlights(SearchFlightsRequest searchFlightsRequest) throws SameAirportException;

    List<Airport> getPartialFlightSearchByAirport(String search);

    public List<Flight> getAll();

}
