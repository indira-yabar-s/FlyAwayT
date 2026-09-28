package com.example.flyawayt.booking.application;

import com.example.flyawayt.booking.domain.BookingService;
import com.example.flyawayt.booking.dto.BookingRequestDto;
import com.example.flyawayt.booking.dto.BookingResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponseDto> book(@Valid @RequestBody BookingRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request.getFlightId()));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
}
