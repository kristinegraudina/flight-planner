package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepository {
    private final List<Flight> flightList;

    public FlightRepository() {
        this.flightList = new ArrayList<>();
    }

    public void add(Flight flight) {
        this.flightList.add(flight);
    }

    public Flight getFlightById(int id) {
        return this.flightList.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteFlightById(int id) {
        Flight flightToDelete = flightList.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst()
                .orElse(null);
        flightList.remove(flightToDelete);
    }

    public void clearFlightList() {
        this.flightList.clear();
    }

    public List<Flight> getAll() {
        return flightList;
    }

}
