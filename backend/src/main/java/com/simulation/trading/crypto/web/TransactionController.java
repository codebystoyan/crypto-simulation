package com.simulation.trading.crypto.web;

import com.simulation.trading.crypto.model.Transaction;
import com.simulation.trading.crypto.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Transaction> getUserTransactions(@PathVariable("id") int userId) {
        return transactionService.getTransactionHistory(userId);
    }

    //TODO: when catching exceptions catch DBAccessSomethingException to signal exact problem, perhaps DBdown?
    @PostMapping
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionService.trade(transaction);
    }
}
