package com.example.flyawayt.flight.infrastructure;

import com.example.flyawayt.flight.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumber(String flightNumber);

    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineContainingIgnoreCaseAndDepartureTimeBetween(
            String flightNumber, String airline, LocalDateTime departureFrom, LocalDateTime departureTo);
}
