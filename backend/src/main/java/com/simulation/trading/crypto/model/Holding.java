package com.simulation.trading.crypto.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Holding {
    private final int userId;
    private final String symbol;
    private BigDecimal amount;
    private Instant lastUpdated;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
