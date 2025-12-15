package com.solvd;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyExchangeService {
    private final CurrencyRates rates;
    private BigDecimal feePercent = BigDecimal.ZERO;
    private int scale = 2;

    public CurrencyExchangeService(CurrencyRates rates) {
        if(rates == null) throw new IllegalArgumentException("rates can't be null");
        this.rates = rates;
    }

    public void setRate(String fromCurrency, String toCurrency,  BigDecimal rate) {
        rates.setRate(fromCurrency, toCurrency, rate);
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        return rates.getRate(fromCurrency, toCurrency);
    }

    public void setFeePercent(BigDecimal feePercent) {
        if(feePercent == null) throw new IllegalArgumentException("feePercent can't be null");
        if(feePercent.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("feePercent can't be negative");
        this.feePercent = feePercent;
    }

    public void setScale(int scale) {
        if(scale < 0) throw new IllegalArgumentException("scale can't be negative");
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public boolean supportsPair(String fromCurrency, String toCurrency) {
        if(fromCurrency == null || toCurrency == null) throw new IllegalArgumentException("fromCurrency and toCurrency can't be null");
        String from = fromCurrency.trim().toUpperCase();
        String to = toCurrency.trim().toUpperCase();
        if(from.isEmpty() || to.isEmpty()) throw new IllegalArgumentException("from and to can't be empty");
        if(from.equals(to)) return true;
        return rates.hasRate(from, to);
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if(amount == null) throw new IllegalArgumentException("amount can't be null");
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("amount can't be negative");
        if(fromCurrency == null || toCurrency == null) throw new IllegalArgumentException("fromCurrency and toCurrency can't be null");

        String from = fromCurrency.trim().toUpperCase();
        String to = toCurrency.trim().toUpperCase();

        if(from.isEmpty() || to.isEmpty()) throw new IllegalArgumentException("from and to can't be empty");
        if(from.equals(to)) return round(amount, scale);

        BigDecimal rate = rates.getRate(from, to);
        BigDecimal converted = amount.multiply(rate);
        return round(converted, scale);
    }

    public BigDecimal convertWithFee(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal base = convert(amount, fromCurrency, toCurrency);

        if(feePercent.compareTo(BigDecimal.ZERO) == 0) return base;

        BigDecimal feeMultiplier = BigDecimal.ONE.subtract(feePercent.divide(new BigDecimal("100"), 16, RoundingMode.HALF_UP));
        BigDecimal afterFee = base.multiply(feeMultiplier);

        return round(afterFee, scale);
    }

    public BigDecimal round(BigDecimal amount, int scale) {
        if(amount == null) throw new IllegalArgumentException("amount can't be null");
        if(scale < 0) throw new IllegalArgumentException("scale can't be negative");
        return amount.setScale(scale, RoundingMode.HALF_UP);
    }
}
