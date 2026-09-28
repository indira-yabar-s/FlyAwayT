package com.example.flyawayt.flight.domain;

import com.example.flyawayt.exceptions.BadRequestException;
import com.example.flyawayt.exceptions.ConflictException;
import com.example.flyawayt.flight.dto.FlightRequestDto;
import com.example.flyawayt.flight.dto.FlightResponseDto;
import com.example.flyawayt.flight.infrastructure.FlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    public FlightService(FlightRepository flightRepository, ModelMapper modelMapper) {
        this.flightRepository = flightRepository;
        this.modelMapper = modelMapper;
    }

    public FlightResponseDto create(FlightRequestDto request) {
        if (!request.getDepartureTime().isBefore(request.getArrivalTime())) {
            throw new BadRequestException("La hora de salida debe ser anterior a la hora de llegada");
        }
        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new ConflictException("Ya existe un vuelo con el número: " + request.getFlightNumber());
        }

        Flight flight = modelMapper.map(request, Flight.class);
        flightRepository.save(flight);

        return modelMapper.map(flight, FlightResponseDto.class);
    }

    public List<FlightResponseDto> search(String flightNumber, String airline,
                                          LocalDateTime departureFrom, LocalDateTime departureTo) {
        String number = flightNumber == null ? "" : flightNumber;
        String airlineName = airline == null ? "" : airline;
        LocalDateTime from = departureFrom == null ? LocalDateTime.of(1900, 1, 1, 0, 0) : departureFrom;
        LocalDateTime to = departureTo == null ? LocalDateTime.of(3000, 1, 1, 0, 0) : departureTo;

        return flightRepository
                .findByFlightNumberContainingIgnoreCaseAndAirlineContainingIgnoreCaseAndDepartureTimeBetween(
                        number, airlineName, from, to)
                .stream()
                .map(flight -> modelMapper.map(flight, FlightResponseDto.class))
                .toList();
    }
}
