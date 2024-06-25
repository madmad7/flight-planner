package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import io.codelex.flightplanner.exceptions.FlightAlreadyExistsException;
import io.codelex.flightplanner.exceptions.FlightNotFoundException;
import io.codelex.flightplanner.exceptions.InvalidFlightException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FlightController {
    private final AbstractFlightService flightService;
    public FlightController(AbstractFlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/admin-api/flights/{id}")
    public ResponseToFlightRequest getFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException("Flight not found with id: " + id);
        }
        return flightService.convertToResponseFlight(flight);
    }

    @PostMapping("/testing-api/clear")
    public void clearFlights() {
        flightService.clearAllFlights();
    }

    @PutMapping("/admin-api/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseToFlightRequest addFlight(@RequestBody AddFlightRequest request) {
        try {
            Flight flight = flightService.addFlight(request);
            return flightService.convertToResponseFlight(flight);
        } catch (FlightAlreadyExistsException e) {
            throw new FlightAlreadyExistsException("Flight already exists");
        } catch (BadRequestException e) {
            throw new InvalidFlightException("Invalid flight details");
        }
    }

    @DeleteMapping("/admin-api/flights/{id}")
    public void deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
    }

    @GetMapping("/api/airports")
    public List<AirportResponse> searchAirports(@RequestParam String search) {
        List<Airport> airports = flightService.searchAirports(search);
        return airports.stream()
                .map(AirportResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/flights/search")
    public PageResult<ResponseToFlightRequest> searchFlights(@RequestBody SearchFlightsRequest request) {
        List<Flight> flights = flightService.searchFlights(request);
        List<ResponseToFlightRequest> responseToFlightRequests = flights.stream()
                .map(flightService::convertToResponseFlight)
                .toList();
        return new PageResult<>(0, responseToFlightRequests.size(), responseToFlightRequests);
    }

    @GetMapping("/api/flights/{id}")
    public ResponseToFlightRequest findFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) {
            throw new FlightNotFoundException("Flight not found");
        }
        return flightService.convertToResponseFlight(flight);
    }
}