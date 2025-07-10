package com.simulation.trading.crypto.model;

import java.time.LocalDateTime;

public record Transaction(long id, int userId,
                          String symbol, double amount, double fiatAmount,
                          TransactionType type, LocalDateTime timestamp) {
}
