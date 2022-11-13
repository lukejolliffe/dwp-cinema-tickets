package uk.gov.dwp.uc.pairtest.validation;

import org.junit.Test;
import org.mockito.Mockito;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.rules.BookingValidationRule;

import static org.junit.Assert.*;

public class BookingValidationServiceImplTest {

    @Test(expected = InvalidPurchaseException.class)
    public void validateBookingRaisesExceptionOnFailure() {

        BookingValidationRule failRule = Mockito.mock(BookingValidationRule.class);
        Mockito.when(failRule.validate()).thenReturn(false);

        BookingValidationService sut = new BookingValidationServiceImpl(failRule);
        sut.validateBooking();
    }

    @Test(expected = InvalidPurchaseException.class)
    public void passesAndFailuresRaiseException() {
        BookingValidationRule failRule = Mockito.mock(BookingValidationRule.class);
        Mockito.when(failRule.validate()).thenReturn(false);

        BookingValidationRule passRule = Mockito.mock(BookingValidationRule.class);
        Mockito.when(passRule.validate()).thenReturn(true);

        BookingValidationService sut = new BookingValidationServiceImpl(passRule, failRule);
        sut.validateBooking();
    }

    @Test
    public void validateBookingSuccess() {
        BookingValidationRule passRule = Mockito.mock(BookingValidationRule.class);
        Mockito.when(passRule.validate()).thenReturn(true);

        BookingValidationService sut = new BookingValidationServiceImpl(passRule);
        boolean validateResult = sut.validateBooking();
        assertTrue(validateResult);
    }

    @Test
    public void noRulesPasses() {
        BookingValidationService sut = new BookingValidationServiceImpl();
        boolean validateResult = sut.validateBooking();
        assertTrue(validateResult);
    }
}