package com.simulation.trading.crypto.model;

import java.math.BigDecimal;

public class User {
    private final int userID;
    private final String userName;
    private BigDecimal balance;

    public User(int userID, String userName, BigDecimal balance) {
        this.userID = userID;
        this.userName = userName;
        this.balance = balance;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
