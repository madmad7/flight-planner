package io.codelex.flightplanner.flights;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportDBRepository extends JpaRepository<Airport, Long> {
}
