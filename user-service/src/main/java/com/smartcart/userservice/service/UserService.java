package com.smartcart.userservice.service;

import com.smartcart.userservice.dto.LoginDTO;
import com.smartcart.userservice.entity.User;
import com.smartcart.userservice.repository.UserRepository;
import com.smartcart.userservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public String loginUser(LoginDTO loginDTO) {
        User user = repository.findByUsername(loginDTO.getUsername());

        if (user == null) {
            return "User not found";
        }

        boolean passwordMatches = passwordEncoder.matches(
                loginDTO.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return "Invalid password";
        }

        return jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );
    }
}