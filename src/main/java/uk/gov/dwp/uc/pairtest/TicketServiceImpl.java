package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticket.Ticket;
import uk.gov.dwp.uc.pairtest.ticket.TicketImpl;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationService;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.validation.rules.MaxTicketsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumAdultsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumTicketsRule;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;


public class TicketServiceImpl implements TicketService {

    private final Map<Type, Ticket> ticketInformationMap;
    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationService;
    private BookingValidationService bookingValidationService;

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

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Number of Adult Tickets");
        int adult = reader.nextInt();
        System.out.println("Number of Child Tickets");
        int child = reader.nextInt();
        System.out.println("Number of Infant Tickets");
        int infant = reader.nextInt();
        reader.close();

        TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();
        SeatReservationService seatReservationService = new SeatReservationServiceImpl();
        BookingValidationService bookingValidationService = new BookingValidationServiceImpl(
                new MaxTicketsRule(20),
                new MinimumAdultsRule(),
                new MinimumTicketsRule(0)
        );
        Map<Type, Ticket> ticketInfoMap = new EnumMap<>(Type.class);
        ticketInfoMap.put(Type.ADULT, new TicketImpl(1, 20));
        ticketInfoMap.put(Type.CHILD, new TicketImpl(1, 10));
        ticketInfoMap.put(Type.INFANT, new TicketImpl(0,0));

        TicketService service = new TicketServiceImpl(ticketPaymentService,seatReservationService,bookingValidationService,ticketInfoMap);
        service.purchaseTickets(
                123456l,
                new TicketTypeRequest(Type.ADULT, adult),
                new TicketTypeRequest(Type.CHILD, child),
                new TicketTypeRequest(Type.INFANT, infant)
        );
    }
}
