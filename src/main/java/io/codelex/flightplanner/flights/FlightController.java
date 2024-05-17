package io.codelex.flightplanner.flights;

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
