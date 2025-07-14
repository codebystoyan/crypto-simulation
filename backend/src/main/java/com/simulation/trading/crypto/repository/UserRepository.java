package com.simulation.trading.crypto.repository;

import com.simulation.trading.crypto.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> getUserById(int userID) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try{
            return Optional.of(jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new User(rs.getInt("id"),
                                                        rs.getString("name"),
                                                        rs.getBigDecimal("balance")),
                    userID));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public int updateUserBalance(int userID, BigDecimal balance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, balance, userID);
    }
}
