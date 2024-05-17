package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    @Value("${flight.date-pattern}")
    private String datePattern;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public boolean addFlight(Flight flight) {
        if (!isValidFlight(flight)) {
            throw new BadRequestException("Invalid flight details");
        }

        if (!flightRepository.flightExists(flight)) {
            flightRepository.addFlight(flight);
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidFlight(Flight flight) {
        return flight != null &&
                isAirportValid(flight.getFrom()) &&
                isAirportValid(flight.getTo()) &&
                !airportsAreSame(flight.getFrom(), flight.getTo()) &&
                flight.getCarrier() != null && !flight.getCarrier().trim().isEmpty() &&
                flight.getDepartureTime() != null &&
                flight.getArrivalTime() != null &&
                isArrivalAfterDeparture(flight.getDepartureTime(), flight.getArrivalTime());

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

    private boolean isArrivalAfterDeparture(String departureTime, String arrivalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime departure = LocalDateTime.parse(departureTime, formatter);
        LocalDateTime arrival = LocalDateTime.parse(arrivalTime, formatter);
        return arrival.isAfter(departure);
    }

    public Flight getFlightById(int id) {
        return flightRepository.getFlightById(id);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    public void clearAllFlights(){
        flightRepository.clearAllFlights();
    }

}

