package io.codelex.flightplanner.flights;

import java.time.format.DateTimeFormatter;

public class ResponseToFlightRequest {

    private AirportResponse from;
    private AirportResponse to;
    private String carrier;
    private String departureTime;
    private String arrivalTime;
    private int id;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ResponseToFlightRequest() {
    }

    public static ResponseToFlightRequest fromFlight(Flight flight) {
        ResponseToFlightRequest responseToFlightRequest = new ResponseToFlightRequest();
        responseToFlightRequest.setId(flight.getId());
        responseToFlightRequest.setFrom(new AirportResponse(flight.getFrom()));
        responseToFlightRequest.setTo(new AirportResponse(flight.getTo()));
        responseToFlightRequest.setCarrier(flight.getCarrier());
        responseToFlightRequest.setDepartureTime(flight.getDepartureTime().format(formatter));
        responseToFlightRequest.setArrivalTime(flight.getArrivalTime().format(formatter));
        return responseToFlightRequest;
    }

    public AirportResponse getFrom() {
        return from;
    }

    public void setFrom(AirportResponse from) {
        this.from = from;
    }

    public AirportResponse getTo() {
        return to;
    }

    public void setTo(AirportResponse to) {
        this.to = to;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}




