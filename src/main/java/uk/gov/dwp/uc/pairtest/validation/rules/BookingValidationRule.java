package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public interface BookingValidationRule {

    public boolean validate(TicketTypeRequest ... requests);
}
