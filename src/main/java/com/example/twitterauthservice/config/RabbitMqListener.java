package com.example.twitterauthservice.config;

import com.example.twitterauthservice.credentials.User;
import com.example.twitterauthservice.credentials.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


@Service
public class RabbitMqListener {


    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);
    private final UserRepository userRepository;

    public RabbitMqListener(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.registerUser")
    public void receiveMessage(String json) {
        try {
            User user = objectMapper.readValue(json, User.class);
            userRepository.save(user); // Save the user to the database

        } catch (JsonProcessingException e) {
            System.out.println(e); //receive the JWT
        }
    }
}
