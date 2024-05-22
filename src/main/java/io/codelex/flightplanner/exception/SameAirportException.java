package io.codelex.flightplanner.exception;

public class SameAirportException extends Exception {
    public SameAirportException() {
        super();
    }

    public SameAirportException(String message) {
        super(message);
    }
}
