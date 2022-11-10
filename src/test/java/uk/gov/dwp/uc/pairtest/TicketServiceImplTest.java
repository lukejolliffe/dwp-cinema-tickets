package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.ticket.Ticket;
import uk.gov.dwp.uc.pairtest.ticket.TicketImpl;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationService;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TicketServiceImplTest {

    @Test
    public void purchaseTickets() {
        TicketPaymentService ticketPaymentService = Mockito.mock(TicketPaymentService.class);
        SeatReservationService seatReservationService = Mockito.mock(SeatReservationService.class);
        BookingValidationService bookingValidationService = Mockito.mock(BookingValidationService.class);

        Map<Type, Ticket> ticketTypeMap = new EnumMap<>(Type.class);
        ticketTypeMap.put(Type.ADULT, new TicketImpl(1, 20));
        ticketTypeMap.put(Type.CHILD, new TicketImpl(1, 10));
        ticketTypeMap.put(Type.INFANT, new TicketImpl(0,0));

        TicketServiceImpl sut = new TicketServiceImpl(
                ticketPaymentService,
                seatReservationService,
                bookingValidationService,
                ticketTypeMap
        );

        sut.purchaseTickets(
                123l,
                new TicketTypeRequest(Type.CHILD, 5),
                new TicketTypeRequest(Type.ADULT, 3))
        ;

        Mockito.verify(ticketPaymentService).makePayment(123l, 110);
        Mockito.verify(seatReservationService).reserveSeat(123l, 8);
    }
}