package com.example.twitterauthservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables authUser(){
        return new Declarables(
                new FanoutExchange("x.auth-service"),
                new Queue("q.authenticate" ),
                new Queue("q.token" ),
                new Binding("q.authenticate", Binding.DestinationType.QUEUE, "x.auth-service", "authenticate", null),
                new Binding("q.token", Binding.DestinationType.QUEUE, "x.auth-service", "token", null));
    }

    @Bean
    public Queue authenticateQueue() {
        return new Queue("authenticate", true);
    }

    @Bean
    public Declarables createUser(){
        return new Declarables(
                new FanoutExchange("x.user-service"),
                new Queue("q.userRegister" ),
                new Queue("q.userUpdate" ),
                new Queue("q.saveUser" ),
                new Queue("q.registerUser" ),
                new Queue("q.token" ),
                new Binding("q.userRegister", Binding.DestinationType.QUEUE, "x.user-service", "userRegister", null),
                new Binding("q.userUpdate", Binding.DestinationType.QUEUE, "x.user-service", "userUpdate", null),
                new Binding("q.saveUser", Binding.DestinationType.QUEUE, "x.user-service", "saveUser", null),
                new Binding("q.registerUser", Binding.DestinationType.QUEUE, "x.user-service", "registerUser", null),
                new Binding("q.token", Binding.DestinationType.QUEUE, "x.user-service", "token", null));
    }

    @Bean
    public Declarables deleteUser(){
        return new Declarables(
                new FanoutExchange("x.delete.user-service"),
                new Queue("userDelete" ),
                new Binding("userDelete", Binding.DestinationType.QUEUE, "x.delete.user-service", "userDelete", null));
    }
}

