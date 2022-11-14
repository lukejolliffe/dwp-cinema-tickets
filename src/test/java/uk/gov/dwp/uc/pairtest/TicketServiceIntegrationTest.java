package uk.gov.dwp.uc.pairtest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ticket.Ticket;
import uk.gov.dwp.uc.pairtest.ticket.TicketImpl;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationService;
import uk.gov.dwp.uc.pairtest.validation.BookingValidationServiceImpl;
import uk.gov.dwp.uc.pairtest.validation.rules.MaxTicketsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumAdultsRule;
import uk.gov.dwp.uc.pairtest.validation.rules.MinimumTicketsRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

@RunWith(Parameterized.class)
public class TicketServiceIntegrationTest {
    private final int adults;
    private final int children;
    private final int infants;
    private final boolean passesValidation;
    private final int cost;
    private final int numberOfSeats;
    private final long accountId = 12345L;
    private TicketService sut;
    private SeatReservationService seatReservationService;
    private TicketPaymentService ticketPaymentService;

    @Parameterized.Parameters
    public static Collection<Object[]> testInputs() {
        return Arrays.asList(new Object[][] {
                { 0, 0, 0, false, 0, 0},
                { 0, 0, 1, false, 0, 0},
                { 1, 0, 0, true, 20, 1},
                { 1, 1, 1, true, 30, 2},
                { 100, 15, 10, false, 0, 0},
                { 5, 5, 10, true, 150, 10}
        });
    }

    public TicketServiceIntegrationTest(int adults, int children, int infants, boolean passesValidation, int cost, int numberOfSeats) {
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.passesValidation = passesValidation;
        this.cost = cost;
        this.numberOfSeats = numberOfSeats;
    }

    @Before
    public void setup() {
        this.ticketPaymentService = Mockito.mock(TicketPaymentService.class);
        this.seatReservationService = Mockito.mock(SeatReservationService.class);
        BookingValidationService bookingValidationService = new BookingValidationServiceImpl(
                new MaxTicketsRule(20),
                new MinimumAdultsRule(),
                new MinimumTicketsRule(0)
        );
        Map<TicketTypeRequest.Type, Ticket> ticketInfoMap = new EnumMap<>(TicketTypeRequest.Type.class);
        ticketInfoMap.put(TicketTypeRequest.Type.ADULT, new TicketImpl(1, 20));
        ticketInfoMap.put(TicketTypeRequest.Type.CHILD, new TicketImpl(1, 10));
        ticketInfoMap.put(TicketTypeRequest.Type.INFANT, new TicketImpl(0,0));
        this.sut = new TicketServiceImpl(ticketPaymentService,seatReservationService, bookingValidationService, ticketInfoMap);
    }
    @Test
    public void testPurchaseAndRulesIntegration() {
        boolean testPassedValidation = true;
        try {
            sut.purchaseTickets(
                    accountId,
                    new TicketTypeRequest(TicketTypeRequest.Type.ADULT, adults),
                    new TicketTypeRequest(TicketTypeRequest.Type.CHILD, children),
                    new TicketTypeRequest(TicketTypeRequest.Type.INFANT, infants)
            );

            Mockito.verify(ticketPaymentService, times(1)).makePayment(accountId, cost);
            Mockito.verify(seatReservationService, times(1)).reserveSeat(accountId, numberOfSeats);
        } catch (InvalidPurchaseException e) {
            System.out.println(e);
            testPassedValidation = false;
        } finally {
            assertEquals(passesValidation, testPassedValidation);
        }

    }
}
