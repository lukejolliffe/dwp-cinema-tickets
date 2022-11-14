package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Service to validate a booking request against a set of rules
 */
public interface BookingValidationService {

    /**
     * Validates the booking request against a set of rules
     * @param requests the ticket requests
     * @return true if the booking passes validation
     * @throws InvalidPurchaseException if the booking fails validation this exception is raised with a message containing all failed rules
     */
    public boolean validateBooking(TicketTypeRequest ... requests) throws InvalidPurchaseException;
}
