package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exception.DuplicateFlightException;
import io.codelex.flightplanner.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImplementation implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImplementation(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void add(Flight flight) throws DuplicateFlightException {
        if(isDuplicateFlight(flight)) {
            throw new DuplicateFlightException("A flight like this already exists in our system.");
        } else flightRepository.add(flight);
    }

    public Flight getFlightById(int id) {
        return flightRepository.getFlightById(id);
    }

    public void deleteFlightById(int id) {
        flightRepository.deleteFlightById(id);
    }

    public void clearFlightList() {
        flightRepository.clearFlightList();
    }

    public List<Flight> getAll() {
        return flightRepository.getAll();
    }

    private boolean isDuplicateFlight(Flight flight) {
        return flightRepository.getAll().stream()
                .anyMatch(existingFlight -> existingFlight.equals(flight));
    }
}

