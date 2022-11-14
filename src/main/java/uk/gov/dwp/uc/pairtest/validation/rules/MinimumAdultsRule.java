package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

/**
 * Rule class to specify the minimum adults allowed is 1
 */
public class MinimumAdultsRule implements BookingValidationRule {

    @Override
    public boolean validate(TicketTypeRequest... requests) {
        return Arrays.stream(requests).anyMatch(ticketTypeRequest -> ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT && ticketTypeRequest.getNoOfTickets() > 0);
    }
}
