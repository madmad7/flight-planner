package io.codelex.flightplanner.flights;

import io.codelex.flightplanner.exceptions.BadRequestException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class FlightRepository {

    private List<Flight> flightList = new ArrayList<>();
    private AtomicInteger nextId = new AtomicInteger(1);

    public void addFlight(Flight flight) {
        if (flight == null || isAnyFieldNull(flight) || flightExists(flight)) {
            throw new BadRequestException("Invalid flight details");
        }

        int id = nextId.getAndIncrement();
        flight.setId(id);
        flightList.add(flight);
    }

    public boolean flightExists(Flight newFlight) {
        for (Flight flight : flightList) {
            if (flightsAreEqual(flight, newFlight)) {
                return true;
            }
        }
        return false;
    }

    private boolean flightsAreEqual(Flight flight1, Flight flight2) {
        return flight1.getFrom().equals(flight2.getFrom()) &&
                flight1.getTo().equals(flight2.getTo()) &&
                flight1.getCarrier().equals(flight2.getCarrier()) &&
                flight1.getDepartureTime().equals(flight2.getDepartureTime()) &&
                flight1.getArrivalTime().equals(flight2.getArrivalTime());
    }

    private boolean isAnyFieldNull(Flight flight) {
        return flight.getFrom() == null ||
                flight.getTo() == null ||
                flight.getCarrier() == null ||
                flight.getDepartureTime() == null ||
                flight.getArrivalTime() == null;
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
