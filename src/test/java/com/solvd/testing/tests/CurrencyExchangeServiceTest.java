package com.solvd.testing.tests;

import com.solvd.CurrencyExchangeService;
import com.solvd.CurrencyRates;
import org.testng.annotations.Test;
import static org.testng.Assert.*;


import java.math.BigDecimal;

public class CurrencyExchangeServiceTest {
    @Test
    public void convertUsesRateAndRoundsToTwoDecimals() {
        CurrencyRates rates = new CurrencyRates();
        rates.setRate("USD", "EUR", new BigDecimal("0.9"));

        CurrencyExchangeService cer = new CurrencyExchangeService(rates);

        BigDecimal converted = cer.convert(new BigDecimal("100"),"USD", "EUR");
        assertEquals(converted, new BigDecimal("90.00"));
    }

    @Test
    public void convertWithFeeUsesRateAndRoundsToTwoDecimals(){
        CurrencyRates rates = new CurrencyRates();
        CurrencyExchangeService cer = new CurrencyExchangeService(rates);

        cer.setFeePercent(new BigDecimal("0.1"));
        rates.setRate("USD", "EUR", new BigDecimal("0.9"));
        BigDecimal converted = cer.convertWithFee(new BigDecimal("100"),"USD", "EUR");
        assertEquals(converted, new BigDecimal("89.91"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void supportsPairWhenCurrencyIsBlank(){
        CurrencyRates rates = new CurrencyRates();

        CurrencyExchangeService cer = new CurrencyExchangeService(rates);
        cer.supportsPair("   ", "EUR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void supportsPairWhenCurrencyIsNull(){
        CurrencyRates rates = new CurrencyRates();

        CurrencyExchangeService cer = new CurrencyExchangeService(rates);
        cer.supportsPair(null, "EUR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setFeePercentWhenFeeIsNegative(){
        CurrencyRates rates = new CurrencyRates();
        CurrencyExchangeService cer = new CurrencyExchangeService(rates);
        cer.setFeePercent(new BigDecimal("-1"));
    }
}
