package com.fitness.activityservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



// This is important for stablish the connection and send de messages to the queue in RabbitMQ
@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    public Queue activityQueue(){
        return new Queue(queue, true);
    }
    //Define the exchange
    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(exchange);
    }

    // Make the relation between the code and rabbitMQ
    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange){
        return  BindingBuilder
                .bind(activityQueue)
                .to(activityExchange)
                .with(routingKey);
    }

    //Transform the data to JSON (Necesary for the queue in RabbitMQ)
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
