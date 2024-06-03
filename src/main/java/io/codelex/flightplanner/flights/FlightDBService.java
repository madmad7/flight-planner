package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightDBService extends AbstractFlightService {

    private final FlightDBRepository flightDBRepository;
    @Autowired
    public FlightDBService(FlightDBRepository flightDBRepository) {
        this.flightDBRepository = flightDBRepository;
    }

    @Autowired
    private AirportDBRepository airportDBRepository;

    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        Flight flight = convertToFlight(request);
        if (!isValidFlight(flight)) {
            throw new InvalidFlightException("Invalid flight details");
        }
        if (flightExists(flight)) {
            throw new FlightAlreadyExistsException("Flight already exists");
        }

        Airport fromAirport = airportDBRepository.save(request.getFrom());
        Airport toAirport = airportDBRepository.save(request.getTo());

        flight.setFrom(fromAirport);
        flight.setTo(toAirport);

        return flightDBRepository.save(flight);
    }

    @Override
    public Flight getFlightById(int id) {
        return flightDBRepository.findById(id).orElse(null);
    }

    @Override
    public void clearAllFlights() {
        flightDBRepository.deleteAll();
    }

    @Override
    public synchronized boolean deleteFlight(int id) {
        Flight flight = flightDBRepository.findById(id).orElse(null);
        if (flight != null) {
            flightDBRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Airport> searchAirports(String searchText) {
        return flightDBRepository.findAll().stream()
                .flatMap(flight -> Stream.of(flight.getFrom(), flight.getTo()))
                .filter(airport -> airportMatchesSearchText(airport, searchText))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);

        return flightDBRepository.findAll().stream()
                .filter(flight -> flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                        && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                        && flight.getDepartureTime().toString().startsWith(request.getDepartureDate()))
                .collect(Collectors.toList());
    }

    private boolean flightExists(Flight flight) {
        List<Flight> existingFlights = flightDBRepository.findAll();
        return existingFlights.stream()
                .anyMatch(existingFlight ->
                        existingFlight.getFrom().equals(flight.getFrom()) &&
                                existingFlight.getTo().equals(flight.getTo()) &&
                                existingFlight.getDepartureTime().equals(flight.getDepartureTime()) &&
                                existingFlight.getArrivalTime().equals(flight.getArrivalTime()));
    }

    private boolean airportMatchesSearchText(Airport airport, String searchText) {
        String trimmedSearchText = searchText.trim().toLowerCase();
        return airport.getCountry().toLowerCase().contains(trimmedSearchText) ||
                airport.getCity().toLowerCase().contains(trimmedSearchText) ||
                airport.getAirport().toLowerCase().contains(trimmedSearchText);
    }
}

