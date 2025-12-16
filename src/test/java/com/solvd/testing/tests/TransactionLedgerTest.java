package com.solvd.testing.tests;

import com.solvd.TransactionLedger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.solvd.TransactionLedger.Type.*;
import static org.testng.Assert.assertEquals;

public class TransactionLedgerTest {

    private TransactionLedger tl;

    @BeforeMethod
    public void setUp() {
        tl = new TransactionLedger();
    }

    private void addSample3(TransactionLedger ledger) {
        ledger.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        ledger.addTransaction(WITHDRAW, new BigDecimal("100.00"), "Second transaction");
        ledger.addTransaction(TRANSFER_IN, new BigDecimal("150.00"), "Third transaction");
    }

    private void addForBalance260(TransactionLedger ledger) {
        ledger.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        ledger.addTransaction(WITHDRAW, new BigDecimal("10.00"), "Second transaction");
        ledger.addTransaction(DEPOSIT, new BigDecimal("150.00"), "Third transaction");
    }

    @Test
    public void addTransaction() {
        addSample3(tl);
        assertEquals(tl.size(), 3);
    }

    @Test
    public void getTransactionByType() {
        addSample3(tl);

        List<TransactionLedger.Transaction> deposits = tl.getByType(DEPOSIT);

        assertEquals(deposits.size(), 1);
        assertEquals(deposits.get(0).getType(), DEPOSIT);
        assertEquals(deposits.get(0).getAmount(), new BigDecimal("120.00"));
    }

    @Test
    public void countByTypeCountsCorrectly() {
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("1000.00"), "Second transaction");
        tl.addTransaction(DEPOSIT, new BigDecimal("150.00"), "Third transaction");

        assertEquals(tl.countByType(DEPOSIT), 2);
    }

    @Test
    public void getBalanceChangeIsCorrect() {
        addForBalance260(tl);
        assertEquals(tl.getBalanceChange(), new BigDecimal("260.00"));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void getLastTransactionThrowsWhenEmpty() {
        tl.getLastTransaction();
    }

    @Test
    public void clearEmptiesLedger() {
        addSample3(tl);

        tl.clear();

        assertEquals(tl.size(), 0);
        assertEquals(tl.countByType(DEPOSIT), 0);
        assertEquals(tl.countByType(WITHDRAW), 0);
        assertEquals(tl.countByType(TRANSFER_IN), 0);
    }
}
