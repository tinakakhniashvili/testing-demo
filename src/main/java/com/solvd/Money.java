package com.solvd;

import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Comparable<Money> {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
        if (currency == null) throw new IllegalArgumentException("Currency cannot be null");
        String curr = currency.trim().toUpperCase();
        if(curr.isEmpty()) throw new IllegalArgumentException("Currency cannot be blank");
        this.amount = amount;
        this.currency = curr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money withCurrency(String withCurrency) {
        return new Money(amount, withCurrency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public int compareTo(Money other){
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount);
    }

    private void requireSameCurrency(Money other){
        if(other == null) throw new IllegalArgumentException("Other cannot be null");
        if(!this.currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Money m)) return false;
        return currency.equals(m.currency) && amount.compareTo(m.amount)==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
