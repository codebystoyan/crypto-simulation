package com.simulation.trading.crypto.web;

import com.simulation.trading.crypto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/balance/{user_id:[0-9]+}")
    public double getUserBalance(@PathVariable("user_id") int userId) {
        return userService.getUserBalance(userId);
    }

    //TODO: PUT mapping?? cuz not creating but updating
    @PostMapping("/balance/reset/{user_id:[0-9]+}")
    public void resetUserBalance(@PathVariable("user_id") int userId) {
        userService.resetBalance(userId);
    }
}
