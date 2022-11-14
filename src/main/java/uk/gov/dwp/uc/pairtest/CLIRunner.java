package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.ticket.Ticket;
import uk.gov.dwp.uc.pairtest.ticket.TicketImpl;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationService;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.validation.rules.MaxTicketsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumAdultsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumTicketsRule;

import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class CLIRunner {
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
        Map<TicketTypeRequest.Type, Ticket> ticketInfoMap = new EnumMap<>(TicketTypeRequest.Type.class);
        ticketInfoMap.put(TicketTypeRequest.Type.ADULT, new TicketImpl(1, 20));
        ticketInfoMap.put(TicketTypeRequest.Type.CHILD, new TicketImpl(1, 10));
        ticketInfoMap.put(TicketTypeRequest.Type.INFANT, new TicketImpl(0,0));

        TicketService service = new TicketServiceImpl(ticketPaymentService,seatReservationService,bookingValidationService,ticketInfoMap);
        service.purchaseTickets(
                123456l,
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, adult),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, child),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, infant)
        );
    }
}
