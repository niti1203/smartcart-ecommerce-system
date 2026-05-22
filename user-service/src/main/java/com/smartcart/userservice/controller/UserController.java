package com.smartcart.userservice.controller;

import com.smartcart.userservice.dto.LoginDTO;
import com.smartcart.userservice.entity.User;
import com.smartcart.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return service.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginDTO loginDTO) {
        return service.loginUser(loginDTO);
    }

    @GetMapping("/test")
    public String testApi() {
        return "JWT Authentication Working";
    }
    @GetMapping("/profile")
    public String userProfile() {
        return "Welcome USER";
    }

}