package uk.gov.dwp.uc.pairtest.validation.rules;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.Assert.*;

public class MinimumAdultsRuleTest {

    @Test
    public void noAdultTicketsBoughtFails() {
        MinimumAdultsRule sut = new MinimumAdultsRule();

        //test buying non adult tickets
        boolean nonAdultTest = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT,5)
        );
        assertFalse(nonAdultTest);

        //test buying no tickets
        boolean noTicketsBought = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0)
        );
        assertFalse(noTicketsBought);

        //test specifying no tickets
        boolean noTicketsSpecified = sut.validate();
        assertFalse(noTicketsSpecified);
    }

    @Test
    public void adultTicketBoughtPasses() {
        MinimumAdultsRule sut = new MinimumAdultsRule();

        boolean withKids = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 5),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT,5)
        );
        assertTrue(withKids);

        boolean withoutKids = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
        );
        assertTrue(withoutKids);
    }
}