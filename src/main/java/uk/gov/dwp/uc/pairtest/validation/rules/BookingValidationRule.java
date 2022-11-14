package uk.gov.dwp.uc.pairtest.validation.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/**
 * A rule to verify against a ticket request
 */
public interface BookingValidationRule {

    /**
     * Method used to validate the requests given
     * @param requests the requests given
     * @return boolean based on if the rule passes or fails
     */
    boolean validate(TicketTypeRequest ... requests);
}
