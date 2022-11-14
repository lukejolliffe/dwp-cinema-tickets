package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticket.Ticket;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationService;

import java.util.Arrays;
import java.util.Map;


public class TicketServiceImpl implements TicketService {

    private final Map<Type, Ticket> ticketInformationMap;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final BookingValidationService bookingValidationService;

    public TicketServiceImpl(
            TicketPaymentService ticketPaymentService,
            SeatReservationService seatReservationService,
            BookingValidationService bookingValidationService,
            Map<Type, Ticket> ticketInformationMap
    ) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.bookingValidationService = bookingValidationService;
        this.ticketInformationMap = ticketInformationMap;
    }

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        //throws invalid if it fails
        bookingValidationService.validateBooking(ticketTypeRequests);

        int totalPrice = Arrays.stream(ticketTypeRequests).mapToInt(this::ticketRequestToPrice).reduce(0, Integer::sum);
        ticketPaymentService.makePayment(accountId, totalPrice);

        System.out.printf("Payment made for £%s\n", totalPrice);

        int totalSeats = Arrays.stream(ticketTypeRequests).mapToInt(this::ticketRequestToNumberOfSeats).reduce(0, Integer::sum);
        seatReservationService.reserveSeat(accountId, totalSeats);

        System.out.printf("Seat reservations made for %s seats\n", totalSeats);
    }

    private int ticketRequestToPrice(TicketTypeRequest ticketTypeRequest) {
        int numberOfTickets = ticketTypeRequest.getNoOfTickets();
        int priceOfTicket = ticketInformationMap.get(ticketTypeRequest.getTicketType()).getSeatPrice();

        return numberOfTickets * priceOfTicket;
    }

    private int ticketRequestToNumberOfSeats(TicketTypeRequest ticketTypeRequest) {
        int numberOfTickets = ticketTypeRequest.getNoOfTickets();
        int seatsTaken = ticketInformationMap.get(ticketTypeRequest.getTicketType()).getSeatsTaken();

        return numberOfTickets * seatsTaken;
    }
}
