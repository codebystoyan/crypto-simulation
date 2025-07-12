package com.simulation.trading.crypto.model;

import java.time.LocalDateTime;

public class Holding {
    private final int userId;
    private final String symbol;
    private double amount;
    private LocalDateTime lastUpdated;

    public Holding(int userId, String symbol) {
        this.userId = userId;
        this.symbol = symbol;
    }

    public int getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
