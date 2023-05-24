package com.example.twitterauthservice.RabbitMQ;

import com.example.twitterauthservice.credentials.User;
import com.example.twitterauthservice.credentials.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteUserListener {


    private static final Logger log = LoggerFactory.getLogger(com.example.twitterauthservice.config.RabbitMqListener.class);
    private final UserRepository userRepository;

    public DeleteUserListener(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = "userDelete")
    public void receiveDelete(String json) {
        try {
            // Extract the email from the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);
            String email = jsonNode.get("email").asText();

            // Find the user by email
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                userRepository.delete(user.get());
                System.out.println("User deleted: " + email);
            } else {
                System.out.println("User not found with email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}