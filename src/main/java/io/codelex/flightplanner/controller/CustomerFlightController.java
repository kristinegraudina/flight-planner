package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.dto.request.SearchFlightsRequest;
import io.codelex.flightplanner.dto.response.SearchFlightResponse;
import io.codelex.flightplanner.exception.SameAirportException;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerFlightController {
    private final FlightService flightService;

    public CustomerFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.getPartialFlightSearchByAirport(search);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/flights/search")
    public SearchFlightResponse searchFlights(@RequestBody SearchFlightsRequest searchFlightsRequest) {
        try {
            return flightService.searchFlights(searchFlightsRequest);
        } catch (SameAirportException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/flights/{id}")
    public Flight getFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such flight on our list.");
        } else return flight;
    }
}
