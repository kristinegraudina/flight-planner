package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.exception.DuplicateFlightException;
import io.codelex.flightplanner.exception.SameAirportException;
import io.codelex.flightplanner.exception.StrangeDatesException;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/admin-api/flights")
public class AdminFlightController {
    private final FlightService flightService;
    private final AtomicInteger idAssigner = new AtomicInteger(0);

    public AdminFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping()
    public Flight addFlight(@RequestBody Flight flight) {
        flight.setId(idAssigner.getAndIncrement());
        try {
            flightService.add(flight);
        } catch (DuplicateFlightException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (SameAirportException | StrangeDatesException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return flight;
    }

    @GetMapping("/getall")
    public List<Flight> getAll() {
        return flightService.getAll();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There was no such flight in our list.");
        } else return flight;
    }

    @DeleteMapping("/{id}")
    public void deleteFlightById(@PathVariable int id) {
        flightService.deleteFlightById(id);
    }
}
