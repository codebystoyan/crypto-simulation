package com.simulation.trading.crypto.service;

import com.simulation.trading.crypto.exception.InvalidTransactionDetailsException;
import com.simulation.trading.crypto.exception.ResourceNotCreatedException;
import com.simulation.trading.crypto.model.Transaction;
import com.simulation.trading.crypto.model.TransactionType;
import com.simulation.trading.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public Transaction trade(Transaction transaction) {
        checkValidTransactionDetails(transaction);

        int userId = transaction.userId();
        BigDecimal cryptoFiatAmount = transaction.fiatAmount();
        BigDecimal amountToBuy = transaction.amount();
        String cryptoSymbol = transaction.symbol();

        switch (transaction.type()) {
            case TransactionType.BUY ->
                    userService.buyCrypto(userId, cryptoFiatAmount, amountToBuy, cryptoSymbol);
            case TransactionType.SELL ->
                    userService.sellCrypto(userId, cryptoFiatAmount, amountToBuy, cryptoSymbol);
        }

        int rowsAffected = transactionRepository.addTransaction(transaction);
        if (rowsAffected == 0) {
            throw new ResourceNotCreatedException("Transaction could not be performed");
        }

        return transaction;
    }

    public List<Transaction> getTransactionHistory(int userId) {
        return transactionRepository.getTransactionHistory(userId);
    }

    private void checkValidTransactionDetails(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionDetailsException("Transaction cannot be empty.");
        }

        if (transaction.userId() <= 0) {
            throw new InvalidTransactionDetailsException("Invalid user ID.");
        }

        BigDecimal amount = transaction.amount(); // now BigDecimal
        int arbitraryFixedCeilingOfCryptoAmount = 1_000_000;
        if (amount.compareTo(BigDecimal.ZERO) <= 0 ||
                amount.compareTo(BigDecimal.valueOf(arbitraryFixedCeilingOfCryptoAmount)) > 0) {
            throw new InvalidTransactionDetailsException("Invalid amount: must be positive and below 1,000,000.");
        }

        String cryptoSymbol = transaction.symbol();
        if (cryptoSymbol == null || !cryptoSymbol.matches("^[A-Z]{3,5}$")) {
            throw new InvalidTransactionDetailsException("Invalid cryptocurrency symbol");
        }

        Instant now = Instant.now();
        Instant timestamp = transaction.timestamp();
        if (timestamp == null || timestamp.isAfter(now)) {
            throw new InvalidTransactionDetailsException("Transaction time cannot be in the future.");
        }
        if (timestamp.isBefore(now.minus(Duration.ofHours(1)))) {
            throw new InvalidTransactionDetailsException("Transaction is too old (more than 1 hour).");
        }
    }
}
