package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.rules.BookingValidationRule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookingValidationServiceImpl implements BookingValidationService {

    private final BookingValidationRule[] rules;
    public BookingValidationServiceImpl(BookingValidationRule ... rules){
        this.rules = rules;
    }

    /**
     * {@inheritDoc}
     * @implNote checks are not lazy and will all be processed before throwing
     */
    @Override
    public boolean validateBooking(TicketTypeRequest ... requests) throws InvalidPurchaseException {
        List<BookingValidationRule> failedChecks = Arrays.stream(rules).filter(bookingValidationRule -> !bookingValidationRule.validate(requests)).collect(Collectors.toList());

        if (!failedChecks.isEmpty()) {
            //Gather all failed classnames
            String failed = failedChecks.stream().map(Object::getClass).map(Class::getName).collect(Collectors.joining(","));
            throw new InvalidPurchaseException(failed);
        }
        return true;
    }
}
