package com.simulation.trading.crypto.web;

import com.simulation.trading.crypto.model.Transaction;
import com.simulation.trading.crypto.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/user/{id:[0-9]+}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable("id") int userId) {
        List<Transaction> transactions = transactionService.getTransactionHistory(userId);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.trade(transaction);
        return ResponseEntity.ok(saved);
    }
}
