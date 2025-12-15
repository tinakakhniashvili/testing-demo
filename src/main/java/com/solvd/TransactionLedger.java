package com.solvd;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionLedger {

    public enum Type{
        DEPOSIT,
        WITHDRAW,
        TRANSFER_IN,
        TRANSFER_OUT
    }

    public static final class Transaction{
        private final Type type;
        private final BigDecimal amount;
        private final Instant timestamp;
        private final String note;

        public Transaction(Type type, BigDecimal amount, String note) {
            if ( type == null ) throw new IllegalArgumentException("type can't be null");
            if (amount == null ) throw new IllegalArgumentException("amount can't be null");
            if (amount.compareTo(BigDecimal.ZERO) < 0 ) throw new IllegalArgumentException("amount can't be negative");
            this.type = type;
            this.amount = amount;
            this.timestamp = Instant.now();
            this.note = note;
        }

        public Type getType() {
            return type;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public String getNote() {
            return note;
        }
    }

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Type type, BigDecimal amount, String note) {
        transactions.add(new Transaction(type, amount, note));
    }

    public List<Transaction> getByType(Type type) {
        Objects.requireNonNull(type, "type");
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toUnmodifiableList());
    }

    public long countByType(Type type) {
        Objects.requireNonNull(type, "type");
        return transactions.stream().filter(t -> t.getType() == type).count();
    }

    public BigDecimal getBalanceChange(){
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction t : transactions){
            switch (t.getType()){
            case DEPOSIT, TRANSFER_IN -> sum = sum.add(t.getAmount());
            case WITHDRAW , TRANSFER_OUT-> sum = sum.subtract(t.getAmount());
            }
        }
        return sum;
    }

    public Transaction getLastTransaction(){
        return transactions.stream()
                .max(Comparator.comparing(Transaction::getTimestamp))
                .orElseThrow(() -> new IllegalStateException("Ledger is empty"));
    }

    public void clear(){
        transactions.clear();
    }

    public int size(){
        return transactions.size();
    }
}
