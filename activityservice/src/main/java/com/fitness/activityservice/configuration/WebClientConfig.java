package com.fitness.activityservice.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


//This code is necessary for make calls HTTP to servers in this case is for USER-SERVICE
@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced //this anotation is util for use the name in the UREKA server, for example -> USER-SERVICE
    public WebClient.Builder webClientBuilder (){
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder){
        return webClientBuilder
                .baseUrl("http://USER-SERVICE")
                .build();
    }

}
