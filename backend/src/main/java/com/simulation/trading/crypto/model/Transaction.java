package com.simulation.trading.crypto.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(long id, int userId,
                          String symbol, BigDecimal amount, BigDecimal fiatAmount,
                          TransactionType type, Instant timestamp) {
}
