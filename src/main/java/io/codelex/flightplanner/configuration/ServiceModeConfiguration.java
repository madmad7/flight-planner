package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.flights.*;
import io.codelex.flightplanner.flights.db.AirportDBRepository;
import io.codelex.flightplanner.flights.db.FlightDBRepository;
import io.codelex.flightplanner.flights.db.FlightDBService;
import io.codelex.flightplanner.flights.inMemory.FlightInMemoryRepository;
import io.codelex.flightplanner.flights.inMemory.FlightInMemoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceModeConfiguration {

    @Value("database")
    private String storageMode;

    @Bean
    @ConditionalOnProperty(name = "flight-storage-mode", havingValue = "database")
    public FlightDBService createFlightDbService(FlightDBRepository flightDBRepository, AirportDBRepository airportRepository) {
        return new FlightDBService(flightDBRepository, airportRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "flight-storage-mode", havingValue = "in-memory")
    public FlightInMemoryService createFlightInMemoryService(FlightInMemoryRepository flightInMemoryRepository) {
        return new FlightInMemoryService(flightInMemoryRepository);
    }

    @Bean
    public AbstractFlightService flightService(FlightDBService flightDBService, FlightInMemoryService flightInMemoryService) {
        if ("database".equals(storageMode)) {
            return flightDBService;
        } else {
            return flightInMemoryService;
        }
    }
}