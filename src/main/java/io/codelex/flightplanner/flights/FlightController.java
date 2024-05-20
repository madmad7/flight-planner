package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.FlightNotFoundException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/admin-api/flights/{id}")
    public Flight getFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException("Flight not found with id: " + id);
        } else {
            return flight;
        }
    }

    @PostMapping("/testing-api/clear")
    public void clearFlights() {
        flightService.clearAllFlights();
    }

    @PutMapping("/admin-api/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@RequestBody Flight flight) {
        try {
            boolean addedSuccessfully = flightService.addFlight(flight);
            if (addedSuccessfully) {
                return flight;
            } else {
                throw new FlightAlreadyExistsException("Flight already exists");
            }
        } catch (BadRequestException e) {
            throw new InvalidFlightException("Invalid flight details");
        }
    }

    @DeleteMapping("/admin-api/flights/{id}")
    public void deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
    }

    @GetMapping("/api/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }

    @PostMapping("/api/flights/search")
    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest request) {
            List<Flight> flights = flightService.searchFlights(request);
            return new PageResult<>(0, flights.size(), flights);
    }


    @GetMapping("/api/flights/{id}")
    public Flight findFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException("Flight not found");
        }
        return flight;
    }

}
