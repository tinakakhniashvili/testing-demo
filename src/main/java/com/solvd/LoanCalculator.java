package com.solvd;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class LoanCalculator {
    private static final MathContext MC = MathContext.DECIMAL128;

    private final BigDecimal principal;
    private final BigDecimal annualRatePercet;
    private final int termMonths;

    public LoanCalculator(BigDecimal principal, BigDecimal annualRatePercet, int termMonths) {
        if(principal == null || annualRatePercet == null) throw new IllegalArgumentException("principal and annualRatePercet can't be null");
        if(principal.compareTo(BigDecimal.ZERO)<=0)  throw new IllegalArgumentException("principal can't be negative");
        if(annualRatePercet.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("annualRatePercet can't be negative");
        if(termMonths < 0) throw new IllegalArgumentException("termMonths can't be negative");
        this.principal = principal;
        this.annualRatePercet = annualRatePercet;
        this.termMonths = termMonths;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public BigDecimal getAnnualRatePercet() {
        return annualRatePercet;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public BigDecimal monthlRate(){
        return annualRatePercet.divide(new BigDecimal("100"), MC)
                .divide(new BigDecimal("12"), MC);
    }

    public BigDecimal monthlyPayment(int scale){
        BigDecimal r  = monthlRate();
        if(r.compareTo(BigDecimal.ZERO)<=0){
            return principal.divide(new BigDecimal(termMonths), scale, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusR = BigDecimal.ONE.add(r, MC);
        BigDecimal pow = onePlusR.pow(termMonths, MC);
        BigDecimal numerator = principal.multiply(r, MC).multiply(pow, MC);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE, MC);

        BigDecimal payment = numerator.divide(denominator, MC);
        return payment.setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal totalPayment(int scale){
        return monthlyPayment(scale).multiply(new BigDecimal(termMonths), MC).setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal totalInterest(int scale){
        BigDecimal total = totalPayment(scale);
        BigDecimal interest = total.subtract(principal, MC);
        return interest.setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal remainingBalanceAfterPayment(int monthsPaid, int scale){
        if(monthsPaid < 0 || monthsPaid > termMonths) throw new IllegalArgumentException("monthsPaid out of range");

        BigDecimal r = monthlRate();
        BigDecimal payment = monthlyPayment(scale);

        if(r.compareTo(BigDecimal.ZERO)==0){
            BigDecimal paid = payment.multiply(new BigDecimal(monthsPaid), MC);
            BigDecimal remaining = principal.subtract(paid, MC);
            if(remaining.compareTo(BigDecimal.ZERO) < 0) remaining = BigDecimal.ZERO;
            return remaining.setScale(scale, RoundingMode.HALF_UP);
        }

        BigDecimal onePlusR = BigDecimal.ONE.add(r, MC);
        BigDecimal pow = onePlusR.pow(termMonths, MC);

        BigDecimal part1 = principal.multiply(pow, MC);
        BigDecimal part2 = payment.multiply(pow.subtract(BigDecimal.ONE, MC).divide(r, MC));

        BigDecimal balance = part1.subtract(part2, MC);
        if(balance.compareTo(BigDecimal.ZERO)<0) balance = BigDecimal.ZERO;

        return balance.setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal firstMonthInterest(int scale){
        BigDecimal r = monthlRate();
        BigDecimal interest = principal.multiply(r, MC);
        return interest.setScale(scale, RoundingMode.HALF_UP);
    }
}
