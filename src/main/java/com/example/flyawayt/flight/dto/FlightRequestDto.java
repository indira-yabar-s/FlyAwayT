package com.example.flyawayt.flight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FlightRequestDto {

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "solo A-Z y 0-9, máximo 6 caracteres")
    private String flightNumber;

    @NotBlank
    private String airline;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    @Positive
    private Integer availableSeats;
}
