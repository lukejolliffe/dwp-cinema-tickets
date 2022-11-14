package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

/**
 * Rule to set the maximum amount of tickets to be allowed
 */
public class MaximumTicketsRule implements BookingValidationRule {

    private final int maxTickets;

    public MaximumTicketsRule(int maxTickets) {
        this.maxTickets = maxTickets;
    }
    @Override
    public boolean validate(TicketTypeRequest... requests) {
        return Arrays.stream(requests).mapToInt(TicketTypeRequest::getNoOfTickets).reduce(0, Integer::sum) <= maxTickets;
    }
}
