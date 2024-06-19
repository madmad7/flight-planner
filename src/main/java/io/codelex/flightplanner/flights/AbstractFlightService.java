package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public abstract class AbstractFlightService {

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public abstract Flight addFlight(AddFlightRequest request);

    public abstract Flight getFlightById(int id);

    public abstract void clearAllFlights();

    public abstract boolean deleteFlight(int id);

    public abstract List<Airport> searchAirports(String searchText);

    public abstract List<Flight> searchFlights(SearchFlightsRequest request);

    protected Flight convertToFlight(AddFlightRequest request) {
        if (!isValidAddFlightRequest(request)) {
            throw new InvalidFlightException("Invalid flight details");
        }

        LocalDateTime departureTime = LocalDateTime.parse(request.getDepartureTime(), formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(request.getArrivalTime(), formatter);

        return new Flight(
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                departureTime,
                arrivalTime
        );
    }

    protected boolean isValidAddFlightRequest(AddFlightRequest request) {
        if (request == null) {
            return false;
        }

        if (request.getFrom() == null ||
                request.getTo() == null ||
                request.getCarrier() == null || request.getCarrier().trim().isEmpty() ||
                request.getDepartureTime() == null ||
                request.getArrivalTime() == null) {
            return false;
        }

        try {
            LocalDateTime departureTime = LocalDateTime.parse(request.getDepartureTime(), formatter);
            LocalDateTime arrivalTime = LocalDateTime.parse(request.getArrivalTime(), formatter);

            if (!arrivalTime.isAfter(departureTime)) {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }

    protected boolean isValidFlight(Flight flight) {
        return flight != null &&
                isAirportValid(flight.getFrom()) &&
                isAirportValid(flight.getTo()) &&
                !airportsAreSame(flight.getFrom(), flight.getTo()) &&
                flight.getCarrier() != null && !flight.getCarrier().trim().isEmpty() &&
                flight.getDepartureTime() != null &&
                flight.getArrivalTime() != null &&
                flight.getArrivalTime().isAfter(flight.getDepartureTime());
    }

    private boolean isAirportValid(Airport airport) {
        return airport != null &&
                airport.getCountry() != null && !airport.getCountry().trim().isEmpty() &&
                airport.getCity() != null && !airport.getCity().trim().isEmpty() &&
                airport.getAirport() != null && !airport.getAirport().trim().isEmpty();
    }

    private boolean airportsAreSame(Airport from, Airport to) {
        return from.getCountry().trim().equalsIgnoreCase(to.getCountry().trim()) &&
                from.getCity().trim().equalsIgnoreCase(to.getCity().trim()) &&
                from.getAirport().trim().equalsIgnoreCase(to.getAirport().trim());
    }

    public ResponseToFlightRequest convertToResponseFlight(Flight flight) {
        return ResponseToFlightRequest.fromFlight(flight);
    }

    protected void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null ||
                request.getFrom().trim().isEmpty() || request.getTo().trim().isEmpty() || request.getDepartureDate().trim().isEmpty() ||
                request.getFrom().equalsIgnoreCase(request.getTo())) {
            throw new BadRequestException("Invalid search request");
        }
    }
}


