package com.solvd.testing.tests;

import com.solvd.CurrencyExchangeService;
import com.solvd.CurrencyRates;
import org.testng.annotations.*;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class CurrencyExchangeServiceTest {
    
    private CurrencyRates rates;
    private CurrencyExchangeService cer;

    @BeforeClass
    public void beforeClass() {
        System.out.println("BeforeClass: " + getClass().getSimpleName());
    }

    @AfterClass
    public void afterClass() {
        System.out.println("AfterClass: " + getClass().getSimpleName());
    }

    @BeforeMethod
    public void beforeMethod() {
        rates = new CurrencyRates();
        cer = new CurrencyExchangeService(rates);
        System.out.println("BeforeMethod");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("AfterMethod");
    }

    @Test
    public void convertUsesRateAndRoundsToTwoDecimals() {
        rates.setRate("USD", "EUR", new BigDecimal("0.9"));

        BigDecimal converted = cer.convert(new BigDecimal("100"),"USD", "EUR");
        assertEquals(converted, new BigDecimal("90.00"));
    }

    @Test
    public void convertWithFeeUsesRateAndRoundsToTwoDecimals(){

        cer.setFeePercent(new BigDecimal("0.1"));
        rates.setRate("USD", "EUR", new BigDecimal("0.9"));
        BigDecimal converted = cer.convertWithFee(new BigDecimal("100"),"USD", "EUR");
        assertEquals(converted, new BigDecimal("89.91"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void supportsPairWhenCurrencyIsBlank(){
        cer.supportsPair("   ", "EUR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void supportsPairWhenCurrencyIsNull(){
        cer.supportsPair(null, "EUR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void setFeePercentWhenFeeIsNegative(){
        cer.setFeePercent(new BigDecimal("-1"));
    }
}
