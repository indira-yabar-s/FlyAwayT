package com.example.flyawayt.booking.events;

import com.example.flyawayt.booking.domain.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Component
public class BookingEmailNotifier {

    private final Logger logger = LoggerFactory.getLogger(BookingEmailNotifier.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendConfirmationEmail(BookingCreatedEvent event) {
        Booking booking = event.getBooking();
        DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        String content = "Confirmación de reserva - Fly Away Travel\n"
                + "Reserva: " + booking.getId() + "\n"
                + "Pasajero: " + booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName() + "\n"
                + "Número de vuelo: " + booking.getFlight().getFlightNumber() + "\n"
                + "Salida: " + booking.getFlight().getDepartureTime().format(iso) + "\n"
                + "Llegada: " + booking.getFlight().getArrivalTime().format(iso) + "\n"
                + "Fecha de reserva: " + booking.getBookingDate().format(iso) + "\n";

        try {
            Files.writeString(Path.of("flight_booking_email_" + booking.getId() + ".txt"), content);
        } catch (IOException exception) {
            logger.error("No se pudo crear el email de la reserva {}: {}", booking.getId(), exception.getMessage());
        }
    }
}
