package com.solvd.testing.tests;

import com.solvd.CurrencyRates;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CurrencyRatesTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setRateWhenRateIsNegative(){
        CurrencyRates rates = new CurrencyRates();
        rates.setRate("EUR", "USD", new BigDecimal("-0.85"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setRateWhenRateIsNull(){
        CurrencyRates rates = new CurrencyRates();
        rates.setRate("EUR", "USD", null);
    }

    @Test
    public void verifyPairKeyNormalization(){
        CurrencyRates rates = new CurrencyRates();
        rates.setRate(" euR ", "usd ", new BigDecimal("0.85"));

        assertTrue(rates.hasRate("EUR", "USD"));
    }

    @Test
    public void verifyGetRate(){
        CurrencyRates rates = new CurrencyRates();
        rates.setRate("EUR", "USD", new BigDecimal("0.85"));

        assertEquals(rates.getRate("EUR", "USD"), new BigDecimal("0.85"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getRateThrowsWhenMissing(){
        CurrencyRates rates = new CurrencyRates();
        rates.getRate("EUR", "USD");
    }
}
