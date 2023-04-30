package com.example.twitterauthservice.config;

import com.example.twitterauthservice.credentials.User;
import com.example.twitterauthservice.credentials.credentialsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class RabbitMqListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);
    private final com.example.twitterauthservice.credentials.credentialsRepository credentialsRepository;

    public RabbitMqListener(credentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }


//    @RabbitListener(queues = {"q.saveUser"})
//    public void onUserRegistration(User user) {
//        log.info("User received: {}", user);
//        credentialsRepository.save(user);
//    }

    @RabbitListener(queues = "q.saveUser")
    public void receiveMessage(String json) {
        try {
            User user = new ObjectMapper().readValue(json, User.class);
            credentialsRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}
