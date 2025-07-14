package com.simulation.trading.crypto.web;

import com.simulation.trading.crypto.model.Holding;
import com.simulation.trading.crypto.service.HoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/holdings")
public class HoldingsController {
    private final HoldingsService holdingsService;

    @Autowired
    public HoldingsController(HoldingsService holdingsService) {
        this.holdingsService = holdingsService;
    }

    @GetMapping("/user/{user_id:[0-9]+}")
    public ResponseEntity<List<Holding>> getUserHoldings(@PathVariable("user_id") int userId) {
        List<Holding> holdings = holdingsService.getHoldingsForUser(userId);
        return ResponseEntity.ok(holdings);
    }
}
