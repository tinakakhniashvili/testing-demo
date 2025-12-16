package com.solvd;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CurrencyRates {
    private final Map<String, BigDecimal> rates = new HashMap<>();

    public void setRate(String fromCurrency, String toCurrency,  BigDecimal rate) {
        String key = pairKey(fromCurrency, toCurrency);
        if(rate == null) throw new IllegalArgumentException("Rate is null");
        if(rate.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Rate is negative or zero");
        rates.put(key, rate);
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        String key = pairKey(fromCurrency, toCurrency);
        BigDecimal rate = rates.get(key);
        if(rate == null) throw new IllegalArgumentException("Rate is null");
        return rate;
    }

    public boolean hasRate(String fromCurrency, String toCurrency) {
        String key = pairKey(fromCurrency, toCurrency);
        return rates.containsKey(key);
    }

    public BigDecimal removeRate(String fromCurrency, String toCurrency) {
        String key = pairKey(fromCurrency, toCurrency);
        return rates.remove(key);
    }

    public Set<String> listPairs() {
        return Collections.unmodifiableSet(rates.keySet());
    }

    public void clear() {
        rates.clear();
    }

    public int size() {
        return rates.size();
    }

    private String pairKey(String fromCurrency, String toCurrency) {
        if(fromCurrency== null || toCurrency == null) throw new IllegalArgumentException("Currency cannot be null");
        String from = fromCurrency.trim().toUpperCase();
        String to = toCurrency.trim().toUpperCase();
        if(from.isEmpty() || to.isEmpty()) throw new IllegalArgumentException("Currency cannot be blank");
        return from + "->" + to;
    }
}
