package com.solvd.testing.tests;

import com.solvd.TransactionLedger;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.solvd.TransactionLedger.Type.*;
import static org.testng.Assert.*;

public class TransactionLedgerTest {

    @Test
    public void addTransaction() {
        TransactionLedger tl = new TransactionLedger();
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("100.00"), "Second transaction");
        tl.addTransaction(TRANSFER_IN, new BigDecimal("150.00"), "Third transaction");

        assertEquals(tl.size(), 3);
    }

    @Test
    public void getTransactionByType(){
        TransactionLedger tl = new TransactionLedger();
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("100.00"), "Second transaction");
        tl.addTransaction(TRANSFER_IN, new BigDecimal("150.00"), "Third transaction");

       List<TransactionLedger.Transaction> deposits = tl.getByType(DEPOSIT);

        assertEquals(deposits.size(), 1);
        assertEquals(deposits.get(0).getType(), TransactionLedger.Type.DEPOSIT);
        assertEquals(deposits.get(0).getAmount(), new BigDecimal("120.00"));
    }

    @Test
    public void countByTypeCountsCorrectly(){
        TransactionLedger tl = new TransactionLedger();
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("1000.00"), "Second transaction");
        tl.addTransaction(DEPOSIT, new BigDecimal("150.00"), "Third transaction");

        assertEquals(tl.countByType(DEPOSIT), 2);
    }

    @Test
    public void getBalanceChangeIsCorrect(){
        TransactionLedger tl = new TransactionLedger();
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("10.00"), "Second transaction");
        tl.addTransaction(DEPOSIT, new BigDecimal("150.00"), "Third transaction");

        assertEquals(tl.getBalanceChange(), new BigDecimal("260.00"));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void getLastTransactionThrowsWhenEmpty(){
        TransactionLedger tl = new TransactionLedger();
        tl.getLastTransaction();
    }

    @Test
    public void clearEmptiesLedger(){
        TransactionLedger tl = new TransactionLedger();
        tl.addTransaction(DEPOSIT, new BigDecimal("120.00"), "first transaction");
        tl.addTransaction(WITHDRAW, new BigDecimal("10.00"), "Second transaction");
        tl.addTransaction(DEPOSIT, new BigDecimal("150.00"), "Third transaction");

        tl.clear();
        assertEquals(tl.size(), 0);
        assertEquals(tl.countByType(TransactionLedger.Type.DEPOSIT), 0);
    }
}
