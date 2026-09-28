package com.example.flyawayt.booking.domain;

import com.example.flyawayt.booking.dto.BookingResponseDto;
import com.example.flyawayt.booking.events.BookingCreatedEvent;
import com.example.flyawayt.booking.infrastructure.BookingRepository;
import com.example.flyawayt.exceptions.BadRequestException;
import com.example.flyawayt.exceptions.ConflictException;
import com.example.flyawayt.exceptions.ResourceNotFoundException;
import com.example.flyawayt.flight.domain.Flight;
import com.example.flyawayt.flight.infrastructure.FlightRepository;
import com.example.flyawayt.user.domain.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository,
                          ApplicationEventPublisher applicationEventPublisher) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public BookingResponseDto book(Long flightId) {
        User customer = currentUser();

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un vuelo con id: " + flightId));

        if (!flight.getDepartureTime().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("No se puede reservar un vuelo pasado o en tránsito");
        }
        if (flight.getAvailableSeats() <= 0) {
            throw new ConflictException("El vuelo no tiene asientos disponibles");
        }
        if (bookingRepository.existsByCustomerIdAndFlightDepartureTimeBeforeAndFlightArrivalTimeAfter(
                customer.getId(), flight.getArrivalTime(), flight.getDepartureTime())) {
            throw new ConflictException("Ya tienes una reserva que se cruza con el horario de este vuelo");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        bookingRepository.save(booking);

        applicationEventPublisher.publishEvent(new BookingCreatedEvent(this, booking));

        return toResponseDto(booking);
    }

    public BookingResponseDto findById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una reserva con id: " + id));
        return toResponseDto(booking);
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(booking.getId());
        response.setFlightId(booking.getFlight().getId());
        response.setFlightNumber(booking.getFlight().getFlightNumber());
        response.setDepartureTime(booking.getFlight().getDepartureTime());
        response.setArrivalTime(booking.getFlight().getArrivalTime());
        response.setCustomerId(booking.getCustomer().getId());
        response.setCustomerFirstName(booking.getCustomer().getFirstName());
        response.setCustomerLastName(booking.getCustomer().getLastName());
        response.setBookingDate(booking.getBookingDate());
        return response;
    }
}
