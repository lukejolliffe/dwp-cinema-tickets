package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

public class MinimumTicketsRule implements BookingValidationRule {

    private final int minTickets;

    public MinimumTicketsRule(int minTickets) {
        this.minTickets = minTickets;
    }
    @Override
    public boolean validate(TicketTypeRequest... requests) {
        return Arrays.stream(requests).mapToInt(TicketTypeRequest::getNoOfTickets).reduce(0, Integer::sum) > minTickets;
    }
}
