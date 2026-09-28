package com.example.flyawayt.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long flightId;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private LocalDateTime bookingDate;
}
