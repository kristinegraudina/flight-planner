package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exception.DuplicateFlightException;

import java.util.List;

public interface FlightService {

    void add(Flight flight) throws DuplicateFlightException;

    Flight getFlightById(int id);

    void deleteFlightById(int id);

    void clearFlightList();

    public List<Flight> getAll();

}
