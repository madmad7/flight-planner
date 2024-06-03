package io.codelex.flightplanner.flights;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDBRepository extends JpaRepository<Flight, Integer> {
}

