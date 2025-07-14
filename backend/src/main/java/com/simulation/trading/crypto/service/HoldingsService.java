package com.simulation.trading.crypto.service;

import com.simulation.trading.crypto.exception.HoldingNotFoundException;
import com.simulation.trading.crypto.exception.InsufficientFundsException;
import com.simulation.trading.crypto.exception.ResourceNotCreatedException;
import com.simulation.trading.crypto.exception.ResourceNotUpdatedException;
import com.simulation.trading.crypto.model.Holding;
import com.simulation.trading.crypto.repository.HoldingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class HoldingsService {
    private final HoldingsRepository holdingsRepository;

    @Autowired
    public HoldingsService(HoldingsRepository holdingsRepository) {
        this.holdingsRepository = holdingsRepository;
    }

    public List<Holding> getHoldingsForUser(int userId) {
        return holdingsRepository.getUserHoldings(userId);
    }

    public Holding getHolding(int userId, String symbol) {
        return holdingsRepository.getCryptoHolding(userId, symbol)
                .orElseThrow(() -> new HoldingNotFoundException("User has no holding for currency: " + symbol));
    }

    public void addToHoldings(int userId, String symbol, BigDecimal amount) {
        amount = amount.setScale(8, RoundingMode.HALF_UP); // crypto-level precision
        try {
            Holding holding = getHolding(userId, symbol);
            BigDecimal oldAmount = holding.getAmount();
            BigDecimal newAmount = oldAmount.add(amount);
            holding.setAmount(newAmount);
            holding.setLastUpdated(Instant.now());

            int rowsAffected = holdingsRepository.updateCryptoHolding(holding);
            if (rowsAffected == 0) {
                throw new ResourceNotUpdatedException("Holdings could not be updated for currency: " + symbol);
            }
        } catch (HoldingNotFoundException e) {
            Holding newHolding = new Holding(userId, symbol);
            newHolding.setAmount(amount);
            newHolding.setLastUpdated(Instant.now());
            int rowsAffected = holdingsRepository.insertCryptoHolding(newHolding);
            if (rowsAffected == 0) {
                throw new ResourceNotCreatedException("Holdings could not be created for currency: " + symbol);
            }
        }
    }

    public void subtractFromHoldings(int userId, String symbol, BigDecimal amount) {
        amount = amount.setScale(8, RoundingMode.HALF_UP);

        Holding holding = getHolding(userId, symbol);
        BigDecimal oldAmount = holding.getAmount();
        BigDecimal newAmount = oldAmount.subtract(amount);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient amount of holdings for currency: " + symbol);
        }

        if (newAmount.compareTo(BigDecimal.ZERO) == 0) {
            int rowsAffected = holdingsRepository.deleteCryptoHolding(userId, symbol);
            if (rowsAffected == 0) {
                throw new ResourceNotUpdatedException("Holding could not be deleted for currency: " + symbol);
            }
            return;
        }

        holding.setAmount(newAmount);
        holding.setLastUpdated(Instant.now());
        int rowsAffected = holdingsRepository.updateCryptoHolding(holding);
        if (rowsAffected == 0) {
            throw new ResourceNotUpdatedException("Holding could not be updated for currency: " + symbol);
        }
    }
}
