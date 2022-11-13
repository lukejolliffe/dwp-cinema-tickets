package uk.gov.dwp.uc.pairtest.validation.rules;

import org.junit.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.Assert.*;

public class MinimumTicketsRuleTest {

    @Test
    public void belowMinimumTicketsFails() {
        MinimumTicketsRule sut = new MinimumTicketsRule(0);

        boolean allTypesSpecifiedNoneBought = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT,0)
        );

        assertFalse(allTypesSpecifiedNoneBought);

        boolean noneSpecified = sut.validate();
        assertFalse(noneSpecified);
    }

    @Test
    public void aboveMinimumPasses() {
        MinimumTicketsRule sut = new MinimumTicketsRule(0);

        boolean oneAdult = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1)
        );
        assertTrue(oneAdult);


        boolean oneChild = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1)
        );
        assertTrue(oneChild);

        boolean oneInfant = sut.validate(
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        );

        assertTrue(oneInfant);
    }
}