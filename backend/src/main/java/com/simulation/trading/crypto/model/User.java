package com.simulation.trading.crypto.model;

public class User {
    private final int userID;
    private final String userName;
    private double balance;

    public User(int userID, String userName, double balance) {
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
