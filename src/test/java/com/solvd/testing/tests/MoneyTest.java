package com.solvd.testing.tests;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.solvd.Money;

import java.math.BigDecimal;

public class MoneyTest {

    @Test
    public void currencyIsNormalizedToUppercaseAndTrimmed(){
        Money m = new Money(new BigDecimal("10"), "usd ");
        assertEquals(m.getCurrency(), "USD");
    }

    @Test
    public void addSumsCurrenciesWhenCurrenciesMatch(){
        Money m = new Money(new BigDecimal("10"), "USD");
        Money m2 = new Money(new BigDecimal("15"), "USD");

        Money m3 = m.add(m2);

        assertTrue(m3.getAmount().compareTo(new BigDecimal("25")) == 0);
        assertEquals(m3.getCurrency(), "USD");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addThrowsWhenCurrenciesDoNotMatch(){
        Money m = new Money(new BigDecimal("10"), "USD");
        Money m2 = new Money(new BigDecimal("15"), "EUR");

        Money m3 = m.add(m2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void constructorThrowsWhenAmountIsNull(){
        Money m = new Money(null, "GEL");
    }

    @Test
    public void isNegativeReturnsTrueForNegativeAmount(){
        Money m = new Money(new BigDecimal("-1"), "GEL");
        assertTrue(m.isNegative());
    }

    @Test
    public void withCurrencyChangesCurrency(){
        Money m = new Money(new BigDecimal("15"), "GEL");

        Money result = m.withCurrency(" gel ");

        assertTrue(result.getAmount().compareTo(new BigDecimal("15")) == 0);
        assertEquals(result.getCurrency(), "GEL");
    }
}
