package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
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
    public ResponseEntity<Flight> getFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);

        if (flight == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(flight, HttpStatus.OK);
        }
    }

    @PostMapping("/testing-api/clear")
    public ResponseEntity<Void> clearFlights() {
        flightService.clearAllFlights();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin-api/flights")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        try {
            // Attempt to add the flight
            boolean addedSuccessfully = flightService.addFlight(flight);

            // If the flight was added successfully, return 201 Created
            if (addedSuccessfully) {
                return new ResponseEntity<>(flight, HttpStatus.CREATED);
            } else {
                // If the flight already exists, return 409 Conflict
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (BadRequestException e) {
            // If the request is invalid, return 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}




//    @GetMapping("/flights/123")
//    public List<Flight> getFlightList() {
//        return flightService.getAllFlights();
//    }

//    @GetMapping("/flights/{id}")
//    public Flight getFlightById(@PathVariable int id) {
//        return flightService.getFlightById(id);
//    }




//    @GetMapping("/flights}")
//    public ResponseEntity<List<Flight>> getFlightList() {
//        List<Flight> flights = flightService.getAllFlights();
//        if (flights.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>(flights, HttpStatus.OK);
//        }
//    }
