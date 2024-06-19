package io.codelex.flightplanner.flights.db;

import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import io.codelex.flightplanner.flights.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightDBService extends AbstractFlightService {


    private final FlightDBRepository flightRepository;
    private final AirportDBRepository airportRepository;

    public FlightDBService(FlightDBRepository flightRepository, AirportDBRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public synchronized Flight addFlight(AddFlightRequest request) {
        Flight flight = convertToFlight(request);

        if (!isValidFlight(flight)) {
            throw new InvalidFlightException("Invalid flight details");
        }
        if (flightExists(flight)) {
            throw new FlightAlreadyExistsException("Flight already exists");
        }

        return flightRepository.save(flight);
    }

    @Override
    public Flight getFlightById(int id) {
        return flightRepository.findById(id).orElse(null);
    }

    @Override
    public void clearAllFlights() {
        flightRepository.deleteAll();
    }

    @Override
    public synchronized boolean deleteFlight(int id) {
        Flight flight = flightRepository.findById(id).orElse(null);
        if (flight != null) {
            flightRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Airport> searchAirports(String parameter){
        String trimmedParameter = parameter.trim();
        return airportRepository.searchAirports(trimmedParameter);
    }

    @Override
    public List<Flight> searchFlights(SearchFlightsRequest request) {
        validateSearchRequest(request);

        return flightRepository.searchFlights(
                request.getFrom(),
                request.getTo(),
                request.getDepartureDate());
    }

    private boolean flightExists(Flight flight) {
        return flightRepository.existsByFlightDetails(
                flight.getFrom(),
                flight.getTo(),
                flight.getDepartureTime(),
                flight.getArrivalTime());
    }

    private Airport handleAirport(Airport airport) {
        if (airport == null) {
            throw new InvalidFlightException("Airport cannot be null");
        }

        if (airportExists(airport)) {
            Optional<Airport> optionalAirport = airportRepository.searchAirports(airport.getCountry())
                    .stream()
                    .filter(a -> a.getCity().equalsIgnoreCase(airport.getCity()) && a.getAirport().equalsIgnoreCase(airport.getAirport()))
                    .findFirst();
            return optionalAirport.map(foundAirport ->
                            airportRepository.findById(foundAirport.getId())
                                    .orElseThrow(() -> new InvalidFlightException("Airport not found")))
                    .orElseThrow(() -> new InvalidFlightException("Airport not found"));

        } else {
            return airportRepository.save(airport);
        }
    }


    private boolean airportExists(Airport airport) {
        return airportRepository.existsByAirportDetails(
                airport.getCountry(),
                airport.getCity(),
                airport.getAirport());
    }

    @Override
    protected Flight convertToFlight(AddFlightRequest request) {
        if (!isValidAddFlightRequest(request)) {
            throw new InvalidFlightException("Invalid flight details");
        }

        LocalDateTime departureTime = LocalDateTime.parse(request.getDepartureTime(), formatter);
        LocalDateTime arrivalTime = LocalDateTime.parse(request.getArrivalTime(), formatter);

        Airport fromAirport = handleAirport(request.getFrom());
        Airport toAirport = handleAirport(request.getTo());

        return new Flight(
                fromAirport,
                toAirport,
                request.getCarrier(),
                departureTime,
                arrivalTime
        );
    }
}
