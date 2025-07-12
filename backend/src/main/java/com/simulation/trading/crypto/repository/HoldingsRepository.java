package com.simulation.trading.crypto.repository;

import com.simulation.trading.crypto.exception.UserNotFoundException;
import com.simulation.trading.crypto.model.Holding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class HoldingsRepository {
    private final JdbcTemplate jdbcTemplate;

    private static class HoldingRowMapper implements RowMapper<Holding> {
        @Override
        public Holding mapRow(ResultSet rs, int rowNum) throws SQLException {
            Holding h = new Holding(rs.getInt("user_id"),
                    rs.getString("crypto_symbol"));

            h.setAmount(rs.getDouble("amount"));
            h.setLastUpdated(rs.getTimestamp("last_updated").toLocalDateTime());
            return h;
        }
    }

    @Autowired
    public HoldingsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Holding> getUserHoldings(int userId) {
        String sql = "SELECT * FROM holdings WHERE user_id=?";
        return jdbcTemplate.query(sql, new HoldingRowMapper(), userId);
    }

    public Optional<Holding> getCryptoHolding(int userId, String symbol) {
        String sql = "SELECT * FROM holdings WHERE user_id=? AND crypto_symbol=?";
        try{
            return Optional.of(jdbcTemplate.queryForObject(sql, new HoldingRowMapper(), userId, symbol));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int updateCryptoHolding(Holding holding) {
        String sql = "UPDATE holdings SET amount=?, last_updated=? WHERE user_id=? AND crypto_symbol=?";
        return jdbcTemplate.update(sql,  holding.getAmount(),
                Timestamp.valueOf(holding.getLastUpdated()), holding.getUserId(), holding.getSymbol());
    }

    public int insertCryptoHolding(Holding holding) {
        String sql = "INSERT INTO holdings(user_id,crypto_symbol,amount,last_updated) VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql, holding.getUserId(), holding.getSymbol(),
                holding.getAmount(), Timestamp.valueOf(holding.getLastUpdated()));
    }

    public int deleteCryptoHolding(int userId, String symbol) {
        String sql = "DELETE FROM holdings WHERE user_id=? AND crypto_symbol=?";
        return jdbcTemplate.update(sql, userId, symbol);
    }
}
