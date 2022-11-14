package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

public class MaxTicketsRule implements BookingValidationRule {

    private final int maxTickets;

    public MaxTicketsRule(int maxTickets) {
        this.maxTickets = maxTickets;
    }
    @Override
    public boolean validate(TicketTypeRequest... requests) {
        return Arrays.stream(requests).mapToInt(TicketTypeRequest::getNoOfTickets).reduce(0, Integer::sum) <= maxTickets;
    }
}
