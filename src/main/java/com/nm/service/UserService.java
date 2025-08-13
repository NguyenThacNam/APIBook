package com.nm.service;

import com.nm.entity.User;
import com.nm.repository.UserRepository;
import com.nm.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        saved.setPassword(null); 
        return saved;
    }

    public String loginAndGetToken(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        Map<String, Object> claims = Map.of(
            "id", user.getId(),
            "role", user.getRole()
        );
        return jwtService.generateToken(user.getEmail(), claims); // subject = email
    }
    
    public User getUserFromToken(String token) {
        String email = jwtService.extractEmail(token); // Dùng JWT parse token ra email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
