package com.example.flyawayt.booking.infrastructure;

import com.example.flyawayt.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByCustomerIdAndFlightDepartureTimeBeforeAndFlightArrivalTimeAfter(
            Long customerId, LocalDateTime arrivalTime, LocalDateTime departureTime);
}
