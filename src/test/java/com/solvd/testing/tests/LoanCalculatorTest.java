package com.solvd.testing.tests;

import com.solvd.LoanCalculator;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.testng.Assert.assertEquals;

public class LoanCalculatorTest {

    @Test
    public void testCalculateLoan() {
        LoanCalculator loanCalculator = new LoanCalculator(new BigDecimal("20.00"), new BigDecimal("15.00"), 12);

        BigDecimal monthlyRate = loanCalculator.monthlyRate();
        assertEquals(monthlyRate, new BigDecimal("0.0125"));
    }

    @Test
    public void testMonthlyPayment() {
        LoanCalculator loanCalculator = new LoanCalculator(new BigDecimal("20.00"), new BigDecimal("15.00"), 12);

        BigDecimal payment = loanCalculator.monthlyPayment(2);
        assertEquals(payment, new BigDecimal("1.81"));
    }

    @Test
    public void totalPaymentEqualsMonthlyPaymentTimesTerm() {
        LoanCalculator loanCalculator = new LoanCalculator(new BigDecimal("20.00"), new BigDecimal("15.00"), 12);

        BigDecimal monthly = loanCalculator.monthlyPayment(2);
        BigDecimal total = loanCalculator.totalPayment(2);

        assertEquals(total, monthly.multiply(new BigDecimal("12")).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void totalInterestEqualsTotalPaymentMinusPrincipal() {
        LoanCalculator loanCalculator = new LoanCalculator(new BigDecimal("20.00"), new BigDecimal("15.00"), 12);

        BigDecimal totalPayment = loanCalculator.totalPayment(2);
        BigDecimal interest = loanCalculator.totalInterest(2);

        assertEquals(interest, totalPayment.subtract(new BigDecimal("20.00")).setScale(2, RoundingMode.HALF_UP));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void remainingBalanceThrowsWhenMonthsPaidOutOfRange() {
        LoanCalculator loanCalculator = new LoanCalculator(new BigDecimal("20.00"), new BigDecimal("15.00"), 12);
        loanCalculator.remainingBalanceAfterPayment(13, 2);
    }
}
