package io.codelex.flightplanner.flights;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void add(Flight flight) {
        flightRepository.add(flight);
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

