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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FlightServiceImplementation implements FlightService {
    private final FlightRepository flightRepository;

    private final AtomicInteger idAssigner = new AtomicInteger(0);

    public FlightServiceImplementation(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void add(Flight flight) throws DuplicateFlightException, SameAirportException, StrangeDatesException {
        validateFlight(flight);
        flight.setId(idAssigner.getAndIncrement());
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
        validateAirportRoute(searchFlightsRequest.getFrom(), searchFlightsRequest.getTo());
        validateSearchFlightRequest(searchFlightsRequest);
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

    private void validateFlight(Flight flight) throws DuplicateFlightException, SameAirportException, StrangeDatesException {
        if (flight.getFrom() == null || flight.getTo() == null || flight.getCarrier() == null || flight.getCarrier().isEmpty() || flight.getDepartureTime() == null || flight.getArrivalTime() == null) {
            throw new IllegalArgumentException("Flight object cannot have null or empty fields.");
        }

        isDuplicateFlight(flight);

        validateAirportRoute(flight.getFrom().getAirport(), flight.getTo().getAirport());

        validateAirportValues(flight.getFrom(), flight.getTo());


        validateDates(flight);

    }

    private void validateAirportRoute(String from, String to) throws SameAirportException {
        if (from.toLowerCase().trim().equalsIgnoreCase(to.toLowerCase().trim())) {
            throw new SameAirportException("You cannot have a flight from and to the same airport.");
        }
    }

    private void validateAirportValues(Airport from, Airport to) {
        if (from.getCountry() == null || from.getCity() == null || from.getAirport() == null || to.getCountry() == null || to.getCity() == null || to.getAirport() == null ||
                from.getCountry().isEmpty() || from.getCity().isEmpty() || from.getAirport().isEmpty() || to.getCountry().isEmpty() || to.getCity().isEmpty() || to.getAirport().isEmpty()) {
            throw new IllegalArgumentException("Airport object cannot have null or empty fields.");
        }
    }

    private void validateSearchFlightRequest(SearchFlightsRequest sfq) {
        if (sfq.getFrom() == null || sfq.getTo() == null || sfq.getDepartureDate() == null || sfq.getFrom().isEmpty() || sfq.getTo().isEmpty()) {
            throw new IllegalArgumentException("Request cannot have null or empty values.");
        }
    }

    public void validateDates(Flight flight) throws StrangeDatesException {
        if (flight.getDepartureTime().isAfter(flight.getArrivalTime()) || flight.getDepartureTime().equals(flight.getArrivalTime())) {
            throw new StrangeDatesException("Either departure or arrival are the same, or arrival date is before departure date.");
        }
    }

    private void isDuplicateFlight(Flight flight) throws DuplicateFlightException {
        if (flightRepository.getAll().stream().anyMatch(existingFlight -> existingFlight.equals(flight))) {
            throw new DuplicateFlightException("A flight like this already exists in our system.");
        }
    }
}

