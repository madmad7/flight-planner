package io.codelex.flightplanner.flights;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepository {

    private List<Flight> flightList = new ArrayList<>();

    public void add(Flight flight) {
        flightList.add(flight);
    }

    public Flight getFlightById(int id) {
        for (Flight flight : flightList) {
            if (flight.getId() == id) {
                return flight;
            }
        }
        return null;
    }

    public Flight getFlightByDestination(String destinationName) {
        for (Flight flight  : flightList) {
            if (flight.getTo().equals(destinationName)) {
                return flight;
            }
        }
        return null;
    }

    public List<Flight> getAllFlights(){
        return new ArrayList<>(flightList);
    }

    public void clearAllFlights(){
        flightList.clear();
    }

}


//public FlightRepository() {
//    Flight sampleFlight = new Flight("Riga", "Barcelona", 122);
//    flightList.add(sampleFlight);
//}
