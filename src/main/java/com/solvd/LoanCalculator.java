package com.solvd;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LoanCalculator {
    private static final MathContext MC = MathContext.DECIMAL128;

    private final BigDecimal principal;
    private final BigDecimal annualRatePercent;
    private final int termMonths;

    public LoanCalculator(BigDecimal principal, BigDecimal annualRatePercent, int termMonths) {
        if (principal == null || annualRatePercent == null) {
            throw new IllegalArgumentException("principal and annualRatePercent can't be null");
        }
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("principal must be > 0");
        }
        if (annualRatePercent.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("annualRatePercent can't be negative");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("termMonths must be > 0");
        }

        this.principal = principal;
        this.annualRatePercent = annualRatePercent;
        this.termMonths = termMonths;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getAnnualRatePercent() {
        return annualRatePercent;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public BigDecimal monthlyRate() {
        return annualRatePercent.divide(new BigDecimal("100"), MC)
                .divide(new BigDecimal("12"), MC);
    }

    public BigDecimal monthlyPayment(int scale) {
        if (scale < 0) throw new IllegalArgumentException("scale can't be negative");

        BigDecimal r = monthlyRate();
        if (r.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(termMonths), scale, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusR = BigDecimal.ONE.add(r, MC);
        BigDecimal pow = onePlusR.pow(termMonths, MC);
        BigDecimal numerator = principal.multiply(r, MC).multiply(pow, MC);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE, MC);

        BigDecimal payment = numerator.divide(denominator, MC);
        return payment.setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal totalPayment(int scale) {
        if (scale < 0) throw new IllegalArgumentException("scale can't be negative");
        return monthlyPayment(scale)
                .multiply(new BigDecimal(termMonths), MC)
                .setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal totalInterest(int scale) {
        if (scale < 0) throw new IllegalArgumentException("scale can't be negative");
        return totalPayment(scale)
                .subtract(principal, MC)
                .setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal remainingBalanceAfterPayment(int monthsPaid, int scale) {
        if (scale < 0) throw new IllegalArgumentException("scale can't be negative");
        if (monthsPaid < 0 || monthsPaid > termMonths) {
            throw new IllegalArgumentException("monthsPaid out of range");
        }

        BigDecimal r = monthlyRate();
        BigDecimal payment = monthlyPayment(scale);

        if (r.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal paid = payment.multiply(new BigDecimal(monthsPaid), MC);
            BigDecimal remaining = principal.subtract(paid, MC);
            if (remaining.compareTo(BigDecimal.ZERO) < 0) remaining = BigDecimal.ZERO;
            return remaining.setScale(scale, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusR = BigDecimal.ONE.add(r, MC);
        BigDecimal pow = onePlusR.pow(monthsPaid, MC);

        BigDecimal part1 = principal.multiply(pow, MC);
        BigDecimal part2 = payment.multiply(pow.subtract(BigDecimal.ONE, MC).divide(r, MC), MC);

        BigDecimal balance = part1.subtract(part2, MC);
        if (balance.compareTo(BigDecimal.ZERO) < 0) balance = BigDecimal.ZERO;

        return balance.setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal firstMonthInterest(int scale) {
        if (scale < 0) throw new IllegalArgumentException("scale can't be negative");
        BigDecimal interest = principal.multiply(monthlyRate(), MC);
        return interest.setScale(scale, RoundingMode.HALF_UP);
    }
}
