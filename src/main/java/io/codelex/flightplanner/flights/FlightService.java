package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public synchronized Flight addFlight(AddFlightRequest request) {
        Flight flight = convertToFlight(request);
        if (!isValidFlight(flight)) {
            throw new InvalidFlightException("Invalid flight details");
        }
        if (flightRepository.flightExists(flight)) {
            throw new FlightAlreadyExistsException("Flight already exists");
        }
        flightRepository.addFlight(flight);
        return flight;
    }

    public Flight convertToFlight(AddFlightRequest request) {
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

    private boolean isValidAddFlightRequest(AddFlightRequest request) {
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

    public ResponseToFlightRequest convertToResponseFlight(Flight flight) {
        return ResponseToFlightRequest.fromFlight(flight);
    }


    private boolean isValidFlight(Flight flight) {
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

    public Flight getFlightById(int id) {
        return flightRepository.getFlightById(id);
    }

    public void clearAllFlights() {
        flightRepository.clearAllFlights();
    }

    public synchronized boolean deleteFlight(int id) {
        Flight flight = flightRepository.getFlightById(id);
        if (flight != null) {
            flightRepository.deleteFlight(id);
            return true;
        } else {
            return true;
        }
    }

    public List<Airport> searchAirports(String searchText) {
        return flightRepository.getAllFlights().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airportMatchesSearchText(airport, searchText))
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean airportMatchesSearchText(Airport airport, String searchText) {
        String trimmedSearchText = searchText.trim().toLowerCase();
        return airport.getCountry().toLowerCase().contains(trimmedSearchText) ||
                airport.getCity().toLowerCase().contains(trimmedSearchText) ||
                airport.getAirport().toLowerCase().contains(trimmedSearchText);
    }

    public List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);

        return flightRepository.getAllFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                        && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                        && flight.getDepartureTime().toString().startsWith(request.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private void validateSearchRequest(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null ||
                request.getFrom().trim().isEmpty() || request.getTo().trim().isEmpty() || request.getDepartureDate().trim().isEmpty() ||
                request.getFrom().equalsIgnoreCase(request.getTo())) {
            throw new BadRequestException("Invalid search request");
        }
    }

}



