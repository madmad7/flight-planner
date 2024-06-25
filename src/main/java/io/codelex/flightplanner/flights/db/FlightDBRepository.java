package io.codelex.flightplanner.flights.db;

import io.codelex.flightplanner.flights.Airport;
import io.codelex.flightplanner.flights.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDBRepository extends JpaRepository<Flight, Integer> {

    @Query("SELECT f FROM Flight f WHERE " +
            "LOWER(f.from.airport) LIKE LOWER(CONCAT('%', :from, '%')) AND " +
            "LOWER(f.to.airport) LIKE LOWER(CONCAT('%', :to, '%')) AND " +
            "TO_CHAR(f.departureTime, 'YYYY-MM-DD') = :departureDate")
    List<Flight> searchFlights(@Param("from") String from,
                               @Param("to") String to,
                               @Param("departureDate") String departureDate);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Flight f WHERE " +
            "f.from = :from AND " +
            "f.to = :to AND " +
            "f.departureTime = :departureTime AND " +
            "f.arrivalTime = :arrivalTime")
    boolean existsByFlightDetails(@Param("from") Airport from,
                                  @Param("to") Airport to,
                                  @Param("departureTime") LocalDateTime departureTime,
                                  @Param("arrivalTime") LocalDateTime arrivalTime);
}

