package io.codelex.flightplanner.service;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.request.SearchFlightsRequest;
import io.codelex.flightplanner.dto.response.SearchFlightResponse;
import io.codelex.flightplanner.exception.DuplicateFlightException;
import io.codelex.flightplanner.exception.SameAirportException;
import io.codelex.flightplanner.exception.StrangeDatesException;
import io.codelex.flightplanner.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImplementation implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImplementation(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void add(Flight flight) throws DuplicateFlightException, SameAirportException, StrangeDatesException {
        validateFlight(flight);
        flightRepository.add(flight);
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

    public SearchFlightResponse searchFlights(SearchFlightsRequest searchFlightsRequest) throws SameAirportException {
        validateAirports(searchFlightsRequest.getFrom(), searchFlightsRequest.getTo());
        List<Flight> searchFlightsResponseContent = flightRepository.getAll().stream()
                .filter(flight -> flight.getFrom().getAirport().equals(searchFlightsRequest.getFrom()))
                .filter(flight -> flight.getTo().getAirport().equals(searchFlightsRequest.getTo()))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(searchFlightsRequest.getDepartureDate()))
                .toList();
        return new SearchFlightResponse(searchFlightsResponseContent.size(), 0, searchFlightsResponseContent);
    }

    public List<Airport> getPartialFlightSearchByAirport(String search) {
        String formattedSearch = search.toLowerCase().trim();
        return flightRepository.getAll().stream()
                .map(Flight::getFrom)
                .filter(from -> from.getCountry().toLowerCase().contains(formattedSearch)
                        || from.getCity().toLowerCase().contains(formattedSearch)
                        || from.getAirport().toLowerCase().contains(formattedSearch))
                .toList();
    }

    public List<Flight> getAll() {
        return flightRepository.getAll();
    }

    public void validateFlight(Flight flight) throws DuplicateFlightException, SameAirportException, StrangeDatesException {
        if (isDuplicateFlight(flight)) {
            throw new DuplicateFlightException("A flight like this already exists in our system.");
        }

        validateAirports(flight.getFrom().getAirport(), flight.getTo().getAirport());

        if (flight.getDepartureTime().isAfter(flight.getArrivalTime()) || flight.getDepartureTime().equals(flight.getArrivalTime())) {
            throw new StrangeDatesException("Either departure or arrival are the same, or arrival date is before departure date.");
        }
    }

    private void validateAirports(String from, String to) throws SameAirportException {
        if (from.equalsIgnoreCase(to)) {
            throw new SameAirportException("You cannot have a flight from and to the same airport.");
        }

    }

    private boolean isDuplicateFlight(Flight flight) {
        return flightRepository.getAll().stream()
                .anyMatch(existingFlight -> existingFlight.equals(flight));
    }


}

