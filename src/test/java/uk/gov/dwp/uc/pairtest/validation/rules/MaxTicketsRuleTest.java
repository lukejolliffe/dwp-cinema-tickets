package uk.gov.dwp.uc.pairtest.validation.rules;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.Assert.*;

public class MaxTicketsRuleTest {

    @Test
    public void testMaxTicketsFails() {
        MaximumTicketsRule sut = new MaximumTicketsRule(20);

        //all tickets in one request 21 is boundary maximum
        boolean result = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21)
        );
        assertFalse(result);

        //tickets in separate requests
        boolean seperateRequestResult = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        );
        assertFalse(seperateRequestResult);
    }

    @Test
    public void testLimitMinusOnePasses() {
        MaximumTicketsRule sut = new MaximumTicketsRule(20);

        //test just below maximum
        boolean result = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20)
        );
        assertTrue(result);

        //tickets in separate requests
        boolean seperateRequestResult = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 8),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 8),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 4)
        );
        assertTrue(seperateRequestResult);
    }
}