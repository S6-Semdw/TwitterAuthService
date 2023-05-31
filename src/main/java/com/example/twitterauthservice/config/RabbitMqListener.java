package com.example.twitterauthservice.config;

import com.example.twitterauthservice.credentials.User;
import com.example.twitterauthservice.credentials.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class RabbitMqListener {


    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);
    private final UserRepository userRepository;

    private final RabbitTemplate template;

    public RabbitMqListener(UserRepository userRepository, ObjectMapper objectMapper, RabbitTemplate template) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.template = template;
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

    @RabbitListener(queues = "getUser")
    public void getMessage(String email) {
        try {
            System.out.println(email); //receive the email
            template.convertAndSend("x.send-user", "sendUser", email);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
