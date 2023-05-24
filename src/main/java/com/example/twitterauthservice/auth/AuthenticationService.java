package com.example.twitterauthservice.auth;


import com.example.twitterauthservice.config.JwtService;
import com.example.twitterauthservice.credentials.Role;
import com.example.twitterauthservice.credentials.User;
import com.example.twitterauthservice.credentials.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RabbitTemplate template;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        String encodedToken = jwtToken.split("\\.")[1];

        // Decode the Base64-encoded payload
        byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
        String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

        // Print the decoded payload
        System.out.println("Decoded JWT payload: " + decodedPayload);
        template.convertAndSend("x.auth-service", "authenticate", jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
