package com.simulation.trading.crypto.service;

import com.simulation.trading.crypto.exception.InsufficientFundsException;
import com.simulation.trading.crypto.exception.ResourceNotUpdatedException;
import com.simulation.trading.crypto.exception.UserNotFoundException;
import com.simulation.trading.crypto.model.Holding;
import com.simulation.trading.crypto.model.User;
import com.simulation.trading.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final HoldingsService holdingsService;

    private final BigDecimal STARTING_BALANCE;

    @Autowired
    public UserService(UserRepository userRepository, HoldingsService holdingsService,
                       @Value("${app.user.starting-balance}") double startingBalance) {
        this.userRepository = userRepository;
        this.holdingsService = holdingsService;
        this.STARTING_BALANCE = new BigDecimal(startingBalance).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getUserBalance(int userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getBalance();
    }

    @Transactional
    public void buyCrypto(int userId, BigDecimal fiatAmount, BigDecimal cryptoAmount, String symbol) {
        fiatAmount = fiatAmount.setScale(2, RoundingMode.HALF_UP);
        cryptoAmount = cryptoAmount.setScale(8, RoundingMode.HALF_UP);

        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BigDecimal currentBalance = user.getBalance();
        BigDecimal newBalance = currentBalance.subtract(fiatAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds to buy currency: " + symbol);
        }

        int rowsAffected = userRepository.updateUserBalance(userId, newBalance);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance could not be updated");
        }

        holdingsService.addToHoldings(userId, symbol, cryptoAmount);
    }

    @Transactional
    public void sellCrypto(int userId, BigDecimal fiatAmount, BigDecimal cryptoAmount, String symbol) {
        fiatAmount = fiatAmount.setScale(2, RoundingMode.HALF_UP);
        cryptoAmount = cryptoAmount.setScale(8, RoundingMode.HALF_UP);

        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Holding holding = holdingsService.getHolding(userId, symbol);

        if (cryptoAmount.compareTo(holding.getAmount()) > 0) {
            throw new InsufficientFundsException("Sell amount exceeds current holding for: " + symbol);
        }

        BigDecimal newBalance = user.getBalance().add(fiatAmount);
        int rowsAffected = userRepository.updateUserBalance(userId, newBalance);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance could not be updated");
        }

        holdingsService.subtractFromHoldings(userId, symbol, cryptoAmount);
    }

    public BigDecimal resetBalance(int userId) {
        int rowsAffected = userRepository.updateUserBalance(userId, STARTING_BALANCE);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("User balance could not be reset");
        }
        return STARTING_BALANCE;
    }
}
