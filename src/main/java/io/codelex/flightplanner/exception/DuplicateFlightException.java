package io.codelex.flightplanner.exception;

public class DuplicateFlightException extends Exception {
    public DuplicateFlightException() {
        super();
    }

    public DuplicateFlightException(String message) {
        super(message);
    }
}
