package com.simulation.trading.crypto.web;

import com.simulation.trading.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/balance/{user_id:[0-9]+}")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable("user_id") int userId) {
        BigDecimal balance = userService.getUserBalance(userId);
        return ResponseEntity.ok(balance);
    }

    //TODO: PUT mapping?? cuz not creating but updating
    @PutMapping("/balance/reset/{user_id:[0-9]+}")
    public ResponseEntity<BigDecimal> resetUserBalance(@PathVariable("user_id") int userId) {
        BigDecimal newResetBalance = userService.resetBalance(userId);
        return ResponseEntity.ok(newResetBalance);
    }
}
