package io.codelex.flightplanner.flights.db;

import io.codelex.flightplanner.flights.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirportDBRepository extends JpaRepository<Airport, Integer> {

    @Query("SELECT a FROM Airport a WHERE " +
            "LOWER(a.country) LIKE LOWER(:parameter || '%') OR " +
            "LOWER(a.city) LIKE LOWER(:parameter || '%') OR " +
            "LOWER(a.airport) LIKE LOWER(:parameter || '%')")
    List<Airport> searchAirports(@Param("parameter") String parameter);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Airport a WHERE " +
            "LOWER(a.country) = LOWER(:country) AND " +
            "LOWER(a.city) = LOWER(:city) AND " +
            "LOWER(a.airport) = LOWER(:airport)")
    boolean existsByAirportDetails(@Param("country") String country,
                                   @Param("city") String city,
                                   @Param("airport") String airport);
}
