package io.codelex.flightplanner.flights;


import java.util.Objects;
public class AirportResponse {
    private String country;
    private String city;
    private String airport;

    public AirportResponse() {}

    public AirportResponse(Airport airport) {
        this.country = airport.getCountry();
        this.city = airport.getCity();
        this.airport = airport.getAirport();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public String toString() {
        return "AirportResponse{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", airport='" + airport + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirportResponse that = (AirportResponse) o;
        return Objects.equals(country, that.country) && Objects.equals(city, that.city) && Objects.equals(airport, that.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}