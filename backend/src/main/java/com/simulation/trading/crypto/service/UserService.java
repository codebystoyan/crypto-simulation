package com.simulation.trading.crypto.service;

import com.simulation.trading.crypto.exception.InsufficientFundsException;
import com.simulation.trading.crypto.exception.ResourceNotUpdatedException;
import com.simulation.trading.crypto.exception.UserNotFoundException;
import com.simulation.trading.crypto.model.Holding;
import com.simulation.trading.crypto.model.User;
import com.simulation.trading.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HoldingsService holdingsService;

    @Autowired
    public UserService(UserRepository userRepository, HoldingsService holdingsService) {
        this.userRepository = userRepository;
        this.holdingsService = holdingsService;
    }

    public double getUserBalance(int userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getBalance();
    }

    //TODO: transaction??
    @Transactional
    public void buyCrypto(int userId, double fiat_amount, double amount, String symbol) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        double currentBalance = user.getBalance();
        double newBalance = currentBalance - fiat_amount;
        if (newBalance < 0) {
            throw new InsufficientFundsException("Insufficient funds to buy amount of currency " + symbol);
        }

        int rowsAffected = userRepository.updateUserBalance(userId, newBalance);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance not affected, could not be updated");
        }
        holdingsService.addToHoldings(user.getUserID(), symbol, amount);
    }

    @Transactional
    public void sellCrypto(int userId, double fiat_amount, double amount, String symbol) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Holding holding = holdingsService.getHolding(userId, symbol);

        if (amount > holding.getAmount()) {
            throw new InsufficientFundsException("Sell amount specified larger than holding amount");
        }

        double newBalance = fiat_amount + user.getBalance();
        int rowsAffected = userRepository.updateUserBalance(userId, newBalance);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance not affected, could not be updated");
        }
        holdingsService.subtractFromHoldings(user.getUserID(), symbol, amount);
    }

    public void resetBalance(int userId) {
        //TODO: maybe move to app.props?
        int userStartBalance = 10000;
        int rowsAffected = userRepository.updateUserBalance(userId, userStartBalance);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance could not be reset");
        }
    }
}
