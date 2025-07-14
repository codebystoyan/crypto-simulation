package com.simulation.trading.crypto.repository;

import com.simulation.trading.crypto.model.Transaction;
import com.simulation.trading.crypto.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (id, user_id, crypto_symbol, amount, fiat_amount, type, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, transaction.id(), transaction.userId(), transaction.symbol(),transaction.amount(),
                transaction.fiatAmount(), transaction.type().name().toUpperCase(), transaction.timestamp());
    }

    public List<Transaction> getTransactionHistory(int userID) {
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Transaction(rs.getInt("id"),
                                                rs.getInt("user_id"),
                                                rs.getString("crypto_symbol"),
                                                rs.getBigDecimal("amount"),
                                                rs.getBigDecimal("fiat_amount"),
                                                TransactionType.valueOf(rs.getString("type").toUpperCase()),
                                                rs.getTimestamp("timestamp").toInstant()),
                userID);
    }
}
