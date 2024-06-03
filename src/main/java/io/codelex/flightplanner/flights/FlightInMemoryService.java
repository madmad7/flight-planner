package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightInMemoryService extends AbstractFlightService {

    private final FlightInMemoryRepository flightInMemoryRepository;

    @Autowired
    public FlightInMemoryService(FlightInMemoryRepository flightInMemoryRepository) {
        this.flightInMemoryRepository = flightInMemoryRepository;
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        Flight flight = convertToFlight(request);
        if (!isValidFlight(flight)) {
            throw new InvalidFlightException("Invalid flight details");
        }
        if (flightInMemoryRepository.flightExists(flight)) {
            throw new FlightAlreadyExistsException("Flight already exists");
        }
        flightInMemoryRepository.addFlight(flight);
        return flight;
    }

    @Override
    public Flight getFlightById(int id) {
        return flightInMemoryRepository.getFlightById(id);
    }

    @Override
    public void clearAllFlights() {
        flightInMemoryRepository.clearAllFlights();
    }

    @Override
    public synchronized boolean deleteFlight(int id) {
        Flight flight = flightInMemoryRepository.getFlightById(id);
        if (flight != null) {
            flightInMemoryRepository.deleteFlight(id);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public List<Airport> searchAirports(String searchText) {
        return flightInMemoryRepository.getAllFlights().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airportMatchesSearchText(airport, searchText))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);

        return flightInMemoryRepository.getAllFlights().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                        && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                        && flight.getDepartureTime().toString().startsWith(request.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private boolean airportMatchesSearchText(Airport airport, String searchText) {
        String trimmedSearchText = searchText.trim().toLowerCase();
        return airport.getCountry().toLowerCase().contains(trimmedSearchText) ||
                airport.getCity().toLowerCase().contains(trimmedSearchText) ||
                airport.getAirport().toLowerCase().contains(trimmedSearchText);
    }
}


